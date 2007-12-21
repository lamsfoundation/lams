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
	private var toggleBtn:MovieClip;
	private var textFieldBackground_mc:MovieClip;
	private var labelBackground_mc:MovieClip;
	private var idxTextField:TextField;
	private var _textFieldContents:String;
	private var defaultString:String;
	private var rangeLabel;
		
	private var buttonsShown:Boolean;
	private var navigationButtonsDrawn:Boolean;
	
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
		navigationButtonsDrawn = false;
		defaultString = "Enter search query or page no.";
		
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
				if (infoObj.tabID == _tabID && !mm.locked && (mm.numIndexButtons>1 || mm.inSearchView)) {
					mm.updateIndexButtons();
					setupButtons(mm);
					this._visible = true;						

				}
				break;
			case 'DRAW_BUTTONS' : // this event is only fired when << or >> buttons clicked as it doesn't redraw learnertabview contents
				if (infoObj.tabID == _tabID && !mm.locked && mm.numIndexButtons>1) {
					if (!buttonsShown || (mm.numIndexButtons > displayedButtons.length)) {
						//drawButtons = false;
						setupButtons(mm); // this only renames the index buttons as drawbuttons equals false
						this._visible = true;
					}
				}
				break;
			default :
				Debugger.log('unknown update type :' + infoObj.updateType,Debugger.GEN,'update','org.lamsfoundation.lams.MonitorTabView');
		}
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
	
	/*public function setupButtons(mm:MonitorModel):Void {
		rangeLabel.text = "Page " + mm.currentLearnerIndex + " of " + mm.numIndexButtons;
		if ((displayedButtons.length > 0) && (drawButtons == true)) {
			removeButtons();
		}
		
		if (drawButtons == true) {
			_buttonsPanel_mc = this.createEmptyMovieClip("_buttonsPanel_mc", DepthManager.kTop);
			addRangeLabel(mm);
			if (mm.numIndexButtons > mm.numPreferredIndexButtons) {
				addBackNavigationButton(mm);
			}
		}
			addIndexButtons(mm); // if drawButtons = false, just rename labels
			
		if (drawButtons == true) {
			if (mm.numIndexButtons > mm.numPreferredIndexButtons) {
				addForwardNavigationButton(mm);
				navigationButtonsDrawn = true;
			}
			addIndexTextField(mm);
			addGoButton(mm);
			addToggleViewButton(mm);
		
			buttonsShown = true;
			direction = null;
		}
		//if (mm.inSearchView)
		//	toggleBtn._visible = true;
		
		nextPosition = 0;
	}*/
	public function setupButtons(mm:MonitorModel):Void {
		
		rangeLabel.text = "Page " + mm.currentLearnerIndex + " of " + mm.numIndexButtons;
		
		if (!navigationButtonsDrawn	&& mm.numIndexButtons > displayedButtons.length && displayedButtons.length == mm.numPreferredIndexButtons)
			mm.drawIndexButtons = true;	
			
		var dib = mm.drawIndexButtons; // false on refresh should be true
		Debugger.log("setupButtons dib: "+dib, Debugger.CRITICAL, "setupButtons", "LearnerIndexView");
		if ((displayedButtons.length > 0) && (mm.drawIndexButtons)) {
			removeButtons();
		}
		
		if (mm.drawIndexButtons) {
			_buttonsPanel_mc = this.createEmptyMovieClip("_buttonsPanel_mc", DepthManager.kTop);
			addRangeLabel(mm);
			addIndexTextField(mm);
			addGoButton(mm);
			if (mm.inSearchView) {
				addToggleViewButton(mm);
			}
		}
		
		if (mm.drawIndexButtons) {
			if (mm.numIndexButtons > mm.numPreferredIndexButtons) {
				addBackNavigationButton(mm);
			}
		}
		
		// if drawButtons = false, just rename labels
		addIndexButtons(mm);
		
		if (mm.drawIndexButtons) {
			if (mm.numIndexButtons > mm.numPreferredIndexButtons) {
				addForwardNavigationButton(mm);
				navigationButtonsDrawn = true;
				buttonsShown = true;
				direction = null;
			}
		}
		
		nextPosition = 0;
	}
	
	public function removeButtons(){
		Debugger.log("Removing Index Buttons", Debugger.GEN, "removeButtons", "LearnerIndexView");
		
		//labelBackground_mc.removeMovieClip(rangeLabel);
		_buttonsPanel_mc.removeMovieClip(rangeLabel);
		
		if (mm.numIndexButtons > mm.numPreferredIndexButtons)
			_buttonsPanel_mc.removeMovieClip(backBtn);
		
		while (displayedButtons.length != 0) {
			var idxBtn:MovieClip = MovieClip(displayedButtons.pop());
			_buttonsPanel_mc.removeMovieClip(idxBtn);
		}
		
		if (mm.numIndexButtons > mm.numPreferredIndexButtons) {
			_buttonsPanel_mc.removeMovieClip(nextBtn);
			navigationButtonsDrawn = false;
		}
			
		//need to remove the text field from the background
		textFieldBackground_mc.removeMovieClip(idxTextField);
		_buttonsPanel_mc.removeMovieClip(textFieldBackground_mc);
	
		_buttonsPanel_mc.removeMovieClip(goBtn);
		_buttonsPanel_mc.removeMovieClip(toggleBtn);
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
	
	private function addBackNavigationButton(mm:MonitorModel):Void {
		// add back navigation button
		backBtn = _buttonsPanel_mc.attachMovie("IndexButton", "backBtn", _buttonsPanel_mc.getNextHighestDepth(), {_width: btnWidth-5, _labelText: "<<"});	
		_indexButton = IndexButton(backBtn);
		_indexButton.init(mm, undefined);
		backBtn._x = nextPosition;
		nextPosition += (btnWidth-5);
	}
	
	private function addIndexButtons(mm:MonitorModel):Void {
		// The index buttons
		Debugger.log("mm.numIndexButtons: "+mm.numIndexButtons, Debugger.GEN, "addIndexButton", "LearnerIndexView");
		Debugger.log("mm.firstDisplayedIndexButton: "+mm.firstDisplayedIndexButton, Debugger.GEN, "addIndexButton", "LearnerIndexView");
		Debugger.log("mm.lastDisplayedIndexButton: "+mm.lastDisplayedIndexButton, Debugger.GEN, "addIndexButton", "LearnerIndexView");
		var count:Number = 0;

		Debugger.log("displayedButtons[displayedButtons.length-1].label.text: "+displayedButtons[displayedButtons.length-1].label.text, Debugger.GEN, "addIndexButton", "LearnerIndexView");

		if (mm.inSearchView && (mm.firstDisplayedIndexButton == mm.lastDisplayedIndexButton)) {
			// do nothing
			// won't draw numbered index buttons if in search view and there's only one page of results
		}
		else {
			Debugger.log("this should be false mm.drawIndexButtons: "+mm.drawIndexButtons, Debugger.GEN, "addIndexButton", "LearnerIndexView");
			for (var i=mm.firstDisplayedIndexButton; i<=mm.lastDisplayedIndexButton; i++) {	
				if (mm.drawIndexButtons) {
					var idxBtn:MovieClip = _buttonsPanel_mc.attachMovie("IndexButton", "idxBtn"+i, _buttonsPanel_mc.getNextHighestDepth(), {_width: btnWidth, _labelText: String(i)});	
					_indexButton = IndexButton(idxBtn);
					_indexButton.init(mm, undefined);
					displayedButtons.push(idxBtn);
					idxBtn._x = nextPosition;
					nextPosition += btnWidth;
				} else {

					_indexButton = IndexButton(displayedButtons[count]);
					displayedButtons[count].label = String(i);
					nextPosition += btnWidth
					count++;
				}
			}
		}
	}
		
	private function addForwardNavigationButton(mm:MonitorModel):Void {
		nextBtn = _buttonsPanel_mc.attachMovie("IndexButton", "nextBtn", _buttonsPanel_mc.getNextHighestDepth(), {_width: btnWidth-5, _labelText: ">>"});
		_indexButton = IndexButton(nextBtn);
		_indexButton.init(mm, undefined);
		nextBtn._x = nextPosition;
		nextPosition += (btnWidth-5);
	}
	
	private function addIndexTextField(mm:MonitorModel):Void {
		
		_buttonsPanel_mc.attachMovie("textFieldBackground", "textFieldBackground_mc", _buttonsPanel_mc.getNextHighestDepth(), {_x: nextPosition, _y: 0});

		var textFieldBackground = _buttonsPanel_mc["textFieldBackground_mc"];
		textFieldBackground._width = 150;
		
		textFieldBackground.createTextField("idxTextField", textFieldBackground.getNextHighestDepth(), 0, 0, 150, 20);
		
		idxTextField = textFieldBackground["idxTextField"];
		idxTextField._visible = true;
		idxTextField.enabled = true;
		idxTextField._editable = true;
		idxTextField.type = "input";
		idxTextField.autosize = "center"
		idxTextField.text = (_textFieldContents == undefined) ? defaultString : _textFieldContents;
		nextPosition += idxTextField._width;
		
		idxTextField.onSetFocus = Delegate.create(this, textFieldHasFocus);
	}
	
	public function set textFieldContents(s:String) {
		_textFieldContents = s;
	}
	
	private function addGoButton(mm:MonitorModel):Void {
		goBtn = _buttonsPanel_mc.attachMovie("IndexButton", "goBtn", _buttonsPanel_mc.getNextHighestDepth(), {_width: btnWidth, _labelText: "Go"});
		_indexButton = IndexButton(goBtn);
		_indexButton.init(mm, undefined);
		goBtn._x = nextPosition;
		nextPosition += (btnWidth);
	}
	
	private function addToggleViewButton(mm:MonitorModel):Void {
		toggleBtn = _buttonsPanel_mc.attachMovie("IndexButton", "toggleBtn", _buttonsPanel_mc.getNextHighestDepth(), {_width: (btnWidth * 2), _labelText: "Index View"});
		_indexButton = IndexButton(toggleBtn);
		_indexButton.init(mm, undefined);
		toggleBtn._x = nextPosition;
		nextPosition += (btnWidth * 2);
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
	
	public function getToggleBtn():MovieClip {
		return toggleBtn;
	}
}