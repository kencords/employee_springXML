package ecc.cords;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;	
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class EditEmployeeController extends SimpleFormController {
	
	private DTO_EntityMapper mapper;
	private EmployeeManager empManager;
	private FormValidator validator;
	private List<LogMsg> logMsgs = new ArrayList<>();

	public EditEmployeeController() {
		setCommandClass(EmployeeDTO.class);
	}

	public void setEmployeeManager(EmployeeManager empManager) {
		this.empManager = empManager;
	}

	public void setMapper(DTO_EntityMapper mapper) {
		this.mapper = mapper;
	}

	public void setFormValidator(FormValidator validator) {
		this.validator = validator;
	}

	@Override
	protected ModelAndView showForm(HttpServletRequest req, HttpServletResponse res, BindException errors) {
		EmployeeDTO employee = loadEmployee(req);
		List<ContactDTO> contacts = new ArrayList<>(employee.getContacts());
		List<RoleDTO> roles = new ArrayList<>(employee.getRoles());
		ModelAndView mav = new ModelAndView(getFormView());
		valueFiller(req, employee);
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
		EmployeeDTO employee = loadEmployee(req);
		List<ContactDTO> contacts = new ArrayList<>(employee.getContacts());
		List<RoleDTO> roles = new ArrayList<>(employee.getRoles());
		ModelAndView mav = new ModelAndView(getFormView());

		if(req.getParameter("saveEmployeeBtn") != null) {
			try {
				validator.saveEmployeeIfValid(logMsgs, contacts, roles, req, res, true);
			} catch(Exception ex) {
				ex.printStackTrace();
				logMsgs.add(new LogMsg("Problem occured in saving employee", "red"));
			}
		}
		if(req.getParameter("backBtn") != null) {;
			logMsgs.clear();
			req.getSession().invalidate();
			return new ModelAndView("redirect:/employeeProfile?empId=" +  employee.getEmpId());
		}
		if(req.getParameter("addContactBtn") != null) {
			processAddContact(req.getParameter("conOpt"), req.getParameter("contact"), employee);
		}
		if(req.getParameter("delConBtn") != null) {
			processDeleteContact(employee, contacts, Integer.parseInt(req.getParameter("delConBtn")));
		}
		if(req.getParameter("updateConBtn") != null) {
			int index = Integer.parseInt(req.getParameter("updateConBtn"));
			contacts.get(index).setContactValue(req.getParameter("contact"+index));
		}
		if(req.getParameter("addRoleBtn") != null){
			processAddRole(employee, Integer.parseInt(req.getParameter("roleOpt")));
		}
		if(req.getParameter("delRoleBtn") != null) {
			empManager.deleteEmployeeRole(employee, roles.get(Integer.parseInt(req.getParameter("delRoleBtn"))));		
		}

		valueFiller(req, employee);
		req.setAttribute("logMsgs", Utils.sortLogMsgs(logMsgs));
		logMsgs.clear();
		req.setAttribute("contacts", new ArrayList<>(employee.getContacts()));
		req.setAttribute("roles", new ArrayList<>(employee.getRoles()));
		req.setAttribute("availRoles", getAvailRoles(roles));
		mav.addObject("employee", employee);
		return mav;
	}

	private EmployeeDTO loadEmployee(HttpServletRequest req) {
		HttpSession session = req.getSession();
		EmployeeDTO employee = new EmployeeDTO();
		try {
			if(session.getAttribute("employee") == null) {
				return employee;
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		employee = (EmployeeDTO) session.getAttribute("employee");
		return employee;
	}

	private List<RoleDTO> getAvailRoles(List<RoleDTO> roles) {
		return mapper.getAllRoles().stream()
								   .filter(role -> !roles.contains(role))
								   .sorted((role1,role2) -> Long.compare(role1.getRoleId(), role2.getRoleId()))
								   .collect(Collectors.toList());
	}

	private void processAddContact(String contactType, String contactValue, EmployeeDTO employee) {
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
		employee.getContacts().add(new ContactDTO((contactType.equals(Utils.contactOptions[0])? "Landline" : 
		(contactType.equals(Utils.contactOptions[1])? "Mobile" : "Email"))  , contactValue));
	}

	private void processAddRole(EmployeeDTO employee, int role_id) {
		try {
			employee = empManager.addEmployeeRole(employee, role_id);
		} catch(Exception ex) {
			ex.printStackTrace();
			logMsgs.add(new LogMsg("Cannot add role!", "red"));
		}
	}

	private void processDeleteContact(EmployeeDTO employee, List<ContactDTO> contacts, int index) {
		if(contacts.size() == 1) {
			logMsgs.add(new LogMsg("Employee must have atleast one Contact!", "red"));
			return;
		}	
		try {
			System.out.println("index :" + index + " contact value: " + contacts.get(index).getContactValue());
			empManager.deleteContact(employee, contacts.get(index));
		} catch(Exception ex) {
			ex.printStackTrace();
			logMsgs.add(new LogMsg("Cannot delete contact!", "red"));
		}
	}

	private void valueFiller(HttpServletRequest req, EmployeeDTO employee) {
		try {
			req.setAttribute("title", req.getParameter("title")==null? employee.getTitle() : req.getParameter("title"));
			req.setAttribute("lastName", req.getParameter("lastName")==null? employee.getLastName() : req.getParameter("lastName"));
			req.setAttribute("firstName", req.getParameter("firstName")==null? employee.getFirstName() : req.getParameter("firstName"));
			req.setAttribute("middleName", req.getParameter("middleName")==null? employee.getMiddleName() : req.getParameter("middleName"));
			req.setAttribute("suffix", req.getParameter("suffix")==null? employee.getSuffix() : req.getParameter("suffix"));
			req.setAttribute("gwa", req.getParameter("gwa")==null? employee.getGwa() : req.getParameter("gwa"));
			req.setAttribute("strNo", req.getParameter("strNo")==null? employee.getAddress().getStreetNo() + "": req.getParameter("strNo"));
			req.setAttribute("street", req.getParameter("street")==null? employee.getAddress().getStreet() : req.getParameter("street"));
			req.setAttribute("brgy", req.getParameter("brgy")==null? employee.getAddress().getBrgy() : req.getParameter("brgy"));
			req.setAttribute("city", req.getParameter("city")==null? employee.getAddress().getCity() : req.getParameter("city"));
			req.setAttribute("zipcode", req.getParameter("zipcode")==null? employee.getAddress().getZipcode() : req.getParameter("zipcode"));
			req.setAttribute("bDate", req.getParameter("bDate")==null? Utils.formatDateSimplified(employee.getBirthDate()): req.getParameter("bDate"));
			req.setAttribute("hDate", req.getParameter("hDate")==null? Utils.formatDateSimplified(employee.getHireDate()): req.getParameter("hDate"));
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
