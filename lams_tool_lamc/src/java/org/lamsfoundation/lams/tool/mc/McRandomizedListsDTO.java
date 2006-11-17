/***************************************************************************
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
 * ***********************************************************************/
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc;

import java.util.List;




/**
 * <p> DTO that holds randomized lists
 * </p>
 * 
 * @author Ozgur Demirtas
 */
public class McRandomizedListsDTO implements Comparable
{
    List listCandidateAnswers;
    List listCandidateAnswerUids;
    
    
	public int compareTo(Object o)
    {
	    McRandomizedListsDTO mcRandomizedListsDTO = (McRandomizedListsDTO) o;
     
        if (mcRandomizedListsDTO == null)
        	return 1;
		else
			return 0;
    }
    
    
    /**
     * @return Returns the listCandidateAnswers.
     */
    public List getListCandidateAnswers() {
        return listCandidateAnswers;
    }
    /**
     * @param listCandidateAnswers The listCandidateAnswers to set.
     */
    public void setListCandidateAnswers(List listCandidateAnswers) {
        this.listCandidateAnswers = listCandidateAnswers;
    }
    /**
     * @return Returns the listCandidateAnswerUids.
     */
    public List getListCandidateAnswerUids() {
        return listCandidateAnswerUids;
    }
    /**
     * @param listCandidateAnswerUids The listCandidateAnswerUids to set.
     */
    public void setListCandidateAnswerUids(List listCandidateAnswerUids) {
        this.listCandidateAnswerUids = listCandidateAnswerUids;
    }
}
