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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Random;

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

	private static final String LOGO_TAG = "<img src=\"<lams:LAMSURL/>";

	private static final String ALPHABET = "ABCDEFGHIJKLMOPQRSTUWVXYZabcdefghijklmnopqrstuvwxyz_1234567890";

	private static final String IMAGE_FOLDER_SUFFIX = File.separatorChar 
			+ "lams-www.war" + File.separatorChar + "images";

	private static final String LOGIN_PAGE_PATH_SUFFIX = File.separatorChar
			+ "lams-central.war" + File.separatorChar + "login.jsp";

	private static final String NEWS_PAGE_PATH_SUFFIX = File.separatorChar
			+ "lams-www.war" + File.separatorChar + "news.html";

	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

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
				String fileName = fixBuggyFileName(file.getFileName());
				createImageFile(file, fileName);
				updateLoginPage(buildURL(fileName));
			}
			updateNewsPage(loginMaintainForm.getString("news"));
		} else {
			saveErrors(request, errors);
			return mapping.findForward("loginmaintain");
		}
		return mapping.findForward("sysadmin");
	}

	private String fixBuggyFileName(String fileName) {
		fileName = generateRandomFileName() + fileName.substring(fileName.lastIndexOf('.'));
		int i=0;
		while(new File(Configuration.get(ConfigurationKeys.LAMS_EAR_DIR) 
				+ IMAGE_FOLDER_SUFFIX + File.separatorChar + fileName).exists()){
			fileName = fileName.replace(".", new Integer(i).toString());
			i++;
		}
		log.debug("generated filename " + fileName);
		return fileName;
	}

	private String generateRandomFileName() {
		StringBuilder name = new StringBuilder();
		int length = 1 + new Random().nextInt(ALPHABET.length());
		for (int i = 0; i < length; i++) {
			name.append(ALPHABET.charAt(new Random().nextInt(ALPHABET.length())));
		}
		log.debug("generated random name " + name);
		return name.toString();
	}

	private void updateNewsPage(String news) throws IOException {
		BufferedWriter bWriter = null;
		try {
			OutputStreamWriter ow = new OutputStreamWriter(new FileOutputStream(
					Configuration.get(ConfigurationKeys.LAMS_EAR_DIR) + NEWS_PAGE_PATH_SUFFIX), 
					Charset.forName("UTF-8"));
			bWriter = new BufferedWriter(ow);
			bWriter.write(news);
			bWriter.flush();
		} finally {
			if (bWriter != null)
				bWriter.close();
		}
	}

	private void updateLoginPage(String url) throws IOException {
		BufferedReader bReader = null;
		BufferedWriter bWriter = null;
		try {
			bReader = new BufferedReader(new FileReader(
					Configuration.get(ConfigurationKeys.LAMS_EAR_DIR) + LOGIN_PAGE_PATH_SUFFIX));
			StringBuilder source = new StringBuilder();
			String line = bReader.readLine();
			while (line != null) {
				source.append(line).append('\n');
				line = bReader.readLine();
			}
			int index = source.indexOf(LOGO_TAG);
			if (index != -1) {
				int startIndex = index + LOGO_TAG.length();
				int endIndex = source.indexOf("\"", startIndex);
				source.replace(startIndex, endIndex, url);
				bWriter = new BufferedWriter(new FileWriter(
						Configuration.get(ConfigurationKeys.LAMS_EAR_DIR) + LOGIN_PAGE_PATH_SUFFIX));
				bWriter.write(source.toString());
				bWriter.flush();
			}
		} finally {
			if (bReader != null)
				bReader.close();
			if(bWriter != null)
				bWriter.close();
		}
	}

	private static String buildURL(String fileName)
			throws UnsupportedEncodingException {
		return "/www/images/" + fileName;
	}

	private void createImageFile(FormFile file, String fileName)
			throws IOException {
		File imagesFolder = new File(
				Configuration.get(ConfigurationKeys.LAMS_EAR_DIR) + IMAGE_FOLDER_SUFFIX);
		if (!imagesFolder.exists()) {
			imagesFolder.mkdir();
		}
		String imageFilePath = Configuration.get(ConfigurationKeys.LAMS_EAR_DIR) 
			+ IMAGE_FOLDER_SUFFIX + File.separatorChar + fileName;
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(imageFilePath);
			out.write(file.getFileData());
			out.flush();
		} finally {
			if (out != null)
				out.close();
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
