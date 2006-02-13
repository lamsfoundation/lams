import org.lamsfoundation.lams.common.*;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.util.ui.*;
import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.authoring.tk.*;
import mx.controls.*;
import mx.managers.*;
import mx.events.*;

/**  
* TemplateActivity  - 
* these are the visual elements in the toolkit - 
* each one representing a LearningLibrary template activity
*/  
class org.lamsfoundation.lams.authoring.tk.TemplateActivity extends MovieClip{  
     //Declarations  
	 private var bkg_pnl:MovieClip;
	 private var title_lbl:Label;
	 private var select_btn:Button;
	 private var icon_mc:MovieClip;
	
	 private var _instance:TemplateActivity;
	 private var icon_mcl:MovieClipLoader;
	 

	 //this is set by the init object
	 //contains refs to the classInstances of the activities in this TemplateActivity
	 private var _activities:Array;
	 private var _mainActivity:Activity;
	 private var _childActivities:Array;

	 
	 private var _toolkitView:ToolkitView;
	 
    /**  
     Constructor - creates an onEnterFrame doLater call to init
    */ 
    function TemplateActivity() {  
        this.onEnterFrame = init;
		
		_childActivities = new Array();
		
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
		//_global.breakpoint();
		Debugger.log('_activities.length:'+_activities.length,Debugger.GEN,'init','TemplateActivity');
		//find the 'main'/container activity
		if(_activities.length > 1){
			for (var i=0; i<_activities.length; i++){

				if(_activities[i].activityTypeID == Activity.PARALLEL_ACTIVITY_TYPE){
					_mainActivity = _activities[i];
					//SOMEBODY THINK OF THE CHILDREN!
					setUpComplexActivity();
					
				}
			}
		}else if(_activities.length == 1){
			_mainActivity = _activities[0];
		}else{
			new LFError("The activities array recieved is not valid, maybe undefined","init",this);
			//we must bail entirely now to prevent a crash or loop
			this.removeMovieClip();
		}
		
		
		
		
		icon_mcl = new MovieClipLoader();
		icon_mcl.addListener(this);
		
		//Debugger.log('_toolkitView:'+_toolkitView.getClassname(),4,'init','TemplateActivity');
		//set up the button handlers
		select_btn.onPress = Proxy.create(this,this['select']);
		//create an mc to hold th icon
		icon_mc = this.createEmptyMovieClip("icon_mc",getNextHighestDepth());
		loadIcon();
	}

	/**
	 * Populates the _childActivities array if this is a complex activity
	 * @usage   
	 * @return  
	 */
	private function setUpComplexActivity(){
		if(_mainActivity.activityTypeID == Activity.PARALLEL_ACTIVITY_TYPE){
			//populate the _childActivities hash
			for(var i=0; i<_activities.length; i++){
				if(_activities[i].parentActivityID == _mainActivity.activityID){
					//TODO: Check they are tool activities, if not give a warning and bail.
					_childActivities.push(_activities[i]);
				}
			}
		}else{
			new LFError("Cannot handle this activity type yet","setUpComplexActivity",this,'_mainActivity.activityTypeID:'+_mainActivity.activityTypeID);
		}
	}
	
	/**  
	* Loads the icon for the temopate activity using a movieclip loader
	*
	*/
	private function loadIcon(loadDefaultIcon:Boolean):Void{
		//Debugger.log('Loading icon:'+Config.getInstance().serverUrl+_toolActivity.libraryActivityUIImage,4,'loadIcon','TemplateActivity');
		//TODO: Get URL from packet when its done.
		//icon_mcl.loadClip(Config.getInstance().serverUrl+"images/icon_chat.swf",icon_mc);
		if(loadDefaultIcon){
			Debugger.log('Going to use default icon: images/icon_missing.swf',Debugger.GEN,'loadIcon','TemplateActivity');
			//icon_missing.swf
			_mainActivity.libraryActivityUIImage = "images/icon_missing.swf";
			//icon_mcl.loadClip(Config.getInstance().serverUrl+"images/icon_missing.swf",icon_mc);
			
		}
		var icon_url = Config.getInstance().serverUrl+_mainActivity.libraryActivityUIImage;
		Debugger.log('Loading icon:'+icon_url,4,'loadIcon','TemplateActivity');
		icon_mcl.loadClip(icon_url,icon_mc);
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
	
	private function onLoadError(icon_mc:MovieClip,errorCode:String):Void{
		switch(errorCode){
			
			case 'URLNotFound' :
				Debugger.log('TemplateActivity icon failed to load - URL is not found:'+icon_mc._url,Debugger.CRITICAL,'onLoadError','TemplateActivity');	
				break;
			case 'LoadNeverCompleted' :
				Debugger.log('TemplateActivity icon failed to load - Load never completed:'+icon_mc,Debugger.CRITICAL,'onLoadError','TemplateActivity');	
				break;
		}
		
		//if there was an error - try and load the missing.swf
		loadIcon(true);
		//draw();
		
	}
	
	/**  
	* Does the visual rendering work of this TemplateActivity
	*/
	private function draw():Void{
		
		//Debugger.log('bkg_pnl:'+bkg_pnl,4,'draw','TemplateActivity');
		bkg_pnl.setStyle("borderStyle","outset");
		//Debugger.log('Setting '+this+' title '+_templateActivityData.title,4,'draw','TemplateActivity')
		title_lbl.text=_mainActivity.title;
		//attach the icon now...
		
		//icon_mc.loadMovie("http://dolly.uklams.net/lams/lams_central/icons/icon_chat.swf");
		icon_mc._width = 20;
		icon_mc._height = 20;
		//Debugger.log('icon_mc._width:'+icon_mc._width,4,'draw','TemplateActivity');
		//Debugger.log('icon_mc._height:'+icon_mc._height,4,'draw','TemplateActivity');
	}
	
	/**  
	* Gets this TemplateActivity's data
	*/
	function get toolActivity():Object{
		/*
		//if we only have one element then return that cos it must be a single toolActiivity
		if(_activities.length ==1){
			return _mainActivity;
		}else{
			return new LFError("There is more than one item in the activities array, may be a complex activity - cant return a ToolActitiy","get toolActivity",this);
		}
		*/
		Debugger.log('This function is deprecated, use mainActivity instead',Debugger.MED,'getToolActivity','TemplateActivity');
		return _mainActivity;
		
	}
	
	function get mainActivity():Activity{
		return _mainActivity;
	}
	
	/**
	 * 
	 * @usage   
	 * @param   newactivities 
	 * @return  
	 */
	public function set activities (newactivities:Array):Void {
		_activities = newactivities;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get activities ():Array {
		return _activities;
	}

	

	function setState(selected:Boolean):Void{
		//Debugger.log('this'+this+' selected:'+selected,4,'setState','TemplateActivity');
		//_global.breakpoint();
		//Debugger.log('bkg_pnl:'+bkg_pnl,4,'setState','TemplateActivity');
		if(selected){
			//TODO: Get the StyleObj from Style manager and apply the selected type state thang
			bkg_pnl.setStyle("backgroundColor",0x0099ff);
			bkg_pnl.setStyle("borderStyle","inset");
			
		}else{
			bkg_pnl.setStyle("backgroundColor",0xCCCCCC);
			bkg_pnl.setStyle("borderStyle","outset");
		}
	}
	
	private function select():Void{
		//Debugger.log('btn: '+this,4,'select','TemplateActivity');
		//Debugger.log('_toolkitView:'+_toolkitView.className(),4,'select','TemplateActivity');
		var toolkitController = _toolkitView.getController();
		toolkitController.selectTemplateActivity(this);			
	}
	
	
	/**
	 * 
	 * @usage   
	 * @param   newchildActivities 
	 * @return  
	 */
	public function set childActivities (newchildActivities:Array):Void {
		_childActivities = newchildActivities;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get childActivities ():Array {
		return _childActivities;
	}
	
}