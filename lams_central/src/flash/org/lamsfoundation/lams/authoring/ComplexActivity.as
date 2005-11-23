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
	
	
	/**
	 * Creates a complex activity from a dto... which is nice
	 * @usage   
	 * @param   dto 
	 * @return  
	 */
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
	 * Creates an object containing all the props of the ComplexActivity.  
	 * If a value is null then it is ommitted... if itsd the null value from const 
	 * then its included
	 * @usage   
	 * @return  the DTO
	 */
	public function toData():Object{
		var dto:Object = super.toData();
		if(_activityTypeID == Activity.OPTIONS_ACTIVITY_TYPE){
			if(_maxOptions){	dto.maxOptions = _maxOptions;		}
			if(_minOptions){	dto.minOptions = _minOptions;		}
			if(_optionsInstructions){	dto.optionsInstructions = _optionsInstructions;		}
		}
		return dto;
	}
	
	/**
	 * Creates an exact copy of this ComplexActivity
	 * @usage   
	 * @return  the copy
	 */
	public function clone():ComplexActivity{
		var dto:Object = toData();
		var ca = new ComplexActivity();
		ca.populateFromDTO(dto);
		return ca;
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

