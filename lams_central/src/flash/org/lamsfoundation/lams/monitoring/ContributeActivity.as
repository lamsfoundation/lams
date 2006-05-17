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

import org.lamsfoundation.lams.monitoring.*;
import org.lamsfoundation.lams.authoring.Activity;

/*
* Contribute Activity - singleton class representing a contributing activity
*/
class ContributeActivity extends Activity {
	
	/* Contribution Types - defined in org.lamsfoundation.lams.learningdesign.ContributionTypes */
	public static var MODERATION:Number = 1;
	public static var DEFINE_LATER:Number = 2;
	public static var PERMISSION_GATE:Number = 3;
	public static var SYNC_GATE:Number = 4;
	public static var SCHEDULE_GATE:Number = 5;
	public static var CHOSEN_GROUPING:Number = 6;
	public static var CONTRIBUTION:Number = 7;
	
	private var _childActivities:Array;
	private var _contributeEntries:Array;
	private var _contributionType:Number;
	private var _taskURL:String;
	private var _isRequired:Boolean;
	
	private static var _instance:ContributeActivity = null;
	
	/**
	* Constructor.
	*/
	public function ContributeActivity (){
		super(null);
		_childActivities = new Array();
		_contributeEntries = new Array();
	}
	
	/**
	 * 
	 * @return the ContributeActivity
	 */
	public static function getInstance():ContributeActivity{
		if(ContributeActivity._instance == null){
            ContributeActivity._instance = new ContributeActivity();
        }
        return ContributeActivity._instance;
	}
	
	
	public function populateFromDTO(dto:Object){
		trace('populating from dto...');
		_activityID = dto.activityID;
		_parentActivityID = dto.parentActivityID;
		_activityTypeID = dto.activityTypeID;
		
		if(dto.childActivities != null){
			// create children
			for(var i=0; i<dto.childActivities.length;i++){
				var ca:ContributeActivity = new ContributeActivity();
				ca.populateFromDTO(dto.childActivities[i]);
				_childActivities.push(ca);
				trace('adding a child');
			}
		}
		
		if(dto.contributeEntries != null){
			// create entries
			for(var i=0; i<dto.contributeEntries.length;i++){
				var ca:ContributeActivity = new ContributeActivity();
				ca.populateFromDTO(dto.contributeEntries[i]);
				_contributeEntries.push(ca);
				trace('adding a entry');
			}
		}
		_title = dto.title;
		_description = dto.description;
		_taskURL = dto.URL;
		trace("URL in DTO for "+dto.title+ " is: "+dto.URL)
		_contributionType = dto.contributionType;
		_isRequired = dto.isRequired;
	}
	
	public function set contributionType(a:Number):Void{
		_contributionType = a;
	}
	public function get contributionType():Number{
		return _contributionType;
	}
	
	public function set contributeEntries(a:Array):Void{
		_contributeEntries = a;
	}
	
	public function get contributeEntries():Array{
		return _contributeEntries;
	}
	
	public function set childActivities(a:Array):Void{
		_childActivities = a;
	}
	public function get childActivities():Array{
		return _childActivities;
	}
	
	public function set taskURL(a:String):Void{
		_taskURL = a;
	}
	
	public function get taskURL():String{
		return _taskURL;
	}
	
	public function set isRequired(a:Boolean):Void{
		_isRequired = a;
	}
	public function get isRequired():Boolean{
		return _isRequired;
	}
	
}