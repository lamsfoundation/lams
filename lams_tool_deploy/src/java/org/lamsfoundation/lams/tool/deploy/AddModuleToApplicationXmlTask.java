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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class AddModuleToApplicationXmlTask extends UpdateApplicationXmlTask {
    /** Creates a new instance of AddWebAppToApplicationXmlTask */
    public AddModuleToApplicationXmlTask() {
    }

    /**
     * Add the web uri and context root elements to the Application xml
     */
    @Override
    protected void updateApplicationXml(Document doc) throws DeployException {
	Element moduleElement = findElementWithModule(doc);
	if (moduleElement != null) {
	    doc.getDocumentElement().removeChild(moduleElement);
	}

	//create new module
	moduleElement = doc.createElement("module");
	Element javaElement = doc.createElement("java");
	javaElement.appendChild(doc.createTextNode(module));
	moduleElement.appendChild(javaElement);

	doc.getDocumentElement().appendChild(moduleElement);

    }

    /**
     * Add the given module to the application.xml file
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

	if ((args.length < 2) || (args[0] == null)) {
	    throw new IllegalArgumentException("Usage: AddModuleToApplicationXmlTask <lams.ear path> <module>");
	}
	try {
	    System.out.println("Attempting to update " + args[0] + "/META-INF/application.xml");
	    AddModuleToApplicationXmlTask addModuleTask = new AddModuleToApplicationXmlTask();
	    addModuleTask.setLamsEarPath(args[0]);
	    addModuleTask.setModule(args[1]);
	    addModuleTask.execute();
	    System.out.println("application.xml update completed");

	} catch (Exception ex) {
	    System.out.println("Application.xml update failed: " + ex.getMessage());
	    ex.printStackTrace();
	}
    }
}
