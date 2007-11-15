/***************************************************************************
 * Copyright (C) 2007 LAMS Foundation (http://lamsfoundation.org)
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
 
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.ui.*;
import org.lamsfoundation.lams.common.style.*;
import org.lamsfoundation.lams.monitoring.mv.*;
import org.lamsfoundation.lams.monitoring.mv.tabviews.*;
import org.lamsfoundation.lams.monitoring.*;
import org.lamsfoundation.lams.common.dict.*; 
import org.lamsfoundation.lams.common.mvc.*;
import org.lamsfoundation.lams.authoring.Activity;
import org.lamsfoundation.lams.common.ToolTip;

import mx.managers.*;
import mx.containers.*;
import mx.events.*;
import mx.utils.*;
import mx.controls.*;

class org.lamsfoundation.lams.monitoring.mv.IndexButton extends mx.core.UIObject {
	
	private var idxLabel:Label;
	    
	//These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	
	function IndexButton() {
		
		init();
		setPosition();
		
		//Init for event delegation
        mx.events.EventDispatcher.initialize(this);
	}
	
	/**
	* Called to Indexbutton. Called by LearnerIndexView
	*/
	public function init(m:Observable,c:Controller){
		this.createClassObject(mx.controls.Label, "idxLabel", this.getNextHighestDepth(), {text:"", autoSize: "center"});
		
		this.onRollOver = Delegate.create(this, onMouseOver);
		this.onPress = Delegate.create(this, indexClicked);
		
		MovieClipUtils.doLater(Proxy.create(this, draw)); 
    }   
	
	
	public function draw():Void {
		
		Debugger.log("draw invoked", Debugger.CRITICAL, "draw", "IndexButton");
	}
	
	public function onMouseOver(): Void {
		Debugger.log("onMouseOver event triggered", Debugger.GEN, "onMouseOver", "IndexButton");
		// TODO: some cool mouse over effect
	}
	
	public function indexClicked(): Void {
		Debugger.log("onMousePress event triggered by "+label.text, Debugger.CRITICAL, "indexClicked", "IndexButton");
	}
	
	public function setPosition() {
		idxLabel._x = this._x + this._width/2 - idxLabel._width/2;
		idxLabel._y = this._y;
	}
	
	public function set label(a:String):Void {
		idxLabel.text = a;
	}
	
	public function get label():Label {
		return idxLabel;
	}
}