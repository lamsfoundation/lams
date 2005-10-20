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

package org.lamsfoundation.lams.tool.mc.dao;

import org.lamsfoundation.lams.tool.mc.McQueContent;


/**
 * 
 * @author ozgurd
 *
 */
public interface IMcQueContentDAO
{
	public McQueContent getMcQueContentByUID(Long uid);
	
	public McQueContent getToolDefaultQuestionContent(final long mcContentId);
	
	public McQueContent getQuestionContentByQuestionText(final String question, final Long mcContentUid);
	
 	public void cleanAllQuestions(final Long mcContentUid);
	
	public void resetAllQuestions(final Long mcContentUid);
	
	public void removeQuestionContentByMcUid(final Long mcContentUid);
 	
 	public void saveMcQueContent(McQueContent mcQueContent);
    
	public void updateMcQueContent(McQueContent mcQueContent);
	
	public void saveOrUpdateMcQueContent(McQueContent mcQueContent);
	
	public void removeMcQueContentByUID(Long uid);
	
	public void removeMcQueContent(McQueContent mcQueContent);
}
