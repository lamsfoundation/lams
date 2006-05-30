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

import org.lamsfoundation.lams.common.mvc.*;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.wizard.*;
import org.lamsfoundation.lams.common.ws.*;
import mx.utils.*

/**
* Controller for the sequence library
*/
class WizardController extends AbstractController {
	private var _wizardModel:WizardModel;
	private var _wizardController:WizardController;
	private var _isBusy:Boolean;
	/**
	* Constructor
	*
	* @param   wm   The model to modify.
	*/
	public function WizardController (wm:Observable) {
		super (wm);
		_wizardModel = WizardModel(model);
		_wizardController = this;
		_isBusy = false;
	}
	
	// add control methods
	

	public function click(evt):Void{
		trace(evt.target);
		var tgt:String = new String(evt.target);
		
		// button click event handler - next, prev, finish, cancel
	}
	
	/**
    * Workspace dialog OK button clicked handler
    */
    private function okClicked(evt:Object) {
		
		if(evt.type == 'okClicked'){
			//invalidate the cache of folders
			//getView().workspaceView.getModel().clearWorkspaceCache(evt.target.resultDTO.targetWorkspaceFolderID);
			
			//pass the resultant DTO back to the class that called us.
            Application.getInstance().getWorkspace().onOKCallback(evt.target.resultDTO);
			
		}
	}
	
	/**
    * Invoked when the node is opened.  it must be a folder
    */
    public function onTreeNodeOpen (evt:Object){
		var treeview = evt.target;
		var nodeToOpen:XMLNode = evt.node;
		Debugger.log('nodeToOpen organisationID:'+nodeToOpen.attributes.data.organisationID,Debugger.GEN,'onTreeNodeOpen','org.lamsfoundation.lams.MonitorController');
		Debugger.log('nodeToOpen org name:'+nodeToOpen.attributes.data.name,Debugger.GEN,'onTreeNodeOpen','org.lamsfoundation.lams.MonitorController');
		//if this ndoe has children then the 
		//data has already been got, nothing to do
		
    }
	
	/**
    * Treeview data changed event handler
    */
    public function onTreeNodeClose (evt:Object){
		Debugger.log('type::'+evt.type,Debugger.GEN,'onTreeNodeClose','org.lamsfoundation.lams.MonitorController');
		var treeview = evt.target;
    }
	
	public function onTreeNodeChange (evt:Object){
		Debugger.log('type::'+evt.type,Debugger.GEN,'onTreeNodeChange','org.lamsfoundation.lams.MonitorController');
		var treeview = evt.target;
		if(!_isBusy){
			setBusy();
			_wizardModel.setSelectedTreeNode(treeview.selectedNode);
		} else {
			treeview.selectedNode = _wizardModel.getSelectedTreeNode();
		}
	}
	
	public function selectTreeNode(node:XMLNode){
		if(node!=null){
			if(!_isBusy){
				setBusy();
				_wizardModel.setSelectedTreeNode(node);
			}
		}
	}
	
	/**
	 * Initialize lesson returning new LessonID
	 *   
	 * @param   resultDTO Wizard data
	 *  
	 */
	
	public function initializeLesson(resultDTO:Object){
		_wizardModel.resultDTO = resultDTO;
		var callback:Function = Proxy.create(this,saveLessonClass);
		_wizardModel.getWizard().initializeLesson(resultDTO, callback);
	}
	
	/**
	 * Save Lesson Class after Lesson is initialized
	 *  
	 * @param   lessonID 
	 * @return  
	 */
	
	public function saveLessonClass(lessonID:Number){
		trace('saving lesson class after lesson initialized');
		_wizardModel.lessonID = lessonID;
		_wizardModel.getWizard().createLessonClass();
	}
	
	private function getView():WizardView{
		return WizardView(super.getView());
	}
	
	public function setBusy(){
		_isBusy = true;
	}
	
	public function clearBusy(){
		_isBusy = false;
	}
	
}