/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ************************************************************************
 */
 
import mx.controls.*
import mx.utils.*
import mx.managers.*
import mx.events.*

import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.wizard.*
import org.lamsfoundation.lams.wizard.steps.*
import org.lamsfoundation.lams.monitoring.User;
import org.lamsfoundation.lams.monitoring.Orgnanisation;
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.mvc.*
import org.lamsfoundation.lams.common.ws.*
import org.lamsfoundation.lams.common.Config

import it.sephiroth.TreeDnd


/**
* Wizard view
* Relects changes in the WizardModel
*/

class WizardView extends AbstractView {
	
	private var _className = "WizardView";
	
	//constants
	public static var RT_ORG:String = "Organisation";
	public static var STRING_NULL:String = "string_null_value";
	
	// step views
	public static var WIZARD_SEQ_VIEW:Number = 1;
	public static var WIZARD_ORG_VIEW:Number = 2;
	public static var WIZARD_LESSON_VIEW:Number = 3;
	public static var WIZARD_SUMMARY_VIEW:Number = 4;
	
	// submission modes
	public static var FINISH_MODE:Number = 0;
	public static var START_MODE:Number = 1;
	public static var START_SCH_MODE:Number = 2;
	
	private static var X_BUTTON_OFFSET:Number = 10;
	private static var Y_BUTTON_OFFSET:Number = 15;
	
	public static var LOGO_PATH:String = "www/images/monitor.logo.swf";

	private var _wizardView:WizardView;
	
	// MovieClip for each Step
	private var _wizardSeqView:WizardSequenceView;
	private var _wizardOrgView:WizardOrganisationView;
	private var _wizardLessonView:WizardLessonDetailsView;
	private var _wizardSummaryView:WizardSummaryView;
	
	private var _tm:ThemeManager;
	private var _dictionary:Dictionary;
	
	private var _wizardView_mc:MovieClip;
    private var logo:MovieClip;

	//Dimensions for resizing
    private var xNextOffset:Number;
    private var yNextOffset:Number;
    private var xPrevOffset:Number;
    private var yPrevOffset:Number;
    private var xCancelOffset:Number;
    private var yCancelOffset:Number;
	private var xLogoOffset:Number;
	
	private var lastStageHeight:Number;
	private var header_pnl:MovieClip;		  // top panel base
	private var footer_pnl:MovieClip;
	
	public var panel:MovieClip;       //The underlaying panel base
	
	// common elements
	private var wizTitle_lbl:Label;
	private var wizDesc_txt:TextField;
	
	// buttons
	private var finish_btn:Button;
	private var cancel_btn:Button;
	private var next_btn:Button;
	private var prev_btn:Button;
	private var close_btn:Button;
	private var start_btn:Button;
	
	private var desc_txa:TextArea;
	private var desc_scr:MovieClip;
	
	private var _resultDTO:Object;

	private var _wizardController:WizardController;
	
	private var _workspaceModel:WorkspaceModel;
	private var _workspaceView:WorkspaceView;
	private var _workspaceController:WorkspaceController;

    //Defined so compiler can 'see' events added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	
	/**
	* Constructor
	*/
	function WizardView(){
		_wizardView = this;
		_wizardView_mc = this;
		
		mx.events.EventDispatcher.initialize(this);
		
		_tm = ThemeManager.getInstance();
		_dictionary = Dictionary.getInstance();
		_dictionary.addEventListener('init',Proxy.create(this,setUpLabels));
		
		_resultDTO = new Object();
	}
	
	/**
	* Called to initialise Canvas  . CAlled by the Canvas container
	*/
	public function init(m:Observable,c:Controller){
		super (m, c);
		
		// add views as observers
		var _model:WizardModel = WizardModel(getModel());
		
		_model.addObserver(_wizardSeqView);
		_model.addObserver(_wizardOrgView);
		_model.addObserver(_wizardLessonView);
		_model.addObserver(_wizardSummaryView);
		
		Debugger.log("model: " + _model, Debugger.CRITICAL, "init", "WizardView");
		
		loadLogo();
		
		xNextOffset = panel._width - next_btn._x;
        yNextOffset = panel._height - next_btn._y;
		xPrevOffset = panel._width - prev_btn._x;
        yPrevOffset = panel._height - prev_btn._y;
        xCancelOffset = panel._width - cancel_btn._x;
        yCancelOffset = panel._height - cancel_btn._y;
		xLogoOffset = header_pnl._width - logo._x;

		lastStageHeight = Stage.height;

		MovieClipUtils.doLater(Proxy.create(this,draw)); 
		
    }    
	
	private function loadLogo():Void{
		logo = this.createEmptyMovieClip("logo", this.getNextHighestDepth());
		var ml = new MovieLoader(Config.getInstance().serverUrl+WizardView.LOGO_PATH,null,this,logo);	
	}
	
	
	/**
	 * Recieved update events from the WizardModel. Dispatches to relevent handler depending on update.Type
	 * @usage   
	 * @param   event
	 */
	public function update (o:Observable,infoObj:Object):Void{
		
       var wm:WizardModel = WizardModel(o);
	   
	   _wizardController = getController();

	   switch (infoObj.updateType){
			case 'STEP_CHANGED' :
				updateScreen(infoObj.data.lastStep, infoObj.data.currentStep);
                break;
			case 'SAVED_LC' :
				conclusionStep(infoObj.data, wm);
				break;
			case 'LESSON_STARTED' :
				conclusionStep(infoObj.data, wm);
				break;
			case 'USERS_LOADED' :
				_wizardOrgView.loadLearners(wm.organisation.getLearners(), true);
				_wizardOrgView.loadStaff(wm.organisation.getMonitors(), false);
				_wizardOrgView.selectStaffMember(_root.userID, true);
				_wizardOrgView.enableUsers((resultDTO.selectedJointLessonID == null));
				_wizardController.clearBusy();
				Debugger.log("controller: " + _wizardController, Debugger.CRITICAL, "update", "WizardView");
				break;
			case 'STAFF_RELOAD' :
				_wizardOrgView.loadStaff(wm.organisation.getMonitors(), false);
				break;
			case 'LEARNER_RELOAD' :
				_wizardOrgView.loadLearners(wm.organisation.getLearners(), true);
				break;
			case 'TABCHANGE' :
				_wizardSeqView.showTab(infoObj.tabID);
                break;
			case 'POSITION' :
				setPosition(wm);
                break;
            case 'SIZE' :
			    setSize(wm);
				break;
            default :
                Debugger.log('unknown update type :' + infoObj.updateType,Debugger.CRITICAL,'update','org.lamsfoundation.lams.WizardView');
		}

	}
	
	/**
    * layout visual elements on the canvas on initialisation
    */
	private function draw(){
		var c = undefined;
		var m = WizardModel(getModel());
		
		Debugger.log("model: " + m, Debugger.CRITICAL, "draw", "WizardView");
		
		_wizardSeqView.init(m, c);
		_wizardOrgView.init(m, c);
		_wizardLessonView.init(m, c);
		_wizardSummaryView.init(m, c);
		
		setStyles();
		_wizardLessonView.setScheduleDateRange();
		
		showStep1();
	    
		dispatchEvent({type:'load',target:this});
		
	}
	
	/**
	 * Called by the wizardController after the workspace has loaded 
	 */
	public function setUpContent():Void{
		//register to recive updates form the model
		WorkspaceModel(workspaceView.getModel()).addEventListener('viewUpdate', this);
		
		var controller = getController();
		
		this.addEventListener('okClicked',Delegate.create(controller, controller.okClicked));
		
		next_btn.addEventListener("click",controller);
		prev_btn.addEventListener("click",controller);
		finish_btn.addEventListener("click",controller);
		cancel_btn.addEventListener("click",controller);
		close_btn.addEventListener("click",controller);
		start_btn.addEventListener("click",controller);
		
		_wizardOrgView.setupContent();
		_wizardLessonView.setupContent();
		
		//Set up the treeview
        _wizardSeqView.setUpTreeview();
		
	}
	
	/**
	 * Sets i8n labels for UI elements 
	 * 
	 */
	public function setUpLabels():Void{
		//buttons
		next_btn.label = Dictionary.getValue('next_btn');
		prev_btn.label = Dictionary.getValue('prev_btn');
		cancel_btn.label = Dictionary.getValue('cancel_btn');
		finish_btn.label = Dictionary.getValue('finish_btn');
		close_btn.label = Dictionary.getValue('close_btn');
		start_btn.label = Dictionary.getValue('start_btn');
		
		//labels
		setTitle(Dictionary.getValue('wizardTitle_1_lbl'));
		setDescription(Dictionary.getValue('wizardDesc_1_lbl'));
		
		_wizardLessonView.setupLabels();
		_wizardOrgView.setupLabels();
		
		resizeButtons([cancel_btn, prev_btn, next_btn, close_btn, finish_btn, start_btn, _wizardLessonView.getScheduleBtn()]);
		positionButtons();
	}
	
	/**
	 * Recursive function to set any folder with children to be a branch
	 * TODO: Might / will have to change this behaviour once designs are being returned into the mix
	 * @usage   
	 * @param   node 
	 * @return  
	 */
    private function setBranches(treeview:Tree, node:XMLNode, isOpen:Boolean, iconLinkage:String){
		if(node.hasChildNodes() || node.attributes.isBranch){
			treeview.setIsBranch(node, true);
			if(iconLinkage != null) treeview.setIcon(node, iconLinkage);
			if(isOpen || node.attributes.isOpen){ treeview.setIsOpen(node, true);}
			for (var i = 0; i<node.childNodes.length; i++) {
				var cNode = node.getTreeNodeAt(i);
				setBranches(treeview, cNode, false, iconLinkage);				
			}
		}
	}

	/**
	 * Sets up the inital branch detials
	 * @usage   
	 * @return  
	 */
	public function setUpBranchesInit(treeview:Tree, data:XML, hideRoot:Boolean, isOpen:Boolean, iconLinkage:String){
		Debugger.log('Running...',Debugger.GEN,'setUpBranchesInit','org.lamsfoundation.lams.wizard.WizardView');
		
		// clear tree
		treeview.removeAll();
		
		if(hideRoot){
			treeview.dataProvider = data.firstChild;
		} else {
			treeview.dataProvider = data;
		}
		
		var fNode = treeview.dataProvider.firstChild;
		setBranches(treeview, fNode, isOpen, iconLinkage);
		treeview.refresh();
	}
	
	public function setUpOrgTree(hideRoot:Boolean){
		Debugger.log("setting up org tree: " + hideRoot, Debugger.CRITICAL, "setUpOrgTree", "WizardView");
		_wizardOrgView.setUpOrgTree(hideRoot);
	}
	
	public function setupLocationTabs(){
		_wizardSeqView.setupLocationTabs();
	}
	
	/** Resize the buttons according to the label length */
	private function resizeButtons(btns:Array) {
		this.createTextField("dummylabel", this.getNextHighestDepth(), -1000, -1000, 0, 0); 
		Debugger.log('//////////////////// Resizing Buttons ////////////////////',Debugger.CRITICAL,'resizeButtons','org.lamsfoundation.lams.WizardView');
        
		for(var i=0; i<btns.length; i++) {
			var btn:Button = btns[i];
			var btnLabel:String = btn.label;
			var btn_text = this["dummylabel"];
			
			btn_text.autoSize = true;
			btn_text.text = btnLabel;
			
			var btnWidth:Number = btn_text._width;
			Debugger.log('item: ' + i + ' width: ' + btnWidth + ' label: ' + btnLabel,Debugger.CRITICAL,'resizeButtons','org.lamsfoundation.lams.WizardView');
        
			btn.setSize(btnWidth + 37, 20);
		}
		
		btn_text.removeTextField();
	}
	
	/** Align the buttons correctly on the screen  */
	private function positionButtons(a:Boolean) {
		var button:Button;
		var RET_OFFSET:Number = 0;
		
		// set button x position
		next_btn._x = panel._width - next_btn._width - X_BUTTON_OFFSET;
		start_btn._x = panel._width - start_btn._width - X_BUTTON_OFFSET;
		close_btn._x = panel._width - close_btn._width - X_BUTTON_OFFSET;
		
		finish_btn._x = 10; // panel._width - finish_btn._width - X_BUTTON_OFFSET;
		
		Debugger.log("visible: "+ _wizardLessonView.getScheduleBtn().visible, Debugger.CRITICAL, "positionButtons", "WizardView");
		if(_wizardLessonView.getScheduleBtn().visible) {
			button = _wizardLessonView.getScheduleBtn();
			RET_OFFSET = _wizardLessonView._x;
			button._x = panel._width - button._width - X_BUTTON_OFFSET - RET_OFFSET;
		} else {
			button = start_btn;
		}
			
		prev_btn._x = (a) ? button._x - prev_btn._width - X_BUTTON_OFFSET + RET_OFFSET
							: next_btn._x - prev_btn._width - X_BUTTON_OFFSET;
							
		cancel_btn._x = prev_btn._x - cancel_btn._width - (2*X_BUTTON_OFFSET);
			
	}
	
	/**
    * Set the styles for the Wizard called from init. and themeChanged event handler
    */
    private function setStyles() {
		
		var styleObj = _tm.getStyleObject('button');
		next_btn.setStyle('styleName',styleObj);
		prev_btn.setStyle('styleName',styleObj);
		finish_btn.setStyle('styleName',styleObj);
		cancel_btn.setStyle('styleName',styleObj);
		close_btn.setStyle('styleName',styleObj);
		start_btn.setStyle('styleName',styleObj);
		
		styleObj = _tm.getStyleObject('BGPanel');
		header_pnl.setStyle('styleName',styleObj);
		footer_pnl.setStyle('styleName',styleObj);
		
		styleObj = _tm.getStyleObject('WZPanel');
		panel.setStyle('styleName',styleObj);
		
		styleObj = _tm.getStyleObject('label');
		wizTitle_lbl.setStyle('styleName',styleObj);
		
		_wizardLessonView.setStyles(_tm);
		_wizardOrgView.setStyles(_tm);
		
	}
	
	/**
    * Event fired by StyleManager class to notify listeners that Theme has changed
    * it is up to listeners to then query Style Manager for relevant style info
    */
    public function themeChanged(event:Object){
        if(event.type=='themeChanged') {
            //Theme has changed so update objects to reflect new styles
            setStyles();
        }else {
            Debugger.log('themeChanged event broadcast with an object.type not equal to "themeChanged"',Debugger.CRITICAL,'themeChanged','org.lamsfoundation.lams.WorkspaceDialog');
        }
    }
	
	/**
	 * Recieved update events from the WorkspaceModel. Dispatches to relevent handler depending on update.Type
	 * @usage   
	 * @param   event
	 */
	public function viewUpdate(event:Object):Void{
		var wm:WorkspaceModel = event.target;
	    //set a permenent ref to the model for ease (sorry mvc guru)
		
		switch (event.updateType){
			case 'REFRESH_TREE' :
                _wizardSeqView.refreshTree(wm);
                break;
			case 'UPDATE_CHILD_FOLDER' :
				_wizardSeqView.updateChildFolderBranches(event.data,wm);
				_wizardSeqView.openFolder(event.data, wm);
			case 'UPDATE_CHILD_FOLDER_NOOPEN' :
				_wizardSeqView.updateChildFolderBranches(event.data,wm);
				break;
			case 'ITEM_SELECTED' :
				_wizardLessonView.itemSelected(event.data,wm);
				break;
			case 'OPEN_FOLDER' :
				_wizardSeqView.openFolder(event.data, wm);
				break;
			case 'CLOSE_FOLDER' :
				_wizardSeqView.closeFolder(event.data, wm);
				break;
			case 'REFRESH_FOLDER' :
				_wizardSeqView.refreshFolder(event.data, wm);
				break;
			case 'SET_UP_BRANCHES_INIT' :
				setUpBranchesInit();
				break;
            default :
                Debugger.log('unknown update type :' + event.updateType,Debugger.GEN,'viewUpdate','org.lamsfoundation.lams.ws.WorkspaceDialog');
		
		}
	}
	
	// SCREEN UPDATES
	private function updateScreen(cl_step:Number, sh_step:Number){
		
		switch(cl_step){
			case WIZARD_SEQ_VIEW:
				_wizardSeqView.show(false);
				break;
			case WIZARD_ORG_VIEW: 
				_wizardOrgView.show(false);
				break;
			case WIZARD_LESSON_VIEW:
				positionButtons(false);
				_wizardLessonView.clear();
				break;
			case WIZARD_SUMMARY_VIEW:
				_wizardSummaryView.show(false);
				break;
			default:
				break;
		}
		
		switch(sh_step){
			case WIZARD_SEQ_VIEW:
				showStep1();
				break;
			case WIZARD_ORG_VIEW: 
				showStep2();
				break;
			case WIZARD_LESSON_VIEW:
				positionButtons(true);
				showStep3();
				break;
			case WIZARD_SUMMARY_VIEW:
				showFinish();
				break;
			default:
				break;
		}
		
		
	}
	
	// VALIDATE STEPS
	public function validateStep(wm:WizardModel):Boolean{
		Debugger.log('validating step: ' + wm.stepID, Debugger.CRITICAL, "validateStep", "WizardView");
		switch(wm.stepID){
			case WIZARD_SEQ_VIEW:
				return _wizardSeqView.validate(wm);
				break;
			case WIZARD_ORG_VIEW: 
				return _wizardOrgView.validate(wm);
				break;
			case WIZARD_LESSON_VIEW:
				return _wizardLessonView.validate(wm);
				break;
			default:
				return false;
				break;
		}
		
	}
	
	private function showStep1():Void{
		
		WizardModel(getModel()).getWizard().getOrganisations(_root.courseID, _root.classID);
		
		setTitle(Dictionary.getValue('wizardTitle_1_lbl'));
		setDescription(Dictionary.getValue('wizardDesc_2_lbl'));
		
		_wizardSeqView.show(true);
		_wizardOrgView.show(false);
		_wizardLessonView.clear();
		_wizardSummaryView.show(false);
		
		showButtons([true, true, true, false, false, false]);
		
		prev_btn.enabled = false;
		next_btn.enabled = true;
		
		Debugger.log("prev btn e: " + prev_btn.enabled, Debugger.CRITICAL, "showStep1", "WizardView");
		
	}
	
	private function showStep2():Void{
		setTitle(Dictionary.getValue('wizardTitle_3_lbl'));
		setDescription(Dictionary.getValue('wizardDesc_3_lbl'));
		
		// enable prev button after Step 1
		prev_btn.enabled = true;
		
		if(!resultDTO.selectedLearners && !resultDTO.selectedStaff){
			WizardModel(getModel()).getWizard().getOrganisations(_root.courseID, _root.classID);
		}
		
		_wizardOrgView.show(true);
		showButtons([true, true, true, false, false]);
		
		
	}
	
	private function showStep3():Void{
		
		setTitle(Dictionary.getValue('wizardTitle_4_lbl'));
		setDescription(Dictionary.getValue('wizardDesc_4_lbl'));
		
		showButtons([false, true, true, true, false]);
		_wizardLessonView.show();
	}
	
	private function showFinish():Void{
		
		setTitle(Dictionary.getValue('wizardTitle_x_lbl', [resultDTO.resourceTitle]));
		setDescription("");
		
		_wizardSummaryView.showConfirmMessage(resultDTO.mode);
		showButtons([false, false, false, false, false, true]);
	}
	
	private function conclusionStep(mode:Number, wm:WizardModel):Void{
		switch(mode){
			case FINISH_MODE :
				updateScreen(wm.stepID, wm.stepID+1);
				break;
			case START_MODE :
				wm.getWizard().startLesson(false, wm.lessonID);
				break;
			case START_SCH_MODE :
				wm.getWizard().startLesson(true, wm.lessonID, wm.resultDTO.scheduleDateTime);
				break;
			default:
				break;
		}
	}
	
	public function disableButtons():Void{
		next_btn.enabled = false;
		prev_btn.enabled = false;
		cancel_btn.enabled = false;
		finish_btn.enabled = false;
		start_btn.enabled = false;
	}
	
	public function enableButtons(wm:WizardModel):Void{
		next_btn.enabled = true;
		prev_btn.enabled = true;
		cancel_btn.enabled = true;
		finish_btn.enabled = true;
		start_btn.enabled = true;
		
		if(wm.stepID == WIZARD_SEQ_VIEW)
			prev_btn.enabled = false;
	}
	
	public function showButtons(v:Array) {
		next_btn.visible = (v[0] != null) ? v[0] : false;
		prev_btn.visible = (v[1] != null)  ? v[1] : false;
		cancel_btn.visible = (v[2] != null) ? v[2] : false;
		finish_btn.visible = (v[3] != null) ? v[3] : false;
		start_btn.visible = (v[4] != null) ? v[4] : false;
		close_btn.visible = (v[5] != null) ? v[5] : false;
	}
	
	/*
	* Clear Method to clear movies from scrollpane
	* 
	*/
	public static function clearScp(array:Array):Array{
		if(array != null){
			for (var i=0; i <array.length; i++){
				array[i].removeMovieClip();
			}
		}
		
		array = new Array();
		return array;
	}
	
	/**
    * Sets the size of the canvas on stage, called from update
    */
	private function setSize(wm:WizardModel):Void{
        var s:Object = wm.getSize();
		
		header_pnl.setSize(s.w, header_pnl._height);
		panel.setSize(s.w, s.h-header_pnl._height);
		
		// move buttons
        next_btn.move(panel._width-xNextOffset,panel._height-yNextOffset);
		prev_btn.move(panel._width-xPrevOffset,panel._height-yPrevOffset);
		close_btn.move(panel._width-xCancelOffset,panel._height-yCancelOffset);
        cancel_btn.move(panel._width-xCancelOffset,panel._height-yCancelOffset);
				
		finish_btn.move(10, panel._height-yNextOffset);
		
		// move logo
		logo._x = header_pnl._width-xLogoOffset;
		
		// calculate height change
		var dHeight:Number = Stage.height - lastStageHeight;
		
		lastStageHeight = Stage.height;
		
		_wizardOrgView.setSize(dHeight);
		_wizardSeqView.setSize(dHeight);
		_wizardLessonView.setSize(dHeight);
		
	}
	
	/**
    * Sets the position of the canvas on stage, called from update
    * @param cm Canvas model object 
    */
	private function setPosition(wm:WizardModel):Void{
        var p:Object = wm.getPosition();
		
		this._x = p.x;
        this._y = p.y;
	}
	
	public function get workspaceView():WorkspaceView{
		return _wizardSeqView.workspaceView;
	}
	
	public function set workspaceView(a:WorkspaceView){
		_wizardSeqView.workspaceView = a;
	}
	
	public function get resultDTO():Object{
		return _resultDTO;
	}
	
	public function setTitle(title:String){
		wizTitle_lbl.text = "<b>" + title + "</b>";
	}
	
	public function setDescription(desc:String){
		wizDesc_txt.wordWrap = true;
		wizDesc_txt.text = desc;
	}
	
	/**
	 * Overrides method in abstract view to ensure cortect type of controller is returned
	 * @usage   
	 * @return  CanvasController
	 */
	public function getController():WizardController{
		var c:Controller = super.getController();
		return WizardController(c);
	}
	
	/*
    * Returns the default controller for this view.
    */
    public function defaultController (model:Observable):Controller {
        return new WizardController(model);
    }

	
}