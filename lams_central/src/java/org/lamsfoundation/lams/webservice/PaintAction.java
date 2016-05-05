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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.webservice;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.util.Base64StringToImageUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;

/**
 * @author Paul Georges
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *         Currently this action class is unused, but we will retain it in the source for any future use
 *         with the Paint FCKEditor plugin.
 *
 */
public class PaintAction extends LamsDispatchAction {

    private static Logger logger = Logger.getLogger(PaintAction.class);

    /**
     * @deprecated
     */
    @Deprecated
    public ActionForward saveImage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	try {
	    String toolContentId = WebUtil.readStrParam(request, "toolContentId");
	    String userId = WebUtil.readStrParam(request, "userId");
	    String data = WebUtil.readStrParam(request, "data");

	    Date today = new Date();
	    String filename = String.valueOf(today.getTime());
	    String dir = File.separator + "lams-www.war" + File.separator + "secure" + File.separator + toolContentId
		    + File.separator + "Paint" + File.separator + userId + File.separator;

	    String absoluteFilePath = dir + filename + ".png";

	    File newPath = new File(dir);
	    newPath.mkdirs();

	    File newFile = new File(absoluteFilePath);
	    newFile.createNewFile();

	    boolean success = Base64StringToImageUtil.create(dir, filename, "png", data);

	    if (success) {
		writeAJAXResponse(response, absoluteFilePath);
	    } else {
		writeAJAXResponse(response, "");
	    }
	} catch (Exception e) {
	    writeAJAXResponse(response, e.getMessage());
	}

	return null;
    }
}
