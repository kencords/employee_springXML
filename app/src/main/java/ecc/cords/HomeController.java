package ecc.cords;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;	
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class HomeController extends SimpleFormController {

	private final Logger logger = LoggerFactory.getLogger(HomeController.class);

	private DTO_EntityMapper mapper;
	private EmployeeManager empManager;
	private List<LogMsg> logMsgs = new ArrayList<>();

	public HomeController() {
		setCommandClass(EmployeeDTO.class);
	}

	public void setEmployeeManager(EmployeeManager empManager) {
		this.empManager = empManager;
	}

	public void setMapper(DTO_EntityMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	protected ModelAndView showForm(HttpServletRequest req, HttpServletResponse res, BindException errors) {
		req.getSession().invalidate();
		ModelAndView mav = new ModelAndView("home");
		List<EmployeeDTO> empList = mapper.mapSimplifiedEmployees("name.lastName", "asc", "");
		mav.addObject("empList", empList);
		return mav;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest req, 
									HttpServletResponse res,
									Object command, BindException errors) {
		logger.info("called onSubmit()");
		List<EmployeeDTO> empList = null;
		ModelAndView mav = new ModelAndView("home");
		String order = "";
		String ascDesc = "";
		String query = "";

		if(req.getParameter("searchBtn") != null) {
			order = "name.lastName";
			ascDesc = "asc";
			query = req.getParameter("search");
			String msg = (query==null || query.trim().equals("")? "Search field cannot be empty!" :
			"Searched by name matching %" + query + "%");
			String color =(query==null || query.trim().equals("")? "red" : "green");
			logMsgs.add(new LogMsg(msg, color));
		}
		if(req.getParameter("sortBtn") != null) {
			order = req.getParameter("sort");
			ascDesc = req.getParameter("asc_desc");
			query = req.getParameter("search");
			logMsgs.add(new LogMsg("Sorted By " + order + ":" + ascDesc + 
			(query!=null && !query.trim().equals("") ? " with name matching %" + query + "%" : ""), "green"));
		}

		if(req.getParameter("delEmpBtn") != null) {
			logMsgs.add(empManager.deleteEmployee(Integer.parseInt(req.getParameter("delEmpBtn"))));
		}

		if(req.getParameter("viewEmpBtn") != null) {
			return new ModelAndView("redirect:/employeeProfile?empId=" + req.getParameter("viewEmpBtn"));
		}

		req.setAttribute("logMsgs", Utils.sortLogMsgs(logMsgs));
		logMsgs.clear();
		empList = displayEmployees(order, ascDesc, query);
		mav.addObject("empList", empList);
		return mav;
	}

	public List<EmployeeDTO> displayEmployees(String order, String asc_desc, String query) {
		String dbOrder = "name.lastName";
		String dbAscDesc = (asc_desc!=null && !asc_desc.equals(""))? asc_desc : "asc";
		if(order!=null && !order.equals("")) {
			dbOrder = order.equals("GWA")? "gwa" : (order.equals("LastName")? "name.lastName" : "hireDate");
		}
		return mapper.mapSimplifiedEmployees(dbOrder, dbAscDesc, query);
	}
}