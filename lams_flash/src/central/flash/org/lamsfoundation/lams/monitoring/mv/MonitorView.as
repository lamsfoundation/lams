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
import org.lamsfoundation.lams.monitoring.mv.tabviews.*
import org.lamsfoundation.lams.monitoring.*;
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.mvc.*
import org.lamsfoundation.lams.common.Config;
import org.lamsfoundation.lams.common.ToolTip;
import org.lamsfoundation.lams.common.ApplicationParent;

import mx.managers.*
import mx.containers.*
import mx.events.*
import mx.utils.*
import mx.controls.*


/**
*Monitoring view for the Monitor
* Relects changes in the MonitorModel
*/

class org.lamsfoundation.lams.monitoring.mv.MonitorView extends AbstractView{
	
	private var _className = "MonitorView";
	
	//constants:
	private var GRID_HEIGHT:Number;
	private var GRID_WIDTH:Number;
	private var H_GAP:Number;
	private var V_GAP:Number;
	private var Offset_Y_TabLayer_mc:Number;
	private var _tm:ThemeManager;
	private var _tip:ToolTip;
	
	private var _monitorView_mc:MovieClip;
	
	//Canvas clip
	
	private var _monitorLesson_mc:MovieClip;
	private var monitorLesson_scp:MovieClip;
	private var _monitorSequence_mc:MovieClip;
	private var monitorSequence_scp:MovieClip;
	private var _monitorLearner_mc:MovieClip
	private var monitorLearner_scp:MovieClip;
	private var monitorTabs_tb:MovieClip;
	private var learnerMenuBar:MovieClip;
    private var bkg_pnl:MovieClip;
	private var bkgHeader_pnl:MovieClip;
	
    private var _gridLayer_mc:MovieClip;
    private var _lessonTabLayer_mc:MovieClip;
	private var _monitorTabLayer_mc:MovieClip;
	private var _learnerTabLayer_mc:MovieClip;
	private var _monitorPanels_mc:MovieClip;
	private var _todoTabLayer_mc:MovieClip;
	private var _editOnFlyLayer_mc:MovieClip;
	private var refresh_btn:Button;
	private var help_btn:Button;
	private var exportPortfolio_btn:Button;
	private var viewJournals_btn:Button;
	private var editFly_btn:Button;
	
    private var _monitorView:MonitorView;
	private var _monitorModel:MonitorModel;
	
	//Tab Views Initialisers
	
	//LessonTabView
	private var lessonTabView:LessonTabView;
	private var lessonTabView_mc:MovieClip;
	//MonitorTabView
	private var monitorTabView:MonitorTabView;
	private var monitorTabView_mc:MovieClip;
	//MonitorGateView
	private var monitorGateView:MonitorGateView;
	private var monitorGateView_mc:MovieClip;
	//TodoTabView
	private var todoTabView:TodoTabView;
	private var todoTabView_mc:MovieClip;
	//LearnerTabView
	private var learnerTabView:LearnerTabView;
	private var learnerTabView_mc:MovieClip;
	//LearnerIndexView
	private var learnerIndexView:LearnerIndexView;
	private var learnerIndexView_mc:MovieClip;
	
	private var _monitorController:MonitorController;
	
	private var lessonTabLoaded;
	private var monitorTabLoaded;
	private var monitorGateLoaded;
	private var learnerTabLoaded;
	private var learnerIndexLoaded;
	
    //Defined so compiler can 'see' events added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	
	
	/**
	* Constructor
	*/
	function MonitorView(){
		_monitorView = this;
		_tm = ThemeManager.getInstance();
		_tip = new ToolTip();
		
		lessonTabLoaded = false;
		monitorTabLoaded = false;
		monitorGateLoaded = false;
		learnerTabLoaded = false;
		learnerIndexLoaded = false;
		
		//Init for event delegation
        mx.events.EventDispatcher.initialize(this);
	}
	
	/**
	* Called to initialise Canvas  . Called by the Canvas container
	*/
	public function init(m:Observable,c:Controller,x:Number,y:Number,w:Number,h:Number){

		super (m, c);
        //Set up parameters for the grid
		H_GAP = 10;
		V_GAP = 10;
		//_monitorModel = getModel();
		bkg_pnl._visible = false;
		
		MovieClipUtils.doLater(Proxy.create(this,draw)); 
		
    }    
	
	private function tabLoaded(evt:Object){
        Debugger.log('tabLoaded called: ' + evt.target,Debugger.GEN,'tabLoaded','MonitorView');
		
		if(evt.type=='load') {
			var tgt:String = new String(evt.target);
            if(tgt.indexOf('lessonTabView_mc') != -1) { lessonTabLoaded = true; }
			else if(tgt.indexOf('monitorTabView_mc') != -1) { monitorTabLoaded = true; }
			else if(tgt.indexOf('monitorGateView_mc') != -1) { monitorGateLoaded = true; }
			else if(tgt.indexOf('learnerTabView_mc') != -1) { learnerTabLoaded = true; }
			else if(tgt.indexOf('learnerIndexView_mc') != -1) { learnerIndexLoaded = true; }
			else Debugger.log('not recognised instance ' + evt.target,Debugger.GEN,'tabLoaded','MonitorView');
		
			if(lessonTabLoaded && monitorTabLoaded && learnerTabLoaded && monitorGateLoaded && learnerIndexLoaded) { 
				dispatchEvent({type:'tload',target:this}); 
			}
			
        }else {
            //Raise error for unrecognized event
        }
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
			case 'TABCHANGE' :
				showData(mm);
				break;
			case 'EXPORTSHOWHIDE' :
				exportShowHide(infoObj.data);
				break;
			case 'JOURNALSSHOWHIDE' :
				journalsShowHide(infoObj.data);
				break;
			case 'EDITFLYSHOWHIDE' :
				editFlyShowHide(infoObj.data);
				break;
            default :
                Debugger.log('unknown update type :' + infoObj.updateType,Debugger.CRITICAL,'update','org.lamsfoundation.lams.MonitorView');
		}

	}
	
	/**
    * Sets the size of the canvas on stage, called from update
    */
	private function showData(mm:MonitorModel):Void{
        var s:Object = mm.getSequence();		
	}
	
	private function exportShowHide(v:Boolean):Void{
		exportPortfolio_btn.visible = v;
	}
	
	private function journalsShowHide(v:Boolean):Void{
		viewJournals_btn.visible = v;
	}
	
	private function editFlyShowHide(v:Boolean):Void{
		Debugger.log("test root val: " + _root.editOnFly, Debugger.CRITICAL, "editFlyShowHide", "MonitorView");
		
		editFly_btn.visible = (v && _root.editOnFly == 'true') ? true : false;

		Debugger.log("visible: " + editFly_btn.visible, Debugger.CRITICAL, "editFlyShowHide", "MonitorView");
	}
	
	/**
    * layout visual elements on the canvas on initialisation
    */
	private function draw(){
		var mcontroller = getController();
		
		//get the content path for Tabs
		_monitorLesson_mc = monitorLesson_scp.content;
		_monitorSequence_mc = monitorSequence_scp.content;
		_monitorLearner_mc = monitorLearner_scp.content;
		
		_lessonTabLayer_mc = _monitorLesson_mc.createEmptyMovieClip("_lessonTabLayer_mc", _monitorLesson_mc.getNextHighestDepth());
		_monitorTabLayer_mc = _monitorSequence_mc.createEmptyMovieClip("_monitorTabLayer_mc", _monitorSequence_mc.getNextHighestDepth());
		_learnerTabLayer_mc = _monitorLearner_mc.createEmptyMovieClip("_learnerTabLayer_mc", _monitorLearner_mc.getNextHighestDepth());
		
		var tab_arr:Array = [{label:Dictionary.getValue('mtab_lesson'), data:"lesson"}, {label:Dictionary.getValue('mtab_seq'), data:"monitor"}, {label:Dictionary.getValue('mtab_learners'), data:"learners"}];
		
		monitorTabs_tb.dataProvider = tab_arr;
		monitorTabs_tb.selectedIndex = 0;
		
		refresh_btn.addEventListener("click",mcontroller);
		help_btn.addEventListener("click",mcontroller);
		exportPortfolio_btn.addEventListener("click", mcontroller);
		viewJournals_btn.addEventListener("click", mcontroller);
		editFly_btn.addEventListener("click", mcontroller);
		
		refresh_btn.onRollOver = Proxy.create(this,this['showToolTip'], refresh_btn, "refresh_btn_tooltip");
		refresh_btn.onRollOut = Proxy.create(this,this['hideToolTip']);
		
		help_btn.onRollOver = Proxy.create(this,this['showToolTip'], help_btn, "help_btn_tooltip");
		help_btn.onRollOut = Proxy.create(this,this['hideToolTip']);
		
		exportPortfolio_btn.onRollOver = Proxy.create(this,this['showToolTip'], exportPortfolio_btn, "class_exportPortfolio_btn_tooltip");
		exportPortfolio_btn.onRollOut = Proxy.create(this,this['hideToolTip']);
		
		viewJournals_btn.onRollOver = Proxy.create(this,this['showToolTip'], viewJournals_btn, "learner_viewJournals_btn_tooltip");
		viewJournals_btn.onRollOut = Proxy.create(this,this['hideToolTip']);
		
		editFly_btn.onRollOver = Proxy.create(this,this['showToolTip'], editFly_btn, "ls_sequence_live_edit_btn_tooltip");
		editFly_btn.onRollOut = Proxy.create(this,this['hideToolTip']);
		
		monitorTabs_tb.addEventListener("change",mcontroller);
		
		setLabels();
		setStyles();
		setupTabInit();
	    dispatchEvent({type:'load',target:this});
		
	}
	
	private function setupTabInit(){
		
		var mm:Observable = getModel();
		
		_monitorPanels_mc = this.createEmptyMovieClip('_monitorPanels_mc', DepthManager.kTop);
		
		// Inititialsation for Lesson Tab View 
		lessonTabView_mc = _lessonTabLayer_mc.attachMovie("LessonTabView", "lessonTabView_mc",DepthManager.kTop)
		lessonTabView = LessonTabView(lessonTabView_mc);
		lessonTabView.init(mm, undefined);
		lessonTabView.addEventListener('load',Proxy.create(this,tabLoaded));
		
		// Inititialsation for Monitor Tab View 
		monitorTabView_mc = _monitorTabLayer_mc.attachMovie("MonitorTabView", "monitorTabView_mc",DepthManager.kTop)
		monitorTabView = MonitorTabView(monitorTabView_mc);
		monitorTabView.init(mm, undefined);
		monitorTabView.addEventListener('load',Proxy.create(this,tabLoaded));
		
		// Inititialsation for Monitor Gate View 	
		monitorGateView_mc = _monitorPanels_mc.attachMovie("endGate", "monitorGateView_mc", _monitorPanels_mc.getNextHighestDepth(), {_x: 0, _y: 0});
		monitorGateView = MonitorGateView(monitorGateView_mc);
		monitorGateView.init(mm, undefined);
		monitorGateView.addEventListener('load',Proxy.create(this,tabLoaded));
		
		// Inititialsation for Learner Tab View 
		learnerTabView_mc = _learnerTabLayer_mc.attachMovie("LearnerTabView", "learnerTabView_mc",DepthManager.kTop)
		learnerTabView = LearnerTabView(learnerTabView_mc);
		learnerTabView.init(mm, undefined);
		learnerTabView.addEventListener('load',Proxy.create(this,tabLoaded));
		
		// Inititialsation for Learner Index View
		learnerIndexView_mc = _monitorPanels_mc.attachMovie("LearnerIndexView", "learnerIndexView_mc", _monitorPanels_mc.getNextHighestDepth());
		learnerIndexView = LearnerIndexView(learnerIndexView_mc);
		learnerIndexView.init(mm, undefined);
		learnerIndexView.addEventListener('load',Proxy.create(this,tabLoaded));
		
		mm.addObserver(lessonTabView);
		mm.addObserver(monitorTabView);
		mm.addObserver(monitorGateView);
		mm.addObserver(learnerTabView);
		mm.addObserver(learnerIndexView);
	}
	
	public function showToolTip(btnObj, btnTT:String):Void{
		var btnLabel = btnObj.label;
		var xpos:Number;
		if (btnLabel == "Help"){
			xpos = bkgHeader_pnl.width - 165 //btnObj._x - 105
		}else if (btnLabel == "Refresh"){
			xpos = bkgHeader_pnl.width - 165 //btnObj._x - 40
		}else{
			xpos = btnObj._x
		}
		var Xpos = Application.MONITOR_X+ xpos;
		var Ypos = (Application.MONITOR_Y+ btnObj._y+btnObj.height)+5;
		var ttHolder = Application.tooltip;
		var ttMessage = Dictionary.getValue(btnTT);
		_tip.DisplayToolTip(ttHolder, ttMessage, Xpos, Ypos);
		
	}
	
	public function hideToolTip():Void{
		_tip.CloseToolTip();
	}

	/**
	 * Get the CSSStyleDeclaration objects for each component and apply them
	 * directly to the instance  
	 */
	private function setStyles():Void{
		var styleObj = _tm.getStyleObject('BGPanel');
		bkg_pnl.setStyle('styleName',styleObj);
		styleObj = _tm.getStyleObject('MHPanel');
		bkgHeader_pnl.setStyle('styleName',styleObj);
		styleObj = _tm.getStyleObject('scrollpane');
		monitorLesson_scp.setStyle('styleName',styleObj);
		monitorSequence_scp.setStyle('styleName',styleObj);
		monitorLearner_scp.setStyle('styleName',styleObj);
		styleObj = _tm.getStyleObject('button');
		monitorTabs_tb.setStyle('styleName', styleObj);
		refresh_btn.setStyle('styleName',styleObj);
		exportPortfolio_btn.setStyle('styleName',styleObj);
		help_btn.setStyle('styleName',styleObj);
		viewJournals_btn.setStyle('styleName', styleObj);
		editFly_btn.setStyle('styleName', styleObj);
		
	}
	
	private function setLabels():Void{
		refresh_btn.label = Dictionary.getValue('refresh_btn');
		help_btn.label = Dictionary.getValue('help_btn');
		exportPortfolio_btn.label = Dictionary.getValue('learner_exportPortfolio_btn');
		viewJournals_btn.label = Dictionary.getValue('learner_viewJournals_btn');
		editFly_btn.label = Dictionary.getValue('ls_sequence_live_edit_btn');
	}
		
	/**
    * Sets the size of the canvas on stage, called from update
    */
	private function setSize(mm:MonitorModel):Void{
        var s:Object = mm.getSize();
		bkg_pnl.setSize(s.w,s.h);
		bkgHeader_pnl.setSize(s.w, bkgHeader_pnl._height);
		monitorLesson_scp.setSize(s.w-monitorLesson_scp._x, s.h-monitorLesson_scp._y);
		monitorSequence_scp.setSize(s.w-monitorSequence_scp._x, s.h-40.7);
		monitorLearner_scp.setSize(s.w-monitorLearner_scp._x, s.h-monitorLearner_scp._y);
		monitorGateView_mc.setSize(s.w, 40.7);
		learnerIndexView.setSize(mm);
		
		viewJournals_btn._x = s.w - 260;
		exportPortfolio_btn._x = s.w - 260;
		editFly_btn._x = s.w - 360;
		refresh_btn._x = s.w - 160
		help_btn._x = s.w - 80
	}
	
	 /**
    * Sets the position of the canvas on stage, called from update
    * @param cm Canvas model object 
    */
	private function setPosition(mm:MonitorModel):Void{
        var p:Object = mm.getPosition();
		/*Debugger.log("mm.getSequence().noStartedLearners: "+mm.getSequence().noStartedLearners, Debugger.CRITICAL, "setPosition", "MonitorView");
		Debugger.log("mm.learnersPerPage: "+mm.learnersPerPage, Debugger.CRITICAL, "setPosition", "MonitorView");
		if (Math.ceil(mm.getSequence().noStartedLearners/mm.learnersPerPage) > 1) {*/
			//monitorLearner_scp._y = monitorLearner_scp._y+20; 
			/*Debugger.log("if", Debugger.CRITICAL, "setPosition", "MonitorView");
			
		}
		else {
			Debugger.log("else", Debugger.CRITICAL, "setPosition", "MonitorView");
		}*/
        this._x = p.x;
        this._y = p.y;
	}
	
	public function getLessonTabView():LessonTabView{
		return lessonTabView;
	}
	
	public function getMonitorTabView():MonitorTabView{
		return monitorTabView;
	}
	
	/**
	 * Overrides method in abstract view to ensure correct type of controller is returned
	 * @usage   
	 * @return  CanvasController
	 */
	public function getController():MonitorController{
		var c:Controller = super.getController();
		return MonitorController(c);
	}
	
	public function getMonitorTab():MovieClip{
		return monitorTabs_tb;
	}
	
	public function getMonitorLessonScp():MovieClip{
		return monitorLesson_scp;
	}
	public function getMonitorSequenceScp():MovieClip{
		return monitorSequence_scp;
	}
	public function getMonitorLearnerScp():MovieClip{
		return monitorLearner_scp;
	}
	
	public function getLearnerIndexPanel():MovieClip { 
		return learnerIndexView_mc;
	}

	/*
    * Returns the default controller for this view.
    */
    public function defaultController (model:Observable):Controller {
        return new MonitorController(model);
    }
	
}