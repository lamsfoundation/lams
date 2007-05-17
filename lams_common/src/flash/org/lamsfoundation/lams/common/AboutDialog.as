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
import mx.controls.*;
import mx.events.*;

import org.lamsfoundation.lams.common.*
import org.lamsfoundation.lams.common.comms.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.common.dict.*

/**
* About - About dialog
* @author   MS
*  
*/
class AboutDialog extends MovieClip implements Dialog {

    //Declarations
    //Static vars
	private static var LOGO_X:Number = 9;
	private static var LOGO_Y:Number = 5;
	public static var LOGO_WIDTH:Number = 67;
	public static var LOGO_HEIGHT:Number = 25;
	
	public static var LOGO_PATH:String = "www/images/about.logo.swf";
	
	public var className:String = 'AboutDialog';
	
	
	private var _bg:Panel;
	private var logo:MovieClip; 			// LAMS/RAMS logo
	private var version_lbl:TextField;
	private var version_txt:TextField;
	private var copyright_txt:TextField;
	private var trademark_txt:TextField;
	private var license_txt:TextField;
	
	private var _scrollList_scp:MovieClip;
	private var _listText:String;
	
	//References to components + clips 
    private var _container:MovieClip;  //The container window that holds the dialog
	
	private var app:ApplicationParent;
    private var tm:ThemeManager;
	private var _dictionary:Dictionary;
	private var _comms:Communication;
    private var fm:FocusManager;            //Reference to focus manager
    
	//These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	
    //Constructor
    function AboutDialog() {
		//Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);
		
		//Create a clip that will wait a frame before dispatching init to give components time to setup
        this.onEnterFrame = init;
		this._visible = false;
	}
    
	public function init():Void {
		//Delete the enterframe dispatcher
        delete this.onEnterFrame;
		
		//Get a reference to the application, ThemeManager and Dictionary
        app = ApplicationParent.getInstance();
        tm = ThemeManager.getInstance();
	
        _comms = ApplicationParent.getInstance().getComms();
		
		_dictionary = Dictionary.getInstance();
		_dictionary.addEventListener('init', Delegate.create(this, setup));
		
		//Assign Click (close button) and resize handlers
        _container.addEventListener('click',this);
        _container.addEventListener('size',this);

		//get focus manager + set focus to OK button, focus manager is available to all components through getFocusManager
        fm = _container.getFocusManager();
        fm.enabled = true;

		// autosize labels/textfields
		version_lbl.autoSize = true;
		version_txt.autoSize = true;
		copyright_txt.autoSize = true;
		trademark_txt.autoSize = true;
		license_txt.autoSize = true;

		getList();
		
	}
    
	/* 
	 * @usage   
	 * @return  
	 */
	public function setUpContent():Void{
		this._visible = true;
	}
	
	/**
    * set the container refernce to the window holding the dialog
    */
    function set container(value:MovieClip){
        _container = value;
    }
	
    /**
    * overrides UIObject.setStyle to provide custom style setting
    */
    public function setStyles() {
		var styleObj = tm.getStyleObject('AboutDialogPanel');
		_bg.setStyle('styleName', styleObj);
    }
	
	public function setLabels() {
		
	}
	
	public function getList():Void {
		var callBack = Proxy.create(this,loadData);
        _comms.getRequest('flashxml/contributorData.xml',callBack);
	}
	
	public function loadData(data:Object) {
		if(data instanceof LFError) {
			Debugger.log("unsuccessful load", Debugger.CRITICAL, "openAboutLams", "Canvas");
			return;
		}
		
		var list_mc:MovieClip = _scrollList_scp.content;
		var empty_mc = list_mc.createEmptyMovieClip('empty_mc', list_mc.getNextHighestDepth());
		var xOffset:Number = 1;
		var yOffset:Number = 1;
		var styleObj_General = tm.getStyleObject("AboutDialogScpGeneralItem");
		var styleObj_Header = tm.getStyleObject("AboutDialogScpHeaderItem");
		
		for (var prop in data) {
			
			// parse xml to create text string
			var child = data[prop];
			var _labelHeader:Label = empty_mc.attachMovie("Label", "scrollList_" + prop.toString(), empty_mc.getNextHighestDepth(), {_x:xOffset, _y:yOffset, _height:20, autoSize:true, text:prop.toString(), styleName:styleObj_Header});
			yOffset += 15;
			
			for(var i=0; i < child.length; i++) {
				var name = child[i].value;
				var _label = empty_mc.attachMovie("Label", "scrollList_" + prop.toString() + "_" + i, empty_mc.getNextHighestDepth(), {_x:xOffset+10, _y:yOffset, _height:20, autoSize:true, text:name, styleName:styleObj_General});
				yOffset += 15;
			}
			
			yOffset += 20;
			
		}
		
		setup();
		
		_scrollList_scp._width = this._width - (2*_scrollList_scp._x);
		_scrollList_scp.redraw(true);
		
		//fire event to say we have loaded
		_container.contentLoaded();
	}
	
	public function setup() {
		setStyles();
		loadLogo();
		
		Debugger.log("build no: " + ApplicationParent.SERIAL_NO, Debugger.GEN, "setup", "AboutDialog");
		
		
		version_lbl.text = Dictionary.getValue('about_popup_version_lbl');
		version_txt.htmlText = _root.version;
		
		version_txt._x = version_lbl._x + version_lbl.textWidth + 2;
		
		var ref_lbl:String = Dictionary.getValue('stream_reference_lbl');
		var stream_url:String = Dictionary.getValue('stream_url', [ref_lbl.toLowerCase()]);
		var stream_url_atag:String = "<u><a href='http://" + stream_url + "' target='_blank'>" + stream_url + "</a></u>" 
		
		copyright_txt.htmlText = Dictionary.getValue('about_popup_copyright_lbl', [ref_lbl]);
		trademark_txt.htmlText = Dictionary.getValue('about_popup_trademark_lbl', [ref_lbl, stream_url_atag]);
		
		var license_url:String = Dictionary.getValue('gpl_license_url');
		var license_url_atag:String = "<u><a href='http://" + license_url + "' target='_blank'>" + license_url + "</a></u>";
		license_txt.htmlText = Dictionary.getValue('about_popup_license_lbl', [license_url_atag]);
		
	}
	
	private function loadLogo():Void{
		logo = this.createEmptyMovieClip("logo", this.getNextHighestDepth());
		var ml = new MovieLoader(Config.getInstance().serverUrl+AboutDialog.LOGO_PATH,setUpLogo,this,logo);	
	}
	
	private function setUpLogo(logo):Void{
		logo._x = AboutDialog.LOGO_X;
		logo._y = AboutDialog.LOGO_Y;
	}
	
	/**
    * Main resize method, called by scrollpane container/parent
    */
    public function setSize(w:Number,h:Number):Void{
    }
    
	
	/**
    * Event fired by StyleManager class to notify listeners that Theme has changed
    * it is up to listeners to then query Style Manager for relevant style info
    */
    public function themeChanged(event:Object):Void{
        if(event.type=='themeChanged') {
            //Theme has changed so update objects to reflect new styles
            setStyles();
        }else {
            Debugger.log('themeChanged event broadcast with an object.type not equal to "themeChanged"',Debugger.CRITICAL,'themeChanged','org.lamsfoundation.lams.WorkspaceDialog');
        }
    }
	
    /**
    * Event dispatched by parent container when close button clicked
    */
    public function click(e:Object):Void{
        e.target.deletePopUp();
    }
    
    
}