package ecc.cords.impl;

import java.util.HashSet;
import java.util.List;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.commons.lang3.StringUtils;

import ecc.cords.*;

public class FormValidatorImpl implements FormValidator{

	private String lName;
	private String fName;
	private String mName;
	private String title;
	private String suffix;
	private String bDate;
	private String gwa;
	private String strNo;
	private String street;
	private String brgy;
	private String city;
	private String zipcode;
	private String curHired;
	private String hDate;
	private boolean isAccepted;
	private EmployeeManager empManager;

	public void setEmployeeManager(EmployeeManager empManager) {
		this.empManager = empManager;
	}

	public void saveEmployeeIfValid(List<LogMsg> logMsgs, List<ContactDTO> contacts, List<RoleDTO> roles,
	HttpServletRequest req, HttpServletResponse res, boolean isEdit) 
	throws IOException, ServletException {
		isAccepted = true;
		lName = req.getParameter("lastName");
		fName = req.getParameter("firstName");
		mName = req.getParameter("middleName");
		title = req.getParameter("title");
		suffix = req.getParameter("suffix");
		bDate = req.getParameter("bDate");
		gwa = req.getParameter("gwa");
		strNo = req.getParameter("strNo");
		street = req.getParameter("street");
		brgy = req.getParameter("brgy");
		city = req.getParameter("city");
		zipcode = req.getParameter("zipcode");
		curHired = req.getParameter("currentlyHired");
		hDate = req.getParameter("hDate");
		isAccepted = validateEmployeeForm(logMsgs, contacts.size());
		if(isAccepted) {
			try {
				saveEmployee((EmployeeDTO) req.getSession().getAttribute("employee"), logMsgs, contacts, roles, isEdit);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private void saveEmployee(EmployeeDTO employee, List<LogMsg> logMsgs, List<ContactDTO> contacts, List<RoleDTO> roles, boolean isEdit) throws Exception {
		if(!isEdit) { 
			employee = new EmployeeDTO();
		}
		employee.setLastName(lName);
		employee.setFirstName(fName);
		employee.setMiddleName(mName);
		employee.setSuffix(suffix);
		employee.setTitle(title);
		employee.setBirthDate(Utils.convertToDate(bDate));
		employee.setGwa(Float.parseFloat(gwa));
		employee.setAddress(fillAddress());		
		employee.setContacts(new HashSet<ContactDTO>(contacts));
		employee.setCurrentlyHired(curHired.equals("YES"));
		employee.setHireDate(Utils.convertToDate(hDate));
		employee.setRoles(new HashSet<RoleDTO>(roles));
		if(!isEdit){
			logMsgs.add(empManager.addEmployee(employee));
		}
		if(isEdit) {
			logMsgs.add(empManager.updateEmployee(employee));
			System.out.println("save: " + employee.getContacts());
		}
	}

	private AddressDTO fillAddress() {
		AddressDTO address = new AddressDTO();
		address.setStreetNo(Integer.parseInt(strNo));
		address.setStreet(street);
		address.setBrgy(brgy);
		address.setCity(city);
		address.setZipcode(zipcode);
		return address;
	}

	private boolean validateEmployeeForm(List<LogMsg> logMsgs, int contactSize) {
		boolean isValid = StringUtils.isNotBlank(lName) &&
		StringUtils.isNotBlank(fName) &&
		StringUtils.isNotBlank(mName) &&
		StringUtils.isNotBlank(bDate) &&
		StringUtils.isNotBlank(gwa) &&
		StringUtils.isNotBlank(strNo) &&
		StringUtils.isNotBlank(street) &&
		StringUtils.isNotBlank(brgy) &&
		StringUtils.isNotBlank(city) &&
		StringUtils.isNotBlank(zipcode) &&
		StringUtils.isNotBlank(hDate);

		if(!isValid) {
			logMsgs.add(new LogMsg("Please fill up fields marked with *", "red"));
		}
		if(contactSize == 0) {
			isValid = false;
			logMsgs.add(new LogMsg("Employee must have atleast one contact!", "red"));
		}
		if(StringUtils.isNotBlank(bDate) && !Utils.isValidDate(bDate)){
			isValid = false;
			logMsgs.add(new LogMsg("Invalid Birthdate!", "red"));
		}
		if(StringUtils.isNotBlank(hDate) && !Utils.isValidDate(hDate)){
			isValid = false;
			logMsgs.add(new LogMsg("Invalid Hire Date!", "red"));
		}
		return isValid;
	}
}