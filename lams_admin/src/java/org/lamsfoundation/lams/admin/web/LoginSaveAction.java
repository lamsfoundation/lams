/****************************************************************
 * Copyright (C) 2006 LAMS Foundation (http://lamsfoundation.org)
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

package org.lamsfoundation.lams.admin.web;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;

/**
 * <p>
 * <a href="LoginSaveAction.java.html"><i>View Source</i><a>
 * </p>
 *
 * Use DispatchAction for future extension convenience, e.g. add preview feature
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */

/**
 * struts doclet
 *
 * @struts.action path = "/loginsave" name = "LoginMaintainForm" parameter =
 *                "method" scope = "request" input = ".loginmaintian" validate =
 *                "false"
 *
 * @struts.action-forward name="sysadmin" path="/sysadminstart.do"
 * @struts.action-forward name="loginmaintain" path=".loginmaintain"
 */
public class LoginSaveAction extends LamsDispatchAction {

    private static final String IMAGE_FOLDER_SUFFIX = File.separatorChar + "lams-www.war" + File.separatorChar
	    + "images";

    private static final String NEWS_PAGE_PATH_SUFFIX = File.separatorChar + "lams-www.war" + File.separatorChar
	    + "news.html";

    private static final String LOGO_FILENAME = "lams_login.gif";

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	if (isCancelled(request)) {
	    return mapping.findForward("sysadmin");
	}

	DynaActionForm loginMaintainForm = (DynaActionForm) form;
	ActionMessages errors = new ActionMessages();
	FormFile file = (FormFile) loginMaintainForm.get("logo");
	if ((file != null) && (file.getFileSize() != 0)) {
	    checkFile(errors, file);
	}
	if (errors.isEmpty()) {
	    if ((file != null) && (file.getFileSize() != 0)) {
		updateImageFile(file, LOGO_FILENAME);

	    }
	    updateNewsPage(loginMaintainForm.getString("news"));
	} else {
	    saveErrors(request, errors);
	    return mapping.findForward("loginmaintain");
	}
	return mapping.findForward("sysadmin");
    }

    private void updateNewsPage(String news) throws IOException {
	BufferedWriter bWriter = null;
	try {
	    OutputStreamWriter ow = new OutputStreamWriter(
		    new FileOutputStream(Configuration.get(ConfigurationKeys.LAMS_EAR_DIR) + NEWS_PAGE_PATH_SUFFIX),
		    Charset.forName("UTF-8"));
	    bWriter = new BufferedWriter(ow);
	    bWriter.write(news);
	    bWriter.flush();
	} finally {
	    if (bWriter != null) {
		bWriter.close();
	    }
	}
    }

    private void updateImageFile(FormFile file, String fileName) throws IOException {
	File imagesFolder = new File(Configuration.get(ConfigurationKeys.LAMS_EAR_DIR) + IMAGE_FOLDER_SUFFIX);
	if (!imagesFolder.exists()) {
	    imagesFolder.mkdir();
	}
	String imageFilePath = Configuration.get(ConfigurationKeys.LAMS_EAR_DIR) + IMAGE_FOLDER_SUFFIX
		+ File.separatorChar + fileName;
	FileOutputStream out = null;
	try {
	    out = new FileOutputStream(imageFilePath);
	    out.write(file.getFileData());
	    out.flush();
	} finally {
	    if (out != null) {
		out.close();
	    }
	}

    }

    private void checkFile(ActionMessages errors, FormFile file) {
	boolean imgFormat = file.getContentType().contains("image");
	if (!imgFormat) {
	    errors.add("format", new ActionMessage("error.img.format"));
	}
	if (file.getFileSize() > 4096 * 1024) {
	    errors.add("size", new ActionMessage("error.img.size"));
	}
    }

}
