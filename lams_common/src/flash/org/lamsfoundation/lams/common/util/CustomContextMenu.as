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
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.authoring.cv.*
/**
* DTO  Generic data transfer obj
*/
class CustomContextMenu {
	//Declarations
	//signifies update or errrr another type
	
	private var rootMenu:ContextMenu;
	private var authorMenu:ContextMenu;
	private var menuArr:Array;
	//private var _pi:PropertyInspectorNew;
	
	//ContextMenu instance is stored as a static in the CustomContextMenu class
    private static var _instance:CustomContextMenu = null;    
	
    //Defined so compiler can 'see' events added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	//Constructor
	
	private function CustomContextMenu (){
		
		
		//To hide builtin menu for root
		rootMenu = new ContextMenu();
		
		rootMenu.hideBuiltInItems();  
		_root.menu = rootMenu;
		authorMenu = new ContextMenu();
		authorMenu.hideBuiltInItems();
		//To show hide menu for activities
		
		
		mx.events.EventDispatcher.initialize(this);
	}
	
	  /**
    * Retrieves an instance of the Application singleton
    */ 
    public static function getInstance():CustomContextMenu{
        if(CustomContextMenu._instance == null){
            CustomContextMenu._instance = new CustomContextMenu();
        }
        return CustomContextMenu._instance;
    }
	
	/**
	* Load the dictionary for the language specified
	* @param language (string) language parameter.
	*/
	public function loadMenu(cmType:String):Array {
	
       trace("Value for this: "+this)
		var v:Boolean; 
		var myCopy:Array = new Array();
		var menuArr:Array = new Array();
		if (cmType == "activity"){
			var v = true;
		}else {
			var v = false;
		}
		menuArr[0] = ["Open/Edit Activity Content", getOpenEditActivtiyContent, false, v, true];
		menuArr[1] = ["Copy Activity", getCopy, false, v, true];
		menuArr[2] = ["Paste Activity",getPaste, false, v, true];
		menuArr[3] = ["Property Inspector...",getPI, true, true, true];
		menuArr[4] = ["Author Help",getPaste, true, false, true];
		
		for (var i=0; i<menuArr.length; i++){
			var myObj:Object = new Object();
			myObj.cmlabel = menuArr[i][0];
			myObj.handler = menuArr[i][1]; 
			myObj.isSeparator = menuArr[i][2]; 
			myObj.isEnable= menuArr[i][3]; 
			myObj.isVisible = menuArr[i][4]; 
			//if (menuArr[i][0] == "Copy Activity"){
				//menuArr[i].separatorBefore = true;
			//}
			myCopy[i]= myObj;			
		} 
		return myCopy;
		
	}
	
	public function showCustomCM(cmItems:Object){
		//authorMenu.customItems = new Array();
		//trace("CM Item label: "+cmItems.length)
		for (var j=0; j<authorMenu.customItems.length; j++){
			trace("removing item: "+j)
			authorMenu.customItems.splice(0)
		}
		for (var i=0; i<cmItems.length; i++){
			trace("CM seperater: "+cmItems[i].isSeparator)
			trace("CM enabled: "+cmItems[i].isEnable)
			trace("CM visible: "+cmItems[i].isVisible)
			var menuItem_cmi:ContextMenuItem = new ContextMenuItem(cmItems[i].cmlabel.toString(), cmItems[i].handler, cmItems[i].isSeparator, cmItems[i].isEnable, cmItems[i].isVisible);
			authorMenu.customItems.push(menuItem_cmi);
		}
	
		//authorMenu.onSelect = itemSelected; //all purpose func called when any menu item has been selected
		//trace("mvieclip passed is: "+mc)
		_root.menu = authorMenu;
	}
	
	
	//---------------------------------
	
	public function getOpenEditActivtiyContent(){
		Application.getInstance().openEditActivtiyContent();
	}
	public function getCopy(){
		Application.getInstance().copy();
	}
	public function getPaste(){
		Application.getInstance().paste();
	}
	
	public function getPI(){
		Application.getInstance().expandPI();
		
	}		
}
