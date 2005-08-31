import org.lamsfoundation.lams.authoring.*;
/*
*
* @author      DC
* @version     0.1
* @comments    Tool Activity Data storage class. 
* @see		   Activity
*/
class ToolActivity extends Activity{
	
	private var _toolContentID:Number;
	private var _toolID:Number;
	

	
	function ToolActivity(activityUIID:Number, activityTypeID:Number, activityCategoryID:Number, learningLibraryID:Number,libraryActivityUIImage:String, toolContentID:Number, toolID:Number){
		super(activityUIID, activityTypeID, activityCategoryID, learningLibraryID,libraryActivityUIImage);
		_toolContentID = toolContentID;
		_toolID = toolID;
	}
	

	/**
	 * 
	 * @usage   
	 * @param   newtoolContentID 
	 * @return  
	 */
	public function set toolContentID (newtoolContentID:Number):Void {
		_toolContentID = newtoolContentID;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get toolContentID ():Number {
		return _toolContentID;
	}

	/**
	 * 
	 * @usage   
	 * @param   newtoolID 
	 * @return  
	 */
	public function set toolID (newtoolID:Number):Void {
		_toolID = newtoolID;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get toolID ():Number {
		return _toolID;
	}


}

