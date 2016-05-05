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
package org.lamsfoundation.lams.tool.qa.service;

import org.lamsfoundation.lams.learningdesign.service.ToolContentVersionFilter;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueContent;

/**
 * Import filter class for different versions of Q&A content.
 */
public class QaImportContentVersionFilter extends ToolContentVersionFilter {

    /**
     * Import 2.0RC1 version content to 2.0RC2 version. Added lock on finish
     * field.
     */
    public void up20061102To20061113() {
	// Change name to suit the version you give the tool.
	this.addField(QaContent.class, "lockWhenFinished", new Boolean(true));
	this.addField(QaContent.class, "showOtherAnswers", new Boolean(true));
    }

    public void up20081126To20101022() {
	this.removeField(QaQueContent.class, "isOptional");
	this.addField(QaQueContent.class, "required", new Boolean(false));
    }

    /**
     * Import 20140101 version content to 20140102 version tool server.
     */
    public void up20140101To20140102() {
	this.removeField(QaContent.class, "runOffline");
	this.removeField(QaContent.class, "onlineInstructions");
	this.removeField(QaContent.class, "offlineInstructions");
	this.removeField(QaContent.class, "qaUploadedFiles");
    }

    /**
     * Import 20140102 version content to 20140527 version tool server.
     */
    public void up20140102To20140102() {
	this.removeField(QaContent.class, "contentLocked");
	this.removeField(QaContent.class, "synchInMonitor");
    }

    /**
     * Import 20140822 version content to 20150511 version tool server.
     */
    public void up20140822To20150511() {
	this.addField(QaContent.class, "minimumRates", 0);
	this.addField(QaContent.class, "maximumRates", 0);

	this.addField(QaQueContent.class, "minWordsLimit", 0);
    }
}
