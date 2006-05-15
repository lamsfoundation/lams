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
	public static var USERS_X:Number = 0;
	public static var USER_OFFSET:Number = 20;
	
	//References to components + clips 
    private var _container:MovieClip;  //The container window that holds the dialog
	
    private var ok_btn:Button;         //OK+Cancel buttons
    private var cancel_btn:Button;
    
	private var panel:MovieClip;       //The underlaying panel base
	
	private var treeview:Tree;              //Treeview for navigation through workspace folder structure
	private var org_dnd:TreeDnd;
	
	private var staff_scp:MovieClip;		// staff/teachers container
	private var learner_scp:MovieClip;		// learners container
	
    private var fm:FocusManager;            //Reference to focus manager
    private var themeManager:ThemeManager;  //Theme manager
	
	//Dimensions for resizing
    private var xOkOffset:Number;
    private var yOkOffset:Number;
    private var xCancelOffset:Number;
    private var yCancelOffset:Number;
	
	private var _lessonTabView:LessonTabView;
	private var _monitorModel:MonitorModel;
	private var _monitorView:MonitorView;
	private var _monitorController:MonitorController;

	private var _learnerList:Array;
	private var _staffList:Array;

	private var _resultDTO:Object;
	private var _selectedOrgId:Number;	// selected organisation
	
	//These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	
	 /**
    * constructor
    */
    function LessonManagerDialog(){
		trace('initialising Lesson Manager Dialog');
        //Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);
		_resultDTO = new Object();
        
        //Create a clip that will wait a frame before dispatching init to give components time to setup
        MovieClipUtils.doLater(Proxy.create(this,init));
	}
	
	/**
    * Called a frame after movie attached to allow components to initialise
    */
    private function init():Void{
        
        trace('now initialising ...');
        //set the reference to the StyleManager
        //themeManager = ThemeManager.getInstance();
        
		// Set the styles
        //setStyles();
		
        //Set the text for buttons
        //ok_btn.label = Dictionary.getValue('lesson_dlg_ok');
        //cancel_btn.label = Dictionary.getValue('lesson_dlg_cancel');
		ok_btn.label = "ok";
		cancel_btn.label = "cancel";
		
		
        //Set the labels
        
        //get focus manager + set focus to OK button, focus manager is available to all components through getFocusManager
        //fm = _container.getFocusManager();
        //fm.enabled = true;
        //ok_btn.setFocus();
		
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
		trace('LF window: ' + _container);
		trace('Tree: ' + treeview);
		//fire event to say we have loaded
		_container.contentLoaded();
    }
	
		/**
	 * Called by the worspaceView after the content has loaded
	 * @usage   
	 * @return  
	 */
	public function setUpContent():Void{
		
		//register to recive updates form the model
		//MonitorModel(_monitorView.getModel()).addEventListener('viewUpdate',this);
		
		Debugger.log('_monitorView:'+_monitorView,Debugger.GEN,'setUpContent','org.lamsfoundation.lams.LessonManagerDialog');
		
		//get a ref to the controller and kkep it here to listen for events:
		_monitorController = _monitorView.getController();
		_monitorModel = MonitorModel(_monitorView.getModel());
		
		 //Add event listeners for ok, cancel and close buttons
        ok_btn.addEventListener('click',Delegate.create(this, ok));
        cancel_btn.addEventListener('click',Delegate.create(this, cancel));
		
		getOrganisations();
		
		//Set up the treeview
        //setUpTreeview();
		
		//itemSelected(treeview.selectedNode);
	}
	
    /**
    * Called on initialisation
    */
    private function setStyles(){
        //LFWindow, goes first to prevent being overwritten with inherited styles.
        var styleObj = themeManager.getStyleObject('LFWindow');
        _container.setStyle('styleName',styleObj);

        //Get the button style from the style manager and apply to both buttons
        styleObj = themeManager.getStyleObject('button');
        ok_btn.setStyle('styleName',styleObj);
        cancel_btn.setStyle('styleName',styleObj);

        //Apply label style 
        styleObj = themeManager.getStyleObject('label');
        
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
        trace('Cancel');
        //close parent window
        _container.deletePopUp();
    }
    
    /**
    * Called by the OK button 
    */
    private function ok(){
        Debugger.log('OK Clicked',Debugger.GEN,'ok','org.lamsfoundation.lams.LessonManagerDialog');
        
		_global.breakpoint();
		
		var snode = treeview.selectedNode;
		_selectedOrgId = Number(snode.attributes.data.organisationID);
		
		doOrganisationDispatch();
    }
    
	public function doOrganisationDispatch(){
		
		var snode = treeview.selectedNode;
		_resultDTO.selectedOrgID = Number(snode.attributes.data.resourceID);

		// add selected users to dto

        dispatchEvent({type:'okClicked',target:this});
	   
        closeThisDialogue();
		
	}
	
	public function closeThisDialogue(){
		 _container.deletePopUp();
	}
	
    /**
    * If an alert was spawned by this dialog this method is called when it's closed
    */
    private function alertClosed(evt:Object){
        //Should prefs dialog be closed?
        //TODO DI 01/06/05 check for delete of dialog
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
        trace('LessonManagerDialog.click');
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
				/*
				if(cNode.hasChildNodes()){
					treeview.setIsBranch(cNode, true);
					setBranches(cNode);
				}
				*/
				
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
		//get the 1st child
		treeview.dataProvider = MonitorModel(_monitorView.getModel()).treeDP.firstChild;
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
			
		//Debugger.log('_workspaceView:'+_workspaceView,Debugger.GEN,'setUpTreeview','org.lamsfoundation.lams.common.ws.WorkspaceDialog');
		
		setUpBranchesInit();
		
		treeview.addEventListener("nodeOpen", Delegate.create(_monitorController, _monitorController.onTreeNodeOpen));
		treeview.addEventListener("nodeClose", Delegate.create(_monitorController, _monitorController.onTreeNodeClose));
		treeview.addEventListener("change", Delegate.create(_monitorController, _monitorController.onTreeNodeChange));

		//org_dnd.addEventListener("drag_complete", Delegate.create(_lessonManagerController, _lessonManagerController.onDragComplete));
		
    }
	
	private function getOrganisations():Void{
		var callback:Function = Proxy.create(this,showOrgTree);
           
		Application.getInstance().getComms().getRequest('workspace.do?method=getOrganisationsByUserRole&userID='+_root.userID+'&roles=STAFF,TEACHER',callback, false);
		
	}
	
	private function showOrgTree(dto:Object):Void{
		trace('organisations tree returned...');
		trace('creating root node...');
		// create root (dummy) node
		
		var odto = getDataObject(dto);
			
		
		var rootNode:XMLNode = _monitorModel.treeDP.addTreeNode(odto.name, odto);
		//rootNode.attributes.isBranch = true;
		_monitorModel.setOrganisationResource(RT_ORG+'_'+odto.organisationID,rootNode);
		
		// create tree xml branches
		createXMLNodes(rootNode, dto.nodes);
		
		// set up the tree
		setUpTreeview();
		
	}

	private function createXMLNodes(root:XMLNode, nodes:Array) {
		for(var i=0; i<nodes.length; i++){
			trace('creating child node...');
			
			var odto = getDataObject(nodes[i]);
			var childNode:XMLNode = root.addTreeNode(odto.name, odto);
			
			trace('adding node with org ID: ' + odto.organisationID);
			
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
	
	/**
	 * Load learners into scrollpane
	 * @param   users Users to load
	 */
	
	public function loadLearners(users:Array):Void{
		trace('loading Learners...');
		_learnerList = new Array();
		trace('list length: ' + users.length);
		for(var i=0; i<users.length; i++){
			var user:User = User(users[i]);
			
			_learnerList[i] = learner_scp.attachMovie('staff_learner_dataRow', 'userDataRow' + i, DepthManager.kTop, {fullName:user.getFirstName(),_x:USERS_X,_y:USER_OFFSET * i});
			trace('new row: ' + _learnerList[i]);
			trace('loading: user ' + user.getFirstName() + ' ' + user.getLastName());
			learner_scp.refreshPane();
		}
	}
	
	/**
	* Load staff into scrollpane
	* @param 	users Users to load
	*/
	public function loadStaff(users:Array):Void{
		trace('loading Staff....');
		trace('list length: ' + users.length);
		_staffList = new Array();
		for(var i=0; i<users.length; i++){
			//_staffList[i] = staff_scp.attachmovie();
			//_staffList[i]._x = USERS_X;
			//_staffList[i[._y = USER_OFFSET * i;
			var user:User = User(users[i]);
			trace('loading: user ' + user.getFirstName() + ' ' + user.getLastName());
			staff_scp.refresh();
		}
	}
	
	
    /**
    * Main resize method, called by scrollpane container/parent
    */
    public function setSize(w:Number,h:Number):Void{
        //Debugger.log('setSize',Debugger.GEN,'setSize','org.lamsfoundation.lams.common.ws.WorkspaceDialog');
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

}