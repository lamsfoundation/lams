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

import mx.controls.*
import mx.utils.*
import mx.managers.*
import mx.events.*

import org.lamsfoundation.lams.learner.*
import org.lamsfoundation.lams.common.Sequence;
import org.lamsfoundation.lams.common.ToolTip;
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.common.Config;

class Header extends MovieClip {
	
	private var _header_mc:MovieClip;
	private var _container:MovieClip;		// Holding Container
	private var logo:MovieClip; 			// LAMS/RAMS logo
	
	public static var LOGO_WIDTH:Number = 67;
	public static var LOGO_HEIGHT:Number = 25;
	public static var LOGO_X:Number = 7;
	public static var LOGO_Y:Number = 4;
	public static var LOGO_PATH:String = "www/images/learner.logo.swf";
	
	private var resume_btn:MovieClip;         //Resume and Exit buttons
    private var exit_btn:MovieClip;
	private var lessonHead_pnl:MovieClip;
	private var resume_lbl:TextField;
	private var exit_lbl:TextField;
	private var _tip:ToolTip;
	private var export_btn:Button;
	private var export_lbl:TextField;
	
	private var _lessonName:Label;
	
	private var panel:MovieClip;       //The underlaying panel base
    
	private var _tm:ThemeManager;
	private var _dictionary:Dictionary;
	
	//These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
   
	
	/** 
	* constructor
	*/
	public function Header() {
		//Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);
		
		_tm = ThemeManager.getInstance();
		_tip = new ToolTip();
		_dictionary = Dictionary.getInstance();
		_dictionary.addEventListener('init',Proxy.create(this,setLabels));
		
		//let it wait one frame to set up the components.
		MovieClipUtils.doLater(Proxy.create(this,init));
		
	}
	
	/**
    * Called a frame after movie attached to allow components to initialise
    */
    public function init(){
		trace('initialing header..');
		
        //Delete the enterframe dispatcher
        delete this.onEnterFrame;
		
		_header_mc = this;
		
		loadLogo();
		
		setLabels();
		resize(Stage.width);
		
		
		//Add event listeners for resume and exit buttons
		
		resume_btn.onRelease = function(){
			trace('on releasing resuming button..');
			var app:Application = Application.getInstance();
			app.getLesson().resumeLesson();
		}
		
		exit_btn.onRelease = function(){
			trace('on releasing exit button..');
			var app:Application = Application.getInstance();
			app.getLesson().exitLesson();
		}
		
		export_btn.onRelease = function(){
			var app:Application = Application.getInstance();
			app.getLesson().exportLesson();
		}
		
		resume_btn.onRollOver = Proxy.create(this,this['showToolTip'], resume_btn, "hd_resume_tooltip");
		resume_btn.onRollOut = Proxy.create(this,this['hideToolTip']);
		
		exit_btn.onRollOver = Proxy.create(this,this['showToolTip'], exit_btn, "hd_exit_tooltip");
		exit_btn.onRollOut = Proxy.create(this,this['hideToolTip']);
		
		export_btn.onRollOver = Proxy.create(this,this['showToolTip'], export_btn, "ln_export_tooltip");
		export_btn.onRollOut = Proxy.create(this,this['hideToolTip']);
		
		export_btn._visible = false;
		export_lbl._visible = false;
		this.onEnterFrame = setLabels;
		
	}
	
	private function loadLogo():Void{
		logo = this.createEmptyMovieClip("logo", this.getNextHighestDepth());
		var ml = new MovieLoader(Config.getInstance().serverUrl+Header.LOGO_PATH,setUpLogo,this,logo);	
	}
	
	private function setUpLogo(logo):Void{
		logo._x = Header.LOGO_X;
		logo._y = Header.LOGO_Y;
	}
	
	public function showToolTip(btnObj, btnTT:String):Void{
		
		var Xpos = Application.HEADER_X+ 5;
		var Ypos = Application.HEADER_Y+( btnObj._y+btnObj._height)+2;
		var ttHolder = Application.tooltip;
		var ttMessage = Dictionary.getValue(btnTT);
		var ttWidth = 150
		_tip.DisplayToolTip(ttHolder, ttMessage, Xpos, Ypos, undefined, ttWidth);
		
	}
	
	public function hideToolTip():Void{
		_tip.CloseToolTip();
	}
	
	private function setStyles(){
		var styleObj = _tm.getStyleObject('smallLabel');
		_lessonName.setStyle('styleName', styleObj);
		
		styleObj = _tm.getStyleObject('BGPanel');
		lessonHead_pnl.setStyle('styleName', styleObj);
		
	}
	
	private function setLabels(){
		//Set the text for buttons
        resume_lbl.text = Dictionary.getValue('hd_resume_lbl');
        exit_lbl.text = Dictionary.getValue('hd_exit_lbl');
		export_lbl.text = Dictionary.getValue('ln_export_btn');
		
		setStyles();
		
		delete this.onEnterFrame; 
		
		dispatchEvent({type:'load',target:this});
        
	}
	
	public function showExportButton(v:Boolean) {
		Debugger.log("Show/Hide Export Button: " + v, Debugger.GEN, "showExportButton", "Header");
		export_btn._visible = v;
		export_lbl._visible = v;
	}
	
	public function setLessonName(lessonName:String){
		_lessonName.text = lessonName;
	}
	
	public function resize(width:Number){
		panel._width = width;
		
	}
	
	function get className():String { 
        return 'Header';
    }
}