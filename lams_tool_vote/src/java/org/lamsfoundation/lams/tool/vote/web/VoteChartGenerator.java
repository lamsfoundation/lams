/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.vote.web;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.VoteGeneralMonitoringDTO;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.service.VoteServiceProxy;

/**
 * <p> Enables generation of JFreeCharts </p>
 * 
 * @author Ozgur Demirtas
 *  
 */
public class VoteChartGenerator extends HttpServlet implements VoteAppConstants {
    static Logger logger = Logger.getLogger(VoteChartGenerator.class.getName());
    public static final String CHART_TYPE_PIE = "pie";
    public static final String CHART_TYPE_BAR = "bar";
    
    public VoteChartGenerator(){
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        logger.debug("dispatching doGet for VoteChartGenerator...");
        
        logger.debug("test6: MAP_STANDARD_NOMINATIONS_CONTENT: " + request.getSession().getAttribute(MAP_STANDARD_NOMINATIONS_CONTENT));
		logger.debug("test6: MAP_STANDARD_RATES_CONTENT: " + request.getSession().getAttribute(MAP_STANDARD_RATES_CONTENT));
        OutputStream out= response.getOutputStream(); 
        
        try{
            String type=request.getParameter("type");
            logger.debug("type: " + type);
            
            String currentSessionId=request.getParameter("currentSessionId");
            logger.debug("currentSessionId: " + currentSessionId);
            logger.debug("getServletContext() is : " + getServletContext());
            
            if (currentSessionId != null)
            {
                logger.debug("currentSessionId is specified, generating data for all sessions dto: ");

                IVoteService voteService = VoteServiceProxy.getVoteService(getServletContext());
        		logger.debug("voteService: " + voteService);
        		
        		VoteSession voteSession=voteService.retrieveVoteSession(new Long(currentSessionId));
        		logger.debug("voteSession uid:" + voteSession.getUid());
        		
        		VoteContent voteContent=voteSession.getVoteContent(); 
        		logger.debug("existing voteContent:" + voteContent);

                VoteGeneralMonitoringDTO voteGeneralMonitoringDTO=new VoteGeneralMonitoringDTO();
                MonitoringUtil.prepareChartData(request, voteService, null, voteContent.getVoteContentId().toString(), 
                        voteSession.getUid().toString(), null, voteGeneralMonitoringDTO);
                logger.debug("creating maps MAP_STANDARD_NOMINATIONS_CONTENT and MAP_STANDARD_RATES_CONTENT: " + currentSessionId);

                logger.debug("post prepareChartData : MAP_STANDARD_NOMINATIONS_CONTENT: " + request.getSession().getAttribute(MAP_STANDARD_NOMINATIONS_CONTENT));
        		logger.debug("post prepareChartData : MAP_STANDARD_RATES_CONTENT: " + request.getSession().getAttribute(MAP_STANDARD_RATES_CONTENT));
            }
            
            
            JFreeChart chart=null;
            
            logger.debug("creating pie chart" + type);
            chart=createChart(request, type);
            
            logger.debug("chart:" + chart);
            if (chart != null)
            {
                response.setContentType("image/png");
                ChartUtilities.writeChartAsPNG(out, chart, 400, 300);
            }
                    
        }
        catch(Exception e)
        {
            logger.error("error occurred generating chart: " + e);
        }
        finally
        {
            out.close();
        }
    }
    
    
    public JFreeChart createChart(HttpServletRequest request, String type)
    {
    	if (type.equals(CHART_TYPE_PIE))
    	{
    	    return createPieChart (request);    
    	}
    	else 
    	{
    	    return createBarChart (request);    
    	}
    }

    public JFreeChart createPieChart(HttpServletRequest request)
    {
        logger.debug("starting createPieChart...");
        DefaultPieDataset data= new DefaultPieDataset();
        
        Map mapNominationsContent=(Map)request.getSession().getAttribute(MAP_STANDARD_NOMINATIONS_CONTENT);
        Map mapVoteRatesContent=(Map)request.getSession().getAttribute(MAP_STANDARD_RATES_CONTENT);
        if ( mapNominationsContent == null || mapNominationsContent == null ) {
        	logger.debug("No voting data, unable to create pie chart");
        	return null;
        }

        Iterator itMap = mapNominationsContent.entrySet().iterator();
    	while (itMap.hasNext()) 
    	{
        	Map.Entry pairs = (Map.Entry)itMap.next();
            String voteRate=(String) mapVoteRatesContent.get(pairs.getKey());
            data.setValue(pairs.getValue().toString(), new Double(voteRate));
		}
        
    	JFreeChart chart=null;
   	    chart=ChartFactory.createPieChart3D(SESSION_VOTES_CHART , data, true, true, false);
   	   
   	    if ( logger.isDebugEnabled() ) {
   	    	logger.debug("chart: " + chart+" data: "+data) ;
   	    }
   	    
   	    return chart;
    }
    
    public JFreeChart createBarChart(HttpServletRequest request)
    {
        logger.debug("starting createBarChart...");
        DefaultCategoryDataset data= new DefaultCategoryDataset();
        
        Map mapNominationsContent=(Map)request.getSession().getAttribute(MAP_STANDARD_NOMINATIONS_CONTENT);
        Map mapVoteRatesContent=(Map)request.getSession().getAttribute(MAP_STANDARD_RATES_CONTENT);
        if ( mapNominationsContent == null || mapNominationsContent == null ) {
        	logger.debug("No voting data, unable to create pie chart");
        	return null;
        }

        Iterator itMap = mapNominationsContent.entrySet().iterator();
    	while (itMap.hasNext()) 
    	{
        	Map.Entry pairs = (Map.Entry)itMap.next();
            String voteRate=(String) mapVoteRatesContent.get(pairs.getKey());
            data.setValue(new Double(voteRate), pairs.getValue().toString(), pairs.getValue().toString());
		}
        
    	JFreeChart chart=null;
   	    chart=ChartFactory.createBarChart3D(SESSION_VOTES_CHART , "Open Vote", "Percentage", 
   	    									data, PlotOrientation.VERTICAL, true, true, false);

   	    if ( logger.isDebugEnabled() ) {
   	    	logger.debug("chart: " + chart+" data: "+data) ;
   	    }

   	    return chart;
    }
    
}
