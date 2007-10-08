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
  
	public static function showMessageAlert(msg, okHandler){
		var title:String = "<b>" + Dictionary.getValue('al_alert') + "</b>\n";
		var _dialog:MovieClip;
		
		if(okHandler != undefined){
			_dialog = Dialog.createAlertDialog(title, msg, Dictionary.getValue('al_ok'), null, okHandler, null, AlertDialog.ALERT);
		}else{
			_dialog = Dialog.createAlertDialog(title, msg, Dictionary.getValue('al_ok'), null, null, null, AlertDialog.ALERT);
		}
	}
  
    public static function showMessageConfirm(msg:String, okHandler:Function, cancelHandler:Function, okLabel:String, cancelLabel:String,  msgTitle:String){
		var _dialog:MovieClip;
		
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
		
		_dialog = Dialog.createAlertDialog(title, msg, okLabel, cancelLabel, okHandler, cancelHandler, AlertDialog.CONFIRM);
	}
  
	public function get reference():Object{
	   return _ref;
	}
		
	public function get fname():String{
		return _fn;
	}
  
}