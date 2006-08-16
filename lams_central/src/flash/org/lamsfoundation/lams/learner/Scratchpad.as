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
import org.lamsfoundation.lams.learner.*;
import org.lamsfoundation.lams.learner.ls.*;
import org.lamsfoundation.lams.common.Sequence;
import org.lamsfoundation.lams.common.ToolTip;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.dict.*;
import org.lamsfoundation.lams.common.style.*;
import mx.controls.*
import mx.utils.*
import mx.managers.*
import mx.events.*


class Scratchpad extends MovieClip {
	
	//Height Properties
	private var spadHeightHide:Number = 20;
	private var spadHeightFull:Number = 217;
	
	//Open Close Identifier
	private var _spadIsExpended:Boolean;
	
	//Component properties
	private var _scratchpad_mc:MovieClip;
	private var spadHead_pnl:MovieClip;
	private var spadTitle_lbl:Label;
	private var _container:MovieClip;		// Holding Container
	private var minIcon:MovieClip;
	private var maxIcon:MovieClip;
	private var clickTarget_mc:MovieClip;
	private var view_btn:MovieClip;         // buttons
    private var save_btn:MovieClip;
	private var view_lbl:TextField;
	private var save_lbl:TextField;
	private var _tip:ToolTip;
	
	public static var SCRATCHPAD_ID:Number = 1;
	
	// notebook data entry fields
	private var _title:Label;
	private var title_txi:TextInput;
	private var entry_txa:TextArea;
	
	private var panel:MovieClip;       //The underlaying panel base
    private var _lessonModel:LessonModel;
	 private var _lessonController:LessonController;
	private var _tm:ThemeManager;
	private var _dictionary:Dictionary;
	
	//These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
   
	
	/** 
	* constructor
	*/
	public function Scratchpad() {
		//Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);
		
		_tm = ThemeManager.getInstance();
		_tip = new ToolTip();
		_dictionary = Dictionary.getInstance();
		_dictionary.addEventListener('init',Proxy.create(this,setLabels));
		
		//let it wait one frame to set up the components.
		MovieClipUtils.doLater(Proxy.create(this,init));
		
	}
	
	/**
    * Called a frame after movie attached to allow components to initialise
    */
    public function init(){
		trace('initialing header..');
		
        //Delete the enterframe dispatcher
        delete this.onEnterFrame;
		_lessonModel = _lessonModel;
		_lessonController = _lessonController;
		_scratchpad_mc = this;
		_spadIsExpended = false;
		maxIcon._visible = false;
		_lessonModel.setSpadHeight(spadHeightHide);
		setLabels();
		resize(Stage.width);
		
		
		//Add event listeners for resume and exit buttons
		
		view_btn.onRelease = function(){
			trace('on releasing view all button..');
			Application.getInstance().getScratchpad().viewNotebookEntries();
		}
		
		save_btn.onRelease = function(){
			trace('on releasing save button..');
			
			Application.getInstance().getScratchpad().saveEntry();
		}
		
		view_btn.onRollOver = Proxy.create(this,this['showToolTip'], view_btn, "sp_view_tooltip");
		view_btn.onRollOut = Proxy.create(this,this['hideToolTip']);
		
		save_btn.onRollOver = Proxy.create(this,this['showToolTip'], save_btn, "sp_save_tooltip");
		save_btn.onRollOut = Proxy.create(this,this['hideToolTip']);
		clickTarget_mc.onRelease = Proxy.create (this, localOnRelease);
		clickTarget_mc.onReleaseOutside = Proxy.create (this, localOnReleaseOutside);
		this.onEnterFrame = setLabels;
		
	}
	
	public function localOnRelease():Void{
		
		if (_spadIsExpended){
			trace("P Pressed in 'localOnRelease' and _spadIsExpended is: "+_spadIsExpended)
			_spadIsExpended = false
			minIcon._visible = true;
			maxIcon._visible = false;
			_lessonModel.setSpadHeight(spadHeightHide);
			
		}else {
			trace("P Pressed in 'localOnRelease' and _spadIsExpended is: "+_spadIsExpended)
			_spadIsExpended = true
			minIcon._visible = false;
			maxIcon._visible = true;
			_lessonModel.setSpadHeight(spadHeightFull);
			//Application.getInstance().onResize();
		}
	}
	
	public function isSpadExpanded():Boolean{
		return _spadIsExpended;
	}
	
	public function spadFullHeight():Number{
		return spadHeightFull;
	}
	
	
	public function localOnReleaseOutside():Void{
		Debugger.log('Release outside so no event has been fired, current state is: ' + _spadIsExpended,Debugger.GEN,'localOnReleaseOutside','Scratch Pad');
		
	}
	public function showToolTip(btnObj, btnTT:String):Void{
		
		var Xpos = Application.HEADER_X+ 5;
		var Ypos = Application.HEADER_Y+( btnObj._y+btnObj._height)+2;
		var ttHolder = Application.tooltip;
		var ttMessage = Dictionary.getValue(btnTT);
		var ttWidth = 150
		_tip.DisplayToolTip(ttHolder, ttMessage, Xpos, Ypos, undefined, ttWidth);
		
	}
	
	public function hideToolTip():Void{
		_tip.CloseToolTip();
	}
	
	private function setStyles(){
		var styleObj = _tm.getStyleObject('smallLabel');
		_title.setStyle('styleName', styleObj);
		
		styleObj = _tm.getStyleObject('textarea');
		title_txi.setStyle('styleName', styleObj);
		entry_txa.setStyle('styleName', styleObj);
		spadTitle_lbl.setStyle('styleName', styleObj);
		
		//For Panels
		styleObj = _tm.getStyleObject('BGPanel');
		spadHead_pnl.setStyle('styleName',styleObj);
	}
	
	private function setLabels(){
		//Set the text for buttons
        view_lbl.text = Dictionary.getValue('sp_view_lbl');
        save_lbl.text = Dictionary.getValue('sp_save_lbl');
		
		//Set text for Scratch pad labels
		spadTitle_lbl.text = Dictionary.getValue('sp_panel_lbl');
		_title.text = Dictionary.getValue('sp_title_lbl');
		
		
		setStyles();
		
		delete this.onEnterFrame; 
		
		dispatchEvent({type:'load',target:this});
        
	}
	
	public function saveEntry(){
		// TODO: validate entry fields
		
		var dto:Object = getDataForSaving();
		
		var callback:Function = Proxy.create(this,onStoreEntryResponse);
		
		Application.getInstance().getComms().sendAndReceive(dto,"notebook/storeNotebookEntry",callback,false);
		
	}
	
	public function getDataForSaving():Object {
		var dto:Object = new Object();
		dto.externalID = Number(_root.lessonID);
		dto.externalIDType = SCRATCHPAD_ID;
		dto.externalSignature = "SCRATCHPAD";
		dto.userID = Number(_root.userID);
		dto.title = title_txi.text;
		dto.entry = entry_txa.text;
		
		return dto;
	}
	
	public function onStoreEntryResponse(r):Void {
		if(r instanceof LFError){
			r.showErrorAlert();
		}else{
			// TODO: SUCCESS MESSAGE/CLEAR FIELDS
			title_txi.text = "";
			entry_txa.text = "";
		}
	}
	
	public function viewNotebookEntries(){
		// TODO: Pop-up for Notebook Entries
		
		var notebook_url:String = _root.serverURL + 'notebook/notebook.jsp?userID=' + _root.userID + '&lessonID='+_root.lessonID;
		
		JsPopup.getInstance().launchPopupWindow(notebook_url, 'Notebook', 410, 640, true, true, false, false, false);
	
	}
	
	public function resize(width:Number){
		panel._width = width;
		
	}
	
	function get className():String { 
        return 'Scratchpad';
    }
}