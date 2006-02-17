import org.lamsfoundation.lams.authoring.*
import org.lamsfoundation.lams.authoring.cv.*
import org.lamsfoundation.lams.common.mvc.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.ui.*
import mx.utils.*


/*
* Makes changes to the Canvas Authoring model's data based on user input.
*/
class org.lamsfoundation.lams.authoring.cv.CanvasController extends AbstractController {
	
	private var _canvasModel:CanvasModel;
	private var _canvasView:CanvasView;
	
	/**
	* Constructor
	*
	* @param   cm   The model to modify.
	*/
	public function CanvasController (cm:Observable) {
		super (cm);
		//have to do an upcast
		_canvasModel = CanvasModel(getModel());
		
	}
   
   public function activityClick(ca:Object):Void{
	   Debugger.log('activityClick CanvasActivity:'+ca.activity.activityUIID,Debugger.GEN,'activityClick','CanvasController');
	   //if transition tool active
	   if(_canvasModel.isTransitionToolActive()){
		   var transitionTarget = createValidTransitionTarget(ca);
		   if(transitionTarget instanceof LFError){
				transitionTarget.showErrorAlert(null); 
				//transitionTarget.showMessageConfirm()
				//TODO: transitionTarget.showErrorAlertCrashDump(null); 
		   }else{
				_canvasModel.addActivityToTransition(transitionTarget);
			}
			/*
			_canvasModel.resetTransitionTool();
			if(ca instanceof CanvasActivity || ca instanceof CanvasParallelActivity ){
				_canvasModel.addActivityToTransition(ca);
			}
			*/
	   }else{
		   //just select the activity
		 _canvasModel.selectedItem = ca;
		 _canvasModel.isDragging = true;
		 ca.startDrag(false);
		   
		}
	   
   }
   
	public function activityDoubleClick(ca:Object):Void{
	   Debugger.log('activityDoubleClick CanvasActivity:'+ca.activity.activityUIID,Debugger.GEN,'activityDoubleClick','CanvasController');
	    _canvasModel.selectedItem = ca;
		if(ca.activity.activityTypeID == Activity.TOOL_ACTIVITY_TYPE){
			_canvasModel.openToolActivityContent(ca.activity);
		}else{
			//TODO: Show the property inspector if its a parralel activity or whatever
		}
   }
   
   public function activityRelease(ca:Object):Void{
	   Debugger.log('activityRelease CanvasActivity:'+ca.activity.activityUIID,Debugger.GEN,'activityRelease','CanvasController');
	   if(_canvasModel.isDragging){
			ca.stopDrag();
			//if we are on the bin - trash it
			if (ca.hitTest(_canvasModel.getCanvas().bin)){
				_canvasModel.getCanvas().removeActivity(ca.activity.activityUIID);
			}
			
			//get a view if ther is not one
			if(!_canvasView){
				_canvasView =  CanvasView(getView());
			}
			
			//give it the new co-ords and 'drop' it
			
			ca.activity.xCoord = ca._x;
			ca.activity.yCoord = ca._y;
			
			//refresh the transitions
			
			Debugger.log('ca.activity.xCoord:'+ca.activity.xCoord,Debugger.GEN,'activityRelease','CanvasController');
			
			
		}
   }
   
   public function activityReleaseOutside(ca:Object):Void{
	   Debugger.log('activityReleaseOutside CanvasActivity:'+ca.activity.activityUIID,Debugger.GEN,'activityReleaseOutside','CanvasController');
	   if(_canvasModel.isTransitionToolActive()){
				
			
			//get a ref to the CanvasActivity the transition pen is over when released
			var currentCursor:MovieClip = Cursor.getCurrentCursorRef();
			//Debugger.log("currentCursor:"+currentCursor, Debugger.GEN,'activityReleaseOutside','CanvasController');
			//strip the last mc off the path as its the click target
			//_global.breakpoint();
			//Debugger.log("currentCursor._droptarget:"+currentCursor._droptarget, Debugger.GEN,'activityReleaseOutside','CanvasController');
			var dt:String = new String(eval(currentCursor._droptarget));
			var i:Number = dt.lastIndexOf(".");
			dt = dt.substring(0,i);
			//Debugger.log("Subst:"+dt, Debugger.GEN,'activityReleaseOutside','CanvasController');
			var transitionTarget_mc:MovieClip = eval(dt);
			Debugger.log("Transition drop target:"+transitionTarget_mc, Debugger.GEN,'activityReleaseOutside','CanvasController');
			
			var transitionTarget = createValidTransitionTarget(transitionTarget_mc);
			if(transitionTarget instanceof LFError){
			//if(transitionTarget instanceof LFMessage){
				//transitionTarget.showErrorAlert(null);
				transitionTarget.showMessageConfirm();
			}else{
				_canvasModel.addActivityToTransition(transitionTarget);
			}
			_canvasModel.resetTransitionTool();
		
			
	   }else{
			if(_canvasModel.isDragging){
				ca.stopDrag();
				if (ca.hitTest(_canvasModel.getCanvas().bin)){
					_canvasModel.getCanvas().removeActivity(ca.activity.activityUIID);
				}
			}
		   
		}
   		
   
   
   }
   
   
    public function transitionClick(ct:CanvasTransition):Void{
	   Debugger.log('transitionClick Transition:'+ct.transition.uiID,Debugger.GEN,'transitionClick','CanvasController');
	    _canvasModel.selectedItem = ct;
		_canvasModel.isDragging = true;
		ct.startDrag(false);
	   
	}
   
   public function transitionDoubleClick(ct:CanvasTransition):Void{
	   Debugger.log('transitionDoubleClick CanvasTransition:'+ct.transition.transitionUIID,Debugger.GEN,'transitionDoubleClick','CanvasController');
	   
	   //TODO: fix this, its null
	   _canvasView =  CanvasView(getView());
	   Debugger.log('_canvasView:'+_canvasView,Debugger.GEN,'transitionDoubleClick','CanvasController');
	   _canvasView.createTransitionPropertiesDialog("centre",Delegate.create(this, transitionPropertiesOK));
	   
	    _canvasModel.selectedItem = ct;
   }
   
   public function transitionRelease(ct:CanvasTransition):Void{
	if(_canvasModel.isDragging){
			ct.stopDrag();
			
				if (ct.hitTest(_canvasModel.getCanvas().bin)){
					_canvasModel.getCanvas().removeTransition(ct.transition.transitionUIID);
				}
			
			
		}
	
   }

	public function transitionReleaseOutside(ct:CanvasTransition):Void{
		transitionRelease(ct);
		
	}
   
	
	/**
	 * Transition Properties OK Handler
	 * @usage   
	 * @return  
	 */
	public function transitionPropertiesOK(evt:Object):Void{
		Debugger.log(evt.gate,Debugger.GEN,'transitionPropertiesOK','CanvasController');
		//cm creates gate act and another transition to make [trans][gate][trans] seq
		//sets in ddm
		//flags dirty to refresh view
		_canvasModel.createGateTransition(_canvasModel.selectedItem.transition.transitionUIID,evt.gate);
		
		
		
	}
		
	/**
	* Clled when we get a click on the canvas, if its in craete gate mode then create a gate
	* 
	 * public static var SYNCH_GATE_ACTIVITY_TYPE:Number = 3;
	 * public static var SCHEDULE_GATE_ACTIVITY_TYPE:Number = 4;
	 * public static var PERMISSION_GATE_ACTIVITY_TYPE:Number = 5;
	 * @usage   
	 * @param   canvas_mc 
	 * @return  
	 */
	public function canvasRelease(canvas_mc:MovieClip){
		Debugger.log(canvas_mc,Debugger.GEN,'canvasRelease','CanvasController');
		Debugger.log('_canvasModel.activeTool:'+_canvasModel.activeTool,Debugger.GEN,'canvasRelease','CanvasController');
		if(_canvasModel.activeTool == CanvasModel.GATE_TOOL){
			var p = new Point(canvas_mc._xmouse, canvas_mc._ymouse); 
			_canvasModel.createNewGate(Activity.PERMISSION_GATE_ACTIVITY_TYPE,p);
			_canvasModel.getCanvas().stopGateTool();
			
		}
		
	}
	
	private function createValidTransitionTarget(transitionTargetObj:Object):Object{
			var targetCA:Object;
			//see what we can cast to
			if(CanvasActivity(transitionTargetObj)!=null){
				Debugger.log("Casting to CanvasActivity", Debugger.GEN,'activityReleaseOutside','CanvasController');
				targetCA = CanvasActivity(transitionTargetObj);
				return targetCA;
			}else if(CanvasParallelActivity(transitionTargetObj)!=null){
				Debugger.log("Casting to CanvasParallelActivity", Debugger.GEN,'activityReleaseOutside','CanvasController');
				targetCA = CanvasParallelActivity(transitionTargetObj);
				return targetCA;
			}else{
				var e = new LFError("__You cannot create a transition to this object__","activityReleaseOutside",this,String(transitionTargetObj));
				//bail
				return e;
			}
			
			/*
			//if(ca instanceof CanvasActivity){
			if(ICanvasActivity(ca) != null){
				Debugger.log("Target implements ICanvasActivity", Debugger.GEN,'activityReleaseOutside','CanvasController');
				var r:Object = _canvasModel.addActivityToTransition(targetCA);
				if(r instanceof LFError){
				//Debugger.error(r);
					r.showErrorAlert(null);
				}
				//TODO: Check on status of try catch bug
				*//*
				try{
					_canvasModel.addActivityToTransition(ca);
				//}catch(e:org.lamsfoundation.lams.common.util.LFError){
				}catch(e:LFError){
					trace('in catch');
					Debugger.error(e);
				}
				*/
				
			
			
			/*
			}else{
				Debugger.log("Target does NOT implement ICanvasActivity", Debugger.CRITICAL,'activityReleaseOutside','CanvasController');
				//released over something other than a CanvasActivity so reset the t tool
				
			}
			*/
	}
	
	
   

   
}
