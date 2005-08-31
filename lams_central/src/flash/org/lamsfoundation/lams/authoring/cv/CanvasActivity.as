import org.lamsfoundation.lams.common.*;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.util.ui.*;
import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.authoring.cv.*;


/**  
* CanvasActivity - 
*/  
class org.lamsfoundation.lams.authoring.cv.CanvasActivity extends MovieClip{  
  
	
	//this is set by the init object
	private var _canvasController:CanvasController;
	private var _canvasView:CanvasView;
	private var _activity:Activity;
	
	//locals
	private var _icon_mc;
	private var title_lbl:MovieClip;
	private var stopSign_mc:MovieClip;
	private var clickTarget_mc:MovieClip;
	private var canvasActivity_mc:MovieClip;	private var _dcStartTime:Number = 0;
	private var _doubleClicking:Boolean;
	
	function CanvasActivity(){
		Debugger.log("_activity:"+_activity.title,4,'Constructor','CanvasActivity');
		//let it wait one frame to set up the components.
		MovieClipUtils.doLater(Proxy.create(this,init));
	}
	
	public function init():Void{
		canvasActivity_mc._visible=false;
		stopSign_mc._visible=false;
		title_lbl._visible=false;
		clickTarget_mc._visible=false;
		_global.breakpoint();
		if(_activity.isGateActivity()){
			loadIcon();
		}
		draw();
	}
	
	public function get activity():Activity{
		return _activity;
	}
	
	public function set activity(a:Activity){
		_activity = a;
	}
	
	private function loadIcon():Void{
		Debugger.log('Running, _activity.libraryActivityUIImage:'+_activity.libraryActivityUIImage,4,'loadIcon','CanvasActivity');
		_icon_mc = this.createEmptyMovieClip("_icon_mc", this.getNextHighestDepth());
		var ml = new MovieLoader(_activity.libraryActivityUIImage,setUpActIcon,this,_icon_mc);	
	}
	
	
	
	
	private function setUpActIcon(icon_mc):Void{
		icon_mc._x = (this._width / 2) - (icon_mc._width / 2);
		icon_mc._y = (this._height / 2) - (icon_mc._height / 2) - 5;
	}
	
	private function draw(){		Debugger.log(_activity.title,4,'draw','CanvasActivity');
		if(_activity.isGateActivity()){
			stopSign_mc._visible = true;
		}else{
			canvasActivity_mc._visible=true;
			title_lbl._visible=true;
			clickTarget_mc._visible=true;
			//write text
			title_lbl.text = _activity.title;
		}
	
		//indicate grouping
		
	}
	
	
	private function onPress():Void{
		
			
			// check double-click
			var now:Number = new Date().getTime();
			
			if((now - _dcStartTime) <= Config.DOUBLE_CLICK_DELAY){
				Debugger.log('DoubleClicking:'+this,Debugger.GEN,'onPress','CanvasActivity');
				_doubleClicking = true;
				_canvasController.activityDoubleClick(this);
				
				
				/*
				if(workspaceRef.inspecting){
			// click to zoom in / zoom out
			if(workspaceRef.zooming == "in"){
				closeZoomedActivity(this._parent);	
			}else if(workspaceRef.zooming == "out"){
				zoomActivity(this._parent, true);
			}
				*/
				
			}else{
				Debugger.log('SingleClicking:+'+this,Debugger.GEN,'onPress','CanvasActivity');
				_doubleClicking = false;
				
				Debugger.log('_canvasController:'+_canvasController,Debugger.GEN,'onPress','CanvasActivity');
				_canvasController.activityClick(this);
				
				
				/*
				if (userDrawingTransition){
					debug('user drawing transition');
					userDrawTransition(this,true);
				}else if (userDrawingBox){
					workspaceBkg.onPress();
				}else{
					doMove = false;
					//move all the acts with the box
					//Mouse.addListener(moveOpActBoxListner);
					this.onEnterFrame = updateOpActPositions;
					
					draggedOptionalActivity = optActId;
					workspaceRef.selectActivity(this);
					this.startDrag(false);
					this.pickup();
				}
				*/
			}
			
			_dcStartTime = now;
	
	}
	
	private function onRelease():Void{
		if(!_doubleClicking){
			Debugger.log('Releasing:'+this,Debugger.GEN,'onRelease','CanvasActivity');
			_canvasController.activityRelease(this);
		}
		
	}
	
	private function onReleaseOutside():Void{
		Debugger.log('ReleasingOutside:'+this,Debugger.GEN,'onReleaseOutside','CanvasActivity');
		_canvasController.activityReleaseOutside(this);
	}
	
	
	
	
	
	



}