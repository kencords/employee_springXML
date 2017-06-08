package ecc.cords.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ecc.cords.*;

public class EmployeeManagerImpl implements EmployeeManager {

	private DaoService daoService;
	private DTOEntityMapper mapper;
	private String logMsg = "";

	public void setDaoService(DaoService daoService) {
		this.daoService = daoService;
	}

	public void setMapper(DTOEntityMapper mapper) {
		this.mapper = mapper;
	}

	public LogMsg addEmployee(EmployeeDTO employeeDTO) {
		Employee employee = mapper.mapToEmployee(employeeDTO, true);
		try {
			daoService.saveElement(employee);
		} catch(Exception exception) {
			exception.printStackTrace();
			return new LogMsg("Employee Creation Failed!", "red");
		}
		return new LogMsg("Employee Creation Successful!", "green");
	}

	public LogMsg updateEmployee(EmployeeDTO employeeDTO) {
		Employee employee = mapper.mapToEmployee(employeeDTO, false);
		try {
			daoService.updateElement(employee);
		} catch(Exception exception) {
			exception.printStackTrace();
			return new LogMsg("Employee Update Failed!", "red");
		}
		return new LogMsg("Employee Updated Successfully!", "green");
	}

	public LogMsg deleteEmployee(int empId) {
		try {
			Employee employee = getEmployee(empId);
			for(Role role : employee.getRoles()) {
				daoService.evictCollection("ecc.cords.Role.employees", role.getRoleId());
			}
			daoService.deleteElement(employee);
		} catch(Exception ex) {
			System.out.println("EmpID: " + empId);
			ex.printStackTrace();
			return new LogMsg("Cannot delete Employee " + empId + "!", "red");
		}
		return new LogMsg("Deleted Employee " + empId + "!", "green");
	}
	
	public EmployeeDTO addContact(EmployeeDTO employee, Set<ContactDTO> contacts) {
		contacts.forEach(contact -> { 
			contact.setEmployee(employee);
			employee.getContacts().add(contact);
		});
		return employee;
	}

	public void updateContact(EmployeeDTO employee, Set<ContactDTO> contacts) {
		employee.getContacts().clear();
		employee.setContacts(contacts);
	}

	public void deleteContact(EmployeeDTO employee, ContactDTO contact) throws Exception {
		Set<ContactDTO> contacts = employee.getContacts();
		contacts.remove(contact);
		employee.setContacts(contacts);
	}
													
	public Employee getEmployee(int id) {
		Employee employee = new Employee();
		try {
			employee = daoService.getElement(Long.valueOf(id), Employee.class);
			employee.getEmpId();
			return employee;
		} catch(Exception exception) {
			logMsg = "Employee not found!";
			throw exception;
		}
	}

	public EmployeeDTO getEmployeeDTO(int id) throws Exception {
		return mapper.mapToEmployeeDTO(getEmployee(id));
	}

	public EmployeeDTO addEmployeeRole(EmployeeDTO employee, int role_id) throws Exception {
		try {
			Set<RoleDTO> roles = employee.getRoles();
			Role role = daoService.getElement(Long.valueOf(role_id), Role.class);
			roles.add(mapper.mapToRoleDTO(role));
			System.out.println(role.getRoleName());
			employee.setRoles(roles);
			return employee;
		} catch(Exception exception) {
			logMsg = "Invalid role!";
			throw new Exception("",exception);
		}
	}

	public EmployeeDTO deleteEmployeeRole(EmployeeDTO employee, RoleDTO role) {
		Set<RoleDTO> roles = employee.getRoles();
		roles.remove(role);
		employee.setRoles(roles);
		return employee;
	}

	public LogMsg createRole(String role_str) {
		Role role = new Role(role_str);
		try {
			daoService.getElement(role);
			return new LogMsg("Role " + role_str + " already exists!", "red");
		} catch(Exception exception) {
			daoService.saveElement(role);
			return new LogMsg("Successfully created " + role_str + " Role!", "green");
		}
	}

	public String deleteRole(RoleDTO roleDTO) throws Exception {
		Role role = mapper.mapToRole(roleDTO);
		try {
			daoService.deleteElement(role);
			return "Successfully deleted role " + role.getRoleName() + "!";
		} catch(Exception e) {
			logMsg = "Role " + role.getRoleName() + " cannot be deleted!";
			throw new Exception();
		}
	}

	public LogMsg updateRole(RoleDTO roleDTO, String role_name) throws Exception {
		Role role = mapper.mapToRole(roleDTO);
		String prev_name = role.getRoleName();
		if(role.getEmployees().size()==0) {
			role.setRoleName(role_name);
			try {
				Role newRole = daoService.getElement(role);
				return new LogMsg("Role " + role_name + " already exists!", "red"); 
			} catch(Exception exception) {
				daoService.updateElement(role);
				return new LogMsg("Successfully updated role " + prev_name + " to " + role.getRoleName() + "!", "green");
			}
		}
		logMsg = "Role " + role.getRoleName() + " cannot be updated!";
		throw new Exception();
	}

	public List<EmployeeDTO> getSimplifiedEmployees(String order, String asc_desc, String query) {
		return mapper.mapSimplifiedEmployees(daoService.getSimplifiedEmployees(order, asc_desc, query));
	}

	public List<RoleDTO> getAllRoles() {
		return mapper.getAllRoles(daoService.getAllElements(Role.class));
	}

	public RoleDTO getRole(int role_id) throws Exception {
		Role role = new Role();
		try {
			role = daoService.getElement(Long.valueOf(role_id), Role.class);
			role.getRoleName();
		} catch(Exception exception) {
			logMsg = "Role does not exist!";
			throw exception;
		}
		return mapper.mapToRoleDTO(role);
	}

	public String getLogMsg() {
		return logMsg;
	}

	private Role addRole(Long roleId) {
		return daoService.getElement(roleId, Role.class);
	}
}