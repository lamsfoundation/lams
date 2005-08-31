import org.lamsfoundation.lams.authoring.*;
/*
*
* @author      DC
* @version     0.1
* @comments    Tool Activity Data storage class. 
* @see		   Activity
*/
class ComplexActivity extends Activity{
	
	private var _maxOptions:Number;
	private var _minOptions:Number;
	private var _optionsInstructions:String;
	
	
	
	function ComplexActivity(activityUIID:Number, activityTypeID:Number, activityCategoryID:Number, learningLibraryID:Number,libraryActivityUIImage:String){
		super(activityUIID, activityTypeID, activityCategoryID, learningLibraryID,libraryActivityUIImage);
	}
	
	/**
	 * 
	 * @usage   
	 * @param   newmaxOptions 
	 * @return  
	 */
	public function set maxOptions (newmaxOptions:Number):Void {
		_maxOptions = newmaxOptions;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get maxOptions ():Number {
		return _maxOptions;
	}

	
	/**
	 * 
	 * @usage   
	 * @param   newminOptions 
	 * @return  
	 */
	public function set minOptions (newminOptions:Number):Void {
		_minOptions = newminOptions;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get minOptions ():Number {
		return _minOptions;
	}

	
	/**
	 * 
	 * @usage   
	 * @param   newoptionsInstructions 
	 * @return  
	 */
	public function set optionsInstructions (newoptionsInstructions:String):Void {
		_optionsInstructions = newoptionsInstructions;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get optionsInstructions ():String {
		return _optionsInstructions;
	}

	
	


	

}

