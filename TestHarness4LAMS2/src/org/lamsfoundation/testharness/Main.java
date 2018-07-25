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
 * @author Fei Yang, Marcin Cieslak
 *
 */
public class Main {

    private static final Logger log = Logger.getLogger(Main.class);

    static {
	// configure log4j
	PropertyConfigurator.configure("log.properties");
	// configure HttpUnit
	// stops Javascript parsing, it's not needed
	HttpUnitOptions.setScriptingEnabled(false);
	// WebClient.getResponse does not throw an exception when it receives an error status.
	HttpUnitOptions.setExceptionsThrownOnErrorStatus(false);
    }

    public static void main(String[] args) {
	Main.log.info("Starting...");
	if (args.length == 0) {
	    Main.log.error("No argument supplied! Please refer to readme.txt");
	    System.exit(1);
	}

	// create test manager and hand over the control to him.
	TestManager manager = new TestManager(args[0]);
	try {
	    manager.start();
	    TestReporter.generateReportLog(manager);
	    TestReporter.generateReportFile(manager);
	    Main.log.info("Test suite finished");
	} catch (Exception e) {
	    Main.log.error("Error in tests", e);
	    System.exit(1);
	}
    }
}