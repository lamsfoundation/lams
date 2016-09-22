/**
 * VerificationServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.lamsfoundation.lams.webservice;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServlet;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;
import org.lamsfoundation.lams.integration.ExtServer;
import org.lamsfoundation.lams.integration.security.Authenticator;
import org.lamsfoundation.lams.integration.service.IntegrationService;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class VerificationServiceSoapBindingImpl implements org.lamsfoundation.lams.webservice.Verification {
    private static MessageContext context = MessageContext.getCurrentContext();

    private static IntegrationService integrationService = (IntegrationService) WebApplicationContextUtils
	    .getRequiredWebApplicationContext(
		    ((HttpServlet) context.getProperty(HTTPConstants.MC_HTTP_SERVLET)).getServletContext())
	    .getBean("integrationService");

    @Override
    public boolean verify(String serverId, String datetime, String hash) throws java.rmi.RemoteException {
	try {
	    ExtServer extServer = integrationService.getExtServer(serverId);
	    Authenticator.authenticate(extServer, datetime, hash);
	    return true;
	} catch (Exception e) {
	    e.printStackTrace(System.err);
	    throw new RemoteException(e.getMessage(), e);
	}
    }

}
