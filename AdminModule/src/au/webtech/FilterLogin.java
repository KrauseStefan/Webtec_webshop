package au.webtech;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import au.webtech.bean.AdminLogin;

/**
 * Filter used to ensure users are always logged in, this filter is applied to
 * all jsf pages in use. It works together with AdminLogin bean.
 * 
 * If AdminLogin reports that the user is logged in this filter will not take
 * action, else it redirects to the login page
 */
@WebFilter(filterName = "authFilter", servletNames = { "Faces Servlet" })
public class FilterLogin implements Filter {

	public FilterLogin() {
	}

	private AdminLogin getAdminLoginBean(ServletRequest request) {
		HttpSession session = ((HttpServletRequest) request).getSession();
		String neededBean = "adminLogin";

		Enumeration<String> names = session.getAttributeNames();
		AdminLogin adminLogin = null;
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			if (name.equals(neededBean)) {
				adminLogin = (AdminLogin) session.getAttribute(neededBean);
				break;
			}
		}

		return adminLogin;
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String url = req.getServletPath();
		boolean doRedirect = false;

		AdminLogin adminLogin = getAdminLoginBean(request);

		if (!url.equals("/login.jsf")) {
			if (adminLogin == null || !adminLogin.isLooggedin()) {
				doRedirect = true;
			}
		}

		if (doRedirect) {
			HttpServletResponse resp = (HttpServletResponse) response;
			resp.reset();
			resp.sendRedirect("login.jsf");
		}

		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {

	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

}
