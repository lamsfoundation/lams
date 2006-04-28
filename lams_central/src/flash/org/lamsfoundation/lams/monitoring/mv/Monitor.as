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

//import org.lamsfoundation.lams.authoring.cv.*
import org.lamsfoundation.lams.monitoring.mv.*
import org.lamsfoundation.lams.authoring.tk.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.monitoring.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.* 
import mx.managers.*
import mx.utils.*

/**
 * The canvas is the main screen area of the LAMS application where activies are added and sequenced
 * Note - This holds the DesignDataModel _ddm 
 * @version 1.0
 * @since   
 */
class org.lamsfoundation.lams.monitoring.mv.Monitor {
	
	//Constants
	public static var USE_PROPERTY_INSPECTOR = true;
	// Model
	private var monitorModel:MonitorModel;
	// View
	private var monitorView:MonitorView;
	private var monitorView_mc:MovieClip;
	
	private var _className:String = "Monitor";
	
	private var app:Application;
	private var _dictionary:Dictionary;
	
	
    private var _pi:MovieClip; //Property inspector
	
	//Defined so compiler can 'see' events added at runtime by EventDispatcher
	private var dispatchEvent:Function;       
    public var addEventListener:Function;  
    public var removeEventListener:Function;
	
	
	/**
	* Canvas Constructor
	*
	* @param   target_mc	Target clip for attaching view
	*/
	public function Monitor (target_mc:MovieClip,depth:Number,x:Number,y:Number,w:Number,h:Number){
        mx.events.EventDispatcher.initialize(this);
        
		//Design Data Model.
		_ddm = new DesignDataModel();
		
		//Create the model.
		//pass in a ref to this container
		monitorModel = new MonitorModel(this);
		
		_dictionary = Dictionary.getInstance();
		
		//Create the view
		_monitorView_mc = target_mc.createChildAtDepth("monitorView",DepthManager.kTop);		

        //Cast monitor view clip as MonitorView and initialise passing in model
		monitorView = MonitorView(_monitorView_mc);
		moniotrView.init(monitorModel,undefined,x,y,w,h);
        
        
        //Get reference to application and design data model
		app = Application.getInstance();
		
		_undoStack = new Array();
		_redoStack = new Array();
		
		//some initialisation:


		//Add listener to view so that we know when it's loaded
        monitorView.addEventListener('load',Proxy.create(this,viewLoaded));
        
		//_ddm.addEventListener('ddmUpdate',Proxy.create(this,onDDMUpdated));
		//_ddm.addEventListener('ddmBeforeUpdate',Proxy.create(this,onDDMBeforeUpdate));
		
		

        //Register view with model to receive update events
		monitorModel.addObserver(monitorView);
        

        //Set the position by setting the model which will call update on the view
        monitorModel.setPosition(x,y);
        //Initialise size to the designed size
        monitorModel.setSize(w,h);
		
		//muist comne after the canvasView as we use the defaultController method to get a controller ref.
		_dictionary.addEventListener('init',Proxy.create(this,setupPI));
		
		
		
		//if in monitor, dont do it!
		//initBin();
	}
	
	
	/**
	 * Event dispatcher to setup Property inspector once dictionary items are loaded.
	 * 
	 * @usage   
	 * @return  
	 */
	public function setupPI(){
		if(USE_PROPERTY_INSPECTOR){
			initPropertyInspector();
		}
	}
	
	
	/**
    * Event dispatched from the view once it's loaded
    */
    public function viewLoaded(evt:Object) {
        
        if(evt.type=='load') {
            dispatchEvent({type:'load',target:this});
        }else {
            Debugger.log('Event type not recognised : ' + evt.type,Debugger.CRITICAL,'viewLoaded','Monitor');
        }
    }
	
	/**
	 * Initialises the property inspector
	 * @usage   
	 */
	public function initPropertyInspector():Void{
		//note the init obnject parameters are passed into the _container object in the embeded class (*in this case PropertyInspector)
		//we are setting up a view so we need to pass the model and controller to it
		
		var cc:CanvasController = canvasView.getController();
		_pi = PopUpManager.createPopUp(Application.root, LFWindow, false,{title:Dictionary.getValue('property_inspector_title'),closeButton:true,scrollContentPath:"PropertyInspector",_canvasModel:canvasModel,_canvasController:cc});
		
		//Assign dialog load handler
        _pi.addEventListener('contentLoaded',Delegate.create(this,piLoaded));
        //okClickedCallback = callBack;
    }
	
	/**
	 * Fired whern property inspector's contentLoaded is fired
	 * Positions the PI
	 * @usage   
	 * @param   evt 
	 * @return  
	 */
	public function piLoaded(evt:Object) {
        if(evt.type == 'contentLoaded'){
			//call a resize to line up the PI
			Application.getInstance().onResize();
			
           
        }else {
            //TODO raise wrong event type error 
        }
		
	}
}