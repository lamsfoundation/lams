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

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.lamsfoundation.lams.learning.kumalive.model.Kumalive;
import org.lamsfoundation.lams.learning.kumalive.model.KumalivePoll;
import org.lamsfoundation.lams.learning.kumalive.model.KumaliveRubric;
import org.lamsfoundation.lams.util.excel.ExcelCell;
import org.lamsfoundation.lams.util.excel.ExcelSheet;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface IKumaliveService {
    Kumalive getKumalive(Long id);

    Kumalive getKumaliveByOrganisation(Integer organisationId);

    Kumalive startKumalive(Integer organisationId, Integer userId, String name, ArrayNode rubrics, boolean isTeacher);

    void finishKumalive(Long id);

    void scoreKumalive(Long rubricId, Integer userId, Long batch, Short score);

    List<KumaliveRubric> getRubrics(Integer organisationId);

    void saveRubrics(Integer organisationId, ArrayNode rubricsJSON);

    ObjectNode getReportOrganisationData(Integer organisationId, String sortColumn, boolean isAscending, int rowLimit,
	    int page);

    ObjectNode getReportKumaliveData(Long kumaliveId, boolean isAscending);

    ObjectNode getReportUserData(Long kumaliveId, Integer userId);

    List<ExcelSheet> exportKumalives(List<Long> kumaliveIds);

    List<ExcelSheet> exportKumalives(Integer organisationId);

    KumalivePoll getPollByKumaliveId(Long kumaliveId);

    KumalivePoll startPoll(Long kumaliveId, String name, ArrayNode answersJSON);

    void finishPoll(Long pollId);

    void saveVote(Long answerId, Integer userId);

    void releasePollResults(Long pollId, boolean votesReleased, boolean votersReleased);

    void log(Long kumaliveId, Integer userId, Date date, short type);
}