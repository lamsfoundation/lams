import org.lamsfoundation.lams.authoring.cv.*;
import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.*
import mx.controls.*
import mx.utils.*
import mx.managers.*
import mx.events.*
/*
*
* @author      DC
* @version     0.1
* @comments    Property Inspector for the canvas
* 
*/
class PropertyInspector extends MovieClip{
	
	private var _canvasModel:CanvasModel;
	private var _canvasController:CanvasController;
	 //References to components + clips 
    private var _container:MovieClip;       //The container window that holds the dialog. Will contain any init params that were passed into createPopUp
   
    private var _tm:ThemeManager;
    private var toolDisplayName_lbl:Label;

   
	//Properties tab
    private var title_lbl:Label;
    private var title_txt:TextInput;
    private var desc_lbl:Label;
    private var desc_txt:TextInput;
    private var grouping_lbl:Label;
	private var currentGrouping_lbl:Label;
	private var editGrouping_btn:Button;
    private var runOffline_chk:CheckBox;
    private var defineLater_chk:CheckBox;
	
	//gates
	private var gateType_lbl:Label;
	private var gateType_cmb:ComboBox;
	private var startOffset_lbl:Label;
	private var endOffset_lbl:Label;
	private var hours_lbl:Label;
	private var mins_lbl:Label;
	private var hours_stp:NumericStepper;
	private var mins_stp:NumericStepper;
	private var endHours_stp:NumericStepper;
	private var endMins_stp:NumericStepper;

	
	

	
	//screen assets:
	private var body_pnl:Panel;
	private var bar_pnl:Panel;
	
	//Defined so compiler can 'see' events added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	
	
	/**
	 * Constructor
	 */
	function PropertyInspector(){
		//register to recive updates form the model
		Debugger.log('Constructor',Debugger.GEN,'PropertyInspector','PropertyInspector');
		_tm = ThemeManager.getInstance();
		//Set up this class to use the Flash event delegation model
		EventDispatcher.initialize(this);
		
		//let it wait one frame to set up the components.
		MovieClipUtils.doLater(Proxy.create(this,init));
		
	}
	
	public function init():Void{
		_canvasModel = _container._canvasModel;
		//might not need this ref..
		_canvasController = _container._canvasController;
		//_global.breakpoint();
		_canvasModel.addEventListener('viewUpdate',this);
		
		//Debugger.log('_canvasModel: ' + _canvasModel,Debugger.GEN,'init','PropertyInspector');
		setStyles();
		//set up handlers
		title_txt.addEventListener("focusOut",this);
		desc_txt.addEventListener("focusOut",this);
		runOffline_chk.addEventListener("click",this);
		defineLater_chk.addEventListener("click",this);
		
		gateType_cmb.addEventListener("change",Delegate.create(this,onGateTypeChange));
		hours_stp.addEventListener("change",Delegate.create(this,onScheduleOffsetChange));
		mins_stp.addEventListener("change",Delegate.create(this,onScheduleOffsetChange));
		endHours_stp.addEventListener("change",Delegate.create(this,onScheduleOffsetChange));
		endMins_stp.addEventListener("change",Delegate.create(this,onScheduleOffsetChange));
		
		gateType_lbl.text = Dictionary.getValue('trans_dlg_gatetypecmb');
		hours_lbl.text = Dictionary.getValue('pi_hours');
		mins_lbl.text = Dictionary.getValue('pi_mins');
		startOffset_lbl.text = Dictionary.getValue('pi_start_offset');
		endOffset_lbl.text = Dictionary.getValue('pi_end_offset');
		
		//populate the synch type combo:
		gateType_cmb.dataProvider = Activity.getGateActivityTypes();
		
		//hide all the controls at startup
		showGateControls(false);
		showToolActivityControls(false);
		showGroupingControls(false);
		
		//fire event to say we have loaded
		_container.contentLoaded();
	}
	
	
	
	/**
	 * Recieves update events from the model.
	 * @usage   
	 * @param   event 
	 * @return  
	 */
	public function viewUpdate(event:Object):Void{
		//Debugger.log('Recived an Event dispather UPDATE!, updateType:'+event.updateType+', target'+event.target,4,'viewUpdate','PropertyInspector');
		 //Update view from info object
       
       var cm:CanvasModel = event.target;
	   
	   switch (event.updateType){
            case 'SELECTED_ITEM' :
                updateItemProperties(cm);
                break;
                   
				
            default :
                //Debugger.log('unknown update type :' + event.updateType,Debugger.CRITICAL,'update','org.lamsfoundation.lams.CanvasView');
		}

	}
	
	/**
	 * Get called when something is selected in the cavas.
	 * Updates the details in the propertyu inspector widgets
	 * @usage   
	 * @param   cm 
	 * @return  
	 */
	private function updateItemProperties(cm:CanvasModel):Void{
		//try to cast the selected item to see what we have (instance of des not seem to work)
		if(CanvasActivity(cm.selectedItem) != null){
			Debugger.log('Its a canvas activity',4,'updateItemProperties','PropertyInspector');
			var ca = CanvasActivity(cm.selectedItem);
			var a:Activity = ca.activity;			
			if(a.isGateActivity()){
				showToolActivityControls(false);
				showGateControls(true);
				showGroupingControls(true);
				showGateActivityProperties(GateActivity(a));
				checkEnableGateControls();
			}else{
				showToolActivityControls(true);
				showGateControls(false);
				showGroupingControls(true);
				showToolActivityProperties(ToolActivity(a));
			}
			/*
			runOffline_txa.text =  StringUtils.cleanNull(cm.selectedItem.activity.offlineInstructions);
			runOnline_txa.text =  StringUtils.cleanNull(cm.selectedItem.activity.onlineInstructions);
			*/
			
		}else if(CanvasTransition(cm.selectedItem) != null){
			var ct = CanvasTransition(cm.selectedItem);
			var t:Transition = ct.transition;
			Debugger.log('Its a canvas transition',4,'updateItemProperties','PropertyInspector');
			
			showTransitionProperties(t);
			
			showGroupingControls(false);
			showToolActivityControls(false);
			showGateControls(false);
			
		}else{
			Debugger.log('Its a something we dont know',Debugger.CRITICAL,'updateItemProperties','PropertyInspector');
			showGroupingControls(false);
			showToolActivityControls(false);
			showGateControls(false);
		}
	}
	
	
	
	private function showToolActivityProperties(ta:ToolActivity){
		
		
		toolDisplayName_lbl.text = StringUtils.cleanNull(ta.toolDisplayName);
		title_txt.text = StringUtils.cleanNull(ta.title);
		desc_txt.text = StringUtils.cleanNull(ta.description);
		runOffline_chk.selected = ta.runOffline;
		defineLater_chk.selected = ta.defineLater;
					
		currentGrouping_lbl.text = "GroupingUIID:"+StringUtils.cleanNull(ta.runOffline.groupingUIID);
			
		
	}
	
	private function showGateActivityProperties(ga:GateActivity){
		toolDisplayName_lbl.text = 'Gate';
		//loop through combo to fins SI of our gate activity type
		for (var i=0; i<gateType_cmb.dataProvider.length;i++){
			if(ga.activityTypeID == gateType_cmb.dataProvider[i].data){
				gateType_cmb.selectedIndex=i;
			}
		}
	
	}
	
	private function showTransitionProperties(t:Transition){
		//show from act and to act
		
	}

	private function checkEnableGateControls(){
		//Debugger.log('Activity.SCHEDULE_GATE_ACTIVITY_TYPE:'+Activity.SCHEDULE_GATE_ACTIVITY_TYPE,Debugger.GEN,'checkEnableGateControls','PropertyInspector');
		//Debugger.log('_canvasModel.selectedItem.activity.activityTypeID:'+_canvasModel.selectedItem.activity.activityTypeID,Debugger.GEN,'checkEnableGateControls','PropertyInspector');
		if(_canvasModel.selectedItem.activity.activityTypeID == Activity.SCHEDULE_GATE_ACTIVITY_TYPE){
			trace('enabling....');
			hours_stp.enabled = true;
			mins_stp.enabled = true;
			endHours_stp.enabled = true;
			endMins_stp.enabled = true;
			
		}
		/**/
		else{
			trace('disabling....');
		hours_stp.enabled = false;
			mins_stp.enabled = false;
			endHours_stp.enabled = false;
			endMins_stp.enabled = false;
		}
		
		//this is a crazy hack to stop the steppter dissapearing after its .enabled property is set.
		//waits 2 frames to re-display the control ( 1 doensnt work!)
		MovieClipUtils.doLater(Proxy.create(this,showGateControlsLater,true));
	}
	
	public function showGateControlsLater(bool:Boolean){
		MovieClipUtils.doLater(Proxy.create(this,showGateControls,bool));
	}
	
	private function showToolActivityControls(v:Boolean){
		title_lbl.visible = v;
		title_txt.visible = v;
		desc_lbl.visible = v;
		desc_txt.visible = v;
		grouping_lbl.visible = v;
		currentGrouping_lbl.visible = v;
		runOffline_chk.visible = v;
		defineLater_chk.visible = v;
		editGrouping_btn.visible = v;
	}
	
	private function showGateControls(v:Boolean){
		trace('showGateControls....'+v);
		hours_lbl.visible = v;
		mins_lbl.visible = v;
		hours_stp.visible = v;
		mins_stp.visible = v;
		endHours_stp.visible = v;
		endMins_stp.visible = v;
		gateType_lbl.visible = v;
		gateType_cmb.visible = v;
		startOffset_lbl.visible = v;
		endOffset_lbl.visible = v;
	}
	
	private function showGroupingControls(v:Boolean){
		trace('show grp controls.....'+v);
		grouping_lbl.visible = v;
		currentGrouping_lbl.visible = v;
		editGrouping_btn.visible = v;
	}
	

	/**
    * Main resize method, called by scrollpane container/parent
    */
    public function setSize(w:Number,h:Number){
        //Debugger.log('setSize',Debugger.GEN,'setSize','org.lamsfoundation.lams.common.ws.WorkspaceDialog');
        //Size the bkg_pnl
		
        body_pnl.setSize(w,h-bar_pnl.height);
        bar_pnl.setSize(w);

        
    }
	
	/**
	 * Get the CSSStyleDeclaration objects for each component and applies them
	 * directly to the instanced
	 * @usage   
	 * @return  
	 */
	private function setStyles() {
        Debugger.log('Button stykle obejct',Debugger.GEN,'setStyles','PropertyInspector');
		var styleObj = _tm.getStyleObject('button');
		ObjectUtils.printObject(styleObj);
		editGrouping_btn.setStyle('styleName',styleObj);
		
		styleObj = _tm.getStyleObject('label');
		title_lbl.setStyle('styleName',styleObj);
		desc_lbl.setStyle('styleName',styleObj);
		grouping_lbl.setStyle('styleName',styleObj);
		currentGrouping_lbl.setStyle('styleName',styleObj);
		gateType_lbl.setStyle('styleName',styleObj);
		gateType_lbl.setStyle('styleName',styleObj);
		startOffset_lbl.setStyle('styleName',styleObj);
		endOffset_lbl.setStyle('styleName',styleObj);
		hours_lbl.setStyle('styleName',styleObj);
		mins_lbl.setStyle('styleName',styleObj);
		toolDisplayName_lbl.setStyle('styleName',styleObj);
		
		styleObj = _tm.getStyleObject('combo');
		gateType_cmb.setStyle('styleName',styleObj);
		
		styleObj = _tm.getStyleObject('textinput');
		title_txt.setStyle('styleName',styleObj);
		desc_txt.setStyle('styleName',styleObj);
		
		styleObj = _tm.getStyleObject('checkbox');
		runOffline_chk.setStyle('styleName',styleObj);
		defineLater_chk.setStyle('styleName',styleObj);
		
		styleObj = _tm.getStyleObject('numericstepper');
		hours_stp.setStyle('styleName',styleObj);
		mins_stp.setStyle('styleName',styleObj);
		endHours_stp.setStyle('styleName',styleObj);
		endMins_stp.setStyle('styleName',styleObj);
		
		
		
    }
    
    //Gets+Sets
    /**
    * set the container refernce to the window holding the dialog
    */
    public function set container(value:MovieClip){
        _container = value;
    }
	
	
	/////////////////////////////////////////////////
	//------------ controller section -------------//
	/////////////////////////////////////////////////
	
	/**
	 *Handles change event fired from the gateType_cmb
	 * @usage   
	 * @param   evt 
	 * @return  
	 */
	private function onGateTypeChange(evt:Object){
		_canvasModel.selectedItem.activity.activityTypeID = evt.target.value;
		Debugger.log('Set gate type to: _canvasModel.selectedItem.activity.activityTypeID:'+_canvasModel.selectedItem.activity.activityTypeID,Debugger.GEN,'onGateTypeChange','PropertyInspector');
		checkEnableGateControls();
	}
	
	private function onScheduleOffsetChange(evt:Object){
		
		var startOffsetMins:Number = (hours_stp.value * 60) + mins_stp.value;
		_canvasModel.selectedItem.activity.gateStartTimeOffset = startOffsetMins;
		var endOffsetMins:Number = (endHours_stp.value * 60) + endMins_stp.value;
		_canvasModel.selectedItem.activity.gateEndTimeOffset = endOffsetMins;
		Debugger.log('activity.gateStartTimeOffset :'+_canvasModel.selectedItem.activity.gateStartTimeOffset ,Debugger.GEN,'onScheduleOffsetChange','PropertyInspector');
		Debugger.log('activity.gateEndTimeOffset :'+_canvasModel.selectedItem.activity.gateEndTimeOffset,Debugger.GEN,'onScheduleOffsetChange','PropertyInspector');
		
		
		
		
	}
	
	
   /**
	 * Recieves the click events from the canvas views (inc Property Inspector) buttons.  Based on the target
	 * the relevent method is called to action the user request
	 * @param   evt 
	 */
	 /**/
	public function click(e):Void{
		var tgt:String = new String(e.target);
		//Debugger.log('click tgt:'+tgt,Debugger.GEN,'click','PropertyInspector');
		//Debugger.log('indexOf("defineLater_chk"):'+tgt.indexOf("defineLater_chk"),Debugger.GEN,'click','PropertyInspector');
		//Debugger.log('indexOf("runOffline_chk"):'+tgt.indexOf("runOffline_chk"),Debugger.GEN,'click','PropertyInspector');
		if(tgt.indexOf("defineLater_chk") != -1){
			
			_canvasModel.selectedItem.activity.defineLater = defineLater_chk.selected;
			Debugger.log('_canvasModel.selectedItem.activity.defineLater:'+_canvasModel.selectedItem.activity.defineLater,Debugger.GEN,'click','PropertyInspector');
		
		}else if(tgt.indexOf("runOffline_chk") != -1){
			
			_canvasModel.selectedItem.activity.runOffline = runOffline_chk.selected;
			Debugger.log('_canvasModel.selectedItem.activity.runOffline:'+_canvasModel.selectedItem.activity.runOffline,Debugger.GEN,'click','PropertyInspector');
		}
	}
	
	
	/**
	 * Recieves the click events from the canvas views (inc Property Inspector) buttons.  Based on the label
	 * the relevent method is called to action the user request
	 * @param   evt 
	 */
	public function focusOut(e):Void{
		var tgt:String = new String(e.target);
		Debugger.log('focusOut tgt:'+tgt,Debugger.GEN,'focusOut','PropertyInspector');
		
		if(tgt.indexOf("title_txt") != -1){
			//todo check if this is the right place to set edited content, should it be ddm?
			_canvasModel.selectedItem.activity.title = title_txt.text;
		}else if(tgt.indexOf("desc_txt") != -1){
			_canvasModel.selectedItem.activity.description= desc_txt.text;
		}
		
	}
	
}

