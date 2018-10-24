/***************************************************************************
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ************************************************************************
 */

package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Stores decoration elements (labels and regions) on Learning Design canvas.
 *
 * @author Marcin Cieslak
 */
public class LearningDesignAnnotation implements Serializable {

    private Long uid;
    private Integer annotationUIID;
    private Long learningDesignId;
    private String title;
    private Integer xcoord;
    private Integer ycoord;
    private Integer endXcoord;
    private Integer endYcoord;
    private String color;
    private Short size;

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Integer getAnnotationUIID() {
	return annotationUIID;
    }

    public void setAnnotationUIID(Integer annotationUIID) {
	this.annotationUIID = annotationUIID;
    }

    public Long getLearningDesignId() {
	return learningDesignId;
    }

    public void setLearningDesignId(Long learningDesignId) {
	this.learningDesignId = learningDesignId;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public Integer getXcoord() {
	return xcoord;
    }

    public void setXcoord(Integer xcoord) {
	this.xcoord = xcoord;
    }

    public Integer getYcoord() {
	return ycoord;
    }

    public void setYcoord(Integer ycoord) {
	this.ycoord = ycoord;
    }

    public Integer getEndXcoord() {
	return endXcoord;
    }

    public void setEndXcoord(Integer endXcoord) {
	this.endXcoord = endXcoord;
    }

    public Integer getEndYcoord() {
	return endYcoord;
    }

    public void setEndYcoord(Integer endYcoord) {
	this.endYcoord = endYcoord;
    }

    public String getColor() {
	return color;
    }

    public void setColor(String color) {
	this.color = color;
    }

    public Short getSize() {
	return size;
    }

    public void setSize(Short size) {
	this.size = size;
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(learningDesignId).append(annotationUIID).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	LearningDesignAnnotation other = (LearningDesignAnnotation) obj;
	if (annotationUIID == null) {
	    if (other.annotationUIID != null) {
		return false;
	    }
	} else if (!annotationUIID.equals(other.annotationUIID)) {
	    return false;
	}
	if (learningDesignId == null) {
	    if (other.learningDesignId != null) {
		return false;
	    }
	} else if (!learningDesignId.equals(other.learningDesignId)) {
	    return false;
	}
	return true;
    }
}