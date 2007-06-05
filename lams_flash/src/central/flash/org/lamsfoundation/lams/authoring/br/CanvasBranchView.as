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
import org.lamsfoundation.lams.authoring.cv.*;
import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.common.dict.*;
import org.lamsfoundation.lams.common.mvc.*;
import org.lamsfoundation.lams.common.CommonCanvasView;

import com.polymercode.Draw;
import mx.controls.*;
import mx.managers.*;
import mx.containers.*;
import mx.events.*;
import mx.utils.*;


/**
*Authoring view for the canvas
* Relects changes in the CanvasModel
*/

class org.lamsfoundation.lams.authoring.br.CanvasBranchView extends CommonCanvasView {
	//constants:
	private var GRID_HEIGHT:Number;
	private var GRID_WIDTH:Number;
	private var H_GAP:Number;
	private var V_GAP:Number;
	
	private var _tm:ThemeManager;
	private var _cm:CanvasModel;
	
	//Canvas Branch clip
	private var _canvas_branch_mc:MovieClip;
	private var canvas_scp:ScrollPane;
	
    private var bkg_pnl:Panel;
	
    private var _gridLayer_mc:MovieClip;
    private var _transitionLayer_mc:MovieClip;
	private var _activityLayerComplex_mc:MovieClip;
	private var _activityLayer_mc:MovieClip;
	
	private var startTransX:Number;
	private var startTransY:Number;
	private var lastScreenWidth:Number = 512;
	private var lastScreenHeight:Number = 389;
	
	private var _transitionPropertiesOK:Function;
    private var _canvasBranchView:CanvasBranchView;
    
	//Defined so compiler can 'see' events added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	
	/**
	* Constructor
	*/
	function CanvasBranchView(){
		_canvasBranchView = this;
		_tm = ThemeManager.getInstance();
        
		//Init for event delegation
        mx.events.EventDispatcher.initialize(this);
	}
    
	/**
	* Called to initialise Canvas  . CAlled by the Canvas container
	*/
	public function init(m:Observable,c:Controller){
		
		super (m, c);
        
		//Set up parameters for the grid
		H_GAP = 10;
		V_GAP = 10;
		
		_cm = CanvasModel(m)
       
	   //register to recive updates form the model
		_cm.addEventListener('viewUpdate',this);
        
		MovieClipUtils.doLater(Proxy.create(this,draw)); 
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
			case 'HIDE_ACTIVITY':
                hideActivity(event.data,cm);
                break;
            case 'REMOVE_ACTIVITY':
                removeActivity(event.data,cm);
                break;
            case 'DRAW_TRANSITION':
                drawTransition(event.data,cm);
				break;
			case 'HIDE_TRANSITION':
                hideTransition(event.data,cm);
				break;
			case 'REMOVE_TRANSITION':
				removeTransition(event.data,cm);
				break;
			case 'SELECTED_ITEM' :
                highlightActivity(cm);
                break;
            default :
                Debugger.log('unknown update type :' + event.updateType,Debugger.CRITICAL,'update','org.lamsfoundation.lams.CanvasView');
		}

	}
	
	/**
    * layout visual elements on the canvas on initialisation
    */
	private function draw(){
		
		// hide for the moment
		this._visible = false;
		
		//get the content path for the sp
		_canvas_branch_mc = canvas_scp.content;
		
		bkg_pnl = _canvas_branch_mc.createClassObject(Panel, "bkg_pnl", getNextHighestDepth());
		
		//set up the layers
		_gridLayer_mc = _canvas_branch_mc.createEmptyMovieClip("_gridLayer_mc", _canvas_branch_mc.getNextHighestDepth());
		_transitionLayer_mc = _canvas_branch_mc.createEmptyMovieClip("_transitionLayer_mc", _canvas_branch_mc.getNextHighestDepth());
		
		_activityLayerComplex_mc = _canvas_branch_mc.createEmptyMovieClip("_activityLayerComplex_mc", _canvas_branch_mc.getNextHighestDepth());
		_activityLayer_mc = _canvas_branch_mc.createEmptyMovieClip("_activityLayer_mc", _canvas_branch_mc.getNextHighestDepth());
		
		bkg_pnl.onRelease = function(){
			Application.getInstance().getCanvas().getCanvasView().getController().canvasRelease(this);
		}
		
		bkg_pnl.useHandCursor = false;
		
		setStyles();
		
        //Dispatch load event 
        dispatchEvent({type:'load',target:this});
	}

	public function initDrawTempTrans(){
		Debugger.log("Initialising drawing temp. Transition", Debugger.GEN, "initDrawTempTrans", "CanvasView");
		_activityLayer_mc.createEmptyMovieClip("tempTrans", _activityLayer_mc.getNextHighestDepth());
		_activityLayer_mc.attachMovie("squareHandle", "h1", _activityLayer_mc.getNextHighestDepth());
		_activityLayer_mc.attachMovie("squareHandle", "h2", _activityLayer_mc.getNextHighestDepth());
		
		_activityLayer_mc.h1._x = _canvas_branch_mc._xmouse
		_activityLayer_mc.h1._y = _canvas_branch_mc._ymouse
		
		_activityLayer_mc.tempTrans.onEnterFrame = drawTempTrans;
		
	}
	
	/**
	 * used to draw temp dotted transtion.
	 * @usage   
	 * @return  
	 */
	private function drawTempTrans(){
	   Debugger.log("Started drawing temp. Transition", Debugger.GEN, "drawTempTrans", "CanvasView");
	   
	   this.clear();
	   
	   Debugger.log("Runtime movieclips cleared from CanvasView: clear()", Debugger.GEN, "drawTempTrans", "CanvasView");
	   
	   Draw.dashTo(this, _parent.h1._x, _parent.h1._y, _parent._parent._xmouse - 3, _parent._parent._ymouse - 3, 7, 4);
	   _parent.h2._x = _parent._parent._xmouse - 3;
	   _parent.h2._y = _parent._parent._ymouse - 3;
   }
	
	public function removeTempTrans(){
	   Debugger.log("Stopped drawing temp. Transition", Debugger.GEN, "removeTempTrans", "CanvasView");
	   delete _activityLayer_mc.tempTrans.onEnterFrame;
	   _activityLayer_mc.tempTrans.removeMovieClip();
	   _activityLayer_mc.h1.removeMovieClip();
	   _activityLayer_mc.h2.removeMovieClip();
	}
	 
	   
	/**
	 * Draws new or replaces existing activity to canvas stage.
	 * @usage   
	 * @param   a  - Activity to be drawn
	 * @param   cm - Refernce to the model
	 * @return  Boolean - successfullit
	 */
	private function drawActivity(a:Activity,cm:CanvasModel):Boolean{
		
		var cbv = CanvasBranchView(this);
		var cbc = getController();
		
		Debugger.log('I am in drawActivity and Activity typeID :'+a.activityTypeID+' added to the cm.activitiesDisplayed hashtable :'+newActivity_mc,4,'drawActivity','CanvasView');
		//take action depending on act type
		if(a.activityTypeID==Activity.TOOL_ACTIVITY_TYPE || a.isGroupActivity()){
			var newActivity_mc = _activityLayer_mc.createChildAtDepth("CanvasActivity",DepthManager.kTop,{_activity:a,_canvasController:cbc,_canvasView:cbv});
			cm.activitiesDisplayed.put(a.activityUIID,newActivity_mc);
			Debugger.log('Tool or gate activity a.title:'+a.title+','+a.activityUIID+' added to the cm.activitiesDisplayed hashtable:'+newActivity_mc,4,'drawActivity','CanvasView');
		}
		else if (a.isGateActivity()){
			var newActivity_mc = _activityLayer_mc.createChildAtDepth("CanvasGateActivity",DepthManager.kTop,{_activity:a,_canvasController:cbc,_canvasView:cbv});
			cm.activitiesDisplayed.put(a.activityUIID,newActivity_mc);
			Debugger.log('Gate activity a.title:'+a.title+','+a.activityUIID+' added to the cm.activitiesDisplayed hashtable:'+newActivity_mc,4,'drawActivity','CanvasView');
		}
		else if(a.activityTypeID==Activity.PARALLEL_ACTIVITY_TYPE){
			//get the children
			var children:Array = cm.getCanvas().ddm.getComplexActivityChildren(a.activityUIID);
			var newActivity_mc = _activityLayer_mc.createChildAtDepth("CanvasParallelActivity",DepthManager.kTop,{_activity:a,_children:children,_canvasController:cbc,_canvasView:cbv, _locked:a.isReadOnly()});
			cm.activitiesDisplayed.put(a.activityUIID,newActivity_mc);
			Debugger.log('Parallel activity a.title:'+a.title+','+a.activityUIID+' added to the cm.activitiesDisplayed hashtable :'+newActivity_mc,4,'drawActivity','CanvasView');
		}
		else if(a.activityTypeID==Activity.OPTIONAL_ACTIVITY_TYPE){
			var children:Array = cm.getCanvas().ddm.getComplexActivityChildren(a.activityUIID);
			var newActivity_mc = _activityLayerComplex_mc.createChildAtDepth("CanvasOptionalActivity",DepthManager.kTop,{_activity:a,_children:children,_canvasController:cbc,_canvasView:cbv,_locked:a.isReadOnly()});
			cm.activitiesDisplayed.put(a.activityUIID,newActivity_mc);
			Debugger.log('Optional activity Type a.title:'+a.title+','+a.activityUIID+' added to the cm.activitiesDisplayed hashtable :'+newActivity_mc,4,'drawActivity','CanvasView');
		}
		else if(a.activityTypeID==Activity.BRANCHING_ACTIVITY_TYPE){	
			var newActivity_mc = _activityLayer_mc.createChildAtDepth("CanvasActivity",DepthManager.kTop,{_activity:a,_canvasController:cbc,_canvasView:cbv});
			cm.activitiesDisplayed.put(a.activityUIID,newActivity_mc);
			Debugger.log('Branching activity Type a.title:'+a.title+','+a.activityUIID+' added to the cm.activitiesDisplayed hashtable :'+newActivity_mc,4,'drawActivity','CanvasView');
	
		}else{
			Debugger.log('The activity:'+a.title+','+a.activityUIID+' is of unknown type, it cannot be drawn',Debugger.CRITICAL,'drawActivity','CanvasView');
		}

		return true;
	}
	
	/**
	 * Add to canvas stage but keep hidden from view.
	 * 
	 * @usage   
	 * @param   a  
	 * @param   cm 
	 * @return  true if successful
	 */
	
	private function hideActivity(a:Activity, cm:CanvasModel):Boolean {
		var cbv = CanvasBranchView(this);
		var cbc = getController();
		
		if (a.isSystemGateActivity()){
			var newActivityObj = new Object();
			newActivityObj.activity = a;
			
			cm.activitiesDisplayed.put(a.activityUIID,newActivityObj);
			
			Debugger.log('Gate activity a.title:'+a.title+','+a.activityUIID+' added (hidden) to the cm.activitiesDisplayed hashtable:'+newActivityObj,4,'hideActivity','CanvasView');
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
		//Debugger.log('a.title:'+a.title,4,'removeActivity','CanvasView');
		var r = cm.activitiesDisplayed.remove(a.activityUIID);
		r.removeMovieClip();
		var s:Boolean = (r==null) ? false : true;
		return s;
	}
	
	/**
	 * Draws a transition on the canvas.
	 * @usage   
	 * @param   t  The transition to draw
	 * @param   cm  the canvas model.
	 * @return  
	 */
	private function drawTransition(t:Transition,cm:CanvasModel):Boolean{
		var cbv = CanvasBranchView(this);
		var cbc = getController();
		var newTransition_mc:MovieClip = _transitionLayer_mc.createChildAtDepth("CanvasTransition",DepthManager.kTop,{_transition:t,_canvasController:cbc,_canvasView:cbv});
		
		cm.transitionsDisplayed.put(t.transitionUIID,newTransition_mc);
		Debugger.log('drawn a transition:'+t.transitionUIID+','+newTransition_mc,Debugger.GEN,'drawTransition','CanvasView');
		return true;
		
	}
	
	/**
	 * Hides a transition on the canvas.
	 * 
	 * @usage   
	 * @param   t  The transition to hide
	 * @param   cm  The canvas model
	 * @return  true if successful
	 */
	
	private function hideTransition(t:Transition, cm:CanvasModel):Boolean{
		var cbv = CanvasBranchView(this);
		var cbc = getController();
		var newTransition_mc:MovieClip = _transitionLayer_mc.createChildAtDepth("CanvasTransition",DepthManager.kTop,{_transition:t,_canvasController:cbc,_canvasView:cbv, _visible:false});
		
		cm.transitionsDisplayed.put(t.transitionUIID,newTransition_mc);
		Debugger.log('drawn (hidden) a transition:'+t.transitionUIID+','+newTransition_mc,Debugger.GEN,'hideTransition','CanvasView');
		
		return true;
	}
	
	/**
	 * Removes a transition from the canvas
	 * @usage   
	 * @param   t  The transition to remove
	 * @param   cm  The canvas model
	 * @return  
	 */
	private function removeTransition(t:Transition,cm:CanvasModel){
		//Debugger.log('t.uiID:'+t.transitionUIID,Debugger.CRITICAL,'removeTransition','CanvasView');
		var r = cm.transitionsDisplayed.remove(t.transitionUIID);
		r.removeMovieClip();
		var s:Boolean = (r==null) ? false : true;
		return s;
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
			dialog = PopUpManager.createPopUp(Application.root, LFWindow, true,{title:Dictionary.getValue('trans_dlg_title'),closeButton:true,scrollContentPath:"TransitionProperties"});
        } else {
            dialog = PopUpManager.createPopUp(Application.root, LFWindow, true,{title:Dictionary.getValue('trans_dlg_title'),closeButton:true,scrollContentPath:"TransitionProperties",_x:pos.x,_y:pos.y});
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
            evt.target.scrollContent.addEventListener('okClicked',_transitionPropertiesOK);
        }else {
            //TODO DI 25/05/05 raise wrong event type error 
        }
    }
	
	
    /**
    * Sets the size of the canvas on stage, called from update
    */
	private function setSize(cm:CanvasModel):Void{
		var s:Object = cm.getSize();
		
		var newWidth:Number = Math.max(s.w, lastScreenWidth)
		var newHeight:Number = Math.max(s.h, lastScreenHeight)
		
		canvas_scp.setSize(s.w,s.h);
		bkg_pnl.setSize(newWidth,newHeight);
		
		//Create the grid.  The gris is re-drawn each time the canvas is resized.
		var grid_mc = Grid.drawGrid(_gridLayer_mc,Math.round(newWidth),Math.round(newHeight),V_GAP,H_GAP);
		
		//position bin in canvas. 
		// TODO: new indep. bin?
		//var bin = cm.getCanvas().bin;
		//bin._x = (s.w - bin._width) - 20;
		//bin._y = (s.h - bin._height) - 20;
		canvas_scp.redraw(true);
		lastScreenWidth = newWidth
		lastScreenHeight = newHeight
	}
	
	/**
	 * Get the CSSStyleDeclaration objects for each component and apply them
	 * directly to the instance
	 * @usage   
	 * @return  
	 */
	private function setStyles() {
        
		var styleObj = _tm.getStyleObject('CanvasPanel');
		bkg_pnl.setStyle('styleName',styleObj);
		
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
	
	public function getTransitionLayer():MovieClip{
		return _transitionLayer_mc;
	}
	
	public function get activityLayer():MovieClip{
		return _activityLayer_mc;
	}
	
	public function closeView():Void {
		this.removeMovieClip();
	}
	
	/**
	 * Overrides method in abstract view to ensure cortect type of controller is returned
	 * @usage   
	 * @return  CanvasController
	 */
	public function getController():CanvasController{
		var c:Controller = super.getController();
		return CanvasController(c);
	}
	
	/**
    * Returns the default controller for this view .
	* Overrides AbstractView.defaultController()
    */
    public function defaultController (model:Observable):Controller {
        return new CanvasController(model);
    }
}