/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.vote.dao;

import java.util.List;

import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;


/**
 * 
 * @author Ozgur Demirtas
 * <p></p>
 *
 */
public interface IVoteQueContentDAO
{
	public VoteQueContent getMcQueContentByUID(Long uid);

	public VoteQueContent getToolDefaultQuestionContent(final long mcContentId);
	
	public VoteQueContent getQuestionContentByQuestionText(final String question, final Long mcContentUid);

	public VoteQueContent getQuestionContentByDisplayOrder(final Long displayOrder, final Long mcContentUid);
	
	public List getAllQuestionEntries(final long mcContentId);

	public List refreshQuestionContent(final Long mcContentId);

	public void cleanAllQuestions(final Long mcContentUid);

	public void cleanAllQuestionsSimple(final Long mcContentUid);
	
 	public void resetAllQuestions(final Long mcContentUid);

	public void removeQuestionContentByMcUid(final Long mcContentUid);

	public void saveMcQueContent(VoteQueContent mcQueContent);
    
	public void updateMcQueContent(VoteQueContent mcQueContent);

	public void saveOrUpdateMcQueContent(VoteQueContent mcQueContent);
	
	public void removeMcQueContentByUID(Long uid);

	public void removeMcQueContent(VoteQueContent mcQueContent);
	
	public List getNextAvailableDisplayOrder(final long mcContentId);
}
