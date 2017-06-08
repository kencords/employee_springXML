package ecc.cords;

import java.util.List;
import java.util.Set;

public interface EmployeeManager {

	public LogMsg addEmployee(EmployeeDTO employeeDTO);

	public LogMsg updateEmployee(EmployeeDTO employeeDTO);

	public LogMsg deleteEmployee(int empId);

	public EmployeeDTO addContact(EmployeeDTO employee, Set<ContactDTO> contacts);

	public void updateContact(EmployeeDTO employee, Set<ContactDTO> contacts);

	public void deleteContact(EmployeeDTO employee, ContactDTO contact) throws Exception;

	public Employee getEmployee(int id);

	public EmployeeDTO getEmployeeDTO(int id) throws Exception;

	public List<EmployeeDTO> getSimplifiedEmployees(String order, String asc_desc, String query);

	public EmployeeDTO addEmployeeRole(EmployeeDTO employee, int role_id) throws Exception;

	public EmployeeDTO deleteEmployeeRole(EmployeeDTO employee, RoleDTO role);

	public LogMsg createRole(String role_str);

	public String deleteRole(RoleDTO roleDTO) throws Exception;

	public LogMsg updateRole(RoleDTO roleDTO, String role_name) throws Exception;

	public List<RoleDTO> getAllRoles();

	public RoleDTO getRole(int role_id) throws Exception;

	public String getLogMsg();
}