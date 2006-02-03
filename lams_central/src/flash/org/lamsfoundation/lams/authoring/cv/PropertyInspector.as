import org.lamsfoundation.lams.authoring.cv.*;
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.ui.*
import mx.utils.*
import mx.events.*
import mx.controls.*

/*
*
* @author      DC
* @version     0.1
* @comments    Property Inspector for the canvas
* 
*/
class PropertyInspector extends MovieClip{
	
	private var _canvasModel:CanvasModel;
	private var _canvasController:CanvasController;
	 //References to components + clips 
    private var _container:MovieClip;       //The container window that holds the dialog. Will contain any init params that were passed into createPopUp
   
    private var toolDisplayName_lbl:Label;
	
	//tabs
	private var prop_btn:Button;
	private var comm_btn:Button;
   
	//Properties tab
    private var title_lbl:Label;
    private var title_txt:TextInput;
    private var desc_lbl:Label;
    private var desc_txt:TextInput;
    private var grouping_lbl:Label;
	private var currentGrouping_lbl:Label;
    private var runOffline_chk:CheckBox;
    private var defineLater_chk:CheckBox;
	
	
	//Comments tab
	/*
	private var runOffline_lbl:Label;
    private var runOnline_lbl:Label;
    private var runOffline_txa:TextArea;
    private var runOnline_txa:TextArea;
   */
   
   
   
	
	
	
	
	
	//screen assets:
	private var body_pnl:Panel;
	private var bar_pnl:Panel;
	
	//Defined so compiler can 'see' events added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	
	
	/**
	 * Constructor
	 */
	function PropertyInspector(){
		//register to recive updates form the model
		Debugger.log('Constructor',Debugger.GEN,'PropertyInspector','PropertyInspector');
		//Set up this class to use the Flash event delegation model
    	
		EventDispatcher.initialize(this);
		
		//let it wait one frame to set up the components.
		MovieClipUtils.doLater(Proxy.create(this,init));
		
	}
	
	public function init():Void{
		_canvasModel = _container._canvasModel;
		//might not need this ref..
		_canvasController = _container._canvasController;
		//_global.breakpoint();
		_canvasModel.addEventListener('viewUpdate',this);
		
		//Debugger.log('_canvasModel: ' + _canvasModel,Debugger.GEN,'init','PropertyInspector');
		
		//set up handlers

		title_txt.addEventListener("focusOut",this);
		desc_txt.addEventListener("focusOut",this);
		runOffline_chk.addEventListener("click",this);
		defineLater_chk.addEventListener("click",this);
		
		
		/*
		runOffline_txa.addEventListener("focusOut",this);
		runOnline_txa.addEventListener("focusOut",this);
		*/
		
		//showProperties();
		
		//fire event to say we have loaded
		_container.contentLoaded();
	}
	
	
	
	/**
	 * Recieves update events from the model.
	 * @usage   
	 * @param   event 
	 * @return  
	 */
	public function viewUpdate(event:Object):Void{
		//Debugger.log('Recived an Event dispather UPDATE!, updateType:'+event.updateType+', target'+event.target,4,'viewUpdate','PropertyInspector');
		 //Update view from info object
       
       var cm:CanvasModel = event.target;
	   
	   switch (event.updateType){
            case 'SELECTED_ITEM' :
                updateItemProperties(cm);
                break;
                   
				
            default :
                //Debugger.log('unknown update type :' + event.updateType,Debugger.CRITICAL,'update','org.lamsfoundation.lams.CanvasView');
		}

	}
	
	/**
	 * 
	 * @usage   
	 * @param   cm 
	 * @return  
	 */
	private function updateItemProperties(cm:CanvasModel):Void{
		//Debugger.log('cm.selectedItem:'+cm.selectedItem+' is a:'+typeof(cm.selectedItem)  ,4,'updateItemProperties','PropertyInspector');
		
		if(cm.selectedItem instanceof CanvasActivity){
			//Debugger.log('Its a canvas activity',4,'updateItemProperties','PropertyInspector');
			
			toolDisplayName_lbl.text = StringUtils.cleanNull(cm.selectedItem.activity.toolDisplayName);
			title_txt.text = StringUtils.cleanNull(cm.selectedItem.activity.title);
			desc_txt.text = StringUtils.cleanNull(cm.selectedItem.activity.description);
			runOffline_chk.selected = cm.selectedItem.activity.runOffline;
			defineLater_chk.selected = cm.selectedItem.activity.defineLater;
						
			currentGrouping_lbl.text = "GroupingUIID:"+StringUtils.cleanNull(cm.selectedItem.activity.runOffline.groupingUIID);
			
			/*
			runOffline_txa.text =  StringUtils.cleanNull(cm.selectedItem.activity.offlineInstructions);
			runOnline_txa.text =  StringUtils.cleanNull(cm.selectedItem.activity.onlineInstructions);
			*/
			
			
			
			
			
			
			
		}else if(cm.selectedItem instanceof CanvasTransition){
			//Debugger.log('Its a canvas transition',4,'updateItemProperties','PropertyInspector');
			title_txt.text = StringUtils.cleanNull(cm.selectedItem.transition.title);
			desc_txt.text = StringUtils.cleanNull(cm.selectedItem.transition.description);
			
			
			
			
		}else{
			//Debugger.log('Its a something we dont know',Debugger.CRITICAL,'updateItemProperties','PropertyInspector');
		
		}
	}
	
	/*
	private function showProperties():Void{
		//Properties tab
		title_lbl._visible = true;
		title_txt._visible = true;
		desc_lbl._visible = true;
		desc_txt._visible = true;
		grouping_lbl._visible = true;
		runOffline_chk._visible = true;
		//Comments tab
		runOffline_lbl._visible = false;
		runOnline_lbl._visible = false;
		runOffline_txa._visible = false;
		runOnline_txa._visible = false;
		
	}
	
	private function showComments():Void{
		//Properties tab
		title_lbl._visible = false;
		title_txt._visible = false;
		desc_lbl._visible = false;
		desc_txt._visible = false;
		grouping_lbl._visible = false;
		runOffline_chk._visible = false;
		//Comments tab
		runOffline_lbl._visible = true;
		runOnline_lbl._visible = true;
		runOffline_txa._visible = true;
		runOnline_txa._visible = true;
		
	
	}
	
	*/
	
	/**
    * Main resize method, called by scrollpane container/parent
    */
    public function setSize(w:Number,h:Number){
        //Debugger.log('setSize',Debugger.GEN,'setSize','org.lamsfoundation.lams.common.ws.WorkspaceDialog');
        //Size the bkg_pnl
		
        body_pnl.setSize(w,h-bar_pnl.height);
        bar_pnl.setSize(w);

        
    }
    
    //Gets+Sets
    /**
    * set the container refernce to the window holding the dialog
    */
    public function set container(value:MovieClip){
        _container = value;
    }
	
	
	/////////////////////////////////////////////////
	//------------ controller section -------------//
	/////////////////////////////////////////////////
	   
   /**
	 * Recieves the click events from the canvas views (inc Property Inspector) buttons.  Based on the target
	 * the relevent method is called to action the user request
	 * @param   evt 
	 */
	 /**/
	public function click(e):Void{
		var tgt:String = new String(e.target);
		//Debugger.log('click tgt:'+tgt,Debugger.GEN,'click','PropertyInspector');
		//Debugger.log('indexOf("defineLater_chk"):'+tgt.indexOf("defineLater_chk"),Debugger.GEN,'click','PropertyInspector');
		//Debugger.log('indexOf("runOffline_chk"):'+tgt.indexOf("runOffline_chk"),Debugger.GEN,'click','PropertyInspector');
		if(tgt.indexOf("defineLater_chk") != -1){
			
			_canvasModel.selectedItem.activity.defineLater = defineLater_chk.selected;
			Debugger.log('_canvasModel.selectedItem.activity.defineLater:'+_canvasModel.selectedItem.activity.defineLater,Debugger.GEN,'click','PropertyInspector');
		
		}else if(tgt.indexOf("runOffline_chk") != -1){
			
			_canvasModel.selectedItem.activity.runOffline = runOffline_chk.selected;
			Debugger.log('_canvasModel.selectedItem.activity.runOffline:'+_canvasModel.selectedItem.activity.runOffline,Debugger.GEN,'click','PropertyInspector');
		}
	}
	
	
	/**
	 * Recieves the click events from the canvas views (inc Property Inspector) buttons.  Based on the label
	 * the relevent method is called to action the user request
	 * @param   evt 
	 */
	public function focusOut(e):Void{
		var tgt:String = new String(e.target);
		Debugger.log('focusOut tgt:'+tgt,Debugger.GEN,'focusOut','PropertyInspector');
		
		if(tgt.indexOf("title_txt") != -1){
			//todo check if this is the right place to set edited content, should it be ddm?
			_canvasModel.selectedItem.activity.title = title_txt.text;
		}else if(tgt.indexOf("desc_txt") != -1){
			_canvasModel.selectedItem.activity.description= desc_txt.text;
		}
		
	}
	
}

