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

package org.lamsfoundation.lams.learning.kumalive.dao;

import java.util.List;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.learning.kumalive.model.Kumalive;
import org.lamsfoundation.lams.learning.kumalive.model.KumalivePoll;
import org.lamsfoundation.lams.learning.kumalive.model.KumaliveRubric;
import org.lamsfoundation.lams.learning.kumalive.model.KumaliveScore;

public interface IKumaliveDAO extends IBaseDAO {
    Kumalive findKumalive(Integer organisationId);

    List<Kumalive> findKumalives(Integer organisationId, String sortColumn, boolean isAscending);

    List<Kumalive> findKumalives(List<Long> kumaliveIds);

    List<KumaliveRubric> findRubrics(Integer organisationId);

    List<KumaliveScore> findKumaliveScore(Long kumaliveId, boolean isAscending);

    List<KumaliveScore> findKumaliveScore(Long kumaliveId, Integer userId);

    KumalivePoll findPollByKumaliveId(Long kumaliveId);
}