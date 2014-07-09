/**************************************************************** 
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA 
 * 
 * http://www.gnu.org/licenses/gpl.txt 
 * **************************************************************** 
 */  
 
/* $Id$ */  
package org.lamsfoundation.lams.tool.assessment.dto;

import java.util.List;

public class RequiredQuestionsDTO {
    private boolean isRequiredAnswerMissed;
    private int pageNumber;
    private List<Integer> missingRequiredQuestions;

    public boolean isRequiredAnswerMissed() {
	return isRequiredAnswerMissed;
    }

    public void setRequiredAnswerMissed(boolean isRequiredAnswerMissed) {
	this.isRequiredAnswerMissed = isRequiredAnswerMissed;
    }

    public int getPageNumber() {
	return pageNumber;
    }

    public void setPageNumber(int userUid) {
	this.pageNumber = userUid;
    }

    public List<Integer> getMissingRequiredQuestions() {
	return missingRequiredQuestions;
    }

    public void setMissingRequiredQuestions(List<Integer> fullName) {
	this.missingRequiredQuestions = fullName;
    }
}
