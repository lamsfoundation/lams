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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ************************************************************************
 */

import org.lamsfoundation.lams.common.*;
import org.lamsfoundation.lams.common.util.*;

/**
* Learner Progress
*/
class Progress {
	private var _className:String = "Progress";
	private static var _instance:Progress = null;
	
	/* data */
	private var _lessonId:Number;
	private var _lessonName:String;
	private var _userName:String;
	private var _learnerId:Number;
	private var _currentActivityId:Number;
	private var _attemptedActivities:Array;
	private var _completedActivities:Array;
	
	/**
	* Constructor.
	*/
	public function Progess (){
	}
	
	/**
	 * 
	 * @return the Sequence 
	 */
	public static function getInstance():Progress{
		if(Progress._instance == null){
            Progress._instance = new Progress();
        }
        return Progress._instance;
	}
	
	public function populateFromDTO(dto:Object){
		trace('populating progress obj:' + dto.lessonId);
		_lessonId = dto.lessonId;
		_lessonName = dto.lessonName;
		_userName = dto.userName;
		_learnerId = dto.learnerId;
		_currentActivityId = dto.currentActivityId;
		_attemptedActivities = dto.attemptedActivities;
		_completedActivities = dto.completedActivities;
	}
	
	public function getLessonId():Number{
		return _lessonId;
	}
	
	public function getLessonName():String{
		return _lessonName;
	}
	
	public function getUserName():String{
		return _userName;
	}
	
	public function getLearnerId():Number{
		return _learnerId;
	}
	
	public function getCurrentActivityId():Number{
		return _currentActivityId;
	}
	
	public function getAttemptedActivities():Array{
		return _attemptedActivities;
	}
	
	public function getCompletedActivities():Array{
		return _completedActivities;
	}
	
	function get className():String{
        return _className;
    }
}