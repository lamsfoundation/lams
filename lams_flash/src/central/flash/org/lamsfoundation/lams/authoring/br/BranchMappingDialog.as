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

import mx.controls.*
import mx.utils.*
import mx.managers.*
import mx.events.*

/*
* Branch Mapping Dialog window
* @author  Mitchell Seaton
*/
class BranchMappingDialog extends MovieClip implements Dialog {

    //References to components + clips 
    private var _container:MovieClip;  //The container window that holds the dialog
    
	private var _primary:Array;
	private var _secondary:Array;
	
    private var close_btn:Button;         // Close button
	
	private var add_match_btn:Button;
	private var remove_match_btn:Button;
    
    private var panel:MovieClip;       //The underlaying panel base
    
    private var primary_lst:List;  
    private var secondary_lst:List;    
    
    private var primary_lst_lbl:Label;        // Group and Branches list labels
    private var secondary_lst_lbl:Label;
	private var match_dgd_lbl:Label;
	
	private var match_dgd:DataGrid;		// Group-Branch Matching listbox
    
    private var fm:FocusManager;            //Reference to focus manager
    private var themeManager:ThemeManager;  //Theme manager
	public var app:Application;
	
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
    function BranchMappingDialog(){
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
        
        //Set the text for buttons
        close_btn.label = Dictionary.getValue("al_done");
		
        //get focus manager + set focus to OK button, focus manager is available to all components through getFocusManager
        fm = _container.getFocusManager();
        fm.enabled = true;

        //work out offsets from bottom RHS of panel
        xOkOffset = panel._width - close_btn._x;
        yOkOffset = panel._height - close_btn._y;
		
        //Register as listener with StyleManager and set Styles
        themeManager.addEventListener('themeChanged',this);
        setStyles();
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
    * Called on initialisation and themeChanged event handler
    */
    private function setStyles(){
        //LFWindow, goes first to prevent being overwritten with inherited styles.
        var styleObj = themeManager.getStyleObject('LFWindow');
        _container.setStyle('styleName', styleObj);

        //Get the button style from the style manager and apply to both buttons
        styleObj = themeManager.getStyleObject('button');
        close_btn.setStyle('styleName', styleObj);
        add_match_btn.setStyle('styleName', styleObj);
        remove_match_btn.setStyle('styleName', styleObj);
		
		
		styleObj = themeManager.getStyleObject('CanvasPanel');
		panel.setStyle('styleName', styleObj);
		
        //Apply label style 
        styleObj = themeManager.getStyleObject('label');
        primary_lst_lbl.setStyle('styleName', styleObj);
        secondary_lst_lbl.setStyle('styleName', styleObj);
		match_dgd_lbl.setStyle('styleName', styleObj);
    }
    
    /**
    * Called by the OK button 
    */
    private function close(){
        Debugger.log('OK Clicked',Debugger.GEN,'ok','org.lamsfoundation.lams.GroupMatchingDialog');
        
        //close popup
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
        panel.setSize(w,h);

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
	
	public function set primary(a:Array){
		_primary = a;
	}
	
	public function set secondary(a:Array){
		_secondary = a;
	}

}