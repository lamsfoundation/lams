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



package org.lamsfoundation.lams.confidencelevel.dao;

import java.util.List;

import org.lamsfoundation.lams.confidencelevel.ConfidenceLevel;

public interface IConfidenceLevelDAO {

    void saveOrUpdate(Object object);
    
    void delete(Object object);

    /** Not limiting by session as the userId is restrictive enough */
    ConfidenceLevel getConfidenceLevel(Integer userId, Long questionUid);

    /** Limiting by tool session */
    List<ConfidenceLevel> getConfidenceLevelsByItem(Long contentId, Long toolSessionId, Long questionUid);

    /** Not limiting by session as the userId is restrictive enough */
    List<ConfidenceLevel> getConfidenceLevelsByUser(Integer userId, Long toolSessionId);
    
    List<ConfidenceLevel> getConfidenceLevelsByQuestionAndSession(Long questionUid, Long toolSessionId);

    ConfidenceLevel get(Long uid);

}
