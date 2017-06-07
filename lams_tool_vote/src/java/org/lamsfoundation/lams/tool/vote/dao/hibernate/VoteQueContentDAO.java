/***************************************************************************
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
 * ***********************************************************************/
package org.lamsfoundation.lams.tool.vote.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.vote.dao.IVoteQueContentDAO;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.springframework.stereotype.Repository;

/**
 * Hibernate implementation for database access to VoteQueContent for the vote
 * tool.
 *
 * @author Ozgur Demirtas
 */
@Repository
public class VoteQueContentDAO extends LAMSBaseDAO implements IVoteQueContentDAO {

    //   private static final String CLEAN_QUESTION_CONTENT_BY_CONTENT_ID_SIMPLE = "from voteQueContent in class VoteQueContent where voteQueContent.voteContentId=:voteContentId";

    private static final String LOAD_QUESTION_CONTENT_BY_CONTENT_ID = "from voteQueContent in class VoteQueContent where voteQueContent.voteContentId=:voteContentId";

    private static final String LOAD_QUESTION_CONTENT_BY_DISPLAY_ORDER = "from voteQueContent in class VoteQueContent where voteQueContent.displayOrder=:displayOrder and voteQueContent.voteContentId=:voteContentUid";

    private static final String SORT_QUESTION_CONTENT_BY_DISPLAY_ORDER = "from voteQueContent in class VoteQueContent where voteQueContent.voteContentId=:voteContentId order by voteQueContent.displayOrder";

    @Override
    public VoteQueContent getQuestionByUid(Long uid) {
	return (VoteQueContent) this.getSession().get(VoteQueContent.class, uid);
    }

    @SuppressWarnings("unchecked")
    @Override
    public VoteQueContent getDefaultVoteContentFirstQuestion() {
	final long voteContentId = 1;
	List<VoteQueContent> list = getSessionFactory().getCurrentSession()
		.createQuery(LOAD_QUESTION_CONTENT_BY_CONTENT_ID).setLong("voteContentId", voteContentId).list();

	if (list != null && list.size() > 0) {
	    VoteQueContent voteq = list.get(0);
	    return voteq;
	}
	return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public VoteQueContent getQuestionByDisplayOrder(final Long displayOrder, final Long voteContentUid) {
	List<VoteQueContent> list = getSessionFactory().getCurrentSession()
		.createQuery(LOAD_QUESTION_CONTENT_BY_DISPLAY_ORDER).setLong("displayOrder", displayOrder.longValue())
		.setLong("voteContentUid", voteContentUid.longValue()).list();

	if (list != null && list.size() > 0) {
	    VoteQueContent voteq = list.get(0);
	    return voteq;
	}
	return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<VoteQueContent> getAllQuestionsSorted(final long voteContentId) {
	List<VoteQueContent> list = getSessionFactory().getCurrentSession()
		.createQuery(SORT_QUESTION_CONTENT_BY_DISPLAY_ORDER).setLong("voteContentId", voteContentId).list();

	return list;
    }

    @Override
    public void saveOrUpdateQuestion(VoteQueContent voteQueContent) {
	this.getSession().saveOrUpdate(voteQueContent);
    }

    @Override
    public void removeQuestion(VoteQueContent voteQueContent) {
	this.getSession().delete(voteQueContent);
    }
}