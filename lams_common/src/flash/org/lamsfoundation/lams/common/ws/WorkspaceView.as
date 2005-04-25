import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.ws.*
import org.lamsfoundation.lams.common.mvc.*
import org.lamsfoundation.lams.common.ui.*
import mx.managers.*

/**
* Authoring view for the canvas
* @author   DI
*/
class org.lamsfoundation.lams.common.ws.WorkspaceView extends AbstractView 
{
	//Canvas clip
	private var _workspace_mc:MovieClip;
	private var workspaceDialog:MovieClip;
	
	/*
	* Constructor
	*/
	public function WorkspaceView (m:Observable, c:Controller,target_mc:MovieClip,depth:Number,x:Number,y:Number,w:Number,h:Number){
		// Invoke superconstructor, which sets up MVC relationships.
		// This view has no user inputs, so no controller is required.
		super (m, c);
		//
		createWorkspace (target_mc,depth,x,y,w,h);
	}
	/*
	* Creates workspace movie clip 
	*
	* @param   target_mc	The clip in which to create the movie clip.
	* @param   depth   		The depth at which to create the movie clip.
	* @param   x   			The movie clip's horizontal position in target.
	* @param   y   			The movie clip's vertical position in target.
	*/
	public function createWorkspace (target_mc:MovieClip,depth:Number,x:Number,y:Number,w:Number,h:Number):Void {
        trace(target_mc);
		_workspace_mc = target_mc.createEmptyMovieClip('workspace_mc',depth);
		_workspace_mc._x = x;
		_workspace_mc._y = y;
		_workspace_mc.bg_mc._width = w;
		_workspace_mc.bg_mc._height = h;
        //Workspace initially invisible
        //_workspace_mc._visible = false;
	}
	
	/*
	* Updates state of the canvas, called by Canvas Model
	*
	* @param   o   		The model object that is broadcasting an update.
	* @param   infoObj  object with details of changes to model
	*/
	public function update (o:Observable,infoObj:Object):Void {
		//Go through update object and update mc with visual changes required 
	}
    
    /**
    * Create a popup for the workspace
    */
    public function createWorkspacePopup(w:Number,height:Number){
        trace('workspace.createWorkspacePopup');
        var initObj = {};
        /*
        with (initObj){
            title='Select Workspace';
            _width = 400;
            _height = 300;
            _x = 200;
            _y = 200;
            contentPath = 'workspaceDialog';    
        }*/
        var popup = PopUpManager.createPopUp(_root, LFWindow, true,{title:'PopUpManager.createPopUp',closeButton:true,contentPath:'workspaceDialog',_x:200,_y:200});
        //var popup = PopUpManager.createPopUp(_workspace_mc, LFWindow, false,{title:'PopUpManager.createPopUp',closeButton:true,contentPath:'workspaceDialog',_x:200,_y:200});
        //var popup = Dialog.createPopUp(_workspace_mc, LFWindow,{title:'Dialog.createPopUp',closeButton:true,contentPath:'workspaceDialog',_x:200,_y:200});
        popup.setSize(550,450);
        
        //var popup = Dialog.createPopUp(_workspace_mc,initObj);
        //workspaceDialog = mx.managers.PopUpManager.createPopUp(_workspace_mc, LFWindow, true,{title:'An LF Window',closeButton:true,_width:400,_height:300,contentPath:'workspaceDialog'});
    }
}
