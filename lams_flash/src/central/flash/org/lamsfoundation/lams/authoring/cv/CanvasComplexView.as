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

import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.ui.*;
import org.lamsfoundation.lams.common.style.*;
import org.lamsfoundation.lams.common.dict.*;
import org.lamsfoundation.lams.common.mvc.*;
import org.lamsfoundation.lams.common.CommonCanvasView;
import org.lamsfoundation.lams.authoring.cv.*;
import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.monitoring.mv.MonitorModel;
import org.lamsfoundation.lams.monitoring.mv.MonitorController;
import org.lamsfoundation.lams.monitoring.mv.tabviews.MonitorTabView;

import mx.controls.*;
import mx.containers.*;
import mx.managers.*;
import mx.utils.*;

/**
*Authoring view for the canvas
* Relects changes in the CanvasModel
*/

class org.lamsfoundation.lams.authoring.cv.CanvasComplexView extends CommonCanvasView {
	
	private var _tm:ThemeManager;
	
	private var _cm:CanvasModel;
	private var _mm:MonitorModel;
	
    private var _canvasComplexView:CanvasComplexView;
	
	private var _complexActivity:CanvasActivity;
	private var _parentActivity:Object;
	private var _prevActiveView;
	
	//private var canvas_scp:ScrollPane;
	
	private var lastScreenWidth:Number = 500;
	private var lastScreenHeight:Number = 300;
	
	private var activitiesDisplayed:Hashtable;
	
	/**
	* Constructor
	*/
	function CanvasComplexView(){
		_canvasComplexView = this;
		_tm = ThemeManager.getInstance();
		
		activitiesDisplayed = new Hashtable("activitiesDisplayed");
		
        //Init for event delegation
        mx.events.EventDispatcher.initialize(this);
	}
    
	/**
	* Called to initialise Canvas. Called by the Canvas container
	*/
	public function init(m:Observable, c:Controller){
		//Invoke superconstructor, which sets up MVC relationships.
		super (m, c);
		
		_cm = (m instanceof CanvasModel) ? CanvasModel(m) : null;
		_mm = (m instanceof MonitorModel) ? MonitorModel(m) : null;
		
	   //register to recive updates form the model
		
		if(_cm != null) _cm.addEventListener('viewUpdate', Proxy.create(this, viewUpdate));
		if(_mm != null) _mm.addEventListener('viewUpdate', Proxy.create(this, viewUpdate));
		 
		MovieClipUtils.doLater(Proxy.create(this, draw)); 
    }    
    
	/**
	 * Recieved update events from the MonitorModel. Dispatches to relevent handler depending on update.Type
	 * @usage   
	 * @param   event
	 */
	public function update (o:Observable, infoObj:Object):Void{
		   var mm:MonitorModel = MonitorModel(o);
		   infoObj.target = mm;
		   
		   viewUpdate(infoObj);
	}
	
	/**
	 * Recieved update events from the CanvasModel. Dispatches to relevent handler depending on update.Type
	 * @usage   
	 * @param   event
	 */
	public function viewUpdate(event:Object):Void{
		Debugger.log('Recived an Event dispather UPDATE!, updateType:'+event.updateType+', target'+event.target,4,'viewUpdate','CanvasView');
		var cm:CanvasModel = event.target;
	   
	    switch (event.updateType){
            case 'POSITION' :
                setPosition(cm);
                break;
            case 'SIZE' :
                setSize(cm);
                break;
            case 'DRAW_ACTIVITY':
                drawActivity(event.data,cm);
                break;
            case 'REMOVE_ACTIVITY':
                removeActivity(event.data,cm);
                break;
			case 'SELECTED_ITEM' :
					Debugger.log("selecting item: " + cm.selectedItem.activity.activityUIID, Debugger.CRITICAL, "viewUpdate", "CanvasComplexView");
					Debugger.log("test parent: " + cm.findParent(cm.selectedItem.activity, _complexActivity.activity), Debugger.CRITICAL, "viewUpdate", "CanvasComplexView");
					
					var caComplex = activitiesDisplayed.get(_complexActivity.activity.activityUIID);
					caComplex.refreshChildren();
					
					highlightActivity(cm);
						
                break;
			case 'SET_ACTIVE' :
				Debugger.log('setting active :' + event.updateType + " event.data: " + event.data + " condition: " + (event.data == this),Debugger.CRITICAL,'update','org.lamsfoundation.lams.CanvasView');
				_visible = true;
				break;
            default :
                Debugger.log('unknown update type :' + event.updateType,Debugger.CRITICAL,'update','org.lamsfoundation.lams.CanvasComplexView');
		}

	}
	
	/**
    * layout visual elements on the canvas on initialisation
    */
	private function draw(){
		//get the content path for the sp
		content = this;
		//content._alpha = 0;
		//bkg_pnl = content.createClassObject(Panel, "bkg_pnl", content.getNextHighestDepth());
		
		//transparentCover = content.createClassObject(Panel, "_transparentCover_mc", content.getNextHighestDepth(), {_visible: false, enabled: false, _alpha: 50});
		//transparentCover.onPress = null;
		
		activityComplexLayer = content.createEmptyMovieClip("_activityComplexLayer_mc", content.getNextHighestDepth());
		activityLayer = content.createEmptyMovieClip("_activityLayer_mc", content.getNextHighestDepth());
		
		//bkg_pnl.useHandCursor = false;

		setStyles();
		
        //Dispatch load event 
        dispatchEvent({type:'load',target:this});
	}
	
	public function showActivity():Void {
		drawActivity(_complexActivity.activity, model);
		
		// change position
		var ca:MovieClip = activitiesDisplayed.get(_complexActivity.activity.activityUIID);

		setSize(model, ca);
		
		this._visible = true;
	}
	
	public function close():Void {
		removeActivity(_complexActivity.activity, model);
		
		if(model instanceof CanvasModel) {
			model.activeView = _prevActiveView;
			model.currentBranchingActivity = (_prevActiveView.activity.isBranchingActivity()) ? _prevActiveView.activity : null;
			
			model.getCanvas().closeComplexView();
		} else {
			//model.getMonitor().getMV().getMonitorTabView().showAssets(true);
			model.activeView = _prevActiveView;
			model.currentBranchingActivity = (_prevActiveView.activity.isBranchingActivity()) ? _prevActiveView.activity : null;
			
			model.getMonitor().closeComplexView();
		}
	}
	
	/**
	 * Draws new or replaces existing activity to canvas stage.
	 * @usage   
	 * @param   a  - Activity to be drawn
	 * @param   cm - Refernce to the model
	 * @return  Boolean - successfullit
	 */
	private function drawActivity(a:Activity, cm):Boolean{
		if(!cm.isActiveView(this)) return false;
		
		var ccv = CanvasComplexView(this);
		var cvc = getController();
		
		//take action depending on act type
		if(a.activityTypeID==Activity.PARALLEL_ACTIVITY_TYPE){
			//get the children
			var children:Array = cm.getCanvas().ddm.getComplexActivityChildren(a.activityUIID);
			
			var newActivity_mc = activityLayer.createChildAtDepth("CanvasParallelActivity",DepthManager.kTop,{_x: 0, _y: 0, _activity:a,_children:children,_canvasController:cvc,_canvasComplexView:ccv, _locked:a.isReadOnly()});
			
			activitiesDisplayed.put(a.activityUIID, newActivity_mc);
			Debugger.log('Parallel activity a.title:'+a.title+','+a.activityUIID+' added to the cm.activitiesDisplayed hashtable :'+newActivity_mc,4,'drawActivity','CanvasView');
		}
		else if(a.activityTypeID==Activity.OPTIONAL_ACTIVITY_TYPE || a.activityTypeID==Activity.OPTIONS_WITH_SEQUENCES_TYPE){
			var children:Array = cm.getCanvas().ddm.getComplexActivityChildren(a.activityUIID);
			var newActivity_mc = activityComplexLayer.createChildAtDepth("CanvasOptionalActivity",DepthManager.kTop,{_x: 0, _y: 0, _activity:a,_children:children,_canvasController:cvc,_canvasComplexView:ccv,_locked:a.isReadOnly()});
			
			activitiesDisplayed.put(a.activityUIID, newActivity_mc);
			
			Debugger.log('Optional activity Type a.title:'+a.title+','+a.activityUIID+' added to the cm.activitiesDisplayed hashtable :'+newActivity_mc,4,'drawActivity','CanvasView');
		} else {
			Debugger.log('The activity:'+a.title+','+a.activityUIID+' is of unknown type, it cannot be drawn',Debugger.CRITICAL,'drawActivity','CanvasView');
		}

		return true;
	}
	
	/**
	 * Removes existing activity from canvas stage. DOES not affect DDM.  called by an update, so DDM change is already made
	 * @usage   
	 * @param   a  - Activity to be Removed
	 * @param   cm - Refernce to the model
	 * @return  Boolean - successfull
	 */
	private function removeActivity(a:Activity,cm:CanvasModel):Boolean{
		if(!cm.isActiveView(this)) return false;

		var r = activitiesDisplayed.remove(a.activityUIID);
		r.removeMovieClip();
		var s:Boolean = (r==null) ? false : true;
		return s;
	}
	
    /**
    * Sets the size of the canvas on stage, called from update
    */
	private function setSize(cm, ca:MovieClip):Void{
		var s:Object = cm.getSize();
		
		//canvas_scp.setSize(ca._width, ca._height);
		//bkg_pnl.setSize(ca._width, ca._height);
		//transparentCover.setSize(s.w, s.h);
		
	}
	
	/**
	 * Get the CSSStyleDeclaration objects for each component and apply them
	 * directly to the instance
	 * @usage   
	 * @return  
	 */
	private function setStyles() {
		var styleObj = _tm.getStyleObject('CanvasPanel');
		//bkg_pnl.setStyle('styleName', styleObj);
		//transparentCover.setStyle('styleName', styleObj);
    }
	
    /**
    * Sets the position of the canvas on stage, called from update
    * @param cm Canvas model object 
    */
	private function setPosition(cm:CanvasModel):Void{
        var p:Object = cm.getPosition();
        this._x = p.x;
        this._y = p.y;
	}
	
	public function get ddm():DesignDataModel {
		if(model instanceof CanvasModel) return CanvasModel(model).ddm
		else return MonitorModel(model).ddm;
	}
	
	public function get model():Object {
		if(_cm != null) return _cm;
		if(_mm != null) return _mm;
		return null;
	}

	/**
	 * Overrides method in abstract view to ensure correct type of controller is returned
	 * @usage   
	 * @return  CanvasController
	 */
	public function getController() {
		var c:Controller = super.getController();
		
		if(model instanceof CanvasModel) {
			Debugger.log("canvas controller: " + c, Debugger.CRITICAL,'getController','CanvasBranchView');
			return CanvasController(c);
		} else {
			Debugger.log("monitor controller: " + c, Debugger.CRITICAL,'getController','CanvasBranchView');
			return MonitorController(c);
		}
	}
	
	/**
    * Returns the default controller for this view .
	* Overrides AbstractView.defaultController()
    */
    public function defaultController (model):Object {
        return (model instanceof CanvasModel) ? new CanvasController(model) : new MonitorModel(model);
    }
	
}