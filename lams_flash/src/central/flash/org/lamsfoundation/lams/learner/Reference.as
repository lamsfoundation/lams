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
import org.lamsfoundation.lams.common.ApplicationParent;
import mx.controls.*
import mx.utils.*
import mx.managers.*
import mx.events.*

/**
* Reference
* Represents the reference activity panel in learner
*/

class Reference extends MovieClip {
	
	//Height Properties
	private var refHeightHide:Number;
	private var refHeightFull:Number;
	
	//Open Close Identifier
	private var _refIsExpanded:Boolean;
	
	//Component properties
	private var _reference_mc:MovieClip;
	
	private var refHead_pnl:MovieClip;
	private var refTitle_lbl:Label;
	private var _container:MovieClip;		// Holding Container
	private var minIcon:MovieClip;
	private var maxIcon:MovieClip;
	private var clickTarget_mc:MovieClip;
	private var _tip:ToolTip;
	
	private var panel:MovieClip;       //The underlaying panel base
    private var _lessonModel:LessonModel;
	private var _lessonController:LessonController;
	private var _tm:ThemeManager;
	private var _dictionary:Dictionary;
	
	private var _numActivities:Number;
	
	//These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
   
	/** 
	* constructor
	*/
	public function Reference() {
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
        //Delete the enterframe dispatcher
        delete this.onEnterFrame;
		_lessonModel = _lessonModel;
		_lessonController = _lessonController;
		//_scratchpad_mc = this;
		_reference_mc = this;
		_refIsExpanded = true;
		maxIcon._visible = false;
		minIcon._visible = true;
		
		refHeightHide = 20;
		
		if (_numActivities == null) _numActivities = 0;
		
		setLabels();
		
		clickTarget_mc.onRelease = Proxy.create (this, localOnRelease);
		clickTarget_mc.onReleaseOutside = Proxy.create (this, localOnReleaseOutside);
		
		this.onEnterFrame = setLabels;
		
	}
	
	public function localOnRelease():Void{
		
		if (_refIsExpanded){
			_refIsExpanded = false
			minIcon._visible = false;
			maxIcon._visible = true;
			_lessonModel.setReferenceHeight(refHeightHide);
			
		}else {
			_refIsExpanded = true
			minIcon._visible = true;
			maxIcon._visible = false;
			
			if (_numActivities > 0)
				refHeightFull = 39 + _numActivities * 30;
			
			_lessonModel.setReferenceHeight(refHeightFull);
		}
	}
	
	public function isRefExpanded():Boolean{
		return _refIsExpanded;
	}
	
	public function refFullHeight():Number{
		return refHeightFull;
	}
	
	public function set numActivities(num:Number) {
		_numActivities = num;
		refHeightFull = (_numActivities > 0) ? (39 + _numActivities * 30) : 20;
		_lessonModel.setReferenceHeight(refHeightFull);
	}
	
	public function get numActivities():Number {
		return _numActivities;
	}
	
	public function localOnReleaseOutside():Void{
		Debugger.log('Release outside so no event has been fired, current state is: ' + _refIsExpanded,Debugger.GEN,'localOnReleaseOutside','Scratch Pad');
	}
	
	private function setStyles(){
		var styleObj = _tm.getStyleObject('BGPanel');
		refHead_pnl.setStyle('styleName',styleObj);
	}
	
	private function setLabels(){
		refTitle_lbl.text = Dictionary.getValue("support_acts_title");
		setStyles();
		
		delete this.onEnterFrame; 
		
		dispatchEvent({type:'load',target:this});
	}
	
	function get className():String { 
        return 'Reference';
    }
}