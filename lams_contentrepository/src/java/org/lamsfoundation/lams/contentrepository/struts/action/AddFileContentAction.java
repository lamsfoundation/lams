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

package org.lamsfoundation.lams.contentrepository.struts.action;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.contentrepository.AccessDeniedException;
import org.lamsfoundation.lams.contentrepository.FileException;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.struts.form.AddFileContentForm;


/** 
 * MyEclipse Struts
 * Creation date: 11-30-2004
 * 
 * XDoclet definition:
 * @struts:action path="/addFileContent" 
 * 				name="addFileContentForm" 
 * 				input="nodeSelection.jsp" 
 * 				scope="request" 
 * 				validate="false" 
 * 				parameter="method"
 * @struts:action-forward name="success" path="/nodeSelection.do?method=getList"
 * @struts:action-forward name="error" path="/error.jsp"
 */
public class AddFileContentAction extends RepositoryDispatchAction {

	// --------------------------------------------------------- Instance Variables

	// --------------------------------------------------------- Methods

	/** 
	 * Uploads a single file.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws FileException
	 * @throws AccessDeniedException
	 * @throws InvalidParameterException
	 * @throws ItemNotFoundException
	 */
	public ActionForward uploadFile(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) throws FileException, AccessDeniedException, InvalidParameterException, ItemNotFoundException {
		
    	ITicket ticket = getTicket(request);
		log.debug("In getNode, ticket is "+ticket);
		if ( ticket == null ) {
			log.error("Ticket missing from session");
        	return returnError(mapping, request, "error.noTicket");
		} 
		
		AddFileContentForm addFileContentForm = (AddFileContentForm) form;
		
		FormFile theFile = addFileContentForm.getTheFile();
		String filename = theFile.getFileName();
		String contentType = theFile.getContentType();
		String versionDescription = addFileContentForm.getDescription();

		Long uuid = addFileContentForm.getUuid();
		
		if ( log.isDebugEnabled() ) {
			log.debug("Adding file content "+filename+" type "+contentType
					+" version description "+versionDescription
					+" for uuid "+uuid);
		}

		InputStream is;
		try {
			is = theFile.getInputStream();
		} catch (Exception e) {
			log.error("Error getting file from input form.", e);
	       	return returnError(mapping, request, "exception.file");
		} 

		
		// exception will be handled by struts but listed explicitly
		// here so (1) I can log them and (2) you can see what
		// exceptions are thrown.
		try {
			NodeKey nodeKey = null;
			if ( uuid != null ) {
				// create a new version of this node
				nodeKey = getRepository().updateFileItem(ticket, uuid, filename,
						is, contentType, versionDescription);
			} else {
				// create a new node
				nodeKey = getRepository().addFileItem(ticket, is, filename, 
						contentType, versionDescription);
			}

			// normally the application would store the node key and
			// various details in their own tables. 

		} catch (FileException e1) {
			log.error("FileException occured ",e1);
			throw e1;
		} catch (AccessDeniedException e1) {
			log.error("Not allowed to do this exception occured ",e1);
			throw e1;
		} catch (InvalidParameterException e1) {
			log.error("Parameter missing exception occured ",e1);
			throw e1;
		} catch (ItemNotFoundException e1) {
			log.error("Item Not Found exception occured ",e1);
			throw e1;
		}

		log.debug("File Uploaded, forwarding to "+mapping.findForward(SUCCESS_PATH));
		return mapping.findForward(SUCCESS_PATH);
	}

	/** 
	 * Uploads a package
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws FileException
	 * @throws AccessDeniedException
	 * @throws InvalidParameterException
	 * @throws ItemNotFoundException
	 */
	public ActionForward uploadPackage(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) throws FileException, AccessDeniedException, InvalidParameterException, ItemNotFoundException {
		
    	ITicket ticket = getTicket(request);
		log.debug("In getNode, ticket is "+ticket);
		if ( ticket == null ) {
			log.error("Ticket missing from session");
        	return returnError(mapping, request,"error.noTicket");
		} 
		
		AddFileContentForm addFileContentForm = (AddFileContentForm) form;

		String dirName = addFileContentForm.getDirName();
		String entryString = addFileContentForm.getEntryString();
		String versionDescription = addFileContentForm.getDescription();

		Long uuid = addFileContentForm.getUuid();
		
		if ( log.isDebugEnabled() ) {
			log.debug("Adding package content "+dirName+" start point "+entryString
					+" version description "+versionDescription
					+" for uuid "+uuid);
		}

		// exception will be handled by struts but listed explicitly
		// here so (1) I can log them and (2) you can see what
		// exceptions are thrown.
		try {
			// if entryString is null, the repository will set it to index.html
			
			NodeKey nodeKey = null;
			if ( uuid != null ) {
				// create a new version of this node
				nodeKey = getRepository().updatePackageItem(ticket, uuid, dirName,
						entryString, versionDescription);
			} else {
				// create a new node
				nodeKey = getRepository().addPackageItem(ticket,dirName, entryString, versionDescription);
			}
			
			// normally the application would store the node key and
			// various details in their own tables. 
			
		} catch (FileException e1) {
			log.error("FileException occured ",e1);
			throw e1;
		} catch (AccessDeniedException e1) {
			log.error("Not allowed to do this exception occured ",e1);
			throw e1;
		} catch (InvalidParameterException e1) {
			log.error("Parameter missing exception occured ",e1);
			throw e1;
		} catch (ItemNotFoundException e1) {
			log.error("Item Not Found exception occured ",e1);
			throw e1;
		}

		log.debug("File Uploaded, forwarding to "+mapping.findForward(SUCCESS_PATH));
		return mapping.findForward(SUCCESS_PATH);
	}


}