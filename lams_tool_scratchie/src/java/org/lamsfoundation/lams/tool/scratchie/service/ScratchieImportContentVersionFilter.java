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


package org.lamsfoundation.lams.tool.scratchie.service;

import org.lamsfoundation.lams.learningdesign.service.ToolContentVersionFilter;
import org.lamsfoundation.lams.tool.scratchie.dto.QbOptionDTO;
import org.lamsfoundation.lams.tool.scratchie.model.Scratchie;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieItem;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieSession;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieUser;

/**
 * Import filter class for different version of Scratchie content.
 *
 */
public class ScratchieImportContentVersionFilter extends ToolContentVersionFilter {

    /**
     * Import 20131130 version content to 20131212 version tool server.
     */
    public void up20131130To20131212() {
	this.removeField(ScratchieUser.class, "totalAttempts");
	this.removeField(ScratchieUser.class, "scratchingFinished");
	this.removeField(ScratchieUser.class, "mark");

	this.removeField(Scratchie.class, "createdBy");

	this.removeField(QbOptionDTO.class, "scratchieItem");

	this.addField(ScratchieSession.class, "mark", "0");
	this.addField(ScratchieSession.class, "scratchingFinished", "0");
    }

    /**
     * Import 20131212 version content to 20140102 version tool server.
     */
    public void up20131212To20140102() {
	this.removeField(Scratchie.class, "runOffline");
	this.removeField(Scratchie.class, "onlineInstructions");
	this.removeField(Scratchie.class, "offlineInstructions");
	this.removeField(Scratchie.class, "attachments");
    }

    /**
     * Import 20131212 version content to 20140102 version tool server.
     */
    public void up20140102To20140505() {
	this.removeField(Scratchie.class, "contentInUse");
    }

    /**
     * Import 20140613 version content to 20150206 version tool server.
     */
    public void up20140613To20150206() {
	this.addField(Scratchie.class, "burningQuestionsEnabled", "1");
    }
    
    /**
     * Import 20140613 version content to 20150206 version tool server.
     */
    public void up20180425To20180828() {
	this.removeField(ScratchieItem.class, "correctAnswer");
	this.removeField(ScratchieItem.class, "firstChoiceAnswerLetter");
	this.removeField(ScratchieItem.class, "userMark");
	this.removeField(ScratchieItem.class, "userAttempts");
    }
}
