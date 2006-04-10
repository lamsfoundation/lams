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

import org.lamsfoundation.lams.authoring.cv.*;
import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.*

import mx.controls.*
import mx.utils.*
import mx.managers.*
import mx.events.*

/*
*
* @author      DC
* @version     0.1
* @comments    Shows validation issues in a datagrid
* 
*/
class org.lamsfoundation.lams.authoring.cv.ValidationIssuesDialog extends MovieClip{
	
	private var MARGIN = 5;
	private var _canvasModel:CanvasModel;
	private var _canvasController:CanvasController;
	private var _validationIssues:Object;
	
	 //References to components + clips 
    private var _container:MovieClip;       //The container window that holds the dialog. Will contain any init params that were passed into createPopUp
   
	private var validationIssues_dgd:DataGrid;
	private var done_btn:Button;
	
    private var fm:FocusManager;
    private var _tm:ThemeManager;
    private var toolDisplayName_lbl:Label;

   
	//Defined so compiler can 'see' events added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	
	
	/**
	 * Constructor
	 */
	function ValidationIssuesDialog(){
		Debugger.log('Constructor',Debugger.GEN,'ValidationIssuesDialog','ValidationIssuesDialog');
		_tm = ThemeManager.getInstance();
		//Set up this class to use the Flash event delegation model
		EventDispatcher.initialize(this);
		
		//let it wait one frame to set up the components.
		MovieClipUtils.doLater(Proxy.create(this,init));
		
	}
	
	public function init():Void{
		_canvasModel = _container.canvasModel;
		_canvasController = _container.canvasController;
		_validationIssues = _container.validationIssues;
	
		_canvasModel.addEventListener('viewUpdate',this);
		
		_container.addEventListener('click',this);
		//get focus manager + set focus , focus manager is available to all components through getFocusManager
        fm = _container.getFocusManager();
        fm.enabled = true;
        validationIssues_dgd.setFocus();
		
		setStyles();
		done_btn.label = Dictionary.getValue('ld_val_done');
		
		//set up handlers
		//TODO connect to the controller
		validationIssues_dgd.addEventListener("change",this);
		done_btn.addEventListener("click",this);

		//set the 1st colum a bit smaller
		validationIssues_dgd.dataProvider = _validationIssues;
		validationIssues_dgd.removeAllColumns();
		validationIssues_dgd.columnNames = ["Activity", "Issue"];
		validationIssues_dgd.getColumnAt(0).width = 110;
		Debugger.log('_validationIssues.messages:'+_validationIssues.messages.length,Debugger.GEN,'init','ValidationIssuesDialog');
		//fire event to say we have loaded
		_container.contentLoaded();
	}
	
	
	

	/**
    * Main resize method, called by scrollpane container/parent
    */
    public function setSize(w:Number,h:Number){
        //Debugger.log('setSize',Debugger.GEN,'setSize','org.lamsfoundation.lams.common.ws.WorkspaceDialog');
        //Size the bkg_pnl
		
    //    body_pnl.setSize(w,h-bar_pnl.height);
      //  bar_pnl.setSize(w);
	  
	  validationIssues_dgd.setSize(w,h - (done_btn._height + MARGIN*2));
	  done_btn._x = (w - done_btn._width) - MARGIN;
	  done_btn._y =  h - (done_btn._height + MARGIN);

        
    }
	
	/**
	 * Get the CSSStyleDeclaration objects for each component and applies them
	 * directly to the instanced
	 * @usage   
	 * @return  
	 */
	private function setStyles() {
       
		var styleObj = _tm.getStyleObject('button');
		done_btn.setStyle('styleName',styleObj);
		styleObj = _tm.getStyleObject('datagrid');
		validationIssues_dgd.setStyle(styleObj);
		
		
    }
    
    //Gets+Sets
    /**
    * set the container refernce to the window holding the dialog
    */
    public function set container(value:MovieClip){
        _container = value;
    }
	

	
   /**
	 * Recieves the click events from the canvas views (inc Property Inspector) buttons.  Based on the target
	 * the relevent method is called to action the user request
	 * @param   evt 
	 */
	 /**/
	public function click(e):Void{
		var tgt:String = new String(e.target);
		Debugger.log('click tgt:'+tgt,Debugger.GEN,'click','ValidationIssuesDialog');
		 _container.deletePopUp();
	}
	
	public function change(e):Void{
		//Debugger.log(ObjectUtils.toString(e.target.selectedItem),Debugger.GEN,'change','ValidationIssuesDialog');
		Debugger.log('e.target.selectedItem.data.uiid:'+e.target.selectedItem.uiid,Debugger.GEN,'change','ValidationIssuesDialog');
		_canvasModel.setSelectedItemByUIID(e.target.selectedItem.uiid);
	}
	
	
}

