import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.ui.*;
import org.lamsfoundation.lams.common.*;
import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.authoring.cv.*;


/**  
* -+ - 
*/  
class org.lamsfoundation.lams.authoring.cv.CanvasTransition extends MovieClip{  
	//set by passing initObj to mc.createClass()
	private var _canvasController:CanvasController;
	private var _canvasView:CanvasView;
	private var _transition:Transition;
	
	private var _drawnLineStyle:Number;
	
	private var arrow_mc:MovieClip;
	private var stopArrow_mc:MovieClip;
	private var stopSign_mc:MovieClip;
	
	private var _dcStartTime:Number = 0;
	private var _doubleClicking:Boolean;
		
	
	function CanvasTransition(){
		
		Debugger.log("_transition.fromUIID:"+_transition.fromUIID,4,'Constructor','CanvasTransition');
		Debugger.log("_transition.toUIID:"+_transition.toUIID,4,'Constructor','CanvasTransition');
		arrow_mc._visible = false;
		stopArrow_mc._visible = false;
		stopSign_mc._visible = false;
		//let it wait one frame to set up the components.
		MovieClipUtils.doLater(Proxy.create(this,init));
		//init();
	}
	
	public function init():Void{
		Debugger.log('Running,',4,'init','CanvasTransition');
		_drawnLineStyle = 0x777E9D;
		draw();
	}

	public function get transition():Transition{
		return _transition;
	}
	
	public function set transition(t:Transition){
		_transition = t;
	}
	
	
	
	
	/**
	 * Renders the transition to stage
	 * @usage   
	 * @return  
	 */
	private function draw():Void{		Debugger.log('',4,'draw','CanvasTransition');
		_global.breakpoint();
			
		var cv:Canvas = Application.getInstance().getCanvas();
		var fromAct_mc = cv.model.getActivityMCByUIID(_transition.fromUIID);
		
		Debugger.log('fromAct_mc:'+fromAct_mc,4,'draw','CanvasTransition');
		
		var toAct_mc = cv.model.getActivityMCByUIID(_transition.toUIID);
		Debugger.log('toAct_mc:'+toAct_mc,4,'draw','CanvasTransition');
		
		var isGateTransition = toAct_mc.activity.isGateActivity();
		
		var startPoint:Point = MovieClipUtils.getCenterOfMC(fromAct_mc);
		var endPoint:Point = MovieClipUtils.getCenterOfMC(toAct_mc);
		
		this.lineStyle(2, _drawnLineStyle);
		this.moveTo(startPoint.x, startPoint.y);
		//this.dashTo(startX, startY, endX, endY, 8, 4);
		this.lineTo(endPoint.x, endPoint.y);
		
		// calculate the position and angle for the arrow_mc
		arrow_mc._x = (startPoint.x + endPoint.x)/2;
		arrow_mc._y = (startPoint.y + endPoint.y)/2;
			
		// gradient
		var angle:Number = Math.atan2((endPoint.y- startPoint.y),(endPoint.x- startPoint.x));
		var degs:Number = Math.round(angle*180/Math.PI);
		arrow_mc._rotation = degs;
		
		/*
		stopArrow_mc._rotation = degs;
			
		// calculate the position for the stop sign
		stopSign_mc._x = arrow_mc._x;
		stopSign_mc._y = arrow_mc._y;
		stopArrow_mc._x = arrow_mc._x;
		stopArrow_mc._y = arrow_mc._y;
		*/
		
		

	
	}
	/*
	private function updateSynchStatus():Void{
		
		if(completionType == "synchronize_teacher"){
			stopSign._visible = true;
			stopSignArrow._visible = true;
		}else{
			stopSign._visible = false;
			stopSignArrow._visible = false;
		}
		
	}
*/

	
	private function onPress():Void{
			// check double-click
			var now:Number = new Date().getTime();
			Debugger.log('now:'+now,Debugger.GEN,'onPress','CanvasTransition');
			Debugger.log('_dcStartTime:'+_dcStartTime,Debugger.GEN,'onPress','CanvasTransition');
			Debugger.log('now - _dcStartTime:'+(now - _dcStartTime)+' Config.DOUBLE_CLICK_DELAY:'+Config.DOUBLE_CLICK_DELAY,Debugger.GEN,'onPress','CanvasTransition');
			if((now - _dcStartTime) <= Config.DOUBLE_CLICK_DELAY){
				Debugger.log('DoubleClicking:'+this,Debugger.GEN,'onPress','CanvasTransition');
				_doubleClicking = true;
				_canvasController.transitionDoubleClick(this);
				
			
			}else{
				Debugger.log('SingleClicking:+'+this,Debugger.GEN,'onPress','CanvasTransition');
				_doubleClicking = false;
				
				Debugger.log('_canvasController:'+_canvasController,Debugger.GEN,'onPress','CanvasTransition');
				_canvasController.transitionClick(this);
				
	
			}
			
			_dcStartTime = now;
	
	}
	/*
	private function onRelease():Void{
		if(!_doubleClicking){
			Debugger.log('Releasing:'+this,Debugger.GEN,'onRelease','CanvasTransition');
			_canvasController.transitionRelease(this);
		}
		
	}
	
	private function onReleaseOutside():Void{
		Debugger.log('ReleasingOutside:'+this,Debugger.GEN,'onReleaseOutside','CanvasTransition');
		_canvasController.transitionReleaseOutside(this);
	}
	
	*/
	

}