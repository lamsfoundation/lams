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

/**
 * Page index panel when LearnerTabView is active
 * @author Daniel Carlier
 */
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
	private var indexViewBtn:MovieClip;
	private var textFieldBackground_mc:MovieClip;
	private var labelBackground_mc:MovieClip;
	private var idxTextField:TextField;
	private var _textFieldContents:String;
	private var defaultString:String;
	private var rangeLabel;
	private var btnSpacing:Number;
	private var txtFieldSpacing:Number;
	private var untranslatedWidth:Number;
	private var fontWidthVariance:Number;
		
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
		btnWidth = 40;
		btnSpacing = 20;
		txtFieldSpacing = 5;
		fontWidthVariance = 1.15; // Tahoma font requires greater width
		buttonsShown = false;
		navigationButtonsDrawn = false;
		defaultString = Dictionary.getValue("mv_search_default_txt");
		untranslatedWidth = Math.ceil(StringUtils.getButtonWidthForStr('?') * fontWidthVariance); 		
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
					mm.resetLearnerIndexBar();
					_textFieldContents = defaultString;
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
					setStyles();
					mm.updateIndexButtons();
					setupButtons(mm);
					this._visible = true;						

				}
				break;
			case 'DRAW_BUTTONS' : // this event is only fired when << or >> buttons clicked as it doesn't redraw learnertabview contents
				if (infoObj.tabID == _tabID && !mm.locked && mm.numIndexButtons>1) {
					if (!buttonsShown || (mm.numIndexButtons > displayedButtons.length)) {
						setStyles();
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
		var styleObj = _tm.getStyleObject('IndexBar');
		var _bgPanelColor:Color = new Color(_bgPanel);
		drawOutline();
	}
	
	public function drawOutline():Void {
		var outline_mc = this.createEmptyMovieClip("outline_mc", _bgPanel.getNextHighestDepth());
		var outline = this['outline_mc'];
	
		outline.lineStyle(0, 0x000000, 100);
		outline.lineTo(10000, 0);  // TODO: base this on mm.getSize().w or Stage._width instead of 10000
		outline.lineTo(10000, 20); // similarly
		outline.lineTo(0, 20);
		outline.lineTo(0, 0);
	}
	
	public function setupButtons(mm:MonitorModel):Void {
		rangeLabel.text = Dictionary.getValue('mv_search_current_page_lbl', [mm.currentLearnerIndex, mm.numIndexButtons]);
		Debugger.log("displayedButtons.length: "+displayedButtons.length, Debugger.CRITICAL, "setupButtons", "LearnerIndexView");
		if (!navigationButtonsDrawn	&& mm.numIndexButtons > displayedButtons.length && displayedButtons.length == mm.numPreferredIndexButtons)
			mm.drawIndexButtons = true;
			
		if ((displayedButtons.length > 0) && (mm.drawIndexButtons)) {
			removeButtons();
		}

		if (mm.drawIndexButtons) {
			_buttonsPanel_mc = this.createEmptyMovieClip("_buttonsPanel_mc", DepthManager.kTop);
			addRangeLabel(mm);
			addIndexTextField(mm);
			addGoButton(mm);
			if (mm.inSearchView) {
				addIndexViewButton(mm);
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

		if (mm.drawIndexButtons) {
			addOrderCheckBox(mm);
		}
	
		nextPosition = 0;
	}
	
	public function removeButtons(){
		
		_buttonsPanel_mc.removeMovieClip();
		_buttonsPanel_mc = null;
	}
	
	private function addRangeLabel(mm:MonitorModel):Void {
		// Label that displays 'Page # of #'
		_buttonsPanel_mc.attachMovie("Label", "rangeLabel", _buttonsPanel_mc.getNextHighestDepth());
		rangeLabel = _buttonsPanel_mc["rangeLabel"];
		
		// style info
		var styleObj = _tm.getStyleObject('IndexButton');
		rangeLabel.setStyle('styleName', styleObj);
		
		rangeLabel._x = 0;
		rangeLabel.autoSize = "center"
		rangeLabel.text = Dictionary.getValue('mv_search_current_page_lbl', [mm.currentLearnerIndex, mm.numIndexButtons]);
		var generatedWidth:Number = Math.ceil(StringUtils.getButtonWidthForStr(String(rangeLabel.text)) * fontWidthVariance);
		rangeLabel._width = (generatedWidth <= untranslatedWidth) ? 90 : generatedWidth + btnSpacing;
		
		nextPosition += rangeLabel._width;
	}
	
	private function addBackNavigationButton(mm:MonitorModel):Void {
		// add back navigation button
		var generatedWidth:Number = StringUtils.getButtonWidthForStr('<<');
		var backIdxBtnWidth:Number = (generatedWidth <= untranslatedWidth) ? btnWidth : generatedWidth + btnSpacing;
		backBtn = _buttonsPanel_mc.attachMovie("IndexButton", "backBtn", _buttonsPanel_mc.getNextHighestDepth(), {_width: backIdxBtnWidth, _labelText: "<<"});	
		_indexButton = IndexButton(backBtn);
		_indexButton.init(mm, undefined);
		_indexButton.btnType = "Previous";
		backBtn._x = nextPosition;
		nextPosition += (backIdxBtnWidth);
	}
	
	private function addIndexButtons(mm:MonitorModel):Void {
		// The index buttons
		Debugger.log("mm.numIndexButtons: "+mm.numIndexButtons, Debugger.GEN, "addIndexButton", "LearnerIndexView");
		Debugger.log("mm.firstDisplayedIndexButton: "+mm.firstDisplayedIndexButton, Debugger.GEN, "addIndexButton", "LearnerIndexView");
		Debugger.log("mm.lastDisplayedIndexButton: "+mm.lastDisplayedIndexButton, Debugger.GEN, "addIndexButton", "LearnerIndexView");
		var count:Number = 0;

		if (mm.inSearchView && (mm.firstDisplayedIndexButton == mm.lastDisplayedIndexButton)) {
			// do nothing
			// won't draw numbered index buttons if in search view and there's only one page of results
		}
		else {
			for (var i=mm.firstDisplayedIndexButton; i<=mm.lastDisplayedIndexButton; i++) {	
				if (mm.drawIndexButtons) {
					var idxBtn:MovieClip = _buttonsPanel_mc.attachMovie("IndexButton", "idxBtn"+i, _buttonsPanel_mc.getNextHighestDepth(), {_width: btnWidth, _labelText: String(i)});	
					_indexButton = IndexButton(idxBtn);
					_indexButton.init(mm, undefined);
					_indexButton.btnType = "Numeric";
					displayedButtons.push(idxBtn);
					idxBtn._x = nextPosition;
					nextPosition += btnWidth;
				} else {
					_indexButton = IndexButton(displayedButtons[count]);
					displayedButtons[count].label = String(i);
					displayedButtons[count]._width = btnWidth;
					nextPosition += btnWidth;
					count++;
				}
			}
		}
	}
		
	private function addForwardNavigationButton(mm:MonitorModel):Void {
		var generatedWidth:Number = StringUtils.getButtonWidthForStr('>>');
		var forwardIdxBtnWidth:Number = (generatedWidth <= untranslatedWidth) ? btnWidth : generatedWidth + btnSpacing;
		nextBtn = _buttonsPanel_mc.attachMovie("IndexButton", "nextBtn", _buttonsPanel_mc.getNextHighestDepth(), {_width: forwardIdxBtnWidth, _labelText: ">>"});
		_indexButton = IndexButton(nextBtn);
		_indexButton.init(mm, undefined);
		_indexButton.btnType = "Next";
		nextBtn._x = nextPosition;
		nextPosition += (forwardIdxBtnWidth);
	}

	private function addOrderCheckBox(mm:MonitorModel):Void {
		
		var checkBoxWidth:Number = Math.ceil(StringUtils.getButtonWidthForStr(Dictionary.getValue('order_learners_by_completion_lbl')) * fontWidthVariance) + 20; 	
		_buttonsPanel_mc.attachMovie("CheckBox", "orderByCompletion_chk", _buttonsPanel_mc.getNextHighestDepth(), {_x:Stage.width-checkBoxWidth, _y:0, _width: checkBoxWidth, label:Dictionary.getValue('order_learners_by_completion_lbl')});
		var orderByCompletion_chk = _buttonsPanel_mc['orderByCompletion_chk'];
		orderByCompletion_chk.selected = (mm.getLearnerSortingMechanism() == "completion");
		orderByCompletion_chk.addEventListener("click", Delegate.create(this, setSortingMechanism));
		
		// TODO: this should go in the setStyles method
		var styleObj = _tm.getStyleObject('label');
		orderByCompletion_chk.setStyle('styleName',styleObj);
	}
	
	private function setSortingMechanism(evt:Object):Void {

		if (evt.target.selected) {
			mm.setLearnerSortingMechanism("completion");
		} else {
			mm.setLearnerSortingMechanism("alphabetically");
		}
	}
	
	private function addIndexTextField(mm:MonitorModel):Void {
		
		_buttonsPanel_mc.attachMovie("textFieldBackground", "textFieldBackground_mc", _buttonsPanel_mc.getNextHighestDepth(), {_x: nextPosition, _y: 0});

		var textFieldBackground = _buttonsPanel_mc["textFieldBackground_mc"];

		var textFieldColor:Color = new Color(textFieldBackground);

		var generatedWidth:Number = Math.round(StringUtils.getButtonWidthForStr(defaultString) * fontWidthVariance);
		textFieldBackground._width = (generatedWidth <= untranslatedWidth) ? 175 : generatedWidth + txtFieldSpacing;

		textFieldBackground.createTextField("idxTextField", textFieldBackground.getNextHighestDepth(), 0, 0, textFieldBackground._width, 20);
		
		idxTextField = textFieldBackground["idxTextField"];

		idxTextField._visible = true;
		idxTextField.enabled = true;
		idxTextField._editable = true;
		idxTextField.type = "input";
		idxTextField.autosize = "center"
		
		// style info
		var styleObj = _tm.getStyleObject('IndexTextField');
		var txtFmt:TextFormat = new TextFormat();
		txtFmt.font = styleObj.fontFamily;
		txtFmt.size = styleObj.fontSize;
		txtFmt.color = styleObj.color;
		idxTextField.setNewTextFormat(txtFmt);

		if (!mm.resetSearchTextField)
			idxTextField.text = (_textFieldContents == undefined) ? defaultString : _textFieldContents;
		else {
			idxTextField.text = defaultString;
			mm.resetSearchTextField = false;
		}
		nextPosition += idxTextField._width;
		
		idxTextField.onSetFocus = Delegate.create(this, textFieldHasFocus);
	}
	
	public function set textFieldContents(s:String) {
		_textFieldContents = s;
	}
	
	private function addGoButton(mm:MonitorModel):Void {
		var generatedWidth:Number = Math.ceil(StringUtils.getButtonWidthForStr(Dictionary.getValue('mv_search_go_btn_lbl')) * fontWidthVariance);
		var goBtnWidth:Number = (generatedWidth <= untranslatedWidth) ? btnWidth : generatedWidth + btnSpacing;
		goBtn = _buttonsPanel_mc.attachMovie("IndexButton", "goBtn", _buttonsPanel_mc.getNextHighestDepth(), {_width: goBtnWidth, _labelText: Dictionary.getValue('mv_search_go_btn_lbl')});
		_indexButton = IndexButton(goBtn);
		_indexButton.init(mm, undefined);
		_indexButton.btnType = "Go";
		goBtn._x = nextPosition;
		nextPosition += (goBtnWidth);
	}
	
	private function addIndexViewButton(mm:MonitorModel):Void {
		var generatedWidth:Number = Math.round(StringUtils.getButtonWidthForStr(Dictionary.getValue('mv_search_index_view_btn_lbl')) * fontWidthVariance);
		var indexViewBtnWidth:Number = (generatedWidth <= untranslatedWidth) ? 93 : generatedWidth + btnSpacing;
		indexViewBtn = _buttonsPanel_mc.attachMovie("IndexButton", "indexViewBtn", _buttonsPanel_mc.getNextHighestDepth(), {_width: indexViewBtnWidth, _labelText: Dictionary.getValue('mv_search_index_view_btn_lbl')});
		_indexButton = IndexButton(indexViewBtn);
		_indexButton.init(mm, undefined);
		_indexButton.btnType = "IndexView";
		indexViewBtn._x = nextPosition;
		nextPosition += indexViewBtnWidth;
		nextPosition--;
	}

	private function setPosition(mm:MonitorModel):Void{		
		var p:Object = mm.getPosition(); 
		
		this._x = p.x;
		this._y = 0;
	}
	
	public function setSize(mm:MonitorModel):Void{
		var panelOffset:Number = mm.getSize().w/100;
		_bgPanel._width = Math.round(mm.getSize().w + panelOffset);
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
	
	public function getIndexViewBtn():MovieClip {
		return indexViewBtn;
	}
}