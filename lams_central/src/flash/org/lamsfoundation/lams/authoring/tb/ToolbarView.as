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
import org.lamsfoundation.lams.common.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.authoring.Application
import org.lamsfoundation.lams.authoring.tb.*
import org.lamsfoundation.lams.common.mvc.*
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.common.dict.*
import mx.managers.*
import mx.controls.*
/*
* Authoring view for the toolbar
*/
class ToolbarView extends AbstractView {
	//Toolbar clip
	private var _toolbar_mc:MovieClip;
	private var _tm:ThemeManager;
	private var _tip:ToolTip;
    private var btnOffset_X:Number = 4;
	private var btnOffset_Y:Number = 6;
	private var new_btn:Button;
	private var open_btn:Button;
	private var save_btn:Button;
	private var copy_btn:Button;
	private var paste_btn:Button;
	private var trans_btn:Button;
	private var optional_btn:Button;
	private var gate_btn:Button;
	private var flow_btn:Button;
	private var branch_btn:Button;
	private var group_btn:Button;
	private var preview_btn:Button;
	private var _toolbarMenu:Array;
	//private var btn_text:TextField;
	private var bkg_pnl:Panel;
	private var flow_bkg_pnl:Panel;
	private var _dictionary:Dictionary;
	
    //Defined so compiler can 'see' events added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
    
    //Compiler needs this, will be overwtitten when mx.managers imported through MovieClip.prototype
    public var createChildAtDepth:Function;
	
	/*
	* Constructor
	*/
	public function ToolbarView (){
        //Set up to use Flash Event Delegation model
		
        mx.events.EventDispatcher.initialize(this);  
		_tm = ThemeManager.getInstance();
		_dictionary = Dictionary.getInstance();
		_tip = new ToolTip();
		//_dictionary.addEventListener('init',Proxy.create(this,setupButtons));
		//_dictionary.addEventListener('init',Proxy.create(this,setupLabels));
	}
    
    /**
    * called by container. Sets up MVC and schedules createToolbar() for a frame later
    */
    public function init(m:Observable, c:Controller) {
		//Invoke superconstructor, which sets up MVC relationships.
		super (m, c);
		//In one frame call createToolbar this gives components one frame to setup etc.
        MovieClipUtils.doLater(Proxy.create(this,createToolbar));		
		this.tabChildren = true;
		setTabIndex();
    }
	
	public function showHideAssets(v:Boolean){
		
		//branch_btn.enabled = false;
		gate_btn.visible = v;
		branch_btn.visible = v;
		flow_bkg_pnl.visible = v;
		var flowW:Number = flow_btn.width
		var gateW:Number = gate_btn.width
		var branchW:Number = branch_btn.width
		var widthSet1:Number = Math.max(flowW, gateW)
		var widthSet2:Number = Math.max(flowW, branchW)
		var maxWidth:Number = Math.max(widthSet1, widthSet2)
		trace("flow  width: "+flowW+" gate width: "+gateW+" branch width: "+branchW+" and max button width: "+maxWidth)
		flow_bkg_pnl.setSize(maxWidth+6, 95)
		flow_bkg_pnl._x = branch_btn._x-3;
		
		
	}
    
	/*
	* Creates toolbar clip 
	*
	* @param   target_mc	container clip for toolbar
	* @param   depth   		clip depth
	* @param   x   			x pos in pixels
	* @param   y   			y pos in pixels
	*/
	public function createToolbar(){
		setStyles();
        _toolbar_mc = this;
		
        //Add the button handlers, essentially this is handing on clicked event to controller.
        var controller = getController();
		
		new_btn.addEventListener("click",controller);
		open_btn.addEventListener("click",controller);
		save_btn.addEventListener("click",controller);
		copy_btn.addEventListener("click",controller);
		paste_btn.addEventListener("click",controller);
		trans_btn.addEventListener("click",controller);
		optional_btn.addEventListener("click",controller);
		flow_btn.addEventListener("click",controller);
		gate_btn.addEventListener("click",controller);
		group_btn.addEventListener("click",controller);
		preview_btn.addEventListener("click",controller);
		
		// Button handler for rollover and rollout.
		
		new_btn.onRollOver = Proxy.create(this,this['showToolTip'], new_btn, "new_btn_tooltip");
		new_btn.onRollOut = Proxy.create(this,this['hideToolTip']);
		
		open_btn.onRollOver = Proxy.create(this,this['showToolTip'], open_btn, "open_btn_tooltip");
		open_btn.onRollOut = Proxy.create(this,this['hideToolTip']);
		
		save_btn.onRollOver = Proxy.create(this,this['showToolTip'], save_btn, "save_btn_tooltip");
		save_btn.onRollOut = Proxy.create(this,this['hideToolTip']);
		
		copy_btn.onRollOver = Proxy.create(this,this['showToolTip'], copy_btn, "copy_btn_tooltip");
		copy_btn.onRollOut = Proxy.create(this,this['hideToolTip']);
		
		paste_btn.onRollOver = Proxy.create(this,this['showToolTip'], paste_btn, "paste_btn_tooltip");
		paste_btn.onRollOut = Proxy.create(this,this['hideToolTip']);
		
		trans_btn.onRollOver = Proxy.create(this,this['showToolTip'], trans_btn, "trans_btn_tooltip");
		trans_btn.onRollOut = Proxy.create(this,this['hideToolTip']);
		
		optional_btn.onRollOver = Proxy.create(this,this['showToolTip'], optional_btn, "optional_btn_tooltip");
		optional_btn.onRollOut = Proxy.create(this,this['hideToolTip']);
		
		flow_btn.onRollOver = Proxy.create(this,this['showToolTip'], flow_btn, "flow_btn_tooltip");
		flow_btn.onRollOut = Proxy.create(this,this['hideToolTip']);
		
		gate_btn.onRollOver = Proxy.create(this,this['showToolTip'], gate_btn, "gate_btn_tooltip");
		gate_btn.onRollOut = Proxy.create(this,this['hideToolTip']);
		
		branch_btn.onRollOver = Proxy.create(this,this['showToolTip'], branch_btn, "branch_btn_tooltip");
		branch_btn.onRollOut = Proxy.create(this,this['hideToolTip']);
		
		group_btn.onRollOver = Proxy.create(this,this['showToolTip'], group_btn, "group_btn_tooltip");
		group_btn.onRollOut = Proxy.create(this,this['hideToolTip']);
		
		preview_btn.onRollOver = Proxy.create(this,this['showToolTip'], preview_btn, "preview_btn_tooltip");
		preview_btn.onRollOut = Proxy.create(this,this['hideToolTip']);
		
		showHideAssets(false);
		Debugger.log('dispatch it',Debugger.GEN,'createToolbar','ToolbarView');
        //Now that view is setup dispatch loaded event
       dispatchEvent({type:'load',target:this});
	}
	
	public function setupLabels(){
		
		new_btn.label = Dictionary.getValue('new_btn');
		open_btn.label = Dictionary.getValue('open_btn');
		save_btn.label = Dictionary.getValue('save_btn');
		copy_btn.label = Dictionary.getValue('copy_btn');
		paste_btn.label = Dictionary.getValue('paste_btn');
		trans_btn.label = Dictionary.getValue('trans_btn');
		optional_btn.label = Dictionary.getValue('optional_btn');
		gate_btn.label = Dictionary.getValue('gate_btn');
		branch_btn.label = Dictionary.getValue('branch_btn');
		flow_btn.label = Dictionary.getValue('flow_btn');
		group_btn.label = Dictionary.getValue('group_btn');
		preview_btn.label = Dictionary.getValue('preview_btn');
			}
	
	private function setTabIndex(selectedTab:String){
		
		//All Buttons Tab Index
		new_btn.tabIndex = 201
		open_btn.tabIndex = 202
		save_btn.tabIndex = 203
		copy_btn.tabIndex = 204
		paste_btn.tabIndex = 205
		trans_btn.tabIndex = 206
		optional_btn.tabIndex = 207
		flow_btn.tabIndex = 208
		gate_btn.tabIndex = 209
		branch_btn.tabIndex = 210
		group_btn.tabIndex = 211
		preview_btn.tabIndex = 212		
	}
	
	public function setupButtons(tm:ToolbarModel, menuList:Array){
		trace("setupButtons Called")
		_toolbarMenu = new Array();
		this.createTextField("btn_text", this.getNextHighestDepth(), btnOffset_X, -100, 10, 18); 
		for (var i=0; i<menuList.length; i++){
			trace("button label is: "+Dictionary.getValue(menuList[i][0]))
			var btnLabel:String = Dictionary.getValue(menuList[i][0])
			var btn_text = this["btn_text"]
			btn_text.autoSize = true;
			btn_text.html = true;
			btn_text.htmlText = btnLabel
			
			var btnWidth:Number = btn_text.textWidth+37
			trace("textwidth for button "+menuList[i][0]+" is: "+btnWidth)
			_toolbarMenu[i] = this.attachMovie("Button", menuList[i][0], this.getNextHighestDepth(), {label:btnLabel, icon:menuList[i][1] })
			_toolbarMenu[i].setSize(btnWidth, 25)
			if (i == 0){
				_toolbarMenu[i]._x  = btnOffset_X
			}else {
				_toolbarMenu[i]._x = (_toolbarMenu[i-1]._x+_toolbarMenu[i-1].width)+btnOffset_X
			}
			_toolbarMenu[i]._y = btnOffset_Y;
			
			if (i >= menuList.length-2){
				_toolbarMenu[i]._x = this.flow_btn._x;
				_toolbarMenu[i]._y = (_toolbarMenu[i-1]._y+_toolbarMenu[i-1].height)+btnOffset_Y
			}
			if (i == menuList.length){
				btn_text.removeTextField();
			}
			//_toolbarMenu[i].onRollOver = function(){
				
			//}
			//_toolbarMenu[i].onRollOut = Proxy.create (this, localOnRollOut); 
		}
		
	}
	/*
	* Updates state of the Toolbar, called by Toolbar Model
	*
	* @param   o   		The model object that is broadcasting an update.
	* @param   infoObj  object with details of changes to model
	*/
	public function update (o:Observable,infoObj:Object):Void {
	    //Cast the generic observable object into the Toolbar model.
        var tm:ToolbarModel = ToolbarModel(o);
		
        //Update view from info object
        switch (infoObj.updateType) {
            case 'POSITION' :
                setPosition(tm);
                break;
            case 'SIZE' :
                setSize(tm);
                break;
			case 'BUTTON' :
                setState(tm, infoObj);
                break;
			case 'SETMENU' :
				setupButtons(tm, infoObj.data);
				break;
            default :
                Debugger.log('unknown update type :' + infoObj.updateType,Debugger.CRITICAL,'update','org.lamsfoundation.lams.ToolbarView');
        }
	}
    
	/**
    * Sets the botton state of the Toolbar on stage, called from update
    */
	private function setState(tm:ToolbarModel, infoObj:Object):Void{
		Debugger.log('button name in setButtonState is : '+infoObj.button, Debugger.GEN,'setState','ToolbarView');		
        if (infoObj.button == "preview"){
			
			this.preview_btn.enabled = infoObj.buttonstate;
		}
	}
	
	public function showToolTip(btnObj, btnTT:String):Void{
		var Xpos = Application.TOOLBAR_X+ btnObj._x;
		var Ypos = (Application.TOOLBAR_Y+ btnObj._y+btnObj.height)+5;
		var ttHolder = Application.tooltip;
		//var ttMessage = btnObj.label;
		var ttMessage = Dictionary.getValue(btnTT);
		_tip.DisplayToolTip(ttHolder, ttMessage, Xpos, Ypos);
		
	}
	
	public function hideToolTip():Void{
		_tip.CloseToolTip();
	}
	
    /**
    * Sets the size of the Toolbar on stage, called from update
    */
	private function setSize(tm:ToolbarModel):Void{
		
        var s:Object = tm.getSize();
        //Size panel
		trace('toolbar view  setting width to '+s.w);
		bkg_pnl.setSize(s.w,bkg_pnl._height);
	}
	
    /**
    * Sets the position of the Toolbar on stage, called from update
    * @param tm Toolbar model object 
    */
	private function setPosition(tm:ToolbarModel):Void{
        var p:Object = tm.getPosition();
        this._x = p.x;
        this._y = p.y;
	}
	
	/**
    * Set the styles for the PI called from init. and themeChanged event handler
    */
    private function setStyles() {
        
		var styleObj = _tm.getStyleObject('button');
		new_btn.setStyle('styleName',styleObj);
		open_btn.setStyle('styleName',styleObj);
		save_btn.setStyle('styleName',styleObj);
		copy_btn.setStyle('styleName',styleObj);
		paste_btn.setStyle('styleName',styleObj);
		trans_btn.setStyle('styleName',styleObj);
		optional_btn.setStyle('styleName',styleObj);
		gate_btn.setStyle('styleName',styleObj);
		flow_btn.setStyle('styleName',styleObj);
		branch_btn.setStyle('styleName',styleObj);
		group_btn.setStyle('styleName', styleObj);
		preview_btn.setStyle('styleName',styleObj);
		styleObj = _tm.getStyleObject('BGPanel');
		bkg_pnl.setStyle('styleName',styleObj);
		styleObj = _tm.getStyleObject('FlowPanel');
		flow_bkg_pnl.setStyle('styleName',styleObj);
		/*
		_toolbar_mc.open_btn.addEventListener("click",controller);
		_toolbar_mc.save_btn.addEventListener("click",controller);
		_toolbar_mc.copy_btn.addEventListener("click",controller);
		_toolbar_mc.paste_btn.addEventListener("click",controller);
		_toolbar_mc.trans_btn.addEventListener("click",controller);
		_toolbar_mc.optional_btn.addEventListener("click",controller);
		_toolbar_mc.gate_btn.addEventListener("click",controller);
		_toolbar_mc.preview_btn.addEventListener("click",controller);
        */
    }
	
	/**
	 * Overrides method in abstract view to ensure cortect type of controller is returned
	 * @usage   
	 * @return  ToolbarController
	 */
	public function getController():ToolbarController{
		var c:Controller = super.getController();
		return ToolbarController(c);
	}
    
    /*
    * Returns the default controller for this view.
    */
    public function defaultController (model:Observable):Controller {
        return new ToolbarController(model);
    }
}
