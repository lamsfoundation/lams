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

import org.lamsfoundation.lams.authoring.*;

/**
 *
 * @author Mitchell Seaton
 * @version 2.1
 **/
class GroupBranchActivityEntry extends BranchActivityEntry {
	
	private var _group:Group;
	private var _isGroupType:Boolean;
	
	function GroupBranchActivityEntry(entryID:Number, entryUIID:Number, group:Group, sequenceActivity:SequenceActivity, branchingActivity:BranchingActivity){
		this.entryID = entryID;
		this.entryUIID = entryUIID;
		this.sequenceActivity = sequenceActivity;
		this.branchingActivity = branchingActivity;
		this.isGroupType = (branchingActivity.activityTypeID == Activity.GROUP_BRANCHING_ACTIVITY_TYPE);
		this.group = group;
	}
	
	public function toData():Object {
		var dto:Object = new Object();
		
		if(this.entryID) dto.entryID = this.entryID;
		if(this.entryUIID) dto.entryUIID = this.entryUIID;
		if(this.sequenceActivity) dto.sequenceActivityUIID = this.sequenceActivity.activityUIID;
		if(this.branchingActivity) dto.branchingActivityUIID = this.branchingActivity.activityUIID;
		
		if(_group) dto.groupUIID = _group.groupUIID;
		
		return dto;
	}
	
	public function set group(a:Group) {
		_group = a;
	}
	
	public function get group():Group {
		return _group;
	}
	
	public function get groupName():String {
		return _group.groupName;
	}
	
	public function set isGroupType(a:Boolean) {
		_isGroupType = a;
	}
	
	public function get isGroupType():Boolean {
		return _isGroupType;
	}

}