/****************************************************************
 * Copyright (C) 2006 LAMS Foundation (http://lamsfoundation.org)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.testharness;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.meterware.httpunit.HttpUnitOptions;

/**
 * @version
 *
 * <p>
 * <a href="Main.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 *
 */
public class Main {

	static {
		// configure log4j
		PropertyConfigurator.configure("log.properties");
		// configure HttpUnit
		// stops the unsupported javascript stuff from throwing an exception
		HttpUnitOptions.setExceptionsThrownOnScriptError(false);
		//WebClient.getResponse does not throw an exception when it receives an error status.
		HttpUnitOptions.setExceptionsThrownOnErrorStatus(false);
	}

	private static final Logger log = Logger.getLogger(Main.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		log.info("Starting...");
		if (args.length == 0) {
			log.error("No argument supplied! Please refer to readme.txt");
			System.exit(1);
		}
		// create test manager and hand over the control to him.
		AbstractTestManager manager = new PropertyFileTestManager(args[0]);
		try{
			manager.kickOff();
			TestReporter.generateReport(manager);
			log.info("It's done, anyway");
		}catch(Exception e){
			log.error(e.getMessage(),e);
			System.exit(1);
		}
	}
}
