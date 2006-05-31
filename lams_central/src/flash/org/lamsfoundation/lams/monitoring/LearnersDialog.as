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
class LearnersDialog extends MovieClip implements Dialog{
	
	
	public var RT_ORG:String = "Organisation";
	public static var USERS_X:Number = -5;
	public static var USER_OFFSET:Number = 20;
	
	//References to components + clips 
    private var _container:MovieClip;  //The container window that holds the dialog
	
    private var close_btn:Button;         // Close window button
    private var learners_txt:TextField;
	private var panel:MovieClip;       //The underlaying panel base
	
	private var learner_scp:MovieClip;		// learners container
	private var _learner_mc:MovieClip;
	
    private var fm:FocusManager;            //Reference to focus manager
    private var themeManager:ThemeManager;  //Theme manager
	
	//Dimensions for resizing
    private var xCloseOffset:Number;
    private var yCloseOffset:Number;
	
	private var _lessonTabView:LessonTabView;
	private var _monitorModel:MonitorModel;
	private var _monitorView:MonitorView;
	private var _monitorController:MonitorController;

	private var _learnerList:Array;
	
	//These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	
	 /**
    * constructor
    */
    function LearnersDialog(){
		trace('initialising Lesson Manager Dialog');
        //Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);
        
        //Create a clip that will wait a frame before dispatching init to give components time to setup
        MovieClipUtils.doLater(Proxy.create(this,init));
	}
	
	/**
    * Called a frame after movie attached to allow components to initialise
    */
    private function init():Void{
        
        trace('now initialising ...');
        //set the reference to the StyleManager
        themeManager = ThemeManager.getInstance();
        
		// Set the styles
        setStyles();
		
        //Set the text for buttons
		close_btn.label = "Close";
		
        //Set the labels
		
        //EVENTS
        //Add event listeners for ok, cancel and close buttons
        close_btn.addEventListener('click',Delegate.create(this, close));
        //Assign Click (close button) and resize handlers
        _container.addEventListener('click',this);
        _container.addEventListener('size',this);
        
        //work out offsets from bottom RHS of panel
        xCloseOffset = panel._width - close_btn._x;
        yCloseOffset = panel._height - close_btn._y;
		
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
		
		Debugger.log('_monitorView:'+_monitorView,Debugger.GEN,'setUpContent','org.lamsfoundation.lams.LearnersDialog');
		
		//get a ref to the controller and kkep it here to listen for events:
		_monitorController = _monitorView.getController();
		_monitorModel = MonitorModel(_monitorView.getModel());
		
		learners_txt.text = "Learners in class: " + _monitorModel.getSequence().organisationName;
		
		 //Add event listeners for ok, cancel and close buttons
        close_btn.addEventListener('onPress',Delegate.create(this, close));
		
		var callback:Function = Proxy.create(this,loadLearners);
		Application.getInstance().getComms().getRequest('monitoring/monitoring.do?method=getLessonLearners&lessonID='+_monitorModel.getSequence().ID,callback, false);
		
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
        close_btn.setStyle('styleName',styleObj);
        
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
    * Called by the close button 
    */
    private function close(){
        trace('Close');
        //close parent window
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
		_learnerList = clearScp(_learnerList);
		_learner_mc = learner_scp.content;

		trace('list length: ' + users.length);
		for(var i=0; i<users.length; i++){
			var user:User = new User(users[i]);
			trace(_learner_mc);
			_learnerList[i] = this._learner_mc.attachMovie('staff_learner_dataRow', 'staff_learner_dataRow' + i, this._learner_mc.getNextHighestDepth());
			_learnerList[i].fullName.text = user.getFirstName() + " " + user.getLastName() + " (" + user.getUsername() + ")";
			_learnerList[i]._x = USERS_X;
			_learnerList[i]._y = USER_OFFSET * i;
			var listItem:MovieClip = MovieClip(_learnerList[i]);
			//listItem.attachMovie('CheckBox', 'user_cb', listItem.getNextHighestDepth(), {_x:0, _y:3, selected:true})
			trace('new row: ' + _learnerList[i]);
			trace('loading: user ' + user.getFirstName() + ' ' + user.getLastName());

		}
		learner_scp.redraw(true);
	}
	
    /**
    * Main resize method, called by scrollpane container/parent
    */
    public function setSize(w:Number,h:Number):Void{
        //Debugger.log('setSize',Debugger.GEN,'setSize','org.lamsfoundation.lams.common.ws.WorkspaceDialog');
        //Size the panel
        panel.setSize(w,h);

        //Buttons
        close_btn.move(w-xCloseOffset,h-yCloseOffset);
		
    }
    
    /**
    * set the container refernce to the window holding the dialog
    */
    function set container(value:MovieClip){
        _container = value;
    }
	
	public function get learnerList():Array{
		return _learnerList;
	}
	
}