/***************************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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

import org.lamsfoundation.lams.learner.*;
import org.lamsfoundation.lams.learner.ls.*;
import org.lamsfoundation.lams.common.Sequence;
import org.lamsfoundation.lams.common.ToolTip;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.dict.*;
import org.lamsfoundation.lams.common.style.*;
import org.lamsfoundation.lams.common.ApplicationParent;
import mx.controls.*
import mx.utils.*
import mx.managers.*
import mx.events.*

class Presence extends MovieClip {
	
	//Height Properties
	private var presenceHeightHide:Number = 20;
	private var presenceHeightFull:Number = 217;
	
	//Open Close Identifier
	private var _presenceIsExpanded:Boolean;
	
	//Presence stuff
	private var selectedChatItem:Object;
	private var lastClickX:Number;
	private var lastClickY:Number;
	private var clickInterval:Number;
	private var dataGridInitialized:Boolean;
	private var DOUBLECLICKSPEED:Number = 500;
	private var chatDialog:MovieClip;
	
	//Component properties
	private var _presence_mc:MovieClip;
	private var presenceHead_pnl:MovieClip;
	private var presenceTitle_lbl:Label;
	private var presenceMinIcon:MovieClip;
	private var presenceMaxIcon:MovieClip;
	private var presenceClickTarget_mc:MovieClip;
	private var _tip:ToolTip;
	private var _users_dg:DataGrid;
	
	private var panel:MovieClip;
	private var _lessonModel:LessonModel;
	private var _lessonController:LessonController;
	private var _tm:ThemeManager;
	private var _dictionary:Dictionary;
	
	//These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
	private var dispatchEvent:Function;     
	public var addEventListener:Function;
	public var removeEventListener:Function;
   
	// Constructor
	public function Presence() {
		Debugger.log('PRESENCE: started constructor',Debugger.MED,'Presence','Presence');
		
		// Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);
		
		_tm = ThemeManager.getInstance();
		_tip = new ToolTip();
		_dictionary = Dictionary.getInstance();
		_dictionary.addEventListener('init',Proxy.create(this,setLabels));
		this._visible = false;
		
		Stage.addListener(this);

		// Let it wait one frame to set up the components.
		MovieClipUtils.doLater(Proxy.create(this,init));
		Debugger.log('PRESENCE: calling init method',Debugger.MED,'Presence','Presence');
	}
	
    // Called a frame after movie attached to allow components to initialise
    public function init(){
		Debugger.log('PRESENCE: started init',Debugger.MED,'init','Presence');
			
        //Delete the enterframe dispatcher
        delete this.onEnterFrame;
		_lessonModel = _lessonModel;
		_lessonController = _lessonController;
		_presence_mc = this;
		_presenceIsExpanded = true;
		dataGridInitialized = false;
		presenceMaxIcon._visible = false;
		presenceMinIcon._visible = true;
		dataGridInitialized = false;
		_lessonModel.setPresenceHeight(presenceHeightFull);
		setLabels();
		resize(Stage.width);
	
		presenceClickTarget_mc.onRelease = Proxy.create(this, localOnRelease);
		
		this.onEnterFrame = setLabels;
	}	
	
	// Click handler
	private function localOnRelease():Void{
		if (_presenceIsExpanded){
			_presenceIsExpanded = false
			presenceMinIcon._visible = false;
			presenceMaxIcon._visible = true;
			_lessonModel.setPresenceHeight(presenceHeightHide);
			
		}else {
			_presenceIsExpanded = true
			presenceMinIcon._visible = true;
			presenceMaxIcon._visible = false;
			_lessonModel.setPresenceHeight(presenceHeightFull);
			//Application.getInstance().onResize();
		}
	}
	
	// Attempts a connection with a given Jabber server
	public function attemptConnection():Void{
		Debugger.log('PRESENCE: attempting to connect through Javascript',Debugger.MED,'attemptConnection','Presence');
		
		var myDate = new Date();
		var h = myDate.getHours().toString(), m = myDate.getMinutes().toString(), s = myDate.getSeconds().toString();
		var resource = "LAMSPRESENCE"+h+""+m+""+s;
		
		Debugger.log("PRESENCE: with arguements - " + String(_root.presenceServerUrl) + " " + String(_root.userID) + " " + String(_root.userID) + " " + String(resource) + " " + String(_root.lessonID) + " " + _root.firstName + _root.lastName + " " + "false" + " " + "true",Debugger.MED,'attemptConnection','Presence');
		_root.proxy.call("doLogin", _root.presenceServerUrl, _root.userID, _root.userID, resource, _root.lessonID, _root.firstName + _root.lastName, false, true);
	}
	
	public function isPresenceExpanded():Boolean{
		return _presenceIsExpanded;
	}
	
	public function presenceFullHeight():Number{
		return presenceHeightFull;
	}
	
	public function showToolTip(btnObj, btnTT:String):Void{
		var Xpos = Application.HEADER_X+ 5;
		var Ypos = Application.HEADER_Y+( btnObj._y+btnObj._height)+2;
		var ttHolder = ApplicationParent.tooltip;
		var ttMessage = Dictionary.getValue(btnTT);
		var ttWidth = 150
		_tip.DisplayToolTip(ttHolder, ttMessage, Xpos, Ypos, undefined, ttWidth);
	}
	
	public function hideToolTip():Void{
		_tip.CloseToolTip();
	}
	
	private function setStyles(){
		Debugger.log('PRESENCE: setting styles',Debugger.MED,'setStyles','Presence');
		var styleObj = _tm.getStyleObject('smallLabel');
		
		styleObj = _tm.getStyleObject('textarea');
		presenceTitle_lbl.setStyle('styleName', styleObj);
		
		//For Panels
		styleObj = _tm.getStyleObject('BGPanel');
		presenceHead_pnl.setStyle('styleName',styleObj);
	}
	
	private function setLabels(){
		Debugger.log('PRESENCE: setting labels',Debugger.MED,'setLabels','Presence');
		
		presenceTitle_lbl.text = Dictionary.getValue('pres_panel_lbl');
		setStyles();
		setupDataGrid(null);
		delete this.onEnterFrame; 
		dispatchEvent({type:'load',target:this});
        
	}
	
	public function setupDataGrid(dataProvider:Array){
		// If datagrid is not initialized, do so
		if(!dataGridInitialized) {
			Debugger.log('PRESENCE: setting up dataGrid',Debugger.MED,'setLabels','Presence');
			
			_users_dg.addEventListener("cellPress", Proxy.create(this, cellPress));
			_users_dg.columnNames = ["nick"];
			
			var col:mx.controls.gridclasses.DataGridColumn;
			col = _users_dg.getColumnAt(0);
			col.headerText = Dictionary.getValue('pres_colnamelearners_lbl');
			col.width = _users_dg._width;
			dataGridInitialized = true;
		}

		// If dataprovider is null (called from Presence), set label to loading and wait for call from javascript
		if(!dataProvider) {
			_users_dg.dataProvider = [{nick:Dictionary.getValue('pres_dataproviderloading_lbl')}];
		}
		// Initialize dataprovider from javascript
		else {
			_users_dg.dataProvider = dataProvider;
			_users_dg.sortItemsBy("nick");
			_users_dg.invalidate();
		}
		

	}

	private function cellPress(event) {
		//	I'm not clicking on an empty cell
		if (event.target.selectedItem.nick != undefined) {
			//Double click action
			if (clickInterval != null && _users_dg.selectedItem.nick == selectedChatItem.nick) {
				Debugger.log('PRESENCE: double click event',Debugger.MED,'cellPress','Presence');
				
				//_debugDialog = PopUpManager.createPopUp(Application.root, LFWindow, false,{title:'Debug',closeButton:true,scrollContentPath:'debugDialog'});
				
				clearInterval(clickInterval);
				clickInterval = null;			// First click
			} else {
				clearInterval(clickInterval);
				clickInterval = null;
				selectedChatItem = _users_dg.selectedItem;
				lastClickX = _root._xmouse;
				lastClickY = _root._ymouse;
				clickInterval = setInterval(Proxy.create(this, endClickTimer), DOUBLECLICKSPEED);
			}
		}
	}
	
	private function endClickTimer() {
		// Do actions of first click
		Debugger.log('PRESENCE: single click event',Debugger.MED,'cellPress','Presence');
		clearInterval(clickInterval);
		//ajoutMenuChat(xK, yK);
		clickInterval = null;
	}
	
	public function resize(width:Number){
		panel._width = width;
		
	}
	
	function get className():String { 
        return 'Presence';
    }
	
	public function showPresence(v:Boolean) {
		Debugger.log("Show/Hide Presence: " + v, Debugger.GEN, "showPresence", "Presence");
		this._visible = v;
	}
	
}