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

import org.lamsfoundation.lams.authoring.Application
import org.lamsfoundation.lams.authoring.cv.Canvas
import org.lamsfoundation.lams.authoring.tk.Toolkit
import org.lamsfoundation.lams.authoring.tb.Toolbar
import org.lamsfoundation.lams.authoring.layout.*
import org.lamsfoundation.lams.common.ApplicationParent
import org.lamsfoundation.lams.common.util.Proxy
import org.lamsfoundation.lams.common.layout.ILayoutManager
import org.lamsfoundation.lams.common.layout.LFLayoutManager
import org.lamsfoundation.lams.common.layout.LFLayoutItem

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
        
		// Menu Bar
        app.menubar = Application.root.attachMovie('LFMenuBar','_menu_mc', Application.MENU_DEPTH, {_x:0,_y:0});
        app.menubar.addEventListener('load',Proxy.create(app,app.UIElementLoaded));
		setItem(app.menubar);
		
        var depth:Number = Application.root.getNextHighestDepth();
		
		// Toolbar
        app.toolbar = new Toolbar(Application.toolbarContainer,Application.toolbarContainer.getNextHighestDepth(), Application.TOOLBAR_X, Application.TOOLBAR_Y, Toolbar.NORMAL_MODE);
		app.toolbar.addEventListener('load',Proxy.create(app,app.UIElementLoaded));
		setItem(app.toolbar);
		
		// Canvas
        app.canvas = new Canvas(Application.root,depth++, Application.CANVAS_X, Application.CANVAS_Y, Application.CANVAS_W, 495);
        app.canvas.addEventListener('load',Proxy.create(app,app.UIElementLoaded));
		setItem(app.canvas);
		
		// Toolkit
		app.toolkit = new Toolkit(Application.root,depth++, Application.TOOLKIT_X, Application.TOOLKIT_Y);
        app.toolkit.addEventListener('load',Proxy.create(app,app.UIElementLoaded));
		setItem(app.toolkit);
		
		// Property Inspector
		app.pi = Application.root.attachMovie('PropertyInspector','_pi_mc', Application.PI_DEPTH, {_x:Application.PI_X,_y:Application.PI_Y, _canvasModel:app.canvas.model, _canvasController:app.canvas.view.getController()});
		app.pi.addEventListener('load',Proxy.create(app,app.UIElementLoaded));
		setItem(app.pi);
	}
	
	public function resize(w:Number, h:Number) {
		//Menu - only need to worry about width
        app.menubar.setSize(w,app.menubar._height);

        //Canvas
        app.toolkit.setSize(app.toolkit.width, h-Application.TOOLKIT_Y);
		app.canvas.setSize(w-app.toolkit.width, h-(Application.CANVAS_Y + app.canvas.model.getPIHeight()));
       //Toolbar
        app.toolbar.setSize(w, Application.TOOLBAR_HEIGHT);
		//Property Inspector
		app.pi.setSize(w-app.toolkit.width, app.pi._height)
		app.pi._y = h - app.canvas.model.getPIHeight();
		
		var piHeight:Number = app.canvas.model.getPIHeight();
		app.pi.showExpand(false)
		if (piHeight != app.pi.piFullHeight()){
			app.pi.showExpand(true);
		}
	}
	
}