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
package org.lamsfoundation.lams.learning.export.web.action;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
import org.lamsfoundation.lams.learning.export.ExportPortfolioConstants;
import org.lamsfoundation.lams.learning.export.ExportPortfolioException;
/**
 * @author mtruong
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExportNotSupportedServlet extends AbstractExportPortfolioServlet {
    
    private static Logger log = Logger.getLogger(ExportNotSupportedServlet.class);
    private MessageResources resources = MessageResources.getMessageResources(ExportPortfolioConstants.MESSAGE_RESOURCE_CONFIG_PARAM);
	    
    private final String FILENAME = "export_unsupported.html";
    
    public String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies)
	{
        String htmlOutput = generateHtmlContent();	
        
        String absoluteFilePath = directoryName + File.separator + FILENAME;
           
		//writing the file to the temp export folder.	
        try
        {
			BufferedWriter fileout = new BufferedWriter(new FileWriter(absoluteFilePath));
			fileout.write(htmlOutput);
			fileout.close();
        }
        catch(IOException e)
        {
            throw new ExportPortfolioException(e);
        }
        
        return FILENAME;
        
	}
    
    private String generateHtmlContent()
    {
//        StringBuffer htmlPage = new StringBuffer();
//	    htmlPage.append("<html><head><title>Export Portfolio Unsupported</title></head>");
//	    htmlPage.append("<body><p>Export Portfolio for this activity is unsupported</p></body>");
//	    htmlPage.append("</html>");
//	    
//	    return htmlPage.toString();
    	return resources.getMessage(ExportPortfolioConstants.EXPORT_NOT_SUPPORTED_KEY);
    	
    }
   
   
    

}
