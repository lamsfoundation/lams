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

import mx.controls.*
import mx.utils.*
import mx.managers.*
import mx.events.*

/**
 *
 * @author Mitchell Seaton
 * @version 2.0.3
 **/
class org.lamsfoundation.lams.wizard.steps.WizardLessonDetailsView  extends AbstractView {
	
	private var title_lbl:Label;
	private var resourceTitle_txi:TextInput;
	private var desc_lbl:Label;
    private var resourceDesc_txa:TextArea;
	
	public static var SUMMERY_X:Number = 11;
	public static var SUMMERY_Y:Number = 140;
	public static var SUMMERY_W:Number = 500;
	public static var SUMMERY_H:Number = 20;
	public static var SUMMERY_OFFSET:Number = 2;
	
	private var schedule_cb:CheckBox;
	private var learner_expp_cb:CheckBox;
	private var learner_enpres_cb:CheckBox;
	private var learner_enim_cb:CheckBox;
	private var start_btn:Button;
	private var schedule_time:MovieClip;
	private var summery_lbl:Label;
	private var date_lbl:Label;
	private var time_lbl:Label;
	private var summery_scp:MovieClip;
	private var _summery_mc:MovieClip;
	private var _summeryList:Array;
	private var scheduleDate_dt:DateField;
	private var summery_lbl_arr:Array;

	private var schedule_btn:Button;
	
	//Defined so compiler can 'see' events added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	
	function WizardLessonDetailsView(){
		mx.events.EventDispatcher.initialize(this);
		
	}
	
	public function init(m:Observable,c:Controller) {
		super(m, c)
	}
	
	public function setupContent():Void {
		schedule_btn.addEventListener('click', Delegate.create(this, scheduleNow));
		schedule_cb.addEventListener("click", Delegate.create(this, scheduleChange));
		learner_expp_cb.addEventListener("click", Delegate.create(this, toogleExpPortfolio));
		learner_enpres_cb.addEventListener("click", Delegate.create(this, toggleEnableIm));
		
		learner_expp_cb.selected = true;
	}
	
	public function setupLabels():Void {
		title_lbl.text = Dictionary.getValue('title_lbl');
		desc_lbl.text = Dictionary.getValue('desc_lbl');
		summery_lbl.text = Dictionary.getValue('summery_lbl');
		date_lbl.text = Dictionary.getValue('date_lbl');
		time_lbl.text = Dictionary.getValue('time_lbl');
		
		schedule_btn.label = Dictionary.getValue('schedule_cb_lbl');
		
		schedule_cb.label = Dictionary.getValue('schedule_cb_lbl');
		learner_expp_cb.label = Dictionary.getValue('wizard_learner_expp_cb_lbl');
		learner_enpres_cb.label = Dictionary.getValue('wizard_learner_enpres_cb_lbl');
		learner_enim_cb.label = Dictionary.getValue('wizard_learner_enim_cb_lbl');
	}
	
	public function setStyles(_tm:ThemeManager):Void {
		var styleObj = _tm.getStyleObject('button');
		schedule_btn.setStyle('styleName',styleObj);
		
		styleObj = _tm.getStyleObject('label');
		title_lbl.setStyle('styleName',styleObj);
		desc_lbl.setStyle('styleName',styleObj);
		schedule_cb.setStyle('styleName', styleObj);
		learner_expp_cb.setStyle('styleName', styleObj);
		date_lbl.setStyle('styleName', styleObj);
		time_lbl.setStyle('styleName', styleObj);
		summery_lbl.setStyle('styleName',styleObj);
		
		styleObj = _tm.getStyleObject('textarea');
		resourceDesc_txa.setStyle('styleName',styleObj);
		resourceTitle_txi.setStyle('styleName',styleObj);
		
		styleObj = _tm.getStyleObject('scrollpane');
		summery_scp.setStyle('styleName',styleObj);
	}
	
	public function show():Void {
		title_lbl.visible = true;
		resourceTitle_txi.visible = true;
		desc_lbl.visible = true;
		resourceDesc_txa.visible = true;
		
		date_lbl.visible = true;
		time_lbl.visible = true;
		start_btn.visible = true;
		summery_scp.visible = true;
		summery_lbl.visible = true;
		learner_expp_cb.visible = true;
		learner_enpres_cb.visible = true;
		learner_enim_cb.visible = false;
		learner_enim_cb.enabled = false;
		schedule_cb.visible = true;
		schedule_time._visible = true;
		scheduleDate_dt.visible = true;
		
		// check for NULL value
		if(resourceDesc_txa.text == WizardView.STRING_NULL){
			resourceDesc_txa.text = "";
		}
		
		writeSummery(ThemeManager.getInstance());
		
		if(schedule_cb.selected){
			schedule_time.f_enableTimeSelect(true);
			scheduleDate_dt.enabled = true;
			setScheduleDateRange();
			schedule_btn.visible = true;
			
			_parent.showButtons([false, true, true, false, false]);
			
		} else {
			schedule_time.f_enableTimeSelect(false);
			scheduleDate_dt.enabled = false;
			schedule_btn.visible = false;
			
			_parent.showButtons([false, true, true, true, true]);
		}
		
		learner_expp_cb.selected = true;
		_parent.positionButtons(true);
		
	}
	
	public function clear():Void {
		if(summery_lbl_arr.length > 0) {
			for(var i=0; i<summery_lbl_arr.length; i++) {
				summery_lbl_arr[i].removeMovieClip();
			}
		}
		
		title_lbl.visible = false;
		resourceTitle_txi.visible = false;
		desc_lbl.visible = false;
		resourceDesc_txa.visible = false;
		
		date_lbl.visible = false;
		time_lbl.visible = false;
		start_btn.visible = false;
		schedule_btn.visible = false;
		summery_scp.visible = false;
		summery_lbl.visible = false;
		learner_expp_cb.visible = false;
		learner_enpres_cb.visible = false;
		learner_enim_cb.visible = false;
		schedule_cb.visible = false;
		schedule_time._visible = false;
		scheduleDate_dt.visible = false;
	}
	
	public function validate(wm:WizardModel):Boolean{
		
		var valid:Boolean = true;
		if(resourceTitle_txi.text == ""){
			valid = false;
		} 
		
		if(valid){
			if(resourceTitle_txi.text != ""){_parent.resultDTO.resourceTitle = resourceTitle_txi.text;}
			_parent.resultDTO.resourceDescription = resourceDesc_txa.text;
		} else {
			LFMessage.showMessageAlert(Dictionary.getValue('al_validation_msg2'), null, null);
		}
		
		if(schedule_cb.selected && (scheduleDate_dt.selectedDate == null || scheduleDate_dt.selectedDate == undefined)){
			LFMessage.showMessageAlert(Dictionary.getValue('al_validation_schstart'), null, null);
			valid = false;
		}else {
			
		}
		
		_parent.resultDTO.enablePresence = learner_enpres_cb.selected;
		_parent.resultDTO.enableIm = learner_enim_cb.selected;
		_parent.resultDTO.learnerExpPortfolio = learner_expp_cb.selected;
		
		return valid;
	}
	
	public function setSize(dHeight:Number):Void {
		resourceDesc_txa.setSize(resourceDesc_txa.width, Number(resourceDesc_txa.height + dHeight));
	}
	
	public function setScheduleDateRange():Void{
		
		var mydate = new Date();
		var year = mydate.getFullYear();
		var month = mydate.getMonth();
		var date = mydate.getDate();
		
		Debugger.log('schedule date range starts from :'+date + "/" +month+ "/" +year,Debugger.CRITICAL,'setScheduleDateRange','WizardLessonDetailsView');
		scheduleDate_dt.selectableRange = {rangeStart: new Date(year, month, date)};
	}
	
	private function writeSummery(_tm:ThemeManager):Void{
		
		if(summery_lbl_arr.length > 0) {
			for(var i=0; i<summery_lbl_arr.length; i++) {
				summery_lbl_arr[i].removeMovieClip();
			}
		}
		
		summery_lbl_arr = new Array();
		var summery_lbl;
		
		// design label
		summery_lbl_arr.push(this.attachMovie('Label', 'wizardSummery_lbl_design', this.getNextHighestDepth(), {_x:SUMMERY_X - this._x+_parent.panel._x, _y:SUMMERY_Y+_parent.panel._y - this._y, _width: SUMMERY_W, _height: SUMMERY_H, styleName: _tm.getStyleObject('label'), text: (_parent.resultDTO.selectedJointLessonID != null) ? Dictionary.getValue('summery_design_lbl') + ' ' + _parent.resultDTO.selectedJointNode.attributes.data.extLessonName : Dictionary.getValue('summery_design_lbl') + ' ' + _parent.resultDTO.resourceName}));
		summery_lbl = this['wizardSummery_lbl_design'];
		
		// course label
		summery_lbl_arr.push(this.attachMovie('Label', 'wizardSummery_lbl_course', this.getNextHighestDepth(), {_x:summery_lbl._x, _y:summery_lbl._y + summery_lbl._height + SUMMERY_OFFSET, _width: SUMMERY_W, _height: SUMMERY_H, styleName: _tm.getStyleObject('label'), text:Dictionary.getValue('summery_course_lbl') + ' ' + _parent.resultDTO.courseName}));
		summery_lbl = this['wizardSummery_lbl_course'];
		
		// class label
		summery_lbl_arr.push(this.attachMovie('Label', 'wizardSummery_lbl_class', this.getNextHighestDepth(), {_x:SUMMERY_X+_parent.panel._x - this._x, _y:summery_lbl._y + summery_lbl._height + SUMMERY_OFFSET , _width: SUMMERY_W, _height: SUMMERY_H, styleName: _tm.getStyleObject('label'), text:Dictionary.getValue('summery_class_lbl') + ' ' + _parent.resultDTO.className}));
		summery_lbl = this['wizardSummery_lbl_class'];
		
		// staff label
		summery_lbl_arr.push(this.attachMovie('Label', 'wizardSummery_lbl_staff', this.getNextHighestDepth(), {_x:SUMMERY_X+_parent.panel._x - this._x, _y:summery_lbl._y + summery_lbl._height + SUMMERY_OFFSET , _width: SUMMERY_W, _height: SUMMERY_H, styleName: _tm.getStyleObject('label'), text:Dictionary.getValue('summery_staff_lbl') + ' ' + String(_parent.resultDTO.selectedStaff.length) + '/' + _parent.resultDTO.staffListTotal}));
		summery_lbl = this['wizardSummery_lbl_staff'];
			
		// learners label
		summery_lbl_arr.push(this.attachMovie('Label', 'wizardSummery_lbl_learners', this.getNextHighestDepth(), {_x:SUMMERY_X+_parent.panel._x - this._x, _y:summery_lbl._y + summery_lbl._height+ SUMMERY_OFFSET, _width: SUMMERY_W, _height: SUMMERY_H, styleName: _tm.getStyleObject('label'), text:Dictionary.getValue('summery_learners_lbl') + ' ' + String(_parent.resultDTO.selectedLearners.length) + '/' + _parent.resultDTO.learnersListTotal}));
	}
	
	public function getScheduleDateTime(date:Date, timeStr:String):Object{
		var bs:String = "%2F";		// backslash char
		var dayStr:String;
		var monthStr:String;
		var mydate = new Date();
		var dtObj = new Object();
		if(date==null){
			return null;
		}
		
		var day = date.getDate();
		dayStr=day.toString();
		
		var month = date.getMonth()+1;
		monthStr = month.toString();
		
		var dateStr = dayStr + bs + monthStr + bs + date.getFullYear();
		Debugger.log('schedule time to starts :'+timeStr,Debugger.CRITICAL,'getScheduleDateTime','org.lamsfoundation.lams.WizardView');
		if (day == mydate.getDate() && month == mydate.getMonth()+1 && date.getFullYear() == mydate.getFullYear()){
			dtObj.validTime = validateTime();
		}else {
			dtObj.validTime = true
		}
		dtObj.dateTime = dateStr + '+' + timeStr;
		
		return dtObj;
	}
	
	private function validateTime():Boolean{
		var mydate = new Date();
		var checkHours:Number;
		var hours = mydate.getHours();
		var minutes = mydate.getMinutes();
		var selectedHours = Number(schedule_time.tHour.text);
		var selectedMinutes = Number(schedule_time.tMinute.text);
		if (schedule_time.tMeridian.selectedItem.data == "AM"){
			checkHours = 0
		}
		if (schedule_time.tMeridian.selectedItem.data == "PM"){
			if (Number (selectedHours) == 12){
				checkHours = 0
			}else {
				checkHours = 12
			}
		}
		if (hours > (Number(selectedHours+checkHours))){
			return false;
		}else if (hours == Number(selectedHours+checkHours)){
			if (minutes > selectedMinutes){
				return false;
			}else {
				return true;
			}
		}else {
			return true;
		}
	}
	
	private function scheduleNow(evt:Object){
		var wm:WizardModel = WizardModel(getModel());
		if(_parent.validateStep(wm)){
			var schDT = getScheduleDateTime(scheduleDate_dt.selectedDate, schedule_time.f_returnTime());
			_parent.resultDTO.scheduleDateTime = schDT.dateTime
					
			if(_parent.resultDTO.scheduleDateTime == null || _parent.resultDTO.scheduleDateTime == undefined){
				LFMessage.showMessageAlert(Dictionary.getValue('al_validation_schstart'), null, null);
				return;
				
			} else {
				if (!schDT.validTime){
					LFMessage.showMessageAlert(Dictionary.getValue('al_validation_schtime'), null, null);
					return;
				}else {
					_parent.resultDTO.mode = WizardView.START_SCH_MODE;
				}
			}
			
			_parent.disableButtons();
			getController().initializeLesson(_parent.resultDTO);
		}
	}
	
	private function scheduleChange(evt:Object){
		var isSelected:Boolean = schedule_cb.selected;
		
		if(isSelected){
			schedule_time.f_enableTimeSelect(true);
			schedule_btn.enabled = true;
			schedule_btn.visible = true;
			scheduleDate_dt.enabled = true;
			
			_parent.showButtons([false, true, true, false, false]);
			_parent.positionButtons(true);
		
		} else {
			schedule_time.f_enableTimeSelect(false);
			scheduleDate_dt.enabled = false;
			
			schedule_btn.visible = false;
			
			_parent.showButtons([false, true, true, true, true]);
			_parent.positionButtons(true);
		}
	}

	private function toggleEnableIm(evt:Object) {
		Debugger.log("Enable instant messaging checkbox", Debugger.GEN, "toggleEnableIm", "WizardLessonDetailsView");
		if (learner_enpres_cb.selected){
			learner_enim_cb.enabled = true;
		}
		else{
			learner_enim_cb.selected = false;
			learner_enim_cb.enabled = false;
		}
	}
	
	private function toogleExpPortfolio(evt:Object) {
		Debugger.log("Toogle Staff Selection", Debugger.GEN, "toogleStaffSelection", "WizardLessonDetailsView");
		var target:CheckBox = CheckBox(evt.target);
		_parent.resultDTO.learnerExpPortfolio = target.selected;
	}
	
	public function itemSelected(newSelectedNode:XMLNode, wm:WorkspaceModel){
		//update the UI with the new info
		var nodeData = newSelectedNode.attributes.data;
		if(nodeData.resourceType == wm.RT_FOLDER){
			resourceTitle_txi.text = "";
			resourceDesc_txa.text = "";
		}else{
			if(nodeData.name == null){
				resourceTitle_txi.text = "";
			} else {
				resourceTitle_txi.text = nodeData.name;
			}
			
			if(nodeData.description == null){
				resourceDesc_txa.text = "";
			} else {
				resourceDesc_txa.text = nodeData.description;
			}
		}
		
	}
	
	/**
	 * Overrides method in abstract view to ensure cortect type of controller is returned
	 * @usage   
	 * @return  CanvasController
	 */
	public function getController():WizardController{
		var c:Controller = _parent.getController();
		return WizardController(c);
	}
	
	/*
    * Returns the default controller for this view.
    */
    public function defaultController (model:Observable):Controller {
        return new WizardController(model);
    }
	
	public function getScheduleBtn():Button {
		return schedule_btn;
	}
	
}