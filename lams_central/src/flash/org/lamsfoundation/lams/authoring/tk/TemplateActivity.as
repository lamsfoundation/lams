import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.util.ui.*;
import org.lamsfoundation.lams.authoring.tk.*;
import org.lamsfoundation.lams.authoring.Application;
import mx.controls.*;
import mx.managers.*;
import mx.events.*;

/**  
* TemplateActivity  - these are the visual elements in the toolkit - each one representing a LearningLibrary template activity
*/  
class TemplateActivity extends MovieClip{  
     //Declarations  
	 private var bkg_pnl:MovieClip;
	 private var title_lbl:Label;
	 private var select_btn:Button;	 private var icon_mc:MovieClip;		 private var _instance:TemplateActivity;	 private var icon_mcl:MovieClipLoader;
	 
	 //this is set by the init object
	 private var _templateActivityData:Object;
	 private var _toolkitView:ToolkitView;
	 
    /**  
    * Constructor - creates an onEnterFrame doLater call to init
    */ 
    function TemplateActivity() {          this.onEnterFrame = init;
    }  
  
	/**  
	* Initialises the class. set up button handlers
	*/
	function init():Void{
		delete this.onEnterFrame;
		
		_instance = this;
				var tkv = ToolkitView(_toolkitView);
		
		//Set up this class to use the Flash event delegation model  
        EventDispatcher.initialize(this);  
		
		
		
				
		icon_mcl = new MovieClipLoader();
		icon_mcl.addListener(this);
				//Debugger.log('_toolkitView:'+_toolkitView.getClassname(),4,'init','TemplateActivity');
		//set up the button handlers
		select_btn.onPress = Proxy.create(this,this['select']);
		//create an mc to hold th icon		icon_mc = this.createEmptyMovieClip("icon_mc",getNextHighestDepth());
		loadIcon();
	}

	/**  
	* Sets this TemplateActivity's data
	*/
	function setTemplateActivity(templateActivityData:Object):Void{
		_templateActivityData = templateActivityData;
		
	}
		/**  
	* Loads the icon for the temopate activity using a movieclip loader
	*
	*/
	private function loadIcon():Void{
		Debugger.log('!!!! HARD-CODED ICON URL !!!!!',4,'loadIcon','TemplateActivity');
		//TODO: Get URL from packet when its done.
		icon_mcl.loadClip("http://dolly.uklams.net/lams/lams_central/icons/icon_chat.swf",icon_mc);
	}
	
	/**  
	* Called by the MovieClip loader that loaded thie icon.
	* 
	* @param icon_mc The target mc that got the loaded movie
	*/
	private function onLoadInit(icon_mc:MovieClip):Void{
		Debugger.log('icon_mc:'+icon_mc,4,'onLoadInit','TemplateActivity');	
		//now icon is loaded lets call draw to display the stuff
		draw();
	}
	
	/**  
	* Does the visual rendering work of this TemplateActivity
	*/
	private function draw():Void{
		
		//Debugger.log('bkg_pnl:'+bkg_pnl,4,'draw','TemplateActivity');
		bkg_pnl.setStyle("borderStyle","outset");		//Debugger.log('Setting '+this+' title '+_templateActivityData.title,4,'draw','TemplateActivity');		title_lbl.text=_templateActivityData.title;
		//attach the icon now...		
		//icon_mc.loadMovie("http://dolly.uklams.net/lams/lams_central/icons/icon_chat.swf");		icon_mc._width = 20;
		icon_mc._height = 20;
		Debugger.log('icon_mc._width:'+icon_mc._width,4,'draw','TemplateActivity');
		Debugger.log('icon_mc._height:'+icon_mc._height,4,'draw','TemplateActivity');
	}
	
	/**  
	* Gets this TemplateActivity's data
	*/
	function getTemplateActivityData():Object{
		return _templateActivityData;
	}

	function setState(selected:Boolean):Void{
		Debugger.log('this'+this+' selected:'+selected,4,'setState','TemplateActivity');
		//_global.breakpoint();
		//Debugger.log('bkg_pnl:'+bkg_pnl,4,'setState','TemplateActivity');
		if(selected){
			//TODO: Get the StyleObj from Style manager and apply the selected type state thang
			bkg_pnl.setStyle("backgroundColor",0x0099ff);
			bkg_pnl.setStyle("borderStyle","inset");			
		}else{
			bkg_pnl.setStyle("backgroundColor",0xCCCCCC);
			bkg_pnl.setStyle("borderStyle","outset");
		}	}
	
	private function select():Void{
		//Debugger.log('btn: '+this,4,'select','TemplateActivity');
		var dummy:Number = 0;
		Debugger.log('_toolkitView:'+_toolkitView.className(),4,'select','TemplateActivity');
		var toolkitController = _toolkitView.getController();
		toolkitController.selectTemplateActivity(this);			
	}
}