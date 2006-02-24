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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.contentrepository.AccessDeniedException;
import org.lamsfoundation.lams.contentrepository.FileException;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.contentrepository.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.NodeKey;


/** 
 * MyEclipse Struts
 * Creation date: 01-13-2005
 * 
 * The exceptions will be handled by struts but are listed explicitly
 * here so (1) I can log them and (2) you can see what exceptions are thrown.
 * 
 * XDoclet definition:
 * @struts:action path="/nodeSelection" 
 * 				name="nodeSelectionForm" 
 * 				input="/nodeSelection.jsp" 
 * 				scope="request" 
 * 				parameter="method"
 * @struts:action-forward name="nodelist" path="/nodeSelection.jsp"
 * @struts:action-forward name="error" path="/error.jsp"
 * @struts:action-forward name="packagelist" path="/packageList.jsp"
 */
public class NodeSelectionAction extends RepositoryDispatchAction {

	protected Logger log = Logger.getLogger(NodeSelectionAction.class);

    /** 
	 * Get the list of nodes in the workspace. Displays the node list
	 * screen.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
     * @throws AccessDeniedException
     * @throws ItemNotFoundException
     * @throws FileException
	 */
	public ActionForward getList(ActionMapping mapping, 
        ActionForm form,
        HttpServletRequest request, 
        HttpServletResponse response) throws AccessDeniedException, ItemNotFoundException, FileException  {
		
    	ITicket ticket = getTicket(request);
		log.debug("In getList, ticket is "+ticket);
		if ( ticket == null ) {
			log.error("Ticket missing from session");
        	return returnError(mapping, request, "error.noTicket");
		} 
		
		// get the map of nodes and put it in the request,
		// ready to be rendered by the jsp. This isn't "normal" - 
		// usually the app will have a list of all its nodes
		// and know what they are. This getNodeList method
		// was put in just to write this demo. So its not an 
		// efficient method, and should not be used by a real 
		// application.
		log.debug("Getting node map");
		SortedMap map = null;
		try {
			map = getRepository().getNodeList(ticket);
		} catch (AccessDeniedException e) {
			log.error("Not allowed to do this exception occured ",e);
			throw e;
		}
		log.debug("Got map, map is "+map);
		
		// Normally, the application will have their own details about
		// all the nodes but we don't, so go back and get the real
		// node details, and only list (on the JSP) the direct file
		// nodes and the package nodes - not the file nodes within
		// the package nodes. 
		ArrayList jspList = new ArrayList();
		if ( map != null ) {

			Iterator iter = map.keySet().iterator();
			
			while (iter.hasNext()) {
				Long uuid = (Long) iter.next();
				
				IVersionedNode node;
				try {
					
					node = getRepository().getFileItem(ticket, uuid, null);
					
				} catch (AccessDeniedException e) {
					log.error("Not allowed to do this exception occured ",e);
					throw e;
				} catch (ItemNotFoundException e) {
					log.error("Item not found exception occured ",e);
					throw e;
				} catch (FileException e) {
					log.error("File exception occured ",e);
					throw e;
				}
				
				if ( ! node.hasParentNode() ) {
					jspList.add(node);
					jspList.add(map.get(uuid));
				}
			}
		}
		request.setAttribute(NODE_LIST_NAME, jspList);

		log.debug("Added map to request, forwarding to "+mapping.findForward("nodelist"));
		return mapping.findForward("nodelist");

	}

    /** 
	 * Get the list of nodes in the workspace. Displays the node list
	 * screen.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
     * @throws AccessDeniedException
     * @throws ItemNotFoundException
     * @throws FileException
	 */
	public ActionForward viewPackage(ActionMapping mapping, 
        ActionForm form,
        HttpServletRequest request, 
        HttpServletResponse response) throws AccessDeniedException, ItemNotFoundException, FileException  {
		
    	ITicket ticket = getTicket(request);
		log.debug("In getList, ticket is "+ticket);
		if ( ticket == null ) {
			log.error("Ticket missing from session");
        	return returnError(mapping, request,"error.noTicket");
		}
		
		Long uuid = getLong(request.getParameter(UUID_NAME));
		Long version = getLong(request.getParameter(VERSION_NAME));

		if ( uuid == null ) {
			log.error("UUID missing");
        	return returnError(mapping, request,"error.uuidMissing");
		} 

		log.debug("Deleting node "+uuid+" version "+version);

		List packageList = null;
		try {
			
			packageList = getRepository().getPackageNodes(ticket, uuid, version);
			request.setAttribute(PACKAGE_LIST, packageList);
			
		} catch (AccessDeniedException e) {
			log.error("Not allowed to do this exception occured ",e);
			throw e;
		} catch (ItemNotFoundException e) {
			log.error("Item not found exception occured ",e);
			throw e;
		}
		
		log.debug("Added map to request, forwarding to "+mapping.findForward("packagelist"));
		return mapping.findForward("packagelist");
	}
	
    /** 
	 * Delete a version of a node, or the whole node.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
     * @throws ItemNotFoundException
     * @throws InvalidParameterException
     * @throws AccessDeniedException
     * @throws Exception
	 */
	public ActionForward deleteNode(ActionMapping mapping, 
        ActionForm form,
        HttpServletRequest request, 
        HttpServletResponse response) 
	
		throws ItemNotFoundException, InvalidParameterException, 
				AccessDeniedException, FileException  {

    	ITicket ticket = getTicket(request);
		log.debug("In deleteNode, ticket is "+ticket);
		if ( ticket == null ) {
			log.error("Ticket missing from session");
        	return returnError(mapping, request, "error.noTicket");
		} 
		
		Long uuid = getLong(request.getParameter(UUID_NAME));
		Long version = getLong(request.getParameter(VERSION_NAME));
	
		if ( uuid == null ) {
			log.error("UUID missing");
        	return returnError(mapping, request, "error.uuidMissing");
		} 


		log.debug("Deleting node "+uuid+" version "+version);

		try {
			if ( version == null ) {
				String[] problemFiles = getRepository().deleteNode(ticket,uuid);
				log.info("Deleted node, "
						+(problemFiles==null||problemFiles.length==0?0:problemFiles.length)
						+" problem files were encountered.");
			} else {
				String[] problemFiles = getRepository().deleteVersion(ticket,uuid,version);
			}
			
			log.debug("Deleted nodes, forwarding to list");
			return getList(mapping,form,request,response);
			
		} catch (AccessDeniedException e) {
			log.error("Not allowed to do this exception occured ",e);
			throw e;
		} catch (ItemNotFoundException e) {
			log.error("Item not found exception occured ",e);
			throw e;
		}

	}

    /** 
	 * Copy a version of a node.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
     * @throws ItemNotFoundException
     * @throws InvalidParameterException
     * @throws AccessDeniedException
     * @throws Exception
	 */
	public ActionForward copyNode(ActionMapping mapping, 
        ActionForm form,
        HttpServletRequest request, 
        HttpServletResponse response) 
	
		throws ItemNotFoundException, InvalidParameterException, 
				AccessDeniedException, FileException  {

    	ITicket ticket = getTicket(request);
		log.debug("In copyNode, ticket is "+ticket);
		if ( ticket == null ) {
			log.error("Ticket missing from session");
        	return returnError(mapping, request, "error.noTicket");
		} 
		
		Long uuid = getLong(request.getParameter(UUID_NAME));
		Long version = getLong(request.getParameter(VERSION_NAME));
	
		if ( uuid == null ) {
			log.error("UUID missing");
        	return returnError(mapping, request, "error.uuidMissing");
		} 


		log.debug("Copy node "+uuid+" version "+version);

		try {
			NodeKey key = getRepository().copyNodeVersion(ticket,uuid,version);
			log.info("Copy node, new ids "+key);
			return getList(mapping,form,request,response);

		} catch (AccessDeniedException e) {
			log.error("Not allowed to do this exception occured ",e);
			throw e;
		} catch (ItemNotFoundException e) {
			log.error("Item not found exception occured ",e);
			throw e;
		}

	}
}