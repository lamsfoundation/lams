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
package org.lamsfoundation.lams.usermanagement.dto;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

public class OrganisationDTOFactory {

	/** Convert the given list of organisation DTOs to a hierarchy of OrganisationDTOs.
	 * 
	 * @param orgs	Collection of Organisation DTO's 
	 * @param root  Root Organisation DTO 
	 * @return OrganisationDTO tree
	 */
	public static OrganisationDTO createTree(Collection<OrganisationDTO> orgs){
		
		int count = 0;								// bystander node count
		OrganisationDTO rootOrgDTO = null;			// root DTO holder
		
		Iterator it = orgs.iterator();
		
		while(it.hasNext()){
			if(rootOrgDTO == null) {
				/** create dummy root for tree and add first element as child node */
				OrganisationDTO rt = new OrganisationDTO(new Integer(-1), new Integer(-1), "Root", "Root Description", new Integer(1));
				OrganisationDTO initial = (OrganisationDTO) it.next();
				rt.addNode(initial);
				rootOrgDTO = rt;
			} else {
				/** position the DTO in the tree */
				OrganisationDTO organisationDTO = (OrganisationDTO) it.next();
				OrganisationDTO parent = findParent(rootOrgDTO, organisationDTO);
				
				if(parent!=null) {
					/** save amended tree */
					rootOrgDTO = parent;
				} else {
					/** make bystander node */
					rootOrgDTO.addNode(organisationDTO);
					count++;
				}
			}
		}
		
		/** Move bystander nodes to correct position in tree */
		if(count>0){
			Vector<OrganisationDTO> nodes = rootOrgDTO.getNodes();
			Iterator i = nodes.iterator();
			while(i.hasNext()){
				OrganisationDTO dto = (OrganisationDTO) i.next();
				OrganisationDTO parent = findParent(rootOrgDTO, dto);
				if(parent!= null){
					/** remove bystander node and save amended tree */
					i.remove();
					rootOrgDTO = parent;
				}
			}
		}
		
		return rootOrgDTO;
	}
	
	/**
	 *  Find the correct position for the DTO node in the tree
	 * 
	 * 
	 * @param root Dummy root DTO
	 * @param tmp DTO to position
	 * @return The amended organisation tree if adjustment made
	 */
	private static OrganisationDTO findParent(OrganisationDTO root, OrganisationDTO tmp){
			/** check the root's child nodes
			 * 	note: root here is substitute root */ 
			if(checkNodes(root, tmp))
				return root;
			else
				return null;
	}
	
	/**
	 * Check if passed in node is a child node of the root's child nodes.
	 * 
	 * @param root Root DTO
	 * @param tmp DTO to position
	 * @return If added to branch returns true, otherwise returns false
	 */
	private static boolean checkNodes(OrganisationDTO root, OrganisationDTO tmp){
		if(root.equals(tmp))
			return false;
		
		/** child DTO nodes */
		Vector nodes = root.getNodes();
		
		if(nodes.size() > 0){
			Iterator it = nodes.iterator();
			while(it.hasNext()){
				OrganisationDTO child = (OrganisationDTO) it.next();
					if(child.getOrganisationID().equals(tmp.getParentID())) {
						/** added as child node */ 
						child.addNode(tmp);
						return true;
					} else if(child.getParentID().equals(tmp.getOrganisationID())) {
						/** added as parent node of child node and child of root */
						OrganisationDTO temp = child;
						it.remove();
						tmp.addNode(temp);
						root.addNode(tmp);
						return true;
					} else {
						/** check nodes of child */
						if(checkNodes(child, tmp))
							return true;
					}
			}
			
		}
		
		return false;
	}

}
