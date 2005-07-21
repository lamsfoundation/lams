/* 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

http://www.gnu.org/licenses/gpl.txt 
*/
package org.lamsfoundation.lams.web.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.actions.LookupDispatchAction;
import org.lamsfoundation.lams.web.util.TokenProcessor;

/**
 * Extension to the Struts LookupDispatchAction. Differences:
 * <OL>
 * <LI>Uses the LAMS TokenProcessor rather than the Struts TokenProcessor
 * Reason for this unknown as the coder who implemented it failed to say why!
 * </OL>
 * 
 * @author Fiona Malikoff
 */
public abstract class LamsLookupDispatchAction extends LookupDispatchAction {
    
    private static TokenProcessor token = TokenProcessor.getInstance();
    protected static Logger log = Logger.getLogger(LamsLookupDispatchAction.class);    

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
