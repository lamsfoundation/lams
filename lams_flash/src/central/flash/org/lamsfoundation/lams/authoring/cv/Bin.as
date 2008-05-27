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

import org.lamsfoundation.lams.common.*;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.dict.*;
import org.lamsfoundation.lams.common.util.ui.*;
import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.authoring.cv.*;


/**  
* CanvasActivity - 
*/  
class org.lamsfoundation.lams.authoring.cv.Bin extends MovieClip{
	
	//this is set by the init object
	private var _canvasController:CanvasController;
	private var _targetView:MovieClip;
	private var _tip:ToolTip;
	
	//locals
	private var tooltipXoffset:Number = 100;
	private var up_mc:MovieClip;
	private var over_mc:MovieClip;
	
	private var _blocked:Boolean;
	
	//Defined so compiler can 'see' events added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	
	
	function Bin(){
		//let it wait one frame to set up the components.
		MovieClipUtils.doLater(Proxy.create(this,init));
	}
	
	public function init():Void{
		//set up handlers
		_tip = new ToolTip();
		up_mc.addEventListener("onRollOver",this);
		up_mc.addEventListener("onRollOut",this);
		up_mc.addEventListener("onRelease",this);
		draw();
	}
	
	private function draw(){
		over_mc._visible = false;
		up_mc._visible = true;
		
		setStyles();
	}
	
	private function onRelease():Void{
		Debugger.log('onRelease:'+this,Debugger.GEN,'onRelease','Bin');
		_tip.CloseToolTip();
	}
	
	private function onRollOver():Void{
		var Xpos = (Application.CANVAS_X+ this._x)-tooltipXoffset;
		var Ypos = (Application.CANVAS_Y+ this._y);
		var ttHolder = ApplicationParent.tooltip;
		var ttMessage = Dictionary.getValue("bin_tooltip");
		
		//param "true" is to specify that tooltip needs to be shown above the component 
		_tip.DisplayToolTip(ttHolder, ttMessage, Xpos, Ypos, true);
	}
	
	private function onRollOut():Void{
		_tip.CloseToolTip();
	}
	
	private function setStyles():Void {
		//var styleObj = _tm.getStyleObject("bin");
		over_mc._alpha = 40;
	}
	
	private function setOn(a:Boolean):Void {
		over_mc._visible = a;
		up_mc._visible = !a;
	}
	
	public function setOff():Void {
		setOn(false);
	}
	
	public function isActivityOverBin(ca:MovieClip, isInit:Boolean):Void {
		var hit = ca.hitTest(this);
		_blocked = (isInit || (!hit && _blocked)) ? hit : _blocked;
		
		if(!isInit && !_blocked)
			setOn(hit);
	}
	
	public function initBlocked(ca:MovieClip):Boolean {
		isActivityOverBin(ca, true);
		
		return _blocked;
	}
	
	public function set blocked(a:Boolean):Void {
		_blocked = blocked;
	}
	
	public function get blocked():Boolean {
		return _blocked;
	}
}