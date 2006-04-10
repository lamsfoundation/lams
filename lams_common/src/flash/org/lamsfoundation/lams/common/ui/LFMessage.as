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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
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
 */
  public static function showMessageAlert(msg ,okHandler, icon){
	var alt:Alert;
	//TODO: increase line breaks to account for stoopid bug in MMs alert.  
	// if an icon is being used then the width of the icon is not taken into account
	//msg += "\n  \n  \n  \n  ";
	Alert.okLabel = Dictionary.getValue('al_ok');
	if(okHandler != undefined){
	   //alt = Alert.show(msg,"__Message__",Alert.OK,null,okHandler,"alertIcon_gen",Alert.OK);
	   alt = Alert.show(msg,Dictionary.getValue('al_alert'),Alert.OK,null,okHandler,null,Alert.OK);
	}else{
	   alt = Alert.show(msg,Dictionary.getValue('al_alert'),Alert.OK,null,null,null,Alert.OK);
	   //alt = super.show(msg,"__Message__",Alert.OK,null,null,"alertIcon_gen",Alert.OK);
	}
	
	//alt.setSize(800,250);
  }
  
/**
 * Shows an alert confirm dialogue.  It is centred in the root time line and diplays the standard LAMS alert icon
 * @usage   
 * @param   msg     	The message to display
 * @param   handler		A handler for the click events broadcast when the buttons are clicked. In addition to the standard click event object properties, there is an additional detail property, which contains the flag value of the button that was clicked (Alert.OK, Alert.CANCEL, Alert.YES, Alert.NO). This handler can be a function or an object
 * @return  
 */
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
	alt = Alert.show(msg,msgTitle,Alert.OK | Alert.CANCEL, null, handlerObj, null, Alert.OK);
  }
  
  public function get reference():Object{
   return _ref;
  }
	
  public function get fname():String{
	return _fn;
  }
  
  
  /**
  * this function is in mx.controls.alertClasses.AlertForm and i think it has a bug in getting a size with an icon
  * 
* @private
* get size according to contents of form 
	
	function getSize(Void):Object
	{
		trace("DAVES OVERRIDE");
		var s:Object = new Object();
		s.height = buttons[0].height + (3 * 8);
		var tf2:Object = _parent.back_mc.title_mc._getTextFormat();
		extent = tf2.getTextExtent2(_parent.title) ;
 		s.width = Math.max( Math.max(2, buttons.length) * (buttons[0].width + 8),(extent.width) + 4 + 8);
		var tf:Object = text_mc._getTextFormat();
 		extent = tf.getTextExtent2(_parent.text);
 
 		// stick the text in the measuring TextField and let it flow baby
 		textMeasure_mc._width = 2*s.width;
 		textMeasure_mc.setNewTextFormat(text_mc._getTextFormat());
 		textMeasure_mc.text = _parent.text;
 		
 		// now the TextField height should have been adjusted since its' autoFlow
 		s.height += textMeasure_mc.textHeight + 8;
 		var numlines:Number = Math.ceil(textMeasure_mc.textHeight / extent.height);
 
		if (numlines > 1)
		{
			extent.width = 2* s.width;
			text_mc.wordWrap = true;
		}
			

//width is larger of buttons or text but not more than twice as wide as buttons
//  add extra 8 to extent.width for the 8 pixel galley on the right side

		var width:Number = Math.min(extent.width + 4 + 8, 2 * s.width);
		var bWidth = s.width;
		s.width = Math.max(width, s.width) + 8 ; 
		if (icon_mc != undefined)
		{

//calculate the additional width if we add the icon

			extent.width += icon_mc.width + 8;
			width = Math.min(extent.width + 4 + 8, 2 * bWidth);
			s.width = Math.max(width, s.width) + 8 ; 

//increase size if bigger

			var i:Number = icon_mc.height - (numlines * (extent.height + 4));
			if (i > 0)
				s.height += i;
		}
		return s;
	}
	 */
}