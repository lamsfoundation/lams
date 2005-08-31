import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.ui.*
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
*/

class org.lamsfoundation.lams.authoring.cv.CanvasView extends AbstractView{
	//constants:
	private var GRID_HEIGHT:Number;
	private var GRID_WIDTH:Number;
	private var H_GAP:Number;
	private var V_GAP:Number;
	
	//Canvas clip
	private var _canvas_mc:MovieClip;
	private var canvas_scp:ScrollPane;
    private var bkg_pnl:Panel;
	
    private var _gridLayer_mc:MovieClip;
    private var _transitionLayer_mc:MovieClip;
	private var _activityLayer_mc:MovieClip;
    
    //Defined so compiler can 'see' events added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;

	
	/**
	* Constructor
	*/
	function CanvasView (){
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
       
		CanvasModel(m).addEventListener('viewUpdate',this);
        
		MovieClipUtils.doLater(Proxy.create(this,draw)); 
    }    
    

	
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
            case 'DRAW_DESIGN' :
                drawDesign(cm);
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
			case 'START_TRANSITION_TOOL':
				startDrawingTransition(cm);
				break;
			case 'STOP_TRANSITION_TOOL':
				stopDrawingTransition(cm);
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
		_canvas_mc = canvas_scp.content;
		Debugger.log('_canvas_mc'+_canvas_mc,Debugger.GEN,'draw','CanvasView');
		
		bkg_pnl = _canvas_mc.createClassObject(Panel, "bkg_pnl", getNextHighestDepth());

		//set up the 
		//_canvas_mc = this;
		_gridLayer_mc = _canvas_mc.createEmptyMovieClip("_gridLayer_mc", _canvas_mc.getNextHighestDepth());
		_transitionLayer_mc = _canvas_mc.createEmptyMovieClip("_transitionLayer_mc", _canvas_mc.getNextHighestDepth());
		_activityLayer_mc = _canvas_mc.createEmptyMovieClip("_activityLayer_mc", _canvas_mc.getNextHighestDepth());
			
		
	/*	
		//var s = canvasModel.getSize();
        
		Debugger.log('border?'+this.bkg_pnl,4,'layout','CanvasView');
		this.bkg_pnl.setSize(w,h);
		
		//create the grid
		Grid.drawGrid(_canvas_mc,w,h,V_GAP,H_GAP);
		
		*/
        Debugger.log('canvas view dispatching load event'+_canvas_mc,Debugger.GEN,'draw','CanvasView');
        //Dispatch load event 
        dispatchEvent({type:'load',target:this});
	}
   
	
	/**
	 * Renders the whole design to the canvas
	 * @usage   
	 * @param   o - The Canvas MOdel
	 * @return  
	 */
	private function drawDesign(cm:CanvasModel):Void{
		Debugger.log('Running - !!!!NOT IMPLEMENTED!!!!',4,'drawDesign','CanvasView');
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
		Debugger.log('a.title:'+a.title,4,'drawActivity','CanvasView');
		//var initObj:Object = {_activity=a};
		//_global.breakpoint();
		var cvv = CanvasView(this);
		
		var cvc = getController();
		var newActivity_mc = _activityLayer_mc.createChildAtDepth("CanvasActivity",DepthManager.kTop,{_activity:a,_canvasController:cvc,_canvasView:cvv});
		cm.activitiesDisplayed.put(a.activityUIID,newActivity_mc);
		
		//position
		newActivity_mc._x = a.xCoord;
		newActivity_mc._y = a.yCoord;
		
		newActivity_mc._visible = true;
		
		s = true;
		
		return s;
	}
	
	/**
	 * Removes existing activity from canvas stage.
	 * @usage   
	 * @param   a  - Activity to be Removed
	 * @param   cm - Refernce to the model
	 * @return  Boolean - successfull
	 */
	private function removeActivity(a:Activity,cm:CanvasModel):Boolean{
		var s:Boolean = false;
		Debugger.log('a.title'+a.title,4,'removeActivity','CanvasView');
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
		Debugger.log('t.fromUIID:'+t.fromUIID+', t.toUIID:'+t.toUIID,Debugger.GEN,'drawTransition','CanvasView');
		var cvv = CanvasView(this);
		var cvc = getController();
		var newTransition_mc:MovieClip = _transitionLayer_mc.createChildAtDepth("CanvasTransition",DepthManager.kTop,{_transition:t,_canvasController:cvc,_canvasView:cvv});
		
		
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
		Debugger.log('!Function not Implemented!',Debugger.CRITICAL,'removeTransition','CanvasView');
		//todo:DO
	}
	
	private function startDrawingTransition(cm:CanvasModel):Void{
		
	}
	
	private function stopDrawingTransition(cm:CanvasModel):Void{
		
	}
	 /*
	* 
	* CanvasActivity now calls methods of controller directly.
	//Event handlers to pass onto controller
	public function activityClick(mc):Void{
		Debugger.log('activityClick mc:'+mc,Debugger.GEN,'activityClick','CanvasView');
		getController().activityClick(mc);
	}
	
	public function activityDoubleClick(mc):Void{
		Debugger.log('activityClick mc:'+mc,Debugger.GEN,'activityDoubleClick','CanvasView');
		getController().activityDoubleClick(mc);
	}
	
	public function activityRelease(mc):Void{
		Debugger.log('activityClick mc:'+mc,Debugger.GEN,'activityRelease','CanvasView');
		getController().activityRelease(mc);
	}
	
	public function activityReleaseOutside(mc):Void{
		Debugger.log('activityClick mc:'+mc,Debugger.GEN,'activityReleaseOutside','CanvasView');
		getController().activityReleaseOutside(mc);
	}
	*/
	
	/**
    * Create a popup dialog to set transition parameters
    * @param    pos - Position, either 'centre' or an object containing x + y coordinates
    */
    public function createTransitionPropertiesDialog(pos:Object,callBack:Function){
	   Debugger.log('Call',Debugger.GEN,'createTransitionPropertiesDialog','CanvasView');
	   var dialog:MovieClip;
        //Check to see whether this should be a centered or positioned dialog
        if(typeof(pos)=='string'){
			Debugger.log('pos:'+pos,Debugger.GEN,'createTransitionPropertiesDialog','CanvasView');
            dialog = PopUpManager.createPopUp(Application.root, LFWindow, true,{title:Dictionary.getValue('trans_dlg_title'),closeButton:true,scrollContentPath:"TransitionProperties"});
        } else {
            dialog = PopUpManager.createPopUp(Application.root, LFWindow, true,{title:Dictionary.getValue('trans_dlg_title'),closeButton:true,scrollContentPath:"TransitionProperties",_x:pos.x,_y:pos.y});
        }
        //Assign dialog load handler
        dialog.addEventListener('contentLoaded',Delegate.create(this,dialogLoaded));
        //okClickedCallback = callBack;
    }
	
	    /**
    * called when the dialog is loaded
    */
    public function dialogLoaded(evt:Object) {
        Debugger.log('!evt.type:'+evt.type,Debugger.GEN,'dialogLoaded','CanvasView');
        //Check type is correct
        if(evt.type == 'contentLoaded'){
            //Set up callback for ok button click
            Debugger.log('!evt.target.scrollContent:'+evt.target.scrollContent,Debugger.GEN,'dialogLoaded','CanvasView');
            evt.target.scrollContent.addEventListener('okClicked',Delegate.create(this,okClicked));
        }else {
            //TODO DI 25/05/05 raise wrong event type error 
        }
    }
	
	 /**
    * Workspace dialog OK button clicked handler
    */
    private function okClicked(evt:Object) {
        Debugger.log('!okClicked:',Debugger.GEN,'okClicked','CanvasView');
        //Check type is correct
        if(evt.type == 'okClicked'){
            //Call the callback, passing in the design selected designId
            //okClickedCallback(evt.target.selectedDesignId);
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

		//Create the grid.  The gris is re-dwarn ech time the canvas is res-zed.
		var grid_mc = Grid.drawGrid(_gridLayer_mc,s.w,s.h,V_GAP,H_GAP);
		//Debugger.log('grid_mc depth:'+grid_mc.getDepth(),4,'setSize','CanvasView');
		//Debugger.log('_activityLayer_mc depth:'+_activityLayer_mc.getDepth(),4,'setSize','CanvasView');
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
    * Returns the default controller for this view .
	* Overrides AbstractView.defaultController()
    */
    public function defaultController (model:Observable):Controller {
        return new CanvasController(model);
    }
}