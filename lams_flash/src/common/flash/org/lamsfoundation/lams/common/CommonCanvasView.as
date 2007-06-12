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
import org.lamsfoundation.lams.common.ApplicationParent;
import org.lamsfoundation.lams.common.dict.*;
import org.lamsfoundation.lams.common.ui.*;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.mvc.*;
import org.lamsfoundation.lams.authoring.cv.*;
import org.lamsfoundation.lams.monitoring.mv.*;
import org.lamsfoundation.lams.authoring.Activity;
import org.lamsfoundation.lams.authoring.Transition;

import com.polymercode.Draw;
import mx.managers.*;
import mx.events.*;
import mx.utils.*;

/**
 * Provides common services for the "canvas view" of
 * a either Author or Monitor applications.
 */
class org.lamsfoundation.lams.common.CommonCanvasView extends AbstractView {
	
	public var GRID_HEIGHT:Number;
	public var GRID_WIDTH:Number;
	public var H_GAP:Number;
	public var V_GAP:Number;

	private var model:Observable;
	private var controller:Controller;
	
    private var bkg_pnl:Panel;
	
	private var _content_mc:MovieClip;
  
	private var _gridLayer_mc:MovieClip;
    private var _transitionLayer_mc:MovieClip;
	private var _activityLayer_mc:MovieClip;
	private var _activityComplexLayer_mc:MovieClip;
	
	private var _transitionPropertiesOK:Function;

	//Defined so compiler can 'see' events added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;

	public function CommonCanvasView (m:Observable, c:Controller) {
		super(m, c);
		
        mx.events.EventDispatcher.initialize(this);
	}

	public function highlightActivity(model:Observable){
		Debugger.log('running..',Debugger.CRITICAL,'highlightActivity','CommonCanvasView');
		
		var m = (model instanceof MonitorModel) ? MonitorModel(model) : null;
		m = (model instanceof CanvasModel) ? CanvasModel(model) : m;
		
		if(m != null) {
			
			var ca = CanvasActivity(m.selectedItem);
			var a:Activity = ca.activity;	
		
			// deselect previously selected item
			if(m.prevSelectedItem != null) {
				// if child of an complex activity is previously selected, it is easiest to clear all the children
				if(m.prevSelectedItem.activity.parentUIID != null) {
					var caComplex = m.activitiesDisplayed.get(m.prevSelectedItem.activity.parentUIID);
					caComplex.refreshChildren();
				} else {
					var dca:ICanvasActivity = m.activitiesDisplayed.get(m.prevSelectedItem.activity.activityUIID);
					dca.setSelected(false);
				}
			}
			
			//try to cast the selected item to see what we have (instance of des not seem to work)
			if(ICanvasActivity(m.selectedItem) != null){
				Debugger.log('Its a canvas activity',4,'highlightActivity','CommonCanvasView');
				ca.setSelected(true);
			} else if(CanvasTransition(m.selectedItem) != null) {
				var ct = CanvasTransition(m.selectedItem);
				var t:Transition = ct.transition;
				Debugger.log('Its a canvas transition',4,'highlightActivity','CommonCanvasView');
			} else {
				Debugger.log('Its a something we dont know',Debugger.CRITICAL,'highlightActivity','CommonCanvasView');
			}
			
		}
	}
	
	private function initDrawTempTrans(){
		Debugger.log("Initialising drawing temp. Transition", Debugger.GEN, "initDrawTempTrans", "CommonCanvasView");
		
		activityLayer.createEmptyMovieClip("tempTrans", activityLayer.getNextHighestDepth());
		activityLayer.attachMovie("squareHandle", "h1", activityLayer.getNextHighestDepth());
		activityLayer.attachMovie("squareHandle", "h2", activityLayer.getNextHighestDepth());
		
		activityLayer.h1._x = content._xmouse
		activityLayer.h1._y = content._ymouse
		
		activityLayer.tempTrans.onEnterFrame = drawTempTrans;
		
	}
	
	/**
	 * used to draw temp dotted transtion.
	 * @usage   
	 * @return  
	 */
	public function drawTempTrans(){
	   Debugger.log("Started drawing temp. Transition", Debugger.GEN, "drawTempTrans", "CommonCanvasView");
	   
	   this.clear();
	   
	   Debugger.log("Runtime movieclips cleared from CanvasView: clear()", Debugger.GEN, "drawTempTrans", "CommonCanvasView");
	   
	   Draw.dashTo(this, _parent.h1._x, _parent.h1._y, _parent._parent._xmouse - 3, _parent._parent._ymouse - 3, 7, 4);
	   _parent.h2._x = _parent._parent._xmouse - 3;
	   _parent.h2._y = _parent._parent._ymouse - 3;
    }
	
	public function removeTempTrans(){
	   Debugger.log("Stopped drawing temp. Transition", Debugger.GEN, "removeTempTrans", "CommonCanvasView");
	   delete activityLayer.tempTrans.onEnterFrame;
	   activityLayer.tempTrans.removeMovieClip();
	   activityLayer.h1.removeMovieClip();
	   activityLayer.h2.removeMovieClip();
	}
	
	/**
    * Create a popup dialog to set transition parameters
    * @param    pos - Position, either 'centre' or an object containing x + y coordinates
    */
    public function createTransitionPropertiesDialog(pos:Object,callBack:Function){
	   var dialog:MovieClip;
	   _transitionPropertiesOK = callBack;
	   
        //Check to see whether this should be a centered or positioned dialog
        if(typeof(pos)=='string'){
            dialog = PopUpManager.createPopUp(ApplicationParent.root, LFWindow, true,{title:Dictionary.getValue('trans_dlg_title'),closeButton:true,scrollContentPath:"TransitionProperties"});
        } else {
            dialog = PopUpManager.createPopUp(ApplicationParent.root, LFWindow, true,{title:Dictionary.getValue('trans_dlg_title'),closeButton:true,scrollContentPath:"TransitionProperties",_x:pos.x,_y:pos.y});
        }
        //Assign dialog load handler
        dialog.addEventListener('contentLoaded',Delegate.create(this,transitionDialogLoaded));
    }
	
	/**
    * called when the transitionDialogLoaded is loaded
    */
    public function transitionDialogLoaded(evt:Object) {
       //Check type is correct
        if(evt.type == 'contentLoaded'){
            //Set up callback for ok button click
            evt.target.scrollContent.addEventListener('okClicked',_transitionPropertiesOK);
        }else {
            //TODO DI 25/05/05 raise wrong event type error 
        }
    }
	
	
	public function get content():MovieClip {
		return _content_mc;
	}
	
	public function set content(a:MovieClip):Void {
		_content_mc = a;
	}
	
	public function get activityLayer():MovieClip {
		return _activityLayer_mc;
	}
	
	public function set activityLayer(a:MovieClip):Void {
		_activityLayer_mc = a;
	}
	
	public function get activityComplexLayer():MovieClip {
		return _activityComplexLayer_mc;
	}
	
	public function set activityComplexLayer(a:MovieClip):Void {
		_activityComplexLayer_mc = a;
	}
	
	public function get gridLayer():MovieClip {
		return _gridLayer_mc;
	}
	
	public function set gridLayer(a:MovieClip):Void {
		_gridLayer_mc = a;
	}
	
	public function get transitionLayer():MovieClip {
		return _transitionLayer_mc;
	}
	
	public function set transitionLayer(a:MovieClip):Void {
		_transitionLayer_mc = a;
	}
	

}