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
import org.lamsfoundation.lams.common.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.style.*

/*
* Dialogue to collect a string form the user, adn call some specified action on OK
* @author  DC
*/
class org.lamsfoundation.lams.common.ui.InputDialog extends MovieClip implements Dialog{

    //References to components + clips 
    private var _container:MovieClip;  //The container window that holds the dialog
    
    private var ok_btn:Button;         //OK+Cancel buttons
    private var cancel_btn:Button;
    private var instructions_lbl:Label;
    private var input_txi:TextInput;
	
	private var _okHandler:Function;
	private var _cancelHandler:Function;
    
    private var panel:MovieClip;       //The underlaying panel base
    
    
    private var fm:FocusManager;            //Reference to focus manager
    private var themeManager:ThemeManager;  //Theme manager
    
    
    //These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
    
    
    /**
    * constructor
    */
    function InputDialog(){
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
		ok_btn.addEventListener('click',Delegate.create(this, ok));
        cancel_btn.addEventListener('click',Delegate.create(this, cancel));
		
		//get focus manager + set focus to OK button, focus manager is available to all components through getFocusManager
        fm = _container.getFocusManager();
        fm.enabled = true;
        input_txi.setFocus();
        //Set the text for buttons
    //    ok_btn.label = Dictionary.getValue('input_dlg_ok');
      //  cancel_btn.label = Dictionary.getValue('input_dlg_cancel');
		//Fire contentLoaded event, this is required by all dialogs so that creator of LFWindow can know content loaded
        _container.contentLoaded();
        
	}
	
		
		
	public function setInstructionsLabel(lbl:String){
		instructions_lbl.text = lbl;
	}
	
	public function setOKButton(lbl:String,fn:Function){
		ok_btn.label = lbl;
		_okHandler = fn;
	}
	
	public function setCancelButton(lbl:String,fn:Function){
		cancel_btn.label = lbl;
		_cancelHandler = fn;
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
        _container.setStyle('styleName',styleObj);

        //Get the button style from the style manager and apply to both buttons
        styleObj = themeManager.getStyleObject('button');
        ok_btn.setStyle('styleName',styleObj);
        cancel_btn.setStyle('styleName',styleObj);
      
         //Apply label style 
        styleObj = themeManager.getStyleObject('label');
        instructions_lbl.setStyle('styleName',styleObj);
       
    }

    /**
    * Called by the cancel button 
    */
    private function cancel(){
       Debugger.log('cancel click',Debugger.GEN,'cancel','org.lamsfoundation.lams.common.ui.InputDialog');
        //close parent window
        _container.deletePopUp();
		_cancelHandler();
    }
    
    /**
    * Called by the OK button 
    */
    private function ok(){
		Debugger.log('ok click',Debugger.GEN,'ok','org.lamsfoundation.lams.common.ui.InputDialog');
		var r:String = input_txi.text;
		_okHandler(r);
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
    * Event dispatched by parent container when close button clicked
    */
    public function click(e:Object):Void{
        trace('PreferencesDialog.click');
        e.target.deletePopUp();
    }
    
    
    /**
    * Main resize method, called by scrollpane container/parent
    */
    public function setSize(w:Number,h:Number):Void{
        //Debugger.log('setSize',Debugger.GEN,'setSize','org.lamsfoundation.lams.common.ws.WorkspaceDialog');
        //Size the panel
        panel.setSize(w,h);

        //Buttons
        //ok_btn.move(w-xOkOffset,h-yOkOffset);
        //cancel_btn.move(w-xCancelOffset,h-yCancelOffset);
    }
    
    //Gets+Sets
    /**
    * set the container refernce to the window holding the dialog
    */
    function set container(value:MovieClip){
        _container = value;
    }

}