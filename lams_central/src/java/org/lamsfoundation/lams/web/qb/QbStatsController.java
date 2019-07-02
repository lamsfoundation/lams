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

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.qb.dto.QbStatsDTO;
import org.lamsfoundation.lams.qb.model.QbCollection;
import org.lamsfoundation.lams.qb.service.IQbService;
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

    @RequestMapping("/show")
    public String showStats(@RequestParam long qbQuestionUid, Model model) throws Exception {
	QbStatsDTO stats = qbService.getQbQuestionStats(qbQuestionUid);
	model.addAttribute("stats", stats);

	Collection<QbCollection> existingCollections = qbService.getQuestionCollectionsByUid(qbQuestionUid);
	model.addAttribute("existingCollections", existingCollections);

	Integer userId = getUserId();
	Collection<QbCollection> availableCollections = qbService.getUserCollections(userId);
	availableCollections.removeAll(existingCollections);
	model.addAttribute("availableCollections", availableCollections);

	boolean permanentRemove = existingCollections.size() <= 1;
	model.addAttribute("permanentRemove", permanentRemove);
	model.addAttribute("permanentRemovePossible",
		permanentRemove ? qbService.removeQuestionPossibleByUid(qbQuestionUid) : false);

	model.addAttribute("transferAllowed",
		Configuration.getAsBoolean(ConfigurationKeys.QB_COLLECTIONS_TRANSFER_ALLOW));

	return "qb/stats";
    }

    private Integer getUserId() {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user != null ? user.getUserID() : null;
    }
}