/*
 * Created on 7/02/2005
 *
 */
package org.lamsfoundation.lams.web.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.web.util.TokenProcessor;

/**
 * @author daveg
 *
 */
public abstract class Action extends org.apache.struts.action.Action {
    
    protected static String className = "Action";
	
    private static TokenProcessor token = TokenProcessor.getInstance();
    
    private static String LOG_NAME = "lams.web.action.Logger";
    /**
     * Logger used for action classes.
     * TODO: revisit logging.
     */
    protected static Logger log = Logger.getLogger(LOG_NAME);

	protected void saveToken(javax.servlet.http.HttpServletRequest request) {
        token.saveToken(request);
	}
	
	protected boolean isTokenValid(javax.servlet.http.HttpServletRequest request) {
        return token.isTokenValid(request, false);
	}
    
	protected boolean isTokenValid(javax.servlet.http.HttpServletRequest request, boolean reset) {
        return token.isTokenValid(request, reset);
	}
	
    protected void resetToken(HttpServletRequest request) {
        token.resetToken(request);
    }
    
    /*protected void saveForward(javax.servlet.http.HttpServletRequest request, ActionForward forward) {
    	token.saveForward(request, forward);
    }
    
    protected ActionForward getForward(javax.servlet.http.HttpServletRequest request) {
    	return token.getForward(request, true);
    }*/
    
}
