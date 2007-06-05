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

import mx.containers.*
import mx.managers.*
import mx.utils.*
import org.lamsfoundation.lams.authoring.DesignDataModel
import org.lamsfoundation.lams.monitoring.Application
import org.lamsfoundation.lams.monitoring.mv.Monitor
import org.lamsfoundation.lams.monitoring.layout.*
import org.lamsfoundation.lams.common.ApplicationParent
import org.lamsfoundation.lams.common.Sequence
import org.lamsfoundation.lams.common.util.Proxy
import org.lamsfoundation.lams.common.util.Debugger
import org.lamsfoundation.lams.common.layout.ILayoutManager
import org.lamsfoundation.lams.common.layout.LFLayoutManager
import org.lamsfoundation.lams.common.layout.LFLayoutItem
import org.lamsfoundation.lams.common.ws.Workspace

/**
* DefaultLayoutManager - Default Layout manager for an Application
* @author   Mitchell Seaton
*  
*/
class DefaultLayoutManager extends LFLayoutManager {
	
	public static var COMPONENT_NO:Number = 5;
	private var app:Application = null;
	
    //Constructor
    function DefaultLayoutManager(name:String) {
		super(name);
		app = Application.getInstance();
	}
	
	public function setup(target:ApplicationParent) {
		super.setup(target);
		
		setupUI();
	}
	
	private function setItem(obj) {
		setEmptyLayoutItem(new LFLayoutItem(obj.classname, obj));
	}
	
	private function setupUI() {
		var _sequence:Sequence = app.sequence;
		var obj:Object = new Object();
		
		if(_sequence != null || _sequence != undefined) {
			obj = checkAvailability(_sequence);
		}
		
		doMenuBar(obj.locked);
		
			
		//WORKSPACE
		app.workspace = new Workspace();
		setItem(app.workspace);
		
		
		// MONITOR
		doMonitor(obj.locked, obj.isEditingUser);
        
	}
	
	public function doMenuBar(isLocked:Boolean) {
		// Menu Bar
		app.menubar = Application.containermc.attachMovie('LFMenuBar','_menu_mc',Application.MENU_DEPTH, {env:'Monitoring',_x:0,_y:0, isLocked:isLocked});
		app.menubar.addEventListener('load',Proxy.create(app,app.UIElementLoaded));	
		setItem(app.menubar);
	}
	
	public function checkAvailability(_sequence:Sequence):Object {
			var _ddm:DesignDataModel = null;
			_ddm = (_sequence.getLearningDesignModel() != null) ? _sequence.getLearningDesignModel() : _ddm;
			
			return checkAvailabilityOnDDM(_ddm);
	}
	
	public function checkAvailabilityOnDDM(_ddm:DesignDataModel):Object {
		var _obj:Object = new Object();
			
		_obj.locked = _ddm.editOverrideLock;
		_obj.isEditingUser = (_ddm.editOverrideUserID == _root.userID) ? true : false;
			
		return _obj;
	}
	
	public function doMonitor(isLocked:Boolean, isEditingUser:Boolean) {
		
        var depth:Number = Application.root.getNextHighestDepth();
		app.monitor = new Monitor(Application.root,depth++,Application.MONITOR_X,Application.MONITOR_Y, Application.MONITOR_W, Application.MONITOR_H, isLocked, isEditingUser);
		app.monitor.addEventListener('load',Proxy.create(app,app.UIElementLoaded));
		setItem(app.monitor);
	}
	
	public function resize(w:Number, h:Number) {
		
		var someListener:Object = new Object();
		someListener.onMouseUp = function () {
			
			//Menu - only need to worry about width
			if(app.menubar)
				app.menubar.setSize(w,app.menubar._height);
			
			//MONITOR
			app.monitor.setSize(w-15,h-app.monitor.y);
        
		}
		
		//Menu - only need to worry about width
        if(app.menubar)
			app.menubar.setSize(w,app.menubar._height);
		
		//MONITOR
		app.monitor.setSize(w,h-app.monitor.y);
		
	}
	
}