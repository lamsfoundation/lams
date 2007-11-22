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
import org.lamsfoundation.lams.common.ApplicationParent;
import org.lamsfoundation.lams.authoring.Activity;
import org.lamsfoundation.lams.authoring.ComplexActivity;
import org.lamsfoundation.lams.authoring.cv.CanvasActivity;
import org.lamsfoundation.lams.common.Sequence;
import org.lamsfoundation.lams.common.ToolTip;
import org.lamsfoundation.lams.authoring.Transition;

import mx.managers.*;
import mx.containers.*;
import mx.events.*;
import mx.utils.*;
import mx.controls.*;

class org.lamsfoundation.lams.monitoring.mv.tabviews.LearnerIndexView extends AbstractView {
	
	public static var _tabID:Number = 2;
	private var _className = "LearnerIndexView";
	
	private var _tm:ThemeManager;
	private var _tip:ToolTip;
	
	private var mm:MonitorModel;
	private var _learnerIndexView:LearnerIndexView;
	
	//IndexButton
	private var _indexButton:IndexButton;
	private var _indexButton_mc:MovieClip;
	private var _buttonsPanel_mc:MovieClip;
	
	private var buttonsDrawn:Boolean;
	
	private var dispatchEvent:Function; 
	public var addEventListener:Function;
	public var removeEventListener:Function;

	/**
	* Constructor
	*/
	function LearnerIndexView(){
		Debugger.log("LearnerIndexView Constructor", Debugger.CRITICAL, "LearnerIndexView", "LearnerIndexView");

		_tm = ThemeManager.getInstance();
		_tip = new ToolTip();
		
		this._visible = false;
				
        //Init for event delegation
        mx.events.EventDispatcher.initialize(this);
	}
	

	public function init(m:Observable,c:Controller){
		Debugger.log("LearnerIndexView init", Debugger.CRITICAL, "init", "LearnerIndexView");
		//Invoke superconstructor, which sets up MVC relationships.
		super (m, c);
		mm = MonitorModel(model)

		_learnerIndexView = this;
		mm.learnerIndexView = this;

		buttonsDrawn = false;
		
		MovieClipUtils.doLater(Proxy.create(this,draw)); 
    }

	public function update (o:Observable,infoObj:Object):Void {
		Debugger.log("LearnerIndexView update", Debugger.GEN, "update", "LearnerIndexView");
		var mm:MonitorModel = MonitorModel(o);

		switch (infoObj.updateType){
			case 'TABCHANGE' :
				if (infoObj.tabID == _tabID && !mm.locked){
					if (!buttonsDrawn && getNumButtons()>1){
						setupButtons();
					}
					this._visible = true;
				}else {
					this._visible = false;
				}
				break;
			case 'POSITION' :
				if (infoObj.tabID == _tabID && !mm.locked){
					setPosition(mm);
				}
				break;
			case 'SIZE' :	
				if (infoObj.tabID == _tabID && !mm.locked){
					setSize(mm);
				}
				break;	
			default :
				Debugger.log('unknown update type :' + infoObj.updateType,Debugger.CRITICAL,'update','org.lamsfoundation.lams.MonitorTabView');
		}
	}
		
	private function draw(){
				
		dispatchEvent({type:'load',target:this});
	}
	
	public function getNumButtons(): Number {
		return Math.ceil(mm.getSequence().noStartedLearners/mm.learnersPerPage);
	}
	
	public function setupButtons():Void {
		
		var btnWidth:Number = 48;
		var numButtons:Number = getNumButtons();
		
		_buttonsPanel_mc = this.createEmptyMovieClip("_buttonsPanel_mc", DepthManager.kTop);
			
		for (var i=1; i<=numButtons; i++) {	
			var idxBtn:MovieClip = _buttonsPanel_mc.attachMovie("IndexButton", "indexButton"+i, _buttonsPanel_mc.getNextHighestDepth(), {_width: btnWidth});	
			_indexButton = IndexButton(idxBtn);
			_indexButton.init(mm);
			_indexButton.label = i;
			Debugger.log("idxBtn._width: "+idxBtn._width, Debugger.CRITICAL, "setupButtons", "LearnerIndexView");
			if (i > 1)
				idxBtn._x = (i-1)*btnWidth-1;
			else
				idxBtn._x = (i-1)*btnWidth;
		}
		buttonsDrawn = true;
	}

	private function setPosition(mm:MonitorModel):Void{		
		Debugger.log("LearnerIndexView setPosition", Debugger.CRITICAL, "setPosition", "LearnerIndexView");
		var p:Object = mm.getPosition(); 
		
		this._x = p.x;
		this._y = 0;
	}
	
	private function setSize(mm:MonitorModel):Void{
		Debugger.log("setSize invoked", Debugger.CRITICAL, "setSize", "LearnerIndexView");
		
		Debugger.log("PNT 1: "+mm.learnerIndexView._width, Debugger.CRITICAL, "setSize", "LearnerIndexView");
		this._width = Stage.width;
		Debugger.log("PNT 2: "+mm.learnerIndexView.width, Debugger.CRITICAL, "setSize", "LearnerIndexView");
	}

	public function getController():MonitorController{
		var c:Controller = super.getController();
		return MonitorController(c);
	}
	

    public function defaultController (model:Observable):Controller {
        return new MonitorController(model);
    }
	
	public function getIndexView():MovieClip{
		return this;
	}
}