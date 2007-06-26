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
import org.lamsfoundation.lams.authoring.br.BranchConnector;
import org.lamsfoundation.lams.common.*;
import org.lamsfoundation.lams.common.util.*;


class Branch extends Transition {

	private var UIID:Number;
	private var _sequenceActivity:Activity;
	
	private var _direction:Number;
	private var _targetUIID:Number;
	private var _hubUIID:Number;
	
	
	// TODO: add learningDesignID

	public function Branch(activityUIID:Number, _dir:Number, targetUIID:Number, hubUIID:Number, sequenceActivity:Activity, learningDesignID:Number){
		if(_dir == BranchConnector.DIR_FROM_START)
			super(null, hubUIID, targetUIID, learningDesignID);
		else
			super(null, targetUIID, hubUIID, learningDesignID);
		
		UIID = activityUIID;
		_direction = _dir;
		_targetUIID = targetUIID;
		_hubUIID = hubUIID;
		_sequenceActivity = sequenceActivity;
		
	}
	
	public function toData():Object{
		var dto:Object = super.toData();
		dto.branchUIID = branchUIID;
		dto.sequenceActivity = sequenceActivity;
		return dto;
	}
	
	public function get branchUIID():Number {
		return UIID;
	}
	
	public function get sequenceActivity():Activity {
		return _sequenceActivity;
	}
	
	public function get direction():Number {
		return _direction;
	}
	
	public function get targetUIID():Number {
		return _targetUIID;
	}
	
	public function get hubUIID():Number {
		return _hubUIID;
	}
}

