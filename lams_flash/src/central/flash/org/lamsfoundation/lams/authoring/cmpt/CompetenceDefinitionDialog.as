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
import org.lamsfoundation.lams.authoring.cmpt.CompetenceEditorDialog;
 
import mx.controls.*
import mx.utils.*
import mx.managers.*
import mx.events.*

/*
* Competence Definition Dialog for defining/editing and adding new competences
* @author  Daniel Carlier
*/
class org.lamsfoundation.lams.authoring.cmpt.CompetenceDefinitionDialog extends MovieClip implements Dialog {

    //References to components + clips 
    private var _container:MovieClip;  //The container window that holds the dialog
	
	private var add_competence_btn:Button;
	private var save_competence_btn:Button;
    private var close_btn:Button;         // Close button
    
	private var competence_title_lbl:Label;
	private var competence_description_lbl:Label;
	
	private var competence_title_txt:TextField;
	private var competence_description_txt:TextArea;
	
    private var _bgpanel:MovieClip;       //The underlaying panel base
	
	private var editingCompetence:String; // The competence (if any) being edited
	
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
    function CompetenceDefinitionDialog(){
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
        
		fm = _container.getFocusManager();
		
        //Set the text for buttons
        close_btn.label = Dictionary.getValue("prefs_dlg_cancel");
		
		competence_title_lbl.text = Dictionary.getValue("competence_title_lbl");
		competence_description_lbl.text = Dictionary.getValue("competence_desc_lbl");
		
		add_competence_btn.label = Dictionary.getValue("competence_editor_add_competence_btn");
		save_competence_btn.label = Dictionary.getValue("mnu_file_save");
		
		editingCompetence = CompetenceEditorDialog(app.dialog.scrollContent).editingCompetence;
		
        //EVENTS
        //Add event listeners for ok, cancel and close buttons
		add_competence_btn.addEventListener('click', Delegate.create(this, addNewCompetence));
		save_competence_btn.addEventListener('click', Delegate.create(this, updateCompetence));
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
		setTabIndex();

        //fire event to say we have loaded
		_container.contentLoaded();
	}
	
	private function setTabIndex() {
		competence_title_txt.tabIndex = 501;
		competence_description_txt.tabIndex = 502;
		
		add_competence_btn.tabIndex = 503;
		save_competence_btn.tabIndex = 504;
		close_btn.tabIndex = 505;
	}
	
	public function setUpContent():Void{
		
		if (editingCompetence != null && editingCompetence != undefined) { // if we are editing an existing competence
			add_competence_btn.visible = false;
			save_competence_btn.visible = true;
			competence_title_txt.text = editingCompetence;
			competence_description_txt.text = app.getDesignDataModel().competences.get(editingCompetence);
		} else {
			add_competence_btn.visible = true;
			save_competence_btn.visible = false;
			Selection.setFocus(competence_title_txt);
		}
		
		this._visible = true;
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
		add_competence_btn.setStyle('styleName', styleObj);
		save_competence_btn.setStyle('styleName', styleObj);
        close_btn.setStyle('styleName', styleObj);
       
		styleObj = themeManager.getStyleObject('CanvasPanel');
		_bgpanel.setStyle('styleName', styleObj);
		
        //Apply label style 
        styleObj = themeManager.getStyleObject('label');
        competence_title_lbl.setStyle('styleName', styleObj);
        competence_description_lbl.setStyle('styleName', styleObj);
		competence_title_txt.setStyle('styleName', styleObj);
		competence_description_txt.setStyle('styleName', styleObj);
    }
	
	private function addNewCompetence():Void {
		var competenceName:String = StringUtils.trim(competence_title_txt.text);
		var competenceDesc:String = StringUtils.trim(competence_description_txt.text);
		
		if (competenceName.length == 0) {
			LFMessage.showMessageAlert(Dictionary.getValue("competence_editor_warning_title_blank"), null);
			
		}
		else if (app.getDesignDataModel().competences.containsKey(competenceName)) {
			// entry already exists, do not add
			LFMessage.showMessageAlert(Dictionary.getValue("competence_editor_warning_title_exists", [competenceName]), null);
		} 
		else {
			// add the new competence to the hashtable			app.getDesignDataModel().competences.put(competenceName, competenceDesc);

			CompetenceEditorDialog(app.dialog.scrollContent).clear();
			CompetenceEditorDialog(app.dialog.scrollContent).draw();
		}
		
		_container.deletePopUp();
	}
	
	// used for updating an existing competence
	private function updateCompetence():Void {
		
		var updatedCompetenceName:String = StringUtils.trim(competence_title_txt.text);
		var updatedCompetenceDesc:String = StringUtils.trim(competence_description_txt.text);
		
		if (editingCompetence != null && editingCompetence != undefined && app.getDesignDataModel().competences.containsKey(editingCompetence)) {
			if (updatedCompetenceName.length > 0) {
				app.getDesignDataModel().competences.remove(editingCompetence);  // remove the original competencetitle
				app.getDesignDataModel().competences.put(updatedCompetenceName, updatedCompetenceDesc);
	
				// if the competence title has changed, update the mappings for all activities to the new competence title
				if (editingCompetence != updatedCompetenceName) { 
					
					var activityKeys:Array = app.getDesignDataModel().activities.keys(); // contains the activity UIIDs
					
					for (var i=0; i<activityKeys.length; ++i) {

						if (app.getDesignDataModel().activities.get(activityKeys[i]) instanceof ToolActivity) {
							var competenceMappings:Array = ToolActivity(app.getDesignDataModel().activities.get(activityKeys[i])).competenceMappings;
							for (var j=0; j<competenceMappings.length; ++j) {
								if (competenceMappings[j] == editingCompetence) {
									competenceMappings[j] = updatedCompetenceName;
								}
							}
						}
					}
				}
				
				CompetenceEditorDialog(app.dialog.scrollContent).clear();
				CompetenceEditorDialog(app.dialog.scrollContent).draw();
			}
			else {
				LFMessage.showMessageAlert(Dictionary.getValue("competence_editor_warning_title_blank"), null);
			}
		} 
		
		_container.deletePopUp();
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

        //Buttons
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