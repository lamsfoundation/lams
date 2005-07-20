/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.tool.qa;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * @author Ozgur Demirtas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * QaQueContent Value Object
 * The value object that maps to our model database table: tl_laqa11_que_content
 * The relevant hibernate mapping resides in: QaQueContent.hbm.xml
 *
 * Holds question content within a particular content  
 */

public class QaUploadedFile implements Serializable
{
	/** identifier field */
    private Long submissionId;
    
    /** persistent field */
    private String uuid;

    /** persistent field */
    private boolean isOnlineFile;
    
    /** persistent field */
    private String fileName;
    
    private Long qaContentId;
    
    /** persistent field */
    private QaContent qaContent;
    
    
    /** full constructor */
    public QaUploadedFile(String uuid, 
    					boolean isOnlineFile, 
    					String fileName,
						QaContent qaContent)  
    {
        this.uuid = uuid;
        this.isOnlineFile = isOnlineFile;
        this.fileName = fileName;
        this.qaContent=qaContent;
    }
    
    
    public String toString() {
        return new ToStringBuilder(this)
            .append("uuid: ", getUuid())
			.append("fileName: ", getFileName())
			.toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof QaUploadedFile) ) return false;
        QaUploadedFile castOther = (QaUploadedFile) other;
        return new EqualsBuilder()
            .append(this.getUuid(), castOther.getUuid())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getUuid())
            .toHashCode();
    }
  
    
        /**
	 * @return Returns the fileName.
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName The fileName to set.
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return Returns the isOnlineFile.
	 */
	public boolean isOnlineFile() {
		return isOnlineFile;
	}
	/**
	 * @param isOnlineFile The isOnlineFile to set.
	 */
	public void setOnlineFile(boolean isOnlineFile) {
		this.isOnlineFile = isOnlineFile;
	}
	/**
	 * @return Returns the qaContent.
	 */
	public QaContent getQaContent() {
		return qaContent;
	}
	/**
	 * @param qaContent The qaContent to set.
	 */
	public void setQaContent(QaContent qaContent) {
		this.qaContent = qaContent;
	}
	/**
	 * @return Returns the qaContentId.
	 */
	public Long getQaContentId() {
		return qaContentId;
	}
	/**
	 * @param qaContentId The qaContentId to set.
	 */
	public void setQaContentId(Long qaContentId) {
		this.qaContentId = qaContentId;
	}
	/**
	 * @return Returns the submissionId.
	 */
	public Long getSubmissionId() {
		return submissionId;
	}
	/**
	 * @param submissionId The submissionId to set.
	 */
	public void setSubmissionId(Long submissionId) {
		this.submissionId = submissionId;
	}
	/**
	 * @return Returns the uuid.
	 */
	public String getUuid() {
		return uuid;
	}
	/**
	 * @param uuid The uuid to set.
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
