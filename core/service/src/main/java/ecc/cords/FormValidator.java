package ecc.cords;

import java.util.List;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public interface FormValidator {

	public void saveEmployeeIfValid(List<LogMsg> logMsgs, List<ContactDTO> contacts, List<RoleDTO> roles,
	HttpServletRequest req, HttpServletResponse res, boolean isEdit) 
	throws IOException, ServletException;
}