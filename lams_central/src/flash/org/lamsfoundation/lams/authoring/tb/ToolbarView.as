import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.util.*
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
    
	private var new_btn:Button;
	private var open_btn:Button;
	private var save_btn:Button;
	private var copy_btn:Button;
	private var paste_btn:Button;
	private var trans_btn:Button;
	private var optional_btn:Button;
	private var gate_btn:Button;
	private var preview_btn:Button;
	private var bkg_pnl:Panel;
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
		_dictionary.addEventListener('init',Proxy.create(this,setupLabels));
	}
    
    /**
    * called by container. Sets up MVC and schedules createToolbar() for a frame later
    */
    public function init(m:Observable, c:Controller) {
		//Invoke superconstructor, which sets up MVC relationships.
		super (m, c);
		
		
        //In one frame call createToolbar this gives components one frame to setup etc.
        MovieClipUtils.doLater(Proxy.create(this,createToolbar));		
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
		
		
		
		_toolbar_mc.new_btn.addEventListener("click",controller);
		_toolbar_mc.open_btn.addEventListener("click",controller);
		_toolbar_mc.save_btn.addEventListener("click",controller);
		_toolbar_mc.copy_btn.addEventListener("click",controller);
		_toolbar_mc.paste_btn.addEventListener("click",controller);
		_toolbar_mc.trans_btn.addEventListener("click",controller);
		_toolbar_mc.optional_btn.addEventListener("click",controller);
		_toolbar_mc.gate_btn.addEventListener("click",controller);
		_toolbar_mc.group_btn.addEventListener("click",controller);
		_toolbar_mc.preview_btn.addEventListener("click",controller);
		
		
        Debugger.log('dispatch it',Debugger.GEN,'createToolbar','ToolbarView');
        
       //Now that view is setup dispatch loaded event
       dispatchEvent({type:'load',target:this});
	}
	public function setupLabels(){
		
		_toolbar_mc.new_btn.label = Dictionary.getValue('new_btn');
		_toolbar_mc.open_btn.label = Dictionary.getValue('open_btn');
		_toolbar_mc.save_btn.label = Dictionary.getValue('save_btn');
		_toolbar_mc.copy_btn.label = Dictionary.getValue('copy_btn');
		_toolbar_mc.paste_btn.label = Dictionary.getValue('paste_btn');
		_toolbar_mc.trans_btn.label = Dictionary.getValue('trans_btn');
		_toolbar_mc.optional_btn.label = Dictionary.getValue('optional_btn');
		_toolbar_mc.gate_btn.label = Dictionary.getValue('gate_btn');
		_toolbar_mc.group_btn.label = Dictionary.getValue('group_btn');
		_toolbar_mc.preview_btn.label = Dictionary.getValue('preview_btn');
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
	
    /**
    * Sets the size of the Toolbar on stage, called from update
    */
	private function setSize(tm:ToolbarModel):Void{
		
        var s:Object = tm.getSize();
        //Size panel
		trace('toolbar view  setting width to '+s.w);
		bkg_pnl.setSize(s.w,bkg_pnl._width);
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
		preview_btn.setStyle('styleName',styleObj);
		styleObj = _tm.getStyleObject('BGPanel');
		bkg_pnl.setStyle('styleName',styleObj);
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
    
    /*
    * Returns the default controller for this view.
    */
    public function defaultController (model:Observable):Controller {
        return new ToolbarController(model);
    }
}
