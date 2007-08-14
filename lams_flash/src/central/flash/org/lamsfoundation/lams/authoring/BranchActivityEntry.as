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
class BranchActivityEntry   {
	
	private var _entryID:Number;
	private var _entryUIID:Number;
	private var _sequenceActivity:SequenceActivity;
	private var _branchingActivity:BranchingActivity;
	
	function BranchActivityEntry(){
	}
	
	public function set entryID(a:Number) {
		_entryID = a;
	}
	
	public function get entryID():Number {
		return _entryID;
	}
	
	public function set entryUIID(a:Number) {
		_entryUIID = a;
	}
	
	public function get entryUIID():Number {
		return _entryUIID;
	}
	
	public function set sequenceActivity(a:SequenceActivity) {
		_sequenceActivity = a;
	}
	
	public function get sequenceActivity():SequenceActivity {
		return _sequenceActivity;
	}
	
	public function set branchingActivity(a:BranchingActivity) {
		_branchingActivity = a;
	}
	
	public function get branchingActivity():BranchingActivity {
		return _branchingActivity;
	}
	
	public function get sequenceName():String {
		return _sequenceActivity.title;
	}

}