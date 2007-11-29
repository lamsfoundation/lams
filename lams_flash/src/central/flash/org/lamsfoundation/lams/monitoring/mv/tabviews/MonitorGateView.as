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
 
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.*
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.monitoring.mv.*
import org.lamsfoundation.lams.monitoring.mv.tabviews.*;
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.mvc.* 
import org.lamsfoundation.lams.monitoring.Application;

import mx.managers.*
import mx.containers.*;
import mx.events.*
import mx.utils.*
import mx.controls.*;

/*
* Monitor Gate View for the monitor
* Reflects Changes in the MonitorModel
*/
 class org.lamsfoundation.lams.monitoring.mv.tabviews.MonitorGateView extends AbstractView {
	
	public static var _tabID:Number = 1;
	private var _className = "MonitorGateView";
	
	private var _tm:ThemeManager;
	
	private var _tip:ToolTip;
	
	private var mm:MonitorModel;
	private var _monitorGateView:MonitorGateView;
	
	private var learner_X:Number = 22;
	private var learner_Y:Number = 19;
	
	private var lessonEnd_lbl:Label;
	private var finishedLearnersList:Array;
	private var bg_pnl:MovieClip;
	private var doorClosed:MovieClip;
	private var doorOpen:MovieClip;
	private var bar_pnl:MovieClip;
	private var _learnerContainer_mc:MovieClip;
	
    private var dispatchEvent:Function; 
	public var addEventListener:Function;
	
	/**
	* Constructor
	*/
	function MonitorGateView(){
		_monitorGateView = this;
	
		_tm = ThemeManager.getInstance();
		_tip = new ToolTip();
		
		this._visible = false;		
		
        //Init for event delegation
        mx.events.EventDispatcher.initialize(this);
	}
	
	/**
	* Called to initialise gate view. Called by setupTabInit method in MonitorView class
	*/
	public function init(m:Observable,c:Controller){
		//Invoke superconstructor, which sets up MVC relationships.
		super (m, c);
		mm = MonitorModel(model)
		
		setPosition(mm);
		
		MovieClipUtils.doLater(Proxy.create(this, draw)); 
    }   
	
	public function update (o:Observable,infoObj:Object):Void{
		   var mm:MonitorModel = MonitorModel(o);
		   
		   switch (infoObj.updateType){
				case 'POSITION' :
					setPosition(mm);
					break;
				case 'SIZE' :
					setPosition(mm);
					setSize(mm);
					break;
				case 'TABCHANGE' :
					if (infoObj.tabID == _tabID && !mm.locked){
						this._visible = true;
					}else {
						this._visible = false;
					}
			
					break;
				case 'DRAW_DESIGN' :
					if (infoObj.tabID == _tabID && !mm.locked){
						
						setStyles();
						setSize(mm);
						
						showEndGateData(mm);
						mm.drawDesign(infoObj.tabID);
					}
					break;
				default :
					Debugger.log('unknown update type :' + infoObj.updateType,Debugger.CRITICAL,'update','org.lamsfoundation.lams.MonitorTabView');
		}
	}
		
	/**
    * layout visual elements on the MonitorTabView on initialisation
    */
	private function draw(){
		_learnerContainer_mc = this.createEmptyMovieClip("_learnerContainer_mc", DepthManager.kTop);
		
		var s:Object = mm.getSize();
		
		mm.endGate = this;
		
		mm.endGate.tt_btn.onRollOver = Proxy.create(this,this['showToolTip'], "finish_learner_tooltip");
		mm.endGate.tt_btn.onRollOut =  Proxy.create(this,this['hideToolTip']);
				
		setStyles();

		dispatchEvent({type:'load',target:this});
	}
	
	public function showToolTip(btnTT:String):Void{
		var Xpos = this._x + 5;
		var Ypos = this._y + this._height;
		var ttHolder = Application.tooltip;	
		var ttMessage = Dictionary.getValue(btnTT);
		Debugger.log("ttMessage"+ttMessage, Debugger.CRITICAL);

		//param "true" is to specify that tooltip needs to be shown above the component 
		_tip.DisplayToolTip(ttHolder, ttMessage, Xpos, Ypos, true);
	}
	
	public function hideToolTip():Void{
		_tip.CloseToolTip();
	}
	
	private function showEndGateData(mm:MonitorModel):Void{
		Debugger.log("p1 in draw segd", Debugger.CRITICAL, "showEndGateData", "MonitorGateView");
		var mc = getController();
		var finishedLearners:Number = 0; 
		var totalLearners:Number = mm.allLearnersProgress.length;
		
		doorClosed._visible = true;
		finishedLearnersList = new Array();
		
		for (var i=0; i<mm.allLearnersProgress.length; i++){
			if (mm.allLearnersProgress[i].isLessonComplete()){
				
				var learner:Object = new Object();
				learner = mm.allLearnersProgress[i]
				var temp_mc = _learnerContainer_mc.attachMovie("learnerIcon", "learnerIcon"+learner.getUserName(), _learnerContainer_mc.getNextHighestDepth(),{learner:learner, _monitorController:mc, _x:learner_X+(finishedLearners*10), _y:(learner_Y), _hasPlus:false});
				finishedLearnersList.push(temp_mc);
				finishedLearners++;
				
				var learnerIcon_mc = _learnerContainer_mc["learnerIcon"+learner.getUserName()];
				learnerIcon_mc.init();
			}
		}
		
		lessonEnd_lbl.text = "<b>"+Dictionary.getValue('title_sequencetab_endGate')+"</b> "+finishedLearners+" of "+ totalLearners;
		Debugger.log("p2 in draw segd", Debugger.CRITICAL, "showEndGateData", "MonitorGateView");
	}
			
	/**
    * Sets the position of the gate, called from update
    * @param cm Canvas model object 
    */
	private function setPosition(mm:MonitorModel):Void{
		var p:Object = mm.getPosition(); 
		
		this._x = p.x;
		this._y = mm.getSize().h - 40.7;
	}
	
	/**
	 * Get the CSSStyleDeclaration objects for each component and apply them
	 * directly to the instance
	 */
	private function setStyles():Void{
		var styleObj = _tm.getStyleObject('MHPanel');
		bg_pnl.setStyle('styleName',styleObj);
		styleObj = _tm.getStyleObject('BGPanel');
		bar_pnl.setStyle('styleName',styleObj);
		styleObj = _tm.getStyleObject('EndGatelabel');
		lessonEnd_lbl.setStyle('styleName',styleObj);
	}
	
	private function setSize(mm:MonitorModel):Void{

		//var s:Object = mm.getSize();
		
		//TODO fixes width of endgate components, but there's prob a better way
		var s:Object = MonitorModel(getModel()).getSize(); 

		bg_pnl.setSize(s.w, bg_pnl.height);
		bar_pnl.setSize(s.w-20, bar_pnl.height);
		mm.endGate.tt_btn.setSize(s.w, bg_pnl.height);
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
	
	/*
    * Returns the default controller for this view.
    */
    public function defaultController (model:Observable):Controller {
        return new MonitorController(model);
    }
	
	public function getEndGate():MovieClip{
		return this;
	}
}