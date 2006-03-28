/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
/* $$Id$$ */
package org.lamsfoundation.lams.tool.deploy;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Task to add a web application entry to an EAR application XML
 * @author chris
 */
public class AddWebAppToApplicationXmlTask extends UpdateApplicationXmlTask
{
    
    
    
    /** Creates a new instance of AddWebAppToApplicationXmlTask */
    public AddWebAppToApplicationXmlTask()
    {
    }
    
    /**
     * Add the web uri and context root elements ot the Application xml
     */
    protected void updateApplicationXml(Document doc) throws DeployException
    {
        
        //find & remove web uri element
        Element moduleElement = findElementWithWebURI(doc); 
        if ( moduleElement != null ) {
            doc.getDocumentElement().removeChild(moduleElement);
        }

        //create new module
        moduleElement = doc.createElement("module");
        Element webElement = doc.createElement("web");
        moduleElement.appendChild(webElement);
        //create new web-uri element in the web element
        Element webUriElement = doc.createElement("web-uri");
        webUriElement.appendChild(doc.createTextNode(webUri));
        webElement.appendChild(webUriElement);
        
        //create new context root element in the web element
        Element contextRootElement = doc.createElement("context-root");
        contextRootElement.appendChild(doc.createTextNode(contextRoot));
        webElement.appendChild(contextRootElement);
        
        doc.getDocumentElement().appendChild(moduleElement);
        
    }
    
}
