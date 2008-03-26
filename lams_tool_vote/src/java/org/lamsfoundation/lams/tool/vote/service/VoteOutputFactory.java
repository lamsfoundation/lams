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

/* $Id$ */
package org.lamsfoundation.lams.tool.vote.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.lamsfoundation.lams.learningdesign.BranchCondition;
import org.lamsfoundation.lams.tool.OutputFactory;
import org.lamsfoundation.lams.tool.OutputType;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.vote.VoteUtils;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUsrAttempt;

public class VoteOutputFactory extends OutputFactory {

	protected static final String OUTPUT_NAME_NOMINATION_SELECTION = "learner.selection";
	
	/** 
	 * @see org.lamsfoundation.lams.tool.OutputDefinitionFactory#getToolOutputDefinitions(java.lang.Object)
	 */
	public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject)  {
		
		TreeMap<String, ToolOutputDefinition> definitionMap =  new TreeMap<String, ToolOutputDefinition>();

		if ( toolContentObject != null ) {

			VoteContent content = (VoteContent) toolContentObject;
			
			if ( content.getMaxNominationCount() != null && ! content.getMaxNominationCount().equals("1") ) {
				log.error("Unable to build output definitions for Voting if the user can have more than one nomination. Vote "+content);
			} else if ( content.isAllowText() ) {
				log.error("Unable to build output definitions for Voting if the user can enter free text. Vote "+content);
			} else {
			
				ToolOutputDefinition definition = buildLongOutputDefinition(OUTPUT_NAME_NOMINATION_SELECTION);
				if ( definition.getDefaultConditions() == null )
					definition.setDefaultConditions(new ArrayList<BranchCondition>());
				
				List<BranchCondition> defaultConditions = definition.getDefaultConditions();
				int min = 1;
				int max = 1;
				
				Iterator iter = content.getVoteQueContents().iterator();
				while ( iter.hasNext() ) {
					VoteQueContent nomination = (VoteQueContent) iter.next();
					int displayOrder = nomination.getDisplayOrder();
					if ( displayOrder < min ) min = displayOrder;
					if ( displayOrder > max ) max = displayOrder;
					String displayOrderAsString = new Long(displayOrder).toString();
					defaultConditions.add(new BranchCondition(null, null, new Integer(1), OUTPUT_NAME_NOMINATION_SELECTION, 
							VoteUtils.stripHTML(nomination.getQuestion()), 
							OutputType.OUTPUT_LONG.toString(),
							displayOrderAsString, 
							displayOrderAsString, 
							null));
				}
				definition.setStartValue(new Long(min));
				definition.setEndValue(new Long(max));
				definition.setShowConditionNameOnly(Boolean.TRUE);

				definitionMap.put(OUTPUT_NAME_NOMINATION_SELECTION, definition);
				
			}
		} else {
			log.error("Unable to build output definitions for Vote as no tool content object supplied.");
		}

		return definitionMap;
	}

	public SortedMap<String, ToolOutput> getToolOutput(List<String> names, IVoteService voteService, Long toolSessionId, Long learnerId) {
		
		VoteSession session = voteService.findVoteSessionById(toolSessionId);
		VoteQueUsr queUser = voteService.getVoteUserBySession(learnerId, session.getUid());

		TreeMap<String,ToolOutput> output = new TreeMap<String, ToolOutput>();
		if ( names == null || names.contains(OUTPUT_NAME_NOMINATION_SELECTION) ) {
			output.put(OUTPUT_NAME_NOMINATION_SELECTION, getDisplayOrderOfVoteQueContent(queUser) );
		}
		return output;
	}

	public ToolOutput getToolOutput(String name, IVoteService voteService, Long toolSessionId, Long learnerId) {
		if ( name != null ) {
			VoteSession session = voteService.findVoteSessionById(toolSessionId);
			VoteQueUsr queUser = voteService.getVoteUserBySession(learnerId, session.getUid());

			if ( name.equals(OUTPUT_NAME_NOMINATION_SELECTION) ) {
				return getDisplayOrderOfVoteQueContent(queUser);
			} 
		}
		return null;
	}
	
	/**
	 * Get the selected nomination for this user.
	 */
	private ToolOutput getDisplayOrderOfVoteQueContent(VoteQueUsr queUser) {
		
		if ( queUser != null ) {
			Set voteAttempts = queUser.getVoteUsrAttempts();
			if ( voteAttempts.size() > 0 ) {
				if ( voteAttempts.size() > 1)
					log.error("Attempting to match on nomination, but more than one nomination selected for this user. Taking first nomination which isn't a free text entry. User "+queUser);

				Iterator iter = voteAttempts.iterator();
				while ( iter.hasNext() ) {
					VoteUsrAttempt attempt = (VoteUsrAttempt) iter.next();					
					// VoteQueContentId == 1 indicates that it is a free text entry
					if ( attempt.getVoteQueContentId().longValue() != 1) {
						VoteQueContent nomination = attempt.getVoteQueContent();
						return new ToolOutput(OUTPUT_NAME_NOMINATION_SELECTION, getDescription(OUTPUT_NAME_NOMINATION_SELECTION), new Long(nomination.getDisplayOrder()));
					}
				}
			}
		}
		return null;
	}



}
