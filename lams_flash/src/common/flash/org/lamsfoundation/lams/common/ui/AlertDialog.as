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

import mx.transitions.Tween
import mx.transitions.easing.*

import org.lamsfoundation.lams.common.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.common.dict.*

/*
* Dialogue to collect a string form the user, and call some specified action on OK
* @author  DC
*/
class org.lamsfoundation.lams.common.ui.AlertDialog extends MovieClip {

	public static var ALERT:Number = 0;
	public static var CONFIRM:Number = 1;
	
	private var _bgpanel:MovieClip;
	private var _bgcolor:Color;

    private var ok_btn:Button;         //OK+Cancel buttons
    private var cancel_btn:Button;
	
	private var _title:TextArea;
    private var _message:TextArea;	// Alert message text
	
	private var _okHandler:Function;
	private var _cancelHandler:Function;
	
	private var _type:Number;
	private var _isDragging:Boolean;
    
	private var app:Object;
    private var fm:FocusManager;            //Reference to focus manager
    private var themeManager:ThemeManager;  //Theme manager
    
	private var transparentCover:MovieClip;
	private var clickTarget:MovieClip;
    
    //These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
    
	private var _textHeight:Number;
	
    /**
    * constructor
    */
    function AlertDialog(){
        //Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);
		EventDispatcher.initialize(clickTarget);
		
		this._visible = false;
		
		app = ApplicationParent.getInstance();

        //set the reference to the StyleManager
        themeManager = ThemeManager.getInstance();

        //Create a clip that will wait a frame before dispatching init to give components time to setup
        this.onEnterFrame = init;
    }

    /**
    * Called a frame after movie attached to allow components to initialise
    */
    private function init():Void{
        //Delete the enterframe dispatcher
        delete this.onEnterFrame;

		ok_btn.visible = false;
		cancel_btn.visible = false;
		_isDragging = false;
		
		clickTarget.enabled = true;	
		
		//clickTarget.onPress = Proxy.create(this, onDrag);
		clickTarget.onPress = Proxy.create(this, onDrag);
		clickTarget.onRelease = Proxy.create(this, onDrag);
		
		clickTarget.onReleaseOutside = Proxy.create(this, onDrag);

		ok_btn.addEventListener('click', Delegate.create(this, onOkPress));
        cancel_btn.addEventListener('click', Delegate.create(this, onCancelPress));
		
		//get focus manager + set focus to OK button, focus manager is available to all components through getFocusManager
        fm = _parent.getFocusManager();
        fm.enabled = true;
        
		setStyles();
		
		addTransparentLayer(this._parent);
		
		contentLoaded();
	}
	
	/**
    * method called by content when it is loaded
    */
    public function contentLoaded() {
        //dispatch an onContentLoaded event to listeners
        dispatchEvent({type:'contentLoaded',target:this});
    }
	
	private function onDrag() {
		if(!_isDragging) {
			this.startDrag();
			_isDragging = true;
		}
		else {
			if (this._x < 0) { this._x = 0; }
			if (this._x > Stage.width) { this._x = Stage.width; }
			if (this._y < 0) { this._y = 0; }
			if (this._y > Stage.height) { this._y = Stage.height; }

			this.stopDrag();
			_isDragging = false;
		}
	}
	
	private function addTransparentLayer(target:MovieClip) {
		var styleObj = themeManager.getStyleObject('CanvasPanel');
		
		transparentCover = target.createClassObject(Panel, "transparentCover", DepthManager.kTop, {_visible: true, enabled: false, _alpha: 0, _width: Stage.width, _height: Stage.height, styleName: styleObj});
		transparentCover.onPress = null;
		
		///org.lamsfoundation.lams.authoring.Application.getInstance().getToolbar().disableAll();
	}
	
	private function removeTransparentLayer() {
		transparentCover.removeMovieClip();
		//org.lamsfoundation.lams.authoring.Application.getInstance().getToolbar().enableAll();
	}
	
	public function setOKButton(lbl:String,fn:Function){
		if(lbl != null) {
			ok_btn.label = lbl;
			ok_btn.visible = true;
		}
			
		_okHandler = fn;
		var w = StringUtils.getButtonWidthForStr(lbl);
		
		if(w > ok_btn.width) 
			ok_btn.setSize(w, ok_btn.height);
	}
	
	public function setCancelButton(lbl:String,fn:Function){
		if(lbl != null) {
			cancel_btn.label = lbl;
			cancel_btn.visible = true;
		}

		_cancelHandler = fn;
		var w = StringUtils.getButtonWidthForStr(lbl);
		
		if(w > cancel_btn.width)
			cancel_btn.setSize(w, cancel_btn.height);
		
	}
	
	public function set title(__title:String) {
		modTextArea(_title);
		_title.text = __title;
	}
	
	public function set message(msg:String) {
		modTextArea(_message);
		_message.text = "<p align='center'>" + msg + "</p>";

		setMessageHeight();
		setSize(200, 200);
		
		if(_parent == ApplicationParent.root)
			setPosition(Stage.width/2, Stage.height/2);
		else
			setPosition(Stage.width/2 - _parent._x,  Stage.height/2 - _parent._y);
			
		_title._y = _bgpanel._y + 10;
		_title._x = -_title._width/2;
			
		_message._x = -_message._width/2;
		_message._y = _title._y + _title._height;
	}
	
	private function modTextArea(_obj:TextArea) {
		_obj._alpha = 0;
		_obj.enabled = false;
		_obj.html = true;
	}
	
	private function setMessageHeight() {
			this.createTextField("message", this.getNextHighestDepth(), -1000, -1000, 0, 0); 

			var msg_text = this["message"];
			
			msg_text.html = true;
			msg_text.htmlText = _message.text;
			msg_text.wordWrap = true;
			msg_text.autoSize = true;
			msg_text._width = _message.width;
				
			Debugger.log('textHeight: ' + msg_text.textHeight + 120, Debugger.GEN, 'setMessageHeight', 'org.lamsfoundation.lams.Alertialog');
			_message.setSize(_message.width, msg_text.textHeight + 120); 
			_textHeight = msg_text.textHeight + 120;			
	}
	
	public function set type(a:Number) {
		_type = a;
		
		if(_type == AlertDialog.ALERT) {
			// centre OK button
			ok_btn._x = _bgpanel._x + _bgpanel._width/2 - ok_btn._width/2;
			ok_btn._y = _bgpanel._y + _bgpanel._height - ok_btn._height - 10;
			
		} else {
			
			cancel_btn._x = _bgpanel._x + _bgpanel._width/2 + 5;
			cancel_btn._y = _bgpanel._y + _bgpanel._height - cancel_btn._height - 10;
			
			ok_btn._x = _bgpanel._x + _bgpanel._width/2 - ok_btn._width;
			ok_btn._y = _bgpanel._y + _bgpanel._height - ok_btn._height - 10;
		}

		Debugger.log("bg width: " + _bgpanel._width + " setting height: " + Math.round(Math.abs(_bgpanel._y) + Math.abs(ok_btn._y) + ok_btn._height), Debugger.CRITICAL, "type", "AlertDialog");

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
        
        //Get the button style from the style manager and apply to both buttons
        var styleObj = themeManager.getStyleObject('button');
        ok_btn.setStyle('styleName',styleObj);
        cancel_btn.setStyle('styleName',styleObj);
		
		styleObj = themeManager.getStyleObject('textarea');
        _message.setStyle("styleName", styleObj);
        _message.setStyle("disabledColor", "0xFFFFFF");
		
        _title.setStyle("styleName", styleObj);
        _title.setStyle("disabledColor", "0xFFFFFF");
		
		styleObj = themeManager.getStyleObject('AlertDialog');
		var colorTransform = styleObj.colorTransform;
		
		_bgcolor = new Color(_bgpanel);
		_bgcolor.setTransform(colorTransform);
		
    }

	/** Fade out on click if normal Alert (not Confirm)*/
	private function onOkPress(evt:Object) {

		var tween_obj_handler:Object = new Tween(this, "_alpha", Regular.easeIn, 100, 0, 0.25, true);
		tween_obj_handler.onMotionFinished = Delegate.create(this, ok);
	}

	/** Fade out on click if normal Alert (not Confirm)*/
	private function onCancelPress(evt:Object) {

		var tween_obj_handler:Object = new Tween(this, "_alpha", Regular.easeIn, 100, 0, 0.25, true);
		tween_obj_handler.onMotionFinished = Delegate.create(this, cancel);

	}

    /**
    * Called by the cancel button 
    */
    private function cancel(){
		Debugger.log('cancel click',Debugger.GEN,'cancel','org.lamsfoundation.lams.common.ui.InputDialog');
       
		removeTransparentLayer();
	   
		_cancelHandler();
		this.removeMovieClip();
    }
    
    /**
    * Called by the OK button 
    */
    public function ok(){
		Debugger.log('ok click',Debugger.GEN,'ok','org.lamsfoundation.lams.common.ui.InputDialog');
		
		removeTransparentLayer();
		
		Debugger.log('okHandler fn:' + _okHandler, Debugger.CRITICAL, "ok", "AlertDialog");		
		
		_okHandler();
		this.removeMovieClip();
    }
	
    /**
    * If an alert was spawned by this dialog this method is called when it's closed
    */
    private function alertClosed(){
		removeTransparentLayer();
        this.removeMovieClip();
    }
	
    /**
    * Main resize method, called by scrollpane container/parent
    */
    public function setSize(w:Number,h:Number):Void{
		
		_bgpanel._width = w;
		clickTarget._width = w;
		
		if (_textHeight > h) {
			_bgpanel._height = _textHeight;
			clickTarget._height = _textHeight;
		} else {
			_bgpanel._height = h;
			clickTarget._height = h;
		}
    }
	
	public function setPosition(x:Number, y:Number):Void {
		_x = x;
		_y = y;
		
		_bgpanel._x = -_bgpanel._width/2;
		_bgpanel._Y = -_bgpanel._height/2;
		
		clickTarget._x = -clickTarget._width/2;
		clickTarget._Y = -clickTarget._height/2;
	}

}