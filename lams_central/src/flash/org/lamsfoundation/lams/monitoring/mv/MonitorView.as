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

import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.authoring.cv.*
import org.lamsfoundation.lams.authoring.*
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.mvc.*
import mx.managers.*
import mx.containers.*;
import mx.events.*
import mx.utils.*
import mx.events.ItemClickEvent;
import mx.controls.TabBar;


/**
*Monitoring view for the Monitor
* Relects changes in the MonitorModel
*/

class org.lamsfoundation.lams.monitoring.mv.MonitorView extends AbstractView{
	//constants:
	private var GRID_HEIGHT:Number;
	private var GRID_WIDTH:Number;
	private var H_GAP:Number;
	private var V_GAP:Number;
	
	private var _tm:ThemeManager;
	
	//Canvas clip
	private var _monitor_mc:MovieClip;
	private var monitor_scp:ScrollPane;
	private var monitorTabs_tb:TabBar;
    private var bkg_pnl:Panel;
	//private var act_pnl:Panel;
	
    private var _gridLayer_mc:MovieClip;
    //private var _transitionLayer_mc:MovieClip;
	//private var _activityLayerComplex_mc:MovieClip;
	//private var _activityLayer_mc:MovieClip;
	
	//private var _transitionPropertiesOK:Function;
    private var _monitorView:MonitorView;
    //Defined so compiler can 'see' events added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	//public var menu:ContextMenu;

	
	/**
	* Constructor
	*/
	function MonitorView(){
		_monitorView = this;
		_tm = ThemeManager.getInstance();
        //Init for event delegation
        mx.events.EventDispatcher.initialize(this);
	}
	
	/**
	* Called to initialise Canvas  . CAlled by the Canvas container
	*/
	public function init(m:Observable,c:Controller,x:Number,y:Number,w:Number,h:Number){
		//Invoke superconstructor, which sets up MVC relationships.
		//if(c==undefined){
		//	c==defaultController();
		//}
		super (m, c);
        //Set up parameters for the grid
		H_GAP = 10;
		V_GAP = 10;
        //setupCM();
	   //register to recive updates form the model
		MonitorModel(m).addEventListener('viewUpdate',this);
        
		MovieClipUtils.doLater(Proxy.create(this,draw)); 
    }    
	
	/**
 * Recieved update events from the CanvasModel. Dispatches to relevent handler depending on update.Type
 * @usage   
 * @param   event
 */
public function viewUpdate(event:Object):Void{
		Debugger.log('Recived an Event dispather UPDATE!, updateType:'+event.updateType+', target'+event.target,4,'viewUpdate','CanvasView');
		 //Update view from info object
        //Debugger.log('Recived an UPDATE!, updateType:'+infoObj.updateType,4,'update','CanvasView');
       var cm:CanvasModel = event.target;
	   
	   switch (event.updateType){
            case 'POSITION' :
                setPosition(cm);
                break;
            case 'SIZE' :
                setSize(cm);
                break;
            default :
                Debugger.log('unknown update type :' + event.updateType,Debugger.CRITICAL,'update','org.lamsfoundation.lams.CanvasView');
		}

	}
	
	/**
    * layout visual elements on the canvas on initialisation
    */
	private function draw(){
		//get the content path for the sp
		_monitor_mc = monitor_scp.content;
		//Debugger.log('_canvas_mc'+_canvas_mc,Debugger.GEN,'draw','CanvasView');
		
    
		var tab_arr:Array = [{label:"Lesson", data:"lesson"}, {label:"Monitor", data:"monitor"}, {label:"Learners", data:"learners"}, {label:"Todo", data:"todo"}];
		
		monitorTabs_tb.dataProvider = tab_arr;
		monitorTabs_tb.selectedIndex = 0;
		
		monitorTabs_tb.addEventListener("change", Delegate.create(this,clickEvt));
		
		bkg_pnl = _monitor_mc.createClassObject(Panel, "bkg_pnl", getNextHighestDepth());

		//set up the 
		//_canvas_mc = this;
		_gridLayer_mc = _monitor_mc.createEmptyMovieClip("_gridLayer_mc", _monitor_mc.getNextHighestDepth());
		
		bkg_pnl.useHandCursor = false;
		
		//setStyles();
	    dispatchEvent({type:'load',target:this});
	}
	
	/**
	 * Event listener for when when tab is clicked
	 * 
	 * @usage   
	 * @param   evt 
	 * @return  
	 */
	private function clickEvt(evt:ItemClickEvent):Void{
		   trace(evt.target);
		   trace("test: "+ String(evt.target.selectedIndex))
					//forClick.text="label is: " + evt.itemIndex.label + " index is: " + evt.index + " capital is: " +               targetComp.dataProvider[evt.index].data;
		}
	/**
    * Sets the size of the canvas on stage, called from update
    */
	private function setSize(mm:MonitorModel):Void{
        var s:Object = mm.getSize();
		monitor_scp.setSize(s.w,s.h);
		bkg_pnl.setSize(s.w,s.h);

		//Create the grid.  The gris is re-drawn each time the canvas is resized.
		var grid_mc = Grid.drawGrid(_gridLayer_mc,Math.round(s.w),Math.round(s.h),V_GAP,H_GAP);
				
	}
}