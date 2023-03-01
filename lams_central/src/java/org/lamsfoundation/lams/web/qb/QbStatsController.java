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

package org.lamsfoundation.lams.web.qb;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.outcome.OutcomeMapping;
import org.lamsfoundation.lams.outcome.service.IOutcomeService;
import org.lamsfoundation.lams.outcome.service.OutcomeService;
import org.lamsfoundation.lams.qb.dto.QbStatsDTO;
import org.lamsfoundation.lams.qb.model.QbCollection;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.qb.service.IQbService;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/qb/stats")
public class QbStatsController {
    private static Logger log = Logger.getLogger(QbStatsController.class);

    @Autowired
    @Qualifier("centralMessageService")
    private MessageService messageService;

    @Autowired
    private IQbService qbService;

    @Autowired
    private IOutcomeService outcomeService;

    @Autowired
    private ISecurityService securityService;

    @RequestMapping("/show")
    public String showStats(@RequestParam long qbQuestionUid, Model model) throws Exception {
	QbStatsDTO stats = qbService.getQuestionStats(qbQuestionUid);
	model.addAttribute("stats", stats);

	Integer userId = getUserId();
	int qbQuestionId = stats.getQuestion().getQuestionId();
	boolean managementAllowed = securityService.isSysadmin(getUserId(), "allow QB question editing", true)
		|| qbService.isQuestionInPublicCollection(qbQuestionId)
		|| qbService.isQuestionInUserOwnCollection(qbQuestionId, userId)
		|| qbService.isQuestionInUserSharedCollection(qbQuestionId, userId);
	model.addAttribute("managementAllowed", managementAllowed);

	Collection<QbCollection> existingCollections = qbService.getQuestionCollectionsByUid(qbQuestionUid);
	model.addAttribute("existingCollections", existingCollections);

	if (managementAllowed) {
	    Collection<QbCollection> availableCollections = qbService.getUserCollections(userId);
	    availableCollections.removeAll(existingCollections);
	    model.addAttribute("availableCollections", availableCollections);

	    boolean permanentRemove = existingCollections.size() <= 1;
	    model.addAttribute("permanentRemove", permanentRemove);
	    model.addAttribute("permanentRemovePossible",
		    permanentRemove ? qbService.removeQuestionPossibleByUid(qbQuestionUid) : false);

	    model.addAttribute("transferAllowed",
		    Configuration.getAsBoolean(ConfigurationKeys.QB_COLLECTIONS_TRANSFER_ALLOW));
	    model.addAttribute("mergeAllowed", Configuration.getAsBoolean(ConfigurationKeys.QB_MERGE_ENABLE));

	} else {
	    List<OutcomeMapping> outcomeMappings = outcomeService.getOutcomeMappings(null, null, null, qbQuestionId);
	    OutcomeService.filterQuestionMappings(outcomeMappings);
	    List<String> outcomes = outcomeMappings.stream()
		    .collect(Collectors.mapping(m -> m.getOutcome().getName(), Collectors.toList()));
	    model.addAttribute("outcomes", outcomes);
	}

	return "qb/stats";
    }

    @RequestMapping(path = "/merge", method = RequestMethod.POST)
    public String mergeQuestions(@RequestParam long sourceQbQuestionUid, @RequestParam long targetQbQuestionUid,
	    Model model) throws Exception {
	if (!Configuration.getAsBoolean(ConfigurationKeys.QB_MERGE_ENABLE)) {
	    throw new SecurityException("Question merging is disabled");
	}

	QbQuestion sourceQuestion = qbService.getQuestionByUid(sourceQbQuestionUid);
	QbQuestion targetQuestion = qbService.getQuestionByUid(targetQbQuestionUid);
	List<String> mergeErrors = new LinkedList<>();

	if (sourceQuestion == null) {
	    // TODO rewrite it to i18n keys
	    mergeErrors.add("Source question does not exist");
	}
	if (targetQuestion == null) {
	    mergeErrors.add("Target question does not exist");
	}

	if (mergeErrors.isEmpty()) {
	    if (!sourceQuestion.getType().equals(targetQuestion.getType())) {
		mergeErrors.add("Source question type is different to target question type");
	    }

	    if (sourceQuestion.getQbOptions().size() != targetQuestion.getQbOptions().size()) {
		mergeErrors.add("Number of options in source and target questions does not match");
	    }
	}
	if (mergeErrors.isEmpty()) {
	    int answersChanged = qbService.mergeQuestions(sourceQbQuestionUid, targetQbQuestionUid);
	    model.addAttribute("mergeSourceQbQuestionUid", sourceQbQuestionUid);
	    model.addAttribute("mergeResult", answersChanged);
	    return showStats(targetQbQuestionUid, model);
	}

	model.addAttribute("mergeErrors", mergeErrors);
	return showStats(sourceQbQuestionUid, model);
    }

    private Integer getUserId() {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user != null ? user.getUserID() : null;
    }
}