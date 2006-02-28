import org.lamsfoundation.lams.common.*;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.ui.*;
import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.authoring.cv.*;
import mx.controls.*;
import mx.managers.*


/**  
* CanvasParallelActivity
* This is the UI / view representation of a complex (parralel) activity
*/  
class org.lamsfoundation.lams.authoring.cv.CanvasParallelActivity extends MovieClip implements ICanvasActivity{   
//class org.lamsfoundation.lams.authoring.cv.CanvasParallelActivity extends MovieClip {   
  
	private var CHILD_OFFSET_X:Number = 8;
	private var CHILD1_OFFSET_Y:Number = 45;
	private var CHILD2_OFFSET_Y:Number = 108;
	//this is set by the init object
	private var _canvasController:CanvasController;
	private var _canvasView:CanvasView;
	
	//Set by the init obj
	private var _activity:Activity;
	private var _children:Array;
	
	//refs to screen items:
	private var container_pnl:Panel;
	private var header_pnl:Panel;
	private var title_lbl:Label;
	private var actCount_lbl:Label;
	private var childActivities_mc:MovieClip;
	private var clickTarget_mc:MovieClip;
	private var padlockClosed_mc:MovieClip;
	private var padlockOpen_mc:MovieClip;
	
	
	private var _ddm:DesignDataModel;
	private var _dcStartTime:Number = 0;
	private var _doubleClicking:Boolean;
	private var child1_mc:MovieClip;
	private var child2_mc:MovieClip;
	private var _locked:Boolean;
	private var _visibleHeight:Number;
	private var _visibleWidth:Number;
	
	
	
	
	function CanvasParallelActivity(){
		Debugger.log("_activity:"+_activity.title+'uiID:'+_activity.activityUIID+' children:'+_children.length,Debugger.GEN,'Constructor','CanvasParallelActivity');
		_visible = false;
		_ddm = new DesignDataModel()
		_visibleHeight = container_pnl._height;
		_visibleWidth = container_pnl._width;
		//init();
		MovieClipUtils.doLater(Proxy.create(this,init));
	}
	
	public function init():Void{
		
		
		
		//set up some handlers:
		clickTarget_mc.onPress = Proxy.create(this,localOnPress);
		clickTarget_mc.onRelease = Proxy.create(this,localOnRelease);
		clickTarget_mc.onReleaseOutside = Proxy.create(this,localOnReleaseOutside);

		_ddm.getComplexActivityChildren(_activity.activityUIID);
		_locked = true;
		
		
		
		var child1:Activity;
		var child2:Activity;
		if(_children[0].orderID < _children[1].orderID){
			child1 = _children[0];
			child2 = _children[1];
			
		}else{
			child1 = _children[1];
			child2 = _children[0];
		
		}
		//set the positioning co-ords
		child1.xCoord = CHILD_OFFSET_X;
		child1.yCoord = CHILD1_OFFSET_Y;
		child2.xCoord = CHILD_OFFSET_X;
		child2.yCoord = CHILD2_OFFSET_Y;
		//for some reason attachmovie is not showing mc, only the label fields!?
		//child1_mc = childActivities_mc.createChildAtDepth("CanvasActivity",DepthManager.kTop,{_activity:child1,_canvasController:_canvasController,_canvasView:_canvasView});
		//var child1_mc = childActivities_mc.attachMovie("CanvasActivity",'child_'+child1.activityUIID,this.getNextHighestDepth(),{_activity:child1,_canvasController:_canvasController,_canvasView:_canvasView});
		//child2_mc = childActivities_mc.createChildAtDepth("CanvasActivity",DepthManager.kTop,{_activity:child2,_canvasController:_canvasController,_canvasView:_canvasView});
		//var child2_mc = childActivities_mc.attachMovie("CanvasActivity",'child_'+child2.activityUIID,this.getNextHighestDepth(),{_activity:child2,_canvasController:_canvasController,_canvasView:_canvasView});
		
		//so now it is placed on in the IDE and we just call init
		child1_mc.init({activity:child1,canvasController:_canvasController,canvasView:_canvasView});
		child2_mc.init({activity:child2,canvasController:_canvasController,canvasView:_canvasView});
		
		//let it wait one frame to set up the components.
		childActivities_mc.createChildAtDepth("Bin",DepthManager.kTop);
		MovieClipUtils.doLater(Proxy.create(this,draw));
		
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
	
	private function draw(){			
			//write text
			title_lbl.text = _activity.title;
			actCount_lbl.text = _children.length+" activities";
			
//			_global.breakpoint();
			
			header_pnl.borderType='outset';
			container_pnl.setStyle("backgroundColor",0x4289FF);
			
			//position the container (this)
			_x = _activity.xCoord;
			_y = _activity.yCoord;
		
			if(_locked){
				padlockClosed_mc._visible = true;
				padlockOpen_mc._visible = false;
				clickTarget_mc._height = 173;
			}else{
				padlockOpen_mc._visible = true;
				padlockClosed_mc._visible = false;
				clickTarget_mc._height = 38;
			}
		
			_visible = true;
			//child1_mc._visible = true;
						
	}
	
	
	private function localOnPress():Void{
			
			// check double-click
			var now:Number = new Date().getTime();
			
			if((now - _dcStartTime) <= Config.DOUBLE_CLICK_DELAY){
				Debugger.log('DoubleClicking:'+this,Debugger.GEN,'localOnPress','CanvasParallelActivity');
				_doubleClicking = true;
				
				//if we double click on the glass mask - then open the container to allow the usr to see the activities inside.
				if(_locked){
					_locked = false;
				}else{
					_locked = true;
				}
				draw();
				
				
				
			}else{
				Debugger.log('SingleClicking:+'+this,Debugger.GEN,'localOnPress','CanvasParallelActivity');
				_doubleClicking = false;
				_canvasController.activityClick(this);
				
			}
			
			_dcStartTime = now;
	
	}
	
	private function localOnRelease():Void{
		Debugger.log('_doubleClicking:'+_doubleClicking+', localOnRelease:'+this,Debugger.GEN,'localOnRelease','CanvasParallelActivity');
		if(!_doubleClicking){
			_canvasController.activityRelease(this);
		}
		
	}
	
	private function localOnReleaseOutside():Void{
		Debugger.log('localOnReleaseOutside:'+this,Debugger.GEN,'localOnReleaseOutside','CanvasParallelActivity');
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
	
	
	
	



}