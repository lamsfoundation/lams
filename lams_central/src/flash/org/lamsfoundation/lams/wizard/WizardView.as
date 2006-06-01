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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
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
import org.lamsfoundation.lams.monitoring.User;
import org.lamsfoundation.lams.monitoring.Orgnanisation;
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.mvc.*
import org.lamsfoundation.lams.common.ws.*

import it.sephiroth.TreeDnd


/**
* Wizard view
* Relects changes in the WizardModel
*/

class WizardView extends AbstractView {
	
	private var _className = "WizardView";
	
	//constants
	public var RT_ORG:String = "Organisation";
	public var STRING_NULL:String = "string_null_value"
	public static var USERS_X:Number = 10;
	public static var USER_OFFSET:Number = 20;
	public var SUMMERY_X:Number = 20;
	public var SUMMERY_Y:Number = 20;
	
	// submission modes
	public static var FINISH_MODE:Number = 0;
	public static var START_MODE:Number = 1;
	public static var START_SCH_MODE:Number = 2;

	private var _wizardView:WizardView;
	private var _tm:ThemeManager;
	private var _dictionary:Dictionary;
	//private var _workspace:Workspace;
	
	private var _wizardView_mc:MovieClip;
    private var logo:MovieClip;

	private var org_treeview:Tree;              //Treeview for navigation through workspace folder structure
	
	// step 1 UI elements
	private var location_treeview:Tree;

	// step 2 UI elements
	private var title_lbl:Label;
	private var resourceTitle_txi:TextInput;
	private var desc_lbl:Label;
    private var resourceDesc_txa:TextArea;
	
	// step 3 UI elements
	private var _staffList:Array;
	private var _learnerList:Array;
	private var _learner_mc:MovieClip;
	private var _staff_mc:MovieClip;
	private var staff_scp:MovieClip;		// staff/teachers container
	private var staff_lbl:Label;
	private var learner_scp:MovieClip;		// learners container
	private var learner_lbl:Label;
	
	// step 4 UI elements
	private var schedule_cb:CheckBox;
	private var start_btn:Button;
	private var schedule_time:MovieClip;
	private var summery_lbl:Label;
	private var summery_scp:MovieClip;
	private var _summery_mc:MovieClip;
	private var _summeryList:Array;
	private var scheduleDate_dt:DateField;

	// conclusion UI elements
	private var confirmMsg_txt:TextField;

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
	
	private var panel:MovieClip;       //The underlaying panel base
	
	// common elements
	private var wizTitle_lbl:Label;
	private var wizDesc_txt:TextField;
	private var finish_btn:Button;
	private var cancel_btn:Button;
	private var next_btn:Button;
	private var prev_btn:Button;
	private var close_btn:Button;
	
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
	//public var menu:ContextMenu;

	
	/**
	* Constructor
	*/
	function WizardView(){
		_wizardView = this;
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
			case 'USERS_LOADED' :
				loadLearners(wm.organisation.getLearners());
				loadStaff(wm.organisation.getStaff());
				_wizardController.clearBusy();
				break;
			case 'SAVED_LC' :
				conclusionStep(infoObj.data, wm);
				break;
			case 'LESSON_STARTED' :
				conclusionStep(infoObj.data, wm);
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
	 * Recieved update events from the WorkspaceModel. Dispatches to relevent handler depending on update.Type
	 * @usage   
	 * @param   event
	 */
	public function viewUpdate(event:Object):Void{
		trace('receiving view update event...');
		var wm:WorkspaceModel = event.target;
	   //set a permenent ref to the model for ease (sorry mvc guru)
	   _workspaceModel = wm;
	   
		switch (event.updateType){
			case 'REFRESH_TREE' :
                refreshTree(wm);
                break;
			case 'UPDATE_CHILD_FOLDER' :
				updateChildFolderBranches(event.data,wm);
			case 'ITEM_SELECTED' :
				itemSelected(event.data,wm);
				break;
			case 'OPEN_FOLDER' :
				openFolder(event.data, wm);
				break;
			case 'REFRESH_FOLDER' :
				refreshFolder(event.data, wm);
				break;
			case 'SET_UP_BRANCHES_INIT' :
				setUpBranchesInit();
				break;
            default :
                Debugger.log('unknown update type :' + event.updateType,Debugger.GEN,'viewUpdate','org.lamsfoundation.lams.ws.WorkspaceDialog');
		
		}
	}
	
	/**
    * layout visual elements on the canvas on initialisation
    */
	private function draw(){
		setStyles();
		showStep1();
	    dispatchEvent({type:'load',target:this});
		
	}
	
	/**
	 * Called by the wizardController after the workspace has loaded 
	 */
	public function setUpContent():Void{
		trace('setting up content');
		//register to recive updates form the model
		WorkspaceModel(workspaceView.getModel()).addEventListener('viewUpdate',this);
		var controller = getController();
		this.addEventListener('okClicked',Delegate.create(controller,controller.okClicked));
		next_btn.addEventListener('click',Delegate.create(this, next));
		prev_btn.addEventListener('click',Delegate.create(this, prev));
		finish_btn.addEventListener('click',Delegate.create(this, finish));
		cancel_btn.addEventListener('click',Delegate.create(this, cancel));
		close_btn.addEventListener('click', Delegate.create(this, close));
		start_btn.addEventListener('click', Delegate.create(this, start));
		schedule_cb.addEventListener("click", Delegate.create(this, scheduleChange));

		//Set up the treeview
        setUpTreeview();
		//itemSelected(location_treeview.selectedNode, WorkspaceModel(workspaceView.getModel()));
	
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
		title_lbl.text = Dictionary.getValue('title_lbl');
		desc_lbl.text = Dictionary.getValue('desc_lbl');
		staff_lbl.text = Dictionary.getValue('staff_lbl');
		learner_lbl.text = Dictionary.getValue('learner_lbl');
		summery_lbl.text = Dictionary.getValue('summery_lbl');
		
		schedule_cb.label = Dictionary.getValue('schedule_cb_lbl');
		
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
		
		styleObj = _tm.getStyleObject('WZPanel');
		panel.setStyle('styleName',styleObj);
		
		styleObj = _tm.getStyleObject('label');
		wizTitle_lbl.setStyle('styleName',styleObj);
		title_lbl.setStyle('styleName',styleObj);
		desc_lbl.setStyle('styleName',styleObj);
		staff_lbl.setStyle('styleName',styleObj);
		learner_lbl.setStyle('styleName',styleObj);
		summery_lbl.setStyle('styleName',styleObj);
		staff_lbl.setStyle('styleName',styleObj);
		schedule_cb.setStyle('styleName', styleObj);
		
		//styleObj = _tm.getStyleObject('treeview');
        //org_treeview.setStyle('styleName',styleObj);
        //location_treeview.setStyle('styleName',styleObj);
		
		
		styleObj = _tm.getStyleObject('textarea');
		resourceDesc_txa.setStyle('styleName',styleObj);
		resourceTitle_txi.setStyle('styleName',styleObj);
		
		styleObj = _tm.getStyleObject('scrollpane');
		staff_scp.setStyle('styleName',styleObj);
		learner_scp.setStyle('styleName',styleObj);
		summery_scp.setStyle('styleName',styleObj);
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
	
	private function itemSelected(newSelectedNode:XMLNode, wm:WorkspaceModel){
		//update the UI with the new info
		var nodeData = newSelectedNode.attributes.data;
		trace('selected node data: ' + nodeData);
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
	 * Recursive function to set any folder with children to be a branch
	 * TODO: Might / will have to change this behaviour once designs are being returned into the mix
	 * @usage   
	 * @param   node 
	 * @return  
	 */
    private function setBranches(treeview:Tree, node:XMLNode, isOpen:Boolean){
		if(node.hasChildNodes() || node.attributes.isBranch){
			treeview.setIsBranch(node, true);
			if(isOpen){ treeview.setIsOpen(node, true);}
			for (var i = 0; i<node.childNodes.length; i++) {
				var cNode = node.getTreeNodeAt(i);
				setBranches(cNode);				
			}
		}
	}
	

	/**
	 * Sets up the inital branch detials
	 * @usage   
	 * @return  
	 */
	private function setUpBranchesInit(treeview:Tree, data:XML, hideRoot:Boolean, isOpen:Boolean){
		Debugger.log('Running...',Debugger.GEN,'setUpBranchesInit','org.lamsfoundation.lams.wizard.WizardView');
		//get the 1st child
		
		trace('data value:' + data);
		trace('hide root:' + hideRoot);
		// clear tree
		treeview.removeAll();
		
		if(hideRoot){
			treeview.dataProvider = data.firstChild;
		} else {
			treeview.dataProvider = data;
		}
		var fNode = treeview.dataProvider.firstChild;
		trace(fNode);
		setBranches(treeview, fNode, isOpen);
		treeview.refresh();
	}
	
	
	/**
	 * Sets up the treeview with whatever datya is in the treeDP
	 * TODO - extend this to make it recurse all the way down the tree
	 * @usage   
	 * @return  
	 */
	private function setUpTreeview(){
			
		setUpBranchesInit(location_treeview, WorkspaceModel(workspaceView.getModel()).treeDP, false, false);
		_workspaceController = _workspaceView.getController();
		location_treeview.addEventListener("nodeOpen", Delegate.create(_workspaceController, _workspaceController.onTreeNodeOpen));
		location_treeview.addEventListener("nodeClose", Delegate.create(_workspaceController, _workspaceController.onTreeNodeClose));
		location_treeview.addEventListener("change", Delegate.create(_workspaceController, _workspaceController.onTreeNodeChange));
		
		var wsNode:XMLNode = location_treeview.firstVisibleNode;
		location_treeview.setIsOpen(wsNode, true);
		_workspaceController.forceNodeOpen(wsNode);
    }
	
	/**
	 * Sets up the treeview with whatever data is in the treeDP
	 * TODO - extend this to make it recurse all the way down the tree
	 * @usage   
	 * @return  
	 */
	public function setUpOrgTree(hideRoot:Boolean){
			
		//Debugger.log('_workspaceView:'+_workspaceView,Debugger.GEN,'setUpTreeview','org.lamsfoundation.lams.common.ws.WorkspaceDialog');
		
		setUpBranchesInit(org_treeview, WizardModel(getModel()).treeDP, hideRoot, true);
		
		org_treeview.addEventListener("nodeOpen", Delegate.create(_wizardController, _wizardController.onTreeNodeOpen));
		org_treeview.addEventListener("nodeClose", Delegate.create(_wizardController, _wizardController.onTreeNodeClose));
		org_treeview.addEventListener("change", Delegate.create(_wizardController, _wizardController.onTreeNodeChange));

		org_treeview.selectedNode = org_treeview.firstVisibleNode;
		getController().selectTreeNode(org_treeview.selectedNode);

		//org_dnd.addEventListener("drag_complete", Delegate.create(_lessonManagerController, _lessonManagerController.onDragComplete));
		
    }
	
	/**
	 * called witht he result when a child folder is opened..
	 * updates the tree branch satus, then refreshes.
	 * @usage   
	 * @param   changedNode 
	 * @param   wm          
	 * @return  
	 */
	private function updateChildFolderBranches(changedNode:XMLNode,wm:WorkspaceModel){
		 Debugger.log('updateChildFolder....:' ,Debugger.GEN,'updateChildFolder','org.lamsfoundation.lams.ws.WorkspaceDialog');
		 //we have to set the new nodes to be branches, if they are branches
		if(changedNode.attributes.isBranch){
			location_treeview.setIsBranch(changedNode,true);
			//do its kids
			for(var i=0; i<changedNode.childNodes.length; i++){
				var cNode:XMLNode = changedNode.getTreeNodeAt(i);
				if(cNode.attributes.isBranch){
					location_treeview.setIsBranch(cNode,true);
				}
			}
		}
		
		 openFolder(changedNode);
	}
	
	private function refreshTree(){
		 Debugger.log('Refreshing tree....:' ,Debugger.GEN,'refreshTree','org.lamsfoundation.lams.ws.WorkspaceDialog');
		 location_treeview.refresh();

	}
	
	/**
	 * Just opens the fodler node - DOES NOT FIRE EVENT - so is used after updatting the child folder
	 * @usage   
	 * @param   nodeToOpen 
	 * @param   wm         
	 * @return  
	 */
	private function openFolder(nodeToOpen:XMLNode, wm:WorkspaceModel){
		Debugger.log('openFolder:'+nodeToOpen ,Debugger.GEN,'openFolder','org.lamsfoundation.lams.ws.WorkspaceDialog');
		//open the node
		location_treeview.setIsOpen(nodeToOpen,true);
		refreshTree();
	
	}
	/**
	 * Closes folder, then sends openEvent to controller
	 * @usage   
	 * @param   nodeToOpen 
	 * @param   wm         
	 * @return  
	 */
	private function refreshFolder(nodeToOpen:XMLNode, wm:WorkspaceModel){
		Debugger.log('refreshFolder:'+nodeToOpen ,Debugger.GEN,'refreshFolder','org.lamsfoundation.lams.ws.WorkspaceDialog');
		//close the node
		location_treeview.setIsOpen(nodeToOpen,false);		
		//we are gonna need to fire the event manually for some stupid reason the tree is not firing it.
		//dispatchEvent({type:'nodeOpen',target:treeview,node:nodeToOpen});
		_workspaceController = _workspaceView.getController();
		_workspaceController.onTreeNodeOpen({type:'nodeOpen',target:location_treeview,node:nodeToOpen});
	}

	// BUTTON EVENT HANDLER methods

	/**
    * Called by the NEXT button
	*
	*/
    private function next(evt:Object){
        trace('NEXT CLICKED');
		_global.breakpoint();
		var wm:WizardModel = WizardModel(getModel());
		if(validateStep(wm)){
			wm.stepID++;
			trace('new step ID: ' + wm.stepID);
		}
    }
	
	private function prev(evt:Object){
		trace('PREV CLICKED');
		var wm:WizardModel = WizardModel(getModel());
		wm.stepID--;
		trace('new step ID: ' + wm.stepID);
	}
	
	private function finish(evt:Object){
		trace('FINISH CLICKED');
		disableButtons();
		resultDTO.mode = FINISH_MODE;
		_wizardController.initializeLesson(resultDTO);
	}
	
	private function start(evt:Object){
		trace('START CLICKED');
		
		if(schedule_cb.selected){
			resultDTO.scheduleDateTime = getScheduleDateTime(scheduleDate_dt.selectedDate, schedule_time.f_returnTime());
			
			if(resultDTO.scheduleDateTime != null){
				trace(resultDTO.scheduleDateTime);
				resultDTO.mode = START_SCH_MODE;
			} else {
				LFMessage.showMessageAlert(Dictionary.getValue('al_validation_schstart'), null, null);
				return;
			}
		} else {
			resultDTO.mode = START_MODE;
		}
		
		disableButtons();
		_wizardController.initializeLesson(resultDTO);
	}
	
	private function cancel(evt:Object){
		// close window
		trace('CANCEL CLICKED');
		getURL('javascript:window.close()');
	}
	
	private function close(evt:Object){
		trace('CLOSE WINDOW');
		getURL('javascript:window.close()');
	}
	
	private function scheduleChange(evt:Object){
		trace(evt.target);
		trace('schedule clicked : ' + schedule_cb.selected);
		var isSelected:Boolean = schedule_cb.selected;
		if(isSelected){
			schedule_time.f_enableTimeSelect(true);
			scheduleDate_dt.enabled = true;
		} else {
			schedule_time.f_enableTimeSelect(false);
			scheduleDate_dt.enabled = false;
		}
	}
	
	// SCREEN UPDATES
	
	private function updateScreen(cl_step:Number, sh_step:Number){
		
		switch(cl_step){
			case 1:
				clearStep1();
				break;
			case 2: 
				clearStep2();
				break;
			case 3: 
				clearStep3();
				break;
			case 4:
				clearStep4();
				break;
			case 5:
				clearFinish();
				break;
			default:
				trace('unknown step');
		}
		
		switch(sh_step){
			case 1:
				showStep1();
				break;
			case 2: 
				showStep2();
				break;
			case 3: 
				showStep3();
				break;
			case 4:
				showStep4();
				break;
			case 5:
				showFinish();
				break;
			default:
				trace('unknown step');
		}
		
		
	}
	
	// VALIDATE STEPS
	
	private function validateStep(wm:WizardModel):Boolean{
		switch(wm.stepID){
			case 1:
				return validateStep1(wm);
				break;
			case 2: 
				return validateStep2(wm);
				break;
			case 3: 
				return validateStep3(wm);
				break;
			case 4:
				return validateStep4(wm);
				break;
			default:
				return false;
				break;
		}
		
		
	}
	
	private function showStep1():Void{
		trace('showing step 1');
		setTitle(Dictionary.getValue('wizardTitle_1_lbl'));
		setDescription(Dictionary.getValue('wizardDesc_2_lbl'));
		location_treeview.visible = true;
		
		finish_btn.visible = false;
		prev_btn.enabled = false;
		next_btn.enabled = true;
		close_btn.visible = false;
		
		// hide step 2 (Startup)
		title_lbl.visible = false;
		resourceTitle_txi.visible = false;
		desc_lbl.visible = false;
		resourceDesc_txa.visible = false;
		
		// hide step 3 (Startup)
		org_treeview.visible = false;
		
		staff_lbl.visible = false;
		staff_scp.visible = false;
		learner_lbl.visible = false;
		learner_scp.visible = false;
		
		// hide step 4 (Startup)
		start_btn.visible = false;
		summery_scp.visible = false;
		summery_lbl.visible = false;
		schedule_cb.visible = false;
		schedule_time._visible = false;
		scheduleDate_dt.visible = false;
		
		// hide final screen elements
		confirmMsg_txt.visible = false;
	}
	
	private function clearStep1():Void{
		location_treeview.visible = false;
	}
	
	private function validateStep1(wm:WizardModel):Boolean{
		var snode = location_treeview.selectedNode;
		if (snode.attributes.data.resourceType==wm.RT_FOLDER){
			// set result DTO - folder selected cannot continue
			doWorkspaceDispatch(false);
			
			// show folder selected warning
			trace('folder selected.. need to select LD');
			LFMessage.showMessageAlert(Dictionary.getValue('al_validation_msg1'), null, null);
			return false;
		} else if(snode.attributes.data.resourceType==wm.RT_LD){
			// set result DTO - lesson selected
			trace('selection valid');
			doWorkspaceDispatch(true);
			
			
			return true;
		} else {
			// show general warning
			trace('nothing selected!!!');
			LFMessage.showMessageAlert(Dictionary.getValue('al_validation_msg1'), null, null);
			return false;
		}
	}
	
	private function showStep2():Void{
		trace('showing step 2');
		
		setTitle(Dictionary.getValue('wizardTitle_2_lbl'));
		setDescription(Dictionary.getValue('wizardDesc_2_lbl'));
		// enable prev button after Step 1
		prev_btn.enabled = true;
		
		// display Step 2
		title_lbl.visible = true;
		resourceTitle_txi.visible = true;
		desc_lbl.visible = true;
		resourceDesc_txa.visible = true;
		
		// check for NULL value
		if(resourceDesc_txa.text == STRING_NULL){
			resourceDesc_txa.text = "";
		}
		
	}
	
	private function clearStep2():Void{
		// display Step 2
		title_lbl.visible = false;
		resourceTitle_txi.visible = false;
		desc_lbl.visible = false;
		resourceDesc_txa.visible = false;
	}
	
	private function validateStep2(wm:WizardModel):Boolean{
		var valid:Boolean = true;
		if(resourceTitle_txi.text == ""){
			trace('title is empty must contain value');
			valid = false;
		} 
		
		if(valid){
			if(resourceTitle_txi.text != ""){resultDTO.resourceTitle = resourceTitle_txi.text;}
			resultDTO.resourceDescription = resourceDesc_txa.text;
		} else {
			LFMessage.showMessageAlert(Dictionary.getValue('al_validation_msg2'), null, null);
		}
		return valid;
	}
	
	private function showStep3():Void{
		trace('showing step 3');
		
		setTitle(Dictionary.getValue('wizardTitle_3_lbl'));
		setDescription(Dictionary.getValue('wizardDesc_3_lbl'));
		
		if(!resultDTO.selectedLearners && !resultDTO.selectedStaff){
			WizardModel(getModel()).getWizard().getOrganisations(_root.courseID, _root.classID);
		}
		
		org_treeview.visible = true;
		
		staff_lbl.visible = true;
		staff_scp.visible = true;
		learner_lbl.visible = true;
		learner_scp.visible = true;
	}
	
	private function clearStep3():Void{
		org_treeview.visible = false;
		
		staff_lbl.visible = false;
		staff_scp.visible = false;
		learner_lbl.visible = false;
		learner_scp.visible = false;
	}
	private function validateStep3(wm:WizardModel):Boolean{
		_global.breakpoint();
		
		var valid:Boolean = true;
		var snode = org_treeview.selectedNode;
		var pnode = snode.parentNode;
		var selectedLearners:Array = new Array();
		var selectedStaff:Array = new Array();
			
		if(snode == null){
			trace('no course/class selected');
			LFMessage.showMessageAlert(Dictionary.getValue('al_validation_msg3_1'), null, null);
			return false;
		} else {
			// add selected users to dto
			trace('learners')
			
			
			for(var i=0; i<learnerList.length;i++){
				if(learnerList[i].user_cb.selected){
					trace('select item: ' + learnerList[i].fullName.text);
					selectedLearners.push(learnerList[i].data.userID);
				}
			}
			
	
			trace('staff')
			for(var i=0; i<staffList.length;i++){
				if(staffList[i].user_cb.selected){
					trace('select item: ' + staffList[i].fullName.text);
					selectedStaff.push(staffList[i].data.userID);
				}
			}
			
			if(selectedLearners.length <= 0){
				trace('no learners selected');
				valid = false;
			}
			
			if(selectedStaff.length <= 0){
				trace('no staff selected');
				valid = false;
			}
			
		}
		
		if(valid){
			var selectedOrgID:Number = Number(snode.attributes.data.organisationID);
			resultDTO.organisationID = selectedOrgID;
			
			if(snode.attributes.isBranch){
				resultDTO.courseName = snode.attributes.data.name;
				resultDTO.className = "";
			} else {
				resultDTO.className = snode.attributes.data.name;
				resultDTO.courseName = pnode.attributes.data.name;
			}
			
			resultDTO.selectedStaff = selectedStaff;
			resultDTO.selectedLearners = selectedLearners;
			var orgName:String = snode.attributes.data.name;
			resultDTO.staffGroupName = orgName + 'staff';
			resultDTO.learnersGroupName = orgName + 'learners';
			
			trace('selected org ID is: ' + selectedOrgID);
			
		}else{
			LFMessage.showMessageAlert(Dictionary.getValue('al_validation_msg3_2'), null, null);
		}
		
		return valid;
	}
	
	
	private function showStep4():Void{
		
		
		setTitle(Dictionary.getValue('wizardTitle_4_lbl'));
		setDescription(Dictionary.getValue('wizardDesc_4_lbl'));
		
		writeSummery();
		
		summery_lbl.visible = true;
		schedule_cb.visible = true;
		start_btn.visible = true;
		if(schedule_cb.selected){
			schedule_time.f_enableTimeSelect(true);
			scheduleDate_dt.enabled = true;
		} else {
			schedule_time.f_enableTimeSelect(false);
			scheduleDate_dt.enabled = false;
		}
		schedule_time._visible = true;
		scheduleDate_dt.visible = true;
		next_btn.visible = false;
		finish_btn.visible = true;
	}
	
	private function writeSummery():Void{
		if(_summery_mc != null){
			_summery_mc.removeMovieClip();
		}
		_summery_mc = this.attachMovie('wizardSummery', 'wizardSummery', 0, {_x:SUMMERY_X+panel._x, _y:SUMMERY_Y+panel._y});
		_summery_mc.design_txt.text = resultDTO.resourceName;
		_summery_mc.title_txt.text = resultDTO.resourceTitle;
		_summery_mc.desc_txt.text = resultDTO.resourceDescription;
		_summery_mc.coursename_txt.text = resultDTO.courseName;
		_summery_mc.classname_txt.text = resultDTO.className;
		_summery_mc.staff_txt.text = String(resultDTO.selectedStaff.length) + '/' + staffList.length;
		_summery_mc.learners_txt.text = String(resultDTO.selectedLearners.length) + '/' + learnerList.length;
		//_summery_mc.redraw(true);
		
		trace('text height ' + _summery_mc.desc_txt.textHeight);
		trace(_summery_mc.desc_scr);
		if(_summery_mc.desc_txt.textHeight <= 69){
			trace('making in visible');
			_summery_mc.desc_scr._visible = false;
		}
		
	}
	
	
	
	private function clearStep4():Void{
		_summery_mc.removeMovieClip();
		
		summery_lbl.visible = false;
		schedule_cb.visible = false;
		start_btn.visible = false;
		schedule_time._visible = false;
		scheduleDate_dt.visible = false;
		next_btn.visible = true;
		finish_btn.visible= false;
	}
	
	private function validateStep4(wm:WizardModel):Boolean{
		return true;
	}
	
	private function showFinish():Void{
		
		setTitle(Dictionary.getValue('wizardTitle_x_lbl', [resultDTO.resourceTitle]));
		setDescription("");
		showConfirmMessage(resultDTO.mode);
		next_btn.visible = false;
		prev_btn.visible = false;
		cancel_btn.visible = false;
		finish_btn.visible = false;
		close_btn.visible = true;
	}
	
	private function clearFinish():Void{
		
	}
	
	private function conclusionStep(mode:Number, wm:WizardModel):Void{
		switch(mode){
			case FINISH_MODE : 
				trace('step id (finish mode) ' + wm.stepID);
				updateScreen(wm.stepID, wm.stepID+1);
				break;
			case START_MODE :
				trace('start mode');
				wm.getWizard().startLesson(false, wm.lessonID);
				break;
			case START_SCH_MODE :
				trace('schedule start mode');
				wm.getWizard().startLesson(true, wm.lessonID, wm.resultDTO.scheduleDateTime);
				break;
			default:
				trace('unknown mode');
		}
	}
	
	private function disableButtons():Void{
		next_btn.enabled = false;
		prev_btn.enabled = false;
		cancel_btn.enabled = false;
		finish_btn.enabled = false;
		start_btn.enabled = false;
	}
	
	/**
	 * Dispatches an event - picked up by the canvas in authoring
	 * sends paramter containing:
	 * _resultDTO.selectedResourceID 
	 * _resultDTO.targetWorkspaceFolderID
	 * 	_resultDTO.resourceName 
		_resultDTO.resourceDescription 
	 * @usage   
	 * @param   useResourceID //if its true then we will send the resorceID of teh item selected in the tree - usually this means we are overwriting something
	 * @return  
	 */
	public function doWorkspaceDispatch(useResourceID:Boolean){
		//ObjectUtils.printObject();
		var snode = location_treeview.selectedNode;
		
		if(useResourceID){
			//its an LD
			resultDTO.selectedResourceID = Number(snode.attributes.data.resourceID);
			resultDTO.targetWorkspaceFolderID = Number(snode.attributes.data.workspaceFolderID);
			resultDTO.resourceName = snode.attributes.data.name;
		}else{
			//its a folder
			resultDTO.selectedResourceID  = null;
			resultDTO.targetWorkspaceFolderID = Number(snode.attributes.data.resourceID);
			resultDTO.resourceName = snode.attributes.data.name;
		}

       dispatchEvent({type:'okClicked',target:this});
		
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
	 * Load learners into scrollpane
	 * @param   users Users to load
	 */
	
	public function loadLearners(users:Array):Void{
		trace('loading Learners...');
		_learnerList = WizardView.clearScp(_learnerList);
		_learner_mc = learner_scp.content;
		var _selected:Boolean = true;
		trace('list length: ' + users.length);
		for(var i=0; i<users.length; i++){
			var user:User = User(users[i]);
			
			_learnerList[i] = this._learner_mc.attachMovie('staff_learner_dataRow', 'staff_learner_dataRow' + i, this._learner_mc.getNextHighestDepth());
			_learnerList[i].fullName.text = user.getFirstName();
			_learnerList[i]._x = USERS_X;
			_learnerList[i]._y = USER_OFFSET * i;
			_learnerList[i].data = user.getDTO();
			var listItem:MovieClip = MovieClip(_learnerList[i]);
			
			listItem.attachMovie('CheckBox', 'user_cb', listItem.getNextHighestDepth(), {_x:0, _y:3, selected:_selected})
			trace('new row: ' + _learnerList[i]);
			trace('loading: user ' + user.getFirstName() + ' ' + user.getLastName());
			
		}
		learner_scp.redraw(true);
	}
	
	/**
	* Load staff into scrollpane
	* @param 	users Users to load
	*/
	public function loadStaff(users:Array):Void{
		trace('loading Staff....');
		trace('list length: ' + users.length);
		_staffList = WizardView.clearScp(_staffList);
		_staff_mc = staff_scp.content;
		
		for(var i=0; i<users.length; i++){
			var user:User = User(users[i]);
			
			_staffList[i] = this._staff_mc.attachMovie('staff_learner_dataRow', 'staff_learner_dataRow' + i, this._staff_mc.getNextHighestDepth());
			_staffList[i].fullName.text = user.getFirstName();
			_staffList[i]._x = USERS_X;
			_staffList[i]._y = USER_OFFSET * i;
			_staffList[i].data = user.getDTO();
			var listItem:MovieClip = MovieClip(_staffList[i]);
			listItem.attachMovie('CheckBox', 'user_cb', listItem.getNextHighestDepth(), {_x:0, _y:3, selected:true})
			
			trace('loading: user ' + user.getFirstName() + ' ' + user.getLastName());
			
		}
		staff_scp.redraw(true);
	}
	
	public function getScheduleDateTime(date:Date, timeStr:String):String{
		var bs:String = "%2F";		// backslash char
		var dayStr:String;
		var monthStr:String;
		
		if(date==null){
			return null;
		}
		
		trace('output time: ' + timeStr);
		var day = date.getDate();
		if(day<10){
			dayStr=String(0)+day;
		} else {
			dayStr=day.toString();
		}
		
		var month = date.getMonth()+1;
		if(month<10){
			monthStr=String(0)+month;
		} else {
			monthStr = month.toString();
		}
		
		var dateStr = dayStr + bs + monthStr + bs + date.getFullYear();
		trace('selected date: ' + dateStr);
		return dateStr + '+' + timeStr;
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
		finish_btn.move(panel._width-xNextOffset,panel._height-yNextOffset);
		close_btn.move(panel._width-xCancelOffset,panel._height-yCancelOffset);
        cancel_btn.move(panel._width-xCancelOffset,panel._height-yCancelOffset);
				
		// move logo
		logo._x = header_pnl._width-xLogoOffset;
		
		// calculate height change
		var dHeight:Number = Stage.height - lastStageHeight;
		
		trace('last stage height' + lastStageHeight);
		
		lastStageHeight = Stage.height;
		trace('new height change: ' + dHeight);
			
		org_treeview.setSize(org_treeview.width, Number(org_treeview.height + dHeight));
		location_treeview.setSize(location_treeview.width, Number(location_treeview.height + dHeight));
		
		resourceDesc_txa.setSize(resourceDesc_txa.width, Number(resourceDesc_txa.height + dHeight));
		
		//staff_scp.setSize(staff_scp._width, staff_scp._height + dHeight);
		learner_scp.setSize(learner_scp._width, learner_scp._height + dHeight);
		
		
	}
	
	 /**
    * Sets the position of the canvas on stage, called from update
    * @param cm Canvas model object 
    */
	private function setPosition(wm:WizardModel):Void{
        var p:Object = wm.getPosition();
		trace("X pos set in Model is: "+p.x+" and Y pos set in Model is "+p.y)
        this._x = p.x;
        this._y = p.y;
	}
	
	public function get workspaceView():WorkspaceView{
		return _workspaceView;
	}
	
	public function set workspaceView(a:WorkspaceView){
		_workspaceView = a;
	}
	
	public function get resultDTO():Object{
		return _resultDTO;
	}
	
	public function get learnerList():Array{
		return _learnerList;
	}
	
	public function get staffList():Array{
		return _staffList;
	}
	
	public function setTitle(title:String){
		wizTitle_lbl.text = "<b>" + title + "</b>";
	}
	
	public function setDescription(desc:String){
		wizDesc_txt.text = desc;
	}
	
	public function showConfirmMessage(mode:Number){
		var msg:String = "";
		var lessonName:String = "<b>" + resultDTO.resourceTitle +"</b>";
		switch(mode){
			case FINISH_MODE : 
				msg = Dictionary.getValue('confirmMsg_1_txt', [lessonName]);
				break;
			case START_MODE :
				msg = Dictionary.getValue('confirmMsg_3_txt', [lessonName]);
				break;
			case START_SCH_MODE :
				msg = Dictionary.getValue('confirmMsg_2_txt', [lessonName, unescape(resultDTO.scheduleDateTime)]);
				break;
			default:
				trace('unknown mode');
		}
		confirmMsg_txt.html = true;
		confirmMsg_txt.htmlText = msg;
		confirmMsg_txt._width = confirmMsg_txt.textWidth + 5;
		confirmMsg_txt._x = panel._x + (panel._width/2) - (confirmMsg_txt._width/2);
		confirmMsg_txt._y = panel._y + (panel._height/4);
		
		confirmMsg_txt.visible = true;
		
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