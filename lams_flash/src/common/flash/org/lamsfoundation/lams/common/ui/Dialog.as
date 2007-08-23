/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ************************************************************************
 */

/**
 * Dialog class for creating and managing dialogs.
 * Usage - Create a new dialog passing in the linkage ID of the symbol that makes it's contents
 * 
 * @author  DI
 */
import org.lamsfoundation.lams.common.*;
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.dict.*
import mx.containers.*
import mx.managers.*
import mx.events.*
//import org.lamsfoundation.lams.authoring.*


class org.lamsfoundation.lams.common.ui.Dialog {
    
	private static var _inputDialog:MovieClip;
	private static var _inputInstructions:String;
	private static var _inputTitle:String;
	private static var _inputMessage:String;
	private static var _inputType:Number;
    private static var _inputOkButtonLabel:String;
    private static var _inputCancelButtonLabel:String;
    private static var _inputOkHandler:Function;
    private static var _inputCancelHandler:Function;
	
    static function createPopUp(path:MovieClip,cls:Object, initobj:Object):MovieClip{
        return path.createClassChildAtDepth(cls, DepthManager.kTopmost, initobj);
    }
	
	static function createAlertDialog(title:String, msg:String, okButtonLabel:String, cancelButtonLabel:String, okHandler:Function, cancelHandler:Function, type:Number):MovieClip {
		_inputTitle = title;
		_inputMessage = msg;
		_inputOkButtonLabel = okButtonLabel;
		_inputCancelButtonLabel = cancelButtonLabel;
		_inputOkHandler = okHandler;
		_inputCancelHandler = cancelHandler;
		
		if(type != null) _inputType = type;
		
		var target:MovieClip;
		
		
		if(ApplicationParent.getInstance().getWorkspace().getWV().isOpen) {
			target = ApplicationParent.getInstance().getWorkspace().getWV().workspaceDialog;
		} else if(ApplicationParent.getInstance().dialog.content) {
			target = ApplicationParent.getInstance().dialog;
		} else {
			target = ApplicationParent.dialogue;
		}
		
		var _alertDialog:MovieClip = target.attachMovie('alertDialog', 'alertDialog' + new Date().toString(), DepthManager.kTopmost, {_x:0, _y:0});

		//Assign dialog load handler
		_alertDialog.addEventListener('contentLoaded', Proxy.create(org.lamsfoundation.lams.common.ui.Dialog,alertDialogLoaded));

		return _alertDialog;
	}
	
	static function createInputDialog(instructions:String, okButtonLabel:String, cancelButtonLabel:String, okHandler:Function, cancelHandler:Function){
		_inputInstructions = instructions;
		_inputOkButtonLabel = okButtonLabel;
		_inputCancelButtonLabel = cancelButtonLabel;
		_inputOkHandler = okHandler;
		_inputCancelHandler = cancelHandler;
		_inputDialog = PopUpManager.createPopUp(ApplicationParent.root, LFWindow, true,{title:Dictionary.getValue('ws_dlg_title'),closeButton:true,scrollContentPath:'InputDialog'});
		
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
	
	static function alertDialogLoaded(evt:Object) {
		
        Debugger.log('!evt.type:'+evt.type,Debugger.GEN,'alertDialogLoaded','org.lamsfoundation.lams.common.ui.Dialog');

        //Set up handlers and labels
        Debugger.log('!evt.target:'+evt.target,Debugger.GEN,'inputDialogLoaded','org.lamsfoundation.lams.common.ui.Dialog');
		evt.target.title = _inputTitle;
		evt.target.message = _inputMessage;
		evt.target.setOKButton(_inputOkButtonLabel,_inputOkHandler);
		evt.target.setCancelButton(_inputCancelButtonLabel,_inputCancelHandler);
		
		evt.target.type = (_inputType != null) ? _inputType : AlertDialog.ALERT;
		evt.target._visible = true;
	}

}