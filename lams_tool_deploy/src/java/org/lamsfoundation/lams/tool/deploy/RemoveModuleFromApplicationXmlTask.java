package org.lamsfoundation.lams.tool.deploy;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Removes a module entry from the LAMS Application XML. Throws an exception
 * if the element is not found.
 *
 * @author Luke Foxton
 */
public class RemoveModuleFromApplicationXmlTask extends UpdateApplicationXmlTask {

    /**
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) throws Exception {

	if ((args.length < 2) || (args[0] == null)) {
	    throw new IllegalArgumentException("Usage: RemoveModuleFromApplicationXmlTask <lams.ear path>");
	}
	try {
	    System.out.println("Attempting to update " + args[0] + "\\META-INF\\application.xml");
	    RemoveModuleFromApplicationXmlTask remModAppTask = new RemoveModuleFromApplicationXmlTask();
	    remModAppTask.setLamsEarPath(args[0]);
	    remModAppTask.setModule(args[1]);
	    remModAppTask.execute();
	    System.out.println("application.xml update completed");
	} catch (Exception ex) {
	    System.out.println("Application.xml update failed: " + ex.getMessage());
	    ex.printStackTrace();
	}
    }

    /** Creates a new instance of RemoveWebAppFromApplicationXmlTask */
    public RemoveModuleFromApplicationXmlTask() {
    }

    /**
     * Removes the module and context root from the application xml
     */
    @Override
    protected void updateApplicationXml(Document doc) throws DeployException {
	Element moduleElement = findElementWithModule(doc);
	if (moduleElement == null) {
	    System.out.println("No element found with text matching: " + module);
	    System.out.println("application.xml update completed");
	    System.exit(0);
	    //throw new DeployException("No element found with text matching \""+doc.getElementsByTagName("java")+"\"");
	} else {
	    System.out.println("Doc: " + moduleElement.compareDocumentPosition(doc));
	    System.out.println("Doc: " + doc.getDocumentElement().toString());
	    System.out.println("Module: " + moduleElement.getNodeName());
	    doc.getDocumentElement().removeChild(moduleElement);
	}
    }
}
