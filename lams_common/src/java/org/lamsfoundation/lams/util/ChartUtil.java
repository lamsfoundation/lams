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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;


/**
 * Utility classes for JFreeChart. Ensures all our charts are of a consistent size. Depending on the size of the legend, the chart will be 
 * either 400x400 or 750x550.
 */
public class ChartUtil
{
	static Logger log = Logger.getLogger(ChartUtil.class);

    private static final Integer SMALL_WIDTH = new Integer(400);
    private static final Integer SMALL_HEIGHT = new Integer(400);
    private static final Integer LARGE_WIDTH = new Integer(750);
    private static final Integer LARGE_HEIGHT = new Integer(550);

    public static final String CHART_TYPE_PIE = "pie";
    public static final String CHART_TYPE_BAR = "bar";    
 
	   public static void outputPieChart(HttpServletResponse response, OutputStream out, String title, DefaultPieDataset data) throws IOException
	    {
	    	boolean useSmall = determineSize(data.getKeys());
	        
	    	JFreeChart chart=null;
	   	    chart=ChartFactory.createPieChart3D(title , data, true, true, false);
	   
			if ( chart != null ) {
	            response.setContentType("image/png");
	            if ( useSmall )
	            	ChartUtilities.writeChartAsPNG(out, chart, SMALL_WIDTH, SMALL_HEIGHT);
	            else 
	            	ChartUtilities.writeChartAsPNG(out, chart, LARGE_WIDTH, LARGE_HEIGHT);
	   	    } else {
	   	    	log.error("Unable to output chart. No chart returned by JFreeChart. Data "+data);
	   	    }
	    }
	    
	    public static void outputBarChart( HttpServletResponse response, OutputStream out, String title, DefaultCategoryDataset data, 
	    		String categoryAxisLabel, String valueAxisLabel ) throws IOException
	    {
	    	boolean useSmall = determineSize(data.getColumnKeys());
	        
	    	JFreeChart chart=ChartFactory.createBarChart3D(title , categoryAxisLabel, valueAxisLabel, 
	   	    									data, PlotOrientation.VERTICAL, true, true, false);

			if ( chart != null ) {
	            response.setContentType("image/png");
	            if ( useSmall )
	            	ChartUtilities.writeChartAsPNG(out, chart, SMALL_WIDTH, SMALL_HEIGHT);
	            else 
	            	ChartUtilities.writeChartAsPNG(out, chart, LARGE_WIDTH, LARGE_HEIGHT);
	   	    } else {
	   	    	log.error("Unable to output chart. No chart returned by JFreeChart. Data "+data);
	   	    }
	    }

	    private static boolean determineSize(Collection legendEntries) {
	    	int numLines = 0;
	    	int numInCurrentLine = 0;
	    	Iterator iter = legendEntries.iterator();
	    	while (iter.hasNext()) {
				Object object = (Object) iter.next();
				int textLength = object.toString().length();
				numInCurrentLine = numInCurrentLine + textLength + 3;
				if ( numInCurrentLine > 50) {
					numInCurrentLine = textLength;
					numLines++;
				}
			}
	    	return numLines < 4;
	    }
	    
}
