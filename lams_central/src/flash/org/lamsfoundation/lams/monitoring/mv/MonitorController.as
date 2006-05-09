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
import org.lamsfoundation.lams.monitoring.*;
import org.lamsfoundation.lams.monitoring.mv.*;
import org.lamsfoundation.lams.monitoring.mv.tabviews.*;
import mx.utils.*

/**
* Controller for the sequence library
*/
class MonitorController extends AbstractController {
	private var _monitorModel:MonitorModel;
	private var _monitorController:MonitorController;
	//private var _monitorView:MonitorView;
	//private var _lessonTabView:LessonTabView;
	//private var _canvasView:CanvasView;
	//private var _canvasView:CanvasView;
	//private var _canvasView:CanvasView;
	
	/**
	* Constructor
	*
	* @param   cm   The model to modify.
	*/
	public function MonitorController (mm:Observable) {
		super (mm);
		_monitorController = this;
		_monitorModel = MonitorModel(model);
		//get a view if ther is not one
		//if(!_lessonTabView){
		//	_lessonTabView =  LessonTabView(getView());
		//}
	}
	
	// add control methods
	
	/**
	 * Event listener for when when tab is clicked
	 * 
	 * @usage   
	 * @param   evt 
	 * @return  
	 */
	public function change(evt):Void{
		trace(evt.target);
		trace("test: "+ String(evt.target.selectedIndex))
		if (_monitorModel.getSequence() == null){
			trace ("None of Sequence is selected yet!");
		}else {
			_monitorModel.changeTab(evt.target.selectedIndex);
		}
	
	
	}
	
	public function click(evt):Void{
		trace(evt.target);
		var tgt:String = new String(evt.target);
		if(tgt.indexOf("selectClass_btn") != -1){
			trace('you clicked select button..');
			_monitorModel.setDialogOpen("LM_DIALOG");
		}
	}
	
	/**
	 * called when the dialog is loaded, calles methods to set up content in dialogue
	 * also sets up the okClicked event listener
	 * @usage   
	 * @param   evt 
	 * @return  
	 */
    public function openDialogLoaded(evt:Object) {
        Debugger.log('!evt.type:'+evt.type,Debugger.GEN,'openDialogLoaded','org.lamsfoundation.lams.MonitorController');
        //Check type is correct
        if(evt.type == 'contentLoaded'){
            //Set up callback for ok button click
            Debugger.log('!evt.target.scrollContent:'+evt.target.scrollContent,Debugger.GEN,'openDialogLoaded','org.lamsfoundation.lams.MonitorView');
            evt.target.scrollContent.addEventListener('okClicked',Delegate.create(this,okClicked));
            //evt.target.scrollContent.addEventListener('locationTabClick',Delegate.create(this,locationTabClick));
            //evt.target.scrollContent.addEventListener('propertiesTabClick',Delegate.create(this,propertiesTabClick));
			trace('test getView ' + getView());
			//set a ref to the view
			evt.target.scrollContent.monitorView = LessonTabView(getView());
			//set a ref to the dia in the view
			LessonTabView(getView()).lessonManagerDialog = evt.target.scrollContent;
			//set up UI
			//note this function registeres the dialog to recieve view updates
			evt.target.scrollContent.setUpContent();		
			//populate the licenses drop down
			//_workspaceModel.populateLicenseDetails();			
			//select the right tab, dont pass anything to show the default tab
			//_workspaceModel.showTab(_workspaceModel.currentTab);
        } else {
            //TODO DI 25/05/05 raise wrong event type error 
        }
    }
	
	/**
    * Workspace dialog OK button clicked handler
    */
    private function okClicked(evt:Object) {
        Debugger.log('!okClicked:'+evt.type+', now follows the resultDTO:',Debugger.GEN,'okClicked','org.lamsfoundation.lams.MonitorController');
        //Check type is correct
		if(evt.type == 'okClicked'){
			
            //Call the callback, passing in the design selected designId

			//invalidate the cache of folders
			//_workspaceModel.clearWorkspaceCache(evt.target.resultDTO.organisationID);
			
			//pass the resultant DTO back to the class that called us.
            //_monitorModel.getMonitor().onOKCallback(evt.target.resultDTO);
        }else {
            //TODO DI 25/05/05 raise wrong event type error 
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
		_monitorModel.setSelectedTreeNode(treeview.selectedNode);
	}
}