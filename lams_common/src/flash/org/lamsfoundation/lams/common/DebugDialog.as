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
import org.lamsfoundation.lams.authoring.*

/*
* Preferences Dialog window for editing user preferences
* @author  DI
*/
class DebugDialog extends MovieClip implements Dialog{

    //References to components + clips 
    private var _container:MovieClip;  //The container window that holds the dialog
    private var cfg:Config;            //local config reference
    
    private var ok_btn:Button;         //OK+Cancel buttons
    private var cancel_btn:Button;
    private var load_btn:Button;
    private var assign_btn:Button;     //Assign property
    private var serialize_btn:Button;  //Serialize an object and trace
	private var sendData_btn:Button;  //Send Data to an object to send it to the server
    private var showProps_btn:Button;  //Serialize an object and trace
    private var clear_btn:Button;  //Clear text area
    
    private var panel:MovieClip;       //The underlaying panel base
    
    private var messages_ta:TextArea;
    private var input_ti:TextInput;     //For inputting text for assign and eval
    
   
    private var fm:FocusManager;            //Reference to focus manager
    private var themeManager:ThemeManager;  //Theme manager
    
    private var currentTheme:String;
    private var newTheme:String;
    
    //Dimensions for resizing
    private var xOkOffset:Number;
    private var yOkOffset:Number;
    private var xCancelOffset:Number;
    private var yCancelOffset:Number;

    //These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
    
    private var debug:Debugger;
    
    
    /**
    * constructor
    */
    function DebugDialog(){
        //Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);

        //set up local confi reference
        cfg = Config.getInstance();
        debug = Debugger.getInstance();
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
        
        setupEvents();
        setupOffsets();
        setStyles();
        showDebugLog();
        messages_ta.wordWrap = false;
    }
    
    
    private function setupOffsets() {
        //work out offsets from bottom RHS of panel
        xOkOffset = panel._width - ok_btn._x;
        yOkOffset = panel._height - ok_btn._y;
        xCancelOffset = panel._width - cancel_btn._x;
        yCancelOffset = panel._height - cancel_btn._y;
    }
    
    private function setupEvents() {
        //EVENTS
        //Add event listeners for ok, cancel and close buttons
        ok_btn.addEventListener('click',Delegate.create(this, ok));
        cancel_btn.addEventListener('click',Delegate.create(this, cancel));
        //Assign Click (close button) and resize handlers
        _container.addEventListener('click',this);
        _container.addEventListener('size',this);
        //Register as listener with StyleManager and set Styles
        themeManager.addEventListener('themeChanged',this);
        
        //Register with the Debugger class to pick up log messages
        debug.addEventListener('update',Delegate.create(this, onDebugUpdate))
        
        assign_btn.addEventListener('click',Delegate.create(this, assignProperty));
        serialize_btn.addEventListener('click',Delegate.create(this, serialize));
		sendData_btn.addEventListener('click',Delegate.create(this, crashDumpToServer));
        showProps_btn.addEventListener('click',Delegate.create(this, showProperties));
        clear_btn.addEventListener('click',Delegate.create(this, clearText));
        load_btn.addEventListener('click',Delegate.create(this, loadHandler));
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
    * Called when debug log is changed
    */
    public function onDebugUpdate(evt:Object){
        if(evt.type=='update') {
            showDebugLog();
        }
    }
    
    /**
    * Gets the log, formats it and populates the text area with it
    */
    private function showDebugLog(){
        //Set the text in the text area + scroll to the bottom
		//TODO: Make this more efficient, currently the WHOLE log is re-rendered each update.... grrrr
        
		if(messages_ta.length < 1){
			messages_ta.text = debug.getFormattedMsgLog();
		}else{
			messages_ta.text += debug.getLatestMsg();
		}
		
		messages_ta.vPosition = messages_ta.maxVPosition;
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
    }
    
    //This will make the assignment
    private function assignProperty() {
        //Get the text to assign
        var text_str:String = input_ti.text;
        
        if(text_str != '' && text_str!=undefined && text_str.indexOf('=')>0) {
            var beginPos:Number = 0;
            var endPos:Number = text_str.indexOf('=');
            var obj_str:String = text_str.substr(0,endPos);
            //var obj = eval(obj_str);
            //get the property which is after the = sign
            var prop = text_str.substr(endPos+1,text_str.length-endPos);
            //trace('obj_str :' + obj_str);
            //trace('prop :' + prop);
           
            //Make the assignment
            //obj = prop;
            var o:String = '_root'
            var p:String = '_root._x';
            var val:String = '500'
            //setProperty(o,p,val);
            //setProperty(obj,prop,
        } else {
            traceMsg('Missing = sign or invalid string');
        }
    }

    /**
    * Evaluates a user defined string to an object, serializes the result and prints it
    */
    private function serialize() {
        //Get the text to assign
        var text_str:String = input_ti.text;
        //Need the text before equals
        var obj:Object = eval(text_str);
        if(obj) {
            //Check if toData or itemToData (for config class) methods exist
            if(obj.toData || obj.itemToData){
                if(obj.itemToData){
                    var data = obj.itemToData();
                } else {
                    var data = obj.toData();
                }
                //Get comms and serialize object
                var comms = Application.getInstance().getComms();
                var sx:String = comms.serializeObj(data);
                //Write out the serialized object
                messages_ta.html=false;
                traceMsg('serializing \n' + input_ti.text +'\n \n' + sx);
                messages_ta.html=true;
                //Copy to clipboard
                System.setClipboard(sx);
            }else {
                traceMsg("no 'toData' or 'itemToData' method or found for :" + text_str);
            }
        } else {
            traceMsg("can't find object :" + text_str);
        }
    }
    
	private function crashDumpToServer(){
		Debugger.crashDataDump();
		
	}
	
    /**
    * Shows the properties of the object entered into the textInput
    */
    function showProperties() {
        var text_str:String = input_ti.text;
        if(text_str != '' || text_str!=undefined ) {
    //		ASSetPropFlags(_global, null, 6, 1);
            //Eval the target
            var target = eval(text_str);
            var i=0;
            if(target!=undefined) {
                traceMsg(text_str + ' = ' + target);
                for (var x in target) {
                    i++;
                    traceMsg(i add '.obj: ' add [x] add '  val: ' add target[x]);
                }
            } else {
                traceMsg("Can't find " add text_str);
            }
        }
    }
    
    /**
    * traces a message to the debug text area
    */
    private function traceMsg(msg:String) {
        messages_ta.text+= msg;
        messages_ta.vPosition = messages_ta.maxVPosition;
    }
    
    /**
    * Clears the text in the text area
    */
    private function clearText() {
        messages_ta.text = '';
    }
    
    /**
    * Called by the cancel button 
    */
    private function cancel(){
        //close parent window
        _container.deletePopUp();
    }
    
    /**
    * Called by the OK button 
    */
    private function ok(){
        //close popup
        _container.deletePopUp();
    }
	
	private function loadHandler(){
		//load some wddx and 
		var url_str:String = input_ti.text;
		var comms = Application.getInstance().getComms();
		comms.getRequest(url_str,testLoadXMLLoaded,true);
		
	}

	/**
    * XML onLoad handler for data
    */
    private function testLoadXMLLoaded (dto:Object){
        _global.breakpoint();
		var test=dto;
		ObjectUtils.printObject(dto);
		
    }

    /**
    * Clean up on unload
    */
    private function onUnload(){
        //Remove event listeners from debug
        debug.removeEventListener('update',onDebugUpdate);
        themeManager.removeEventListener('themeChanged',themeChanged);
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
        if(w>400 && h>260){
            //Size the panel
            panel.setSize(w,h);
            //Buttons
            ok_btn.move(w-xOkOffset-20,h-yOkOffset-20);
            cancel_btn.move(w-xCancelOffset-20,h-yCancelOffset-20);
        
            //Resize text area
            messages_ta.setSize(w-40,h-133);
    
            //align buttons        
            clear_btn._y = showProps_btn._y = serialize_btn._y = assign_btn._y = load_btn._y = h - 75;
            sendData_btn._y = h - 35;
            input_ti.setSize(w-40,input_ti.height);
            input_ti._y = h - 104;
        }
    }
    
    //Gets+Sets
    /**
    * set the container refernce to the window holding the dialog
    */
    function set container(value:MovieClip){
        _container = value;
    }

}