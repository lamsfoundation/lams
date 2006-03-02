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

package org.lamsfoundation.lams.tool.qa;

import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * <p> DTO that holds question and user attempts data for jsp purposes
 * </p>
 * 
 * @author Ozgur Demirtas
 */
public class QaMonitoredAnswersDTO implements Comparable
{
	private String questionUid;
	
	private String question;
	
	private Map questionAttempts;
	
	
	/**
	
	/**
	 * @return Returns the question.
	 */
	public String getQuestion() {
		return question;
	}
	/**
	 * @param question The question to set.
	 */
	public void setQuestion(String question) {
		this.question = question;
	}
	/**
	 * @return Returns the questionUid.
	 */
	public String getQuestionUid() {
		return questionUid;
	}
	/**
	 * @param questionUid The questionUid to set.
	 */
	public void setQuestionUid(String questionUid) {
		this.questionUid = questionUid;
	}
	
	/**
	 * @return Returns the questionAttempts.
	 */
	public Map getQuestionAttempts() {
		return questionAttempts;
	}
	/**
	 * @param questionAttempts The questionAttempts to set.
	 */
	public void setQuestionAttempts(Map questionAttempts) {
		this.questionAttempts = questionAttempts;
	}
	
	public String toString() {
        return new ToStringBuilder(this)
            .append("question", getQuestion())
            .toString();
    }
	
	public int compareTo(Object o)
    {
		QaMonitoredAnswersDTO qaMonitoredAnswersDTO = (QaMonitoredAnswersDTO) o;
     
        if (qaMonitoredAnswersDTO == null)
        	return 1;
		else
			return (int) (new Long(questionUid).longValue() - new Long(qaMonitoredAnswersDTO.questionUid).longValue());
    }
	
}
