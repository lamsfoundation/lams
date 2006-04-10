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

import org.lamsfoundation.lams.authoring.cv.*
import org.lamsfoundation.lams.authoring.tk.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.authoring.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.* 
import mx.managers.*
import mx.utils.*
/**
 * Popup method for opening browser popup window using javascript in author.js file.
 * @author  Pradeep Sharma
 * @version 1.1
 * @since   
 */
class JsPopup{
	
	private static var _instance:JsPopup = null;
	


	/**
	 * Constructor Function
	 * @usage   
	 * @return  
	 */
	private function JsPopup(){
		
	}
	
	
	/**
	 * To create an instance of JsPopup class if does not exist to access all the public methods of this class abs sub classes.
	 * @usage   	create an Instance of the JsPopup class if there is none
	 * @return  	The instance to the caller using Singleton Method.
	 */
	public static function getInstance():JsPopup{
		if (JsPopup._instance == null){
			JsPopup._instance = new JsPopup();
		}
		return JsPopup._instance;
	}
	
	/**
	 * 
	 * @usage   
	 * @param   url   			URL of the lession or activity to be opened in the Popup window.      
	 * @param   windowTitle 	Title of the Window, assigning a new title on every click will open 
	 * 							a new popup window, so be carefull of this param if you don't want to open too many windows. 
	 * @param   height      	height of the popup window
	 * @param   width       	width of the popup window
	 * @param   resize      	true or false based on preference to allow resizing of window.
	 * @param   status      	true or false for showing a status bar in a popup window.
	 * @param   scrollbar   	true or false, treu for having a scroll bar and false for not having onw.
	 * @return  				returns nothing.
	 */
	public function launchPopupWindow(url:String, windowTitle:String, height:Number, width:Number, resize:Boolean, status:Boolean, scrollbar:Boolean):Void{
			//(args, title, h, w, resize, status, scrollbar)
			getURL(url,"_blank");
			//getURL("javascript:openPopUp("+url+", "+windowTitle+");");
	}
	
}