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
package org.lamsfoundation.lams.workspace.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.dto.OrganisationDTO;

public class OrganisationDTOFactory {

	/** Convert the given list of organisation objects to organisationDTOs, and put in the organisationDTOs set.
	 * If organisationDTOs is null, then creates a new set. This allows the call function to build up 
	 * an overall set of organisations more efficiently.
	 * 
	 * Returns the organisationDTOs set. 
	 * @param organisations
	 * @param organisationDTOs
	 */
	public static Set<OrganisationDTO> convertToDTOs(List<Organisation> organisations, Set<OrganisationDTO> organisationDTOs) {
		Set<OrganisationDTO> newDTOs = organisationDTOs != null ? organisationDTOs : new HashSet<OrganisationDTO>();
		Iterator iterator = organisations.iterator();
		
		while (iterator.hasNext()) {
			Organisation organisation = (Organisation) iterator.next();
			newDTOs.add(organisation.getOrganisationDTO());
		}
		
		return newDTOs;

	}
	/** Convert the given list of organisation DTOs to a hierarchy of OrganisationDTOs. */
	public static OrganisationDTO createTree(Collection<OrganisationDTO> orgs, OrganisationDTO root){
		Vector<OrganisationDTO> neworgs = new Vector<OrganisationDTO>();
		OrganisationDTO rootOrgDTO = root;
		Iterator it = orgs.iterator();
		while(it.hasNext()){
			if(rootOrgDTO == null) {
				OrganisationDTO rt = new OrganisationDTO(new Integer(-1), new Integer(-1), "Root", "Root Description");
				OrganisationDTO initial = (OrganisationDTO) it.next();
				rt.addNode(initial);
				rootOrgDTO = rt;
			} else {
				OrganisationDTO organisationDTO = (OrganisationDTO) it.next();
				
				OrganisationDTO parent = findParent(rootOrgDTO, organisationDTO);
				if(parent!=null) {
					rootOrgDTO = parent;
				} else {
					neworgs.add(organisationDTO);
				}
			}
		}
		
		if(neworgs.size() > 0){
			if(orgs.size() == neworgs.size())
				rootOrgDTO.addNodes(orgs);
			else
				return createTree(neworgs, rootOrgDTO);
		}
		
		return rootOrgDTO;
	}
	
	private static OrganisationDTO findParent(OrganisationDTO root, OrganisationDTO tmp){
		if(root.getOrganisationID().equals(tmp.getParentID())){
			root.addNode(tmp);
			return root;
		} else if(root.getParentID().equals(tmp.getOrganisationID())){
			tmp.addNode(root);
			return tmp;
		} else {
			// check to see if can add to tree
			if(checkNodes(root, tmp))
				return root;
			else
				return null;
		}
	}
	
	private static boolean checkNodes(OrganisationDTO root, OrganisationDTO tmp){
		Vector nodes = root.getNodes();
		if(nodes.size() > 0){
			Iterator it = nodes.iterator();
			while(it.hasNext()){
				OrganisationDTO child = (OrganisationDTO) it.next();
				
				if(child.getOrganisationID().equals(tmp.getParentID())) {
					child.addNode(tmp);
					return true;
				} else if(child.getParentID().equals(tmp.getOrganisationID())) {
					OrganisationDTO temp = child;
					it.remove();
					tmp.addNode(temp);
					root.addNode(tmp);
					return true;
				} else {
					if(checkNodes(child, tmp))
						return true;
				}
			}
			
		}
		
		return false;
	}

}
