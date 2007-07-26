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


import org.lamsfoundation.lams.common.ApplicationParent;
import org.lamsfoundation.lams.common.ui.*;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.dict.*;

import mx.controls.Alert;
import mx.managers.*;

/**  
* LFMessage  
* 
* 
*/  
class LFMessage{  
  
     //Declarations  
	private var _ref:Object;
	private var _fn:String;
	private var title:String;
	 
    //Constructor  
	function LFMessage() {
		//enable accesibility for the Alert 
		mx.accessibility.AlertAccImpl.enableAccessibility();
		title = Dictionary.getValue('al_alert')
	}
  
  	
 /**
 * Shows an alert dialogue.  specify the OK Handler and icon.
 * @usage   
 * @param   msg       Message to display
 * @param   okHandler TO execute on click - use the proxy function
 * @param   icon      string linkage name of icon in library
 
  public static function showMessageAlert(msg ,okHandler, icon){
	var alt:Alert;
	//TODO: increase line breaks to account for stoopid bug in MMs alert.  
	// if an icon is being used then the width of the icon is not taken into account
	//msg += "\n  \n  \n  \n  ";
	Alert.okLabel = Dictionary.getValue('al_ok');
	
	if(okHandler != undefined){
	   alt = Alert.show(msg + newline + "\n ", Dictionary.getValue('al_alert') + "\t\t\t\t\t", Alert.OK, null, okHandler, null, Alert.OK);
	}else{
	   alt = Alert.show(msg + newline + "\n ", Dictionary.getValue('al_alert') + "\t\t\t\t\t", Alert.OK, null, null, null, Alert.OK);
	}
	
	alt.buttonWidth = 50;
	
  }
 */ 
  
  public static function showMessageAlert(msg, okHandler){
	var title:String = "<b>" + Dictionary.getValue('al_alert') + "</b>\n";
	  
	if(okHandler != undefined){
		Dialog.createAlertDialog(title, msg, Dictionary.getValue('al_ok'), null, okHandler, null, AlertDialog.ALERT);
	}else{
		Dialog.createAlertDialog(title, msg, Dictionary.getValue('al_ok'), null, null, null, AlertDialog.ALERT);
	}
  }
  
   public static function showMessageConfirm(msg:String, okHandler:Function, cancelHandler:Function, okLabel:String, cancelLabel:String,  msgTitle:String){
	if(msgTitle == null){
		msgTitle = Dictionary.getValue('al_confirm');
	}
	
	var title:String = "<b>" + msgTitle + "</b>\n";
	
	if(!okLabel){
		okLabel = Dictionary.getValue('al_ok');
	}
	
	if(!cancelLabel){
		cancelLabel = Dictionary.getValue('al_cancel');
	}
	
	Dialog.createAlertDialog(title, msg, okLabel, cancelLabel, okHandler, cancelHandler, AlertDialog.CONFIRM);
  }
  
 /**
 * Shows an alert confirm dialogue.  It is centred in the root time line and diplays the standard LAMS alert icon
 * @usage   
 * @param   msg     	The message to display
 * @param   handler		A handler for the click events broadcast when the buttons are clicked. In addition to the standard click event object properties, there is an additional detail property, which contains the flag value of the button that was clicked (Alert.OK, Alert.CANCEL, Alert.YES, Alert.NO). This handler can be a function or an object
 * @return  
 /
  public static function showMessageConfirm(msg:String,okHandler:Function, cancelHandler:Function, okLabel:String, cancelLabel:String,  msgTitle:String){
	var alt:Alert;
	if(msgTitle == null){
		msgTitle = Dictionary.getValue('al_confirm');
	}
	
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
	
	if(okLabel){
		Alert.okLabel = okLabel;
	}else{
		Alert.okLabel = Dictionary.getValue('al_ok');
	}
	if(cancelLabel){
		Alert.cancelLabel= cancelLabel;
	}else{
		Alert.cancelLabel= Dictionary.getValue('al_cancel');
	}
	alt = Alert.show(msg, msgTitle, Alert.OK | Alert.CANCEL, null, handlerObj, null, Alert.OK);
  }
  */
  
  public function get reference():Object{
   return _ref;
  }
	
  public function get fname():String{
	return _fn;
  }
  
}