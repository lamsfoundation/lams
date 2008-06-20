/***************************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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

import org.lamsfoundation.lams.authoring.cv.*;
import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.*

import mx.controls.*
import mx.controls.gridclasses.DataGridColumn;
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

	public function init():Void {
		 //Delete the enterframe dispatcher
        delete this.onEnterFrame;
		
		_canvasModel = _container.canvasModel;
		_canvasController = _container.canvasController;
		_validationIssues = _container.validationIssues;
		
		_canvasModel.addEventListener('viewUpdate',this);
		validationIssues_dgd.addEventListener("change",this);
		
		done_btn.label = Dictionary.getValue('ld_val_done');
		done_btn.addEventListener("click",this);
		
		//Assign Click (close button) and resize handlers
        _container.addEventListener('click',this);
        _container.addEventListener('size',this);
		
		// Should be called by canvasController?
		setupContent();
	}
	
	public function getRowHeight(column_value:DataGridColumn):Number {
		var maxStrWidth:Number = 0;
		for (var i = 0; i < _validationIssues.length; i++) {
			maxStrWidth = Math.max(StringUtils.getButtonWidthForStr(_validationIssues[i].Issue), maxStrWidth);
		}
		
		var numRows:Number = Math.ceil(maxStrWidth/(column_value.width-2*MARGIN));
		var lineHeight:Number;
		if (numRows == 1)
			lineHeight = 25;
		else
			lineHeight = 20;
		
		var _rowHeight:Number = numRows * lineHeight;
		return _rowHeight;
	}

	public function setupContent():Void {
		var column_name:DataGridColumn = new DataGridColumn("Activity");
		column_name.headerText = Dictionary.getValue("ld_val_activity_column");
		column_name.editable = false;
		column_name.width = Math.ceil((validationIssues_dgd.width - 15)*0.3);
		column_name.cellRenderer = "MultiLineCell";
		
		var column_value:DataGridColumn = new DataGridColumn("Issue");
		column_value.headerText = Dictionary.getValue("ld_val_issue_column");
		column_value.editable = false;
		column_value.width = Math.ceil((validationIssues_dgd.width - 15)*0.7);
		column_value.cellRenderer = "MultiLineCell";
		
		validationIssues_dgd.rowHeight = getRowHeight(column_value);
		
		validationIssues_dgd.addColumn(column_name);
		validationIssues_dgd.addColumn(column_value);

		// wait second frame for steppers to be setup
		validationIssues_dgd.dataProvider = _validationIssues;

		_container.contentLoaded();
					
		this.onEnterFrame = initSetup;
		
		setSize(_width, _height);
	}
	
	private function initSetup():Void {
		delete this.onEnterFrame;

		this._visible = true;

	}
	
	/**
    * Main resize method, called by scrollpane container/parent
    */
    public function setSize(w:Number,h:Number){
	  
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
		Debugger.log('e.target.selectedItem.data.uiid:'+e.target.selectedItem.uiid,Debugger.GEN,'change','ValidationIssuesDialog');
		_canvasModel.setSelectedItemByUIID(e.target.selectedItem.uiid);
	}
	
	
}

