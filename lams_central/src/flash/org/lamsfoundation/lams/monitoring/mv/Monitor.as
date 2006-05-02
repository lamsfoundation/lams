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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ************************************************************************
 */
import org.lamsfoundation.lams.monitoring.Application;
import org.lamsfoundation.lams.monitoring.mv.*;
import org.lamsfoundation.lams.monitoring.mv.tabviews.*;
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.* 

import mx.utils.*
import mx.managers.*;
import mx.events.*;

class org.lamsfoundation.lams.monitoring.mv.Monitor {
	//Constants
	public static var USE_PROPERTY_INSPECTOR = true;
	

	private var _className:String = "Monitor";

	// Model
	private var monitorModel:MonitorModel;
	// View
	private var monitorView:MonitorView;
	private var monitorView_mc:MovieClip;

	private var app:Application;
	private var _dictionary:Dictionary;

	private var _pi:MovieClip; //Property inspector
	
	private var dispatchEvent:Function;       
    public var addEventListener:Function;  
    public var removeEventListener:Function;
	
	/**
	 * Monitor Constructor Function
	 * 
	 * @usage   
	 * @return  target_mc		//Target clip for attaching view
	 */
	public function Monitor(target_mc:MovieClip,depth:Number,x:Number,y:Number,w:Number,h:Number){
		mx.events.EventDispatcher.initialize(this);
		
		//Create the model
		monitorModel = new MonitorModel(this);
		
		_dictionary = Dictionary.getInstance();

		//Create the view
		monitorView_mc = target_mc.createChildAtDepth("monitorView",DepthManager.kTop);	
		
		trace(monitorView_mc);
		
		monitorView = MonitorView(monitorView_mc);
		
		
		monitorView.init(monitorModel,undefined,x,y,w,h);
		
        monitorView.addEventListener('load',Proxy.create(this,viewLoaded));
		//lessonTabView_mc = monitorView_mc.
		
       //dictionary.addEventListener('init',Proxy.create(this,setupPI));
		
		//Register view with model to receive update events
		monitorModel.addObserver(monitorView);
		
		//monitorModel.addObserver(monitorView);
		//monitorModel.addObserver(monitorView);
		//monitorModel.addObserver(monitorView);

        //Set the position by setting the model which will call update on the view
        monitorModel.setPosition(x,y);
		monitorModel.setSize(w,h);

		//Get reference to application and design data model
		//app = Application.getInstance();
		
		
	}
	
	/**
	* event broadcast when Monitor is loaded 
	*/ 
	public function broadcastInit(){
		dispatchEvent({type:'init',target:this});		
	}
 
	
	private function viewLoaded(evt:Object){
        Debugger.log('viewLoaded called',Debugger.GEN,'viewLoaded','Monitor');
		
		if(evt.type=='load') {
            dispatchEvent({type:'load',target:this});
        }else {
            //Raise error for unrecognized event
        }
    }
	
	
	/**
	* Used by application to set the size
	* @param width The desired width
	* @param height the desired height
	*/
	public function setSize(width:Number, height:Number):Void{
		monitorModel.setSize(width, height);
	}
    
    public function setPosition(x:Number,y:Number){
        //Set the position within limits
        //TODO DI 24/05/05 write validation on limits
        monitorModel.setPosition(x,y);
    }
	
	//Dimension accessor methods
	public function get width():Number{
		return monitorModel.width;
	}
	
	public function get height():Number{
		return monitorModel.height;
	}
	
	public function get x():Number{
		return monitorModel.x;
	}
	
	public function get y():Number{
		return monitorModel.y;
	}

    function get className():String { 
        return _className;
    }
	public function getMM():MonitorModel{
		return monitorModel;
	}
	public function getMV():MonitorView{
		return monitorView;
	}
}