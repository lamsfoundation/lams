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
import org.lamsfoundation.lams.authoring.cmpt.*;
 
import mx.controls.*
import mx.utils.*
import mx.managers.*
import mx.events.*

/*
* Competence Container representing a single competence within the competence editor dialog
* @author  Daniel Carlier
*/
class org.lamsfoundation.lams.authoring.cmpt.CompetenceContainer extends MovieClip {

    //References to components + clips 
    private var _container:MovieClip;  //The container window that holds the dialog
	
	private var _competenceTitle:String;
	private var _description:String;
	
	private var competence_title_lbl:Label;
	private var competence_description_lbl:Label;
	
	private var competence_title_txt:TextField;
	private var competence_description_txt:TextArea;
	
	private var edit_competence_btn:Button;
	private var delete_competence_btn:Button;
	
    private var _bgpanel:MovieClip;       //The underlaying panel base
	private var definitionDialog:MovieClip;
	
	private var app:Application;
    private var fm:FocusManager;            //Reference to focus manager
    private var themeManager:ThemeManager;  //Theme manager
	
    //Dimensions for resizing
    private var xOkOffset:Number;
    private var yOkOffset:Number;
    private var xCancelOffset:Number;
    private var yCancelOffset:Number;

    //These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
    
    /**
    * constructor
    */
    function CompetenceContainer(){
        //Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);
		
		app = Application.getInstance();
		
        //Create a clip that will wait a frame before dispatching init to give components time to setup
        this.onEnterFrame = init;
    }

    /**
    * Called a frame after movie attached to allow components to initialise
    */
    private function init():Void{
        //Delete the enterframe dispatcher
        delete this.onEnterFrame;
        
        //set the reference to the StyleManager
        themeManager = ThemeManager.getInstance();
		
		//dictionary keys vary for author/monitor and this class is shared between them;
		competence_title_lbl.text = (Dictionary.getValue("pi_lbl_title") != null && Dictionary.getValue("pi_lbl_title") != undefined) ? Dictionary.getValue("pi_lbl_title") : Dictionary.getValue("competence_title_lbl");
		competence_description_lbl.text = (Dictionary.getValue("ws_dlg_description") != null && Dictionary.getValue("ws_dlg_description") != undefined) ? Dictionary.getValue("ws_dlg_description") : Dictionary.getValue("competence_desc_lbl");
		
		// author only buttons
		edit_competence_btn.label = Dictionary.getValue("mnu_edit"); 
		delete_competence_btn.label = Dictionary.getValue("delete_btn"); 
		
		competence_title_txt.editable = false;
		competence_description_txt.editable = false;
		
		if (_competenceTitle != null) competence_title_txt.text = _competenceTitle;
		if (_description != null) competence_description_txt.text = _description;
		
        //get focus manager + set focus to OK button, focus manager is available to all components through getFocusManager
        fm = _container.getFocusManager();
        fm.enabled = true;
        
        //EVENTS
        //Add event listeners for ok, cancel and close buttons
		edit_competence_btn.addEventListener('click',Delegate.create(this, editHandler));
        delete_competence_btn.addEventListener('click',Delegate.create(this, deleteHandler));
		
        //work out offsets from bottom RHS of panel
        xOkOffset = _bgpanel._width;// - close_btn._x;
        yOkOffset = _bgpanel._height;// - close_btn._y;
        
        //Register as listener with StyleManager and set Styles
        themeManager.addEventListener('themeChanged',this);
        setStyles();
		
        //fire event to say we have loaded
		_container.contentLoaded();
	}
    
	public function setUpContent():Void{
		this._visible = true;
	}
	
	public function editHandler():Void {
		Debugger.log("editCompetence clicked: _competenceTitle "+_competenceTitle, Debugger.CRITICAL, "editHandler", "CompetenceContainer");
		CompetenceEditorDialog(app.dialog.scrollContent).editingCompetence = _competenceTitle;
		
		definitionDialog = app.getCanvas().openCompetenceDefinitionWindow();
		Selection.setFocus(definitionDialog.scrollContent);
	}
	
	public function deleteHandler():Void {
		Debugger.log("delete button clicked", Debugger.CRITICAL, "editCompetence", "CompetenceContainer");
		
		var _isMapped:Boolean = isCompetenceMapped();
		
		if (_isMapped) {
			LFMessage.showMessageConfirm(Dictionary.getValue("competence_editor_warning_competence_mapped"), null);
		} else {
			deleteCompetence();
		}
		
	}
	
	/*
	* Returns true if the competence is mapped to an activity
	*/
	private function isCompetenceMapped():Boolean {
		
		var activityKeys = app.getDesignDataModel().activities.keys();
		
		for (var i=0; i<activityKeys.length; i++) {
			if (app.getDesignDataModel().activities.get(activityKeys[i]) instanceof ToolActivity) {
				var competenceMappings:Array = ToolActivity(app.getDesignDataModel().activities.get(activityKeys[i])).competenceMappings;
				for (var j=0; j<competenceMappings.length; j++) {
					if (competenceMappings[j] == _competenceTitle)
						return true;
				}
			}
		}
		return false;
	}
	
	/*
	* Deletes competence mappings to where an activity is mapped to the competence
	*/
	private function deleteMappedCompetences(competenceTitle:String) {
		
		// remove applied mappings
		var activityKeys = app.getDesignDataModel().activities.keys();
				
		for (var i=0; i<activityKeys.length; i++) {
			
			Debugger.log("app.getDesignDataModel().activities.get(activityKeys[i]) instanceof ToolActivity: "+(app.getDesignDataModel().activities.get(activityKeys[i]) instanceof ToolActivity), Debugger.CRITICAL, "deleteMappedCompetences", "CompetenceContainer");
			if (app.getDesignDataModel().activities.get(activityKeys[i]) instanceof ToolActivity) {
				var competenceMappings:Array = ToolActivity(app.getDesignDataModel().activities.get(activityKeys[i])).competenceMappings;
				for (var j=0; j<competenceMappings.length; j++) {
					if (competenceMappings[j] == competenceTitle) {
						competenceMappings.splice(j,1); // remove the competence mapping from the activity
					}
				}
			}
		}
		deleteCompetence();
	}
	
	
	private function deleteCompetence() {
		// app.dialog.scrollContent must be an instance of CompetenceEditorDialog
		var competenceEditorDialog:CompetenceEditorDialog = CompetenceEditorDialog(app.dialog.scrollContent);
		app.getDesignDataModel().competences.remove(_competenceTitle);
			
		competenceEditorDialog.clear();
		MovieClipUtils.doLater(Proxy.create(competenceEditorDialog, competenceEditorDialog.draw));
		
		this.removeMovieClip();
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
            Debugger.log('themeChanged event broadcast with an object.type not equal to "themeChanged"',Debugger.CRITICAL,'themeChanged','CompetenceDefinitionDialog');
        }
    }
	
    /**
    * Called on initialisation and themeChanged event handler
    */
    private function setStyles(){
        //LFWindow, goes first to prevent being overwritten with inherited styles.
        var styleObj = themeManager.getStyleObject('LFWindow');
        _container.setStyle('styleName', styleObj);

        //Get the button style from the style manager and apply to both buttons
        styleObj = themeManager.getStyleObject('button');
		edit_competence_btn.setStyle('styleName', styleObj);
        delete_competence_btn.setStyle('styleName', styleObj);
       
		styleObj = themeManager.getStyleObject('CanvasPanel');
		_bgpanel.setStyle('styleName', styleObj);

        //Apply label style 
        styleObj = themeManager.getStyleObject('label');
        competence_title_lbl.setStyle('styleName', styleObj);
        competence_description_lbl.setStyle('styleName', styleObj);
		competence_title_txt.setStyle('styleName', styleObj);
		competence_description_txt.setStyle('styleName', styleObj);
    }
	
    /**
    * Called by the OK button 
    */
    private function close(){

		_container.deletePopUp();
    }
	
    /**
    * Event dispatched by parent container when close button clicked
    */
    public function click(e:Object):Void{
        e.target.deletePopUp();
    }
    
    /**
    * Main resize method, called by scrollpane container/parent
    */
    public function setSize(w:Number,h:Number):Void{
		//Size the panel
        _bgpanel.setSize(w,h);
		
    }
    
    //Gets+Sets
    /**
    * set the container refernce to the window holding the dialog
    */
    function set container(value:MovieClip){
        _container = value;
    }

}