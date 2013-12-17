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
package org.lamsfoundation.lams.tool.scratchie.service;

import org.lamsfoundation.lams.learningdesign.service.ToolContentVersionFilter;
import org.lamsfoundation.lams.tool.scratchie.model.Scratchie;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieAnswer;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieSession;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieUser;

/**
 * Import filter class for different version of Scratchie content.
 * 
 */
public class ScratchieImportContentVersionFilter extends ToolContentVersionFilter {

    /**
     * Import 20131130 version content to 20131212 version tool server.
     * 
     */
    public void up20131130To20131212() {
	this.removeField(ScratchieUser.class, "totalAttempts");
	this.removeField(ScratchieUser.class, "scratchingFinished");
	this.removeField(ScratchieUser.class, "mark");
	
	this.removeField(Scratchie.class, "createdBy");
	
	this.removeField(ScratchieAnswer.class, "scratchieItem");
	
	this.addField(ScratchieSession.class, "mark", new Integer(0));
	this.addField(ScratchieSession.class, "scratchingFinished", new Integer(0));
    }
}
