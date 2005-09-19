import org.lamsfoundation.lams.common.util.*
import mx.controls.Alert;


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
  function LFError(msg:String,fn:String,ref:Object,debugInfo:String) {
	 super(msg);
	 _fn = fn;
	 _ref = ref;
	 Debugger.log('Creating LFError instance:'+msg,Debugger.CRITICAL,'LFError','LFError');
	 Debugger.log('Function:'+fn,Debugger.CRITICAL,'LFError','LFError');
	 Debugger.log('Ref:'+ref,Debugger.CRITICAL,'LFError','LFError');
	 Debugger.log('debugInfo:'+debugInfo,Debugger.CRITICAL,'LFError','LFError');
	 
  }
  
  public function showErrorAlert(okHandler){
	var a:Alert;
	if(okHandler != undefined){
	   a = Alert.show(message,"__Error__",Alert.OK,null,okHandler,"alertIcon_gen",Alert.OK);
	}else{
	   a = Alert.show(message,"__Error__",Alert.OK,null,null,"alertIcon_gen",Alert.OK);
	}
	
	a.setSize(500,250);
  }
  
  public function showErrorConfirm(okHandler:Function, cancelHandler:Function){

  }
  
  public function get reference():Object{
   return _ref;
  }
	
  public function get fname():String{
	return _fn;
  }

	 
}