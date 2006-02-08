import org.lamsfoundation.lams.common.*;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.util.ui.*;
import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.authoring.cv.*;


/**  
* CanvasActivity - 
*/  
class org.lamsfoundation.lams.authoring.cv.CanvasActivity extends MovieClip implements ICanvasActivity{  
//class org.lamsfoundation.lams.authoring.cv.CanvasActivity extends MovieClip{  
  
	public static var TOOL_ACTIVITY_WIDTH:Number = 123.1;
	public static var TOOL_ACTIVITY_HEIGHT:Number = 50.5;
	public static var GATE_ACTIVITY_HEIGHT:Number = 30;
	public static var GATE_ACTIVITY_WIDTH:Number = 30;
	
	//this is set by the init object
	private var _canvasController:CanvasController;
	private var _canvasView:CanvasView;
	//TODO:This should be ToolActivity
	private var _activity:Activity;
	
	//locals
	private var icon_mc:MovieClip;
	private var icon_mcl:MovieClipLoader;
	private var title_lbl:MovieClip;
	private var stopSign_mc:MovieClip;
	private var clickTarget_mc:MovieClip;
	private var canvasActivity_mc:MovieClip;	private var _dcStartTime:Number = 0;
	private var _doubleClicking:Boolean;
	private var _visibleWidth:Number;
	private var _visibleHeight:Number;
	
	
	
	function CanvasActivity(){
		//Debugger.log("_activity:"+_activity.title,4,'Constructor','CanvasActivity');
		//let it wait one frame to set up the components.
		//this has to be set b4 the do later :)
		if(_activity.isGateActivity()){
			_visibleHeight = GATE_ACTIVITY_HEIGHT;
			_visibleWidth = GATE_ACTIVITY_WIDTH;
		}else{
			_visibleHeight = TOOL_ACTIVITY_HEIGHT;
			_visibleWidth = TOOL_ACTIVITY_WIDTH;
		}
		//call init if we have passed in the _activity as an initObj in the attach movie,
		//otherwise wait as the class outside will call it
		if(_activity != undefined){
			init();
		}
		
	}
	
	public function init(initObj):Void{
		if(initObj){
			_canvasView = initObj.canvasView;
			_canvasController = initObj.canvasController;
			_activity = initObj.activity;
		}

		
		if(!_activity.isGateActivity()){
			loadIcon();
		}
		
		MovieClipUtils.doLater(Proxy.create(this,draw));

	}
	
	
	private function loadIcon():Void{
		//Debugger.log('Running, _activity.libraryActivityUIImage:'+_activity.libraryActivityUIImage,4,'loadIcon','CanvasActivity');
		icon_mc = this.createEmptyMovieClip("icon_mc", this.getNextHighestDepth());
		var ml = new MovieLoader(Config.getInstance().serverUrl+_activity.libraryActivityUIImage,setUpActIcon,this,icon_mc);	
		//icon_mc = MovieLoader.movieCache[Config.getInstance().serverUrl+_activity.libraryActivityUIImage];
		//Debugger.log('icon_mc:'+icon_mc,4,'loadIcon','CanvasActivity');
		setUpActIcon(icon_mc);
	}
	
	
	
	
	
	
	private function setUpActIcon(icon_mc):Void{
		icon_mc._x = (this._width / 2) - (icon_mc._width / 2);
		icon_mc._y = (this._height / 2) - (icon_mc._height / 2) - 5;
	}
	
	private function draw(){		Debugger.log(_activity.title+',_activity.isGateActivity():'+_activity.isGateActivity(),4,'draw','CanvasActivity');
		if(_activity.isGateActivity()){
			stopSign_mc._visible = true;
			canvasActivity_mc._visible=false;
			title_lbl.visible=false;
			clickTarget_mc._width = GATE_ACTIVITY_WIDTH;
			clickTarget_mc._height= GATE_ACTIVITY_HEIGHT;
			
		}else{
			canvasActivity_mc._visible=true;
			title_lbl.visible=true;
			//clickTarget_mc._visible=true;
			stopSign_mc._visible = false;
			
			//write text
			title_lbl.text = _activity.title;
			
			clickTarget_mc._width = TOOL_ACTIVITY_WIDTH;
			clickTarget_mc._height= TOOL_ACTIVITY_HEIGHT;
			
		}
	
		
	
		//indicate grouping
		
		//position
		_x = _activity.xCoord;
		_y = _activity.yCoord;
		

		
		Debugger.log('canvasActivity_mc._visible'+canvasActivity_mc._visible,4,'draw','CanvasActivity');
		_visible = true;
	}
	
	
	private function onPress():Void{
		
			
			// check double-click
			var now:Number = new Date().getTime();
			
			if((now - _dcStartTime) <= Config.DOUBLE_CLICK_DELAY){
				//Debugger.log('DoubleClicking:'+this,Debugger.GEN,'onPress','CanvasActivity');
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
				//Debugger.log('SingleClicking:+'+this,Debugger.GEN,'onPress','CanvasActivity');
				_doubleClicking = false;
				
				//Debugger.log('_canvasController:'+_canvasController,Debugger.GEN,'onPress','CanvasActivity');
				
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
			//Debugger.log('Releasing:'+this,Debugger.GEN,'onRelease','CanvasActivity');
								
			_canvasController.activityRelease(this);
		}
		
	}
	
	private function onReleaseOutside():Void{
		//Debugger.log('ReleasingOutside:'+this,Debugger.GEN,'onReleaseOutside','CanvasActivity');

		_canvasController.activityReleaseOutside(this);
	}
	
	
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function getVisibleWidth ():Number {
		return _visibleWidth;
	}

	
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function getVisibleHeight ():Number {
		return _visibleHeight;
	}

	
	
	public function get activity():Activity{
		return getActivity();
	}
	
	public function set activity(a:Activity){
		setActivity(a);
	}
	
	
	public function getActivity():Activity{
		return _activity;

	}
	
	public function setActivity(a:Activity){
		_activity = a;
	}


}