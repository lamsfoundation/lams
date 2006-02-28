import org.lamsfoundation.lams.common.*;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.ui.*;
import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.authoring.cv.*;
//import org.lamsfoundation.lams.authoring.cv.DesignDataModel;
import org.lamsfoundation.lams.common.style.*;
import mx.controls.*;
import mx.managers.*


/**  
* CanvasOptionalActivity
* This is the UI / view representation of a complex (Optional) activity
*/  
class org.lamsfoundation.lams.authoring.cv.CanvasOptionalActivity extends MovieClip implements ICanvasActivity{   
//class org.lamsfoundation.lams.authoring.cv.CanvasParallelActivity extends MovieClip {   
  
	private var CHILD_OFFSET_X:Number = 8;
	private var CHILD_OFFSET_Y:Number = 57;
	private var CHILD_INCRE:Number = 60;
	//this is set by the init object
	private var _canvasController:CanvasController;
	private var _canvasView:CanvasView;
	
	//Set by the init obj
	private var _activity:Activity;
	private var _children:Array;
	
	//refs to screen items:
	private var container_pnl:Panel;
	private var header_pnl:Panel;
	private var act_pnl:Panel;
	private var title_lbl:Label;
	private var actCount_lbl:Label;
	private var childActivities_mc:MovieClip;
	private var clickTarget_mc:MovieClip;
	private var padlockClosed_mc:MovieClip;
	private var padlockOpen_mc:MovieClip;
	
	
	
	private var _dcStartTime:Number = 0;
	private var _doubleClicking:Boolean;
	//private var child1_mc:MovieClip;
	//private var child2_mc:MovieClip;
	private var child_mc:MovieClip;
	private var _locked:Boolean;
	private var _visibleHeight:Number;
	private var _visibleWidth:Number;
	private var _tm:ThemeManager;
	private var _ddm:DesignDataModel;
	
	
	
	function CanvasOptionalActivity(){
		_ddm = new DesignDataModel();
		Debugger.log("_activity:"+_activity.title+'uiID:'+_activity.activityUIID+' children:'+_children.length,Debugger.GEN,'Constructor','CanvasOptionalActivity');
		_visible = false;
		_tm = ThemeManager.getInstance();
		_visibleHeight = container_pnl._height;
		_visibleWidth = container_pnl._width;
		//init();
		MovieClipUtils.doLater(Proxy.create(this,init));
	}
	
	public function init():Void{
		
		
		//Debugger.log('I am in CanvasOptionalActivity init :',Debugger.CRITICAL,'Init','CanvasOptionalActivity');
		//set up some handlers:
			
		_locked = true;
		
		childActivities_mc = this.createEmptyMovieClip("childActivities_mc", this.getNextHighestDepth());
		//var numofChildren:Array = _children //_ddm.getComplexActivityChildren(_activity.activityUIID);
		Debugger.log(" Iam in Init where uiID:"+_activity.activityUIID+' has children:'+_children.length,Debugger.GEN,'Init','CanvasOptionalActivity');
		var children_mc:Array = new Array();
		for (var i=0; i<_children.length; i++){
			Debugger.log("Init - Child "+_children[i].title+' has UID:'+_children[i].activityUIID,Debugger.GEN,'Init','CanvasOptionalActivity');
		//	
			
				//for some reason attachmovie is not showing mc, only the label fields!?
				
				children_mc[i] = childActivities_mc.createChildAtDepth("CanvasActivity",DepthManager.kTop,{_activity:_children[i],_canvasController:_canvasController,_canvasView:_canvasView});
				//children_mc[i] = childActivities_mc.attachMovie("CanvasActivity","CanvasActivity"+i, childActivities_mc.getNextHighestDepth(), {_activity:_children[i],_canvasController:_canvasController,_canvasView:_canvasView});
				//so now it is placed on in the IDE and we just call init
				
				Debugger.log("Init - Child mc path: "+children_mc[i]+ ' has UID:'+children_mc[i].activity.activityUIID,Debugger.GEN,'Init','CanvasOptionalActivity');
				//let it wait one frame to set up the components.
				
				//set the positioning co-ords
				children_mc[i].activity.xCoord = CHILD_OFFSET_X;
				children_mc[i].activity.yCoord = CHILD_OFFSET_Y+(i*CHILD_INCRE);
				//children_mc[i]._visible = true;
		}
				//childActivities_mc.createChildAtDepth("Bin",DepthManager.kTop);
				this.createEmptyMovieClip("clickTarget_mc", this.getNextHighestDepth());
				clickTarget_mc.attachMovie("clickTarget_mc", "clickTarget_mc", this.getNextHighestDepth(), {_alpha:50});
				
				
				clickTarget_mc.onPress = Proxy.create(this,localOnPress);
				clickTarget_mc.onRelease = Proxy.create(this,localOnRelease);
				clickTarget_mc.onReleaseOutside = Proxy.create(this,localOnReleaseOutside);
				
				MovieClipUtils.doLater(Proxy.create(this,draw(_children.length)));
			//}
		
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
	
	private function draw(numOfChildren:Number){
			
			var panelHeight = CHILD_OFFSET_Y + (numOfChildren*CHILD_INCRE);
			if (numOfChildren <= 1){
				panelHeight = 120;
			}
			setStyles()
			
			//write text
			title_lbl.text = _activity.title = 'Optional Activities';
			actCount_lbl.text = _children.length+" activities";
			//actCount_lbl.setStyle('fontSize', 8);
//			_global.breakpoint();
			
			header_pnl.borderType='outset';
			act_pnl.borderType='inset';
			container_pnl.setStyle("backgroundColor",0x4289FF);
			
			//position the container (this)
			_x = _activity.xCoord;
			_y = _activity.yCoord;
			
			//dimentions of container (this)
			if (numOfChildren > 1){
				container_pnl._height = panelHeight;
			}
			
			if(_locked){
				padlockClosed_mc._visible = true;
				padlockOpen_mc._visible = false;
				//clickTarget_mc.swapDepths(children_mc[numOfChildren-1]);
				clickTarget_mc._height = panelHeight;
			}else{
				padlockOpen_mc._visible = true;
				padlockClosed_mc._visible = false;
				clickTarget_mc._height = 45;
			}
			Debugger.log("I am in Draw :"+_activity.title+'uiID:'+_activity.activityUIID+' children:'+_children.length,Debugger.GEN,'Draw','CanvasOptionalActivity');
			_visible = true;
			//child1_mc._visible = true;
						
	}
	
	
	private function localOnPress():Void{
			
			// check double-click
			var now:Number = new Date().getTime();
			
			if((now - _dcStartTime) <= Config.DOUBLE_CLICK_DELAY){
				Debugger.log('DoubleClicking:'+this,Debugger.GEN,'localOnPress','CanvasOptionalActivity');
				_doubleClicking = true;
				
				//if we double click on the glass mask - then open the container to allow the usr to see the activities inside.
				if(_locked){
					_locked = false;
				}else{
					_locked = true;
				}
				draw();
				
				
				
			}else{
				Debugger.log('SingleClicking:+'+this,Debugger.GEN,'localOnPress','CanvasOptionalActivity');
				_doubleClicking = false;
				_canvasController.activityClick(this);
				
			}
			
			_dcStartTime = now;
	
	}
	
	private function localOnRelease():Void{
		Debugger.log('_doubleClicking:'+_doubleClicking+', localOnRelease:'+this,Debugger.GEN,'localOnRelease','CanvasOptionalActivity');
		if(!_doubleClicking){
			_canvasController.activityRelease(this);
		}
		
	}
	
	private function localOnReleaseOutside():Void{
		Debugger.log('localOnReleaseOutside:'+this,Debugger.GEN,'localOnReleaseOutside','CanvasOptionalActivity');
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
	
	public function get locked ():Boolean {
		return _locked;
	}
	private function setStyles():Void{
		
		
		
		var styleObj = _tm.getStyleObject('label');
		title_lbl.setStyle(styleObj);
		styleObj = _tm.getStyleObject('smlLabel');
		actCount_lbl.setStyle('styleName', styleObj);
		
	}
	
	



}