package org.lamsfoundation.bb.integration.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.lamsfoundation.bb.integration.util.LamsPluginUtil;
import org.xml.sax.SAXException;

import blackboard.base.InitializationException;
import blackboard.data.ValidationException;
import blackboard.persist.PersistenceException;
import blackboard.platform.BbServiceException;
import blackboard.platform.plugin.PlugInException;
import blackboard.platform.plugin.PlugInUtil;

/**
 * Handles displaying and modification of the LAMS BB plugin's config settings.
 */
@SuppressWarnings("serial")
public class ConfigServlet extends HttpServlet{
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	process(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	// SECURITY!
	// Authorise current user for System Admin (automatic redirect)
	try {
	    if (!PlugInUtil.authorizeForSystemAdmin(request, response))
		return;
	} catch (PlugInException e) {
	    throw new RuntimeException(e);
	}

	try {
	    String method = request.getParameter("method");
	    if (method.equals("showConfigSettings")) {
		showConfigSettings(request, response);
		
	    } else if (method.equals("saveConfigSettings")) {
		saveConfigSettings(request, response);
	    }

	} catch (InitializationException e) {
	    throw new ServletException(e);
	} catch (BbServiceException e) {
	    throw new ServletException(e);
	} catch (PersistenceException e) {
	    throw new ServletException(e);
	}  catch (ParseException e) {
	    throw new ServletException(e);
	} catch (ValidationException e) {
	    throw new ServletException(e);
	} catch (ParserConfigurationException e) {
	    throw new ServletException(e);
	} catch (SAXException e) {
	    throw new ServletException(e);
	}
    }
    
    /**
     * Show /admin/config.jsp page.
     */
    private void showConfigSettings(HttpServletRequest request, HttpServletResponse response)
	    throws InitializationException, BbServiceException, PersistenceException, IOException, ServletException {

	// Get the LAMS2 Building Block properties from Blackboard (if set)
	Properties properties = LamsPluginUtil.getProperties();
	
	String lamsServerUrl = properties.getProperty(LamsPluginUtil.PROP_LAMS_URL, "http://");
	request.setAttribute("lamsServerUrl", lamsServerUrl);
	
	String lamsServerId = properties.getProperty(LamsPluginUtil.PROP_LAMS_SERVER_ID, "");
	request.setAttribute("lamsServerId", lamsServerId);
	
	String secretKey = properties.getProperty(LamsPluginUtil.PROP_LAMS_SECRET_KEY, "");
	request.setAttribute("secretKey", secretKey);
	
	String lamsServerTimeRefreshInterval = properties.getProperty(LamsPluginUtil.PROP_LAMS_SERVER_TIME_REFRESH_INTERVAL);
	request.setAttribute("lamsServerTimeRefreshInterval", lamsServerTimeRefreshInterval);

	request.getRequestDispatcher("/admin/config.jsp").forward(request, response);
    }
    
    /**
     * Saves modified configuration settings.
     */
    private void saveConfigSettings(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException, PersistenceException, ParseException, ValidationException,
	    ParserConfigurationException, SAXException {

	// Get the properties object
	Properties properties = LamsPluginUtil.getProperties();

	// Get the LAMS2 Building Block properties from the request
	String lamsServerUrl = request.getParameter("lamsServerUrl");
	String lamsServerId = request.getParameter("lamsServerId");
	String lamsSecretKey = request.getParameter("lamsSecretKey");
	String lamsServerTimeRefreshInterval = request.getParameter("lamsServerTimeRefreshInterval");

	// Save the properties to Blackboard
	properties.setProperty(LamsPluginUtil.PROP_LAMS_URL, lamsServerUrl);
	properties.setProperty(LamsPluginUtil.PROP_LAMS_SECRET_KEY, lamsSecretKey);
	properties.setProperty(LamsPluginUtil.PROP_LAMS_SERVER_ID, lamsServerId);
	properties.setProperty(LamsPluginUtil.PROP_LAMS_SERVER_TIME_REFRESH_INTERVAL, lamsServerTimeRefreshInterval);

	// Persist the properties object
	LamsPluginUtil.setProperties(properties);
	
	request.getRequestDispatcher("/admin/configSuccess.jsp").forward(request, response);
    }
}
