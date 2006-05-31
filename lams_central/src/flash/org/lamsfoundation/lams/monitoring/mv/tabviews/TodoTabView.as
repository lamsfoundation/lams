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

import org.lamsfoundation.lams.common.ApplicationParent;
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.monitoring.mv.*
import org.lamsfoundation.lams.monitoring.*;
import org.lamsfoundation.lams.authoring.Activity;
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

class org.lamsfoundation.lams.monitoring.mv.tabviews.TodoTabView extends AbstractView{
	public static var _tabID:Number = 3;
	private var _className = "TodoTabView";
	//constants:
	private var _tm:ThemeManager;
	private var mm:MonitorModel;
	
	//TabView clips
	private var YPOS:Number = 70; 
	private var listCount:Number = 0; 
	private var todoTaskList:Array = new Array();
	private var _monitorTodoTask_mc:MovieClip;
		
	//Text Items
 	private var genralInfo_txt:TextField;

    private var _todoTabView:TodoTabView;
	private var _monitorController:MonitorController;
	
	//Defined so compiler can 'see' events added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	//public var menu:ContextMenu;

	
	/**
	* Constructor
	*/
	function TodoTabView(){
		_todoTabView = this;
		_monitorTodoTask_mc = this;
		this._visible = false;
		_tm = ThemeManager.getInstance();
        //Init for event delegation
        mx.events.EventDispatcher.initialize(this);
	}
	
	/**
	* Called to initialise Canvas  . CAlled by the Canvas container
	*/
	public function init(m:Observable,c:Controller){
		super (m, c);
	}    
	
	/**
 * Recieved update events from the CanvasModel. Dispatches to relevent handler depending on update.Type
 * @usage   
 * @param   event
 */
public function update (o:Observable,infoObj:Object):Void{
		
       mm = MonitorModel(o);
	   
	   switch (infoObj.updateType){
		    case 'SIZE' :
			    setSize(mm);
                break;
			case 'POSITION' :
				setPosition(mm);
                break;
			case 'TODOS' :
				populateContributeActivities();
                break;
			case 'TABCHANGE' :
				if (infoObj.tabID == _tabID){
				trace("TabID for Selected tab is (LessonTab TABCHANGE): "+infoObj.tabID)
					this._visible = true;
					//mm.setDirty();
					MovieClipUtils.doLater(Proxy.create(this,draw));
				}else {
					this._visible = false;
				}
				break;
			default :
                Debugger.log('unknown update type :' + infoObj.updateType,Debugger.CRITICAL,'update','org.lamsfoundation.lams.LessonTabView');
		}

	}
	
	/**
    * layout visual elements on the canvas on initialisation
    */
	private function draw(){
		listCount = 0; 
		//this.onEnterFrame = setupLabels;
		//get the content path for the sp
		//_monitorTodoTask_mc = reqTasks_scp.content;
		_monitorController = getController();
		//Debugger.log('_canvas_mc'+_canvas_mc,Debugger.GEN,'draw','CanvasView');
		trace("Loaded TodoTabView Data"+ this)
		populateLessonDetails();
		
		trace('seq id: ' + mm.getSequence().getSequenceID());
		trace('last seq id: ' + mm.getLastSelectedSequence().getSequenceID());
		if (mm.getSequence().getSequenceID() == mm.getLastSelectedSequence().getSequenceID()){
			if(mm.getToDos() == null){
				mm.getMonitor().getContributeActivities(mm.getSequence().getSequenceID());
			} else {
				populateContributeActivities();
			}
		}else{
			mm.getMonitor().getContributeActivities(mm.getSequence().getSequenceID());
		}
		
		dispatchEvent({type:'load',target:this});
	}
	
	/**
	 * Populate the lesson details from HashTable Sequence in MOnitorModel
	*/
	private function populateLessonDetails():Void{
		//var mm:Observable = getModel();
		var s:Object = mm.getSequence();
		var desc:String = "<b>Advanced Controls:</b> Use of this Todo Tab is not required to complete the sequence. See the help page for more information. <br><br>This feature is now fully functional."
		genralInfo_txt.htmlText = desc
		
		  
	}

	private function populateContributeActivities():Void{
		var todos = LessonManagerDialog.clearScp(todos)
		todos = mm.getToDos();
		todoTaskList = LessonManagerDialog.clearScp(todoTaskList)
		// show isRequired activities in scrollpane
		for (var i=0; i<todos.length; i++){
			trace('main CA title: ' + todos[i].title);
			var array:Array = getEntries(todos[i]);
			drawTodoTasks(todos[i], array, 10);
		}
	}
	
	/**
	 * Return isRequired entries
	 * 
	 * @usage   
	 * @param   ca ContributeActivity
	 * @return  Array of isRequired entries
	 */
	
	private function getEntries(ca:Object):Array{
		var array:Array = new Array();
		for (var i=0; i<ca.childActivities.length; i++){
			trace(ca.title+"'s Child Activity "+i+" is: "+ca.childActivities[i].title)
			var tmp:Array = getEntries(ca.childActivities[i]);
			if(tmp.length > 0){
				var obj:Object = {}
				obj.entries = tmp;
				obj.child= ca.childActivities[i];
				array.push(obj);
			}
			
			//var tmp:Array = getEntries(ca.childActivities[i]);
			//drawIsRequiredChildTasks(ca, ca.childActivities[i], tmp);
			//return null;
		}
		for (var j=0; j<ca.contributeEntries.length; j++){ 
			trace("Contribute Entry for "+ca.title+" is: "+ca.contributeEntries[j].contributionType)
			//if(ca.contributeEntries[j].isRequired){
				// show isRequired entry
				array.push(ca.contributeEntries[j]);
			//}
		}
		return array;
	}
	
	/**
	 * Draws isRequired tasks
	 *   
	 * @param   ca    
	 * @param   array Holds CA required entries for CA and child CA's
	 * @return  
	 */
	
	private function drawTodoTasks(ca:Object, array:Array, x:Number):Void{
		//var o:Object;
		
		if(array.length > 0){
			// write ca title / details to screen with x position
			todoTaskList[listCount] = _monitorTodoTask_mc.attachMovie("contributeActivityRow", "contributeActivityRow"+listCount, _monitorTodoTask_mc.getNextHighestDepth(), {_x:x, _y:YPOS+(19*listCount)})
			todoTaskList[listCount].contributeActivity.background = true;
			todoTaskList[listCount].contributeActivity._width=_monitorTodoTask_mc._width-20
			if (ca._parentActivityID == null){
				todoTaskList[listCount].contributeActivity.text = "  "+ca.title
				todoTaskList[listCount].contributeActivity.backgroundColor = 0xD5E6FF;
			}else {
				todoTaskList[listCount].contributeActivity.text = "\t"+ca.title
				todoTaskList[listCount].contributeActivity.backgroundColor = 0xF9F2DD;
			}
			
			listCount++
		}
		
		for(var i=0; i<array.length; i++){
			var o:Object = array[i];
			
			if(o instanceof ContributeActivity){
				// normal CA entries
				trace('write out entry with GO link'+o.taskURL);
				todoTaskList[listCount] =_monitorTodoTask_mc.attachMovie("contributeEntryRow", "contributeEntryRow"+listCount, this._monitorTodoTask_mc.getNextHighestDepth(), {_x:x, _y:YPOS+(19*listCount)})
				todoTaskList[listCount].contributeEntry.text = "\t\t"+mm.getMonitor().getCELiteral(o._contributionType);
				todoTaskList[listCount].goContribute._x = this._width-50
				todoTaskList[listCount].goContribute.onRelease = function (){
					trace("Contrybute Type is: "+o.taskURL);
					getURL(String(o.taskURL), "_blank");
				}
				todoTaskList[listCount].goContribute.setStyle("fontSize", "9"); 
				listCount++
			}else{
				// child CA
				trace('child entries length:' + o.entries.length)
				if(o.entries.length > 0){
					trace('now drawing child');
					// write child ca title (indented - x + 10 position)
					drawTodoTasks(o.child, o.entries, x);
				}
				
			}
		}
	}
	
	/**
    * Sets the size of the canvas on stage, called from update
    */
	private function setSize(mm:MonitorModel):Void{
        var s:Object = mm.getSize();
		genralInfo_txt._width = s.w-20
		for (var i=0; i<todoTaskList.length; i++){
			todoTaskList[i].contributeActivity._width = s.w-20
		}				
	}
	
	 /**
    * Sets the position of the canvas on stage, called from update
    * @param cm Canvas model object 
    */
	private function setPosition(mm:MonitorModel):Void{
        var p:Object = mm.getPosition();
		trace("X pos set in Model is: "+p.x+" and Y pos set in Model is "+p.y)
		for (var i=0; i<todoTaskList.length; i++){
			todoTaskList[i].goContribute._x = _monitorTodoTask_mc._width-50;
		}	}
	
	/**
	 * Overrides method in abstract view to ensure cortect type of controller is returned
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
}