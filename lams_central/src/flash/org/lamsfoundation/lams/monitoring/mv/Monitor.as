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
import org.lamsfoundation.lams.monitoring.Application;
import org.lamsfoundation.lams.monitoring.Organisation;
import org.lamsfoundation.lams.monitoring.mv.*;
import org.lamsfoundation.lams.monitoring.mv.tabviews.*;
import org.lamsfoundation.lams.authoring.DesignDataModel;
import org.lamsfoundation.lams.common.ui.*;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.dict.*;
import org.lamsfoundation.lams.common.* ;

import mx.utils.*;
import mx.managers.*;
import mx.events.*;

class Monitor {
	//Constants
	public static var USE_PROPERTY_INSPECTOR = true;
	public var RT_ORG:String = "Organisation";

	private var _className:String = "Monitor";

	// Root movieclip
	private var _root_mc:MovieClip;
	
	// Model
	private var monitorModel:MonitorModel;
	// View
	private var monitorView:MonitorView;
	private var monitorView_mc:MovieClip;

	private var app:Application;
	private var _ddm:DesignDataModel;
	private var _dictionary:Dictionary;

	private var _currentUserType:String;

	private var _pi:MovieClip; //Property inspector
	
	private var dispatchEvent:Function;       
    public var addEventListener:Function;  
    public var removeEventListener:Function;
	
	private var _onOKCallBack:Function;
	
	/**
	 * Monitor Constructor Function
	 * 
	 * @usage   
	 * @return  target_mc		//Target clip for attaching view
	 */
	public function Monitor(target_mc:MovieClip,depth:Number,x:Number,y:Number,w:Number,h:Number){
		mx.events.EventDispatcher.initialize(this);
		
		// Set root movieclip
		_root_mc = target_mc;
		
		//Create the model
		monitorModel = new MonitorModel(this);
		_ddm = new DesignDataModel();
		_dictionary = Dictionary.getInstance();

		//Create the view
		monitorView_mc = target_mc.createChildAtDepth("monitorView",DepthManager.kTop);	
		
		trace(monitorView_mc);
		
		monitorView = MonitorView(monitorView_mc);
		
		
		monitorView.init(monitorModel,undefined,x,y,w,h);
		
        monitorView.addEventListener('load',Proxy.create(this,viewLoaded));
		//lessonTabView_mc = monitorView_mc.
		
       //dictionary.addEventListener('init',Proxy.create(this,setupPI));
		
		//Register view with model to receive update events
		monitorModel.addObserver(monitorView);
		
		//monitorModel.addObserver(monitorView);
		//monitorModel.addObserver(monitorView);
		//monitorModel.addObserver(monitorView);

        //Set the position by setting the model which will call update on the view
        monitorModel.setPosition(x,y);
		monitorModel.setSize(w,h);
		monitorModel.initOrganisationTree();
		//Get reference to application and design data model
		//app = Application.getInstance();
		
		
	}
	
	/**
	* event broadcast when Monitor is loaded 
	*/ 
	public function broadcastInit(){
		dispatchEvent({type:'init',target:this});		
	}
 
	
	private function viewLoaded(evt:Object){
        Debugger.log('viewLoaded called',Debugger.GEN,'viewLoaded','Monitor');
		
		if(evt.type=='load') {
            dispatchEvent({type:'load',target:this});
        }else {
            //Raise error for unrecognized event
        }
    }
	
	public function getOrganisations():Void{
		var callback:Function = Proxy.create(this,showOrgTree);
           
		Application.getInstance().getComms().getRequest('workspace.do?method=getOrganisationsByUserRole&userID='+_root.userID+'&roles=STAFF,TEACHER',callback, false);
		
	}
	
	private function showOrgTree(dto:Object):Void{
		trace('organisations tree returned...');
		trace('creating root node...');
		// create root (dummy) node
		
		var odto = getDataObject(dto);
			
		
		var rootNode:XMLNode = getMM().treeDP.addTreeNode(odto.name, odto);
		//rootNode.attributes.isBranch = true;
		getMM().setOrganisationResource(RT_ORG+'_'+odto.organisationID,rootNode);
		
		// create tree xml branches
		createXMLNodes(rootNode, dto.nodes);
		
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
			
			getMM().setOrganisationResource(RT_ORG+'_'+odto.organisationID,childNode);
			
		}
		
	}

	private function getDataObject(dto:Object):Object{
		var odto= {};
		odto.organisationID = dto.organisationID;
		odto.description = dto.description;
		odto.name = dto.name;
		odto.parentID = dto.parentID;
		
		return odto;
	}

	public function requestOrgUsersByRole(data:Object, role:String){
		trace('requesting org users by role: ' + role);
		var callback:Function = Proxy.create(this,saveUsers);
		_currentUserType = role;
		Application.getInstance().getComms().getRequest('workspace.do?method=getUsersFromOrganisationByRole&organisationID='+data.organisationID+'&role='+role,callback, false);
		
	}

	private function saveUsers(users:Array){
		trace('retrieving back users for org by role: ' + _currentUserType);
		
	}

	/**
	 * server call for learning Dseign and sent it to the save it in DataDesignModel
	 * 
	 * @usage   
	 * @param   		seq type Sequence;
	 * @return  		Void
	 */
	public function openLearningDesign(seq:Sequence){
		trace('opening learning design...'+ seq.getLearningDesignID());
		var designID:Number  = seq.getLearningDesignID();
		var callback:Function = Proxy.create(this,saveDataDesignModel);
           
		Application.getInstance().getComms().getRequest('authoring/author.do?method=getLearningDesignDetails&learningDesignID='+designID,callback, false);
		
	}

	private function saveDataDesignModel(learningDesignDTO:Object){
		trace('returning learning design...');
		trace('saving model data...');
		var seq:Sequence = Sequence(monitorModel.getSequence());
		
		//  clear canvas
		clearCanvas(true);
			
		_ddm.setDesign(learningDesignDTO);
		seq.setLearningDesignModel(_ddm);
		
		monitorModel.broadcastViewUpdate('REDRAW_CANVAS', null, monitorModel.getSelectedTab());
	}
	
	/**
	 * Clears the design in the canvas.but leaves other state variables (undo etc..)
	 * @usage   
	 * @param   noWarn 
	 * @return  
	 */
	public function clearCanvas(noWarn:Boolean):Boolean{
		//_global.breakpoint();
		var s = false;
		var ref = this;
		Debugger.log('noWarn:'+noWarn,4,'clearCanvas','Monitor');
		if(noWarn){
			
			_ddm = new DesignDataModel();
			//as its a new instance of the ddm,need to add the listener again
			//_ddm.addEventListener('ddmUpdate',Proxy.create(this,onDDMUpdated));
			Debugger.log('noWarn2:'+noWarn,4,'clearCanvas','Monitor');//_ddm.addEventListener('ddmBeforeUpdate',Proxy.create(this,onDDMBeforeUpdate));
			//checkValidDesign();
			monitorModel.setDirty();
			return true;
		}else{
			//var fn:Function = Proxy.create(ref,confirmedClearDesign, ref);
			//LFMessage.showMessageConfirm(Dictionary.getValue('new_confirm_msg'), fn,null);
			Debugger.log('Set design failed as old design could not be cleared',Debugger.CRITICAL,"setDesign",'Canvas');		
		}
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
		monitorModel.setSize(width, height);
	}
    
    public function setPosition(x:Number,y:Number){
        //Set the position within limits
        //TODO DI 24/05/05 write validation on limits
        monitorModel.setPosition(x,y);
    }
	
	//Dimension accessor methods
	public function get width():Number{
		return monitorModel.width;
	}
	
	public function get height():Number{
		return monitorModel.height;
	}
	
	public function get x():Number{
		return monitorModel.x;
	}
	
	public function get y():Number{
		return monitorModel.y;
	}

    function get className():String { 
        return _className;
    }
	public function getMM():MonitorModel{
		return monitorModel;
	}
	public function getMV():MonitorView{
		return monitorView;
	}

	public function get ddm():DesignDataModel{
		return _ddm;
	}
}