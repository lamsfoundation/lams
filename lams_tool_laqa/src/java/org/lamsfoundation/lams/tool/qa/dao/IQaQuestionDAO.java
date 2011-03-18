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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.qa.dao;

import java.util.List;

import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQuestion;

/**
 * 
 * @author Ozgur Demirtas
 * 
 */
public interface IQaQuestionDAO {

    public void createQueContent(QaQuestion queContent);

    public void saveOrUpdateQaQueContent(QaQuestion qaQuestion);

    public void removeQueContent(long qaQueContentId);

    public void removeQaQueContent(QaQuestion qaQuestion);

    public List getAllQuestionEntries(final long qaContentId);

    public QaQuestion getQuestionContentByQuestionText(final String question, Long qaContentId);

    public QaQuestion getQuestionByDisplayOrder(Long displayOrder, Long qaContentId);

    public List getAllQuestionEntriesSorted(final long qaContentId);
}
