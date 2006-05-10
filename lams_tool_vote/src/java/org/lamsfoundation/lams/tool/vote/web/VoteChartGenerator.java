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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
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
import org.jfree.data.general.DefaultPieDataset;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;

/**
 * @author Ozgur Demirtas
 * generates JFreeCharts 
 */
public class VoteChartGenerator extends HttpServlet implements VoteAppConstants {
    static Logger logger = Logger.getLogger(VoteChartGenerator.class.getName());
    
    public VoteChartGenerator(){
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        logger.debug("dispatching doGet for VoteChartGenerator...");
        
        logger.debug("MAP_VOTERATES_CONTENT Map: " + request.getSession().getAttribute(MAP_VOTERATES_CONTENT));
        logger.debug("MAP_OPTIONS_CONTENT Map: " + request.getSession().getAttribute(MAP_OPTIONS_CONTENT));
        OutputStream out= response.getOutputStream(); 
        
        try{
            String type=request.getParameter("type");
            logger.debug("type: " + type);
            JFreeChart chart=null;
            
            if (type.equals("pie"))
            {
                logger.debug("creating pie chart");
                chart=createPieChart(request);
            }
            else if (type.equals("bar"))
            {
                logger.debug("creating bar chart");
                chart=createBarChart();
            }
            
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
    
    private JFreeChart createPieChart(HttpServletRequest request)
    {
        logger.debug("starting createPieChart...");
        DefaultPieDataset data= new DefaultPieDataset();
        
        Map mapNominationsContent=(Map)request.getSession().getAttribute(MAP_OPTIONS_CONTENT);
        logger.error("mapNominationsContent: " + mapNominationsContent);
        
        Map mapVoteRatesContent=(Map)request.getSession().getAttribute(MAP_VOTERATES_CONTENT);
        logger.error("mapVoteRatesContent: " + mapVoteRatesContent);

        Iterator itMap = mapNominationsContent.entrySet().iterator();
    	while (itMap.hasNext()) 
    	{
        	Map.Entry pairs = (Map.Entry)itMap.next();
            logger.debug("using the  nomination content pair: " +  pairs.getKey() + " = " + pairs.getValue());
            
            String voteRate=(String) mapVoteRatesContent.get(pairs.getKey());
            logger.debug("voteRate:" + voteRate);
            data.setValue(pairs.getValue().toString(), new Double(voteRate));
		}
        
        JFreeChart chart=ChartFactory.createPieChart("Session Votes Chart", data, true, true, false);
        return chart;
        
    }


    private JFreeChart createBarChart()
    {
        return null;
    }
	
}
