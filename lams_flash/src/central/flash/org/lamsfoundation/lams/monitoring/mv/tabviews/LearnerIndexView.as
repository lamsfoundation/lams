/***************************************************************************
 * Copyright (C) 2007 LAMS Foundation (http://lamsfoundation.org)
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

import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.ui.*;
import org.lamsfoundation.lams.common.style.*;
import org.lamsfoundation.lams.monitoring.mv.*;
import org.lamsfoundation.lams.monitoring.mv.tabviews.*;
import org.lamsfoundation.lams.monitoring.*;
import org.lamsfoundation.lams.common.dict.*; 
import org.lamsfoundation.lams.common.mvc.*;
import org.lamsfoundation.lams.common.ApplicationParent;
import org.lamsfoundation.lams.authoring.Activity;
import org.lamsfoundation.lams.authoring.ComplexActivity;
import org.lamsfoundation.lams.authoring.cv.CanvasActivity;
import org.lamsfoundation.lams.common.Sequence;
import org.lamsfoundation.lams.common.ToolTip;
import org.lamsfoundation.lams.authoring.Transition;

import mx.managers.*;
import mx.containers.*;
import mx.events.*;
import mx.utils.*;
import mx.controls.*;

class org.lamsfoundation.lams.monitoring.mv.tabviews.LearnerIndexView extends AbstractView {
	
	public static var _tabID:Number = 2;
	private var _className = "LearnerIndexView";
	
	private var _bgPanel:MovieClip;
	
	private var _tm:ThemeManager;
	private var _tip:ToolTip;
	
	private var mm:MonitorModel;
	
	//IndexButton
	private var _indexButton:IndexButton;
	private var _indexButton_mc:MovieClip;
	private var _buttonsPanel_mc:MovieClip;
	private var displayedButtons:Array;
	
	private var dispatchEvent:Function; 
	public var addEventListener:Function;
	public var removeEventListener:Function;
	
	private var nextPosition:Number;
	private var btnWidth:Number;
	
	private var backBtn:MovieClip;
	private var nextBtn:MovieClip;
	private var goBtn:MovieClip;
	private var clrBtn:MovieClip;
	private var textFieldBackground_mc:MovieClip;
	private var labelBackground_mc:MovieClip;
	private var idxTextField:TextField;
	private var textFieldContents:String;
	private var defaultString:String;
	private var rangeLabel;
	//private var labelBackground;
		
	private var buttonsShown:Boolean;
	private var drawButtons:Boolean; // buttons to the right of last index button
	
	private var direction:String;

	/**
	* Constructor
	*/
	function LearnerIndexView(){
		Debugger.log("LearnerIndexView Constructor", Debugger.CRITICAL, "LearnerIndexView", "LearnerIndexView");

		_tm = ThemeManager.getInstance();
		_tip = new ToolTip();
		
		nextPosition = 0;
		btnWidth = 45;
		buttonsShown = false;
		drawButtons = true;
		defaultString = "Enter Page Number";
		
		this._visible = false;
		displayedButtons = new Array();
		
        //Init for event delegation
        mx.events.EventDispatcher.initialize(this);
	}
	
	public function textFieldHasFocus() {
		Debugger.log("idxTextField.text: "+idxTextField.text, Debugger.CRITICAL, "textFieldHasFocus", "LearnerIndexView");
		if (idxTextField.text == defaultString) {
			idxTextField.text = "";
		}
	}

	public function init(m:Observable,c:Controller){
		Debugger.log("LearnerIndexView init", Debugger.CRITICAL, "init", "LearnerIndexView");
		//Invoke superconstructor, which sets up MVC relationships.
		super (m, c);
		
		mm = MonitorModel(model)
		mm.learnerIndexView = this;
		
		
		MovieClipUtils.doLater(Proxy.create(this,draw)); 
    }

	public function update (o:Observable,infoObj:Object):Void {
		Debugger.log("LearnerIndexView update", Debugger.GEN, "update", "LearnerIndexView");
		var mm:MonitorModel = MonitorModel(o);

		switch (infoObj.updateType){
			case 'TABCHANGE' :
				if (infoObj.tabID == _tabID && !mm.locked && mm.numIndexButtons>1) {
					setupButtons(mm);
					this._visible = true;
				}else {
					this._visible = false;
				}
				break;
			case 'POSITION' :
				if (infoObj.tabID == _tabID && !mm.locked){
					setPosition(mm);
				}
				break;
			case 'SIZE' :
				if (infoObj.tabID == _tabID && !mm.locked){
					setSize(mm);
				}
				break;
			case 'DRAW_DESIGN' :
				if (infoObj.tabID == _tabID && !mm.locked && mm.numIndexButtons>1) {
					if (!buttonsShown || newButtonsNeeded(mm)) {
						drawButtons = true;
						setupButtons(mm);
						this._visible = true;						
					}
					else
						updatePageLabel();
				}
				break;
			case 'DRAW_BUTTONS' : // this event is only fired when << or >> buttons clicked as it doesn't redraw learnertabview contents
				if (infoObj.tabID == _tabID && !mm.locked && mm.numIndexButtons>1) {
					//if ((mm.numIndexButtons > displayedButtons.length)) {
					if (!buttonsShown || (mm.numIndexButtons > displayedButtons.length)) {
						drawButtons = false;
						setupButtons(mm);
						this._visible = true;
					}
				}
				break;
			default :
				Debugger.log('unknown update type :' + infoObj.updateType,Debugger.GEN,'update','org.lamsfoundation.lams.MonitorTabView');
		}
	}
	
	private function newButtonsNeeded(mm:MonitorModel):Boolean {
		if ((mm.numIndexButtons > displayedButtons.length) && (displayedButtons.length < mm.numPreferredIndexButtons)) {
			mm.updateIndexButtons(); // need to update mm.lastDisplayedIndexButton because it will need to be redrawn
			return true;	
		}
		return false;
	}
	
	public function updatePageLabel():Void {
		rangeLabel.text = "Page " + mm.currentLearnerIndex + " of " + mm.numIndexButtons;
	}
		
	private function draw(){
		setStyles();
		dispatchEvent({type:'load',target:this});
	}
	
	/**
	 * Get the CSSStyleDeclaration objects for each component and apply them
	 * directly to the instance
	 */
	private function setStyles():Void{
		var styleObj = _tm.getStyleObject('IdxBar');
		//bkg_pnl.setStyle('styleName',styleObj);
		
		//var styleObj = _tm.getStyleObject('IndexBar');
	}
	
	public function setupButtons(mm:MonitorModel):Void {
		rangeLabel.text = "Page " + mm.currentLearnerIndex + " of " + mm.numIndexButtons;
		if ((displayedButtons.length > 0) && (drawButtons == true)) {
			Debugger.log("[displayedButtons.length > 0] => removing buttons", Debugger.CRITICAL, "setupButtons", "LearnerIndexView");
			removeButtons();
		}
		
		if (drawButtons == true) {
			_buttonsPanel_mc = this.createEmptyMovieClip("_buttonsPanel_mc", DepthManager.kTop);
			addRangeLabel(mm);
		}
			addIndexButtons(mm); // if drawButtons = false, just rename labels
			
		if (drawButtons == true) {
			addNavigationButtons(mm);
			addIndexTextField(mm);
			addGoButton(mm);
		
			buttonsShown = true;
			direction = null;
			nextPosition = 0;
		}
	}
	
	public function removeButtons(){
		Debugger.log("Removing Index Buttons", Debugger.GEN, "removeButtons", "LearnerIndexView");
		textFieldContents = String(idxTextField.text);
		
		//labelBackground_mc.removeMovieClip(rangeLabel);
		_buttonsPanel_mc.removeMovieClip(rangeLabel);
		while (displayedButtons.length != 0) {
			var idxBtn:MovieClip = MovieClip(displayedButtons.pop());
			_buttonsPanel_mc.removeMovieClip(idxBtn);
		}

		_buttonsPanel_mc.removeMovieClip(backBtn);
		_buttonsPanel_mc.removeMovieClip(nextBtn);
			
		//need to remove the text field from the background
		textFieldBackground_mc.removeMovieClip(idxTextField);
		_buttonsPanel_mc.removeMovieClip(textFieldBackground_mc);
	
		_buttonsPanel_mc.removeMovieClip(goBtn);
	}
	
	private function addRangeLabel(mm:MonitorModel):Void {
		// Label that displays 'Page # of #'
		var idxLabel_mc:MovieClip = _buttonsPanel_mc.attachMovie("Label", "rangeLabel", _buttonsPanel_mc.getNextHighestDepth());
		rangeLabel = _buttonsPanel_mc["rangeLabel"];
		rangeLabel._x = 0;
		rangeLabel._width = 90;
		rangeLabel.autoSize = "center"
		rangeLabel.text = "Page " + mm.currentLearnerIndex + " of " + mm.numIndexButtons;
		nextPosition += rangeLabel._width;
	}
	
	private function addIndexButtons(mm:MonitorModel):Void {
		// The index buttons
		Debugger.log("mm.numIndexButtons: "+mm.numIndexButtons, Debugger.GEN, "addIndexButton", "LearnerIndexView");
		Debugger.log("mm.firstDisplayedIndexButton: "+mm.firstDisplayedIndexButton, Debugger.GEN, "addIndexButton", "LearnerIndexView");
		Debugger.log("mm.lastDisplayedIndexButton: "+mm.lastDisplayedIndexButton, Debugger.GEN, "addIndexButton", "LearnerIndexView");
		var count:Number = 0;
		
		for (var i=mm.firstDisplayedIndexButton; i<=mm.lastDisplayedIndexButton; i++) {	
			if (drawButtons == true) {
				var idxBtn:MovieClip = _buttonsPanel_mc.attachMovie("IndexButton", "idxBtn"+i, _buttonsPanel_mc.getNextHighestDepth(), {_width: btnWidth, _labelText: String(i)});	
				_indexButton = IndexButton(idxBtn);
				_indexButton.init(mm, undefined);
				displayedButtons.push(idxBtn);
				idxBtn._x = nextPosition;
				nextPosition += btnWidth;
			} else {
				_indexButton = IndexButton(displayedButtons[count]);
				//displayedButtons[count]._labelText = String(i);
				displayedButtons[count].label = String(i);
				nextPosition += btnWidth
				count++;
			}
		}
	}
	
	private function addNavigationButtons(mm:MonitorModel):Void {
		// add navigation buttons
		backBtn = _buttonsPanel_mc.attachMovie("IndexButton", "backBtn", _buttonsPanel_mc.getNextHighestDepth(), {_width: btnWidth-5, _labelText: "<<"});	
		_indexButton = IndexButton(backBtn);
		_indexButton.init(mm, undefined);
		backBtn._x = nextPosition;
		nextPosition += (btnWidth-5);
		
		nextBtn = _buttonsPanel_mc.attachMovie("IndexButton", "nextBtn", _buttonsPanel_mc.getNextHighestDepth(), {_width: btnWidth-5, _labelText: ">>"});
		_indexButton = IndexButton(nextBtn);
		_indexButton.init(mm, undefined);
		nextBtn._x = nextPosition;
		nextPosition += (btnWidth-5);
	}
	
	private function addIndexTextField(mm:MonitorModel):Void {
		
		_buttonsPanel_mc.attachMovie("textFieldBackground", "textFieldBackground_mc", _buttonsPanel_mc.getNextHighestDepth(), {_x: nextPosition, _y: 0});

		var textFieldBackground = _buttonsPanel_mc["textFieldBackground_mc"];
		textFieldBackground.createTextField("idxTextField", textFieldBackground.getNextHighestDepth(), 0, 0, 100, 20);
		
		idxTextField = textFieldBackground["idxTextField"];
		idxTextField._visible = true;
		idxTextField.enabled = true;
		idxTextField._editable = true;
		idxTextField.type = "input";
		idxTextField.autosize = "center"
		Debugger.log("textFieldContents: "+textFieldContents, Debugger.CRITICAL, "addIndexTextField", "LearnerIndexView");
		Debugger.log("idxTextField.text: "+idxTextField.text, Debugger.CRITICAL, "addIndexTextField", "LearnerIndexView");
		idxTextField.text = (textFieldContents == undefined) ? defaultString : textFieldContents;
		nextPosition += idxTextField._width;
		
		idxTextField.onSetFocus = Delegate.create(this, textFieldHasFocus);
	}
	
	private function addGoButton(mm:MonitorModel):Void {
		goBtn = _buttonsPanel_mc.attachMovie("IndexButton", "goBtn", _buttonsPanel_mc.getNextHighestDepth(), {_width: btnWidth, _labelText: "Go"});
		_indexButton = IndexButton(goBtn);
		_indexButton.init(mm, undefined);
		goBtn._x = nextPosition;
		nextPosition += (btnWidth);
	}

	private function setPosition(mm:MonitorModel):Void{		
		var p:Object = mm.getPosition(); 
		
		this._x = p.x;
		this._y = 0;
	}
	
	public function setSize(mm:MonitorModel):Void{
		_bgPanel._width = mm.getSize().w;
	}

	public function getController():MonitorController{
		var c:Controller = super.getController();
		return MonitorController(c);
	}
	

    public function defaultController (model:Observable):Controller {
        return new MonitorController(model);
    }
	
	public function getIndexView():MovieClip{
		return this;
	}
	
	public function getRangeLabel():Label {
		return rangeLabel;
	}
	
	public function getIdxTextField():TextField {
		return idxTextField;
	}
}