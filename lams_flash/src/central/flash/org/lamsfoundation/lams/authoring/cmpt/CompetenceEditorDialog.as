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

import org.lamsfoundation.lams.common.*
import org.lamsfoundation.lams.common.ws.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.style.*
 
import org.lamsfoundation.lams.authoring.*
import org.lamsfoundation.lams.authoring.br.*

import org.lamsfoundation.lams.authoring.cmpt.CompetenceContainer;
import org.lamsfoundation.lams.authoring.cmpt.CompetenceDefinitionDialog;
import org.lamsfoundation.lams.authoring.cv.CanvasModel;
import org.lamsfoundation.lams.monitoring.mv.MonitorModel;

import mx.controls.*
import mx.utils.*
import mx.managers.*
import mx.events.*
import mx.containers.ScrollPane;

/*
* Competence Editor Dialog for defining and adding new competences
* @author  Daniel Carlier
*/
class org.lamsfoundation.lams.authoring.cmpt.CompetenceEditorDialog extends MovieClip implements Dialog {

    //References to components + clips 
	private var add_competence_btn:Button;
    private var close_btn:Button;        
	
	public var className:String = 'CompetenceEditorDialog';
	private var _editingCompetence:String; // The competence (if any) being edited
	
	private var competence_lbl:Label;
	
    private var _bgpanel:MovieClip;        //The underlaying panel base
	private var _container:MovieClip;  	//The container window that holds the dialog
	private var competenceContainer:MovieClip;
	private var main_mc:MovieClip
	private var competenceContainerLayer:MovieClip;
	
	private var containerArray:Array;
	
	private var competence_editor_scp:ScrollPane;
		
	private var app; // application instance
	private var model:Observable;
    private var fm:FocusManager;            //Reference to focus manager
    private var themeManager:ThemeManager;  //Theme manager
	
    //Dimensions for resizing
    private var xOkOffset:Number;
    private var yOkOffset:Number;
	private var selectedActivityUIID:Number
	private var yContainerOffset:Number;

    //These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
    
    /**
    * constructor
    */
    function CompetenceEditorDialog(){
        //Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);
		
        //Create a clip that will wait a frame before dispatching init to give components time to setup
		this.onEnterFrame = init;
    }

    /**
    * Called a frame after movie attached to allow components to initialise
    */
    private function init():Void{
        //Delete the enterframe dispatcher
        delete this.onEnterFrame;
        
		this._visible = false;
        
		//set the reference to the StyleManager
        themeManager = ThemeManager.getInstance();
		
        //Set the text for buttons
		competence_lbl.text = Dictionary.getValue('competences_lbl');
		
		add_competence_btn.label = Dictionary.getValue('competence_editor_add_competence_btn');
        close_btn.label = (Dictionary.getValue('mnu_file_close') != "?") ? Dictionary.getValue('mnu_file_close') : Dictionary.getValue('ls_win_learners_close_btn');
			
		main_mc = competence_editor_scp.content;
		
		// add new layer here on top of main_mc and then remove it whenever we need to redraw the container contents
		competenceContainerLayer = main_mc.createEmptyMovieClip("competenceContainerLayer", main_mc.getNextHighestDepth());
		
        //get focus manager + set focus to OK button, focus manager is available to all components through getFocusManager
        fm = _container.getFocusManager();
        
        //EVENTS
        //Add event listeners for ok, cancel and close buttons
		add_competence_btn.addEventListener('click',Delegate.create(this, openCompetenceDefinitionDialog));
        close_btn.addEventListener('click',Delegate.create(this, close));
		
		//Assign Click (close button) and resize handlers
        _container.addEventListener('click',this);
        _container.addEventListener('size',this);
        
		//work out offsets from bottom RHS of panel
        xOkOffset = _bgpanel._width - close_btn._x;
        yOkOffset = _bgpanel._height - close_btn._y;
        
        //Register as listener with StyleManager and set Styles
        themeManager.addEventListener('themeChanged',this);
        setStyles();
		 
        //fire event to say we have loaded
		_container.contentLoaded();
	}
	
    public function setUpContent(model:Observable):Void{
		
		setModel(model);
		
		if (model instanceof CanvasModel) {
			app = org.lamsfoundation.lams.authoring.Application.getInstance();
		} else if (model instanceof MonitorModel){
			app = org.lamsfoundation.lams.monitoring.Application.getInstance();
			
			selectedActivityUIID = app.monitor.model.selectedItem.activity.activityUIID;
			if (app.monitor.ddm.activities.get(selectedActivityUIID) instanceof ToolActivity) {
				var activityTitle:String = ToolActivity(app.monitor.ddm.activities.get(selectedActivityUIID)).title;
				competence_lbl.text = Dictionary.getValue("competences_mapped_to_act_lbl", [activityTitle]);
			}
		} else {
			Debugger.log("model type not recognized", Debugger.CRITICAL, "setUpContent", "CompetenceEditorDialog");
			// other model types not currently supported
		}

		draw(model);
		this._visible = true;
	}
	
	public function clear():Void {
		competenceContainerLayer.removeMovieClip();
		competenceContainerLayer = main_mc.createEmptyMovieClip("competenceContainerLayer", main_mc.getNextHighestDepth());
	}
	
	/*
	* If we are in author the competence keys correspond to all competences in the ddm
	* If we are in monitor the competence keys correspond to the competences mapped to the selected activity
	* @return competenceKeys an array of the competence keys to draw
	*/
	private function getCompetenceKeysToDraw():Array {
		
		var competenceKeys:Array;
		
		if (model instanceof MonitorModel) {			
	
			if (app.monitor.ddm.activities.get(selectedActivityUIID) instanceof ToolActivity) {
				competenceKeys = ToolActivity(app.monitor.ddm.activities.get(selectedActivityUIID)).competenceMappings;
			}
		} else if (model instanceof CanvasModel) { // CanvasModel
			competenceKeys = app.getDesignDataModel().competences.keys();
		}
		return competenceKeys;
	}
	
	public function draw(m:Observable) {
		
		var competenceKeys:Array = getCompetenceKeysToDraw();
		competenceKeys.sort();
		
		yContainerOffset = 0;
		containerArray = new Array();
		
		for (var i=0; i<competenceKeys.length; i++) {
			
			var titleKey:String = competenceKeys[i];		
			var descriptionValue:String = (model instanceof MonitorModel) ? app.monitor.ddm.competences.get(competenceKeys[i]) : app.getDesignDataModel().competences.get(competenceKeys[i]);
			var competenceTitle:String = new String("competenceContainer_"+titleKey);
			var mc:MovieClip = competenceContainerLayer.attachMovie("competenceContainer", competenceTitle, competenceContainerLayer.getNextHighestDepth(), {_x:0, _y:yContainerOffset, _competenceTitle:titleKey, _description:descriptionValue, _model:m});
			
			containerArray.push(mc);
			yContainerOffset = yContainerOffset + 120;
		}
		
		MovieClipUtils.doLater(Delegate.create(this, resizeContainers));
	}
	
	public function resizeContainers() {		for (var i=0; i<containerArray.length; ++i) {
			CompetenceContainer(containerArray[i]).setSize(this._width, this._height);
		}
		
		competence_editor_scp.invalidate();
	}
	
	// The competence to be edited in the CompetenceDefinitionDialog
	public function get editingCompetence():String {
		return _editingCompetence;
	}
	
	public function set editingCompetence(ec:String):Void {
		_editingCompetence = ec;
	}
	
	public function openCompetenceDefinitionDialog():Void{
		
		_editingCompetence = null;
		
		app.getCanvas().openCompetenceDefinitionWindow(); // sets app.dialog to the definition window
		Selection.setFocus(app.dialog.scrollContent);
	}

	/**
    * Event fired by StyleManager class to notify listeners that Theme has changed
    * it is up to listeners to then query Style Manager for relevant style info
    */
    public function themeChanged(evt:Object):Void{
        if(evt.type=='themeChanged') {
            //Theme has changed so update objects to reflect new styles
            setStyles();
        }else {
            Debugger.log('themeChanged event broadcast with an object.type not equal to "themeChanged"',Debugger.CRITICAL,'themeChanged','CompetenceEditorDialog');
        }
    }
	
    /**
    * Called on initialisation and themeChanged event handler
    */
    private function setStyles(){
        //LFWindow, goes first to prevent being overwritten with inherited styles.
        var styleObj = themeManager.getStyleObject('LFWindow');
        _container.setStyle('styleName', styleObj);

		styleObj = themeManager.getStyleObject('scrollpane');
		competence_editor_scp.setStyle('styleName', styleObj);

        //Get the button style from the style manager and apply to both buttons
        styleObj = themeManager.getStyleObject('button');
		add_competence_btn.setStyle('styleName', styleObj);
        close_btn.setStyle('styleName', styleObj);
       
		styleObj = themeManager.getStyleObject('CanvasPanel');
		_bgpanel.setStyle('styleName', styleObj);
		
        //Apply label style 
        styleObj = themeManager.getStyleObject('label');
        competence_lbl.setStyle('styleName', styleObj);
    }
	
    /**
    * Called by the close button 
    */
    private function close(){
		if (model instanceof CanvasModel)
			app.canvas.model.competenceEditorDialog = null;
		
		_container.deletePopUp();
    }
	
    /**
    * Event dispatched by parent container when close button clicked
    */
    public function click(e:Object):Void{
		if (model instanceof CanvasModel)
			app.canvas.model.competenceEditorDialog = null;
        
		e.target.deletePopUp();
    }
	
	/**
    * set model to either 'authoring' or 'monitoring'
    */
	public function setModel(m:Observable) {
		model = m;
	}
	
    /**
    * Main resize method, called by scrollpane container/parent
    */
    public function setSize(w:Number,h:Number):Void{
		//Size the panel
        _bgpanel.setSize(w,h);

		competence_editor_scp.setSize(w-45, h-82);
		
		for (var i=0; i<containerArray.length; ++i) {
			CompetenceContainer(containerArray[i]).setSize(w,h);
		}
		
        //Buttons
		add_competence_btn.move(add_competence_btn._x,h-yOkOffset);
        close_btn.move(w-xOkOffset,h-yOkOffset);
		
    }
    
    //Gets+Sets
    /**
    * set the container refernce to the window holding the dialog
    */
    function set container(value:MovieClip){
        _container = value;
    }

}