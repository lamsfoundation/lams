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

package org.lamsfoundation.lams.tool.qa.web.form;

/* ActionForm for the Authoring environment*/
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.model.QaContent;

/**
 * @author Ozgur Demirtas
 */
public class QaAuthoringForm extends QaLearningForm implements QaAppConstants {
    
    private QaContent qa;

    protected String questionIndex;
    protected String toolContentID;
    private String contentFolderID;
    
    public QaContent getQa() {
	return qa;
    }
    public void setQa(QaContent qa) {
	this.qa = qa;
    }

    /**
     * @return Returns the questionIndex.
     */
    @Override
    public String getQuestionIndex() {
	return questionIndex;
    }

    /**
     * @param questionIndex
     *            The questionIndex to set.
     */
    @Override
    public void setQuestionIndex(String questionIndex) {
	this.questionIndex = questionIndex;
    }

    /**
     * @return Returns the toolContentID.
     */
    public String getToolContentID() {
	return toolContentID;
    }

    /**
     * @param toolContentID
     *            The toolContentID to set.
     */
    public void setToolContentID(String toolContentID) {
	this.toolContentID = toolContentID;
    }

    /**
     * @return Returns the contentFolderID.
     */
    public String getContentFolderID() {
	return contentFolderID;
    }

    /**
     * @param contentFolderID
     *            The contentFolderID to set.
     */
    public void setContentFolderID(String contentFolderID) {
	this.contentFolderID = contentFolderID;
    }
}
