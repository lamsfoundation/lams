import org.lamsfoundation.lams.common.*;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.util.ui.*;
import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.authoring.cv.*;


/**  
* CanvasActivity - 
*/  
class org.lamsfoundation.lams.authoring.cv.Bin extends MovieClip{  
  
	
	//this is set by the init object
	private var _canvasController:CanvasController;
	private var _canvasView:CanvasView;
		
	//locals
	private var up_mc:MovieClip;
	private var over_mc:MovieClip;
	
	//Defined so compiler can 'see' events added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	
	
	function Bin(){
		Debugger.log('hello',4,'Constructor','Bin');
		//let it wait one frame to set up the components.
		MovieClipUtils.doLater(Proxy.create(this,init));
	}
	
	public function init():Void{
		//set up handlers
		up_mc.addEventListener("onRollOver",this);
		over_mc.addEventListener("onRollOut",this);
		over_mc.addEventListener("onRelease",this);
		draw();
	}
	
	private function draw(){
		over_mc._visible = false;
		up_mc._visible = true;
	}
	
		
	
	
	private function onRelease():Void{
		Debugger.log('onRelease:'+this,Debugger.GEN,'onRelease','Bin');
		//see if we have an activity
		
	}
	
	private function onRollOver():Void{
		over_mc._visible = true;
		up_mc._visible = false;
	}
	
	private function onRollOut():Void{
		over_mc._visible = false;
		up_mc._visible = true;
		
	}

/*
	
	private function onPress():Void{
		
			
			// check double-click
			var now:Number = new Date().getTime();
			
			if((now - _dcStartTime) <= Config.DOUBLE_CLICK_DELAY){
				Debugger.log('DoubleClicking:'+this,Debugger.GEN,'onPress','CanvasActivity');
				_doubleClicking = true;
				_canvasController.activityDoubleClick(this);
				
				

			}else{
				Debugger.log('SingleClicking:+'+this,Debugger.GEN,'onPress','CanvasActivity');
				_doubleClicking = false;
				
				Debugger.log('_canvasController:'+_canvasController,Debugger.GEN,'onPress','CanvasActivity');
				_canvasController.activityClick(this);
				
			
			}
			
			_dcStartTime = now;
	
	}
		
	private function onReleaseOutside():Void{
		Debugger.log('ReleasingOutside:'+this,Debugger.GEN,'onReleaseOutside','CanvasActivity');
		_canvasController.activityReleaseOutside(this);
	}
	
	*/
	
	
	
	



}