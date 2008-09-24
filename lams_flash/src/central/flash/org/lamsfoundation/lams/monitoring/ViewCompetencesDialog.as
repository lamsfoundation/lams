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
class ViewCompetencesDialog extends MovieClip implements Dialog{
	
	public static var X_OFFSET:Number = -5;
	public static var Y_OFFSET:Number = 20;
	
	//References to components + clips 
    private var _container:MovieClip;  //The container window that holds the dialog
	
    private var close_btn:Button;         // Close window button
    private var competences_lbl:Label;
	private var panel:MovieClip;       //The underlaying panel base
	
	private var competences_scp:MovieClip;		// learners container
	private var _competences_mc:MovieClip;
	private var _competenceList:Array;
	
    private var fm:FocusManager;            //Reference to focus manager
    private var themeManager:ThemeManager;  //Theme manager
	
	private var app:Application;
	
	//Dimensions for resizing
    private var xCloseOffset:Number;
    private var yCloseOffset:Number;
	
	private var _lessonTabView:LessonTabView;
	private var _monitorModel:MonitorModel;
	private var _monitorView:MonitorView;
	private var _monitorController:MonitorController;

	private var competenceTitles:Array;
	
	//These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	
	 /**
    * constructor
    */
    function ViewCompetencesDialog(){
		//Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);
        
		Debugger.log("ViewCompetencesDialog->Constructor", Debugger.CRITICAL, "ViewCompetencesDialog", "ViewCompetencesDialog");
		
		app = Application.getInstance();
		
        //Create a clip that will wait a frame before dispatching init to give components time to setup
        //MovieClipUtils.doLater(Proxy.create(this,init));
		
		this.onEnterFrame = init;
	}
	
	/**
    * Called a frame after movie attached to allow components to initialise
    */
    private function init():Void{
		
		delete this.onEnterFrame;
		
        //set the reference to the StyleManager
        themeManager = ThemeManager.getInstance();
        
		Debugger.log("ViewCompetencesDialog->init", Debugger.CRITICAL, "init", "ViewCompetencesDialog");
		
		// Set the styles
        setStyles();
		
        //Set the text for buttons
		close_btn.label = Dictionary.getValue('ls_win_learners_close_btn');
		 
		 //Set the labels
		competences_lbl.text = "Competences in learning design: "+ app.monitor.ddm.title;
		
        //EVENTS
        //Add event listeners for ok, cancel and close buttons
        close_btn.addEventListener('click',Delegate.create(this, close));
        
		//Assign Click (close button) and resize handlers
        _container.addEventListener('click',this);
        _container.addEventListener('size',this);
        
        //work out offsets from bottom RHS of panel
        xCloseOffset = panel._width - close_btn._x;
        yCloseOffset = panel._height - close_btn._y;
		
		loadCompetences();
		
		//fire event to say we have loaded
		_container.contentLoaded();
    }
	
	/**
	 * Called by the worspaceView after the content has loaded
	 * @usage   
	 * @return  
	 */
	public function setUpContent():Void{
		
		 //Add event listeners for ok, cancel and close buttons
        close_btn.addEventListener('onPress',Delegate.create(this, close));
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
		competences_scp.setStyle('styleName', styleObj);

        //Get the button style from the style manager and apply to both buttons
        styleObj = themeManager.getStyleObject('button');
        close_btn.setStyle('styleName',styleObj);
        
        //Apply label style 
        styleObj = themeManager.getStyleObject('label');
		competences_lbl.setStyle('styleName', styleObj);
        
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
	
	public function loadCompetences():Void{
		Debugger.log("loadCompetences invoked", Debugger.GEN, "loadCompetences", "ViewCompetencesDialog");
		_competenceList = clearScp(_competenceList);
		_competences_mc = competences_scp.content;
		var competenceTitles:Array = app.monitor.ddm.competences.keys();
		
		competenceTitles.sort();
		Debugger.log("competenceTitles.length: "+competenceTitles.length, Debugger.GEN, "loadCompetences", "ViewCompetencesDialog");
		
		for (var i=0; i<competenceTitles.length; ++i) {
			var singleCompetence_mc:MovieClip = _competences_mc.attachMovie('competence_dataRow', 'competence_dataRow'+i, _competences_mc.getNextHighestDepth());
			singleCompetence_mc.title.text = competenceTitles[i];
			singleCompetence_mc._x = X_OFFSET;
			singleCompetence_mc._y = Y_OFFSET * i;
			_competenceList.push(singleCompetence_mc);
		}

		competences_scp.redraw(true);
	}
	
    /**
    * Main resize method, called by scrollpane container/parent
    */
    public function setSize(w:Number,h:Number):Void{
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
	
	public function get competenceList():Array{
		return _competenceList;
	}
	
}