/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.tool.sbmt.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.sbmt.InstructionFiles;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.dto.AuthoringDTO;
import org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService;
import org.lamsfoundation.lams.tool.sbmt.service.SubmitFilesServiceProxy;
import org.lamsfoundation.lams.tool.sbmt.util.SbmtConstants;
import org.lamsfoundation.lams.util.WebUtil;
/**
 * 
 * @author Steve.Ni
 * 
 * $version$
 * 
 * @struts.action 
 * 			path="/deletefile"
 * 			parameter="method"
 * 
 */
public class DeleteFileAction extends DispatchAction {
	private Logger log = Logger.getLogger(DeleteFileAction.class);

	public ActionForward deleteOfflineFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return deleteFile(request, response,IToolContentHandler.TYPE_OFFLINE);
	}
	public ActionForward deleteOnlineFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return deleteFile(request, response,IToolContentHandler.TYPE_ONLINE);
	}

	/**
	 * @param request
	 * @param response
	 * @param type 
	 * @return
	 */
	private ActionForward deleteFile(HttpServletRequest request, HttpServletResponse response, String type) {
		Long contentID = new Long(WebUtil.readLongParam(request,SbmtConstants.TOOL_CONTENT_ID));
		Long versionID = new Long(WebUtil.readLongParam(request,"versionID"));
		Long uuID = new Long(WebUtil.readLongParam(request,"uuID"));
		
		ISubmitFilesService service = SubmitFilesServiceProxy.getSubmitFilesService(this
				.getServlet().getServletContext());

		service.deleteFromRepository(uuID,versionID);
		service.deleteInstructionFile(contentID,uuID,versionID,type);
		SubmitFilesContent content = service.getSubmitFilesContent(contentID);
		AuthoringDTO authorDto = new AuthoringDTO(content);
		List list;
		if(StringUtils.equals(type,IToolContentHandler.TYPE_OFFLINE))
			list = authorDto.getOfflineFiles();
		else
			list = authorDto.getOnlineFiles();
		Iterator iter = list.iterator();
		StringBuffer sb = new StringBuffer();
		while(iter.hasNext()){
			InstructionFiles file = (InstructionFiles) iter.next();
			sb.append("<li>").append(file.getName()).append("\r\n");
			sb.append(" <a href=\"javascript:launchInstructionsPopup('download/?uuid=").append(file.getUuID()).append("&preferDownload=false')\">");
			sb.append(this.getResources(request).getMessage("label.view"));
			sb.append("</a>\r\n");
			sb.append(" <a href=\"../download/?uuid=").append(file.getUuID()).append("&preferDownload=true\">");
			sb.append(this.getResources(request).getMessage("label.download"));
			sb.append("</a>\r\n");
			sb.append("<a href=\"javascript:loadDoc('/lams/tool/lasbmt11/deletefile.do?method=");
			if(StringUtils.equals(type,IToolContentHandler.TYPE_OFFLINE))
				sb.append("deleteOffline");
			else
				sb.append("deleteOnline");
			sb.append("File&toolContentID=").append(contentID);
			sb.append("&uuID=").append(file.getUuID()).append("&versionID=").append(file.getVersionID()).append("','");
			if(StringUtils.equals(type,IToolContentHandler.TYPE_OFFLINE))
				sb.append("offlinefile");
			else
				sb.append("onlinefile");
			sb.append("')\">");
			
			if(StringUtils.equals(type,IToolContentHandler.TYPE_OFFLINE))
				sb.append(this.getResources(request).getMessage("label.authoring.offline.delete"));
			else
				sb.append(this.getResources(request).getMessage("label.authoring.online.delete"));
			sb.append("</a></li>\r\n");
		}
		try {
			PrintWriter out = response.getWriter();
			out.print(sb.toString());
			out.flush();
		} catch (IOException e) {
			log.error(e);
		}
		return null;
	}
		
}
