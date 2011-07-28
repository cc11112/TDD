package edu.luc.clearing;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class CheckClearingServlet extends HttpServlet {

	private RequestReader requestReader;
	private CheckHistory checkHistory;

	public CheckClearingServlet() {
		this(new DatastoreAdapter());
	}

	CheckClearingServlet(DatastoreAdapter store) {
		requestReader = new RequestReader(store, new SystemClock());
		checkHistory = new CheckHistory(store);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		resp.setContentType("application/json");
		resp.getWriter().print(requestReader.respond(req.getReader()));

	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("application/json");
		resp.getWriter().print(checkHistory.getAoumnts("Checks", req.getParameter("limit")));
	}

}