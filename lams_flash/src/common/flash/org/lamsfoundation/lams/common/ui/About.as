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

import mx.containers.*
import mx.managers.*
import mx.utils.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.common.dict.*

/**
* About - About dialog
* @author   MS
*  
*/
class About extends MovieClip implements Dialog {

    //Declarations
    //Static vars

    //Public vars
	public var className:String = 'About';
    
	private var version_lbl:Label;
	private var build_lbl:Label;
	
	//References to components + clips 
    private var _container:MovieClip;  //The container window that holds the dialog
	
	private var app:ApplicationParent;
    private var tm:ThemeManager;
	private var _dictionary:Dictionary;
    
    //Constructor
    function About() {
		//Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);
		
		this.onEnterFrame = init;
		this._visible=false;
	}
    
	public function init():Void {
		delete this.onEnterFrame;
		
		//Get a reference to the application, ThemeManager and Dictionary
        app = ApplicationParent.getInstance();
        tm = ThemeManager.getInstance();
	
		_dictionary = Dictionary.getInstance();
		_dictionary.addEventListener('init', Delegate.create(this, setup));
		
		setup();
		
		//fire event to say we have loaded
		_container.contentLoaded();
	}
    
	/* 
	 * @usage   
	 * @return  
	 */
	public function setUpContent():Void{
		this._visible = true;
	}
	
    /**
	* called when object is sized, e.g. draw, setSize etc.
	*/
    public function setSize(Void):Void {
       
    }
	
    /**
    * overrides UIObject.setStyle to provide custom style setting
    */
    public function setStyles() {
         var styleObj = tm.getStyleObject('AboutDialog');
        version_lbl.setStyle('styleName',styleObj);
		build_lbl.setStyle('styleName',styleObj);
    }
	
	public function setLabels() {
		version_lbl.text = Dictionary.getValue('about_dialog_version_lbl', [_root.version]);
		build_lbl.text = Dictionary.getValue('about_dialog_build_lbl', [ApplicationParent.SERIAL_NO]);
	}
	
	public function setup() {
		setLabels();
		setStyles();
	}
	
    /**
    * Event dispatched by parent container when close button clicked
    */
    public function click(e:Object):Void{
        trace('LessonManagerDialog.click');
        e.target.deletePopUp();
    }
    
    
}