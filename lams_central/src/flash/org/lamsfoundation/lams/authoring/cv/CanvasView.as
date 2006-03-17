import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.authoring.cv.*
import org.lamsfoundation.lams.authoring.*
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.mvc.*
import mx.managers.*
import mx.containers.*;
import mx.events.*
import mx.utils.*


/**
*Authoring view for the canvas
* Relects changes in the CanvasModel
*/

class org.lamsfoundation.lams.authoring.cv.CanvasView extends AbstractView{
	//constants:
	private var GRID_HEIGHT:Number;
	private var GRID_WIDTH:Number;
	private var H_GAP:Number;
	private var V_GAP:Number;
	
	private var _tm:ThemeManager;
	
	//Canvas clip
	private var _canvas_mc:MovieClip;
	private var canvas_scp:ScrollPane;
    private var bkg_pnl:Panel;
	//private var act_pnl:Panel;
	
    private var _gridLayer_mc:MovieClip;
    private var _transitionLayer_mc:MovieClip;
	private var _activityLayerComplex_mc:MovieClip;
	private var _activityLayer_mc:MovieClip;
	
	private var _transitionPropertiesOK:Function;
    private var _canvasView:CanvasView;
    //Defined so compiler can 'see' events added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	//public var menu:ContextMenu;

	
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
	* Called to initialise Canvas  . CAlled by the Canvas container
	*/
	public function init(m:Observable,c:Controller,x:Number,y:Number,w:Number,h:Number){
		//Invoke superconstructor, which sets up MVC relationships.
		//if(c==undefined){
		//	c==defaultController();
		//}
		super (m, c);
        //Set up parameters for the grid
		H_GAP = 10;
		V_GAP = 10;
        setupCM();
	   //register to recive updates form the model
		CanvasModel(m).addEventListener('viewUpdate',this);
        
		MovieClipUtils.doLater(Proxy.create(this,draw)); 
    }    
    
	public function setupCM():Void{
		trace("Value for this: "+this)
		var myCopy:Array = new Array();
		var menuArr:Array = new Array();
		menuArr[0] =["Copy Activity", getCopy];
		menuArr[1] = ["Paste Activity",getPaste];
		
		for (var i=0; i<menuArr.length; i++){
			var myObj:Object = new Object();
			myObj.cmlabel = menuArr[i][0];
			myObj.handler = menuArr[i][1]; 
			myCopy[i]= myObj;
			
		} 
		var this_cm = Application.getInstance().showCustomCM(true, myCopy);
		_root.menu = this_cm; 
	}
	
	public function getCopy(){
		Application.getInstance().copy();
	}
	public function getPaste(){
		Application.getInstance().paste();
	}
	
/**
 * Recieved update events from the CanvasModel. Dispatches to relevent handler depending on update.Type
 * @usage   
 * @param   event
 */
public function viewUpdate(event:Object):Void{
		Debugger.log('Recived an Event dispather UPDATE!, updateType:'+event.updateType+', target'+event.target,4,'viewUpdate','CanvasView');
		 //Update view from info object
        //Debugger.log('Recived an UPDATE!, updateType:'+infoObj.updateType,4,'update','CanvasView');
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
            case 'DRAW_TRANSITION':
                drawTransition(event.data,cm);
				break;
			case 'REMOVE_TRANSITION':
				removeTransition(event.data,cm);
				break;
			case 'SELECTED_ITEM' :
                highlightActivity(cm);
                break;
			/*
			case 'START_TRANSITION_TOOL':
				startDrawingTransition(cm);
				break;
			case 'STOP_TRANSITION_TOOL':
				stopDrawingTransition(cm);
				break;
				*/
            default :
                Debugger.log('unknown update type :' + event.updateType,Debugger.CRITICAL,'update','org.lamsfoundation.lams.CanvasView');
		}

	}
	/*
	public function onRelease(){
		getController().canvasRelease(_canvas_mc);
	}
	*/
	
	/**
    * layout visual elements on the canvas on initialisation
    */
	private function draw(){
		//get the content path for the sp
		_canvas_mc = canvas_scp.content;
		//Debugger.log('_canvas_mc'+_canvas_mc,Debugger.GEN,'draw','CanvasView');
		
		bkg_pnl = _canvas_mc.createClassObject(Panel, "bkg_pnl", getNextHighestDepth());

		//set up the 
		//_canvas_mc = this;
		_gridLayer_mc = _canvas_mc.createEmptyMovieClip("_gridLayer_mc", _canvas_mc.getNextHighestDepth());
		_transitionLayer_mc = _canvas_mc.createEmptyMovieClip("_transitionLayer_mc", _canvas_mc.getNextHighestDepth());
		_activityLayerComplex_mc = _canvas_mc.createEmptyMovieClip("_activityLayerComplex_mc", _canvas_mc.getNextHighestDepth());
		_activityLayer_mc = _canvas_mc.createEmptyMovieClip("_activityLayer_mc", _canvas_mc.getNextHighestDepth());
		

		
		//_canvas_mc.addEventListener('onRelease',this);
		bkg_pnl.onRelease = function(){
			trace('_canvas_mc.onRelease');
			Application.getInstance().getCanvas().getCanvasView().getController().canvasRelease(this);
		}
		bkg_pnl.useHandCursor = false;
		
		setStyles();
	/*	
		//var s = canvasModel.getSize();
        
		Debugger.log('border?'+this.bkg_pnl,4,'layout','CanvasView');
		this.bkg_pnl.setSize(w,h);
		
		//create the grid
		Grid.drawGrid(_canvas_mc,w,h,V_GAP,H_GAP);
		
		*/
        //Debugger.log('canvas view dispatching load event'+_canvas_mc,Debugger.GEN,'draw','CanvasView');
        //Dispatch load event 
        dispatchEvent({type:'load',target:this});
	}
   
	
	/**
	 * Draws new or replaces existing activity to canvas stage.
	 * @usage   
	 * @param   a  - Activity to be drawn
	 * @param   cm - Refernce to the model
	 * @return  Boolean - successfullit
	 */
	private function drawActivity(a:Activity,cm:CanvasModel):Boolean{
		var s:Boolean = false;
		//Debugger.log('a.title:'+a.title,4,'drawActivity','CanvasView');
		//var initObj:Object = {_activity=a};
		//_global.breakpoint();
		var cvv = CanvasView(this);
		
		var cvc = getController();
		Debugger.log('I am in drawActivity and Activity typeID :'+a+' added to the cm.activitiesDisplayed hashtable :'+newActivity_mc,4,'drawActivity','CanvasView');
		Debugger.log('I am in drawActivity and Activity typeID :'+a.activityTypeID+' added to the cm.activitiesDisplayed hashtable :'+newActivity_mc,4,'drawActivity','CanvasView');
		//take action depending on act type
		if(a.activityTypeID==Activity.TOOL_ACTIVITY_TYPE || a.isGateActivity() || a.isGroupActivity() ){
			var newActivity_mc = _activityLayer_mc.createChildAtDepth("CanvasActivity",DepthManager.kTop,{_activity:a,_canvasController:cvc,_canvasView:cvv});
			cm.activitiesDisplayed.put(a.activityUIID,newActivity_mc);
			Debugger.log('Tool or gate activity a.title:'+a.title+','+a.activityUIID+' added to the cm.activitiesDisplayed hashtable:'+newActivity_mc,4,'drawActivity','CanvasView');
		}
		if(a.activityTypeID==Activity.PARALLEL_ACTIVITY_TYPE){
			//get the children
			var children:Array = cm.getCanvas().ddm.getComplexActivityChildren(a.activityUIID);
			//var newActivity_mc = _activityLayer_mc.createChildAtDepth("CanvasParallelActivity",DepthManager.kTop,{_activity:a,_children:children,_canvasController:cvc,_canvasView:cvv});
			var newActivity_mc = _activityLayer_mc.createChildAtDepth("CanvasParallelActivity",DepthManager.kTop,{_activity:a,_children:children,_canvasController:cvc,_canvasView:cvv});
			cm.activitiesDisplayed.put(a.activityUIID,newActivity_mc);
			Debugger.log('Parallel activity a.title:'+a.title+','+a.activityUIID+' added to the cm.activitiesDisplayed hashtable :'+newActivity_mc,4,'drawActivity','CanvasView');
		}
		if(a.activityTypeID==Activity.OPTIONAL_ACTIVITY_TYPE){
			var children:Array = cm.getCanvas().ddm.getComplexActivityChildren(a.activityUIID);
			//var newActivity_mc = _activityLayer_mc.createChildAtDepth("CanvasParallelActivity",DepthManager.kTop,{_activity:a,_children:children,_canvasController:cvc,_canvasView:cvv});
			var newActivity_mc = _activityLayer_mc.createChildAtDepth("CanvasOptionalActivity",DepthManager.kTop,{_activity:a,_children:children,_canvasController:cvc,_canvasView:cvv});
			cm.activitiesDisplayed.put(a.activityUIID,newActivity_mc);
			Debugger.log('Optional activity Type a.title:'+a.title+','+a.activityUIID+' added to the cm.activitiesDisplayed hashtable :'+newActivity_mc,4,'drawActivity','CanvasView');
			
		}else{
			Debugger.log('The activity:'+a.title+','+a.activityUIID+' is of unknown type, it cannot be drawn',Debugger.CRITICAL,'drawActivity','CanvasView');
		}
		
		
		
		//position
		//newActivity_mc._x = a.xCoord;
		//newActivity_mc._y = a.yCoord;
		
		//newActivity_mc._visible = true;
		
		s = true;
		
		return s;
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
		var s:Boolean = true;
		//Debugger.log('t.fromUIID:'+t.fromUIID+', t.toUIID:'+t.toUIID,Debugger.GEN,'drawTransition','CanvasView');
		var cvv = CanvasView(this);
		var cvc = getController();
		var newTransition_mc:MovieClip = _transitionLayer_mc.createChildAtDepth("CanvasTransition",DepthManager.kTop,{_transition:t,_canvasController:cvc,_canvasView:cvv});
		
		cm.transitionsDisplayed.put(t.transitionUIID,newTransition_mc);
		Debugger.log('drawn a transition:'+t.transitionUIID+','+newTransition_mc,Debugger.GEN,'drawTransition','CanvasView');
		return s;
		
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
	
	private function highlightActivity(cm:CanvasModel){
		Debugger.log('running..',Debugger.CRITICAL,'highlightActivity','CanvasView');
		//deselect everything else
		var CAsArray:Array = cm.activitiesDisplayed.values();
		Debugger.log('CAsArray:'+CAsArray.length,Debugger.CRITICAL,'highlightActivity','CanvasView');
		for(var i=0; i < CAsArray.length; i++){
			CAsArray[i].setSelected(false);
		}
		//try to cast the selected item to see what we have (instance of des not seem to work)
		if(CanvasActivity(cm.selectedItem) != null){
			Debugger.log('Its a canvas activity',4,'highlightActivity','CanvasView');
			var ca = CanvasActivity(cm.selectedItem);
			var a:Activity = ca.activity;			
			ca.setSelected(true);
			/*
			if(a.isGateActivity()){
				
			}else{
			
			}
			*/
			
		}else if(CanvasTransition(cm.selectedItem) != null){
			var ct = CanvasTransition(cm.selectedItem);
			var t:Transition = ct.transition;
			Debugger.log('Its a canvas transition',4,'highlightActivity','CanvasView');
			
		
			
		}else{
			Debugger.log('Its a something we dont know',Debugger.CRITICAL,'updateItemProperties','PropertyInspector');
		
		}
	}
	
		
	/**
    * Create a popup dialog to set transition parameters
    * @param    pos - Position, either 'centre' or an object containing x + y coordinates
    */
    public function createTransitionPropertiesDialog(pos:Object,callBack:Function){
	   //Debugger.log('Call',Debugger.GEN,'createTransitionPropertiesDialog','CanvasView');
	   var dialog:MovieClip;
	   _transitionPropertiesOK = callBack;
        //Check to see whether this should be a centered or positioned dialog
        if(typeof(pos)=='string'){
			//Debugger.log('pos:'+pos,Debugger.GEN,'createTransitionPropertiesDialog','CanvasView');
            dialog = PopUpManager.createPopUp(Application.root, LFWindow, true,{title:Dictionary.getValue('trans_dlg_title'),closeButton:true,scrollContentPath:"TransitionProperties"});
        } else {
            dialog = PopUpManager.createPopUp(Application.root, LFWindow, true,{title:Dictionary.getValue('trans_dlg_title'),closeButton:true,scrollContentPath:"TransitionProperties",_x:pos.x,_y:pos.y});
        }
        //Assign dialog load handler
        dialog.addEventListener('contentLoaded',Delegate.create(this,transitionDialogLoaded));
        //okClickedCallback = callBack;
    }
	
	/**
    * called when the transitionDialogLoaded is loaded
    */
    public function transitionDialogLoaded(evt:Object) {
        //Debugger.log('!evt.type:'+evt.type,Debugger.GEN,'dialogLoaded','CanvasView');
        //Check type is correct
        if(evt.type == 'contentLoaded'){
            //Set up callback for ok button click
            //Debugger.log('!evt.target.scrollContent:'+evt.target.scrollContent,Debugger.GEN,'dialogLoaded','CanvasView');
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
		canvas_scp.setSize(s.w,s.h);
		bkg_pnl.setSize(s.w,s.h);

		//Create the grid.  The gris is re-drawn each time the canvas is resized.
		var grid_mc = Grid.drawGrid(_gridLayer_mc,Math.round(s.w),Math.round(s.h),V_GAP,H_GAP);
		//Debugger.log('grid_mc depth:'+grid_mc.getDepth(),4,'setSize','CanvasView');
		//Debugger.log('_activityLayer_mc depth:'+_activityLayer_mc.getDepth(),4,'setSize','CanvasView');
		//position bin in canvas.  
		var bin = cm.getCanvas().bin;
		bin._x = (s.w - bin._width) - 20;
		bin._y = (s.h - bin._height) - 20;
		
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
	
	public function getViewMc():MovieClip{
		return _canvas_mc;
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