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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.authoring.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtil;
import org.lamsfoundation.lams.web.action.LamsAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
/**
 *  * @struts.action name = "ImportAction"
 * 				  parameter = "method"
 * 				  validate = "false"
 * @struts.action-forward name = "upload" path = "/toolcontent/import.jsp"
 * @struts.action-forward name = "success" path = "/toolcontent/importresult.jsp"
 * 
 * Import tool content servlet. It needs an uploaded learning design zip file. 
 * @author Steve.Ni
 * 
 * @version $Revision$
 */
public class ImportToolContentAction extends LamsAction {

	private static final long serialVersionUID = 1L;
	public static final String EXPORT_TOOLCONTENT_SERVICE_BEAN_NAME = "exportToolContentService";
	public static final String USER_SERVICE_BEAN_NAME = "userManagementService";
	public static final String PARAM_LEARING_DESIGN_ID = "learningDesignID";
	public static final String ATTR_TOOLS_ERROR_MESSAGE = "toolsErrorMessages";
	public static final String ATTR_LD_ERROR_MESSAGE = "ldErrorMessages";
	public static final String ATTR_LD_ID = "learningDesignID";
	

	private Logger log = Logger.getLogger(ImportToolContentAction.class);
	

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String param = mapping.getParameter();
		//-----------------------Resource Author function ---------------------------
		if(param.equals("import")){
			importLD(request);
			return mapping.findForward("success");
		}else{
			//display initial page for upload
			return mapping.findForward("upload");
		}
	}


	/**
	 * @param request
	 */
	private void importLD(HttpServletRequest request) {
		List<String> ldErrorMsgs = new ArrayList<String>();
        try {
        	Integer workspaceFolderUid = null;
        	
        	//get shared session
			HttpSession ss = SessionManager.getSession();
			//get back login user DTO
			UserDTO userDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
			User user = (User)getUserService().findById(User.class,userDto.getUserID());
			
        	FileItem file = null;
        	Map params = new HashMap();
        	String filename = null;
        	
        	String uploadPath = FileUtil.createTempDirectory("_uploaded_learningdesing");

        	DiskFileUpload fu = new DiskFileUpload();
        	// maximum size that will be stored in memory
        	fu.setSizeThreshold(4096);
        	// the location for saving data that is larger than getSizeThreshold()
        	fu.setRepositoryPath(uploadPath);
        	
            List fileItems = fu.parseRequest(request);
            Iterator iter = fileItems.iterator();
            while (iter.hasNext()) {
                FileItem fi = (FileItem) iter.next();
                //UPLOAD_FILE is input field from HTML page
                if (!fi.getFieldName().equalsIgnoreCase("UPLOAD_FILE"))
                    params.put(fi.getFieldName(), fi.getString());
                else {
                    // filename on the client
                	filename = FileUtil.getFileName(fi.getName());
                    file = fi;
                }
                workspaceFolderUid = NumberUtils.createInteger((String) params.get("WORKSPACE_FOLDER_UID"));
            }
            if (file == null) {
            	String msg = "Can not find the upload file.";
            	log.error(msg);
            	throw new ExportToolContentException(msg);
            }
            // write the file
            String ldPath = ZipFileUtil.expandZip(file.getInputStream(),filename);
            IExportToolContentService service = getExportService();
            List<String> toolsErrorMsgs = new ArrayList<String>();
            Long ldId = service.importLearningDesign(ldPath,user,workspaceFolderUid,toolsErrorMsgs);
            if(ldId == -1){
            	String msg = "Learning design saved failed.";
            	throw new ExportToolContentException(msg);
        	}
            request.setAttribute(ATTR_TOOLS_ERROR_MESSAGE,toolsErrorMsgs);
            request.setAttribute(ATTR_LD_ID,ldId);
        } catch (Exception e) {
        	String msg = e.toString();
        	log.error(msg);
        	ldErrorMsgs.add(msg);
         	request.setAttribute(ATTR_LD_ERROR_MESSAGE,ldErrorMsgs);
		}
	}
	
	//***************************************************************************************
	// Private method
	//***************************************************************************************
	private IUserManagementService getUserService(){
		WebApplicationContext webContext = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServlet().getServletContext());
		return (IUserManagementService) webContext.getBean(USER_SERVICE_BEAN_NAME);		
	}
	private IExportToolContentService getExportService(){
		WebApplicationContext webContext = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServlet().getServletContext());
		return (IExportToolContentService) webContext.getBean(EXPORT_TOOLCONTENT_SERVICE_BEAN_NAME);		
	}

}
