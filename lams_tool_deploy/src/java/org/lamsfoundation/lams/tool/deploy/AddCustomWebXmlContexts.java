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



package org.lamsfoundation.lams.tool.deploy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This class updates web.xml with the contexts of non-default lams tools, as of the 2.1 updater
 * 
 * @author lfoxton
 *
 */
public class AddCustomWebXmlContexts {

    /**
     * Add the given web contexts to the web.xml files
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

	if ((args.length < 4) || (args[0] == null)) {
	    throw new IllegalArgumentException(
		    "Usage: AddModuleToApplicationXmlTask <lams.ear path> <dbUser> <dbPass> <dbUrl>");
	}
	try {
	    // creating a list of default tools
	    List<String> defualtTools = new LinkedList<String>();
	    defualtTools.add("lafrum11");
	    defualtTools.add("lanb11");
	    defualtTools.add("laqa11");
	    defualtTools.add("lasbmt11");
	    defualtTools.add("larsrc11");
	    defualtTools.add("lavote11");
	    defualtTools.add("lasurv11");
	    defualtTools.add("lascrb11");
	    defualtTools.add("lantbk11");
	    defualtTools.add("lachat11");

	    AddCustomWebXmlContexts addWebXml = new AddCustomWebXmlContexts();

	    List<String[]> customToolContexts = addWebXml.getCustomToolContexts(defualtTools, args[1], args[2],
		    args[3]);

	    // Creating a list of war files to update
	    List<String> warFiles = new LinkedList<String>();
	    warFiles.add("lams-central.war");
	    warFiles.add("lams-learning.war");
	    warFiles.add("lams-monitoring.war");

	    addWebXml.addContexts(args[0], customToolContexts, warFiles);

	} catch (Exception ex) {
	    System.out.println("Application.xml update failed: " + ex.getMessage());
	    ex.printStackTrace();
	}
    }

    public AddCustomWebXmlContexts() {
    }

    /**
     * Adds the list of contexts to the each of the war files
     * 
     * @param eardir
     * @param contexts
     *            A list of contexts in String[] form, [tool application context path, tool jar file name]
     * @param warFiles
     *            A list of war files that need to be updated
     */
    public void addContexts(String eardir, List<String[]> contexts, List<String> warFiles) {
	InsertToolContextClasspathTask updateWebXmlTask = new InsertToolContextClasspathTask();
	updateWebXmlTask.setLamsEarPath(eardir);
	updateWebXmlTask.setArchivesToUpdate(warFiles);

	Iterator<String[]> it = contexts.iterator();
	while (it.hasNext()) {
	    String context[] = it.next();
	    updateWebXmlTask.setApplicationContextPath(context[0]);
	    updateWebXmlTask.setJarFileName(context[1]);
	    updateWebXmlTask.execute();
	}
    }

    /**
     * Gets a list of tool contexts from the database that arent part of the defual lams tools
     * 
     * @param defualtTools
     *            a list of default too signatures
     * @param dbUser
     * @param dbPass
     * @param dbUrl
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public List<String[]> getCustomToolContexts(List<String> defualtTools, String dbUser, String dbPass, String dbUrl)
	    throws SQLException, ClassNotFoundException {
	System.out.println("User: " + dbUser);
	System.out.println("Pass: " + dbPass);
	System.out.println("Url: " + dbUrl);

	Class.forName("com.mysql.cj.jdbc.Driver");
	Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
	conn.setAutoCommit(false);

	List<String[]> contexts = new ArrayList<String[]>();

	String queryStr = "SELECT context_file, classpath_addition FROM lams_tool WHERE ";
	Iterator<String> it = defualtTools.iterator();
	while (it.hasNext()) {
	    String toolSig = it.next();
	    queryStr += "tool_signature!=\"" + toolSig + "\" ";

	    if (it.hasNext()) {
		queryStr += "AND ";
	    }
	}

	System.out.println("Query string: " + queryStr);

	PreparedStatement query = conn.prepareStatement(queryStr);
	ResultSet results = query.executeQuery();
	while (results.next()) {
	    String context[] = new String[2];
	    context[0] = results.getString("context_file");
	    context[1] = results.getString("classpath_addition");
	    contexts.add(context);
	}

	return contexts;
    }

}
