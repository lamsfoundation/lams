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

/* $Id$ */
package org.lamsfoundation.lams.learningdesign.service;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.dao.ILearningDesignDAO;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;

/** Routines shared by the 2.0 import/export and 1.0.2 import. */ 
public class ImportExportUtil {
	
	/** 
	 * If the learning design has duplicated name in same folder, then rename it with timestamp.
	 * the new name format will be oldname_ddMMYYYY_idx. The idx will be auto incremental index number, start from 1.
	 */
	public static String generateUniqueLDTitle(WorkspaceFolder folder, String titleFromFile, ILearningDesignDAO learningDesignDAO) {
		
		String newTitle = titleFromFile;
		if ( newTitle == null || newTitle.length() == 0 ) 
			newTitle = "unknown";
		
		if(folder != null){
			boolean dupName;
			List<LearningDesign> ldList = learningDesignDAO.getAllLearningDesignsInFolder(folder.getWorkspaceFolderId());
			int idx = 1;
			
			//contruct middle part of name by timestamp
			Calendar calendar = Calendar.getInstance();
			int mth = calendar.get(Calendar.MONTH) + 1;
			String mthStr = new Integer(mth).toString();
			if(mth < 10)
				mthStr = "0" + mthStr;
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			String dayStr = new Integer(day).toString();
			if(day < 10)
				dayStr = "0" + dayStr;
			String nameMid = dayStr + mthStr + calendar.get(Calendar.YEAR);
			while(true){
				dupName = false;
				for(LearningDesign eld :ldList){
					if(StringUtils.equals(eld.getTitle(),newTitle)){
						dupName = true;
						break;
					}
				}
				if(!dupName)
					break;
				newTitle = titleFromFile + "_" + nameMid + "_" + idx;
				idx++;
			}
		}
		
		return newTitle;
	}
}
