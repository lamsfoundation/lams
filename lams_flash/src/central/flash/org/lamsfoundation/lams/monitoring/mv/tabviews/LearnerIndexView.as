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
import org.lamsfoundation.lams.common.ToolTip;
import org.lamsfoundation.lams.authoring.Transition;


//import org.lamsfoundation.lams.common.*
//import org.lamsfoundation.lams.monitoring.Application;

import mx.managers.*;
import mx.containers.*;
import mx.events.*;
import mx.utils.*;
import mx.controls.*;

class org.lamsfoundation.lams.monitoring.mv.tabviews.LearnerIndexView extends AbstractView {
	
	public static var _tabID:Number = 2;
	private var _className = "LearnerIndexView";
	
	private var _tm:ThemeManager;
	
	private var _tip:ToolTip;
	
	private var mm:MonitorModel;
	
	private var _learnerIndexView:LearnerIndexView;
	
	//IndexButton
	private var _indexButton:IndexButton;
	private var _indexButton_mc:MovieClip;
	
	private var dispatchEvent:Function; 
	public var addEventListener:Function;
	public var removeEventListener:Function;

	/**
	* Constructor
	*/
	function LearnerIndexView(){
		Debugger.log("LearnerIndexView Constructor", Debugger.CRITICAL, "LearnerIndexView", "LearnerIndexView");
		_learnerIndexView = this;
		
		_tm = ThemeManager.getInstance();
		_tip = new ToolTip();
		
		this._visible = false;
				
        //Init for event delegation
        mx.events.EventDispatcher.initialize(this);
	}
	

	public function init(m:Observable,c:Controller){
		Debugger.log("LearnerIndexView init", Debugger.CRITICAL, "init", "LearnerIndexView");
		//Invoke superconstructor, which sets up MVC relationships.
		super (m, c);
		mm = MonitorModel(model)
		
		setPosition(mm);
		
		MovieClipUtils.doLater(Proxy.create(this,draw)); 
		mm.getMonitor().getMV().getMonitorLearnerScp()._visible = false;
    }

	public function update (o:Observable,infoObj:Object):Void {
		Debugger.log("LearnerIndexView update", Debugger.CRITICAL, "update", "LearnerIndexView");
		var mm:MonitorModel = MonitorModel(o);

		switch (infoObj.updateType){
			case 'POSITION' :
				setPosition(mm);
				break;
			case 'SIZE' :
				setSize(mm);
				break;
			case 'TABCHANGE' :
				if (infoObj.tabID == _tabID && !mm.locked){
					this._visible = true;
				}else {
					this._visible = false;
				}
				break;
			default :
				Debugger.log('unknown update type :' + infoObj.updateType,Debugger.CRITICAL,'update','org.lamsfoundation.lams.MonitorTabView');
		}
	}
	

	private function draw(){
		Debugger.log("LearnerIndexView draw", Debugger.CRITICAL, "draw", "LearnerIndexView");

		var s:Object = mm.getSize();

		mm.learnerIndexView = this;

		//var idx1:MovieClip = this.attachMovie("indexButton", "indexButton1", this.getNextHighestDepth(), {_x:0, _y:0});
		var idx1:MovieClip = this.attachMovie("IndexButton", "indexButton1", this.getNextHighestDepth()); //, {idxLabel.text: "ttest"}
		var idx2:MovieClip = this.attachMovie("indexButton", "indexButton2", this.getNextHighestDepth());
		var idx3:MovieClip = this.attachMovie("indexButton", "indexButton3", this.getNextHighestDepth());
	
		_indexButton = IndexButton(idx1);
		_indexButton.label = "1";
		
		_indexButton = IndexButton(idx2);
		_indexButton.label = "2";
		
		_indexButton = IndexButton(idx3);
		_indexButton.label = "3";
		
		Debugger.log("_indexButton.label: "+_indexButton.label, Debugger.CRITICAL, "draw", "LearnerIndexView");
		Debugger.log("_indexButton: "+_indexButton, Debugger.CRITICAL, "draw", "LearnerIndexView");
		//_indexButton.init();
		
		//_index
		//var tstLabel:Label = idx1.attachMovie("Label", "tstLabel", idx1.getNextHighestDepth());
		//idx1.createTextField("txt1", idx1.getNextHighestDepth(), -1000, -1000, 0, 0);
		//txt1.text = "TEST";
		//var EP_btn_label:String = Dictionary.getValue('learner_exportPortfolio_btn');

		//_nameLayer_mc.attachMovie("Button", "learnerName"+learner.getLearnerId()+"_btn", _nameLayer_mc.getNextHighestDepth(),{label:EP_btn_label, _x:z.w-110, _y:ACT_Y+2, styleName:styleObj} )	

		idx1._x = 0;
		idx2._x = 100;
		idx3._x = 200;
		
		//var ib1:IndexButton = new IndexButton();
		
		//var idxbtn1:MovieClip = idx1.createClassObject(IndexButton, "idxbtn1", this.getNextHighestDepth());
		//Debugger.log("idxbtn1.getDepth(): "+idxbtn1.getDepth(), Debugger.CRITICAL, "draw", "LearnerIndexView");
		
		//idxbtn1._x = 243;
		//idx1.attachMovie("ib1", "ib1", idx1.getNextHighestDepth());
		
		//var tstMovie:MovieClip = new MovieClip();
		//idx1.createEmptyMovieClip("idx1emc", this.getNextHighestDepth());
		//idx1emc.
		//idx1.attachMovie(tstMovie, "tst)
		
		//setupLabels();
		
		dispatchEvent({type:'load',target:this});
	}
	
	public function setupLabels():Void {
		
		
		//_indexButton = IndexButton(idx1);
		//_indexButton.init()
		//learnerIndexView_mc = _monitorPanels_mc.attachMovie("LearnerIndexView", "learnerIndexView_mc", _monitorPanels_mc.getNextHighestDepth());
		//learnerIndexView = LearnerIndexView(learnerIndexView_mc);
		//learnerIndexView.init(mm, undefined);
	}
	
	public function getNumLearners(mm:MonitorModel): Number {
		var s:MonitorModel = MonitorModel(getModel());
		Debugger.log("Num learners: "+s.getlearnerTabActArr().length, Debugger.CRITICAL, "getNumLearners", "LearnerIndexView");
		
		return s.getlearnerTabActArr().length;
	}

	private function setPosition(mm:MonitorModel):Void{
		Debugger.log("LearnerIndexView setPosition", Debugger.CRITICAL, "setPosition", "LearnerIndexView");
		var p:Object = mm.getPosition(); 
		
		this._x = p.x;
		this._y = 0;
		
	}
	
	private function setSize(mm:MonitorModel):Void{
		Debugger.log("LearnerIndexView setSize", Debugger.CRITICAL, "setSize", "LearnerIndexView");
		//var s:Object = mm.getSize();
		
		//TODO fixes width of learnerIndexView components, but there's prob a better way
		var s:Object = MonitorModel(getModel()).getSize(); 

		//bg_pnl.setSize(s.w, bg_pnl.height);
		//bar_pnl.setSize(s.w-20, bar_pnl.height);
		//mm.endGate.tt_btn.setSize(s.w, bg_pnl.height);
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
}