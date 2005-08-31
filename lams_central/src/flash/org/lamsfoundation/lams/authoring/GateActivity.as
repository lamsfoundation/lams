import org.lamsfoundation.lams.authoring.*;
/*
*
* @author      DC
* @version     0.1
* @comments    Tool Activity Data storage class. 
* @see		   Activity
*/
class GateActivity extends Activity{
	
	private var _gateActivityLevelID:Number;
	private var _gateEndDateTime:Date;
	private var _gateEndTimeOffset:Number;
	private var _gateOpen:Boolean;
	private var _gateStartDateTime:Date;
	private var _gateStartTimeOffset:Number;
	
	
	
	
	
	

	function GateActivity(activityUIID:Number, activityTypeID:Number, activityCategoryID:Number, learningLibraryID:Number,libraryActivityUIImage:String, gateActivityLevelID:Number){
		super(activityUIID, activityTypeID, activityCategoryID, learningLibraryID,libraryActivityUIImage);
		_gateActivityLevelID = gateActivityLevelID;
	}
	
	/**
	 * 
	 * @usage   
	 * @param   newgateActivityLevelID 
	 * @return  
	 */
	public function set gateActivityLevelID (newgateActivityLevelID:Number):Void {
		_gateActivityLevelID = newgateActivityLevelID;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get gateActivityLevelID ():Number {
		return _gateActivityLevelID;
	}

	
	/**
	 * 
	 * @usage   
	 * @param   newgateEndDateTime 
	 * @return  
	 */
	public function set gateEndDateTime (newgateEndDateTime:Date):Void {
		_gateEndDateTime = newgateEndDateTime;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get gateEndDateTime ():Date {
		return _gateEndDateTime;
	}

	
	/**
	 * 
	 * @usage   
	 * @param   newgateEndTimeOffset 
	 * @return  
	 */
	public function set gateEndTimeOffset (newgateEndTimeOffset:Number):Void {
		_gateEndTimeOffset = newgateEndTimeOffset;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get gateEndTimeOffset ():Number {
		return _gateEndTimeOffset;
	}

	
	/**
	 * 
	 * @usage   
	 * @param   newgateOpen 
	 * @return  
	 */
	public function set gateOpen (newgateOpen:Boolean):Void {
		_gateOpen = newgateOpen;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get gateOpen ():Boolean {
		return _gateOpen;
	}

	
	/**
	 * 
	 * @usage   
	 * @param   newgateStartDateTime 
	 * @return  
	 */
	public function set gateStartDateTime (newgateStartDateTime:Date):Void {
		_gateStartDateTime = newgateStartDateTime;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get gateStartDateTime ():Date {
		return _gateStartDateTime;
	}

	
	/**
	 * 
	 * @usage   
	 * @param   newgateStartTimeOffset 
	 * @return  
	 */
	public function set gateStartTimeOffset (newgateStartTimeOffset:Number):Void {
		_gateStartTimeOffset = newgateStartTimeOffset;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get gateStartTimeOffset ():Number {
		return _gateStartTimeOffset;
	}

	
	
}

