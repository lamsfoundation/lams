package org.lamsfoundation.lams.web;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.web.util.HttpSessionManager;


/**
 * Listens for the creation and destruction of http sessions, and reports back to 
 * the ClientSessionInfoManager.
 *
 * @web.listener 
 */
/* Should come out in web.xml as:
 * <!-- Listeners -->
 *	<listener>
 *		<listener-class>
 *		com.webmcq.ld.controller.web.SessionListener
 *		</listener-class>
 *	</listener>
 */
public class SessionListener implements HttpSessionListener
{
	private static Logger log = Logger.getLogger(SessionListener.class);

	/** HttpSessionListener interface */
	public void sessionCreated(HttpSessionEvent se)
	{
		HttpSessionManager.getInstance().sessionCreated(se);
	}

	/** HttpSessionListener interface */
	public void sessionDestroyed(HttpSessionEvent se)
	{
		//nothing to do
	}

}
