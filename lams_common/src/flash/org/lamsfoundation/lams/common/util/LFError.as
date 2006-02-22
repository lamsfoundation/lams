import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.dict.*
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
	 private var title:String;
	 	 
     //Constructor  
  function LFError(msg:String,fn:String,ref:Object,debugInfo:String) {
	 super(msg);
	 _fn = fn;
	 _ref = ref;
	 Debugger.log('Creating LFError instance:'+msg,Debugger.CRITICAL,'LFError','LFError');
	 Debugger.log('Function:'+fn,Debugger.CRITICAL,'LFError','LFError');
	 Debugger.log('Ref:'+ref,Debugger.CRITICAL,'LFError','LFError');
	 Debugger.log('debugInfo:'+debugInfo,Debugger.CRITICAL,'LFError','LFError');
	
	 //title = Dictionary.getValue('al_alert')
	 
  }
  
  public function showErrorAlert(okHandler){
	title = Dictionary.getValue('al_alert')
	var a:Alert;
	if(okHandler != undefined){
		
		//TODO: Fix the problem of size calculation with icons
	   //a = Alert.show(message,"__Error__",Alert.OK,null,okHandler,"alertIcon_gen",Alert.OK);
	   a = Alert.show(message,title,Alert.OK,null,okHandler,null,Alert.OK);
	}else{
	   //a = Alert.show(message,"__Error__",Alert.OK,null,null,"alertIcon_gen",Alert.OK);
	   a = Alert.show(message,title,Alert.OK,null,null,null,Alert.OK);
	}
	
	a.setSize(500,250);
  }
  /**
 * Shows an alert confirm dialogue.  It is centred in the root time line and diplays the standard LAMS alert icon
 * @usage   
 * @param   msg     	The message to display
 * @param   handler		A handler for the click events broadcast when the buttons are clicked. In addition to the standard click event object properties, there is an additional detail property, which contains the flag value of the button that was clicked (Alert.OK, Alert.CANCEL, Alert.YES, Alert.NO). This handler can be a function or an object
 * @return  
 */
  public static function showSendErrorRequest(msg:String, msgTitle:String, okHandler:Function, cancelHandler:Function){
	var alt:Alert;
	var customTitle = Dictionary.getValue(msgTitle)
	var handlerObj = new Object();
	handlerObj.click = function(e){
		if(e.detail == Alert.OK){
			okHandler();
		}else if(e.detail == Alert.CANCEL){
			cancelHandler();
		}else{
			Debugger.log('Unknown event detail form confirm:'+e.detail,Debugger.CRITICAL,"showMessageConfirm",'LFMessage');		
		}
	}
	
	alt = Alert.show(msg, customTitle ,Alert.OK | Alert.CANCEL, null, handlerObj, null, Alert.OK);
	//var winHeight = alt.title._height + alt.content._height + alt.buttonHeight + 10;
	alt.setSize(250, 600);
  }
  
  
/*  
  public function showErrorConfirm(okHandler:Function, cancelHandler:Function){

  }
  */
  public function get reference():Object{
   return _ref;
  }
	
  public function get fname():String{
	return _fn;
  }

	 
}