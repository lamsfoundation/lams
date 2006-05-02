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
import org.lamsfoundation.lams.monitoring.mv.*
import org.lamsfoundation.lams.monitoring.*;
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.mvc.*
import mx.controls.*;
import mx.managers.*;
import mx.containers.*;
import mx.events.*;
import mx.utils.*;

/**
*Monitoring view for the Monitor
* Relects changes in the MonitorModel
*/

class org.lamsfoundation.lams.monitoring.mv.tabviews.LessonTabView extends AbstractView{
	public static var _tabID:Number = 0;
	private var _className = "LessonTabView";
	//constants:
	private var _tm:ThemeManager;
	
	//TabView clips
	private var _monitor_mc:MovieClip;
	private var requiredTask_scp:MovieClip;
	private var monitorTabs_tb:MovieClip;
	
	//Text Items
    private var LSTitle_txt:TextInput;
	private var LSDescription_txt:TextArea;
	private var sessionStatus_txt:TextInput;
	private var numLearners_txt:TextInput;
	private var group_txt:TextInput;
	private var duration_txt:TextInput;
	
	//Button
	private var viewLearners:Button;
	private var editClass:Button;
	private var selectClass:Button;
	private var status1:Button;
	private var status2:Button;
	private var setDateTime:Button;
	
    //private var _transitionLayer_mc:MovieClip;
	//private var _activityLayerComplex_mc:MovieClip;
	//private var _activityLayer_mc:MovieClip;
	
	//private var _transitionPropertiesOK:Function;
    private var _lessonTabView:LessonTabView;
    //Defined so compiler can 'see' events added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	//public var menu:ContextMenu;

	
	/**
	* Constructor
	*/
	function LesssonTabView(){
		_lessonTabView = this;
		_tm = ThemeManager.getInstance();
        //Init for event delegation
        mx.events.EventDispatcher.initialize(this);
	}
	
	/**
	* Called to initialise Canvas  . CAlled by the Canvas container
	*/
	public function init(m:Observable,c:Controller){
		//Invoke superconstructor, which sets up MVC relationships.
		//if(c==undefined){
		//	c==defaultController();
		//}
		super (m, c);
        //Set up parameters for the grid
		//H_GAP = 10;
		//V_GAP = 10;
        //setupCM();
	    //register to recive updates form the model
		//MonitorModel(m).addEventListener('update',this);
        
		//MovieClipUtils.doLater(Proxy.create(this,draw)); 
    }    
	
	/**
 * Recieved update events from the CanvasModel. Dispatches to relevent handler depending on update.Type
 * @usage   
 * @param   event
 */
public function update (o:Observable,infoObj:Object):Void{
		
       var mm:MonitorModel = MonitorModel(o);
	   
	   switch (infoObj.updateType){
		    case 'SIZE' :
			    setSize(mm);
                break;
			case 'REQUIREDTASKS' :
				trace('REQUIREDTASKS');
                break;
			case 'CLASS' :
				trace('CLASS');
                break;
			case 'STATUS' :
				trace('STATUS');
                break;
			case 'DATE_TIME' :
				trace('DATE_TIME');
                break;
			case 'SEQUENCE' :
				trace("TabID for Selected tab is (LessonTab): "+infoObj.tabID)
				if (infoObj.tabID == _tabID){
					MovieClipUtils.doLater(Proxy.create(this,draw));
				}
				break;
            default :
                Debugger.log('unknown update type :' + infoObj.updateType,Debugger.CRITICAL,'update','org.lamsfoundation.lams.CanvasView');
		}

	}
	
	/**
    * layout visual elements on the canvas on initialisation
    */
	private function draw(){
		//get the content path for the sp
		//_monitor_mc = requiredTask_scp.content;
		//Debugger.log('_canvas_mc'+_canvas_mc,Debugger.GEN,'draw','CanvasView');
		
		trace("Loaded LessonTabView Data")
		//set up the 
		//_canvas_mc = this;
		
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
	//private function clickEvt(evt):Void{
		  // trace(evt.target);
		  // trace("test: "+ String(evt.target.selectedIndex))
					//forClick.text="label is: " + evt.itemIndex.label + " index is: " + evt.index + " capital is: " +               targetComp.dataProvider[evt.index].data;
		//}
	/**
    * Sets the size of the canvas on stage, called from update
    */
	private function setSize(mm:MonitorModel):Void{
        var s:Object = mm.getSize();
		trace("Monitor Tab Widtht: "+s.w+" Monitor Tab Height: "+s.h);
		requiredTask_scp.setSize(s.w,s.h);
		
				
	}
	

	/**
	 * Overrides method in abstract view to ensure cortect type of controller is returned
	 * @usage   
	 * @return  CanvasController
	 */
	public function getController():MonitorController{
		var c:Controller = super.getController();
		return MonitorController(c);
	}
}