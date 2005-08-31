import org.lamsfoundation.lams.authoring.*;
/*
*
* @author      DC
* @version     0.1
* @comments    Tool Activity Data storage class. 
* @see		   Activity
*/
class GroupingActivity extends Activity{
	
	private var _createGroupingID:Number;
	private var _createGroupingUIID:Number;

	function GroupingActivity(activityUIID:Number, activityTypeID:Number, activityCategoryID:Number, learningLibraryID:Number,libraryActivityUIImage:String, createGroupingUIID:Number){
		super(activityUIID, activityTypeID, activityCategoryID, learningLibraryID,libraryActivityUIImage);
		_createGroupingUIID = createGroupingUIID;
	}
	
		/**
	 * 
	 * @usage   
	 * @param   newcreateGroupingID 
	 * @return  
	 */
	public function set createGroupingID (newcreateGroupingID:Number):Void {
		_createGroupingID = newcreateGroupingID;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get createGroupingID ():Number {
		return _createGroupingID;
	}

	
	/**
	 * 
	 * @usage   
	 * @param   newcreateGroupingUIID 
	 * @return  
	 */
	public function set createGroupingUIID (newcreateGroupingUIID:Number):Void {
		_createGroupingUIID = newcreateGroupingUIID;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get createGroupingUIID ():Number {
		return _createGroupingUIID;
	}

	
	

	
}

