import org.lamsfoundation.lams.authoring.*
import org.lamsfoundation.lams.authoring.cv.*
import org.lamsfoundation.lams.common.mvc.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.ui.*


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
   
   public function activityClick(ca:CanvasActivity):Void{
	   Debugger.log('activityClick CanvasActivity:'+ca.activity.activityUIID,Debugger.GEN,'activityClick','CanvasController');
	   //if transition tool active
	   if(isTransitionToolActive()){
			_canvasModel.resetTransitionTool();
			if(ca instanceof CanvasActivity){
				_canvasModel.addActivityToTransition(ca);
			}
	   }else{
		   
		   
		}
	   
   }
   
   public function activityDoubleClick(ca:CanvasActivity):Void{
	   Debugger.log('activityDoubleClick CanvasActivity:'+ca.activity.activityUIID,Debugger.GEN,'activityDoubleClick','CanvasController');
	   
   }
   
   public function activityRelease(ca:CanvasActivity):Void{
	   Debugger.log('activityRelease CanvasActivity:'+ca.activity.activityUIID,Debugger.GEN,'activityRelease','CanvasController');
	   
   }
   
   public function activityReleaseOutside(ca:CanvasActivity):Void{
	   Debugger.log('activityReleaseOutside CanvasActivity:'+ca.activity.activityUIID,Debugger.GEN,'activityReleaseOutside','CanvasController');
	   if(isTransitionToolActive()){
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
			//try to cast to a CanvasActivity
			ca = CanvasActivity(transitionTarget_mc);
			if(ca instanceof CanvasActivity){
				
			
				//TODO: Check on status of try catch bug
				/*
				try{
					_canvasModel.addActivityToTransition(ca);
				//}catch(e:org.lamsfoundation.lams.common.util.LFError){
				}catch(e:LFError){
					trace('in catch');
					Debugger.error(e);
				}
				*/
				
				var r:Object = _canvasModel.addActivityToTransition(ca);
				if(r instanceof LFError){
					Debugger.error(r);
				}
			}else{
				//released over something other than a CanvasActivity so reset the t tool
				_canvasModel.resetTransitionTool();
			}
			
			//var path:String = dt.split(".");
			//var droppedActIconId = path[3];
			//check to make sure it is not optional
			//var actualTrTarget = checkTransitionTarget(droppedActIconId);
			//createTransition(workspaceRef[actualTrTarget]);
	   }else{
		   
		   
		}
   		
   
   
   }
   
   
    public function transitionClick(ct:CanvasTransition):Void{
	   Debugger.log('transitionClick Transition:'+ct.transition.uiID,Debugger.GEN,'transitionClick','CanvasController');
	   
	   
	}
   
   public function transitionDoubleClick(ct:CanvasTransition):Void{
	   Debugger.log('transitionDoubleClick CanvasTransition:'+ct.transition.uiID,Debugger.GEN,'transitionDoubleClick','CanvasController');
	   
	   //TODO: fix this, its null
	   _canvasView =  CanvasView(getView());
	   Debugger.log('_canvasView:'+_canvasView,Debugger.GEN,'transitionDoubleClick','CanvasController');
	   _canvasView.createTransitionPropertiesDialog("centre",tpOKHandler);
   }
   
   public function tpOKHandler():Void{
	   
	}
   
   private function isTransitionToolActive():Boolean{
	   if(_canvasModel.activeTool == CanvasModel.TRANSITION_TOOL){
		   return true;
		}else{
			return false;
		}
	}
   
}
