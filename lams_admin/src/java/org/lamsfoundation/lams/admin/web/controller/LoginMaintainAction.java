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
package org.lamsfoundation.lams.admin.web.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;

/**
 * <p>
 * <a href="LoginMaintainAction.java.html"><i>View Source</i><a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */

/**
 * struts doclets
 *
 *
 *
 *
 *
 *
 */
public class LoginMaintainAction extends Action {

    private static final String NEWS_PAGE_PATH_SUFFIX = File.separatorChar + "lams-www.war" + File.separatorChar
	    + "news.html";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	DynaActionForm loginMaintainForm = (DynaActionForm) form;
	loginMaintainForm.set("news", loadNews());
	return mapping.findForward("loginmaintain");
    }

    private String loadNews() throws IOException {
	BufferedReader bReader = null;
	try {
	    InputStreamReader ir = new InputStreamReader(
		    new FileInputStream(Configuration.get(ConfigurationKeys.LAMS_EAR_DIR) + NEWS_PAGE_PATH_SUFFIX),
		    Charset.forName("UTF-8"));
	    bReader = new BufferedReader(ir);
	    StringBuilder news = new StringBuilder();
	    String line = bReader.readLine();
	    while (line != null) {
		news.append(line).append('\n');
		line = bReader.readLine();
	    }
	    return news.toString();
	} finally {
	    if (bReader != null) {
		bReader.close();
	    }
	}
    }

}
