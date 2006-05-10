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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.tool;

import java.util.Date;  

public interface IToolVO {

	public abstract String getAuthorUrl();

	public abstract void setAuthorUrl(String authorUrl);

	public abstract String getContributeUrl();

	public abstract void setContributeUrl(String contributeUrl);

	public abstract Date getCreateDateTime();

	public abstract void setCreateDateTime(Date createDateTime);

	public abstract long getDefaultToolContentId();

	public abstract void setDefaultToolContentId(long defaultToolContentId);

	public abstract String getDefineLaterUrl();

	public abstract void setDefineLaterUrl(String defineLaterUrl);

	public abstract String getDescription();

	public abstract String getExportPortfolioClassUrl();

	public abstract String getExportPortfolioLearnerUrl();

	public abstract Integer getGroupingSupportTypeId();

	public abstract String getLanguageFile();

	public abstract String getLearnerPreviewUrl();

	public abstract String getLearnerProgressUrl();

	public abstract String getLearnerUrl();

	public abstract String getModerationUrl();

	public abstract String getMonitorUrl();

	public abstract String getServiceName();

	public abstract boolean isSupportsGrouping();

	public abstract boolean isSupportsRunOffline();

	public abstract String getToolDisplayName();

	public abstract Long getToolId();

	public abstract String getToolIdentifier();

	public abstract String getToolSignature();

	public abstract String getToolVersion();

	public abstract boolean isValid();

}