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

import mx.managers.*;
import mx.containers.*;
import mx.events.*;
import mx.utils.*;
import mx.controls.*;

class org.lamsfoundation.lams.monitoring.mv.IndexButton extends mx.core.UIObject {
	
	public static var _tabID:Number = 2;
	private var _className = "IndexButton";
	
	private var _bgPanel:MovieClip;
	private var btnWidth:Number;
	private var idxLabel:Label;
	
	private var _tm:ThemeManager;
	private var mm:MonitorModel;
	    
	/**
	* Called to Indexbutton. Called by LearnerIndexView
	*/
	public function init(m:Observable,c:Controller){
		//Invoke superconstructor, which sets up MVC relationships.
		mm = MonitorModel(m);
		
		btnWidth = 48;
		
		_bgPanel._width = btnWidth;
		this.createClassObject(mx.controls.Label, "idxLabel", this.getNextHighestDepth(), {text:"", _width: 43, autoSize: "right"});
		this.onRollOver = Delegate.create(this, onMouseOver);
		this.onPress = Delegate.create(this, indexClicked);
    }
		
	public function onMouseOver(): Void {
		Debugger.log("onMouseOver event triggered", Debugger.GEN, "onMouseOver", "IndexButton");
		// TODO: some cool mouse over effects
	}
	
	public function indexClicked(): Void {
		mm.currentLearnerIndex = Number(label.text);
	}

	
	public function setSize():Void {
		this._width = btnWidth;
		idxLabel._width = btnWidth - 5;
		_bgPanel._width = btnWidth;
	}
	
	public function set label(a:String):Void {
		idxLabel.text = a;
	}
	
	public function get label():Label {
		return idxLabel;
	}
}