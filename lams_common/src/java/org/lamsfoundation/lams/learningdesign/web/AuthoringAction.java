/*
 * Created on Dec 7, 2004
 */
package org.lamsfoundation.lams.learningdesign.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 * @author manpreet
 */
public class AuthoringAction extends DispatchAction{

	public ActionForward saveLearningDesign(ActionMapping mapping,
								   ActionForm form,
								   HttpServletRequest req,
								   HttpServletResponse res)	throws IOException, 
								   							ServletException{
		return null;
	}
	
	public ActionForward getLearningDesign(ActionMapping mapping,
								   ActionForm form,
								   HttpServletRequest req,
								   HttpServletResponse res)throws IOException, 
		   							ServletException{
		return null;
	}	
	
}
