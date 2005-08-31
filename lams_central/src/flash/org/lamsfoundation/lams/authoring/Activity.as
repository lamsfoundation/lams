import org.lamsfoundation.lams.authoring.*;
/*
*
* @author      DC
* @version     0.1
* @comments    Activity Data storage class. USed as a base class for extending to be Tool, Gate and Complex
*/
class org.lamsfoundation.lams.authoring.Activity {
	

	/**
	* static final variables indicating the the category of activities
    *******************************************************************
	public static final int CATEGORY_SYSTEM = 1;
	public static final int CATEGORY_COLLABORATION = 2;
	public static final int CATEGORY_ASSESSMENT = 3;
	public static final int CATEGORY_CONTENT = 4;
	public static final int CATEGORY_SPLIT = 5;
	/******************************************************************/
	
	/**
	 * static final variables indicating the grouping_support of activities
	 *******************************************************************
	 public static final int GROUPING_SUPPORT_NONE = 1;
	 public static final int GROUPING_SUPPORT_OPTIONAL = 2;
	 public static final int GROUPING_SUPPORT_REQUIRED = 3;
	/******************************************************************/
	/*
	//---------------------------------------------------------------------
    // Class Level Constants
    //---------------------------------------------------------------------
	/**
	 * static final variables indicating the type of activities
	 * available for a LearningDesign 
	 * */
	/******************************************************************/
	public static var TOOL_ACTIVITY_TYPE:Number = 1;
	public static var GROUPING_ACTIVITY_TYPE:Number = 2;
	public static var SYNCH_GATE_ACTIVITY_TYPE:Number = 3;
	public static var SCHEDULE_GATE_ACTIVITY_TYPE:Number = 4;
	public static var PERMISSION_GATE_ACTIVITY_TYPE:Number = 5;
	public static var PARALLEL_ACTIVITY_TYPE:Number = 6;
	public static var OPTIONS_ACTIVITY_TYPE:Number = 7;
	public static var SEQUENCE_ACTIVITY_TYPE:Number = 8;
	/******************************************************************/
	
	/**
	* static final variables indicating the the category of activities
    *******************************************************************/
	public static var CATEGORY_SYSTEM:Number = 1;
	public static var CATEGORY_COLLABORATION:Number = 2;
	public static var CATEGORY_ASSESSMENT:Number = 3;
	public static var CATEGORY_CONTENT:Number = 4;
	public static var CATEGORY_SPLIT:Number = 5;
	/******************************************************************/
	
	//Activity Properties:
	// * indicates required field for constructor
	    //---------------------------------------------------------------------
    // Instance variables
    //---------------------------------------------------------------------
	
	private var _objectType:String;				
	private var _activityTypeID:Number;			


	private var _activityID:Number;				
	private var _activityCategoryID:Number;				
	
	private var _activityUIID:Number;

	private var _learningLibraryID:Number;			
	private var _learningDesignID:Number;			
	private var _libraryActivityID:Number;		

	private var _parentActivityID:Number;		
	

	private var _parentUIID:Number;		
	

	private var _orderID:Number;		
	
	private var _groupingID:Number;
	private var _groupingUIID:Number;
	
	
	private var _title:String;						
	private var _description:String;				
	private var _helpText:String;					//*
	private var _xCoord:Number;
	private var _yCoord:Number;
	private var _libraryActivityUIImage:String;	//*
	private var _applyGrouping:Boolean;


	private var _runOffline:Boolean;
	private var _offlineInstructions:String;
	private var _defineLater:Boolean;
	private var _createDateTime:Date;

	private var _groupingSupportType:Number;
	
	
     
    //Constructor
	 /**
	 * Creates an activity with the minimum of fields. 
	 * 
	 * @param   learningActivityTypeId 
	 * @param   learningLibraryId      
	 * @param   toolId                 
	 * @param   toolContentId          
	 * @param   helpText               
	 * @param   libraryActivityUIImage  
	 */
    function Activity(activityUIID:Number, activityTypeID:Number, activityCategoryID:Number, learningLibraryID:Number,libraryActivityUIImage:String){
        //assign the values:
		_objectType = "Activity"; //should be "Activity"
		_activityUIID = activityUIID;
		_activityTypeID = activityTypeID;
		_activityCategoryID = activityCategoryID;
		_learningLibraryID = learningLibraryID;
		_libraryActivityUIImage = libraryActivityUIImage;
		
		
		
    }
	
	//helper methods
	
	/**
	* Validates the activity is ok to be used. basically that al required fields have a value;
	*/
	public function validate(activity:Activity):Boolean{
		var isValid:Boolean;
		
		
		return isValid;
		
	}
	
	public function isGateActivity():Boolean{
		if (_activityTypeID == SYNCH_GATE_ACTIVITY_TYPE){
			return true;
		}else if(_activityTypeID == SCHEDULE_GATE_ACTIVITY_TYPE){
			return true
		}else if (_activityTypeID == PERMISSION_GATE_ACTIVITY_TYPE){
			return true;
		}else{
			return false;
		}
	}
	
	
	//getters and setters:
	public function set objectType(a:String):Void{
		_objectType = a;
	}
	public function get objectType():String{
		return _objectType;
	}

	/**
	 * 
	 * @usage   
	 * @param   newactivityTypeID 
	 * @return  
	 */
	public function set activityTypeID (newactivityTypeID:Number):Void {
		_activityTypeID = newactivityTypeID;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get activityTypeID ():Number {
		return _activityTypeID;
	}

	public function set activityID(a:Number):Void{
		_activityID = a;
	}
	public function get activityID():Number{
		return _activityID;
	}

/**
	 * 
	 * @usage   
	 * @param   newactivityCategoryID 
	 * @return  
	 */
	public function set activityCategoryID (newactivityCategoryID:Number):Void {
		_activityCategoryID = newactivityCategoryID;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get activityCategoryID ():Number {
		return _activityCategoryID;
	}

	/**
	 * 
	 * @usage   
	 * @param   newactivityUIID 
	 * @return  
	 */
	public function set activityUIID (newactivityUIID:Number):Void {
		_activityUIID = newactivityUIID;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get activityUIID ():Number {
		return _activityUIID;
	}
	
	public function set learningLibraryID(a:Number):Void{
		_learningLibraryID = a;
	}
	public function get learningLibraryID():Number{
		return _learningLibraryID;
	}	public function set learningDesignID(a:Number):Void{
		_learningDesignID = a;
	}
	public function get learningDesignID():Number{
		return _learningDesignID;
	}
	
	/**
	 * 
	 * @usage   
	 * @param   newlibraryActivityID 
	 * @return  
	 */
	public function set libraryActivityID (newlibraryActivityID:Number):Void {
		_libraryActivityID = newlibraryActivityID;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get libraryActivityID ():Number {
		return _libraryActivityID;
	}

/**
	 * 
	 * @usage   
	 * @param   newparentActivityID 
	 * @return  
	 */
	public function set parentActivityID (newparentActivityID:Number):Void {
		_parentActivityID = newparentActivityID;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get parentActivityID ():Number {
		return _parentActivityID;
	}
	
/**
	 * 
	 * @usage   
	 * @param   newparentUIID 
	 * @return  
	 */
	public function set parentUIID (newparentUIID:Number):Void {
		_parentUIID = newparentUIID;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get parentUIID ():Number {
		return _parentUIID;
	}
	
	/**
	 * 
	 * @usage   
	 * @param   neworderID 
	 * @return  
	 */
	public function set orderID (neworderID:Number):Void {
		_orderID = neworderID;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get orderID ():Number {
		return _orderID;
	}


	public function set title(a:String):Void{
		_title = a;
	}
	public function get title():String{
		return _title;
	}
	
	public function set description(a:String):Void{
		_description = a;
	}
	public function get description():String{
		return _description;
	}
	
	public function set helpText(a:String):Void{
		_helpText = a;
	}
	public function get helpText():String{
		return _helpText;
	}
	
	public function set xCoord(a:Number):Void{
		_xCoord = a;
	}
	public function get xCoord():Number{
		return _xCoord;
	}
	
	public function set yCoord(a:Number):Void{
		_yCoord = a;
	}
	public function get yCoord():Number{
		return _yCoord;
	}
		
	public function set libraryActivityUIImage(a:String):Void{
		_libraryActivityUIImage = a;
	}
	public function get libraryActivityUIImage():String{
		return _libraryActivityUIImage;
	}
	
	public function set runOffline(a:Boolean):Void{
		_runOffline = a;
	}
	public function get runOffline():Boolean{
		return _runOffline;
	}
	
	public function set offlineInstructions(a:String):Void{
		_offlineInstructions = a;
	}
	public function get offlineInstructions():String{
		return _offlineInstructions;
	}
	
	public function set defineLater(a:Boolean):Void{
		_defineLater = a;
	}
	public function get defineLater():Boolean{
		return _defineLater;
	}
	
	public function set createDateTime(a:Date):Void{
		_createDateTime = a;
	}
	public function get createDateTime():Date{
		return _createDateTime;
	}
	
	/**
	 * 
	 * @usage   
	 * @param   newgroupingID 
	 * @return  
	 */
	public function set groupingID (newgroupingID:Number):Void {
		_groupingID = newgroupingID;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get groupingID ():Number {
		return _groupingID;
	}

	
	/**
	 * 
	 * @usage   
	 * @param   newgroupingUIID 
	 * @return  
	 */
	public function set groupingUIID (newgroupingUIID:Number):Void {
		_groupingUIID = newgroupingUIID;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get groupingUIID ():Number {
		return _groupingUIID;
	}

	/**
	 * 
	 * @usage   
	 * @param   newapplyGrouping 
	 * @return  
	 */
	public function set applyGrouping (newapplyGrouping:Boolean):Void {
		_applyGrouping = newapplyGrouping;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get applyGrouping ():Boolean {
		return _applyGrouping;
	}
	
	/**
	 * 
	 * @usage   
	 * @param   newgroupingSupportType 
	 * @return  
	 */
	public function set groupingSupportType (newgroupingSupportType:Number):Void {
		_groupingSupportType = newgroupingSupportType;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get groupingSupportType ():Number {
		return _groupingSupportType;
	}

	
	

	

	
}