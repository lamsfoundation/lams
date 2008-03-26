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
class org.lamsfoundation.lams.wizard.steps.WizardOrganisationView extends AbstractView {
	
	public static var USERS_X:Number = 10;
	public static var USER_OFFSET:Number = 20;
	
	private var _className = "WizardOrganisationView";
	
	private var org_treeview:Tree;
	
	private var _staffList:Array;
	private var _learnerList:Array;
	private var _learner_mc:MovieClip;
	private var _staff_mc:MovieClip;
	private var staff_scp:MovieClip;		// staff/teachers container
	private var staff_lbl:Label;
	private var learner_scp:MovieClip;		// learners container
	private var learner_lbl:Label;
	private var staff_selAll_cb:CheckBox;
	private var learner_selAll_cb:CheckBox;
	
	private var _wizardController:WizardController;
	
	//Defined so compiler can 'see' events added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	
	function WizardOrganisationView(){
		mx.events.EventDispatcher.initialize(this);
		
	}
	
	public function init(m:Observable,c:Controller) {
		super(m, c)
	}
	
	public function setupContent():Void {
		staff_selAll_cb.addEventListener("click", Delegate.create(this, toogleStaffSelection));
		learner_selAll_cb.addEventListener("click", Delegate.create(this, toogleLearnerSelection));
	}
	
	public function setupLabels():Void {
		staff_lbl.text = Dictionary.getValue('staff_lbl');
		learner_lbl.text = Dictionary.getValue('learner_lbl');
		staff_selAll_cb.label = Dictionary.getValue('wizard_selAll_cb_lbl');
		learner_selAll_cb.label = Dictionary.getValue('wizard_selAll_cb_lbl');
	}
	
	public function setStyles(_tm:ThemeManager) {
		var styleObj = _tm.getStyleObject('label');
		
		staff_lbl.setStyle('styleName',styleObj);
		learner_lbl.setStyle('styleName',styleObj);

		styleObj = _tm.getStyleObject('scrollpane');
		staff_scp.setStyle('styleName',styleObj);
		learner_scp.setStyle('styleName',styleObj);
		
	}
	
	public function show(v:Boolean):Void {
		org_treeview.visible = v;
		
		staff_lbl.visible = v;
		learner_lbl.visible = v;
		
		staff_scp.visible = v;
		learner_scp.visible = v;
		staff_selAll_cb.visible = v;
		learner_selAll_cb.visible = v;
		
		if(_parent.resultDTO.selectedJointLessonID != null) {
			staff_selAll_cb.enabled = false;
			learner_selAll_cb.enabled = false;
			
			enableUsers(false);
			
		} else {
			staff_selAll_cb.enabled = true;
			learner_selAll_cb.enabled = true;
			
			enableUsers(true);
		}
		
	}
	
	public function validate(wm:WizardModel):Boolean{
		var valid:Boolean = true;
		var snode = org_treeview.selectedNode;
		var pnode = snode.parentNode;
		var selectedLearners:Array = new Array();
		var selectedStaff:Array = new Array();
			
		if(snode == null){
			LFMessage.showMessageAlert(Dictionary.getValue('al_validation_msg3_1'), null, null);
			return false;
		} else {
			// add selected users to dto
			
			for(var i=0; i<learnerList.length;i++){
				if(learnerList[i].user_cb.selected){
					selectedLearners.push(learnerList[i].data.userID);
				}
			}
			
	
			for(var i=0; i<staffList.length;i++){
				if(staffList[i].user_cb.selected){
					selectedStaff.push(staffList[i].data.userID);
				}
			}
			
			if(selectedLearners.length <= 0){
				valid = false;
			}
			
			if(selectedStaff.length <= 0){
				valid = false;
			}
			
		}
		
		if(valid){
			var selectedOrgID:Number = Number(snode.attributes.data.organisationID);
			_parent.resultDTO.organisationID = selectedOrgID;
			
			if(snode.attributes.data.organisationTypeId == 2){
				_parent.resultDTO.courseName = snode.attributes.data.name;
				_parent.resultDTO.className = "";
			} else {
				_parent.resultDTO.className = snode.attributes.data.name;
				_parent.resultDTO.courseName = pnode.attributes.data.name;
			}
			
			_parent.resultDTO.selectedStaff = selectedStaff;
			_parent.resultDTO.selectedLearners = selectedLearners;
			_parent.resultDTO.staffListTotal = staffList.length;
			_parent.resultDTO.learnersListTotal = learnerList.length;
			
			var orgName:String = snode.attributes.data.name;
			_parent.resultDTO.staffGroupName = Dictionary.getValue('staff_group_name', [orgName]);
			_parent.resultDTO.learnersGroupName = Dictionary.getValue('learners_group_name', [orgName]);
			
			var courseOrg:XMLNode = wm.getOrganisationResource(wm.getWizard().RT_ORG+'_'+_root.courseID);
			_parent.resultDTO.canOfferJointLessons = courseOrg.attributes.data.canOfferJointLessons;
			_parent.resultDTO.canJoinJointLessons = courseOrg.attributes.data.canJoinJointLessons;
			
		}else{
			LFMessage.showMessageAlert(Dictionary.getValue('al_validation_msg3_2'), null, null);
		}
		
		return valid;
	}
	
	/**
	 * Load learners into scrollpane
	 * @param   users Users to load
	 */
	
	public function loadLearners(users:Array, _selected:Boolean):Void{
		_learnerList = WizardView.clearScp(_learnerList);
		_learner_mc = learner_scp.content;
		
		for(var i=0; i<users.length; i++){
			var user:User = User(users[i]);
			
			_learnerList[i] = this._learner_mc.attachMovie('staff_learner_dataRow', 'staff_learner_dataRow' + i, this._learner_mc.getNextHighestDepth());
			_learnerList[i].fullName.text = user.getFullName();
			_learnerList[i]._x = USERS_X;
			_learnerList[i]._y = USER_OFFSET * i;
			_learnerList[i].data = user.getDTO();
			var listItem:MovieClip = MovieClip(_learnerList[i]);
			
			listItem.attachMovie('CheckBox', 'user_cb', listItem.getNextHighestDepth(), {_x:0, _y:3, selected:_selected});
		}
		
		learner_selAll_cb.selected = _selected;
		
		learner_scp.redraw(true);
	}
	
	public function enableUsers(e:Boolean):Void {
		for(var i=0; i < _learnerList.length; i++) {
			if(e && !_learnerList[i].user_cb.enabled) _learnerList[i].user_cb.enabled = e;
			else if(!e && _learnerList[i].user_cb.enabled) _learnerList[i].user_cb.enabled = e;
		}
		
		for(var i=0; i < _staffList.length; i++) {
			_staffList[i].user_cb.enabled = e;
		}
	}
	
	/**
	* Load staff into scrollpane
	* @param 	users Users to load
	*/
	public function loadStaff(users:Array, _selected:Boolean):Void{
		_staffList = WizardView.clearScp(_staffList);
		_staff_mc = staff_scp.content;
		
		for(var i=0; i<users.length; i++){
			var user:User = User(users[i]);
			
			_staffList[i] = this._staff_mc.attachMovie('staff_learner_dataRow', 'staff_learner_dataRow' + i, this._staff_mc.getNextHighestDepth());
			_staffList[i].fullName.text = user.getFullName();
			_staffList[i]._x = USERS_X;
			_staffList[i]._y = USER_OFFSET * i;
			_staffList[i].data = user.getDTO();
			
			var listItem:MovieClip = MovieClip(_staffList[i]);
			
			var enableCheckBox:Boolean =  (_parent.resultDTO.selectedJointLessonID == null) ? true : false;
			Debugger.log("cb selected:" + enableCheckBox, Debugger.CRITICAL, "loadStaff", "WizardOrganisationView");
			listItem.attachMovie('CheckBox', 'user_cb', listItem.getNextHighestDepth(), {_x:0, _y:3, selected:_selected});
		}
		
		staff_selAll_cb.selected = _selected;
		
		staff_scp.redraw(true);
	}
	
	/**
	* Select/deselect checkbox of a chosen staff member
	* @param 	userID userID of staff member
	* @param _selected true if we want to have the checkbox ticked, false otherwise
	*/
	public function selectStaffMember(userID:Number, _selected:Boolean):Void {
		for (var i=0; i<_staffList.length; i++) {
			if (_staffList[i].data.userID == userID) {
				var listItem:MovieClip = MovieClip(_staffList[i]);
				listItem.user_cb.selected = _selected;
				break;
			}
		}
	}
	
	/**
	 * Sets up the treeview with whatever data is in the treeDP
	 * TODO - extend this to make it recurse all the way down the tree
	 * @usage   
	 * @return  
	 */
	public function setUpOrgTree(hideRoot:Boolean){
		var controller = getController();
		_parent.setUpBranchesInit(org_treeview, WizardModel(_parent.getModel()).treeDP, hideRoot, true);
		
		org_treeview.addEventListener("nodeOpen", Delegate.create(controller, controller.onTreeNodeOpen));
		org_treeview.addEventListener("nodeClose", Delegate.create(controller, controller.onTreeNodeClose));
		org_treeview.addEventListener("change", Delegate.create(controller, controller.onTreeNodeChange));

		org_treeview.selectedNode = org_treeview.firstVisibleNode;
		
		Debugger.log("setting firstNode: " + org_treeview.selectedNode + " controller: " + controller, Debugger.CRITICAL, "setUpOrgTree", "WizardOrgansationView");
		controller.selectTreeNode(org_treeview.selectedNode);

    }
	
	public function get learnerList():Array{
		return _learnerList;
	}
	
	public function get staffList():Array{
		return _staffList;
	}

	public function setSize(dHeight:Number) {
		org_treeview.setSize(org_treeview.width, Number(org_treeview.height + dHeight));
		
		//staff_scp.setSize(staff_scp._width, staff_scp._height + dHeight);
		learner_scp.setSize(learner_scp._width, learner_scp._height + dHeight);
		
	}
	
	private function toogleStaffSelection(evt:Object) {
		Debugger.log("Toogle Staff Selection", Debugger.GEN, "toogleStaffSelection", "WizardView");
		var target:CheckBox = CheckBox(evt.target);
		var wm:WizardModel = WizardModel(getModel());
		loadStaff(wm.organisation.getMonitors(), target.selected);
	}
	
	private function toogleLearnerSelection(evt:Object) {
		Debugger.log("Toogle Staff Selection", Debugger.GEN, "toogleStaffSelection", "WizardView");
		var target:CheckBox = CheckBox(evt.target);
		var wm:WizardModel = WizardModel(getModel());
		
		loadLearners(wm.organisation.getLearners(), target.selected);
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
}