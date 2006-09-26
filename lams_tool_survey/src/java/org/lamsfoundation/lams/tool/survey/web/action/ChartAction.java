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
package org.lamsfoundation.lams.tool.survey.web.action;

import static org.lamsfoundation.lams.tool.survey.SurveyConstants.ATTR_QUESTION_UID;
import static org.lamsfoundation.lams.tool.survey.SurveyConstants.CHART_TYPE;
import static org.lamsfoundation.lams.tool.survey.SurveyConstants.ERROR_MSG_CHART_ERROR;
import static org.lamsfoundation.lams.tool.survey.SurveyConstants.MSG_BARCHART_CATEGORY_AXIS_LABEL;
import static org.lamsfoundation.lams.tool.survey.SurveyConstants.MSG_BARCHART_TITLE;
import static org.lamsfoundation.lams.tool.survey.SurveyConstants.MSG_BARCHART_VALUE_AXIS_LABEL;
import static org.lamsfoundation.lams.tool.survey.SurveyConstants.MSG_OPEN_RESPONSE;
import static org.lamsfoundation.lams.tool.survey.SurveyConstants.MSG_PIECHART_TITLE;
import static org.lamsfoundation.lams.tool.survey.SurveyConstants.QUESTION_TYPE_TEXT_ENTRY;
import static org.lamsfoundation.lams.tool.survey.SurveyConstants.SURVEY_SERVICE;
import static org.lamsfoundation.lams.tool.survey.SurveyConstants.OPTION_SHORT_HEADER;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.lamsfoundation.lams.tool.survey.model.SurveyOption;
import org.lamsfoundation.lams.tool.survey.model.SurveyQuestion;
import org.lamsfoundation.lams.tool.survey.service.ISurveyService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
/**
 * Display chart image by request.
 * 
 * @author Steve.Ni
 * 
 * @version $Revision$
 */
public class ChartAction extends  Action {
    
	static Logger logger = Logger.getLogger(ChartAction.class.getName());
	
	private MessageResources resource;
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		resource = getResources(request);
        OutputStream out= response.getOutputStream(); 
        
        String type= WebUtil.readStrParam(request, CHART_TYPE);
        Long sessionId= WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
        Long questionUid= WebUtil.readLongParam(request, ATTR_QUESTION_UID);
        
    	ISurveyService service = getSurveyService();
		
		SurveyQuestion question =service.getQuestionResponse(sessionId,questionUid);
		if(question.getType() == QUESTION_TYPE_TEXT_ENTRY){
			logger.error("Error question type : Text entry can not generate chart.");
			response.getWriter().print(resource.getMessage(ERROR_MSG_CHART_ERROR));
			return null;
		}
			
		//Try to create chart
        JFreeChart chart=null;
    	if (type.equals("pie")){
    	    chart = createPieChart (question);    
    	}else if (type.equals("bar")){
    		chart = createBarChart (question);    
    	}
    	
        //send chart to response output stream
        if (chart != null){
            response.setContentType("image/png");
            ChartUtilities.writeChartAsPNG(out, chart, 400, 300);
            return null;
        }else{
			response.getWriter().print(resource.getMessage(ERROR_MSG_CHART_ERROR));
			return null;
        }
     
    }


    public JFreeChart createPieChart(SurveyQuestion question){
    
        DefaultPieDataset data= new DefaultPieDataset();
        
        Set<SurveyOption> options = question.getOptions();
		int optIdx = 1;
		for (SurveyOption option : options) {
			data.setValue(OPTION_SHORT_HEADER + optIdx, (Number) option.getResponse());
			optIdx++;
  		}
          
          if(question.isAppendText())
          	 data.setValue(resource.getMessage(MSG_OPEN_RESPONSE), (Number)question.getOpenResponse());
          
        
    	JFreeChart chart=null;
   	    chart=ChartFactory.createPieChart3D(resource.getMessage(MSG_PIECHART_TITLE,question.getSequenceId()) , data, true, true, false);

   	    return chart;
    }
    
    public JFreeChart createBarChart(SurveyQuestion question){
        
    	DefaultCategoryDataset data= new DefaultCategoryDataset();
      
        Set<SurveyOption> options = question.getOptions();
        int optIdx = 1;
        for (SurveyOption option : options) {
            data.setValue((Number)option.getResponse(), OPTION_SHORT_HEADER + optIdx, OPTION_SHORT_HEADER + optIdx);
            optIdx++;
		}
        
        if(question.isAppendText())
        	 data.setValue((Number)question.getOpenResponse(), resource.getMessage(MSG_OPEN_RESPONSE), resource.getMessage(MSG_OPEN_RESPONSE));
        
    	JFreeChart chart=null;
    	
    	
   	    chart=ChartFactory.createBarChart3D(resource.getMessage(MSG_BARCHART_TITLE,question.getSequenceId()), 
   	    									resource.getMessage(MSG_BARCHART_CATEGORY_AXIS_LABEL), 
   	    									resource.getMessage(MSG_BARCHART_VALUE_AXIS_LABEL), 
   	    									data, PlotOrientation.VERTICAL, true, true, false);
   	    return chart;
    }
	//*************************************************************************************
	// Private method 
	//*************************************************************************************
	/**
	 * Return SurveyService bean.
	 */
	private ISurveyService getSurveyService() {
	      WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
	      return (ISurveyService) wac.getBean(SURVEY_SERVICE);
	}
    
}
