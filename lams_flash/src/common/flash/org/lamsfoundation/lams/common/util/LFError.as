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

import org.lamsfoundation.lams.common.ui.*
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
	}
  
	public function showErrorAlert(okHandler){
		LFMessage.showMessageAlert(message, okHandler);
	}
	  
	/**
	* Shows an alert confirm dialogue.  It is centred in the root time line and diplays the standard LAMS alert icon
	* @usage   
	* @param   msg     	The message to display
	* @param   handler		A handler for the click events broadcast when the buttons are clicked. In addition to the standard click event object properties, there is an additional detail property, which contains the flag value of the button that was clicked (Alert.OK, Alert.CANCEL, Alert.YES, Alert.NO). This handler can be a function or an object
	* @return  
	*/
	public static function showSendErrorRequest(msg:String, msgTitle:String, okHandler:Function, cancelHandler:Function){
		var customTitle = Dictionary.getValue(msgTitle);
		LFMessage.showMessageConfirm(msg, okHandler, cancelHandler, Dictionary.getValue('al_send'), Dictionary.getValue('al_cancel'), customTitle);
	  
	}
	  
	public function get reference():Object{
	   return _ref;
	}
		
	public function get fname():String{
		return _fn;
	}
	 
}