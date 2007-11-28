/****************************************************************************
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

import org.lamsfoundation.lams.common.*;
import org.lamsfoundation.lams.authoring.cv.*;
import org.lamsfoundation.lams.authoring.br.BranchConnector;
import org.lamsfoundation.lams.authoring.br.CanvasBranchView;
import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.ui.*;
import org.lamsfoundation.lams.common.dict.*;
import mx.events.*;


/**
 *
 * @author
 * @version
 **/
class org.lamsfoundation.lams.authoring.cv.CanvasSuperModel extends Observable {
	
	private var _defaultGroupingTypeID;
	private var __width:Number;
	private var __height:Number;
	private var __x:Number;
	private var __y:Number;
	private var _piHeight:Number;
	private var infoObj:Object;
	
	private var _cv:Canvas;
	private var _ddm:DesignDataModel;
	private var optionalCA:CanvasOptionalActivity;
	
	//UI State variabls
	private var _isDirty:Boolean;
	private var _activeTool:String;
	private var _selectedItem:Object;  // the currently selected thing - could be activity, transition etc.
	private var _prevSelectedItem:Object;
	private var _isDrawingTransition:Boolean;
	private var _connectionActivities:Array;
	private var _isDragging:Boolean;
	private var _importing:Boolean;
	private var _editing:Boolean;
	private var _autoSaveWait:Boolean;
	
	//these are hashtables of mc refs MOVIECLIPS (like CanvasActivity or CanvasTransition)
	//each on contains a reference to the emelment in the ddm (activity or transition)
	private var _activitiesDisplayed:Hashtable;
	private var _transitionsDisplayed:Hashtable;
	private var _branchesDisplayed:Hashtable;
	
	private var _currentBranchingActivity:Object;
	private var _activeView:Object;
	
	private var _lastBranchActionType:Number;
	
	private var _doRefresh:Boolean;
	
	//These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	
	public function CanvasSuperModel(cv:Canvas) {
		
		_cv = cv;
		_ddm = new DesignDataModel();
		_activitiesDisplayed = new Hashtable("_activitiesDisplayed");
		_transitionsDisplayed = new Hashtable("_transitionsDisplayed");
		_branchesDisplayed = new Hashtable("_branchesDisplayed");
		
		_activeTool = "none";
		_activeView = null;
		_currentBranchingActivity = null;
		_lastBranchActionType = null;
		
		_autoSaveWait = false;
		_connectionActivities = new Array();
		_defaultGroupingTypeID = Grouping.RANDOM_GROUPING;
		
		_doRefresh = true;
		
		 //Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////       TRANSITIONS         //////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Starts the transition tool
	 * @usage   
	 * @return  
	 */
	public function startTransitionTool():Void{
		Debugger.log('Starting transition tool',Debugger.GEN,'startTransitionTool','CanvasModel');			
		resetTransitionTool();
		_activeTool = CanvasModel.TRANSITION_TOOL;
	}
	
	/**
	 * Stops it
	 * @usage   
	 * @return  
	 */
	 
	public function stopTransitionTool():Void{
		Debugger.log('Stopping transition tool',Debugger.GEN,'stopTransitionTool','CanvasModel');
		resetTransitionTool();
		_activeTool = "none";
	}
	
	
	/**
	 * Resets the transition tool to its starting state, e.g. if one chas been created or the user released the mouse over an unsuitable clip
	 * @usage   
	 */
	public function resetTransitionTool():Void{
		//clear the transitions array
		_connectionActivities = new Array();
	}
	
	public function isTransitionToolActive():Boolean{
	   if(_activeTool == CanvasModel.TRANSITION_TOOL){
		   return true;
		}else{
			return false;
		}
	}
	
	/**
    * Notify registered listeners that a data model change has happened
    */
    public function broadcastViewUpdate(_updateType,_data){
        dispatchEvent({type:'viewUpdate',target:this,updateType:_updateType,data:_data});
    }
	
	public function clearAllElements():Void {
		var branch_keys:Array = _branchesDisplayed.keys();
		var act_keys:Array = _activitiesDisplayed.keys();
		var trans_keys:Array = _transitionsDisplayed.keys();
		
		for(var i=0; i<branch_keys.length; i++)
			_branchesDisplayed.get(branch_keys[i]).removeMovieClip();
		for(var i=0; i<trans_keys.length; i++)
			_transitionsDisplayed.get(trans_keys[i]).removeMovieClip();
		for(var i=0; i<act_keys.length; i++) {
			if(_activitiesDisplayed.get(act_keys[i]).activity.isBranchingActivity())
				_activitiesDisplayed.get(act_keys[i]).branchView.removeMovieClip();
			_activitiesDisplayed.get(act_keys[i]).removeMovieClip();
			
		}
		
		_branchesDisplayed.clear();
		_transitionsDisplayed.clear();
		_activitiesDisplayed.clear();
		
	}
	
	public function clearAllBranches(a:Activity):Void {
		var branch_keys:Array = _branchesDisplayed.keys();
		var act_keys:Array = _activitiesDisplayed.keys();
		Debugger.log("clearing branches: " + branch_keys.length, Debugger.CRITICAL, "clearAllBranches", "CanvasModel");
		
			
		for(var i=0; i<branch_keys.length; i++) {
			var branch:Branch = _branchesDisplayed.get(branch_keys[i]).branch;
			
			Debugger.log("branch: " + branch.branchUIID, Debugger.CRITICAL, "clearAllBranches", "CanvasModel");
		
			if(branch.sequenceActivity.parentUIID == a.activityUIID) {
				for(var j=0; j<act_keys.length; j++) {
					
					Debugger.log("activity parent: " + _activitiesDisplayed.get(act_keys[i]).activity.parentUIID, Debugger.CRITICAL, "clearAllBranches", "CanvasModel");
					Debugger.log("seq uiid: " + branch.sequenceActivity.activityUIID, Debugger.CRITICAL, "clearAllBranches", "CanvasModel");
					
					if(_activitiesDisplayed.get(act_keys[i]).activity.parentUIID == branch.sequenceActivity.activityUIID) {
						_activitiesDisplayed.remove(act_keys[i]);
					}
		
				}
			}
		}
		
	}
	
	/**
	 * Returns a reference to the Activity Movieclip for the UIID passed in.  Gets from _activitiesDisplayed Hashable
	 * @usage   
	 * @param   UIID 
	 * @return  Activity Movie clip
	 */
	public function getActivityMCByUIID(UIID:Number):MovieClip{
		
		var a_mc:MovieClip = _activitiesDisplayed.get(UIID);
		return a_mc;
	}

	/**
	* Used by application to set the size
	* @param width The desired width
	* @param height the desired height
	*/
	public function setSize(width:Number, height:Number):Void{
		__width = width;
		__height = height;
		
		broadcastViewUpdate("SIZE");
		
	}
	
	/**
	* Used by View to get the size
	* @returns Object containing width(w) & height(h).  obj.w & obj.h
	*/
	public function getSize():Object{
		var s:Object = {};
		s.w = __width;
		s.h = __height;
		return s;
	}
	
	/**
	* Used by application to set the Position
	* @param x
	* @param y
	*/
	public function setPosition(x:Number, y:Number):Void{
		__x=x;
		__y=y;
		
		broadcastViewUpdate("POSITION");
	}
	
	/**
	* Used by View to get the size
	* @returns Object containing width(w) & height(h).  obj.w & obj.h
	*/
	public function getPosition():Object{
		var p:Object = {};
		p.x = __x;
		p.y = __y;
		return p;
	}
	
	public function set activeView(a:Object):Void{
		_activeView = a;
		
		broadcastViewUpdate("SET_ACTIVE", a);
	}
	
	public function get activeView():Object {
		return _activeView;
	}
	
	public function isActiveView(view:Object):Boolean {
		return (activeView == view);
	}
	
	public function findParent(a:Activity, b:Activity):Boolean {
		if(a.activityUIID == b.activityUIID)
			return true;
		else if(a.isBranchingActivity())
			return false;
		else if(a.parentUIID == null)
			return false;
		else
			return findParent(_cv.ddm.getActivityByUIID(a.parentUIID), b);
    }
	
	public function setPIHeight(h:Number){
		_piHeight = h;
		Application.getInstance().onResize();
	}
	
	public function getPIHeight(){
		return _piHeight;
	}
	
	public function findOptionalActivities():Array{
		var actOptional:Array = new Array();
		var k:Array = _activitiesDisplayed.values();
		
		for (var i=0; i<k.length; i++){
			if (k[i].activity.activityTypeID == Activity.OPTIONAL_ACTIVITY_TYPE) {
				
				if(k[i].activity.parentUIID == null && (_activeView instanceof CanvasView))
					actOptional.push(k[i]);
				else if((_activeView instanceof CanvasBranchView) && findParent(k[i].activity, _activeView.activity))
					actOptional.push(k[i]);
					
				/**if(_activeView instanceof CanvasBranchView)
					if(!findParent(k[i].activity, _activeView.activity))
						actOptional.pop();
				else if(k[i].activity.parentUIID != null)
					actOptional.pop();
					*/
			}
			
		}
		return actOptional;
	}
	
	public function findParallelActivities():Array{
		
		var actParallel:Array = new Array();
		var k:Array = _activitiesDisplayed.values();
		for (var i=0; i<k.length; i++){
			if (k[i].activity.activityTypeID == Activity.PARALLEL_ACTIVITY_TYPE){
				actParallel.push(k[i]);
			}
			
		}
		return actParallel;
	}
	
	public function findTopLevelActivities():Array{
		var actParent:Array = new Array();
		var k:Array = _activitiesDisplayed.values();
		for (var i=0; i<k.length; i++){
			if (k[i].activity.parentUIID == null){
				actParent.push(k[i]);
			}
			
		}
		return actParent;
	}
	
	public function get currentBranchingActivity():Object {
		return _currentBranchingActivity;
	}
	
	public function set currentBranchingActivity(a:Object) {
		_currentBranchingActivity = a;
	}

	//Getters n setters
	
	public function getCanvas():Canvas{
		return _cv;
	}
	
	public function get activitiesDisplayed():Hashtable{
		return _activitiesDisplayed;
	}
	
	public function get transitionsDisplayed():Hashtable{
		return _transitionsDisplayed;
	}
	
	public function get branchesDisplayed():Hashtable{
		return _branchesDisplayed;
	}
	
	
	public function get isDrawingTransition():Boolean{
		return _isDrawingTransition;
	}
	/**
	 * 
	 * @usage   
	 * @param   newactivetool 
	 * @return  
	 */
	public function set activeTool(newactivetool:String):Void {
		_activeTool = newactivetool;
	}
	
	/**
	 * 
	 * @usage   
	 * @param   newactivetool 
	 * @return  
	 */
	public function setActiveTool(newactivetool):Void {
		_activeTool = newactivetool;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get activeTool():String {
		return _activeTool;
	}
	
	private function setSelectedItem(newselectItem:Object){
		prevSelectedItem = _selectedItem;
		_selectedItem = newselectItem;
		broadcastViewUpdate("SELECTED_ITEM");
	}

	public function setSelectedItemByUIID(uiid:Number){
		var selectedCanvasElement;
		if(_activitiesDisplayed.get(uiid) != null){
			selectedCanvasElement = _activitiesDisplayed.get(uiid);
		}else{
			selectedCanvasElement = _transitionsDisplayed.get(uiid);
		}
		
		setSelectedItem(selectedCanvasElement);
		
	}

	/**
	 * 
	 * @usage   
	 * @param   newselectItem 
	 * @return  
	 */
	public function set selectedItem (newselectItem:Object):Void {
		setSelectedItem(newselectItem);
	}
	
	public function set prevSelectedItem (oldselectItem:Object):Void {
		_prevSelectedItem = oldselectItem;
	}
	
	public function get prevSelectedItem():Object {
		return _prevSelectedItem;
	}
	
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get selectedItem ():Object {
		return _selectedItem;
	}
	
	public function get connectionActivities():Array {
		return _connectionActivities;
	}
	
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get isDragging ():Boolean {
		return _isDragging;
	}
	
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function set isDragging (newisDragging:Boolean):Void{
		_isDragging = newisDragging;
	}
	
	public function get importing():Boolean {
		return _importing;
	}
	
	public function set importing(importing:Boolean):Void {
		_importing = importing;
	}
	
	public function get editing():Boolean {
		return _editing;
	}
	
	public function set editing(editing:Boolean):Void {
		_editing = editing;
	}
	
	public function get autoSaveWait():Boolean {
		return _autoSaveWait;
	}
	
	public function set autoSaveWait(autoSaveWait:Boolean):Void {
		_autoSaveWait = autoSaveWait;
	}
	
	public function set lastBranchActionType(a:Number):Void {
		_lastBranchActionType = a;
	}
	
	public function get lastBranchActionType():Number {
		return _lastBranchActionType;
	}

}