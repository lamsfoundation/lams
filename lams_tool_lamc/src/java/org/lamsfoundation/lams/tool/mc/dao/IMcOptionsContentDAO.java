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

import java.util.List;

import org.lamsfoundation.lams.tool.mc.pojos.McOptsContent;

/**
 * @author Ozgur Demirtas
 * <p>Interface for the McOptionsContent DAO, defines methods needed to access/modify mc options content</p>
 * 
 */
public interface IMcOptionsContentDAO
{
	/**
	 *  * <p>Return the persistent instance of a McOptsContent  
	 * with the given identifier <code>uid</code>, returns null if not found. </p>
	 * 
	 * @param uid
	 * @return McOptsContent
	 */
	public McOptsContent getMcOptionsContentByUID(Long uid);

	
	/**
	 *  <p>Return a list of a McOptsContents  
	 * with the given identifier <code>mcQueContentId</code>, returns null if not found. </p>

	 * @param mcQueContentId
	 * @return List
	 */
	public List findMcOptionsContentByQueId(Long mcQueContentId);
	
	/**
	 *  <p>Return the persistent instance of a McOptsContent  
	 * with the given identifiers <code>option</code>, <code>mcQueContentUid  </code> returns null if not found. </p>
	 * 
	 * @param option
	 * @param mcQueContentUid
	 * @return McOptsContent
	 */
	public McOptsContent getOptionContentByOptionText(final String option, final Long mcQueContentUid);
	
	/**
	 *  <p>Return a list of a persisted McOptsContents  
	 * with the given identifier <code>mcQueContentId</code>, returns null if not found. </p>
	 * 
	 * @param mcQueContentId
	 * @return List
	 */
	public List getPersistedSelectedOptions(Long mcQueContentId);
	
	public List getCorrectOption(Long mcQueContentId);
	
	/**
	 *  <p>saves McOptsContent </p>
	 * @param mcOptionsContent
	 */
	public void saveMcOptionsContent(McOptsContent mcOptionsContent);
    
	/**
	 *  <p>updates McOptsContent </p>
	 * @param mcOptionsContent
	 */
	public void updateMcOptionsContent(McOptsContent mcOptionsContent);

	/**
	 *  <p>removes McOptsContent </p>
	 * @param mcOptionsContent
	 */
	public void removeMcOptionsContentByUID(Long uid);

	/**
	 *  <p>removes McOptsContent </p>
	 * @param mcOptionsContent
	 */
	public void removeMcOptionsContentByQueId(Long mcQueContentId);

	/**
	 *  <p>removes McOptsContent </p>
	 * @param mcOptionsContent
	 */
	public void removeMcOptionsContent(McOptsContent mcOptsContent);
	
	public List findMcOptionNamesByQueId(Long mcQueContentId);
}



