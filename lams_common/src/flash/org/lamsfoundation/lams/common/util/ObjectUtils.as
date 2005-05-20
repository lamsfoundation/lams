import org.lamsfoundation.lams.common.util.*

/**  
* Recursivly prints out all the values in an object
* 
* 
*/  
class ObjectUtils{
	
	
	/**
	* Recursively goes through and object and prints out property values
	* @usage   printObject(_global);
	* @param   target 
	* @return  
	*/
	public static function printObject(target) {  
		for(var o in target){  
			//trace('object:' + o  + ' value:' + target[o]);  
			Debugger.log(o  + ':' + target[o],Debugger.GEN,'printObject','org.lamsfoundation.lams.common.util.ObjectUtils');
			printObject(target[o]);  
		}  
		return;  
	} 
}
