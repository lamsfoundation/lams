import org.lamsfoundation.lams.common.util.*
class org.lamsfoundation.lams.common.util.Proxy {
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
