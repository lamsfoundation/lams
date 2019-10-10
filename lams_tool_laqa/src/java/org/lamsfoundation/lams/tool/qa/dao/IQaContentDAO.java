/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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

package org.lamsfoundation.lams.tool.qa.dao;

import org.lamsfoundation.lams.tool.qa.model.QaCondition;
import org.lamsfoundation.lams.tool.qa.model.QaContent;

/**
 * @author Ozgur Demirtas
 */
public interface IQaContentDAO {
    QaContent getQaByContentId(long qaId);

    void saveOrUpdateQa(QaContent qa);

    void removeQa(Long qaContentId);

    void deleteQa(QaContent qa);

    void removeQaById(Long qaId);

    void removeAllQaSession(QaContent content);

    void deleteCondition(QaCondition condition);

    void removeQuestionsFromCache(QaContent qaContent);

    void removeQaContentFromCache(QaContent qaContent);

    void delete(Object object);
}
