import org.lamsfoundation.lams.authoring.*;
/*
* This class represents all the complex activity types.  they are not much different, so we can handle them in one class.
* For reference these are the activity types
* <pre><code>
* public static var PARALLEL_ACTIVITY_TYPE:Number = 6;
* public static var OPTIONS_ACTIVITY_TYPE:Number = 7;
* public static var SEQUENCE_ACTIVITY_TYPE:Number = 8;
* </code></pre>
* @author      DC
* @version     0.1
* @see		   Activity
*/
class ComplexActivity extends Activity{
	
	private var _maxOptions:Number;
	private var _minOptions:Number;
	private var _optionsInstructions:String;
	
	
	
	function ComplexActivity(activityUIID:Number){
		super(activityUIID);
	}
	
	
	public function populateFromDTO(dto:Object){
		super.populateFromDTO(dto);
		if(_activityTypeID == Activity.OPTIONS_ACTIVITY_TYPE){
			_maxOptions = dto.maxOptions;
			_minOptions = dto.minOptions;
			//TODO: This is missing in the Library packet - tell mai.
			_optionsInstructions = dto.optionsInstructions;
		}
	}
	
	/**
	 * Used by OPTIONS_ACTIVITY_TYPE
	 * @usage   
	 * @param   newmaxOptions 
	 * @return  
	 */
	public function set maxOptions (newmaxOptions:Number):Void {
		_maxOptions = newmaxOptions;
	}
	/**
	 * used by OPTIONS_ACTIVITY_TYPE
	 * @usage   
	 * @return  
	 */
	public function get maxOptions ():Number {
		return _maxOptions;
	}

	
	/**
	 * used by OPTIONS_ACTIVITY_TYPE
	 * @usage   
	 * @param   newminOptions 
	 * @return  
	 */
	public function set minOptions (newminOptions:Number):Void {
		_minOptions = newminOptions;
	}
	/**
	 * used by OPTIONS_ACTIVITY_TYPE
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

