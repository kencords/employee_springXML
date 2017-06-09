package ecc.cords;

import java.util.List;

public interface HomeController {
	
	public List<EmployeeDTO> displayEmployees(String order, String asc_desc, String query);
}