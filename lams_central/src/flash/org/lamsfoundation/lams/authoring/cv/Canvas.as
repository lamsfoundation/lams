import org.lamsfoundation.lams.authoring.cv.*
import org.lamsfoundation.lams.authoring.tk.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.authoring.*
import org.lamsfoundation.lams.common.ui.*
import mx.managers.*

/**
* The canvas is the main screen area of the LAMS application where activies are added and sequenced
*/
class org.lamsfoundation.lams.authoring.cv.Canvas {
	//Model
	private var canvasModel:CanvasModel;

	//View
	private var canvasView:CanvasView;
    private var _canvasView_mc:MovieClip;
	private var app:Application;
	private var ddm:DesignDataModel;
    
    //Defined so compiler can 'see' events added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
    

	/**
	* Canvas Constructor
	*
	* @param   target_mc	Target clip for attaching view
	*/
	public function Canvas (target_mc:MovieClip,depth:Number,x:Number,y:Number,w:Number,h:Number){
        mx.events.EventDispatcher.initialize(this);
        
		//Create the model.
		canvasModel = new CanvasModel();

		//Create the view
		_canvasView_mc = target_mc.createChildAtDepth("canvasView",DepthManager.kTop);		

        //Cast toolkit view clip as ToolkitView and initialise passing in model
		canvasView = CanvasView(_canvasView_mc);
		canvasView.init(canvasModel,undefined,x,y,w,h);
        
        //Add listener to view so that we know when it's loaded
        canvasView.addEventListener('load',Proxy.create(this,viewLoaded));

        //Register view with model to receive update events
		canvasModel.addObserver(canvasView);
        
        //Get reference to application and design data model
		app = Application.getInstance();
		ddm = app.getDesignDataModel();
        
        //Set the position by setting the model which will call update on the view
        canvasModel.setPosition(x,y);
        
        //Initialise size to the designed size
        canvasModel.setSize(w,h);
	}
    
    /**
    * Event dispatched from the view once it's loaded
    */
    public function viewLoaded(evt:Object) {
        Debugger.log('Canvas view loaded: ' + evt.type,Debugger.GEN,'viewLoaded','Canvas');
        if(evt.type=='load') {
            dispatchEvent({type:'load',target:this});
        }else {
            Debugger.log('Event type not recognised : ' + evt.type,Debugger.CRITICAL,'viewLoaded','Canvas');
        }
    }
	
	/**
	* Opens a design using workspace and user to select design ID
	*
	*/
	public function openDesignBySelection(){
        //Work space opens dialog and user will select view
        var ws = Application.getInstance().getWorkspace();
        ws.userSelectItem();
	}
    
    public function openDesignById(designId:Number){
        //trace('Canvas.openDesignById');
        //Request design from server via Comms
        var callback:Function = Proxy.create(this,setDesign);
        
		Application.getInstance().getComms().getRequest('http://dolly.uklams.net/lams/lams_authoring/learning_design.xml',callback, true);
		//var designObject:Object = Application.getInstance().getComms().getRequest('authoring/author.do?method=getLearningDesign&learningDesignID='+designId,callback);
        //var designObject:Object = Application.getInstance().getComms().getRequest('http://dolly.uklams.net/lams/lams_authoring/liblist.xml',callback);
        
    }
    
	public function setDroppedTemplateActivity(ta:TemplateActivity):Void{
		var dto:Object = ta.getTemplateActivityData();
		Debugger.log('ta, got a:'+dto.title,4,'setDroppedTemplateActivity','Canvas');		
		//TODO: Create a fully fledged activity - including all required fields.
		
        
		//see what kind of activity we are to produce.
		//if(dto.
		_global.breakPointbreakpoint();
//							function Activity(activityUIID:Number, activityTypeID:Number, activityCategoryID:Number, learningLibraryID:Number,libraryActivityUIImage:String){
		//TODO:FIx this bug here... constructor values are not correct, need to rebuild server first and use actual call.
		Debugger.log('!!!! WARNING USING HARDCODED ACTIVITY VALUES To be fixed at deployment !!!!!!' ,Debugger.CRITICAL,'setDroppedTemplateActivity','Canvas');		
		var actToAdd:Activity = new Activity(ddm.newUIID(), Activity.TOOL_ACTIVITY_TYPE, Activity.CATEGORY_COLLABORATION ,dto.learningLibraryID, dto.libraryActivityUiImage);

		
		//give it the mouse co-ords
		actToAdd.xCoord = canvasView.getViewMc()._xmouse;
		actToAdd.yCoord = canvasView.getViewMc()._ymouse;
		
		//TODO:add all the other fields we have from a template activity
		actToAdd.title = dto.title;
		
		Debugger.log('actToAdd.activityUIID:'+actToAdd.activityUIID,4,'setDroppedTemplateActivity','Canvas');		
		
		//TODO: try to add this to the DDM
		var success:Boolean = ddm.addActivity(actToAdd);
		if(success){
			//set isDirty flag in cv.model
			canvasModel.setDirty();
		}else{
			Debugger.log('Adding activity to DDM failed for :'+dto.title,Debugger.CRITICAL,'setDroppedTemplateActivity','Canvas');			
		}
	}
	
	/**
	 * Called by Comms after a design has been loaded, usually set as the call back of something like openDesignByID.
	 * Will accept a learningDesign DTO and then render it all out.
	 * @usage   
	 * @param   designData 
	 * @return  
	 */
    public function setDesign(designData:Object){
       //_global.breakPoint();
		Debugger.log('designData.title:'+designData.title,4,'setDesign','Canvas');
		
		if(ddm.clearDesign()){
			ddm.setDesign(designData);
			canvasModel.setDirty();
		}else{
			Debugger.log('Set design failed as old design could not be cleared',Debugger.CRITICAL,"setDesign",'Canvas');		
		}
    }
	
	public function toggleTransitionTool():Void{
		var c = Cursor.getCurrentCursor();
		if(c==Application.C_TRANSITION){
			stopTransitionTool();
		}else{
			startTransitionTool();
		}
	}
	
	/**
	 * Called by the top menu bar and the tool bar to start the transition tool, switches cursor.
	 * @usage   
	 * @return  
	 */
	public function startTransitionTool():Void{
		Debugger.log('Starting transition tool',Debugger.GEN,'startTransitionTool','Canvas');			
		Cursor.showCursor(Application.C_TRANSITION);
		canvasModel.startTransitionTool();
	}
	
	/**
	 * Called by the top menu bar and the tool bar to stop the transition tool, switches cursor.
	 * @usage   
	 * @return  
	 */
	public function stopTransitionTool():Void{
		Debugger.log('Stopping transition tool',Debugger.GEN,'stopTransitionTool','Canvas');			
		Cursor.showCursor(Application.C_DEFAULT);
		canvasModel.stopTransitionTool();
	}
	
	/**
	* Used by application to set the size
	* @param width The desired width
	* @param height the desired height
	*/
	public function setSize(width:Number,height:Number):Void{
		canvasModel.setSize(width, height);
	}
	
	/**
	* Used by application to set the Position
	* @param x
	* @param y
	*/
	public function setPosition(x:Number,y:Number):Void{
		canvasModel.setPosition(x,y);
	}
	
	public function get model():CanvasModel{
		return getCanvasModel();
	}
	
	public function getCanvasModel():CanvasModel{
			return canvasModel;
	}
	public function get view():MovieClip{
		return getCanvasView();
	}
		
	
	public function getCanvasView():MovieClip{
		return canvasView;
	}
	
	public function get className():String{
		return 'Canvas';
	}
	
}
