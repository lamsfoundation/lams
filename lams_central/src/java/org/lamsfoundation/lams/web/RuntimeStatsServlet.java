/**************************************************************** 
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org) 
 * ============================================================= 
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/ 
 * 
 * This program is free software; you can redistribute it and/or modify 
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation. 
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU General Public License for more details. 
 * 
 * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA 
 * 
 * http://www.gnu.org/licenses/gpl.txt 
 * **************************************************************** 
 */

/* RuntimeStatsServlet.java,v 1.1 2015/04/28 11:52:07 marcin Exp */
package org.lamsfoundation.lams.web;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.util.Date;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jboss.mx.util.MBeanServerLocator;
import org.lamsfoundation.lams.util.HttpUrlConnectionUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;

public class RuntimeStatsServlet extends HttpServlet {

    private static Logger log = Logger.getLogger(RuntimeStatsServlet.class);

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	boolean isLongStats = WebUtil.readBooleanParam(request, "long", false);
	String stats = null;
	if (isLongStats) {
	    if (RuntimeStatsServlet.log.isDebugEnabled()) {
		RuntimeStatsServlet.log.debug("Getting long runtime stats");
	    }
	    stats = RuntimeStatsServlet.getLongStats();
	} else {
	    if (RuntimeStatsServlet.log.isDebugEnabled()) {
		RuntimeStatsServlet.log.debug("Getting short runtime stats");
	    }
	    stats = RuntimeStatsServlet.getShortStats();
	}

	if (stats != null) {
	    response.setContentType("text/plain;charset=utf-8");
	    response.getWriter().write(stats);
	}
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
	    IOException {
	doGet(request, response);
    }

    private static String getShortStats() {
	StringBuilder resp = new StringBuilder();
	Date date = new Date();

	MBeanServer server = MBeanServerLocator.locateJBoss();
	try {
	    ObjectName engineName = new ObjectName("jboss.web:type=Engine");
	    String jvmRoute = (String) server.getAttribute(engineName, "jvmRoute");

	    ObjectName sessionManager = new ObjectName("jboss.web:type=Manager,path=/lams,host=localhost");
	    Integer activeSessions = (Integer) server.getAttribute(sessionManager, "activeSessions");

	    ObjectName dataSourceName = new ObjectName("jboss.jca:name=jdbc/lams-ds,service=ManagedConnectionPool");
	    Integer pickedConnections = (Integer) server.invoke(dataSourceName, "getConnectionCount", null, null);

	    resp.append("Overall Status : ok");
	    if (pickedConnections > 0) {
		resp.append(" - DB connection established");
	    }
	    resp.append("\nServer : ").append(jvmRoute).append("\n");
	    resp.append("Current Sessions : ").append(SessionManager.getSessionCount()).append("\n");
	    resp.append("Time of Request : ").append(date);
	} catch (Exception e) {
	    RuntimeStatsServlet.log.error("Error while getting short runtime stats", e);
	}

	return resp.toString();
    }

    private static String getLongStats() {
	StringBuilder resp = new StringBuilder();

	MBeanServer server = MBeanServerLocator.locateJBoss();
	try {
	    ObjectName engineName = new ObjectName("jboss.web:type=Engine");
	    String jvmRoute = (String) server.getAttribute(engineName, "jvmRoute");
	    resp.append("jvmRoute: ").append(jvmRoute).append("\n");

	    MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
	    MemoryUsage memoryUsage = memoryBean.getHeapMemoryUsage();
	    resp.append("Heap memory MB [init/used/committed/max]: ")
		    .append(RuntimeStatsServlet.toMB(memoryUsage.getInit())).append("/")
		    .append(RuntimeStatsServlet.toMB(memoryUsage.getUsed())).append("/")
		    .append(RuntimeStatsServlet.toMB(memoryUsage.getCommitted())).append("/")
		    .append(RuntimeStatsServlet.toMB(memoryUsage.getMax())).append("\n");

	    memoryUsage = memoryBean.getNonHeapMemoryUsage();
	    resp.append("Non-heap memory MB [init/used/committed/max]: ")
		    .append(RuntimeStatsServlet.toMB(memoryUsage.getInit())).append("/")
		    .append(RuntimeStatsServlet.toMB(memoryUsage.getUsed())).append("/")
		    .append(RuntimeStatsServlet.toMB(memoryUsage.getCommitted())).append("/")
		    .append(RuntimeStatsServlet.toMB(memoryUsage.getMax())).append("\n");

	    ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
	    resp.append("JVM threads [daemon/total/peak]: ").append(threadBean.getDaemonThreadCount()).append("/")
		    .append(threadBean.getThreadCount()).append("/").append(threadBean.getPeakThreadCount())
		    .append("\n");

	    ObjectName connectorName = RuntimeStatsServlet.getConnectorName();
	    Integer maxThreads = (Integer) server.getAttribute(connectorName, "maxThreads");
	    Integer busyThreads = (Integer) server.getAttribute(connectorName, "currentThreadsBusy");
	    resp.append("Connector threads [busy/max]: ").append(busyThreads).append("/").append(maxThreads)
		    .append("\n");

	    resp.append("Active sessions : ").append(SessionManager.getSessionCount()).append("\n");

	    ObjectName dataSourceName = new ObjectName("jboss.jca:name=jdbc/lams-ds,service=ManagedConnectionPool");
	    Long availConnections = (Long) server.invoke(dataSourceName, "getAvailableConnectionCount", null, null);
	    Long usedConnections = (Long) server.invoke(dataSourceName, "getInUseConnectionCount", null, null);
	    Integer pickedConnections = (Integer) server.invoke(dataSourceName, "getConnectionCount", null, null);
	    resp.append("Connections [in use/picked/total]: ").append(usedConnections).append("/")
		    .append(pickedConnections).append("/").append(availConnections).append("\n");
	} catch (Exception e) {
	    RuntimeStatsServlet.log.error("Error while getting long runtime stats", e);
	}

	return resp.toString();
    }

    private static ObjectName getConnectorName() throws MalformedObjectNameException {
	String bindAddress = System.getProperty(HttpUrlConnectionUtil.JBOSS_BIND_ADDRESS_KEY);
	if (bindAddress.equalsIgnoreCase("localhost")) {
	    bindAddress = "localhost%2F127.0.0.1";
	}
	String bindPort = System.getProperty(HttpUrlConnectionUtil.LAMS_PORT_KEY);
	if (bindPort == null) {
	    bindPort = "8080";
	}
	return new ObjectName("jboss.web:type=ThreadPool,name=http-" + bindAddress + "-" + bindPort);
    }

    private static long toMB(long bytes) {
	return bytes / 1024 / 1024;
    }
}