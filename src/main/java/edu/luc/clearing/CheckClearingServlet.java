package edu.luc.clearing;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@SuppressWarnings("serial")
public class CheckClearingServlet extends HttpServlet {

	private RequestReader requestReader;

	public CheckClearingServlet() {
		requestReader = new RequestReader();
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("application/json");
		resp.getWriter().print(requestReader.respond(req.getReader()));
	}

	

}