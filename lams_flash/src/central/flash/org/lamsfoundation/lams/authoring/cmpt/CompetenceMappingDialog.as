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

import org.lamsfoundation.lams.authoring.cmpt.CompetenceMappingItem;

import mx.controls.*
import mx.utils.*
import mx.managers.*
import mx.events.*
import mx.containers.ScrollPane;

/*
* Competence Mapping Dialog for mapping competences to activities
* @author  Daniel Carlier
*/
class org.lamsfoundation.lams.authoring.cmpt.CompetenceMappingDialog extends MovieClip implements Dialog {

    //References to components + clips 
    private var close_btn:Button;        
	
	public var className:String = 'CompetenceMappingDialog';
    
	private var competence_mapping_lbl:Label;
	
    private var _bgpanel:MovieClip;        //The underlaying panel base
	private var _container:MovieClip;  	//The container window that holds the dialog
	private var main_mc:MovieClip
	private var competenceContainerLayer:MovieClip;
	
	private var competence_mapping_scp:ScrollPane;
		
	private var competenceItemMCs:Array;	
		
	private var app:Application;
    private var fm:FocusManager;            //Reference to focus manager
    private var themeManager:ThemeManager;  //Theme manager
	
    //Dimensions for resizing
    private var xOkOffset:Number;
    private var yOkOffset:Number;
    private var xCancelOffset:Number;
    private var yCancelOffset:Number;
	
	private var yContainerOffset:Number;

    //These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
    
    /**
    * constructor
    */
    function CompetenceMappingDialog(){
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
        
		this._visible = false;
		
		//set the reference to the StyleManager
        themeManager = ThemeManager.getInstance();

        //Set the text for buttons
		competence_mapping_lbl.text = Dictionary.getValue("competences_mapped_to_act_lbl", [app.canvas.model.selectedItem.activity.title]);
		
        close_btn.label = Dictionary.getValue("prefs_dlg_ok");
		
		main_mc = competence_mapping_scp.content;
		
		// add new layer here on top of main_mc and then remove it whenever we need to redraw the container contents
		competenceContainerLayer = main_mc.createEmptyMovieClip("competenceContainerLayer", main_mc.getNextHighestDepth());
		
        //get focus manager + set focus to OK button, focus manager is available to all components through getFocusManager
        fm = _container.getFocusManager();
        fm.enabled = true;
        
        //EVENTS
        //Add event listeners for ok, cancel and close buttons
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
		
		setUpContent(); //maybe call from elsewhere
		
        //fire event to say we have loaded
		_container.contentLoaded();
	}
    
    public function setUpContent():Void{
		draw();
		this._visible = true;
		
	}
	
	public function draw(evt:Object) {
		
		var competenceKeys:Array = app.getDesignDataModel().competences.keys();
		competenceKeys.sort();
		var mappedCompetences:Array = new Array();
		
		competenceItemMCs = new Array();
		yContainerOffset = 15;
		
		var selectedActivityUIID:Number = app.canvas.model.selectedItem.activity.activityUIID;
		
		if (app.getDesignDataModel().activities.get(selectedActivityUIID) instanceof ToolActivity) {
			mappedCompetences = ToolActivity(app.getDesignDataModel().activities.get(selectedActivityUIID)).competenceMappings;
		}
		
		var checked:Boolean; // true if competence is to be checked for selected activity
		
		// for each defined competence, see if it is mapped to this activity
		for (var i=0; i<competenceKeys.length; i++) {
			
			checked = false;
			
			var competenceTitle:String = competenceKeys[i];
			
			for (var j=0; j<mappedCompetences.length; j++) {	
				if (mappedCompetences[j] == competenceTitle) {
					checked = true;
				}
			}
				
			var mc:MovieClip = competenceContainerLayer.attachMovie("competenceMappingItem", competenceTitle, competenceContainerLayer.getNextHighestDepth(), {_x:0, _y:yContainerOffset, _competenceNum:(i+1), _competenceTitle:competenceTitle, _checked:checked});
			
			competenceItemMCs.push(mc);
			yContainerOffset = yContainerOffset + 45;
		}
		
		competence_mapping_scp.invalidate();
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
            Debugger.log('themeChanged event broadcast with an object.type not equal to "themeChanged"',Debugger.CRITICAL,'themeChanged','CompetenceMappingDialog');
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
        close_btn.setStyle('styleName', styleObj);
       
		styleObj = themeManager.getStyleObject('CanvasPanel');
		_bgpanel.setStyle('styleName', styleObj);
		
        //Apply label style 
        styleObj = themeManager.getStyleObject('label');
        competence_mapping_lbl.setStyle('styleName', styleObj);
    }
	
    /**
    * Called by the close button 
    */
    private function close(){
		
		var activityUIID:Number = app.canvas.model.selectedItem.activity.activityUIID;
		var selectedAct = app.getDesignDataModel().activities.get(activityUIID);
		
		if (selectedAct instanceof ToolActivity) {
			//competenceItemMCs contains a reference to drawn competenceItem movieclips
			for (var i=0; i<competenceItemMCs.length; i++){
				var cmi:CompetenceMappingItem = CompetenceMappingItem(competenceItemMCs[i]);
				var _isMapped:Boolean = cmi.isMapped;
				if (_isMapped) {
					var addMapping:Boolean = true;
					for (var j=0; j<selectedAct.competenceMappings.length; j++) {
						if (selectedAct.competenceMappings[j] == cmi.competenceTitle) {
							addMapping = false;
						}
					}
					if (addMapping) {
						selectedAct.competenceMappings.push(cmi.competenceTitle);
						Debugger.log("added a mapping, selectedAct.competenceMappings.length: "+selectedAct.competenceMappings.length, Debugger.CRITICAL, "close", "CompetenceMappingDialog");
					}
				} 
				else {
					// if the mapping exists remove the mapping
					for (var j=0; j<selectedAct.competenceMappings.length; j++) {
						if (selectedAct.competenceMappings[j] == cmi.competenceTitle) {
							selectedAct.competenceMappings.splice(j,1); // remove the competence mapping from the activity
						}
					}
				}
			}
		}
		
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

		competence_mapping_scp.setSize(w, h-80);
		
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