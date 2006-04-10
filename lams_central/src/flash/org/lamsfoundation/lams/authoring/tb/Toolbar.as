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

import org.lamsfoundation.lams.authoring.tb.*
import org.lamsfoundation.lams.common.util.*
import mx.managers.*;

/*
* The canvas is the main screen area of the LAMS application where activies are added and sequenced
*/
class Toolbar {
	// Model
	private var toolbarModel:ToolbarModel;
	// View
	private var toolbarView:ToolbarView;
	private var toolbarView_mc:MovieClip;
    
    //These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;

	/*
	* Canvas Constructor
	*
	* @param   target_mc	Target clip for attaching view
	*/
	function Toolbar (target_mc:MovieClip,x:Number,y:Number){
        mx.events.EventDispatcher.initialize(this);
		//Create the model
		toolbarModel = new ToolbarModel();
        
		//Create the view
		toolbarView_mc = target_mc.createChildAtDepth("toolbarView",DepthManager.kTop);		
        
        //Cast toolbar view clip as ToolbarBview and initialise passing in model
		toolbarView = ToolbarView(toolbarView_mc);
		toolbarView.init(toolbarModel,undefined);
        toolbarView.addEventListener('load',Proxy.create(this,viewLoaded));
		
        //Register view with model to receive update events
		toolbarModel.addObserver(toolbarView);

        //Set the position by setting the model which will call update on the view
        toolbarModel.setPosition(x,y);
		
	}
    
    
	/**
	* Used by application to set the size
	* @param width The desired width
	* @param height the desired height
	*/
	
	public function setSize(width:Number, height:Number):Void{
		toolbarModel.setSize(width, height);
	}
    
    public function setPosition(x:Number,y:Number){
        //Set the position within limits
        //TODO DI write validation on limits
        toolbarModel.setPosition(x,y);
    }    
    
    private function viewLoaded(evt:Object){
        Debugger.log('viewLoaded called',Debugger.GEN,'viewLoaded','Toolbar');
		toolbarModel.setDefaultState();
		if(evt.type=='load') {
            dispatchEvent({type:'load',target:this});
        }else {
            //Raise error for unrecognized event
        }
    }
	
	public function setButtonState(btnName:String, btnState:Boolean){
		toolbarModel.setButtonState(btnName, btnState);
	}
    
    function get className():String{
        return 'Toolbar';
    }
	
	public function get width(){
		return toolbarModel.width;
	}
	public function get height(){
		return toolbarModel.height;
	}
}
