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
class org.lamsfoundation.lams.wizard.steps.WizardSequenceView extends AbstractView {
	
	public static var LOCAL_TAB:Number = 0;
	
	private var _className = "WizardSequenceView";
	
	private var location_treeview:Tree;
	private var location_tb:MovieClip;

	private var _workspaceView:WorkspaceView;
	private var _workspaceController:WorkspaceController;
	
	//Defined so compiler can 'see' events added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;

	function WizardSequenceView(){
		mx.events.EventDispatcher.initialize(this);
	}
	
	public function init(m:Observable,c:Controller) {
		super(m, c)
	}
	
	public function show(v:Boolean):Void {
		location_treeview.visible = v;
		location_tb._visible = false;
	}
	
	public function setupLocationTabs():Void{
		var tab_arr:Array = [{label:Dictionary.getValue('location_tab_local'), data:"local"}];
		location_tb.dataProvider = tab_arr;
		location_tb.selectedIndex = 0;
		
		location_tb.addEventListener("change", getController());
		
		location_tb._visible = true;
		
		WizardModel(_parent.getModel()).setLocationTab(LOCAL_TAB);
	}
	
	public function showTab(tabID:Number):Void {
		location_treeview.visible = true;
	}
	
	public function validate(wm:WizardModel):Boolean{
		var snode;
		
		Debugger.log("dp: " + location_tb.dataProvider, Debugger.CRITICAL, "validate", "WizardSequenceView");
		Debugger.log("visible: " + location_tb.visible, Debugger.CRITICAL, "validate", "WizardSequenceView");
		
		if(wm.getLocationTab() == LOCAL_TAB || !location_tb.visible) {
			snode = location_treeview.selectedNode;
		
			if (snode.attributes.data.resourceType == wm.RT_FOLDER){
				// set result DTO - folder selected cannot continue
				doWorkspaceDispatch(false);
				
				// show folder selected warning
				LFMessage.showMessageAlert(Dictionary.getValue('al_validation_msg1', [Dictionary.getValue('validation_object_seq')]), null, null);
				
			} else if(snode.attributes.data.resourceType==wm.RT_LD){
				// set result DTO - lesson selected
				doWorkspaceDispatch(true);
				
				return true;
			} else {
				// show general warning
				LFMessage.showMessageAlert(Dictionary.getValue('al_validation_msg1', [Dictionary.getValue('validation_object_seq')]), null, null);
				
			}
		}
		
		return false;
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
			_parent.resultDTO.selectedResourceID = Number(snode.attributes.data.resourceID);
			_parent.resultDTO.targetWorkspaceFolderID = Number(snode.attributes.data.workspaceFolderID);
			_parent.resultDTO.resourceName = snode.attributes.data.name;
		}else{
			//its a folder
			_parent.resultDTO.selectedResourceID  = null;
			_parent.resultDTO.targetWorkspaceFolderID = Number(snode.attributes.data.resourceID);
			_parent.resultDTO.resourceName = snode.attributes.data.name;
		}

        dispatchEvent({type:'okClicked',target:_parent});
		
	}
	
	
	/**
	 * Sets up the treeview with whatever data is in the treeDP
	 * TODO - extend this to make it recurse all the way down the tree
	 * @usage   
	 * @return  
	 */
	public function setUpTreeview(){
			
		_parent.setUpBranchesInit(location_treeview, WorkspaceModel(workspaceView.getModel()).treeDP, false, false);
		_workspaceController = _workspaceView.getController();
		
		location_treeview.addEventListener("nodeOpen", Delegate.create(_workspaceController, _workspaceController.onTreeNodeOpen));
		location_treeview.addEventListener("nodeClose", Delegate.create(_workspaceController, _workspaceController.onTreeNodeClose));
		location_treeview.addEventListener("change", Delegate.create(_workspaceController, _workspaceController.onTreeNodeChange));
		
		var wsNode:XMLNode = location_treeview.firstVisibleNode;
		
		location_treeview.setIsOpen(wsNode, true);
		_workspaceController.forceNodeOpen(wsNode);
		
    }
	
	/**
	 * called witht he result when a child folder is opened..
	 * updates the tree branch satus, then refreshes.
	 * @usage   
	 * @param   changedNode 
	 * @param   wm          
	 * @return  
	 */
	public function updateChildFolderBranches(changedNode:XMLNode,wm:WorkspaceModel){
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
	}
	
	public function refreshTree(){
		 Debugger.log('Refreshing tree....:' ,Debugger.GEN,'refreshTree','org.lamsfoundation.lams.ws.WorkspaceDialog');
		 location_treeview.refresh();
	}
	
	/**
	 * Just opens the fodler node - DOES NOT FIRE EVENT - so is used after updatting the child folder
	 * @usage   
	 * @param   nodeToOpen 
	 * @param   wm  
	 * @param 	isForced
	 * @return  
	 */
	public function openFolder(nodeToOpen:XMLNode, wm:WorkspaceModel){
		Debugger.log('openFolder:'+nodeToOpen ,Debugger.GEN,'openFolder','org.lamsfoundation.lams.ws.WorkspaceDialog');
		
		//open the node
		nodeToOpen.attributes.isOpen = true;
		location_treeview.setIsOpen(nodeToOpen,true);
		
		if(wm.isForced() && nodeToOpen.attributes.data.resourceID == WorkspaceModel.ROOT_VFOLDER){
			// select users root workspace folder
			location_treeview.selectedNode = nodeToOpen.firstChild;
			dispatchEvent({type:'change', target:this.location_treeview});
		}
		
		refreshTree();
	
	}
	
	/**
	 * Closes the folder node
	 * 
	 * @usage   
	 * @param   nodeToClose 
	 * @param   wm          
	 * @return  
	 */
	
	public function closeFolder(nodeToClose:XMLNode, wm:WorkspaceModel){
		Debugger.log('closeFolder:'+nodeToClose ,Debugger.GEN,'closeFolder','org.lamsfoundation.lams.ws.WorkspaceDialog');
		
		// close the node
		nodeToClose.attributes.isOpen = false;
		location_treeview.setIsOpen(nodeToClose, false);
		
		refreshTree();
	}
	
	/**
	 * Closes folder, then sends openEvent to controller
	 * @usage   
	 * @param   nodeToOpen 
	 * @param   wm         
	 * @return  
	 */
	public function refreshFolder(nodeToOpen:XMLNode, wm:WorkspaceModel){
		Debugger.log('refreshFolder:'+nodeToOpen ,Debugger.GEN,'refreshFolder','org.lamsfoundation.lams.ws.WorkspaceDialog');
		//close the node
		location_treeview.setIsOpen(nodeToOpen,false);		
		//we are gonna need to fire the event manually for some stupid reason the tree is not firing it.
		//dispatchEvent({type:'nodeOpen',target:treeview,node:nodeToOpen});
		_workspaceController = _workspaceView.getController();
		_workspaceController.onTreeNodeOpen({type:'nodeOpen',target:location_treeview,node:nodeToOpen});
	}
	
	public function setSize(dHeight:Number) {
		location_treeview.setSize(location_treeview.width, Number(location_treeview.height + dHeight));
	}
	
	public function get workspaceView():WorkspaceView{
		return _workspaceView;
	}
	
	public function set workspaceView(a:WorkspaceView){
		_workspaceView = a;
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
    public function defaultController(model:Observable):Controller {
        return new WizardController(model);
    }
	

}