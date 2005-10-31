package org.lamsfoundation.lams.web.filter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author mtruong
 *
 * This ResponseCaptureFilter takes the response that is normally sent
 * to the client, and outputs all the content into a file.
 * All servlets which this filter is applied, must set the request 
 * attribute "filename" to the value of the filename in which the
 * contents should be saved. For example, if I want to save the 
 * contents in the directory D:\portfolio\activityId23 with the filename
 * main.html then the request attribute filename must be
 * D:\portfolio\activityId23\main.html
 * 
 */
public class ResponseCaptureFilter implements Filter
{

	private static Logger log = Logger.getLogger(ResponseCaptureFilter.class);
	
	public void init(FilterConfig config) throws ServletException
	{} 
    public void destroy()
    {}

    public void doFilter(ServletRequest  request,
                         ServletResponse response,
                         FilterChain     chain) throws IOException, ServletException
	{
    	log.debug("Inside ResponseCaptureFilter");
    	
    	ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse)response);
        
        chain.doFilter(request, responseWrapper);
        
        String responseBody = responseWrapper.toString(); //output of the original response that was stored in the response wrapper
        String filename = (String)request.getAttribute(AttributeNames.PARAM_FILENAME);
       
        if (filename != null && !filename.equals(""))
        {
	       	FileWriter file = new FileWriter(filename);
	        
	        // System.out.println(responseWrapper.toString());
	  
	        file.write(responseBody);
	        file.close();
        }
        
        //output to browser
        PrintWriter out = response.getWriter();
        out.write(responseBody);
        
     }
    
  
}
