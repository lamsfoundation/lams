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
			getURL("javascript:openPopUp("+url+", "+windowTitle+", "+height+", "+width+", "+resize+", "+status+", "+scrollbar+")");
	}
	
}