/**
 * Dialog class for creating and managing dialogs.
 * Usage - Create a new dialog passing in the linkage ID of the symbol that makes it's contents
 * 
 * @author  DI
 */

import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.dict.*
import mx.containers.*
import mx.managers.*
import mx.events.*
import org.lamsfoundation.lams.authoring.*

class org.lamsfoundation.lams.common.ui.Dialog {
    
	private static var _inputDialog:MovieClip;
	private static var _inputInstructions:String;
    private static var _inputOkButtonLabel:String;
    private static var _inputCancelButtonLabel:String;
    private static var _inputOkHandler:Function;
    private static var _inputCancelHandler:Function;
	
//   
    //private static var _dialogInstances:Array;
    //private static var _currentIndex:Number;
//    
    ///**
    //* This will return a reference to the Dialog that is currently in focus. If there are none in focus is will return null
    //*/
    //public static function getDialogWithFocus():Object{
        //_dialogInstances = [];
        //_currentIndex = null;
        //return null;
    //}
//    
//  
    ///**
    //* Constuctor
    //* @param target The target movieclip in which
    //*/
    //function Dialog(target:MovieClip,linkageId:String,x:Number,y:Number,w:Number,h:Number){
        ////Create a new window for the dialog
        //trace('Dialog');
        ////componentInstance.createClassObject(linkageName, instanceName, depth, initObject);
    //}
//

    static function createPopUp(path:MovieClip,cls:Object, initobj:Object):MovieClip{
        trace('Dialog.createPopUp');
        return path.createClassChildAtDepth(cls, DepthManager.kTopmost, initobj);
    }
	
	static function createInputDialog(instructions:String, okButtonLabel:String, cancelButtonLabel:String, okHandler:Function, cancelHandler:Function){
		_inputInstructions = instructions;
		_inputOkButtonLabel = okButtonLabel;
		_inputCancelButtonLabel = cancelButtonLabel;
		_inputOkHandler = okHandler;
		_inputCancelHandler = cancelHandler;
		_inputDialog = PopUpManager.createPopUp(Application.root, LFWindow, true,{title:Dictionary.getValue('ws_dlg_title'),closeButton:true,scrollContentPath:'InputDialog'});
		//Assign dialog load handler
		_inputDialog.addEventListener('contentLoaded',Proxy.create(org.lamsfoundation.lams.common.ui.Dialog,inputDialogLoaded));
	}
	
	static function inputDialogLoaded(evt:Object) {
		
        Debugger.log('!evt.type:'+evt.type,Debugger.GEN,'inputDialogLoaded','org.lamsfoundation.lams.common.ui.Dialog');
      
        //Set up handlers and labels
        Debugger.log('!evt.target.scrollContent:'+evt.target.scrollContent,Debugger.GEN,'inputDialogLoaded','org.lamsfoundation.lams.common.ui.Dialog');
		evt.target.scrollContent.setInstructionsLabel(_inputInstructions);
		evt.target.scrollContent.setOKButton(_inputOkButtonLabel,_inputOkHandler);
		evt.target.scrollContent.setCancelButton(_inputCancelButtonLabel,_inputCancelButtonLabel);
		

	}

}