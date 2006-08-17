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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

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
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */

/**
 * struts doclet
 * 
 * @struts.action path = "/loginsave"
 *                name = "LoginMaintainForm"
 *                parameter = "method"
 *                scope = "request"
 *                input = ".loginmaintian"
 *                validate = "false"
 * 
 * @struts.action-forward name="sysadmin" path=".sysadmin"
 * @struts.action-forward name="loginmaintain" path=".loginmaintain"
 */
public class LoginSaveAction extends LamsDispatchAction {

	private static final String LOGO_TAG = "<img src=\"<lams:LAMSURL/>";
	
	public ActionForward save(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception{

		if(isCancelled(request)){
			return mapping.findForward("sysadmin");
		}

		DynaActionForm loginMaintainForm = (DynaActionForm)form;
		ActionMessages errors = new ActionMessages();
		FormFile file = (FormFile)loginMaintainForm.get("logo");
		if((file!=null)&&(file.getFileSize()!=0)){
			checkFile(errors, file);
		}
		if(errors.isEmpty()){
			if((file!=null)&&(file.getFileSize()!=0)){
				String fileName = fixBuggyFileName(file.getFileName());
				createImageFile(file, fileName);
				updateLoginPage(buildURL(fileName));
			}
			updateNewsPage(loginMaintainForm.getString("news"));
			String newsFilePath = Configuration.get(ConfigurationKeys.LAMS_EAR_DIR)
				+ File.separatorChar + "lams-www.war" + File.separatorChar + "news.html";			
			BufferedWriter bWriter = new BufferedWriter(new FileWriter(newsFilePath));
			bWriter.write(loginMaintainForm.getString("news"));
			bWriter.flush();
			bWriter.close();
		}else{
			saveErrors(request,errors);
			return mapping.findForward("loginmaintain");
		}
		return mapping.findForward("sysadmin");
	}
	
	private String fixBuggyFileName(String fileName) {
		int index = fileName.lastIndexOf('.');
		return fileName.replace(fileName.substring(0, index), "logo");
	}

	private void updateNewsPage(String news) throws IOException {
		String newsFilePath = Configuration.get(ConfigurationKeys.LAMS_EAR_DIR)
				+ File.separatorChar + "lams-central.war" + File.separatorChar
				+ "news.html";
		BufferedWriter bWriter = null;
		try{
			bWriter = new BufferedWriter(new FileWriter(newsFilePath));
			bWriter.write(news);
			bWriter.flush();
		}finally{
			if(bWriter != null)
			bWriter.close();
		}
	}

	private void updateLoginPage(String url) throws IOException {
		String loginPagePath = Configuration.get(ConfigurationKeys.LAMS_EAR_DIR)
			+ File.separatorChar + "lams-central.war" + File.separatorChar + "login.jsp";
		BufferedReader bReader = null;
		BufferedWriter bWriter = null;
		try{
			bReader = new BufferedReader(new FileReader(loginPagePath));
			StringBuilder source = new StringBuilder();
			String line = bReader.readLine();
			while(line != null){
				source.append(line).append('\n');
				line = bReader.readLine();
			}
			int index = source.indexOf(LOGO_TAG);
			log.debug("LAMS logo tag index "+index);
			if(index != -1){
				int startIndex = index + LOGO_TAG.length();
				int endIndex = source.indexOf("\"", startIndex);
				source.replace(startIndex, endIndex, url);
				bWriter = new BufferedWriter(new FileWriter(loginPagePath));
				bWriter.write(source.toString());
				bWriter.flush();
			}
		}finally{
			if(bReader!=null) bReader.close();
		}
	}

	private static String buildURL(String fileName) throws UnsupportedEncodingException {
		return "/www/images/" + fileName;
	}

	private void createImageFile(FormFile file, String fileName) throws IOException {
		String imagesFolderPath = Configuration
				.get(ConfigurationKeys.LAMS_EAR_DIR)
				+ File.separatorChar
				+ "lams-www.war"
				+ File.separatorChar
				+ "images";
		File imagesFolder = new File(imagesFolderPath);
		if (!imagesFolder.exists()) {
			imagesFolder.mkdir();
		}
		String imageFilePath = imagesFolderPath + File.separatorChar + fileName;
		FileOutputStream out = null;
		try{
			out = new FileOutputStream(imageFilePath);
			out.write(file.getFileData());
			out.flush();
		}finally{
			if(out != null) out.close();
		}

	}

	private void checkFile(ActionMessages errors, FormFile file){
		boolean imgFormat = file.getContentType().contains("image");
		if(!imgFormat){
			errors.add("format", new ActionMessage("error.img.format"));
		}
		if(file.getFileSize()>4096*1024){
			errors.add("size", new ActionMessage("error.img.size"));
		}
	}
	
}
