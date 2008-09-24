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
class org.lamsfoundation.lams.authoring.cmpt.CompetenceMappingItem extends MovieClip {

    //References to components + clips 
	private var _bgpanel:MovieClip;       //The underlaying panel base
	private var _container:MovieClip;  //The container window that holds the dialog
	
	private var _competenceNum:String;
	private var _competenceTitle:String;
	
	private var _checked:Boolean;
	
	private var competence_number_lbl:Label;
	private var competence_title_txt:TextField;
	
	private var _map_competence_checkbox:CheckBox;
	
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
    function CompetenceMappingItem(){
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

		competence_title_txt.editable = false;
		
		if (_competenceNum != null) competence_number_lbl.text = _competenceNum;
		if (_competenceTitle != null) competence_title_txt.text = _competenceTitle;
		if (_checked != null) _map_competence_checkbox.selected = _checked;
		
        //get focus manager + set focus to OK button, focus manager is available to all components through getFocusManager
        fm = _container.getFocusManager();
        fm.enabled = true;
		
        //Register as listener with StyleManager and set Styles
        themeManager.addEventListener('themeChanged',this);
        setStyles();
		
        //fire event to say we have loaded
		_container.contentLoaded();
	}
    
	public function setUpContent():Void{
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
            Debugger.log('themeChanged event broadcast with an object.type not equal to "themeChanged"',Debugger.CRITICAL,'themeChanged','CompetenceMappingItem');
        }
    }
	
    /**
    * Called on initialisation and themeChanged event handler
    */
    private function setStyles(){
        //LFWindow, goes first to prevent being overwritten with inherited styles.
        var styleObj = themeManager.getStyleObject('LFWindow');
        _container.setStyle('styleName', styleObj);
       
		styleObj = themeManager.getStyleObject('CanvasPanel');
		_bgpanel.setStyle('styleName', styleObj);
		
        //Apply label style 
        styleObj = themeManager.getStyleObject('label');
        competence_number_lbl.setStyle('styleName', styleObj);
		competence_title_txt.setStyle('styleName', styleObj);
    }
	
    /**
    * Event dispatched by parent container when close button clicked
    */
    public function click(e:Object):Void{
		
        e.target.deletePopUp();
    }
	
	public function get isMapped():Boolean {
		return _map_competence_checkbox.selected;
	}
	
	public function get competenceTitle():String {
		return _competenceTitle;
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