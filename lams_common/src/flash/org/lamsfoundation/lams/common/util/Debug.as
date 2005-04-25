import org.lamsfoundation.lams.common.util.*/**  
* Debug  
* Can be used to print message to a floating windoe and to trace windoe.  SHoudl be used over trace().  
* Usage:
* import org.lamsfoundation.lams.common.util.Debug;
* 
* Debug.log('_toolkit_mc:'+_toolkit_mc,5,'createToolkit','ToolkitView');
* 
* 
*/  
class Debug {  
	
     //Declarations  
    private static var _severityLevel:Number = 5;
	private static var _allowDebug:Boolean = true;
	
	private static var _currentClass:String;
	//Constructor  
	function Debug() {  
		
	}  
	
	
	public function set allowDebug(arg:Boolean):Void{
			_allowDebug = arg;
	}
	
	public function get allowDebug():Boolean{
			return _allowDebug;
	}
	
	public function set severityLevel(sLevel:Number):Void{
			_severityLevel = sLevel;
	}
	
	public function get severityLevel():Number{
			return _severityLevel;
	}

			/**
	* Method to print a message to the output - trace or window...
	* @param msg 			The actual message to be printed
	* @param level 			(Optional) Severity of this messgae, 1=critical error > 5 = general debugging message
	* @param fname 			(Optional) Name of the function calling this log message
	* @param currentClass 	(Optional)Name of the class
	*/
	public static function log(msg:String,level:Number,fname:String,currentClass:Object):Void{
	
		if(_allowDebug){
			if(arguments.length == 1){
				level = 5;
			}			
			if(_severityLevel == level){
				//trace('currentClass :' + currentClass);
				//if the class name has changed, then print it out
				if(_currentClass != currentClass){
					_currentClass = String(currentClass);
					trace("In:"+_currentClass);
					
				}
				
				trace("["+fname+"]"+msg);
			}
		}
	}
	
}