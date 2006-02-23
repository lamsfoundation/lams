package org.lamsfoundation.lams.admin.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.monitoring.service.MonitoringServiceProxy;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;

/**
 * Main access calls for the bulk of the system adminstration.
 * 
 * @author Fiona Malikoff
 * 
 * @struts:action path="/sysadmin" validate="false" parameter="method"
 * @struts:action-forward name="sysadmin" path=".sysadmin"
 * @struts:action-forward name="error" path=".admin.error"
 */
public class SysadminAction extends LamsDispatchAction {

	
	private static Logger logger = Logger.getLogger(SysadminAction.class);


	

}