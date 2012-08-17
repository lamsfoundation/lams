/*
 * Copyright 2004-2005 OpenSymphony
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 */

/*
 * Previously Copyright (c) 2001-2004 James House
 */
package org.quartz.ee.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

/**
 * <p>
 * A ServletContextListner that can be used to initialize Quartz.
 * </p>
 *
 * <p>
 * You'll want to add something like this to your WEB-INF/web.xml file:
 *
 * <pre>
 *     &lt;context-param&gt;
 *         &lt;param-name&gt;config-file&lt;/param-name&gt;
 *         &lt;param-value&gt;/some/path/my_quartz.properties&lt;/param-value&gt;
 *     &lt;/context-param&gt;
 *     &lt;context-param&gt;
 *         &lt;param-name&gt;shutdown-on-unload&lt;/param-name&gt;
 *         &lt;param-value&gt;true&lt;/param-value&gt;
 *     &lt;/context-param&gt;
 *     &lt;context-param&gt;
 *         &lt;param-name&gt;start-scheduler-on-load&lt;/param-name&gt;
 *         &lt;param-value&gt;true&lt;/param-value&gt;
 *     &lt;/context-param&gt;
 *
 *     
 *     &lt;listener&gt;
 *         &lt;listener-class&gt;
 *             org.quartz.ee.servlet.QuartzInitializerServletListener
 *         &lt;/listener-class&gt;
 *     &lt;/listener&gt;
 * </pre>
 *
 * </p>
 * <p>
 * The init parameter 'config-file' can be used to specify the path (and
 * filename) of your Quartz properties file. If you leave out this parameter,
 * the default ("quartz.properties") will be used.
 * </p>
 *
 * <p>
 * The init parameter 'shutdown-on-unload' can be used to specify whether you
 * want scheduler.shutdown() called when the servlet is unloaded (usually when
 * the application server is being shutdown). Possible values are "true" or
 * "false". The default is "true".
 * </p>
 *
 * <p>
 * The init parameter 'start-scheduler-on-load' can be used to specify whether
 * you want the scheduler.start() method called when the servlet is first loaded.
 * If set to false, your application will need to call the start() method before
 * teh scheduler begins to run and process jobs. Possible values are "true" or
 * "false". The default is "true", which means the scheduler is started.
 * </p>
 *
 * A StdSchedulerFactory instance is stored into the ServletContext. You can gain access
 * to the factory from a ServletContext instance like this:
 * <br>
 * <code>
 * StdSchedulerFactory factory = (StdSchedulerFactory) ctx
 *				.getAttribute(QuartzFactoryServlet.QUARTZ_FACTORY_KEY);
 * </code>
 * <br>
 * Once you have the factory instance, you can retrieve the Scheduler instance by calling
 * <code>getScheduler()</code> on the factory.
 *
 * @author James House
 * @author Chuck Cavaness
 * @author John Petrocik
 */
public class QuartzInitializerListener implements ServletContextListener {

	public static final String QUARTZ_FACTORY_KEY = "org.quartz.impl.StdSchedulerFactory.KEY";

	private boolean performShutdown = true;

	private Scheduler scheduler = null;

	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *
	 * Interface.
	 *
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */

	public void contextInitialized(ServletContextEvent sce) {

		System.out.println("Quartz Initializer Servlet loaded, initializing Scheduler...");

		ServletContext servletContext = sce.getServletContext();
		StdSchedulerFactory factory;
		try {

			String configFile = servletContext.getInitParameter("config-file");
			String shutdownPref = servletContext.getInitParameter("shutdown-on-unload");

			if (shutdownPref != null)
				performShutdown = Boolean.valueOf(shutdownPref).booleanValue();

			// get Properties
			if (configFile != null) {
				factory = new StdSchedulerFactory(configFile);
			} else {
				factory = new StdSchedulerFactory();
			}

			// Should the Scheduler being started now or later
			String startOnLoad = servletContext
					.getInitParameter("start-scheduler-on-load");
			/*
			 * If the "start-scheduler-on-load" init-parameter is not specified,
			 * the scheduler will be started. This is to maintain backwards
			 * compatability.
			 */
			if (startOnLoad == null || (Boolean.valueOf(startOnLoad).booleanValue())) {
				// Start now
				scheduler = factory.getScheduler();
				scheduler.start();
				System.out.println("Scheduler has been started...");
			} else {
				System.out.println("Scheduler has not been started. Use scheduler.start()");
			}

			System.out.println("Storing the Quartz Scheduler Factory in the servlet context at key: "
					+ QUARTZ_FACTORY_KEY);
			servletContext.setAttribute(QUARTZ_FACTORY_KEY, factory);

		} catch (Exception e) {
			System.out.println("Quartz Scheduler failed to initialize: " + e.toString());
			e.printStackTrace();
		}
	}

	public void contextDestroyed(ServletContextEvent sce) {

		if (!performShutdown)
			return;

		try {
			if (scheduler != null)
				scheduler.shutdown();
		} catch (Exception e) {
			System.out.println("Quartz Scheduler failed to shutdown cleanly: " + e.toString());
			e.printStackTrace();
		}

		System.out.println("Quartz Scheduler successful shutdown.");
	}


}
