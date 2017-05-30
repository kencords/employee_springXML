package ecc.cords;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;	
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class HomeController extends SimpleFormController {

	private static DaoService daoService = new DaoService();
	private static DTO_EntityMapper mapper = new DTO_EntityMapper();
	private List<LogMsg> logMsgs = new ArrayList<>();

	public HomeController() {
		setCommandClass(EmployeeDTO.class);
	}

	@Override
	protected ModelAndView showForm(HttpServletRequest req, HttpServletResponse res, BindException errors) {
		req.getSession().invalidate();
		ModelAndView mav = new ModelAndView("home");
		List<EmployeeDTO> empList = mapper.mapSimplifiedEmployees("name.lastName", "asc");
		mav.addObject("empList", empList);
		return mav;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest req, 
									HttpServletResponse res,
									Object command, BindException errors) {
		List<EmployeeDTO> empList = null;
		ModelAndView mav = new ModelAndView("home");
		String order = "";
		String ascDesc = "";

		if(req.getParameter("sortBtn") != null) {
			order = req.getParameter("sort");
			ascDesc = req.getParameter("asc_desc");
			logMsgs.add(new LogMsg("Sorted By " + order + ":" + ascDesc, "green"));
		}

		if(req.getParameter("delEmpBtn") != null) {
			logMsgs.add(EmployeeManager.deleteEmployee(Integer.parseInt(req.getParameter("delEmpBtn"))));
		}

		if(req.getParameter("viewEmpBtn") != null) {
			return new ModelAndView("redirect:/employeeProfile?empId=" + req.getParameter("viewEmpBtn"));
		}

		req.setAttribute("logMsgs", Utils.sortLogMsgs(logMsgs));
		logMsgs.clear();
		empList = displayEmployees(order, ascDesc);
		mav.addObject("empList", empList);
		return mav;
	}

	public List<EmployeeDTO> displayEmployees(String order, String asc_desc) {
		String dbOrder = "name.lastName";
		String dbAscDesc = (asc_desc!=null && !asc_desc.equals(""))? asc_desc : "asc";
		if(order!=null && !order.equals("")) {
			dbOrder = order.equals("GWA")? "gwa" : (order.equals("LastName")? "name.lastName" : "hireDate");
		}
		return mapper.mapSimplifiedEmployees(dbOrder, dbAscDesc);
	}
}