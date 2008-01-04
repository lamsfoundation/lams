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

import mx.managers.*;
import mx.containers.*;
import mx.events.*;
import mx.utils.*;
import mx.controls.*;

class org.lamsfoundation.lams.monitoring.mv.IndexButton extends MovieClip {
	
	public static var _tabID:Number = 2;
	private var _className = "IndexButton";
	
	private var _bgPanel:MovieClip;
	private var btnWidth:Number;
	
	private var idxLabel_mc:MovieClip;
	private var idxLabel:Label;
	private var _labelText:String;
	
	private var matchesArr:Array;
	
	private var _tm:ThemeManager;
	private var mm:MonitorModel;
	
	/**
	* Called to Indexbutton. Called by LearnerIndexView
	*/
	public function init(m:Observable, c:Controller){
		super (m, c);
		mm = MonitorModel(m);
		
		_tm = ThemeManager.getInstance();
		
		btnWidth = 45;
		_bgPanel._width = btnWidth;
		
		idxLabel_mc = this.attachMovie("Label", "idxLabel", this.getNextHighestDepth(), {text:_labelText, _width: 45, autoSize: "center"});
		idxLabel = Label(idxLabel_mc);
		
		_bgPanel.onRollOver = Delegate.create(this, onMouseOver);
		_bgPanel.onPress = Delegate.create(this, indexClicked);
    }
		
	public function onMouseOver(): Void {
		Debugger.log("onMouseOver event triggered", Debugger.GEN, "onMouseOver", "IndexButton");
		// TODO: some cool mouse over effects
	}
	
	public function indexClicked(): Void {
		Selection.setFocus(mm.getMonitor().getMV().getMonitorLearnerScp()); // take focus off idx textfield as it interferes << and >> button clicks
		var buttonText:String = String(label.text)
		if (buttonText == "<<") {
			Debugger.log("<< clicked", Debugger.GEN, "indexClicked", "IndexButton");
			mm.drawIndexButtons = false;
			mm.updateIndexButtons("<<");
		} else if (buttonText == ">>") {
			Debugger.log(">> clicked", Debugger.GEN, "indexClicked", "IndexButton");
			mm.drawIndexButtons = false;
			mm.updateIndexButtons(">>");
		} else if (buttonText == Dictionary.getValue('mv_search_go_btn_lbl')) { // 'Go' button
			mm.learnerIndexView.textFieldContents = String(mm.learnerIndexView.getIdxTextField().text); // backup the string incase need to remove textfield
			if(!isNaN(mm.learnerIndexView.getIdxTextField().text)) { // if the text field contains a number
				var idx:Number = Number(mm.learnerIndexView.getIdxTextField().text);
				if (idx >= 1 && idx <= mm.numIndexButtons) { // if the selected index exists
					mm.drawIndexButtons = false;
					mm.currentLearnerIndex = idx;
					if (!mm.inSearchView)
						mm.oldIndex = mm.currentLearnerIndex;
				}
				else
					LFMessage.showMessageAlert(Dictionary.getValue('mv_search_invalid_input_msg', [mm.numIndexButtons]), null);
			} 
			else if (mm.learnerIndexView.getIdxTextField().text == "") {
				LFMessage.showMessageAlert(Dictionary.getValue('mv_search_error_msg', [mm.numIndexButtons]), null);
			}
			else {
				var mc:MonitorController = mm.getMonitor().getMV().getController();
				matchesArr = mc.searchForLearners(String(mm.learnerIndexView.getIdxTextField().text));
				if (matchesArr.length > 0) {
					mm.drawIndexButtons = true;
					mm.currentLearnerIndexNoRedraw = 1;
					mm.searchResults = matchesArr;
					var indexViewBtn:MovieClip = mm.getMonitor().getMV().getLearnerIndexPanel().indexViewBtn;
					indexViewBtn._visible = true;
				} else {
					LFMessage.showMessageAlert(Dictionary.getValue('mv_search_not_found_msg', [mm.learnerIndexView.getIdxTextField().text]), null);
				}
			}
		}
		else if (buttonText == Dictionary.getValue('mv_search_index_view_btn_lbl')) { // the 'Index View' button
			mm.drawIndexButtons = true;
			mm.inSearchView = false;
			mm.resetSearchTextField = true;
			mm.currentLearnerIndexNoRedraw = mm.oldIndex;
			mm.setLessonProgressData(mm.progressArrBackup);
		}
		else {
			mm.drawIndexButtons = false;
			mm.indexSelected = true;
			mm.currentLearnerIndex = Number(label.text);
			if (!mm.inSearchView)
				mm.oldIndex = mm.currentLearnerIndex;
		}
		
	}
	
	public function setSize(_btnWidth:Number):Void {
		this._width = _btnWidth;
		
		idxLabel._width = _btnWidth;
		_bgPanel._width = _btnWidth;
	}
	
	public function set label(a:String):Void {
		idxLabel.text = a;
	}
	
	public function get label():Label {
		return idxLabel;
	}
}