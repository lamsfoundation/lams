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
import org.lamsfoundation.lams.tool.SimpleURL;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUsrAttempt;
import org.lamsfoundation.lams.tool.vote.util.VoteUtils;

public class VoteOutputFactory extends OutputFactory {

    protected static final String OUTPUT_NAME_NOMINATION_SELECTION = "learner.selection";
    protected static final int FREE_TEXT_NOM_SELECTION = 0;
    protected static final String FREE_TEXT_NOM_SELECTION_STR = "0";

    /**
     * @see org.lamsfoundation.lams.tool.OutputDefinitionFactory#getToolOutputDefinitions(java.lang.Object)
     */
    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject,
	    int definitionType) {

	TreeMap<String, ToolOutputDefinition> definitionMap = new TreeMap<String, ToolOutputDefinition>();

	if (toolContentObject != null) {

	    VoteContent content = (VoteContent) toolContentObject;

	    if (content.getMaxNominationCount() != null && !content.getMaxNominationCount().equals("1")) {
		log.error(
			"Unable to build output definitions for Voting if the user can have more than one nomination. Vote "
				+ content);
	    } else {

		ToolOutputDefinition definition = buildBooleanSetOutputDefinition(
			VoteOutputFactory.OUTPUT_NAME_NOMINATION_SELECTION);
		if (definition.getDefaultConditions() == null) {
		    definition.setDefaultConditions(new ArrayList<BranchCondition>());
		}

		List<BranchCondition> defaultConditions = definition.getDefaultConditions();
		String trueString = Boolean.TRUE.toString();
		int conditionOrderId = 1;

		if (content.isAllowText()) {
		    defaultConditions.add(new BranchCondition(null, null, new Integer(conditionOrderId++),
			    buildConditionName(VoteOutputFactory.OUTPUT_NAME_NOMINATION_SELECTION,
				    VoteOutputFactory.FREE_TEXT_NOM_SELECTION_STR),
			    getI18NText("label.open.vote", false), OutputType.OUTPUT_BOOLEAN.toString(), null, null,
			    trueString));
		}

		Iterator iter = content.getVoteQueContents().iterator();
		while (iter.hasNext()) {
		    VoteQueContent nomination = (VoteQueContent) iter.next();
		    int displayOrder = nomination.getDisplayOrder();
		    String name = buildConditionName(VoteOutputFactory.OUTPUT_NAME_NOMINATION_SELECTION,
			    new Integer(displayOrder).toString());
		    defaultConditions.add(new BranchCondition(null, null, new Integer(conditionOrderId++), name,
			    VoteUtils.stripHTML(nomination.getQuestion()), OutputType.OUTPUT_BOOLEAN.toString(), null,
			    null, trueString));
		}
		definition.setShowConditionNameOnly(Boolean.TRUE);

		definitionMap.put(VoteOutputFactory.OUTPUT_NAME_NOMINATION_SELECTION, definition);
	    }
	} else {
	    log.error("Unable to build output definitions for Vote as no tool content object supplied.");
	}

	return definitionMap;
    }

    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, IVoteService voteService, Long toolSessionId,
	    Long learnerId) {

	TreeMap<String, ToolOutput> output = null;
	if (names == null) {
	    output = createAllDisplayOrderOutputs(voteService, toolSessionId, learnerId);
	} else {
	    output = new TreeMap<String, ToolOutput>();
	    for (String name : names) {
		ToolOutput newOutput = getToolOutput(name, voteService, toolSessionId, learnerId);
		if (newOutput != null) {
		    output.put(name, newOutput);
		}
	    }
	}
	return output;
    }

    public ToolOutput getToolOutput(String name, IVoteService voteService, Long toolSessionId, Long learnerId) {
	if (name != null && name.startsWith(VoteOutputFactory.OUTPUT_NAME_NOMINATION_SELECTION)) {
	    VoteSession session = voteService.getSessionBySessionId(toolSessionId);
	    VoteQueUsr queUser = voteService.getVoteUserBySession(learnerId, session.getUid());

	    return new ToolOutput(name, getI18NText(VoteOutputFactory.OUTPUT_NAME_NOMINATION_SELECTION, true),
		    checkDisplayOrderOfVoteQueContent(name, queUser));
	}
	return null;
    }

    /**
     * Check the display order embedded in the condition name. The name MUST start with
     * OUTPUT_NAME_NOMINATION_SELECTION.
     */
    private boolean checkDisplayOrderOfVoteQueContent(String name, VoteQueUsr queUser) {

	String[] dcNames = splitConditionName(name);
	if (dcNames[1] == null || dcNames[1].length() == 0) {
	    log.error("Unable to convert the display order to an int for tool output "
		    + VoteOutputFactory.OUTPUT_NAME_NOMINATION_SELECTION
		    + ". Returning false. Name doesn't contain the display order. Condition name was: " + name);
	    return false;
	}

	int displayOrder = 0;
	try {
	    displayOrder = new Integer(dcNames[1]).intValue();
	} catch (NumberFormatException e) {
	    log.error("Unable to convert the display order to an int for tool output "
		    + VoteOutputFactory.OUTPUT_NAME_NOMINATION_SELECTION
		    + ". Returning false. Number format exception thrown. Condition name was: " + name, e);
	    return false;
	}

	if (queUser != null) {
	    Set voteAttempts = queUser.getVoteUsrAttempts();
	    if (voteAttempts.size() > 0) {
		if (voteAttempts.size() > 1) {
		    log.error(
			    "Attempting to match on nomination, but more than one nomination selected for this user. Will try to match on the given display order. User "
				    + queUser);
		}

		Iterator iter = voteAttempts.iterator();
		while (iter.hasNext()) {
		    VoteUsrAttempt attempt = (VoteUsrAttempt) iter.next();
		    Long questionUid = attempt.getVoteQueContent().getUid();
		    if (questionUid.longValue() == 1 && displayOrder == VoteOutputFactory.FREE_TEXT_NOM_SELECTION) {
			// VoteQueContentId == 1 indicates that it is a free text entry
			return true;
		    } else {
			VoteQueContent nomination = attempt.getVoteQueContent();
			if (nomination.getDisplayOrder() == displayOrder) {
			    return true;
			}
		    }
		}
	    }
	}
	return false;
    }

    /**
     * Check the display order embedded in the condition name. The name MUST start with
     * OUTPUT_NAME_NOMINATION_SELECTION.
     */
    private TreeMap<String, ToolOutput> createAllDisplayOrderOutputs(IVoteService voteService, Long toolSessionId,
	    Long learnerId) {

	TreeMap<String, ToolOutput> output = null;

	VoteSession session = voteService.getSessionBySessionId(toolSessionId);
	VoteContent content = session.getVoteContent();
	VoteQueUsr queUser = voteService.getVoteUserBySession(learnerId, session.getUid());
	String i18nDescription = getI18NText(VoteOutputFactory.OUTPUT_NAME_NOMINATION_SELECTION, true);

	// create a false condition for all of them to start with, then create a true for the ones that are correct.
	if (content.isAllowText()) {
	    boolean found = false;
	    if (queUser != null) {
		Set voteAttempts = queUser.getVoteUsrAttempts();
		Iterator iter = voteAttempts.iterator();
		while (iter.hasNext() && !found) {
		    VoteUsrAttempt attempt = (VoteUsrAttempt) iter.next();
		    Long questionUid = attempt.getVoteQueContent().getUid();
		    found = questionUid.longValue() == 1;
		}
	    }
	    String name = buildConditionName(VoteOutputFactory.OUTPUT_NAME_NOMINATION_SELECTION,
		    VoteOutputFactory.FREE_TEXT_NOM_SELECTION_STR);
	    output.put(name, new ToolOutput(name, i18nDescription, found));
	}

	Iterator contentIter = content.getVoteQueContents().iterator();
	while (contentIter.hasNext()) {
	    VoteQueContent nomination = (VoteQueContent) contentIter.next();
	    int displayOrder = nomination.getDisplayOrder();
	    String name = buildConditionName(VoteOutputFactory.OUTPUT_NAME_NOMINATION_SELECTION,
		    new Integer(displayOrder).toString());
	    boolean found = false;
	    if (queUser != null) {
		Set voteAttempts = queUser.getVoteUsrAttempts();
		Iterator iter = voteAttempts.iterator();
		while (iter.hasNext() && !found) {
		    VoteUsrAttempt attempt = (VoteUsrAttempt) iter.next();
		    found = attempt.getVoteQueContent().getDisplayOrder() == displayOrder;
		}
	    }
	    output.put(name, new ToolOutput(name, i18nDescription, found));
	}

	return output;
    }

    @Override
    public Class[] getSupportedDefinitionClasses(int definitionType) {
	if (ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_DATA_FLOW == definitionType) {
	    // currently array of strings and array of arrays of strings are supported
	    Class stringArrayClass = String[].class;
	    Class arrayOfStringArrays = String[][].class;
	    Class stringClass = String.class;
	    Class simpleUrlArrayClass = SimpleURL[].class;
	    Class arrayOfSimpleUrlArrayClass = SimpleURL[][].class;
	    Class simpleUrlClass = SimpleURL.class;
	    return new Class[] { stringArrayClass, arrayOfStringArrays, stringClass, simpleUrlArrayClass,
		    arrayOfSimpleUrlArrayClass, simpleUrlClass };
	}
	return super.getSupportedDefinitionClasses(definitionType);
    }
}
