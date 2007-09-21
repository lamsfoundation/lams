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
package org.lamsfoundation.lams.tool.mc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.lamsfoundation.lams.tool.OutputFactory;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McOptsContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueUsr;
import org.lamsfoundation.lams.tool.mc.pojos.McSession;
import org.lamsfoundation.lams.tool.mc.pojos.McUsrAttempt;
import org.lamsfoundation.lams.tool.mc.web.LearningUtil;

public class MCOutputFactory extends OutputFactory {

	protected static final String OUTPUT_NAME_LEARNER_MARK = "learner.mark";
	protected static final String OUTPUT_NAME_LEARNER_ALL_CORRECT = "learner.all.correct";
	
	/** 
	 * @see org.lamsfoundation.lams.tool.OutputDefinitionFactory#getToolOutputDefinitions(java.lang.Object)
	 */
	public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject)  {
		
		TreeMap<String, ToolOutputDefinition> definitionMap =  new TreeMap<String, ToolOutputDefinition>();
		ToolOutputDefinition definition = buildBooleanOutputDefinition(OUTPUT_NAME_LEARNER_ALL_CORRECT);
		definitionMap.put(OUTPUT_NAME_LEARNER_ALL_CORRECT, definition);

		if ( toolContentObject != null ) {
			McContent content = (McContent) toolContentObject;
			
			definition = buildRangeDefinition(OUTPUT_NAME_LEARNER_MARK,
					new Long(0),
					new Long ( content.getTotalMarksPossible().longValue() ) );
			definitionMap.put(OUTPUT_NAME_LEARNER_MARK, definition);
		} else {
			log.error("Unable to build content based output definitions for Multiple Choice as no tool content object supplied. Only including the definitions that do not need any content.");
		}

		return definitionMap;
	}

	public SortedMap<String, ToolOutput> getToolOutput(List<String> names, IMcService mcService, Long toolSessionId, Long learnerId) {
		
		McSession session = mcService.findMcSessionById(toolSessionId);
		McQueUsr queUser = mcService.getMcUserBySession(learnerId, session.getUid());

		TreeMap<String,ToolOutput> output = new TreeMap<String, ToolOutput>();
		if ( names == null || names.contains(OUTPUT_NAME_LEARNER_MARK) ) {
			output.put(OUTPUT_NAME_LEARNER_MARK, getLearnerMark(queUser) );
		}
		if ( names == null || names.contains(OUTPUT_NAME_LEARNER_ALL_CORRECT) ) {
			output.put(OUTPUT_NAME_LEARNER_ALL_CORRECT, getLearnerAllCorrect(mcService, queUser) );
		}
		return output;
	}

	public ToolOutput getToolOutput(String name, IMcService mcService, Long toolSessionId, Long learnerId) {
		if ( name != null ) {
			McSession session = mcService.findMcSessionById(toolSessionId);
			McQueUsr queUser = mcService.getMcUserBySession(learnerId, session.getUid());

			if ( name.equals(OUTPUT_NAME_LEARNER_MARK) ) {
				return getLearnerMark(queUser);
			} else if ( name.equals(OUTPUT_NAME_LEARNER_ALL_CORRECT) ){
				return getLearnerAllCorrect(mcService, queUser);
			}
		}
		return null;
	}

	/**
	 * Get the mark for a specific user. This gets the mark associated with the last attempt.
	 * Will always return a ToolOutput object.
	 */
	private ToolOutput getLearnerMark(McQueUsr queUser) {
		Long mark = queUser != null ? queUser.getLastAttemptTotalMark() : new Long(0);
		return new ToolOutput(MCOutputFactory.OUTPUT_NAME_LEARNER_MARK, 
				getDescription(MCOutputFactory.OUTPUT_NAME_LEARNER_MARK), mark);
	}

	/**
	 * Did the user get the questions all correct. This checks the answers associated with the last attempt.
	 * Assumes all correct if the mark is equal to the maximum possible mark.
	 * Will always return a ToolOutput object.
	 */
	private ToolOutput getLearnerAllCorrect(IMcService mcService, McQueUsr queUser) {
		boolean allCorrect = allQuestionsCorrect(mcService, queUser);
		return new ToolOutput(MCOutputFactory.OUTPUT_NAME_LEARNER_ALL_CORRECT, 
				getDescription(MCOutputFactory.OUTPUT_NAME_LEARNER_ALL_CORRECT), allCorrect);
	}

	// written to cope with more than one correct option for each question but only tested with
	// one correct option for a question.
    private boolean allQuestionsCorrect(IMcService mcService, McQueUsr queUser) {

    	// Build a list of all the correct answers. If we hit any options that are not a correct option 
    	// we can abort as we know there is a wrong answer.
    	// Otherwise count the number of correct options overall (for comparison later).
    	long correctlearnerOptions = 0;
    	List<McUsrAttempt> latestAttempts = (List<McUsrAttempt>) mcService.getLatestAttemptsForAUser(queUser.getUid()); 
    	for ( McUsrAttempt mcUsrAttempt : latestAttempts ) {
    		McOptsContent mcOptsContent = mcUsrAttempt.getMcOptionsContent();
    		if ( ! mcOptsContent.isCorrectOption() ) {
    			// wrong answer so no point going any further
    			return false;
    		} else {
    			correctlearnerOptions++;
    		}
    	}

    	// now count the overall number of correct options
    	long correctOptions = 0;
    	McContent mcContent = queUser.getMcSession().getMcContent();
    	Iterator questionIterator = mcContent.getMcQueContents().iterator(); 
    	while (questionIterator.hasNext())
    	{
    		McQueContent mcQueContent = (McQueContent) questionIterator.next();
    		Iterator optionIterator = mcQueContent.getMcOptionsContents().iterator();
    		while ( optionIterator.hasNext() ) {
    			McOptsContent mcOptsContent = (McOptsContent) optionIterator.next();
    			if ( mcOptsContent.isCorrectOption() ) {
    				correctOptions++;
    			}
    		}
    	}

    	// We know the user didn't get everything wrong, but did they answer enough options correctly?
    	// This case is used when there is more than one correct option for each answer. Simple way, compare counts!
    	return correctOptions == correctlearnerOptions;
    }

}
