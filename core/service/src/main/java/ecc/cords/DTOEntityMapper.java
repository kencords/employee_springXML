package ecc.cords;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public interface DTOEntityMapper {

	public Address createAddress(AddressDTO addressDTO);

	public AddressDTO createAddressDTO(Address address);

	public Contact createContact(ContactDTO contactDTO);

	public ContactDTO createContactDTO(String contactType, String contactValue);

	public Set<Contact> createContactSet(Employee employee, Set<ContactDTO> contactsDTO);

	public Set<ContactDTO> createContactSetDTO(Set<Contact> contacts);

	public Set<Role> createRoleSet(Set<RoleDTO> rolesDTO);

	public Set<RoleDTO> createRoleSetDTO(Set<Role> roles);

	public List<RoleDTO> getAllRoles(List<Role> roles);

	public Employee mapToEmployee(EmployeeDTO employeeDTO, boolean isNew);

	public EmployeeDTO mapToEmployeeDTO(Employee employee) throws Exception;

	public List<EmployeeDTO> mapEmployeeDTOList(String order);

	public List<EmployeeDTO> mapSimplifiedEmployees(List<Object[]> employees);

	public RoleDTO mapToRoleDTO(Role role) throws Exception;

	public Role mapToRole(RoleDTO roleDTO);
}