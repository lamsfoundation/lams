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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/**
 * Helper program for updater.nsi
 *
 * Uses the InsertToolContextClasspath to change the context paths in the
 * manifest and web.xmls for lams-learning.war, lams-central.war and
 * lams-monitoring.war
 *
 * Instead of reading the context paths from the deploy.xml of each tool, this
 * program reads from the db, so as not to overwrite existing entries for an
 * update
 *
 * @author Luke Foxton
 */

package org.lamsfoundation.lams.tool.deploy;

import java.util.LinkedList;
import java.util.List;

public class UpdateToolContextPath {
    public static void main(String[] args) throws Exception {
	if ((args.length < 1) || (args[0] == null)) {
	    throw new IllegalArgumentException("Usage: UpdateToolContextPath <deploy.xml path>");
	}

	try {
	    DeployToolConfig config = new DeployToolConfig(null, args[0]);

	    // the tool signature of this tool
	    String toolSig = config.getToolSignature();

	    ToolDBUpdater dbUpdater = new ToolDBUpdater();
	    dbUpdater.setDbUsername(config.getDbUsername());
	    dbUpdater.setDbPassword(config.getDbPassword());
	    dbUpdater.setDbDriverClass(config.getDbDriverClass());
	    dbUpdater.setDbDriverUrl(config.getDbDriverUrl());
	    dbUpdater.setToolSignature(toolSig);

	    // the path to the existing ear
	    String lamsEar = config.getLamsEarPath();

	    // tool application context path
	    String appContextPath = dbUpdater.queryTool(toolSig, "context_file");

	    // tool jar file name
	    String jarFileName = dbUpdater.queryTool(toolSig, "classpath_addition");

	    if ((appContextPath.equals("ERROR")) || (jarFileName.equals("ERROR"))) {
		throw new Exception("Could not read context details from lams_tool");
	    }

	    System.out.println("appContextpath: " + appContextPath);
	    System.out.println("jarFileName: " + jarFileName);

	    // the war files that need to be updated
	    List<String> warFiles = new LinkedList<String>();
	    warFiles.add("lams-central.war");
	    warFiles.add("lams-learning.war");
	    warFiles.add("lams-monitoring.war");

	    // updater the conf files in the respective wars
	    InsertToolContextClasspathTask updateWebXmlTask = new InsertToolContextClasspathTask();
	    updateWebXmlTask.setLamsEarPath(lamsEar);
	    updateWebXmlTask.setArchivesToUpdate(warFiles);
	    updateWebXmlTask.setApplicationContextPath(appContextPath);
	    updateWebXmlTask.setJarFileName(jarFileName);
	    updateWebXmlTask.execute();

	} catch (Exception e) {
	    System.out.println("Unable to read deploy.xml: " + e.getMessage());
	    e.printStackTrace();
	}

    }
}
