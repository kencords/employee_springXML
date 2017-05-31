package ecc.cords;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.io.File;
import java.io.FileOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class UploadFileController extends SimpleFormController {

	private final Logger logger = LoggerFactory.getLogger(UploadFileController.class);

	private EmployeeManager empManager;
	private List<LogMsg> logMsgs = new ArrayList<>();

	public void setEmployeeManager(EmployeeManager empManager) {
		this.empManager = empManager;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest req, 
									HttpServletResponse res,
									Object command, BindException errors) {
		logger.info("called onSubmit()");
		EmployeeFile empfile = (EmployeeFile)command;
		MultipartFile file = empfile.getFile();
		System.out.println(file);
		EmployeeDTO employee;

		if(file != null) {
			try {
				employee = parseFile(file);
				empManager.addEmployee(employee);
				logMsgs.add(new LogMsg("Successfully added Employee using File!", "green"));
			} catch(Exception ex) {
				ex.printStackTrace();
				logMsgs.add(new LogMsg("Invalid File Content!", "red"));
			}
		}
		ModelAndView mav = new ModelAndView(getFormView());
		req.setAttribute("logMsgs", Utils.sortLogMsgs(logMsgs));
		logMsgs.clear();
		return mav;
	}

	private EmployeeDTO parseFile(MultipartFile file) throws Exception{
		logger.info("Parsing file " + file.getOriginalFilename());
		String[] lines = null;
		try {
			lines = IOUtils.toString(file.getInputStream(), "UTF-8").split("\n");
		} catch(Exception ex){
			ex.printStackTrace();
		}
		EmployeeDTO employee = new EmployeeDTO();
		AddressDTO address = new AddressDTO();
		Set<ContactDTO> contacts = new HashSet<>();
		Set<RoleDTO> roles = new HashSet<>();

		for(String curline : lines) {
			String[] line = curline.split(":");

			if(line[0].toLowerCase().equals("title")) {
				employee.setTitle(line[1].trim());
			}
			if(line[0].toLowerCase().equals("last name")) {
				employee.setLastName(line[1].trim());
			}
			if(line[0].toLowerCase().equals("first name")) {
				employee.setFirstName(line[1].trim());
			}
			if(line[0].toLowerCase().equals("middle name")) {
				employee.setMiddleName(line[1].trim());
			}
			if(line[0].toLowerCase().equals("suffix")) {
				employee.setSuffix(line[1].trim());
			}
			if(line[0].toLowerCase().equals("gwa")) {
				employee.setGwa(Float.parseFloat(line[1].trim()));
			}
			if(line[0].toLowerCase().equals("birth date")) {
				employee.setBirthDate(Utils.convertToDate(line[1].trim()));
			}
			if(line[0].toLowerCase().equals("hire date")) {
				employee.setHireDate(Utils.convertToDate(line[1].trim()));
			}
			if(line[0].toLowerCase().equals("currently hired")) {
				employee.setCurrentlyHired(line[1].trim().toLowerCase().equals("yes"));
			}
			if(line[0].toLowerCase().equals("street number")) {
				address.setStreetNo(Integer.parseInt(line[1].trim()));
			}
			if(line[0].toLowerCase().equals("street")) {
				address.setStreet(line[1].trim());
			}
			if(line[0].toLowerCase().equals("barangay")) {
				address.setBrgy(line[1].trim());
			}
			if(line[0].toLowerCase().equals("city")) {
				address.setCity(line[1].trim());
			}
			if(line[0].toLowerCase().equals("zipcode")) {
				address.setZipcode(line[1].trim());
			}
			if(line[0].toLowerCase().equals("landline")) {
				if(!Utils.isValidLandline(line[1].trim())) {
					throw new Exception("Invalid landline");
				}
				contacts.add(new ContactDTO("Landline", line[1].trim()));
			}
			if(line[0].toLowerCase().equals("mobile")) {
				if(!Utils.isValidMobile(line[1].trim())) {
					throw new Exception("Invalid mobile");
				}
				contacts.add(new ContactDTO("Mobile", line[1].trim()));
			}
			if(line[0].toLowerCase().equals("email")) {
				if(!Utils.isValidEmail(line[1].trim())) {
					throw new Exception("Invalid email");
				}
				contacts.add(new ContactDTO("Email", line[1].trim()));
			}
			if(line[0].toLowerCase().equals("role id")) {
				try {
					roles.add(empManager.getRole(Integer.parseInt(line[1].trim())));
				} catch(Exception ex) {
					throw new Exception("Role not found");
				}
			}
		}

		if(roles != null) {
			employee.setRoles(roles);
		}
		if(contacts != null) {
			employee.setContacts(contacts);
		}
		if(address != null) {
			employee.setAddress(address);
		}
		return employee;
	}
}