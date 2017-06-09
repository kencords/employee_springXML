package ecc.cords;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public interface EmployeeController {

	public ModelAndView addPage(HttpServletRequest req, HttpServletResponse res) throws Exception;

	public ModelAndView editPage(HttpServletRequest req, HttpServletResponse res) throws Exception;

	public ModelAndView viewPage(HttpServletRequest req, HttpServletResponse res) throws Exception;

	public ModelAndView addOnSubmit(HttpServletRequest req, HttpServletResponse res) throws Exception;

	public ModelAndView editOnSubmit(HttpServletRequest req, HttpServletResponse res) throws Exception;
}