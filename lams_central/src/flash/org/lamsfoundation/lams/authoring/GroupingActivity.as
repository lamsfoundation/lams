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

	
	function GroupingActivity(activityUIID:Number){
		super(activityUIID);
		_activityTypeID = GROUPING_ACTIVITY_TYPE;
		_activityCategoryID = CATEGORY_SYSTEM;
		_groupingSupportType = GROUPING_SUPPORT_OPTIONAL;
	}
	
	
	
	/**
	 * Creates from a dto... which is nice
	 * @usage   
	 * @param   dto 
	 * @return  
	 */
	public function populateFromDTO(dto:Object){
		super.populateFromDTO(dto);
		_createGroupingID = dto.createGroupingID;
		_createGroupingUIID = dto.createGroupingUIID;
	}
	
	/**
	 * Creates an object containing all the props
	 * If a value is null then it is ommitted... if itsd the null value from const 
	 * then its included
	 * @usage   
	 * @return  the DTO
	 */
	public function toData():Object{
		var dto:Object = super.toData();
		if(_createGroupingID){	dto.createGroupingID = _createGroupingID;	}
		if(_createGroupingUIID){	dto.createGroupingUIID = _createGroupingUIID;	}
		return dto;
	}
	

	
	//get and sets
	
	
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

