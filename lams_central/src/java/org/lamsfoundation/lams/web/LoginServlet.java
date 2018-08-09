package org.lamsfoundation.lams.web;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.core.Config;

import org.lamsfoundation.lams.util.LanguageUtil;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	// Take care about login.jsp locale. At first, get it from client's browser and check whether LAMS 
	// supports it. And if not, uses server's default locale.
	Locale browserLocale = request.getLocale();
	Locale preferredLocale = LanguageUtil.getSupportedLocaleByNameOrLanguageCode(browserLocale);
	Config.set(request, Config.FMT_LOCALE, preferredLocale);

	request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	doPost(request, response);
    }

}
