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

import it.sephiroth.TreeDnd
import org.lamsfoundation.lams.common.ws.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.common.*
import org.lamsfoundation.lams.monitoring.*
import org.lamsfoundation.lams.monitoring.mv.*
import org.lamsfoundation.lams.monitoring.mv.tabviews.*

/*
* Lesson Manager Dialog window for selecting an organisation class
* @author  DI
*/
class LessonManagerDialog extends MovieClip implements Dialog{
	
	
	public var RT_ORG:String = "Organisation";
	public static var USERS_X:Number = 10;
	public static var USER_OFFSET:Number = 20;
	private static var USERS_LOAD_CHECK_INTERVAL:Number = 50;
	private static var USERS_LOAD_CHECK_TIMEOUT_COUNT:Number = 200;
	
	//References to components + clips 
    private var _container:MovieClip;  //The container window that holds the dialog
	
    private var ok_btn:Button;         //OK+Cancel buttons
    private var cancel_btn:Button;
    
	// headings (labels)
	private var organisation_lbl:Label;
	private var staff_lbl:Label;
	private var learners_lbl:Label;
	
	private var panel:MovieClip;       //The underlaying panel base
	
	private var treeview:Tree;              //Treeview for navigation through workspace folder structure
	private var org_dnd:TreeDnd;
	
	private var staff_scp:MovieClip;		// staff/teachers container
	private var learner_scp:MovieClip;		// learners container
	private var _learner_mc:MovieClip;
	private var _staff_mc:MovieClip;
	
    private var fm:FocusManager;            //Reference to focus manager
    private var themeManager:ThemeManager;  //Theme manager
	
	//Dimensions for resizing
    private var xOkOffset:Number;
    private var yOkOffset:Number;
    private var xCancelOffset:Number;
    private var yCancelOffset:Number;
	private var user_chk:CheckBox;
	
	private var _lessonTabView:LessonTabView;
	private var _monitorModel:MonitorModel;
	private var _monitorView:MonitorView;
	private var _monitorController:MonitorController;

	private var _learnerList:Array;
	private var _staffList:Array;

	private var _resultDTO:Object;
	private var _selectedOrgId:Number;	// selected organisation
	
	private var _usersLoadCheckCount = 0;				// instance counter for number of times we have checked to see if users are loaded
	private var _UsersLoadCheckIntervalID:Number;         //Interval ID for periodic check on UILoad status
    private var _learnersLoaded:Boolean;                     //UI Loading status
	private var _staffLoaded:Boolean;
	
	//These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	
	 /**
    * constructor
    */
    function LessonManagerDialog(){
		//Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);
		
		_resultDTO = new Object();
        _learnersLoaded = false;
		_staffLoaded = false;
        
		//Create a clip that will wait a frame before dispatching init to give components time to setup
        MovieClipUtils.doLater(Proxy.create(this,init));
	}
	
	/**
    * Called a frame after movie attached to allow components to initialise
    */
    private function init():Void{
		
		//set the reference to the StyleManager
        themeManager = ThemeManager.getInstance();
        
        setStyles();
		setLabels();
		
		// disable on startup until learners/staff loaded
		enableButtons(false);
		
        //EVENTS
        //Add event listeners for ok, cancel and close buttons
        ok_btn.addEventListener('click',Delegate.create(this, ok));
        cancel_btn.addEventListener('click',Delegate.create(this, cancel));
        
		//Assign Click (close button) and resize handlers
        _container.addEventListener('click',this);
        _container.addEventListener('size',this);
        
        //work out offsets from bottom RHS of panel
        xOkOffset = panel._width - ok_btn._x;
        yOkOffset = panel._height - ok_btn._y;
        xCancelOffset = panel._width - cancel_btn._x;
        yCancelOffset = panel._height - cancel_btn._y;
		
		treeview = org_dnd.getTree();
		
		//fire event to say we have loaded
		_container.contentLoaded();
    }
	
		/**
	 * Called by the worspaceView after the content has loaded
	 * @usage   
	 * @return  
	 */
	public function setUpContent():Void{
		
		Debugger.log('_monitorView:'+_monitorView,Debugger.GEN,'setUpContent','org.lamsfoundation.lams.LessonManagerDialog');
		
		//get a ref to the controller and kkep it here to listen for events:
		_monitorController = _monitorView.getController();
		_monitorModel = MonitorModel(_monitorController.getModel());
		
		 //Add event listeners for ok, cancel and close buttons
        ok_btn.addEventListener('onPress',Delegate.create(this, ok));
        cancel_btn.addEventListener('onPress',Delegate.create(this, cancel));
		
		getOrganisations(_monitorModel.getSequence().organisationID, null);

	}

	/**
    * Event fired by StyleManager class to notify listeners that Theme has changed
    * it is up to listeners to then query Style Manager for relevant style info
    */
    public function themeChanged(event:Object):Void{
        if(event.type=='themeChanged') {
            //Theme has changed so update objects to reflect new styles
            setStyles();
        }else {
            Debugger.log('themeChanged event broadcast with an object.type not equal to "themeChanged"',Debugger.CRITICAL,'themeChanged','org.lamsfoundation.lams.WorkspaceDialog');
        }
    }

	/**
    * Called by the cancel button 
    */
    private function cancel(){
        //close parent window
        _container.deletePopUp();
    }
    
    /**
    * Called by the OK button 
    */
    private function ok(){
        Debugger.log('OK Clicked',Debugger.GEN,'ok','org.lamsfoundation.lams.LessonManagerDialog');
        
		_global.breakpoint();
		
		var valid:Boolean = true;
		var snode = treeview.selectedNode;
		var pnode = snode.parentNode;
		var selectedLearners:Array = new Array();
		var selectedStaff:Array = new Array();
			
		if(snode == null){
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
			
			resultDTO.staffGroupName = Dictionary.getValue('staff_group_name', [orgName]);
			resultDTO.learnersGroupName = Dictionary.getValue('learners_group_name', [orgName]);
			
			doOrganisationDispatch();
			
		}else{
		}
		
		
    }
    
	public function doOrganisationDispatch(){
		
		_monitorController.editLessonClass(resultDTO);
	   
        closeThisDialogue();
		
	}
	
	public function closeThisDialogue(){
		 _container.deletePopUp();
	}
	
    /**
    * If an alert was spawned by this dialog this method is called when it's closed
    */
    private function alertClosed(evt:Object){
        //_container.deletePopUp();
    }
    
	/**
	 * 
	 * @usage   
	 * @param   newworkspaceView 
	 * @return  
	 */
	public function set monitorView (newMonitorView:MonitorView):Void {
		_monitorView = newMonitorView;
	}
	
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get monitorView ():MonitorView {
		return _monitorView;
	}
	
    /**
    * Event dispatched by parent container when close button clicked
    */
    public function click(e:Object):Void{
        e.target.deletePopUp();
    }
    
    
	/**
	 * Recursive function to set any folder with children to be a branch
	 * TODO: Might / will have to change this behaviour once designs are being returned into the mix
	 * @usage   
	 * @param   node 
	 * @return  
	 */
    private function setBranches(node:XMLNode){
		if(node.hasChildNodes() || node.attributes.isBranch){
			treeview.setIsBranch(node, true);
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
	private function setUpBranchesInit(){
		Debugger.log('Running...',Debugger.GEN,'setUpBranchesInit','org.lamsfoundation.lams.monitoring.LessonManagerDialog');
		
		// clear tree
		treeview.removeAll();
		
		//get the 1st child
		treeview.dataProvider = MonitorModel(_monitorView.getModel()).treeDP;
		var fNode = treeview.dataProvider.firstChild;
		setBranches(fNode);
		treeview.refresh();
	}
	
	
	/**
	 * Sets up the treeview with whatever data is in the treeDP
	 * TODO - extend this to make it recurse all the way down the tree
	 * @usage   
	 * @return  
	 */
	private function setUpTreeview(){
			
		setUpBranchesInit();
		
		treeview.selectedNode = treeview.firstVisibleNode;
		
		_monitorModel.setOrganisation(new Organisation(treeview.selectedNode.attributes.data));
		
		// run polling method
		checkUsersLoaded();
		
		var callback:Function = Proxy.create(this,loadStaff);
		_monitorModel.requestStaff(treeview.selectedNode.attributes.data, callback);
		
		
		callback = Proxy.create(this,loadLearners);
		_monitorModel.requestLearners(treeview.selectedNode.attributes.data, callback);
			
		
    }
	
	 /**
    * Runs periodically and dispatches events as they are ready
    */
    private function checkUsersLoaded() {
        //If it's the first time through then set up the interval to keep polling this method
        if(!_UsersLoadCheckIntervalID) {
            _UsersLoadCheckIntervalID = setInterval(Proxy.create(this,checkUsersLoaded),USERS_LOAD_CHECK_INTERVAL);
        } else {
			_usersLoadCheckCount++;
            //If all events dispatched clear interval and call start()
            if(_learnersLoaded && _staffLoaded){
				clearInterval(_UsersLoadCheckIntervalID);
				enableButtons(true);
            }else {
				if(_usersLoadCheckCount >= USERS_LOAD_CHECK_TIMEOUT_COUNT){
					//if we havent loaded the dict or theme by the timeout count then give up
					Debugger.log('raeached time out waiting to load dict and themes, giving up.',Debugger.CRITICAL,'checkUILoaded','Application');
					clearInterval(_UsersLoadCheckIntervalID);
				}
            }
        }
    }
	
	public function getOrganisations(courseID:Number, classID:Number):Void{
		// TODO check if already set
		
		var callback:Function = Proxy.create(this,showOrgTree);
           
		if(classID != undefined){
			Application.getInstance().getComms().getRequest('workspace.do?method=getUserOrganisation&userID='+_root.userID+'&organisationID='+classID+'&roles=MONITOR,COURSE MANAGER',callback, false);
		}else if(courseID != undefined){
			Application.getInstance().getComms().getRequest('workspace.do?method=getUserOrganisation&userID='+_root.userID+'&organisationID='+courseID+'&roles=MONITOR,COURSE MANAGER',callback, false);
		}else{
			// TODO no course or class defined
		}
	}
	
	private function showOrgTree(dto:Object):Void{
		
		if(dto instanceof LFError) {
			LFError(dto).showErrorAlert();
			return;
		}
		
		// create root (dummy) node
		var odto = getDataObject(dto);
			
		_monitorModel.initOrganisationTree();
		
		var rootNode:XMLNode = _monitorModel.treeDP.addTreeNode(odto.name, odto);
		_monitorModel.setOrganisationResource(RT_ORG+'_'+odto.organisationID,rootNode);
		
		// create tree xml branches
		createXMLNodes(rootNode, dto.nodes);
		
		// set up the tree
		setUpTreeview();
		
	}

	private function createXMLNodes(root:XMLNode, nodes:Array) {
		for(var i=0; i<nodes.length; i++){
			
			var odto = getDataObject(nodes[i]);
			var childNode:XMLNode = root.addTreeNode(odto.name, odto);
			
			if(nodes[i].nodes.length>0){
				childNode.attributes.isBranch = true;
				createXMLNodes(childNode, nodes[i].nodes);
			} else {
				childNode.attributes.isBranch = false;
			}
			
			_monitorModel.setOrganisationResource(RT_ORG+'_'+odto.organisationID,childNode);
			
		}
		
	}

	private function getDataObject(dto:Object):Object{
		var odto= {};
		odto.organisationID = dto.organisationID;
		odto.organisationTypeId = dto.organisationTypeId;
		odto.description = dto.description;
		odto.name = dto.name;
		odto.parentID = dto.parentID;
		
		return odto;
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
		_learnerList = clearScp(_learnerList);
		_learner_mc = learner_scp.content;

		for(var i=0; i<users.length; i++){
		var user:User = new User(users[i]);

		_learnerList[i] = this._learner_mc.attachMovie('staff_learner_dataRow', 'staff_learner_dataRow' + i, this._learner_mc.getNextHighestDepth());
		_learnerList[i].fullName.text = user.getFullName();
		_learnerList[i]._x = USERS_X;
		_learnerList[i]._y = USER_OFFSET * i;
		_learnerList[i].data = user.getDTO();
		
		var listItem:MovieClip = MovieClip(_learnerList[i]);
		listItem.attachMovie('CheckBox', 'user_cb', listItem.getNextHighestDepth(), {_x:0, _y:3, selected:false})
		
		}
		
		learner_scp.redraw(true);
		
		var callback:Function = Proxy.create(_monitorModel,_monitorModel.saveLearners);
		Application.getInstance().getComms().getRequest('monitoring/monitoring.do?method=getLessonLearners&lessonID='+_monitorModel.getSequence().ID,callback, false);
	}
 
	public function checkLearners(org:Organisation):Void{
		var s:Object = _monitorModel.getSequence();
		
		for(var i=0; i<_learnerList.length; i++){
			if(org.isLearner(_learnerList[i].data.userID)){
				_learnerList[i].user_cb.selected = true;
				if(s.isStarted){
					_learnerList[i].user_cb.enabled = false;
				}
			}
		}
		
		learner_scp.redraw(true);
		_learnersLoaded = true;
	}
 
	/**
	* Load staff into scrollpane
	* @param  users Users to load
	*/
	public function loadStaff(users:Array):Void{
		_staffList = clearScp(_staffList);
		_staff_mc = staff_scp.content;

		for(var i=0; i<users.length; i++){
			var user:User = new User(users[i]);

			_staffList[i] = this._staff_mc.attachMovie('staff_learner_dataRow', 'staff_learner_dataRow' + i, this._staff_mc.getNextHighestDepth());
			_staffList[i].fullName.text = user.getFullName();
			_staffList[i]._x = USERS_X;
			_staffList[i]._y = USER_OFFSET * i;
			_staffList[i].data = user.getDTO();
			
			var listItem:MovieClip = MovieClip(_staffList[i]);
			listItem.attachMovie('CheckBox', 'user_cb', listItem.getNextHighestDepth(), {_x:0, _y:3, selected:false})

		}
		
		staff_scp.redraw(true);
		
		var callback:Function = Proxy.create(_monitorModel,_monitorModel.saveStaff);
		Application.getInstance().getComms().getRequest('monitoring/monitoring.do?method=getLessonStaff&lessonID='+_monitorModel.getSequence().ID,callback, false);
		
	}

	public function checkStaff(org:Organisation):Void{
		var s:Object = _monitorModel.getSequence();
		for(var i=0; i<_staffList.length; i++){
			if(org.isMonitor(_staffList[i].data.userID)){
				_staffList[i].user_cb.selected = true;
				if(s.isStarted){
					_staffList[i].user_cb.enabled = false;
				}
			}
		}
		
		staff_scp.redraw(true);
		_staffLoaded = true;
	}
	
	private function enableButtons(b:Boolean){
		ok_btn.enabled = b;
		cancel_btn.enabled = b;
	}
	
	/**
    * Called on initialisation
    */
    private function setStyles(){
        //LFWindow, goes first to prevent being overwritten with inherited styles.
        var styleObj = themeManager.getStyleObject('LFWindow');
        _container.setStyle('styleName',styleObj);
		
		//Apply panel style
		styleObj = themeManager.getStyleObject('BGPanel');
		panel.setStyle('styleName', styleObj);

		//Apply scrollpane style
		styleObj = themeManager.getStyleObject('scrollpane');
		learner_scp.setStyle('styleName', styleObj);
		staff_scp.setStyle('styleName', styleObj);
		
		//Apply tree style
		styleObj = themeManager.getStyleObject('treeview');
		treeview.setStyle('styleName', styleObj);
		
        //Get the button style from the style manager and apply to both buttons
        styleObj = themeManager.getStyleObject('button');
        ok_btn.setStyle('styleName',styleObj);
        cancel_btn.setStyle('styleName',styleObj);
        
        //Apply label style 
        styleObj = themeManager.getStyleObject('label');
		organisation_lbl.setStyle('styleName', styleObj);
		staff_lbl.setStyle('styleName', styleObj);
		learners_lbl.setStyle('styleName', styleObj);
    }
	
	private function setLabels(){
		ok_btn.label = Dictionary.getValue('ls_win_editclass_save_btn');
		cancel_btn.label = Dictionary.getValue('ls_win_editclass_cancel_btn');
		organisation_lbl.text = Dictionary.getValue('ls_win_editclass_organisation_lbl');
		staff_lbl.text = Dictionary.getValue('ls_win_editclass_staff_lbl');
		learners_lbl.text = Dictionary.getValue('ls_win_editclass_learners_lbl');
	}
	
    /**
    * Main resize method, called by scrollpane container/parent
    */
    public function setSize(w:Number,h:Number):Void{
        //Size the panel
        panel.setSize(w,h);

        //Buttons
        ok_btn.move(w-xOkOffset,h-yOkOffset);
        cancel_btn.move(w-xCancelOffset,h-yCancelOffset);
    }
    
    /**
    * set the container refernce to the window holding the dialog
    */
    function set container(value:MovieClip){
        _container = value;
    }
    
    function get selectedOrgId():Number { 
        return _selectedOrgId;
    }
	
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get resultDTO():Object {
		return _resultDTO;
	}
	
	public function get learnerList():Array{
		return _learnerList;
	}
	
	public function get staffList():Array{
		return _staffList;
	}

}