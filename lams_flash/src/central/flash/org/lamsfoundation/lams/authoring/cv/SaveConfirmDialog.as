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

import org.lamsfoundation.lams.authoring.cv.*;
import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.*

import mx.controls.*
import mx.utils.*
import mx.managers.*
import mx.events.*

/*
*
* @author     M Seaton
* @comments   Dialog displayed after successfully saving a design.
* 
*/
class org.lamsfoundation.lams.authoring.cv.SaveConfirmDialog extends MovieClip {
	
	private static var MARGIN:Number = 5;
	private static var SPACING:Number = 30;
	private var _canvasModel:CanvasModel;
	private var _canvasController:CanvasController;
	private var _msg:String;
	private var _requestSrc:String;
	
    private var btnOffset_X:Number = 10;
	private var btnOffset_Y:Number = 5;
	private var _btnArray:Array;
	
	 //References to components + clips 
    private var _container:MovieClip;       //The container window that holds the dialog. Will contain any init params that were passed into createPopUp
   
	private var okBtn:Button;
	private var retBtn:Button;
	
	private var msgBox:TextArea;
	
	
    private var fm:FocusManager;
    private var _tm:ThemeManager;

   
	//Defined so compiler can 'see' events added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	
	
	/**
	 * Constructor
	 */
	function SaveConfirmDialog(){
		Debugger.log('Constructor',Debugger.GEN,'SaveConfirmDialog','SaveConfirmDialog');
		_tm = ThemeManager.getInstance();
		
		//Set up this class to use the Flash event delegation model
		EventDispatcher.initialize(this);
		
		//let it wait one frame to set up the components.
		MovieClipUtils.doLater(Proxy.create(this,init));
		
	}
	
	public function init():Void{
		_canvasModel = _container.canvasModel;
		_canvasController = _container.canvasController;
		_msg = _container.msg;
		_requestSrc = _container.requestSrc;

		_container.addEventListener('click',this);
		
		//get focus manager + set focus , focus manager is available to all components through getFocusManager
        fm = _container.getFocusManager();
        fm.enabled = true;
		
		setStyles();

		//set up handlers
		okBtn.addEventListener("click",this);
		retBtn.addEventListener("click",this);
		
		msgBox.text = _msg;
		
		stpButtons();
		okBtn.setFocus();
		
		//_container._parent.setSize(150, 120);
		_container._parent.hScrollPolicy = "no";
		_container.resize = false;
		setSize(this._width, this._height);

		//fire event to say we have loaded
		_container.contentLoaded();
	}
	
	public function stpButtons():Void {
		okBtn.label = Dictionary.getValue('al_ok');
		retBtn.label = Dictionary.getValue('cv_close_return_to_ext_src', [_requestSrc]);
		
		this.createTextField("okBtnTxt", this.getNextHighestDepth(), btnOffset_X, -100, 10, 18); 
		var okBtnTxt = this["okBtnTxt"];
		var okBtnWidth = getButtonWidth(okBtnTxt, okBtn.label);
		
		this.createTextField("retBtnTxt", this.getNextHighestDepth(), btnOffset_X, -100, 10, 18); 
		var retBtnTxt = this["retBtnTxt"];
		var retBtnWidth = getButtonWidth(retBtnTxt, retBtn.label);
		
		okBtn.setSize(okBtnWidth, okBtn._height);
		retBtn.setSize(retBtnWidth, retBtn._height);
		
		okBtnTxt.removeTextField();
		retBtnTxt.removeTextField();
	}
	
	private function getButtonWidth(btnText:TextField, btnValue:String):Number {
		btnText.autoSize = "true";
		btnText.html = true;
		btnText.htmlText = btnValue;
		return btnText.textWidth + SPACING;
	}

	/**
    * Main resize method, called by scrollpane container/parent
    */
    public function setSize(w:Number,h:Number){
	  var wid:Number = okBtn._width + retBtn._width + (2*MARGIN);
	  
	  okBtn._x = (w/2) - (wid/2);
	  okBtn._y =  h - okBtn._height - MARGIN;
	  retBtn._x = (w/2) - (wid/2) + okBtn._width + (2*MARGIN);
	  retBtn._y =  h - retBtn._height - MARGIN;
	  
	  
	  
    }
	
	/**
	 * Get the CSSStyleDeclaration objects for each component and applies them
	 * directly to the instanced
	 * @usage   
	 * @return  
	 */
	private function setStyles() {
       
		var styleObj = _tm.getStyleObject('button');
		okBtn.setStyle('styleName',styleObj);
		retBtn.setStyle('styleName',styleObj);
		msgBox.setStyle('borderStyle', 'none');
		
    }
    
    /**
    * set the container refernce to the window holding the dialog
    */
    public function set container(value:MovieClip){
        _container = value;
    }
	
   /**
	 * Recieves the click events from the canvas views (inc Property Inspector) buttons.  Based on the target
	 * the relevent method is called to action the user request
	 * @param   evt 
	 */
	 /**/
	public function click(e):Void{
		var tgt:String = new String(e.target);
		Debugger.log('click tgt:'+tgt,Debugger.GEN,'click','SaveConfirmDialog');
		 
		if(tgt.indexOf('okBtn') != -1) {
			_container.deletePopUp();
		} else if(tgt.indexOf('retBtn') != -1) {
			ApplicationParent.extCall('closeWindow', null);
			_container.deletePopUp();
		} else {
			_container.deletePopUp();
		}
		
		
	}
	
}

