import org.lamsfoundation.lams.common.util.*




/**
 * The proxy class, contains 1 static method
 *          Dave
 * @version 1.0
 * @since   
 */
class org.lamsfoundation.lams.common.util.Proxy {
	
	
	
	/**
	 * Creates a function that executes in the scope passed in as target, 
	 * not the scope it is actually executed in.  
	 * Like MMs delegate function but can accept parameters and pass them onto 
	 * the function
	 * @usage   
	 * @param   oTarget   the scope the function should execute in
	 * @param   fFunction the function to execute, followed by any other parameters to pass on.
	 * @return  
	 */
	public static function create (oTarget:Object, fFunction:Function):Function	{
		var parameters:Array = new Array ();
		var l = arguments.length;
		
		for (var i = 2; i < l; i++) {
			parameters[i - 2] = arguments[i];
		}
		
		var fProxy:Function = function (){
			var totalParameters:Array = arguments.concat (parameters);
			fFunction.apply (oTarget, totalParameters);
		};
		return fProxy;
	}
}
