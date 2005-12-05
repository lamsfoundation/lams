/*
 * Created on 5/12/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
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

import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
import org.lamsfoundation.lams.learning.export.ExportPortfolioException;
/**
 * @author mtruong
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExportNotSupportedServlet extends AbstractExportPortfolioServlet {
    
    private static Logger log = Logger.getLogger(ExportNotSupportedServlet.class);
    
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
        StringBuffer htmlPage = new StringBuffer();
	    htmlPage.append("<html><head><title>Export Portfolio Unsupported</title></head>");
	    htmlPage.append("<body><p>Export Portfolio for this activity is unsupported</p></body>");
	    htmlPage.append("</html>");
	    
	    return htmlPage.toString();
    }
   
   
    

}
