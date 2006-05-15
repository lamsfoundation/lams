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

import org.lamsfoundation.lams.common.ApplicationParent;
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.monitoring.mv.*
import org.lamsfoundation.lams.monitoring.*;
import org.lamsfoundation.lams.authoring.Activity;
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.mvc.*
import mx.controls.*;
import mx.managers.*;
import mx.containers.*;
import mx.events.*;
import mx.utils.*;

/**
*Monitoring view for the Monitor
* Relects changes in the MonitorModel
*/

class org.lamsfoundation.lams.monitoring.mv.tabviews.LessonTabView extends AbstractView{
	public static var _tabID:Number = 0;
	private var _className = "LessonTabView";
	//constants:
	private var _tm:ThemeManager;
	private var mm:MonitorModel;
	
	//TabView clips
	private var _monitorReqTask_mc:MovieClip;
	private var reqTasks_scp:MovieClip;
	private var monitorTabs_tb:MovieClip;
	private var _lessonStateArr:Array;
	//Labels
	private var status_lbl:Label;
	private var learner_lbl:Label;
	private var class_lbl:Label;
	private var elapsed_lbl:Label;
	private var manageClass_lbl:Label;
	private var manageStatus_lbl:Label;
	private var manageStart_lbl:Label;
	private var manageMin_lbl:Label;
	private var manageHour_lbl:Label;
	private var manageDate_lbl:Label;
		
	//Text Items
    private var LSTitle_txt:TextField;
	private var LSDescription_txt:TextField;
	private var sessionStatus_txt:TextField;
	private var numLearners_txt:TextField;
	private var group_txt:TextField;
	private var duration_txt:TextField;
	
	//Button
	private var viewLearners_btn:Button;
	private var editClass_btn:Button;
	private var selectClass_btn:Button;
	private var status1_btn:Button;
	private var status2_btn:Button;
	private var setDateTime_btn:Button;
	
	private var startDate_dt:Date;
	private var startHour_stp:NumericStepper;
	private var startMin_stp:NumericStepper;
	
    //private var _transitionLayer_mc:MovieClip;
	//private var _activityLayerComplex_mc:MovieClip;
	//private var _activityLayer_mc:MovieClip;
	
	//private var _transitionPropertiesOK:Function;
    private var _lessonTabView:LessonTabView;
	private var _monitorController:MonitorController;
	private var _lessonManagerDialog:MovieClip;

    //Defined so compiler can 'see' events added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	//public var menu:ContextMenu;

	
	/**
	* Constructor
	*/
	function LessonTabView(){
		_lessonTabView = this;
		this._visible = false;
		_tm = ThemeManager.getInstance();
        //Init for event delegation
        mx.events.EventDispatcher.initialize(this);
	}
	
	/**
	* Called to initialise Canvas  . CAlled by the Canvas container
	*/
	public function init(m:Observable,c:Controller){
		super (m, c);
	}    
	
	/**
 * Recieved update events from the CanvasModel. Dispatches to relevent handler depending on update.Type
 * @usage   
 * @param   event
 */
public function update (o:Observable,infoObj:Object):Void{
		
       mm = MonitorModel(o);
	   
	   switch (infoObj.updateType){
		    case 'SIZE' :
			    setSize(mm);
                break;
			case 'REQUIREDTASKS' :
				trace('REQUIREDTASKS');
                break;
			case 'CLASS' :
				trace('CLASS');
                break;
			case 'STATUS' :
				trace('STATUS');
                break;
			case 'TODOS' :
				trace('TODOS');
				populateContributeActivities();
				break;
			case 'TABCHANGE' :
				if (infoObj.tabID == _tabID){
				trace("TabID for Selected tab is (LessonTab TABCHANGE): "+infoObj.tabID)
					this._visible = true;
					MovieClipUtils.doLater(Proxy.create(this,draw));
				}else {
					this._visible = false;
				}
				break;
			case 'SEQUENCE' :
				if (infoObj.tabID == _tabID){
				trace("TabID for Selected tab is (LessonTab): "+infoObj.tabID)
					this._visible = true;
					MovieClipUtils.doLater(Proxy.create(this,draw));
				}else {
					this._visible = false;
				}
				break;
			case 'LM_DIALOG' :
				_monitorController = getController();
				showLessonManagerDialog(mm);
				break;
			case 'USERS_LOADED' :
				_lessonManagerDialog.loadLearners(mm.organisation.getLearners());
				_lessonManagerDialog.loadStaff(mm.organisation.getStaff());
				break;
            default :
                Debugger.log('unknown update type :' + infoObj.updateType,Debugger.CRITICAL,'update','org.lamsfoundation.lams.LessonTabView');
		}

	}
	
	/**
    * layout visual elements on the canvas on initialisation
    */
	private function draw(){
		
		this.onEnterFrame = setupLabels;
		//get the content path for the sp
		_monitorReqTask_mc = reqTasks_scp.content;
		_monitorController = getController();
		selectClass_btn.addEventListener("click", _monitorController);
	
		//Debugger.log('_canvas_mc'+_canvas_mc,Debugger.GEN,'draw','CanvasView');
	
		trace("Loaded LessonTabView Data"+ this)
		
		//setStyles();
		populateLessonDetails();
		
		trace('seq id: ' + mm.getSequence().getSequenceID());
		trace('last seq id: ' + mm.getLastSelectedSequence().getSequenceID());
		if (mm.getSequence().getSequenceID() == mm.getLastSelectedSequence().getSequenceID()){
			if(mm.getToDos() == null){
				mm.getMonitor().getContributeActivities(mm.getSequence().getSequenceID());
			} else {
				populateContributeActivities();
			}
		}else{
			mm.getMonitor().getContributeActivities(mm.getSequence().getSequenceID());
		}
		
		dispatchEvent({type:'load',target:this});
	}
	
	/**
	 * Populate the lesson details from HashTable Sequence in MOnitorModel
	*/
	private function populateLessonDetails():Void{
		//var mm:Observable = getModel();
		var s:Object = mm.getSequence();
		trace("Item Description (Lesson Tab View) is : "+s._seqDescription);
		LSTitle_txt.text = s._seqName;
		LSDescription_txt.text = s._seqDescription;
		//sessionStatus_txt.text = _lessonStateArr(s._seqStateID);
		//numLearners_txt.text = s._seqDescription
		//group_txt.text = s._seqDescription
		//duration_txt.text = s._seqDescription
		  
	}

	/**
	* Populate the required tasks for the active Sequence 
	
	private function populateCCActivities(CCAct:ContributeActivity):Void{
		//var cAct:ContributeActivity = ContributeActivity.getInstance()
		// get contribute activities
		var todos:Array = mm.getToDos();
		trace("Looking for contribute entries for: "+CCAct.activityID);
		// show isRequired activities in scrollpane
		//for (var i=0; i<todos.length; i++){
			for (var k=0; k<todos[i]._childActivities.length; k++){
				//trace("Activity IDs in todo list are: "+todos[i].activityID);
				//if (todos[i]._childActivities[k].activityID == CCAct.activityID) {
					//if (todos[i]._childActivities[k]._contributeEntries.length !=0){
						for (var j=0; j<todos[i]._childActivities[k]._contributeEntries.length; j++){ 
							trace("Contribute Entries for child "+todos[i]._childActivities[k].title+" is: "+todos[i]._childActivities[k]._contributeEntries[j]._contributionType)
						}
					//}
				//}
				
			}
	//	}
		
		
	}

	/**
	* Populate the required tasks for the active Sequence 
	
	private function populateContributeActivities():Void{
		// get contribute activities
		var todos:Array = mm.getToDos();
		trace('contrib. act length: ' + todos.length);
		// show isRequired activities in scrollpane
		for (var i=0; i<todos.length; i++){
			trace("_monitorReqTask_mc.Show Title"+todos[i].title)
			if (todos[i]._childActivities.length !=0){
				for (var j=0; j<todos[i]._childActivities.length; j++){
					trace(todos[i].title+"'s Child Activity "+j+" is: "+todos[i]._childActivities[j].title)
					populateCCActivities(todos[i]._childActivities[j])
				}
			}else {
				if (todos[i]._contributeEntries.length !=0){
					for (var j=0; j<todos[i]._contributeEntries.length; j++){ 
						trace("Contribute Entry for "+j+" is: "+todos[i]._contributeEntries[j]._contributionType)
					}
				}
			}
			//}
		}
		
		
	}
	*/
	private function populateContributeActivities():Void{
		var todos:Array = mm.getToDos();
		// show isRequired activities in scrollpane
		for (var i=0; i<todos.length; i++){
			trace('main CA title: ' + todos[i].title);
			getEntries(todos[i]);
		}
	}
	
	private function getEntries(ca:Object):Void{
		for (var i=0; i<ca.childActivities.length; i++){
			trace(ca.title+"'s Child Activity "+i+" is: "+ca.childActivities[i].title)
			getEntries(ca.childActivities[i]);
		}
		for (var j=0; j<ca.contributeEntries.length; j++){ 
			trace("Contribute Entry for "+ca.title+" is: "+ca.contributeEntries[j].contributionType)
			if(ca.contributeEntries[j].isRequired){
				// show isRequired entry
				
			}
		}
	}
	
	/**
    * Opens the lesson manager dialog
    */
    public function showLessonManagerDialog(mm:MonitorModel) {
		trace('doing Lesson Manager popup...');
		trace('app root: ' + mm.getMonitor().root);
		trace('lfwindow: ' + LFWindow);
        var dialog:MovieClip = PopUpManager.createPopUp(mm.getMonitor().root, LFWindow, true,{title:"TEST",closeButton:true,scrollContentPath:'selectClass'});
		dialog.addEventListener('contentLoaded',Delegate.create(_monitorController,_monitorController.openDialogLoaded));
		
    }
	
	/**
	 * 
	 * @usage   
	 * @param   newworkspaceDialog 
	 * @return  
	 */
	public function set lessonManagerDialog (newLessonManagerDialog:MovieClip):Void {
		_lessonManagerDialog = newLessonManagerDialog;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get lessonManagerDialog ():MovieClip {
		return _lessonManagerDialog;
	}
	
	public function setupLabels(){
		
		//max_lbl.text = Dictionary.getValue('pi_max_act');
		
		//populate the synch type combo:
		status_lbl.text = "Status:"
		learner_lbl.text = "Learners:"
		class_lbl.text = "Class:"
		elapsed_lbl.text = "Elapsed duration:"
		manageClass_lbl.text = "Class:"
		manageStatus_lbl.text = "Status:"
		manageStart_lbl.text = "Start:"
		manageMin_lbl.text = "Minutes"
		manageHour_lbl.text = "Hour"
		manageDate_lbl.text = "Date"
			
		//Button
		viewLearners_btn.label = "View Learners"
		editClass_btn.label = "Edit Class"
		selectClass_btn.label = "Select Class"
		status1_btn.label = "Archive"
		status2_btn.label = "Disable"
		setDateTime_btn.label = "Start Now"
		
		_lessonStateArr = ["CREATED", "NOT_STARTED", "STARTED", "SUSPENDED", "FINISHED", "ARCHIVED", "DISABLED"];
		//Call to apply style to all the labels and input fields
		//setStyles();
		delete this.onEnterFrame; 
			
	}
	
	/**
    * Sets the size of the canvas on stage, called from update
    */
	private function setSize(mm:MonitorModel):Void{
        var s:Object = mm.getSize();
		trace("Monitor Tab Widtht: "+s.w+" Monitor Tab Height: "+s.h);
		//reqTasks_scp.setSize(s.w,s.h);
		
				
	}
	

	/**
	 * Overrides method in abstract view to ensure cortect type of controller is returned
	 * @usage   
	 * @return  CanvasController
	 */
	public function getController():MonitorController{
		var c:Controller = super.getController();
		return MonitorController(c);
	}
	
	 /*
    * Returns the default controller for this view.
    */
    public function defaultController (model:Observable):Controller {
        return new MonitorController(model);
    }
}