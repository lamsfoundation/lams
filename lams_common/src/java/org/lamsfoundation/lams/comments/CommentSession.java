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


package org.lamsfoundation.lams.comments;

/**
 * @author Fiona Malikoff
 *
 */
public class CommentSession implements Cloneable {

    private Long uid;
    private Long externalId;
    private Integer externalIdType;
    private String externalSignature;
    private Long externalSecondaryId;

    public CommentSession() {
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Long getExternalId() {
	return externalId;
    }

    public void setExternalId(Long externalId) {
	this.externalId = externalId;
    }

    public Integer getExternalIdType() {
	return externalIdType;
    }

    public void setExternalIdType(Integer externalIdType) {
	this.externalIdType = externalIdType;
    }

    public String getExternalSignature() {
	return externalSignature;
    }

    public void setExternalSignature(String externalSignature) {
	this.externalSignature = externalSignature;
    }

    public Long getExternalSecondaryId() {
	return externalSecondaryId;
    }

    public void setExternalSecondaryId(Long externalSecondaryId) {
	this.externalSecondaryId = externalSecondaryId;
    }

}
