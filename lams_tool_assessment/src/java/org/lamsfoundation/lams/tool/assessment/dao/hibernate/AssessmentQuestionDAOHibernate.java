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
package org.lamsfoundation.lams.tool.assessment.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.tool.assessment.dao.AssessmentQuestionDAO;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion;

public class AssessmentQuestionDAOHibernate extends BaseDAOHibernate implements AssessmentQuestionDAO {

    private static final String FIND_AUTHORING_QUESTIONS = "from " + AssessmentQuestion.class.getName()
	    + " where assessment_uid = ? order by create_date asc";

    @Override
    public List getAuthoringQuestions(Long assessmentUid) {

	return this.getHibernateTemplate().find(FIND_AUTHORING_QUESTIONS, assessmentUid);
    }

    @Override
    public AssessmentQuestion getByUid(Long assessmentQuestionUid) {
	return (AssessmentQuestion) this.getObject(AssessmentQuestion.class, assessmentQuestionUid);
    }

    @Override
    public void evict(Object o) {
	getHibernateTemplate().evict(o);
    }

}
