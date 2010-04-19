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
import mx.controls.gridclasses.DataGridColumn;
import mx.utils.*
import mx.managers.*
import mx.events.*

import org.lamsfoundation.lams.common.ws.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.common.*
import org.lamsfoundation.lams.authoring.Activity;
import org.lamsfoundation.lams.monitoring.*
import org.lamsfoundation.lams.monitoring.mv.*
import org.lamsfoundation.lams.monitoring.mv.tabviews.*

/*
* Lesson Manager Dialog window for selecting an organisation class
* @author  DI
*/
class LearnersDialog extends MovieClip implements Dialog{
	
	//References to components + clips 
    private var _container:MovieClip;  //The container window that holds the dialog
	
    private var close_btn:Button;         // Close window button
	private var view_btn:Button;
	
    private var learners_lbl:Label;
	private var panel:MovieClip;       //The underlaying panel base
	
	private var learner_dgd:DataGrid;
	
    private var fm:FocusManager;            //Reference to focus manager
    private var themeManager:ThemeManager;  //Theme manager
	
	//Dimensions for resizing
    private var xCloseOffset:Number;
    private var yCloseOffset:Number;
	
	private var learner_dgd_Xoffset:Number;
	private var learner_dgd_Yoffset:Number;
	
	private var _monitorModel:MonitorModel;
	private var _monitorView;
	private var _monitorController:MonitorController;
	
	private var currentActivity:Activity;
	
	//These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	
	/**
    * constructor
    */
    function LearnersDialog(){
		//Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);
        
        //Create a clip that will wait a frame before dispatching init to give components time to setup
        MovieClipUtils.doLater(Proxy.create(this, init));
	}
	
	/**
    * Called a frame after movie attached to allow components to initialise
    */
    private function init():Void{
        //set the reference to the StyleManager
        themeManager = ThemeManager.getInstance();
        
		// Set the styles
        setStyles();
		
        //Set the text for buttons
		close_btn.label = Dictionary.getValue('ls_win_learners_close_btn');
		view_btn.label = "View Learner"; //Dictionary.getValue('ls_win_learners_view_btn');
		
		view_btn._visible = false;
		view_btn.enabled = false;
        //Set the labels
		
        //EVENTS
        //Add event listeners for ok, cancel and close buttons
        close_btn.addEventListener('click', Delegate.create(this, close));
        view_btn.addEventListener('click', Delegate.create(this, viewLearner));
		learner_dgd.addEventListener('cellPress', Delegate.create(this, cellPress));
		
		//Assign Click (close button) and resize handlers
        _container.addEventListener('click',this);
        _container.addEventListener('size',this);
        
        //work out offsets from bottom RHS of panel
        xCloseOffset = panel._width - close_btn._x;
        yCloseOffset = panel._height - close_btn._y;
		
		learner_dgd_Xoffset = learner_dgd._width/panel._width;
		learner_dgd_Yoffset = learner_dgd._height/panel._height;
		
		//fire event to say we have loaded
		_container.contentLoaded();
    }
	
	/**
	 * Called by the worspaceView after the content has loaded
	 * @usage   
	 * @return  
	 */
	public function setUpContent(mm:MonitorModel, _currentActivity:Activity):Void{
		//get a ref to the controller and kkep it here to listen for events:
		_monitorController = (_monitorView instanceof LessonTabView) ? LessonTabView(_monitorView).getController() : MonitorTabView(_monitorView).getController();
		_monitorModel = mm;
		
		currentActivity = (_currentActivity != null || _currentActivity != undefined) ? _currentActivity : null;
		
		learners_lbl.text = (currentActivity != null) ? Dictionary.getValue('ls_win_learners_heading_lbl', [Dictionary.getValue('ls_win_learners_heading_activity_lbl'), currentActivity.title]) : Dictionary.getValue('ls_win_learners_heading_lbl', [Dictionary.getValue('ls_win_learners_heading_class_lbl'), _monitorModel.getSequence().organisationName]);
		learners_lbl.autoSize = "left";
		
		 //Add event listeners for ok, cancel and close buttons
        close_btn.addEventListener('onPress', Delegate.create(this, close));
		view_btn._visible = (currentActivity != null);
		
		if(currentActivity == null) {
			var callback:Function = Proxy.create(this, loadLearners);
			Application.getInstance().getComms().getRequest('monitoring/monitoring.do?method=getLessonLearners&lessonID='+_root.lessonID, callback, false);
		} else {
			// get users from progress details
			loadLearners(_monitorModel.allLearnersProgress);
		}
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
		styleObj = themeManager.getStyleObject('datagrid');
		learner_dgd.setStyle('styleName', styleObj);

        //Get the button style from the style manager and apply to both buttons
        styleObj = themeManager.getStyleObject('button');
        close_btn.setStyle('styleName',styleObj);
		view_btn.setStyle('styleName', styleObj);
        
        //Apply label style 
        styleObj = themeManager.getStyleObject('label');
		learners_lbl.setStyle('styleName', styleObj);
        
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
    * Called by the close button 
    */
    private function close(){
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
	public function set monitorView (newMonitorView):Void {
		_monitorView = newMonitorView;
	}
	
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get monitorView () {
		return _monitorView;
	}
	
    /**
    * Event dispatched by parent container when close button clicked
    */
    public function click(e:Object):Void{
        e.target.deletePopUp();
    }

	/*
	* Clear Method to clear movies from scrollpane
	* 
	*/
	public function clearDatagrid():Void{
		learner_dgd.removeAll();
	}
	

	/**
	 * Load learners into scrollpane
	 * @param   users Users to load
	 */
	
	public function loadLearners(users:Array):Void{
		Debugger.log("loadLearners invoked", Debugger.GEN, "loadLearners", "LearnersDialog");
		Debugger.log("currentActivity: " + currentActivity, Debugger.CRITICAL, "loadLearners", "LearnersDialog");
		Debugger.log("users: " + users.length, Debugger.CRITICAL, "loadLearners", "LearnersDialog");
		
		clearDatagrid();
		
		var learner_dgd_col:DataGridColumn = new DataGridColumn;
		learner_dgd_col.headerText = "Learners";
		
		var usersArr:Array = new Array(); // contains User objects
		
		if(currentActivity == null) {
			for(var i=0; i<users.length; i++){
				var user = new User(users[i]);
				usersArr.push(user);
			}
			
			usersArr.sortOn(["_firstName", "_lastName"], Array.CASEINSENSITIVE);
			
			learner_dgd_col.labelFunction = function(user) {
				return (user != null) ? user.getFirstName() + " " + user.getLastName() + " (" + user.getUsername() + ")" : null;
			};
			
		} else {
			
			for(var i=0; i<users.length; i++) {
				if(Progress.isLearnerCurrentActivity(Progress(users[i]), currentActivity.activityID))
					usersArr.push(Progress(users[i]));
			}
			
			usersArr.sortOn(["_learnerFName", "_learnerLName"], Array.CASEINSENSITIVE);
			
			learner_dgd_col.labelFunction = function(user) {
				return (user != null) ? user.getLearnerFirstName() + " " + user.getLearnerLastName() + " (" + user.getUserName() + ")" : null;
			}
		}
		
		learner_dgd.addColumn(learner_dgd_col);
		learner_dgd.dataProvider = usersArr;
		
	}
	
	public function viewLearner():Void {
		if(currentActivity != null) {
			var learner:Progress = Progress(learner_dgd.selectedItem);
			if(learner != null) {
				var URLToSend = _root.serverURL+_root.monitoringURL+'getLearnerActivityURL&activityID='+currentActivity.activityID+'&userID='+learner.getLearnerId()+'&lessonID='+_root.lessonID;
				JsPopup.getInstance().launchPopupWindow(URLToSend, 'MonitorLearnerActivity', 600, 800, true, true, true, false, false);
			}
		}
	}
    /**
    * Main resize method, called by scrollpane container/parent
    */
    public function setSize(w:Number,h:Number):Void{
        //Size the panel
        panel.setSize(w,h);
		learner_dgd.setSize(Math.round(w*learner_dgd_Xoffset), Math.round(h*learner_dgd_Yoffset));

        //Buttons
        close_btn.move(w-xCloseOffset,h-yCloseOffset);
		view_btn.move(close_btn._x - view_btn._width - 15, close_btn._y);
		
    }
	
	private function cellPress(evtObj):Void {
		view_btn.enabled = (learner_dgd.selectedItem != null);
	}
    
    /**
    * set the container refernce to the window holding the dialog
    */
    function set container(value:MovieClip){
        _container = value;
    }
	
}