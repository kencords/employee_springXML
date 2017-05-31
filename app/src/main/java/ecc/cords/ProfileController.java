package ecc.cords;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;	
import org.springframework.web.servlet.mvc.SimpleFormController;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ProfileController extends SimpleFormController {

	private DTO_EntityMapper mapper;
	private EmployeeManager empManager;

	public ProfileController() {
		setCommandClass(EmployeeDTO.class);
	}

	public void setEmployeeManager(EmployeeManager empManager) {
		this.empManager = empManager;
	}

	public void setMapper(DTO_EntityMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	protected ModelAndView showForm(HttpServletRequest req, HttpServletResponse res, BindException errors) {
		EmployeeDTO employee = new EmployeeDTO();
		try {
			employee = loadEmployee(req, res);
			req.setAttribute("bDate", Utils.formatDate(employee.getBirthDate()));
			req.setAttribute("hDate", Utils.formatDate(employee.getHireDate()));
			req.setAttribute("curHired", employee.isCurrentlyHired() ? "YES" : "NO");
		} catch(Exception ex) {	
			return null;
		}
		ModelAndView mav = new ModelAndView(getFormView());
		mav.addObject("employee", employee);
		return mav;
	}

	private EmployeeDTO loadEmployee(HttpServletRequest req, HttpServletResponse res) throws Exception {
		EmployeeDTO employee = new EmployeeDTO();
		String empId = req.getParameter("empId");
		HttpSession session = req.getSession();
		if(session.getAttribute("employee") == null || !empId.equals(((EmployeeDTO)session.getAttribute("employee")).getEmpId() + "")) {
			try {
				employee = mapper.mapToEmployeeDTO(empManager.getEmployee(Integer.parseInt(req.getParameter("empId"))));
				session.setAttribute("employee", employee);
				return employee;
			} catch(Exception ex) {
				ex.printStackTrace();
				res.sendError(404,"Employee not found!");
				return employee;
			}
		}
		return (EmployeeDTO)session.getAttribute("employee");
	}
}