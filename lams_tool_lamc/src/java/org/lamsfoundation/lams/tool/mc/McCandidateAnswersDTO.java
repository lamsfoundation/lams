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



/**
 * <p> DTO that holds candiate answers data for summary page
 * </p>
 * 
 * @author Ozgur Demirtas
 */
public class McCandidateAnswersDTO implements Comparable
{
    /**
     * @return Returns the candidateAnswer.
     */
    public String getCandidateAnswer() {
        return candidateAnswer;
    }
    /**
     * @param candidateAnswer The candidateAnswer to set.
     */
    public void setCandidateAnswer(String candidateAnswer) {
        this.candidateAnswer = candidateAnswer;
    }
    /**
     * @return Returns the correct.
     */
    public String getCorrect() {
        return correct;
    }
    /**
     * @param correct The correct to set.
     */
    public void setCorrect(String correct) {
        this.correct = correct;
    }
	private String candidateAnswer;
	
	private String correct;
	
	
	public int compareTo(Object o)
    {
	    McCandidateAnswersDTO mcCandidateAnswersDTO = (McCandidateAnswersDTO) o;
     
        if (mcCandidateAnswersDTO == null)
        	return 1;
		else
			return 0;
    }
	
}
