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
package org.lamsfoundation.lams.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsAction;

/**
 * @author lfoxton
 * 
 * Takes a relative path as a parameter and redirects the user there after they
 * have been authenticated. This allows direct linking to learner and monitor
 * from outside sources. It must be done through central as it is the only
 * project that offers this functionality
 * 
 * Usage r.do?URL=<relURL>
 * where rekURL = the URL-encoded relative LAMS url 
 * 
 * @struts:action path="/r" validate="false"
 * @struts:action-forward name="error" path=".error"
 * 
 */
public class RedirectAction extends LamsAction {

    private static Logger log = Logger.getLogger(RedirectAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
	    throws Exception {
	try {
	    String relativePath = WebUtil.readStrParam(req, CentralConstants.PARAM_REDIRECT_URL);

	    res.sendRedirect(Configuration.get(ConfigurationKeys.SERVER_URL) + relativePath);
	    return null;
	} catch (Exception e) {
	    log.error("Failed redirect to url", e);
	    return mapping.findForward("error");
	}
    }
}