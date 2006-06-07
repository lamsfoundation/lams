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
	private var _learnerLName:String;
	private var _learnerFName:String;
	private var _learnerId:Number;
	private var _currentActivityId:Number;
	private var _attemptedActivities:Array;
	private var _completedActivities:Array;
	
	/**
	* Constructor.
	*/
		public function Progess (dto:Object){
		if(dto != null){
			populateFromDTO(dto);
		}
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
		Debugger.log('populating from dto.',Debugger.CRITICAL,'populateFromDTO','org.lamsfoundation.lams.Progress');
		trace('populating progress obj:' + dto.lessonId);
		_lessonId = dto.lessonId;
		_lessonName = dto.lessonName;
		_userName = dto.userName;
		_learnerId = dto.learnerId;
		_learnerFName = dto.learnerFirstName;
		_learnerLName = dto.learnerLastName;
		_currentActivityId = dto.currentActivityId;
		_attemptedActivities = dto.attemptedActivities;
		_completedActivities = dto.completedActivities;
	}
	
	public static function compareProgressData(learner:Object, activityID:Number):String{
		trace ("activity ID passed is: "+activityID)
		trace("Number of Activities completed in the lesson are: "+learner.getCompletedActivities().length)
		
		var arrLearnerProgComp = learner.getCompletedActivities()
		for (var i=0; i<arrLearnerProgComp.length; i++){
			if (activityID == arrLearnerProgComp[i]){
				var clipName:String = "completed_mc";
				return clipName;
			}
		
		}
		
		var arrLearnerProgAttempt = learner.getAttemptedActivities()
		trace("Attempted activities are: "+arrLearnerProgAttempt.length)
		for (var j=0; j<arrLearnerProgAttempt.length; j++){
			trace("Activity Id Passed is "+activityID+" and attempted ID is "+arrLearnerProgAttempt[j])
			if (activityID == arrLearnerProgAttempt[j]){
				if (activityID == learner.getCurrentActivityId()){
					var clipName:String = "current_mc";
					return clipName;
				}else {
					var clipName:String = "attempted_mc";
					return clipName;
				}
			}
			
		}
		//arrLearnerProg = learner.getCurrentActivityId()
		if (activityID == learner.getCurrentActivityId()){
			var clipName:String = "current_mc";
			return clipName;
		}
	}
	
	public static function isLearnerCurrentActivity(learner:Object, activityID:Number):Boolean{
		if (activityID == learner.getCurrentActivityId()){
			return true;
		} else {
			return false;
		}
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
	
	public function getLearnerFirstName():String{
		return _learnerFName;
	}
	
	public function getLearnerLastName():String{
		return _learnerLName;
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