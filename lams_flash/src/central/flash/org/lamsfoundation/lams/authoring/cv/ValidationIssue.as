class org.lamsfoundation.lams.authoring.cv.ValidationIssue {
	
	private var _code:String;
	private var _message:String;
	private var _UIID:Number
	
	
	public static var TRANSITION_ERROR_KEY:String  = "validation_error_transitionNoActivityBeforeOrAfter";				// T
	public static var ACTIVITY_TRANSITION_ERROR_KEY:String = "validation_error_activityWithNoTransition";				// AT
	public static var INPUT_TRANSITION_ERROR_TYPE1_KEY:String = "validation_error_inputTransitionType1";				// IT
	public static var INPUT_TRANSITION_ERROR_TYPE2_KEY:String = "validation_error_inputTransitionType2";  				
	public static var OUTPUT_TRANSITION_ERROR_TYPE1_KEY:String = "validation_error_outputTransitionType1";				// OT
	public static var OUTPUT_TRANSITION_ERROR_TYPE2_KEY:String = "validation_error_outputTransitionType2";
	
	public static var TRANSITION_ERROR_CODE:String = "T";
	public static var ACTIVITY_TRANSITION_ERROR_CODE:String = "AT";
	public static var INPUT_TRANSITION_ERROR_CODE:String = "IT";
	public static var OUTPUT_TRANSITION_ERROR_CODE:String = "OT1";
	
	function ValidationIssue(code:String, message:String, UIID:Number) {
		_code = code;
		_message = message;
		_UIID = UIID;
	}
	
	function get message():String {
		return _message;
	}
	
	function get UIID():Number {
		return _UIID;
	}
	
}
	
	