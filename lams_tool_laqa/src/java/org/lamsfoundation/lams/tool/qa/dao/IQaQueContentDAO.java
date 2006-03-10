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
package org.lamsfoundation.lams.tool.qa.dao;

import java.util.List;

import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueContent;


/**
 * 
 * @author Ozgur Demirtas
 *
 */
public interface IQaQueContentDAO
{
    public QaQueContent getQaQueById(long qaQueContentId);
        
    public QaQueContent getToolDefaultQuestionContent(final long qaContentId);
    
    public List getQuestionIndsForContent(QaContent qa);
    
    public void createQueContent(QaQueContent queContent);
    
    public void updateQaQueContent(QaQueContent qaQueContent);
    
    public void removeQueContent(long qaQueContentId);
    
    public List getQaQueContentsByContentId(long qaContentId);
    
    public List getAllQuestionEntries(final long qaContentId);
    
    public QaQueContent getQuestionContentByQuestionText(final String question, Long qaContentId);
    
 	public QaQueContent getQuestionContentByDisplayOrder(Long displayOrder, Long qaContentId);
}
