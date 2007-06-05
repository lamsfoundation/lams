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
 
import org.lamsfoundation.lams.common.util.Proxy;
import org.lamsfoundation.lams.common.util.Debugger;
import org.lamsfoundation.lams.common.ui.*;

import mx.controls.*
import mx.utils.*
import mx.events.*

/**
* Application loading screen for LAMS
*  
* @author  MS
*/
class LFLoader extends MovieClip {
	
	// components
	//private var panel:MovieClip;
	private var lams_logo:MovieClip;
	private var pBar:MovieClip;
	private var pBar_lbl:Label;
	
	// static variables
	private static var LOGO_X_OFFSET:Number = 43;
	private static var LABEL_X_OFFSET:Number = 66;
	private static var LOGO_Y_OFFSET:Number = 25;
	private static var LABEL_Y_OFFSET:Number = 10;
	private static var LOADER_DELAY:Number = 2000;
	
	// state variables
	private var LOADER_INT:Number;
	private var loadedCount:Number;
	private var _noComponents:Number;
	private var _noCompleted:Number;
	
	//These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
    
    /**
    * constructor
    */
    private function LFLoader(){
        //Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);
		_noCompleted = 0;
		
        //Create a clip that will wait a frame before dispatching init to give components time to setup
        this.onEnterFrame = init;
    }
	
	/**
    * Called a frame after movie attached to allow components to initialise
    */
    private function init(initObj):Void{
		
		//Delete the enterframe dispatcher
		delete this.onEnterFrame;
		
		resize();
	}
	
	/**
	 * Set log message for progress bar
	 *  
	 * @param   message  	Message to log
	 */
	
	public function setLoadingMessage(message:String){
		//pBar.label = message;
		Debugger.log(message,Debugger.GEN,'setLoadingMessage','org.lamsfoundation.lams.common.LFLoader');
	}
	
	/**
	* Control visibility of progress bar
	* @param v 
	*/
	
	public function hideProgress(v:Boolean){
		pBar.visible = v;
	}
	
	/**
	 * Adjust view components
	 * 
	 */
	
	private function resize(){
		//panel._width = Stage.width;
		//panel._height = Stage.height;
		pBar._x = (Stage.width - pBar._width)/2;
		pBar._y = (Stage.height - pBar._height)/2;
		pBar_lbl._x = pBar._x + LABEL_X_OFFSET;
		pBar_lbl._y = pBar._y + LABEL_Y_OFFSET;
		lams_logo._x = pBar._x + LOGO_X_OFFSET;
		lams_logo._y = pBar._y - (lams_logo._height + LOGO_Y_OFFSET);
	}
	
	/**
	 * Set initial progess and application component no to evaluate percentage
	 *  
	 * @param   componentNo 	The number of application components to load
	 *
	 */
	
	public function start(componentNo:Number){
		_noComponents = componentNo;
		setProgress(_noCompleted, _noComponents);
	}
	
	/**
	* Fill the progress bar when finished loading
	*/
	
	public function stop(){
		setProgress(1,1);
		destroy();
	}

	/**
    * Called on initialisation and themeChanged event handler
    /
    private function setStyles(){
		panel.setStyle('backgroundColor', 0xDBE6FD);
		pBar.setStyle('themeColor', 'haloBlue');
		pBar_lbl.setStyle('color', 0x0B333C);
		pBar_lbl.setStyle('fontFamily', 'Tahoma');
		pBar_lbl.setStyle('fontSize', 9);
	}
	*/
	public function setProgress(completed:Number, total:Number){
		if(completed != undefined && total != undefined){
			pBar.setProgress(completed, total);
			pBar_lbl.text = Math.round(pBar.percentComplete) + "%";
		} else {
			// TODO: ERROR updating progress
		}
	}
	
	/**
	 * Update the progress bar after a component has been completed
	 *  
	 */
	
	public function complete(){
		_noCompleted++;
		if(_noCompleted <= _noComponents){
			// update progress
			setProgress(_noCompleted, _noComponents);
		} else {
			// loading progress completed
			setProgress(1,1);
		}
	}
	
	/**
	* Set the number of application components to load
	*/
	
	public function set noComponents(a:Number){
		_noComponents = a;
	}
	
	/**
	 * Remove the MovieClip
	 *  
	 */
	
	private function destroy(){
		if(!LOADER_INT){
			trace('setting loader interval');
			LOADER_INT = setInterval(Proxy.create(this, remove), LOADER_DELAY);
		} else {
			trace('already interval');
		}
	}
	
	private function remove(){
		clearInterval(LOADER_INT);
		this.removeMovieClip();
	}
}