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
import org.lamsfoundation.lams.wizard.Application;
import org.lamsfoundation.lams.monitoring.Organisation;
import org.lamsfoundation.lams.monitoring.User;
import org.lamsfoundation.lams.wizard.*;
import org.lamsfoundation.lams.common.ui.*;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.dict.*;
import org.lamsfoundation.lams.common.ws.*;
import org.lamsfoundation.lams.common.* ;

import mx.utils.*;
import mx.managers.*;
import mx.events.*;

class Wizard {
	//Constants
	public static var USE_PROPERTY_INSPECTOR = true;
	public var RT_ORG:String = "Organisation";
	private var _className:String = "Wizard";

	// Root movieclip
	private var _root_mc:MovieClip;
	
	// Model
	private var wizardModel:WizardModel;
	// View
	private var wizardView:WizardView;
	private var wizardView_mc:MovieClip;

	private var app:Application;
	private var _dictionary:Dictionary;
	
	private var dispatchEvent:Function;       
    public var addEventListener:Function;  
    public var removeEventListener:Function;
	
	private var _onOKCallBack:Function;
	
	
	
	/**
	 * Wizard Constructor Function
	 * 
	 * @usage   
	 * @return  target_mc		//Target clip for attaching view
	 */
	public function Wizard(target_mc:MovieClip,x:Number,y:Number,w:Number,h:Number){
		mx.events.EventDispatcher.initialize(this);
		
		// Set root movieclip
		_root_mc = target_mc;
		
		//Create the model
		wizardModel = new WizardModel(this);
		_dictionary = Dictionary.getInstance();

		//Create the view
		wizardView_mc = target_mc.createChildAtDepth("wizardView",DepthManager.kTop);	
		
		trace(wizardView_mc);
		
		wizardView = WizardView(wizardView_mc);
		wizardView.init(wizardModel,undefined);
        wizardView.addEventListener('load',Proxy.create(this,viewLoaded));

		//Register view with model to receive update events
		wizardModel.addObserver(wizardView);
		wizardModel.addEventListener('learnersLoad',Proxy.create(this,onUserLoad));
		wizardModel.addEventListener('staffLoad',Proxy.create(this,onUserLoad));

        //Set the position by setting the model which will call update on the view
        wizardModel.setPosition(x,y);
		wizardModel.setSize(w,h);
		wizardModel.initOrganisationTree();
		
	}
	
	/**
	* event broadcast when Wizard is loaded 
	*/ 
	public function broadcastInit(){
		dispatchEvent({type:'init',target:this});		
	}
 
	
	private function viewLoaded(evt:Object){
        Debugger.log('viewLoaded called',Debugger.GEN,'viewLoaded','Wizard');
		
		if(evt.type=='load') {
			openDesignByWizard();
            dispatchEvent({type:'load',target:this});
        }else {
            //Raise error for unrecognized event
        }
    }
	
	/**
    * Called when Users loaded for role type
	* @param evt:Object	the event object
    */
    private function onUserLoad(evt:Object){
        if(evt.type=='staffLoad'){
            wizardModel.staffLoaded = true;
			Debugger.log('Staff loaded :',Debugger.CRITICAL,'onUserLoad','Wizard');			
        } else if(evt.type=='learnersLoad'){
			wizardModel.learnersLoaded = true;
			Debugger.log('Learners loaded :',Debugger.CRITICAL,'onUserLoad','Wizard');			
		} else {
            Debugger.log('event type not recognised :'+evt.type,Debugger.CRITICAL,'onUserLoad','Wizard');
        }
    }
	
	public function initWorkspace(){
		wizardView.setUpContent();
	}
	
	public function getOrganisations():Void{
		// TODO check if already set
		
		var callback:Function = Proxy.create(this,showOrgTree);
           
		Application.getInstance().getComms().getRequest('workspace.do?method=getOrganisationsByUserRole&userID='+_root.userID+'&roles=STAFF,TEACHER',callback, false);
		
	}
	
	private function showOrgTree(dto:Object):Void{
		trace('organisations tree returned...');
		trace('creating root node...');
		// create root (dummy) node
		
		var odto = getDataObject(dto);
			
		
		var rootNode:XMLNode = wizardModel.treeDP.addTreeNode(odto.name, odto);
		//rootNode.attributes.isBranch = true;
		wizardModel.setOrganisationResource(RT_ORG+'_'+odto.organisationID,rootNode);
		
		// create tree xml branches
		createXMLNodes(rootNode, dto.nodes);
		
		// set up the org tree
		wizardView.setUpOrgTree();
		
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
			
			wizardModel.setOrganisationResource(RT_ORG+'_'+odto.organisationID,childNode);
			
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
	* Opens a design using workspace and user to select design ID
	* passes the callback function to recieve selected ID
	*/
	public function openDesignByWizard(){
        //Work space opens dialog and user will select view
        var callback:Function = Proxy.create(this, openDesignById);
		var ws = Application.getInstance().getWorkspace();
        ws.wizardSelectDesign(callback);
	}
    
	/**
	 * Request design from server using supplied ID.
	 * @usage   
	 * @param   designId 
	 * @return  
	 */
    private function openDesignById(workspaceResultDTO:Object){
		trace('step 1 completed');
		ObjectUtils.toString(workspaceResultDTO);
		wizardModel.workspaceResultDTO = workspaceResultDTO;
		//var designId:Number = workspaceResultDTO.selectedResourceID;
		//var lessonName:String = workspaceResultDTO.resourceName;
		//var lessonDesc:String = workspaceResultDTO.resourceDescription;
        //var callback:Function = Proxy.create(this,setLesson);
		
		//Application.getInstance().getComms().getRequest('monitoring/monitoring.do?method=initializeLesson&learningDesignID='+designId+'&userID='+_root.userID+'&lessonName='+lessonName+'&lessonDescription='+lessonDesc,callback, false);

        
    }
	
	public function requestUsers(role:String, orgID:Number, callback:Function){
		Application.getInstance().getComms().getRequest('workspace.do?method=getUsersFromOrganisationByRole&organisationID='+orgID+'&role='+role,callback, false);
	
	}

	/**
	 * 
	 * @usage   
	 * @param   newonOKCallback 
	 * @return  
	 */
	public function set onOKCallback (newonOKCallback:Function):Void {
		_onOKCallBack = newonOKCallback;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get onOKCallback ():Function {
		return _onOKCallBack;
	}

	/**
	* Used by application to set the size
	* @param width The desired width
	* @param height the desired height
	*/
	public function setSize(width:Number, height:Number):Void{
		wizardModel.setSize(width, height);
	}
    
    public function setPosition(x:Number,y:Number){
        //Set the position within limits
        //TODO DI 24/05/05 write validation on limits
        wizardModel.setPosition(x,y);
    }
	
	//Dimension accessor methods
	public function get width():Number{
		return wizardModel.width;
	}
	
	public function get height():Number{
		return wizardModel.height;
	}
	
	public function get x():Number{
		return wizardModel.x;
	}
	
	public function get y():Number{
		return wizardModel.y;
	}

    function get className():String { 
        return _className;
    }
	public function getWM():WizardModel{
		return wizardModel;
	}
	public function getWV():WizardView{
		return wizardView;
	}
	
	public function set workspaceView(a:WorkspaceView){
		wizardView.workspaceView = a;
	}
	
	public function get workspaceView():WorkspaceView{
		return wizardView.workspaceView;
	}

	public function get root():MovieClip{
		return _root_mc;
	}
}