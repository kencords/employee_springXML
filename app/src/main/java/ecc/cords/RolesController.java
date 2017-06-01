package ecc.cords;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;	
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RolesController extends SimpleFormController {

	private DTO_EntityMapper mapper;
	private EmployeeManager empManager;
	private List<LogMsg> logMsgs = new ArrayList<>();

	public RolesController() {
		setCommandClass(RoleDTO.class);
	}

	public void setEmployeeManager(EmployeeManager empManager) {
		this.empManager = empManager;
	}

	public void setMapper(DTO_EntityMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	protected ModelAndView showForm(HttpServletRequest req, HttpServletResponse res, BindException errors) {
		if(req.getSession(false) != null) {
			req.getSession().invalidate();
		}
		ModelAndView mav = new ModelAndView(getFormView());
		mav.addObject("roleList", getRoles());
		return mav;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest req, 
									HttpServletResponse res,
									Object command, BindException errors) {
		ModelAndView mav = new ModelAndView(getFormView());
		if(req.getParameter("addNowBtn") != null) {
			processAddRole(req.getParameter("role_name").trim());
		}
		if(req.getParameter("addRoleBtn") != null) {
			req.setAttribute("addRoleField", "true");
		}
		if(req.getParameter("cancel") != null) {
			logMsgs.add(new LogMsg(req.getParameter("cancel"), "red"));
		}
		if(req.getParameter("delRoleBtn") != null) {
			processDeleteRole(Integer.parseInt(req.getParameter("delRoleBtn")));
		}
		if(req.getParameter("editNowBtn") != null) {
			processEditRole(Integer.parseInt(req.getParameter("editNowBtn")), req.getParameter("role_name_ed"));
		}
		if(req.getParameter("editRoleBtn") != null) {
			req.setAttribute("role", getRole(Integer.parseInt(req.getParameter("editRoleBtn"))));
		}
		if(req.getParameter("showOwnerBtn") != null) {
			req.setAttribute("role_OV", getRole(Integer.parseInt(req.getParameter("showOwnerBtn"))));
		}

		req.setAttribute("logMsgs", Utils.sortLogMsgs(logMsgs));
		logMsgs.clear();
		mav.addObject("roleList", getRoles());
		return mav;
	}

	private List<RoleDTO> getRoles() {
		return mapper.getAllRoles().stream()
								   .sorted((role1,role2) -> Long.compare(role1.getRoleId(), role2.getRoleId()))
					 			   .collect(Collectors.toList());
	}

	private RoleDTO getRole(int roleId) {
		RoleDTO role = new RoleDTO();
		try {
			role = empManager.getRole(roleId);
		} catch(Exception ex) {
			logMsgs.add(new LogMsg(empManager.getLogMsg(), "red"));
		}
		return role;
	}

	private String getRoleOwners(RoleDTO role) {
		StringBuilder sb = new StringBuilder();
		role.getEmployees().stream()
						   .sorted((emp1,emp2) -> Long.compare(emp1.getEmpId(), emp2.getEmpId()))
		                   .forEach(employee -> sb.append("<br>" + employee));
		return sb.toString();
	}

	private void processAddRole(String roleName) {
		if(roleName.equals("")) {
			logMsgs.add(new LogMsg("Role Name must not be Empty!", "red"));
			return;
		}
		logMsgs.add(empManager.createRole(roleName.toUpperCase()));
	}

	private void processDeleteRole(int roleId) {
		RoleDTO role = new RoleDTO();
		try {
			role = getRole(roleId);
			logMsgs.add(new LogMsg(empManager.deleteRole(role), "green"));
		} catch(Exception ex) {
			logMsgs.add(new LogMsg(empManager.getLogMsg(), "red"));
			logMsgs.add(new LogMsg("Role is currently used by: \n" + getRoleOwners(role), "red"));
		}
	}

	private void processEditRole(int roleId, String roleName) {
		if(roleName.equals("")) {
			logMsgs.add(new LogMsg("Role Name must not be Empty!", "red"));
			return;
		}
		RoleDTO role = new RoleDTO();
		try {
			role = empManager.getRole(roleId);
			logMsgs.add(empManager.updateRole(role, roleName.toUpperCase()));
		} catch(Exception ex) {
			logMsgs.add(new LogMsg(empManager.getLogMsg(), "red"));
			logMsgs.add(new LogMsg("Role is currently used by: \n" + getRoleOwners(role), "red"));
		}
	}

}