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

import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.monitoring.mv.*
import org.lamsfoundation.lams.monitoring.*;
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.mvc.*

import org.lamsfoundation.lams.common.Config;

import org.lamsfoundation.lams.common.ApplicationParent;

import mx.managers.*
import mx.containers.*
import mx.events.*
import mx.utils.*
import mx.controls.*


/**
*Monitoring Lock view for the Monitor
* Relects changes in the MonitorModel
*/

class org.lamsfoundation.lams.monitoring.mv.MonitorLockView extends AbstractView {
	
	private var _className = "MonitorLockView";
	
	private var _app:Application;
	private var _tm:ThemeManager;
	
	private var _monitorLockView_mc:MovieClip;
	
	//Background panel clip
    private var bkg_pnl:MovieClip;
	
	// Message box
	private var messageBox:MovieClip;
	
	// message and action labels
	private var msg_lbl:Label;
	private var action_lbl:Label;
	
	// buttons
	private var action_btn:Button;
	
	// state variable
	private var _enabled:Boolean;
	private var _isChecking:Boolean;
	
    private var _monitorLockView:MonitorLockView;
	private var _monitorModel:MonitorModel;
	private var _monitorController:MonitorController;
	
    //Defined so compiler can 'see' events added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	
	
	/**
	* Constructor
	*/
	function MonitorLockView(){
		_monitorLockView = this;
		_app = Application.getInstance();
		_tm = ThemeManager.getInstance();
		
		action_btn = messageBox.action_btn;
		msg_lbl = messageBox.msg_lbl;
		action_lbl = messageBox.action_lbl;
		
		_isChecking = false;
		
		//Init for event delegation
        mx.events.EventDispatcher.initialize(this);
	}
	
	/**
	* Called to initialise Canvas  . CAlled by the Canvas container
	*/
	public function init(m:Observable,c:Controller,x:Number,y:Number,w:Number,h:Number, enabled:Boolean){

		super (m, c);
		
		MovieClipUtils.doLater(Proxy.create(this,draw,enabled)); 
		
    }    
	
	/**
	 * Recieved update events from the CanvasModel. Dispatches to relevent handler depending on update.Type
	 * @usage   
	 * @param   event
	 */
	public function update (o:Observable,infoObj:Object):Void{
		
		var mm:MonitorModel = MonitorModel(o);
		_monitorController = getController();

		switch (infoObj.updateType){
			case 'POSITION' :
				setPosition(mm);
                break;
            case 'SIZE' :
			    setSize(mm);
                break;
			case 'TRIGGER_ACTION' :
				callAction(mm);
				break;
			case 'SEQUENCE' :
				break;
			case 'PROGRESS' :
				checkAvailability(mm);
				break;
            default :
                Debugger.log('unknown update type :' + infoObj.updateType,Debugger.CRITICAL,'update','org.lamsfoundation.lams.MonitorView');
		}

	}
	
	/**
    * layout visual elements on the canvas on initialisation
    */
	private function draw(enabled:Boolean){
		var mcontroller = getController();
		
		_enabled = enabled;
		action_btn.addEventListener("click",mcontroller);
		
		// display continue button?
		//action_btn._visible = (enabled) ? true : false;
		
		setLabels(enabled);
		setStyles();
		
	    dispatchEvent({type:'load',target:this});
		
	}

	/**
	 * Get the CSSStyleDeclaration objects for each component and apply them
	 * directly to the instance  
	 */
	private function setStyles():Void{
		var styleObj = _tm.getStyleObject('BGPanel');
		bkg_pnl.setStyle('styleName',styleObj);
		
		styleObj = _tm.getStyleObject('Label');
		msg_lbl.setStyle('styleName', styleObj);
		action_lbl.setStyle('styleName', styleObj);
		
		styleObj = _tm.getStyleObject('Button');
		action_btn.setStyle('styleName', styleObj);
	}
	
	private function setLabels(enabled:Boolean):Void{
		if(enabled) {
			msg_lbl.text = Dictionary.getValue("ls_continue_lbl", [_app.sequence.getSequenceName(), _app.sequence.getLearningDesignModel().editOverrideUserFullName]);
			action_lbl.text = Dictionary.getValue("ls_continue_action_lbl", [Dictionary.getValue("continue_btn")]);
			action_btn.label = Dictionary.getValue("continue_btn");
		} else {
			msg_lbl.text = Dictionary.getValue("ls_locked_msg_lbl", [_app.sequence.getSequenceName(), _app.sequence.getLearningDesignModel().editOverrideUserFullName])
			action_lbl.text = "";
			action_btn.label = Dictionary.getValue("check_avail_btn");
		}
	}
		
	/**
    * Sets the size of the canvas on stage, called from update
    */
	private function setSize(mm:MonitorModel):Void{
        bkg_pnl.setSize(Stage.width,Stage.height);
		
		var offset:Number = 8;
		
		// adjust bubble width
		messageBox.messageBubble.messageBubbleRect._width = (msg_lbl._width < action_lbl._width) ? action_lbl._width + offset : msg_lbl._width + offset;
		
		// position message box in center
		messageBox._x = (bkg_pnl._width/2) - (messageBox._width/2);
		messageBox._y = (bkg_pnl._height/2) - (messageBox._height/2);
		
	}
	
	 /**
    * Sets the position of the canvas on stage, called from update
    * @param cm Canvas model object 
    */
	private function setPosition(mm:MonitorModel):Void{
        var p:Object = mm.getPosition();
		
		this._x = p.x;
        this._y = p.y - Application.MONITOR_Y;
	}
	
	private function callAction(mm:MonitorModel):Void{
		
		if(_enabled) {
			// continue action - open live edit
			mm.getMonitor().readyEditOnFly(true);
		} else {
			// check availabilty action - re-get model and check
			action_lbl.text = Dictionary.getValue("msg_bubble_check_action_lbl");  // "Checking..."
			_isChecking = true;
			mm.getMonitor().reloadLessonToMonitor();
		}
	}
	
	private function checkAvailability(mm:MonitorModel):Void {
		var app:Application = Application.getInstance();
		var obj:Object = app.layout.manager.checkAvailability(mm.getSequence());
		
		Debugger.log("isLocked: " + obj.locked + " isEditingUser: " + obj.isEditingUser, Debugger.CRITICAL, "checkAvailability", "MonitorLockView");
		if(!obj.locked) {
			// reload monitor
			Debugger.log("Now unlocked, Reloading UI" + obj.isEditingUser, Debugger.CRITICAL, "checkAvailability", "MonitorLockView");
			app.sequence = mm.getSequence();
			
			mm.getMonitor().getMV()._visible = true;
			LFMenuBar.getInstance().setVisible(true);
			destroy();
		} else if(obj.locked && !obj.isEditingUser && _isChecking) {
			action_lbl.text = Dictionary.getValue("msg_bubble_failed_action_lbl"); // "Unavailable. Try Again.";
			_isChecking = false;
		}
	}
	
	/**
	 * Overrides method in abstract view to ensure cortect type of controller is returned
	 * @usage   
	 * @return  CanvasController
	 */
	public function getController():MonitorController{
		var c:Controller = super.getController();
		return MonitorController(c);
	}
	
	/*
    * Returns the default controller for this view.
    */
    public function defaultController (model:Observable):Controller {
        return new MonitorController(model);
    }
	
	public function destroy() {
		this.removeMovieClip();
	}
	
}