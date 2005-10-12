import org.lamsfoundation.lams.common.util.Observable;
import org.lamsfoundation.lams.common.ws.*;
/*
* Model for the Canvas
*/
class org.lamsfoundation.lams.common.ws.WorkspaceModel extends Observable {
	
	
	private var _workspaceID:Number;
	private var _rootFolderID:Number;
	

	/*
	* Constructor.
	*/
	public function WorkspaceModel (){
	
	}	
	
	
	/**
	 * 
	 * @usage   
	 * @param   newworkspaceID 
	 * @return  
	 */
	public function set workspaceID (newworkspaceID:Number):Void {
		_workspaceID = newworkspaceID;
	}
	
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get workspaceID ():Number {
		return _workspaceID;
	}
	
	/**
	 * 
	 * @usage   
	 * @param   newrootFolderID 
	 * @return  
	 */
	public function set rootFolderID (newrootFolderID:Number):Void {
		_rootFolderID = newrootFolderID;
	}
	
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get rootFolderID ():Number {
		return _rootFolderID;
	}
	
	

}
