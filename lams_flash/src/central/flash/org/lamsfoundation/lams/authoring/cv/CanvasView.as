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

import mx.controls.*;
import mx.containers.*;
import mx.managers.*;
import mx.utils.*;

/**
*Authoring view for the canvas
* Relects changes in the CanvasModel
*/

class org.lamsfoundation.lams.authoring.cv.CanvasView extends CommonCanvasView {
	
	private var _tm:ThemeManager;
	
	private var _cm:CanvasModel;
    private var _canvasView:CanvasView;
	
	private var canvas_scp:ScrollPane;
	
	private var isRread_only:Boolean = false;
	private var isRedit_on_fly:Boolean = false;
	
	private var read_only:MovieClip;
	private var titleBar:MovieClip;
	private var leftCurve:MovieClip;
	private var rightCurve:MovieClip;
	private var nameBG:MovieClip;
	private var designName_lbl:Label;
	
	private var startTransX:Number;
	private var startTransY:Number;
	
	private var lastScreenWidth:Number = 1024;
	private var lastScreenHeight:Number = 768;
	
	/**
	* Constructor
	*/
	function CanvasView(){
		_canvasView = this;
		_tm = ThemeManager.getInstance();
		
        //Init for event delegation
        mx.events.EventDispatcher.initialize(this);
	}
    
	/**
	* Called to initialise Canvas. Called by the Canvas container
	*/
	public function init(m:Observable,c:Controller,x:Number,y:Number,w:Number,h:Number){
		//Invoke superconstructor, which sets up MVC relationships.
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
			case 'POSITION_TITLEBAR':
				setDesignTitle(cm);
				break;
			case 'SET_ACTIVE' :
				Debugger.log('setting activie :' + event.updateType + " event.data: " + event.data + " condition: " + (event.data == this),Debugger.CRITICAL,'update','org.lamsfoundation.lams.CanvasView');
				transparentCover._visible = (event.data == this) ? false : true;
				break;
            default :
                Debugger.log('unknown update type :' + event.updateType,Debugger.CRITICAL,'update','org.lamsfoundation.lams.CanvasView');
		}

	}
	
	/**
    * layout visual elements on the canvas on initialisation
    */
	private function draw(){
		//get the content path for the sp
		content = canvas_scp.content;
		
		bkg_pnl = content.createClassObject(Panel, "bkg_pnl", getNextHighestDepth());
		gridLayer = content.createEmptyMovieClip("_gridLayer_mc", content.getNextHighestDepth());
		transitionLayer = content.createEmptyMovieClip("_transitionLayer_mc", content.getNextHighestDepth());
		
		activityComplexLayer = content.createEmptyMovieClip("_activityComplexLayer_mc", content.getNextHighestDepth());
		activityLayer = content.createEmptyMovieClip("_activityLayer_mc", content.getNextHighestDepth());
		
		transparentCover = content.createClassObject(Panel, "_transparentCover_mc", content.getNextHighestDepth(), {_visible: false, enabled: false, _alpha: 50});
		transparentCover.onPress = null;
		
		titleBar = _canvasView.attachMovie("DesignTitleBar", "titleBar", _canvasView.getNextHighestDepth())
		
		var styleObj = _tm.getStyleObject('label');
		read_only = _canvasView.attachMovie('Label', 'read_only', _canvasView.getNextHighestDepth(), {_x:5, _y:titleBar._y, _visible:true, autoSize:"left", html:true, styleName:styleObj});
		
		bkg_pnl.onRelease = function(){
			trace('content.onRelease');
			Application.getInstance().getCanvas().getCanvasView().getController().canvasRelease(this);
		}
		
		bkg_pnl.useHandCursor = false;
		
		setDesignTitle();
		styleTitleBar();
		setStyles();
		
        //Dispatch load event 
        dispatchEvent({type:'load',target:this});
	}
	
	private function setDesignTitle(cm:CanvasModel){
		var dTitle:String;
		var titleToCheck:String;
		
		if (isRread_only){
			dTitle = cm.getCanvas().ddm.title + " (<font color='#FF0000'>"+Dictionary.getValue('cv_readonly_lbl')+"</font>)"
			titleToCheck = cm.getCanvas().ddm.title + Dictionary.getValue('cv_readonly_lbl')
		} else if(isRedit_on_fly) {
			dTitle = cm.getCanvas().ddm.title + " (<font color='#036D00'>"+Dictionary.getValue('cv_edit_on_fly_lbl')+"</font>)"
			titleToCheck = cm.getCanvas().ddm.title + Dictionary.getValue('cv_edit_on_fly_lbl')
		}else {
			dTitle = cm.getCanvas().ddm.title
			titleToCheck = dTitle
		}
		if (dTitle == undefined || dTitle == null || dTitle == ""){
			dTitle = Dictionary.getValue('cv_untitled_lbl');
			titleToCheck = dTitle
		}
			
		read_only.text = dTitle;
		setSizeTitleBar(titleToCheck);
	}
	
	private function setSizeTitleBar(dTitle:String):Void{
		
		dTitle = StringUtils.replace(dTitle, " ", "")
		_canvasView.createTextField("designTitle", _canvasView.getNextHighestDepth(), -10000, -10000, 20, 20)
		
		var nameTextFormat = new TextFormat();
		nameTextFormat.bold = true;
		nameTextFormat.font = "Verdana";
		nameTextFormat.size = 12;
		
		var titleTxt = _canvasView["designTitle"];
		titleTxt.multiline = false;
		titleTxt.autoSize = true
		titleTxt.text = dTitle;
		titleTxt.setNewTextFormat(nameTextFormat);
		
		var bgWidth = titleTxt.textWidth;
		titleBar.nameBG._width = bgWidth;
		titleBar.nameBGShadow._width = bgWidth;
		titleBar.nameBG._visible  = true;
		titleBar.rightCurve._x = bgWidth+27;
		titleBar.rightCurveShadow._x = titleBar.rightCurve._x+2;
		
	}
	
	private function positionTitleBar(cm:CanvasModel):Void{
		titleBar._y = canvas_scp._y;
		titleBar._x = (canvas_scp.width/2)-(titleBar._width/2)
		read_only._x = titleBar._x + 5;
	}

    private function styleTitleBar():Void {
		
		var titleBarBg:mx.styles.CSSStyleDeclaration = _tm.getStyleObject("BGPanel");
		var titleBarBgShadow:mx.styles.CSSStyleDeclaration = _tm.getStyleObject("BGPanelShadow");
		
		var nameBGColor:Color = new Color(titleBar.nameBG);
		var nameBGShadowColor:Color = new Color(titleBar.nameBGShadow);
		var rightCurveColor:Color = new Color(titleBar.rightCurve);
		var rightCurveShadowColor:Color = new Color(titleBar.rightCurveShadow);
		
		var bgColor:Number = titleBarBg.getStyle("backgroundColor");
		var bgShadowColor:Number = titleBarBgShadow.getStyle("backgroundColor");
		
		nameBGColor.setRGB(bgColor);
		nameBGShadowColor.setRGB(bgShadowColor);
		rightCurveColor.setRGB(bgColor);
		rightCurveShadowColor.setRGB(bgShadowColor);
		
	}
	   
	/**
	 * Draws new or replaces existing activity to canvas stage.
	 * @usage   
	 * @param   a  - Activity to be drawn
	 * @param   cm - Refernce to the model
	 * @return  Boolean - successfullit
	 */
	private function drawActivity(a:Activity,cm:CanvasModel):Boolean{
		if(!cm.isActiveView(this)) return false;
		
		var cvv = CanvasView(this);
		var cvc = getController();
		
		Debugger.log('I am in drawActivity and Activity typeID :'+a.activityTypeID+' added to the cm.activitiesDisplayed hashtable :'+newActivity_mc,4,'drawActivity','CanvasView');
		//take action depending on act type
		if(a.activityTypeID==Activity.TOOL_ACTIVITY_TYPE || a.isGroupActivity()){
			var newActivity_mc = activityLayer.createChildAtDepth("CanvasActivity",DepthManager.kTop,{_activity:a,_canvasController:cvc,_canvasView:cvv});
			cm.activitiesDisplayed.put(a.activityUIID,newActivity_mc);
			Debugger.log('Tool or gate activity a.title:'+a.title+','+a.activityUIID+' added to the cm.activitiesDisplayed hashtable:'+newActivity_mc,4,'drawActivity','CanvasView');
		}
		else if (a.isGateActivity()){
			var newActivity_mc = activityLayer.createChildAtDepth("CanvasGateActivity",DepthManager.kTop,{_activity:a,_canvasController:cvc,_canvasView:cvv});
			cm.activitiesDisplayed.put(a.activityUIID,newActivity_mc);
			Debugger.log('Gate activity a.title:'+a.title+','+a.activityUIID+' added to the cm.activitiesDisplayed hashtable:'+newActivity_mc,4,'drawActivity','CanvasView');
		}
		else if(a.activityTypeID==Activity.PARALLEL_ACTIVITY_TYPE){
			//get the children
			var children:Array = cm.getCanvas().ddm.getComplexActivityChildren(a.activityUIID);
			var newActivity_mc = activityLayer.createChildAtDepth("CanvasParallelActivity",DepthManager.kTop,{_activity:a,_children:children,_canvasController:cvc,_canvasView:cvv, _locked:a.isReadOnly()});
			cm.activitiesDisplayed.put(a.activityUIID,newActivity_mc);
			Debugger.log('Parallel activity a.title:'+a.title+','+a.activityUIID+' added to the cm.activitiesDisplayed hashtable :'+newActivity_mc,4,'drawActivity','CanvasView');
		}
		else if(a.activityTypeID==Activity.OPTIONAL_ACTIVITY_TYPE){
			var children:Array = cm.getCanvas().ddm.getComplexActivityChildren(a.activityUIID);
			var newActivity_mc = activityComplexLayer.createChildAtDepth("CanvasOptionalActivity",DepthManager.kTop,{_activity:a,_children:children,_canvasController:cvc,_canvasView:cvv,_locked:a.isReadOnly()});
			cm.activitiesDisplayed.put(a.activityUIID,newActivity_mc);
			Debugger.log('Optional activity Type a.title:'+a.title+','+a.activityUIID+' added to the cm.activitiesDisplayed hashtable :'+newActivity_mc,4,'drawActivity','CanvasView');
		}
		else if(a.isBranchingActivity()){	
			var newActivity_mc = activityLayer.createChildAtDepth("CanvasActivity",DepthManager.kTop,{_activity:a,_canvasController:cvc,_canvasView:cvv});
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
		if(!cm.isActiveView(this)) return false;
		
		var cvv = CanvasView(this);
		var cvc = getController();
		
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
		if(!cm.isActiveView(this)) return false;
		
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
		if(!isActivityOnLayer(cm.activitiesDisplayed.get(t.fromUIID), this.activityLayer) && !isActivityOnLayer(cm.activitiesDisplayed.get(t.toUIID), this.activityLayer)) return false;
		
		var cvv = CanvasView(this);
		var cvc = getController();
		var newTransition_mc:MovieClip = transitionLayer.createChildAtDepth("CanvasTransition",DepthManager.kTop,{_transition:t,_canvasController:cvc,_canvasView:cvv});
		
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
		if(!cm.isActiveView(this)) return false;
		
		var cvv = CanvasView(this);
		var cvc = getController();
		var newTransition_mc:MovieClip = transitionLayer.createChildAtDepth("CanvasTransition",DepthManager.kTop,{_transition:t,_canvasController:cvc,_canvasView:cvv, _visible:false});
		
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
		if(!cm.isActiveView(this)) return false;
		
		var r = cm.transitionsDisplayed.remove(t.transitionUIID);
		r.removeMovieClip();
		var s:Boolean = (r==null) ? false : true;
		return s;
	}
	
    /**
    * Sets the size of the canvas on stage, called from update
    */
	private function setSize(cm:CanvasModel):Void{
		var s:Object = cm.getSize();
		var newWidth:Number = Math.max(s.w, lastScreenWidth);
		var newHeight:Number = Math.max(s.h, lastScreenHeight);
		
		canvas_scp.setSize(s.w,s.h);
		bkg_pnl.setSize(newWidth, newHeight);
		transparentCover.setSize(newWidth, newHeight);
		
		//Create the grid.  The gris is re-drawn each time the canvas is resized.
		var grid_mc = Grid.drawGrid(gridLayer,Math.round(newWidth),Math.round(newHeight),V_GAP,H_GAP);
		
		//position bin in canvas.  
		var bin = cm.getCanvas().bin;
		bin._x = (s.w - bin._width) - 20;
		bin._y = (s.h - bin._height) - 20;
		
		canvas_scp.redraw(true);
		
		lastScreenWidth = newWidth;
		lastScreenHeight = newHeight;
	}
	
	/**
	 * Get the CSSStyleDeclaration objects for each component and apply them
	 * directly to the instance
	 * @usage   
	 * @return  
	 */
	private function setStyles() {
		var styleObj = _tm.getStyleObject('CanvasPanel');
		bkg_pnl.setStyle('styleName', styleObj);
		transparentCover.setStyle('styleName', styleObj);
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

	public function showReadOnly(b:Boolean){
		isRread_only = b;
	}
	
	public function showEditOnFly(b:Boolean){
		isRedit_on_fly = b;
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