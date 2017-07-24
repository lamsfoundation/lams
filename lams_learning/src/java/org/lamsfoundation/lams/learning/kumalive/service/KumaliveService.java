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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.learning.kumalive.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.lamsfoundation.lams.learning.kumalive.dao.IKumaliveDAO;
import org.lamsfoundation.lams.learning.kumalive.model.Kumalive;
import org.lamsfoundation.lams.learning.kumalive.model.KumaliveRubric;
import org.lamsfoundation.lams.learning.kumalive.model.KumaliveScore;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;

public class KumaliveService implements IKumaliveService {
    // ---------------------------------------------------------------------
    // Instance variables
    // ---------------------------------------------------------------------
    private static Logger logger = Logger.getLogger(KumaliveService.class);

    private IKumaliveDAO kumaliveDAO;
    private ISecurityService securityService;

    public void setSecurityService(ISecurityService securityService) {
	this.securityService = securityService;
    }

    public void setKumaliveDAO(IKumaliveDAO kumaliveDAO) {
	this.kumaliveDAO = kumaliveDAO;
    }

    /**
     * Fetches or creates a Kumalive
     *
     * @throws JSONException
     */
    @Override
    public Kumalive startKumalive(Integer organisationId, Integer userId, String name, JSONArray rubricsJSON,
	    boolean isTeacher) throws JSONException {
	if (isTeacher) {
	    securityService.isGroupMonitor(organisationId, userId, "start kumalive", true);
	}
	Kumalive kumalive = kumaliveDAO.findKumaliveByOrganisationId(organisationId);
	if (kumalive == null) {
	    if (!isTeacher) {
		return null;
	    }
	} else {
	    return kumalive;
	}

	Organisation organisation = (Organisation) kumaliveDAO.find(Organisation.class, organisationId);
	User createdBy = (User) kumaliveDAO.find(User.class, userId);
	kumalive = new Kumalive(organisation, createdBy, name);
	kumaliveDAO.insert(kumalive);

	Set<KumaliveRubric> rubrics = new HashSet<>();
	if (rubricsJSON == null) {
	    KumaliveRubric rubric = new KumaliveRubric(organisation, kumalive, (short) 0, null);
	    kumaliveDAO.insert(rubric);
	    rubrics.add(rubric);
	} else {
	    for (Short rubricIndex = 0; rubricIndex < rubricsJSON.length(); rubricIndex++) {
		String rubricName = rubricsJSON.getString(rubricIndex.intValue());
		KumaliveRubric rubric = new KumaliveRubric(organisation, kumalive, rubricIndex, rubricName);
		kumaliveDAO.insert(rubric);
		rubrics.add(rubric);
	    }
	}
	kumalive.setRubrics(rubrics);
	kumaliveDAO.update(kumalive);

	if (logger.isDebugEnabled()) {
	    logger.debug("Teacher " + userId + " started Kumalive " + kumalive.getKumaliveId());
	}

	return kumalive;
    }

    /**
     * Ends Kumalive
     */
    @Override
    public void finishKumalive(Long id) {
	Kumalive kumalive = (Kumalive) kumaliveDAO.find(Kumalive.class, id);
	kumalive.setFinished(true);
	kumaliveDAO.update(kumalive);
    }

    /**
     * Save Kumalive score
     */
    @Override
    public void scoreKumalive(Long rubricId, Integer userId, Short score) {
	KumaliveRubric rubric = (KumaliveRubric) kumaliveDAO.find(KumaliveRubric.class, rubricId);
	User user = (User) kumaliveDAO.find(User.class, userId);
	KumaliveScore kumaliveScore = new KumaliveScore(rubric, user, score);
	kumaliveDAO.insert(kumaliveScore);
    }

    @Override
    public List<KumaliveRubric> getRubrics(Integer organisationId) {
	return kumaliveDAO.findRubricsByOrganisationId(organisationId);
    }

    @Override
    public void saveRubrics(Integer organisationId, JSONArray rubricsJSON) throws JSONException {
	Organisation organisation = (Organisation) kumaliveDAO.find(Organisation.class, organisationId);
	kumaliveDAO.deleteByProperty(KumaliveRubric.class, "organisation", organisation);
	for (Short rubricIndex = 0; rubricIndex < rubricsJSON.length(); rubricIndex++) {
	    String name = rubricsJSON.getString(rubricIndex.intValue());
	    KumaliveRubric rubric = new KumaliveRubric(organisation, null, rubricIndex, name);
	    kumaliveDAO.insert(rubric);
	}
    }
}