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
package org.lamsfoundation.lams.tool.vote.service;

import org.lamsfoundation.lams.learningdesign.service.ToolContentVersionFilter;
import org.lamsfoundation.lams.tool.vote.model.VoteContent;

/**
 * Import filter class for different versions of Vote content.
 */
public class VoteImportContentVersionFilter extends ToolContentVersionFilter {

    /**
     * Import 2.0RC1 version content to 2.0RC2 version. Added lock on finish field.
     */
    public void up20061102To20061113() {
	this.removeField(VoteContent.class, "voteChangable");
    }

    /**
     * Version 2.1 added a showResults column and this should default to true. 20080108 was an pre-release version of
     * 2.1, but using 20080108 will cover the prerelease 2.1 + 2.0.4 created import files.
     */
    public void up20080108To20080326() {
	this.addField(VoteContent.class, "showResults", "true");
    }

    /** Version 2.3.4 added a minNominationCount column and this should default to "1". */
    public void up20090726To20100309() {
	this.addField(VoteContent.class, "minNominationCount", "1");
    }

    /**
     * Import 20131227 version content to 20140102 version tool server.
     */
    public void up20131227To20140102() {
	this.removeField(VoteContent.class, "runOffline");
	this.removeField(VoteContent.class, "onlineInstructions");
	this.removeField(VoteContent.class, "offlineInstructions");
	this.removeField(VoteContent.class, "voteAttachments");
    }

    /**
     * Import 20140102 version content to 20140520 version tool server.
     */
    public void up20140102To20140520() {
	this.removeField(VoteContent.class, "contentInUse");
    }

    public void up20171023To20181202() {
	this.renameClass("org.lamsfoundation.lams.tool.vote.pojos.", "org.lamsfoundation.lams.tool.vote.model.");
    }
}
