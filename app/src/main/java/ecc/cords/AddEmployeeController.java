package ecc.cords;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;	
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class AddEmployeeController extends SimpleFormController {
	
	private static DTO_EntityMapper mapper = new DTO_EntityMapper();

	private List<LogMsg> logMsgs = new ArrayList<>();

	public AddEmployeeController() {
		setCommandClass(EmployeeDTO.class);
	}

	@Override
	protected ModelAndView showForm(HttpServletRequest req, HttpServletResponse res, BindException errors) {
		EmployeeDTO employee = createEmployee(req);
		List<ContactDTO> contacts = new ArrayList<>(employee.getContacts());
		List<RoleDTO> roles = new ArrayList<>(employee.getRoles());
		ModelAndView mav = new ModelAndView(getFormView());
		req.setAttribute("page", "add");
		req.setAttribute("contacts", contacts);
		req.setAttribute("roles", roles);
		req.setAttribute("availRoles", getAvailRoles(roles));
		mav.addObject("employee", employee);
		return mav;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest req, 
									HttpServletResponse res,
									Object command, BindException errors) {
		EmployeeDTO employee = createEmployee(req);
		List<ContactDTO> contacts = new ArrayList<>(employee.getContacts());
		List<RoleDTO> roles = new ArrayList<>(employee.getRoles());
		ModelAndView mav = new ModelAndView(getFormView());

		if(req.getParameter("addContactBtn") != null) {
			System.out.println("wtf");
			processAddContact(contacts, req.getParameter("conOpt"), req.getParameter("contact"));
		}

		req.setAttribute("logMsgs", Utils.sortLogMsgs(logMsgs));
		logMsgs.clear();
		
		employee.setContacts(new HashSet<>(contacts));
		employee.setRoles(new HashSet<>(roles));

		req.setAttribute("page", "add");
		req.setAttribute("contacts", contacts);
		req.setAttribute("roles", roles);
		req.setAttribute("availRoles", getAvailRoles(roles));
		mav.addObject("employee", employee);
		return mav;
	}

	private EmployeeDTO createEmployee(HttpServletRequest req) {
		EmployeeDTO employee = new EmployeeDTO();
		try {
			if(req.getSession().getAttribute("newEmp") == null) {
				employee.setContacts(new HashSet<ContactDTO>());
				employee.setRoles(new HashSet<RoleDTO>());
				req.getSession().setAttribute("newEmp", employee);
				return employee;
			}
			employee = (EmployeeDTO) req.getSession().getAttribute("newEmp");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return employee;
	}

	private List<RoleDTO> getAvailRoles(List<RoleDTO> roles) {
		return mapper.getAllRoles().stream()
								   .filter(role -> !roles.contains(role))
								   .sorted((role1,role2) -> Long.compare(role1.getRoleId(), role2.getRoleId()))
								   .collect(Collectors.toList());
	}

	private void processAddContact(List<ContactDTO> contacts, String contactType, String contactValue) {
		if(contactType.equals(Utils.contactOptions[0]) && !Utils.isValidLandline(contactValue)) {
			logMsgs.add(new LogMsg("Invalid Landline!", "red"));
			return;
		}
		else if(contactType.equals(Utils.contactOptions[1]) && !Utils.isValidMobile(contactValue)) {
			logMsgs.add(new LogMsg("Invalid Mobile!", "red"));
			return;
		}
		else if(contactType.equals(Utils.contactOptions[2]) && !Utils.isValidEmail(contactValue)) {
			logMsgs.add(new LogMsg("Invalid Email!", "red"));
			return;
		}
		contacts.add(new ContactDTO((contactType.equals(Utils.contactOptions[0])? "Landline" : 
		(contactType.equals(Utils.contactOptions[1])? "Mobile" : "Email"))  , contactValue));
	}
}