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
import javax.management.ObjectName;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;

public class RuntimeStatsServlet extends HttpServlet {
    private static final long serialVersionUID = 6834774025623257743L;
    private static Logger log = Logger.getLogger(RuntimeStatsServlet.class);

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	boolean isLongStats = WebUtil.readBooleanParam(request, "long", false);
	String stats = null;
	if (isLongStats) {
	    if (log.isDebugEnabled()) {
		log.debug("Getting long runtime stats");
	    }
	    stats = RuntimeStatsServlet.getLongStats();
	} else {
	    if (log.isDebugEnabled()) {
		log.debug("Getting short runtime stats");
	    }
	    stats = RuntimeStatsServlet.getShortStats();
	}

	if (stats != null) {
	    response.setContentType("text/plain;charset=utf-8");
	    response.getWriter().write(stats);
	}
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	doGet(request, response);
    }

    private static String getShortStats() {
	StringBuilder resp = new StringBuilder();
	Date date = new Date();
	MBeanServer server = ManagementFactory.getPlatformMBeanServer();
	try {
	    ObjectName dataSourceName = new ObjectName(
		    "jboss.as.expr:subsystem=datasources,data-source=lams-ds,statistics=pool");
	    Integer activeCount = Integer.valueOf((String) server.getAttribute(dataSourceName, "ActiveCount"));

	    resp.append("Overall Status : OK");
	    if (activeCount > 0) {
		resp.append(" - DB connection established");
	    }
	    resp.append("\njvmRoute: ").append(SessionManager.getJvmRoute()).append("\n");
	    resp.append("Active sessions [user/total]: ").append(SessionManager.getSessionUserCount()).append("/")
		    .append(SessionManager.getSessionTotalCount()).append("\n");
	    resp.append("Time of request: ").append(date);
	} catch (Exception e) {
	    log.error("Error while getting short runtime stats", e);
	}

	return resp.toString();
    }

    private static String getLongStats() {
	StringBuilder resp = new StringBuilder();

	MBeanServer server = ManagementFactory.getPlatformMBeanServer();
	try {
	    resp.append("jvmRoute: ").append(SessionManager.getJvmRoute()).append("\n");

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

	    ObjectName connectorName = new ObjectName("jboss.as.expr:subsystem=io,worker=default");
	    String busyThreads = (String) server.getAttribute(connectorName, "ioThreads");
	    String maxThreads = (String) server.getAttribute(connectorName, "taskMaxThreads");
	    resp.append("IO threads [io/task max]: ").append(busyThreads).append("/").append(maxThreads).append("\n");

	    resp.append("Active sessions [user/total]: ").append(SessionManager.getSessionUserCount()).append("/")
		    .append(SessionManager.getSessionTotalCount()).append("\n");

	    ObjectName dataSourceName = new ObjectName(
		    "jboss.as.expr:subsystem=datasources,data-source=lams-ds,statistics=pool");
	    Integer inUseConnections = Integer.parseInt((String) server.getAttribute(dataSourceName, "InUseCount"));
	    Integer activeConnections = Integer.parseInt((String) server.getAttribute(dataSourceName, "ActiveCount"));
	    Integer availConnections = Integer.parseInt((String) server.getAttribute(dataSourceName, "AvailableCount"));
	    String maxUsageTime = (String) server.getAttribute(dataSourceName, "MaxUsageTime");
	    resp.append("Connection count [in use/idle/left]: ").append(inUseConnections).append("/")
		    .append(activeConnections - inUseConnections).append("/")
		    .append(availConnections - activeConnections).append("\n");
	    resp.append("Connection max usage time ms: ").append(maxUsageTime);
	} catch (Exception e) {
	    log.error("Error while getting long runtime stats", e);
	}

	return resp.toString();
    }

    private static long toMB(long bytes) {
	return bytes / 1024 / 1024;
    }
}