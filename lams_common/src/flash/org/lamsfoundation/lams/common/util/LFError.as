import org.lamsfoundation.lams.common.util.*

/**  
* LFError  
* 
* 
*/  
class LFError extends Error{  
  
     //Declarations  
	 private var _ref:Object;
	 private var _fn:String;
	 
     //Constructor  
  function LFError(msg:String,fn:String,ref:Object) {
	 super(msg);
	 _fn = fn;
	 _ref = ref;
	 
  }
  
  public function get reference():Object{
	   return _ref;
	}
	
	public function get fname():String{
		return _fn;
	}

	 
}