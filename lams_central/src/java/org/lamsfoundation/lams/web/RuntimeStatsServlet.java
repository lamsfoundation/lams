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

package org.lamsfoundation.lams.web;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.util.Locale;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.stat.CacheRegionStatistics;
import org.lamsfoundation.lams.util.NumberUtil;
import org.lamsfoundation.lams.util.hibernate.HibernateSessionManager;
import org.lamsfoundation.lams.web.session.SessionManager;

public class RuntimeStatsServlet extends HttpServlet {
    private static final long serialVersionUID = 6834774025623257743L;
    private static Logger log = Logger.getLogger(RuntimeStatsServlet.class);

    private static final int CACHE_STATS_COLLECT_SECONDS = 5;
    private static long cacheTimestamp;
    private static long cacheHits;
    private static int cacheHitsPerSecond;
    private static long cacheMisses;
    private static int cacheMissesPerSecond;
    private static long queryCacheHits;
    private static int queryCacheHitsPerSecond;
    private static long queryCacheMisses;
    private static int queryCacheMissesPerSecond;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	if (log.isDebugEnabled()) {
	    log.debug("Getting runtime stats");
	}

	StringBuilder stats = new StringBuilder();
	try {
	    MBeanServer server = ManagementFactory.getPlatformMBeanServer();
	    stats.append("Overall Status : OK").append("\n");
	    stats.append("jvmRoute: ").append(SessionManager.getJvmRoute()).append("\n");

	    MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
	    MemoryUsage memoryUsage = memoryBean.getHeapMemoryUsage();
	    stats.append("Heap memory MB [init/used/committed/max]: ")
		    .append(RuntimeStatsServlet.toMB(memoryUsage.getInit())).append("/")
		    .append(RuntimeStatsServlet.toMB(memoryUsage.getUsed())).append("/")
		    .append(RuntimeStatsServlet.toMB(memoryUsage.getCommitted())).append("/")
		    .append(RuntimeStatsServlet.toMB(memoryUsage.getMax())).append("\n");

	    memoryUsage = memoryBean.getNonHeapMemoryUsage();
	    stats.append("Non-heap memory MB [init/used/committed/max]: ")
		    .append(RuntimeStatsServlet.toMB(memoryUsage.getInit())).append("/")
		    .append(RuntimeStatsServlet.toMB(memoryUsage.getUsed())).append("/")
		    .append(RuntimeStatsServlet.toMB(memoryUsage.getCommitted())).append("/")
		    .append(RuntimeStatsServlet.toMB(memoryUsage.getMax())).append("\n");

	    ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
	    stats.append("JVM threads [daemon/total/peak]: ").append(threadBean.getDaemonThreadCount()).append("/")
		    .append(threadBean.getThreadCount()).append("/").append(threadBean.getPeakThreadCount())
		    .append("\n");

	    ObjectName connectorName = new ObjectName("jboss.as.expr:subsystem=io,worker=default");
	    String busyThreads = (String) server.getAttribute(connectorName, "ioThreads");
	    String maxThreads = (String) server.getAttribute(connectorName, "taskMaxThreads");
	    stats.append("IO threads [io/task max]: ").append(busyThreads).append("/").append(maxThreads).append("\n");

	    stats.append("Active sessions [user/total]: ").append(SessionManager.getSessionUserCount()).append("/")
		    .append(SessionManager.getSessionTotalCount()).append("\n");

	    ObjectName dataSourceName = new ObjectName(
		    "jboss.as.expr:subsystem=datasources,data-source=lams-ds,statistics=pool");
	    Integer inUseConnections = Integer.parseInt((String) server.getAttribute(dataSourceName, "InUseCount"));
	    Integer activeConnections = Integer.parseInt((String) server.getAttribute(dataSourceName, "ActiveCount"));
	    Integer availConnections = Integer.parseInt((String) server.getAttribute(dataSourceName, "AvailableCount"));
	    String maxUsageTime = (String) server.getAttribute(dataSourceName, "MaxUsageTime");
	    stats.append("Connection count [in use/idle/left]: ").append(inUseConnections).append("/")
		    .append(activeConnections - inUseConnections).append("/")
		    .append(availConnections - activeConnections).append("\n");
	    stats.append("Connection max usage time ms: ").append(maxUsageTime).append("\n\n");

	    // 2nd level cache general stats
	    ObjectName cacheContainerName = new ObjectName(
		    "org.wildfly.clustering.infinispan:type=CacheManager,name=\"hibernate\",component=CacheContainerStats");
	    boolean isAvailable = server.isRegistered(cacheContainerName);
	    if (isAvailable) {
		long currentTimestamp = System.currentTimeMillis();
		float secondsPassed = cacheTimestamp > 0 ? (currentTimestamp - cacheTimestamp) / 1000f : 0;
		boolean updatePerSecondStats = secondsPassed >= CACHE_STATS_COLLECT_SECONDS;

		Integer currentNumberOfEntriesInMemory = (Integer) server.getAttribute(cacheContainerName,
			"currentNumberOfEntriesInMemory");
		stats.append("Cache number of entries in memory: ").append(currentNumberOfEntriesInMemory).append("\n");

		Long hits = (Long) server.getAttribute(cacheContainerName, "hits");
		stats.append("Cache hits: ").append(hits).append("\n");
		if (updatePerSecondStats) {
		    cacheHitsPerSecond = Math.round((hits - cacheHits) / secondsPassed);
		    cacheHits = hits;
		}
		stats.append("Cache hits per second: ").append(cacheHitsPerSecond).append("\n");

		Long misses = (Long) server.getAttribute(cacheContainerName, "misses");
		stats.append("Cache misses: ").append(misses).append("\n");
		if (updatePerSecondStats) {
		    cacheMissesPerSecond = Math.round((misses - cacheMisses) / secondsPassed);
		    cacheMisses = misses;
		}
		stats.append("Cache misses per second: ").append(cacheMissesPerSecond).append("\n");

		Double hitRatio = (Double) server.getAttribute(cacheContainerName, "hitRatio");
		stats.append("Cache hit ratio: ").append(NumberUtil.formatLocalisedNumber(hitRatio, (Locale) null, 2))
			.append("\n");
		Double readWriteRatio = (Double) server.getAttribute(cacheContainerName, "readWriteRatio");
		stats.append("Cache read/write ratio: ")
			.append(NumberUtil.formatLocalisedNumber(readWriteRatio, (Locale) null, 2)).append("\n\n");

		// query cache stats
		CacheRegionStatistics queryCacheStatistics = HibernateSessionManager.getStatistics()
			.getQueryRegionStatistics("default-query-results-region");
		if (queryCacheStatistics != null) {
		    stats.append("Query cache number of entries in memory: ")
			    .append(queryCacheStatistics.getElementCountInMemory()).append("\n");
		    hits = queryCacheStatistics.getHitCount();
		    stats.append("Query cache hits: ").append(hits).append("\n");
		    if (updatePerSecondStats) {
			queryCacheHitsPerSecond = Math.round((hits - queryCacheHits) / secondsPassed);
			queryCacheHits = hits;
		    }
		    stats.append("Query cache hits per second: ").append(queryCacheHitsPerSecond).append("\n");

		    misses = queryCacheStatistics.getMissCount();
		    stats.append("Query cache misses: ").append(misses).append("\n");
		    if (updatePerSecondStats) {
			queryCacheMissesPerSecond = Math.round((misses - queryCacheMisses) / secondsPassed);
			queryCacheMisses = misses;
		    }
		    stats.append("Query cache misses per second: ").append(queryCacheMissesPerSecond).append("\n");

		    hitRatio = hits.doubleValue() / (hits + misses);
		    stats.append("Query cache hit ratio: ")
			    .append(NumberUtil.formatLocalisedNumber(hitRatio, (Locale) null, 2)).append("\n");
		}

		if (cacheTimestamp == 0 || updatePerSecondStats) {
		    cacheTimestamp = currentTimestamp;
		}
	    }
	} catch (Exception e) {
	    log.error("Error while getting long runtime stats", e);
	}

	if (stats.length() > 0) {
	    response.setContentType("text/plain;charset=utf-8");
	    response.getWriter().write(stats.toString());
	}
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	doGet(request, response);
    }

    private static long toMB(long bytes) {
	return bytes / 1024 / 1024;
    }
}
