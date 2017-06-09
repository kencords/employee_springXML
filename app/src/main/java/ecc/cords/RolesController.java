package ecc.cords;

import java.util.List;

public interface RolesController {

	public List<RoleDTO> getRoles();

	public RoleDTO getRole(int roleId);

	public String getRoleOwners(RoleDTO role);
}