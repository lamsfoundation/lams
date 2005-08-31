import org.lamsfoundation.lams.common.util.Observable;
import org.lamsfoundation.lams.authoring.cv.*;
import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.common.util.*;
import mx.events.*
/*
* Model for the Canvas
*/
class org.lamsfoundation.lams.authoring.cv.CanvasModel extends Observable {
	
	public static var TRANSITION_TOOL:String = "TRANSITION";
	public static var OPTIONAL_TOOL:String = "OPTIONAL";
	
	private var __width:Number;
	private var __height:Number;
	private var __x:Number;
	private var __y:Number;
	
	private var infoObj:Object;
	
	private var _ddm:DesignDataModel;
		//UI State variabls	private var _isDirty:Boolean;
	private var _activeTool:String;
	

	private var _isDrawingTransition:Boolean;
	private var _transitionActivities:Array;
	
	
	//these are hashtables of mc refs MOVIECLIPS
	private var _activitiesDisplayed:Hashtable;
	private var _transitionsDisplayed:Hashtable;
	//These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	
	/**
	* Constructor.
	*/
	public function CanvasModel (){
		
		 //Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);
		
		_activitiesDisplayed = new Hashtable("_activitiesDisplayed");
		_transitionsDisplayed = new Hashtable("_transitionsDisplayed");
		_ddm = Application.getInstance().getDesignDataModel();
		_activeTool = null;
		_transitionActivities = new Array();
	}

	
	/**
	* Used by application to set the size
	* @param width The desired width
	* @param height the desired height
	*/
	public function setSize(width:Number, height:Number):Void{
		__width = width;
		__height = height;
		
		/*
		//send an update
		setChanged();
		infoObj = {};
		infoObj.updateType = "SIZE";
		notifyObservers(infoObj);
		*/
		broadcastViewUpdate("SIZE");
		
	}
	
	/**
	* Used by View to get the size
	* @returns Object containing width(w) & height(h).  obj.w & obj.h
	*/
	public function getSize():Object{
		var s:Object = {};
		s.w = __width;
		s.h = __height;
		return s;
	}
	
	
	
	/**
	* Used by application to set the Position
	* @param x
	* @param y
	*/
	public function setPosition(x:Number, y:Number):Void{
		__x=x;
		__y=y;
		/*
		//send an update
		setChanged();
		infoObj = {};
		infoObj.updateType = "POSITION";
		notifyObservers(infoObj);
		*/
		broadcastViewUpdate("POSITION");
	}
	
	
	
	/**
	* Used by View to get the size
	* @returns Object containing width(w) & height(h).  obj.w & obj.h
	*/
	public function getPosition():Object{
		var p:Object = {};
		p.x = __x;
		p.y = __y;
		return p;
	}

	public function setDirty(){
		_isDirty = true;
		/*
		//work out what we need to redraw.
		//for now lets just do a full re-draw
		//send an update
		setChanged();
		infoObj = {};
		infoObj.updateType = "DRAW_DESIGN";
		notifyObservers(infoObj);
		*/
		refreshDesign();
	}
	
	/**
	 * Starts the transition tool
	 * @usage   
	 * @return  
	 */
	public function startTransitionTool():Void{
		Debugger.log('Starting transition tool',Debugger.GEN,'startTransitionTool','CanvasModel');			
		resetTransitionTool();
		_activeTool = CanvasModel.TRANSITION_TOOL;
		broadcastViewUpdate("START_TRANSITION_TOOL");
	}
	
	/**
	 * Stops it
	 * @usage   
	 * @return  
	 */
	 
	public function stopTransitionTool():Void{
		Debugger.log('Stopping transition tool',Debugger.GEN,'stopTransitionTool','CanvasModel');
		broadcastViewUpdate("STOP_TRANSITION_TOOL");
	}
	
	public function addActivityToTransition(ca:CanvasActivity):Object{
		//check we have not added too many
		
		if(_transitionActivities.length >= 2){
			//TODO: show an error
			Debugger.log('Too many activities in the Transition',Debugger.CRITICAL,'addActivityToTransition','CanvasModel');
			return false;
		}
		Debugger.log('Adding Activity.UIID:'+ca.activity.activityUIID,Debugger.GEN,'addActivityToTransition','CanvasModel');
		_transitionActivities.push(ca);
		
		if(_transitionActivities.length == 2){
			//check we have 2 valid acts to create the transition.
			if(_transitionActivities[0].activity.activityUIID == _transitionActivities[1].activity.activityUIID){
				return new LFError("You cannot create a Transition between the same Activities","addActivityToTransition",this);
			}
			if(!_activitiesDisplayed.containsKey(_transitionActivities[0].activity.activityUIID)){
				return new LFError("First activity of the Transition is missing, UIID:"+ca[0].activity.activityUIID,"addActivityToTransition",this);
			}
			if(!_activitiesDisplayed.containsKey(_transitionActivities[1].activity.activityUIID)){
				return new LFError("Second activity of the Transition is missing, UIID:"+ca[1].activity.activityUIID,"addActivityToTransition",this);
			}
			
			
			
			//lets make the transition
			var t:Transition = createTransition(_transitionActivities);
			resetTransitionTool();
			//add it to the DDM
			var success:Object = _ddm.addTransition(t);
			//flag the model as dirty and trigger a refresh
			setDirty();
			
			
		}
		
		
		
		
		return true;
	}
	
	/**
	 * Resets the transition tool to its starting state, e.g. if one chas been created or the user released the mouse over an unsuitable clip
	 * @usage   
	 */
	public function resetTransitionTool():Void{
		//clear the transitions array
			_transitionActivities = new Array();
	}
	
	private function createTransition(transitionActs:Array):Transition{
		var fromAct:Activity = transitionActs[0].activity;
		var toAct:Activity = transitionActs[1].activity;
		
		var t:Transition = new Transition(_ddm.newUIID(),fromAct.activityUIID,toAct.activityUIID,_ddm.learningDesignID);
		
		return t;
	}
	
	
	
	
	
	
	private function compareActivities(ddm_activity:Activity,cm_activity:Activity):Object{
		var r:Object = new Object();
		
		//if they are the same (ref should point to same act) then nothing to do.
		//if the ddm does not have an act displayed then we need to remove it from the cm
		//if the ddm has an act that cm does not ref, then we need to add it.
			
		if(ddm_activity === cm_activity){
			return r = "SAME";
		}
		
		//check for a new act in the dmm
		if(cm_activity == null){
			return r = "NEW";
		}
		
		//check if act has been removed from canvas
		if(ddm_activity == null){
			return r = "DELETE";
		}
		
			
	}
	
	private function compareTransitions(ddm_transition:Transition, cm_transition:Transition):Object{
		var r:Object = new Object();
		if(ddm_transition === cm_transition){
			return r = "SAME";
		}
		
		//check for a new act in the dmm
		if(cm_transition == null){
			return r = "NEW";
		}
		
		//check if act has been removed from canvas
		if(ddm_transition == null){
			return r = "DELETE";
		}
		
		
	}
	
	private function refreshDesign(){
		//porobbably need to get a bit more granular		Debugger.log('Running',Debugger.GEN,'refreshDesign','CanvasModel');
		//go through the design and see what has changed, compare DDM to canvasModel
		//_global.breakpoint();
		var ddmActivity_keys:Array = _ddm.activities.keys();
		var cmActivity_keys:Array = _activitiesDisplayed.keys();
		var longest = Math.max(ddmActivity_keys.length, cmActivity_keys.length);
		
		//loop through and do comparison
		for(var i=0;i<longest;i++){
			//check DDM against CM, DDM is king.
			var keyToCheck:Number = ddmActivity_keys[i];
			var ddm_activity:Activity = _ddm.activities.get(keyToCheck);
			var cm_activity:Activity = _activitiesDisplayed.get(keyToCheck).activity;			//if they are the same (ref should point to same act) then nothing to do.
			//if the ddm does not have an act displayed then we need to remove it from the cm
			//if the ddm has an act that cm does not ref, then we need to add it.
			//_global.breakpoint();
			var r_activity:Object = compareActivities(ddm_activity, cm_activity);
			
			if(r_activity == "NEW"){
				//draw this activity
				broadcastViewUpdate("DRAW_ACTIVITY",ddm_activity);
				
			}else if(r_activity == "DELETE"){
				//remove this activity
				broadcastViewUpdate("REMOVE_ACTIVITY",ddm_activity);
			}
		
			
		}
		
		//now check the transitions:
		/**/
		var ddmTransition_keys:Array = _ddm.transitions.keys();
		var cmTransition_keys:Array = _transitionsDisplayed.keys();
		longest = Math.max(ddmTransition_keys.length, cmTransition_keys.length);
		//loop through and do comparison
		for(var i=0;i<longest;i++){
			var transitionKeyToCheck:Number = ddmTransition_keys[i];
			var ddmTransition:Transition = _ddm.transitions.get(transitionKeyToCheck);
			var cmTransition:Transition = _transitionsDisplayed.get(transitionKeyToCheck).transition;
			
			var r_transition:Object = compareTransitions(ddmTransition, cmTransition);
			
			if(r_transition == "NEW"){
				broadcastViewUpdate("DRAW_TRANSITION",ddmTransition);
			}else if(r_transition == "DELETE"){
				broadcastViewUpdate("REMOVE_TRANSITION",ddmTransition);
			}
			
		}
		
		
		
		
		
	}
	
	/**
    * Notify registered listeners that a Styles update has happened
    */
    public function broadcastViewUpdate(_updateType,_data){
        dispatchEvent({type:'viewUpdate',target:this,updateType:_updateType,data:_data});
        trace('broadcast');
    }
	
	//Getters n setters
	
	/**
	 * Returns a reference to the Activity Movieclip for the UIID passed in.  Gets from _activitiesDisplayed Hashable
	 * @usage   
	 * @param   UIID 
	 * @return  Activity Movie clip
	 */
	public function getActivityMCByUIID(UIID:Number):MovieClip{
		
		var a_mc:MovieClip = _activitiesDisplayed.get(UIID);
		Debugger.log('UIID:'+UIID+'='+a_mc,Debugger.GEN,'getActivityMCByUIID','CanvasModel');
		return a_mc;
	}
	
	public function get activitiesDisplayed():Hashtable{
		return _activitiesDisplayed;
	}
	
	public function get trasitionsDisplayed():Hashtable{
		return _transitionsDisplayed;
	}
	
	public function get isDrawingTransition():Boolean{
		return _isDrawingTransition;
	}
	/**
	 * 
	 * @usage   
	 * @param   newactivetool 
	 * @return  
	 */
	public function set activeTool (newactivetool:String):Void {
		_activeTool = newactivetool;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get activeTool ():String {
		return _activeTool;
	}
	
}
