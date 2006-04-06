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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.tool.deploy;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
;
/**
 * Removes a web app entry from the LAMS Application XML. Throws an exception
 * if the element is not found.
 * 
 * @author Chris Perfect, with modifications by Fiona Malikoff
 */
public class RemoveWebAppFromApplicationXmlTask extends UpdateApplicationXmlTask
{
    
    
    /** Creates a new instance of RemoveWebAppFromApplicationXmlTask */
    public RemoveWebAppFromApplicationXmlTask()
    {
    }
    
    
    /**
     * Removes the web uri adn context root from the application xml
     */
    protected void updateApplicationXml(Document doc) throws DeployException
    {
        Element moduleElement = findElementWithWebURI(doc); 
        if (moduleElement == null)
        {
            throw new DeployException("No element found with text matching \""+doc.getElementsByTagName("web-uri")+"\"");
        } else {
            doc.getDocumentElement().removeChild(moduleElement);
        }

    }
    
}
