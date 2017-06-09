package ecc.cords.impl;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;	
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import ecc.cords.*;

public class EmployeeControllerImpl extends MultiActionController implements EmployeeController {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeControllerImpl.class);

	private EmployeeManager empManager;
	private FormValidator validator;
	private List<LogMsg> logMsgs = new ArrayList<>();
	private final String addView = "employeeForm";
	private final String editView = "editEmployee";
	private final String profileView = "profile";

	public void setEmployeeManager(EmployeeManager empManager) {
		this.empManager = empManager;
	}

	public void setFormValidator(FormValidator validator) {
		this.validator = validator;
	}

	public ModelAndView addPage(HttpServletRequest req, HttpServletResponse res) throws Exception{
		logger.info("Called addPage()");
		EmployeeDTO employee = createEmployee(req);
		List<ContactDTO> contacts = new ArrayList<>(employee.getContacts());
		List<RoleDTO> roles = new ArrayList<>(employee.getRoles());
		ModelAndView mav = new ModelAndView(addView);
		req.setAttribute("contacts", contacts);
		req.setAttribute("roles", roles);
		req.setAttribute("availRoles", getAvailRoles(roles));
		mav.addObject("employee", employee);
		return mav;
	}

	public ModelAndView editPage(HttpServletRequest req, HttpServletResponse res) throws Exception {
		logger.info("Called editPage()");
		EmployeeDTO employee = loadEmployee(req);
		List<ContactDTO> contacts = new ArrayList<>(employee.getContacts());
		List<RoleDTO> roles = new ArrayList<>(employee.getRoles());
		ModelAndView mav = new ModelAndView(editView);
		valueFiller(req, employee);
		req.setAttribute("contacts", contacts);
		req.setAttribute("roles", roles);
		req.setAttribute("availRoles", getAvailRoles(roles));
		mav.addObject("employee", employee);
		return mav;
	}

	public ModelAndView viewPage(HttpServletRequest req, HttpServletResponse res) throws Exception {
		logger.info("Called viewPage()");
		EmployeeDTO employee = new EmployeeDTO();
		try {
			employee = loadEmployee(req, res);
			req.setAttribute("bDate", Utils.formatDate(employee.getBirthDate()));
			req.setAttribute("hDate", Utils.formatDate(employee.getHireDate()));
			req.setAttribute("curHired", employee.isCurrentlyHired() ? "YES" : "NO");
		} catch(Exception ex) {	
			return null;
		}
		ModelAndView mav = new ModelAndView(profileView);
		mav.addObject("employee", employee);
		return mav;
	}

	public ModelAndView addOnSubmit(HttpServletRequest req, HttpServletResponse res) throws Exception {
		logger.info("Called addOnSubmit()");
		EmployeeDTO employee = createEmployee(req);
		List<ContactDTO> contacts = new ArrayList<>(employee.getContacts());
		List<RoleDTO> roles = new ArrayList<>(employee.getRoles());
		ModelAndView mav = new ModelAndView(addView);
		if(req.getParameter("saveEmployeeBtn") != null) {
			try{
				validator.saveEmployeeIfValid(logMsgs, contacts, roles, req, res, false);
			} catch(Exception ex) {
				logMsgs.add(new LogMsg("Problem occured in adding employee", "red"));
			}
		}
		if(req.getParameter("addContactBtn") != null) {
			processAddContact(contacts, req.getParameter("conOpt"), req.getParameter("contact"));
		}
		if(req.getParameter("addRoleBtn") != null){
			processAddRole(roles, Integer.parseInt(req.getParameter("roleOpt")));
		}
		if(req.getParameter("delConBtn") != null) {
			processDeleteContact(contacts, Integer.parseInt(req.getParameter("delConBtn")));
		}
		if(req.getParameter("delRoleBtn") != null) {
			roles.remove(Integer.parseInt(req.getParameter("delRoleBtn")));
		}
		employee.setContacts(new HashSet<>(contacts));
		employee.setRoles(new HashSet<>(roles));
		req.setAttribute("logMsgs", Utils.sortLogMsgs(logMsgs));
		logMsgs.clear();
		req.setAttribute("page", "add");
		req.setAttribute("contacts", contacts);
		req.setAttribute("roles", roles);
		req.setAttribute("availRoles", getAvailRoles(roles));
		mav.addObject("employee", employee);
		return mav;
	}

	public ModelAndView editOnSubmit(HttpServletRequest req, HttpServletResponse res) throws Exception {
		logger.info("Called editOnSubmit()");
		EmployeeDTO employee = loadEmployee(req);
		List<ContactDTO> contacts = new ArrayList<>(employee.getContacts());
		List<RoleDTO> roles = new ArrayList<>(employee.getRoles());
		ModelAndView mav = new ModelAndView(editView);
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
			return new ModelAndView("redirect:/employee/viewPage?empId=" +  employee.getEmpId());
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
		roles = new ArrayList<>(employee.getRoles());
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

	private EmployeeDTO loadEmployee(HttpServletRequest req, HttpServletResponse res) throws Exception {
		EmployeeDTO employee = new EmployeeDTO();
		String empId = req.getParameter("empId");
		HttpSession session = req.getSession();
		if(session.getAttribute("employee") == null || !empId.equals(((EmployeeDTO)session.getAttribute("employee")).getEmpId() + "")) {
			try {
				employee = empManager.getEmployeeDTO(Integer.parseInt(req.getParameter("empId")));
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

	private List<RoleDTO> getAvailRoles(List<RoleDTO> roles) {
		return empManager.getAllRoles().stream()
								   .filter(role -> !roles.contains(role))
								   .sorted((role1,role2) -> Long.compare(role1.getRoleId(), role2.getRoleId()))
								   .collect(Collectors.toList());
	}

	private void processAddContact(List<ContactDTO> contacts, String contactType, String contactValue) {
		if(checkValidContact(contactType, contactValue)) {
			contacts.add(new ContactDTO((contactType.equals(Utils.contactOptions[0])? "Landline" : 
			(contactType.equals(Utils.contactOptions[1])? "Mobile" : "Email"))  , contactValue));
		}
	}

	private void processAddContact(String contactType, String contactValue, EmployeeDTO employee) {
		if(checkValidContact(contactType, contactValue)) {
			employee.getContacts().add(new ContactDTO((contactType.equals(Utils.contactOptions[0])? "Landline" : 
			(contactType.equals(Utils.contactOptions[1])? "Mobile" : "Email"))  , contactValue));
		}
	}

	private boolean checkValidContact(String contactType, String contactValue) {
		if(contactType.equals(Utils.contactOptions[0]) && !Utils.isValidLandline(contactValue)) {
			logMsgs.add(new LogMsg("Invalid Landline!", "red"));
			return false;
		}
		else if(contactType.equals(Utils.contactOptions[1]) && !Utils.isValidMobile(contactValue)) {
			logMsgs.add(new LogMsg("Invalid Mobile!", "red"));
			return false;
		}
		else if(contactType.equals(Utils.contactOptions[2]) && !Utils.isValidEmail(contactValue)) {
			logMsgs.add(new LogMsg("Invalid Email!", "red"));
			return false;
		}
		return true;
	}

	private void processAddRole(List<RoleDTO> roles, int roleId) {
		RoleDTO role = new RoleDTO();
		try {
			role = empManager.getRole(roleId);
		} catch(Exception ex) {
			logMsgs.add(new LogMsg(empManager.getLogMsg(), "red"));
		}
		roles.add(role);
	}

	private void processAddRole(EmployeeDTO employee, int roleId) {
		try {
			employee = empManager.addEmployeeRole(employee, roleId);
		} catch(Exception ex) {
			ex.printStackTrace();
			logMsgs.add(new LogMsg("Cannot add role!", "red"));
		}
	}

	private void processDeleteContact(List<ContactDTO> contacts, int index) {
		if(contacts.size()==1) {
			logMsgs.add(new LogMsg("Employee must have atleast one Contact!", "red"));
			return;
		}
		contacts.remove(index);
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