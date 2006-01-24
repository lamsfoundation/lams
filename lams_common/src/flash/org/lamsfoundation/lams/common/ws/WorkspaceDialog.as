import mx.controls.*
import mx.utils.*
import mx.managers.*
import mx.events.*
import org.lamsfoundation.lams.common.ws.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.common.ui.*
/**
* @author      DI & DC
*/
class WorkspaceDialog extends MovieClip{
 
    //private static var OK_OFFSET:Number = 50;
    //private static var CANCEL_OFFSET:Number = 50;

    //References to components + clips 
    private var _container:MovieClip;       //The container window that holds the dialog
    private var ok_btn:Button;              //OK+Cancel buttons
    private var cancel_btn:Button;
    private var panel:MovieClip;            //The underlaying panel base
    
	private var location_btn:Button;
    private var properties_btn:Button;
	
	//location tab elements
	private var treeview:Tree;              //Treeview for navigation through workspace folder structure
	private var input_txt:TextInput;
	private var currentPath_lbl:Label;
	private var name_lbl:Label;
	private var resourceTitle_txi:TextInput;
	
	//properties
	private var description_lbl:Label;
	private var license_lbl:Label;
    private var resourceDesc_txa:TextArea;
    private var license_txa:TextArea;
    private var licenseID_cmb:ComboBox;
	
	
	
    private var fm:FocusManager;            //Reference to focus manager
    private var themeManager:ThemeManager;  //Theme manager
	
	private var _workspaceView:WorkspaceView;
	private var _workspaceModel:WorkspaceModel;

    
    //Dimensions for resizing
    private var xOkOffset:Number;
    private var yOkOffset:Number;
    private var xCancelOffset:Number;
    private var yCancelOffset:Number;
    
	private var _resultDTO:Object;			//This is an object to contain whatever the user has selected / set - will be passed back to the calling function
	

    private var _selectedDesignId:Number;
    
    //These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
    
    
    /**
    * constructor
    */
    function WorkspaceDialog(){
        //trace('WorkSpaceDialog.constructor');
        //Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);
        _resultDTO = new Object();
        //Create a clip that will wait a frame before dispatching init to give components time to setup
        this.onEnterFrame = init;
    }

    /**
    * Called a frame after movie attached to allow components to initialise
    */
	private function init(){
        //Delete the enterframe dispatcher
        delete this.onEnterFrame;
		//TODO: DC apply the themes here
        
        //set the reference to the StyleManager
        themeManager = ThemeManager.getInstance();
        
        //Set the container reference
        Debugger.log('container=' + _container,Debugger.GEN,'init','org.lamsfoundation.lams.WorkspaceDialog');

        //Set the text on the labels
        
        
        //Set the text for buttons
		location_btn.label = Dictionary.getValue('ws_dlg_location_button');
		properties_btn.label = Dictionary.getValue('ws_dlg_properties_button');
        ok_btn.label = Dictionary.getValue('ws_dlg_ok_button');
        cancel_btn.label = Dictionary.getValue('ws_dlg_cancel_button');
		

        //get focus manager + set focus to OK button, focus manager is available to all components through getFocusManager
        fm = _container.getFocusManager();
        fm.enabled = true;
        ok_btn.setFocus();
        //fm.defaultPushButton = ok_btn;
        
        Debugger.log('ok_btn.tabIndex: '+ok_btn.tabIndex,Debugger.GEN,'init','org.lamsfoundation.lams.WorkspaceDialog');
        
        //Add event listeners for ok, cancel and close buttons
        ok_btn.addEventListener('click',Delegate.create(this, ok));
        cancel_btn.addEventListener('click',Delegate.create(this, cancel));
		location_btn.addEventListener('click',Delegate.create(this, showLocationTab));
		properties_btn.addEventListener('click',Delegate.create(this, showPropertiesTab));
        //Tie parent click event (generated on clicking close button) to this instance
        _container.addEventListener('click',this);
        //Register for LFWindow size events
        _container.addEventListener('size',this);
		
		
        
        //Debugger.log('setting offsets',Debugger.GEN,'init','org.lamsfoundation.lams.common.ws.WorkspaceDialog');

        //work out offsets from bottom RHS of panel
        xOkOffset = panel._width - ok_btn._x;
        yOkOffset = panel._height - ok_btn._y;
        xCancelOffset = panel._width - cancel_btn._x;
        yCancelOffset = panel._height - cancel_btn._y;
        
        //Register as listener with StyleManager and set Styles
        themeManager.addEventListener('themeChanged',this);
		//TODO: Make setStyles more efficient
		//setStyles();
        
        //Fire contentLoaded event, this is required by all dialogs so that creator of LFWindow can know content loaded
        _container.contentLoaded();
    }
	
	/**
	 * Called by the worspaceView after the content has loaded
	 * @usage   
	 * @return  
	 */
	public function setUpContent():Void{
		
		
		//register to recive updates form the model
		WorkspaceModel(_workspaceView.getModel()).addEventListener('viewUpdate',this);
		
		//Set up the treeview
        setUpTreeview();
		
	}
	
	/**
	 * Recieved update events from the WorkspaceModel. Dispatches to relevent handler depending on update.Type
	 * @usage   
	 * @param   event
	 */
	public function viewUpdate(event:Object):Void{
		Debugger.log('Recived an Event dispather UPDATE!, updateType:'+event.updateType+', target'+event.target,4,'viewUpdate','org.lamsfoundation.lams.ws.WorkspaceDialog');
		 //Update view from info object
        //Debugger.log('Recived an UPDATE!, updateType:'+infoObj.updateType,4,'update','CanvasView');
       var wm:WorkspaceModel = event.target;
	   //set a permenent ref to the model for ease (sorry mvc guru)
	   _workspaceModel = wm;
	  
	   switch (event.updateType){

			case 'REFRESH_TREE' :
                refreshTree(event.data,wm);
                break;
			case 'ITEM_SELECTED' :
				itemSelected(event.data,wm);
				break;
			case 'SHOW_TAB' :
				showTab(event.data,wm);
				break;
            default :
                Debugger.log('unknown update type :' + event.updateType,Debugger.GEN,'viewUpdate','org.lamsfoundation.lams.ws.WorkspaceDialog');
		}

	}
	
	private function refreshTree(changedNode:XMLNode,wm:WorkspaceModel){
		 Debugger.log('Refreshing tree....:' ,Debugger.GEN,'refreshTree','org.lamsfoundation.lams.ws.WorkspaceDialog');
		 //we have to set the new nodes to be branches, if they are branches
		if(changedNode.attributes.isBranch){
			treeview.setIsBranch(changedNode,true);
			//do its kids
			for(var i=0; i<changedNode.childNodes.length; i++){
				var cNode:XMLNode = changedNode.getTreeNodeAt(i);
				if(cNode.attributes.isBranch){
					treeview.setIsBranch(cNode,true);
				}
			}
		}
		 
		 treeview.refresh();
	}
	
	private function itemSelected(newSelectedNode:XMLNode,wm:WorkspaceModel){
		//update the UI with the new info:
		//_global.breakpoint();
		//Only update the details if the node if its a resource:a
		var nodeData = newSelectedNode.attributes.data;
		if(nodeData.resourceType == "Folder"){
			resourceTitle_txi.text = nodeData.name;
			resourceDesc_txa.text = nodeData.description;
			
			//TODO These Items must also be in the FolderContentsDTO
			/*
			license_txa.text = ;
			licenseID_cmb.value = ;
			*/
			
		}
		
	}
	
	private function setLocationContentVisible(v:Boolean){
		Debugger.log('v:'+v,Debugger.GEN,'setLocationContentVisible','org.lamsfoundation.lams.ws.WorkspaceDialog');
		treeview.visible = v;
		input_txt.visible = v;
		currentPath_lbl.visible = v;
		name_lbl.visible = v;
		resourceTitle_txi.visible = v;
		
	
	}
	
	private function setPropertiesContentVisible(v:Boolean){
		Debugger.log('v:'+v,Debugger.GEN,'setPropertiesContentVisible','org.lamsfoundation.lams.ws.WorkspaceDialog');
		description_lbl.visible = v;
		license_lbl.visible = v;
		resourceDesc_txa.visible = v;
		license_txa.visible = v;
		
		
	}
	
		
	/**
	 * updates the view to show the right controls for the tab
	 * @usage   
	 * @param   tabToSelect 
	 * @param   wm          
	 * @return  
	 */
	private function showTab(tabToSelect:String,wm:WorkspaceModel){
		Debugger.log('tabToSelect:'+tabToSelect,Debugger.GEN,'showTab','org.lamsfoundation.lams.ws.WorkspaceDialog');
		if(tabToSelect == "LOCATION"){
			setLocationContentVisible(true);
			setPropertiesContentVisible(false);
			//set the right label on the 'doit' button
			if(wm.currentMode=="OPEN"){
				ok_btn.label = Dictionary.getValue('ws_dlg_open_btn');
			}else if(wm.currentMode=="SAVE" || wm.currentMode=="SAVEAS"){
				ok_btn.label = Dictionary.getValue('ws_dlg_save_btn');
			}else{
				Debugger.log('Dont know what mode the Workspace is in!',Debugger.CRITICAL,'showTab','org.lamsfoundation.lams.ws.WorkspaceDialog');
				ok_btn.label = Dictionary.getValue('ws_dlg_ok_btn');
			}
				
				
		}else if(tabToSelect == "PROPERTIES"){
			setLocationContentVisible(false);
			setPropertiesContentVisible(true);
			
			ok_btn.label = Dictionary.getValue('ws_dlg_ok_btn');
		}
	}
	
	
    
    /**
    * Event fired by StyleManager class to notify listeners that Theme has changed
    * it is up to listeners to then query Style Manager for relevant style info
    */
    public function themeChanged(event:Object){
        if(event.type=='themeChanged') {
            //Theme has changed so update objects to reflect new styles
            setStyles();
        }else {
            Debugger.log('themeChanged event broadcast with an object.type not equal to "themeChanged"',Debugger.CRITICAL,'themeChanged','org.lamsfoundation.lams.WorkspaceDialog');
        }
    }
    
    /**
    * Called on initialisation and themeChanged event handler
    */
    private function setStyles(){
        //LFWindow, goes first to prevent being overwritten with inherited styles.
        var styleObj = themeManager.getStyleObject('LFWindow');
        _container.setStyle('styleName',styleObj);

        //Get the button style from the style manager
        styleObj = themeManager.getStyleObject('button');
        
        //apply to both buttons
        Debugger.log('styleObject : ' + styleObj,Debugger.GEN,'setStyles','org.lamsfoundation.lams.WorkspaceDialog');
        ok_btn.setStyle('styleName',styleObj);
        cancel_btn.setStyle('styleName',styleObj);
        
        //Get label style and apply to label
        styleObj = themeManager.getStyleObject('label');
        //myLabel_lbl.setStyle('styleName',styleObj);

        //Apply treeview style 
        styleObj = themeManager.getStyleObject('treeview');
        treeview.setStyle('styleName',styleObj);

        //Apply datagrid style 
        styleObj = themeManager.getStyleObject('datagrid');
        //datagrid.setStyle('styleName',styleObj);

/*
        //Apply combo style 
        styleObj = themeManager.getStyleObject('combo');
        combo.setStyle('styleName',styleObj);
  */
  }

    /**
    * Called by the cancel button 
    */
    private function cancel(){
        trace('Cancel');
        //close parent window
        _container.deletePopUp();
    }
    
    /**
    * Called by the OK button
	* Dispatches the okClicked event and passes a result DTO containing:
	* <code>
	*	_resultDTO.selectedResourceID 	//The ID of the resource that was selected when the dialogue closed
	*	_resultDTO.resourceName 		//The contents of the Name text field
	*	_resultDTO.resourceDescription 	//The contents of the description field on the propertirs tab
	*	_resultDTO.resourceLicenseText 	//The contents of the license text field
	*	_resultDTO.resourceLicenseID 	//The ID of the selected license from the drop down.
    *</code>
	*/
    private function ok(){
        trace('OK');
		_global.breakpoint();
		
		//TODO: Rmeove this code as its been here only for deflopment
		//set the selectedDesignId
		/**/
		if(StringUtils.isNull(input_txt.text)){
			//get the selected value off the tree
			var snode = treeview.selectedNode;
			input_txt.text = snode.attributes.data.resourceID;
			
		}
		_selectedDesignId = Number(input_txt.text);
		
		
		//TODO: Validate you are allowed to use the name etc... Are you overwriting - NOTE Same names are nto allowed in this version
		
		var snode = treeview.selectedNode;
		 Debugger.log('_workspaceModel.currentMode: ' + _workspaceModel.currentMode,Debugger.GEN,'setStyles','org.lamsfoundation.lams.WorkspaceDialog');
		if(_workspaceModel.currentMode=="SAVE" || _workspaceModel.currentMode=="SAVEAS"){
			//var rid:Number = Number(snode.attributes.data.resourceID);
			if(snode.attributes.data.resourceType=="LearningDesign"){
				//run a confirm dialogue as user is about to overwrite a design!
				LFMessage.showMessageConfirm("LOOKOUT ABOUT TO OVERWRITE A RESOURCE!", Proxy.create(this,doWorkspaceDispatch,true), Proxy.create(this,closeThisDialogue));
	
			}else if (snode.attributes.data.resourceType=="Folder"){
				doWorkspaceDispatch(false);
			}else{
				LFMessage.showMessageAlert("__Please click on either a Folder to save in, or a Design to overwrite__",null);
			}
		}else{
			doWorkspaceDispatch(true);
		}
		
    }
	
	
	
	public function doWorkspaceDispatch(useResourceID:Boolean){
		//ObjectUtils.printObject();
		var snode = treeview.selectedNode;
		
		if(useResourceID){
			_resultDTO.selectedResourceID = Number(snode.attributes.data.resourceID);
			
		}else{
			_resultDTO.selectedResourceID  = null;
			
		}
		_resultDTO.targetWorkspaceFolderID = Number(snode.attributes.data.resourceID);
		_resultDTO.resourceName = resourceTitle_txi.text;
		_resultDTO.resourceDescription = resourceDesc_txa.text;
		_resultDTO.resourceLicenseText = license_txa.text;
		_resultDTO.resourceLicenseID = licenseID_cmb.value;
		

        dispatchEvent({type:'okClicked',target:this});
	   
        closeThisDialogue();
		
	}
	
	public function closeThisDialogue(){
		 _container.deletePopUp();
	}
	
	
	//TODO: maan must be able to just send a single event type and detect the name od the button
	
	private function showLocationTab(){
		
		//send to wsp controller
		dispatchEvent({type:'locationTabClick',target:this});
	}
	
	private function showPropertiesTab(){
		
		dispatchEvent({type:'propertiesTabClick',target:this});
	}
    
    /**
    * Event dispatched by parent container when close button clicked
    */
    private function click(e:Object){
        trace('WorkspaceDialog.click');
        e.target.deletePopUp();
    }
    
	/**
	 * Recursive function to set any folder with children to be a branch
	 * TODO: Might / will have to change this behaviour once designs are being returned into the mix
	 * @usage   
	 * @param   node 
	 * @return  
	 */
    private function setBranches(node:XMLNode){
		if(node.hasChildNodes() || node.attributes.isBranch){
			treeview.setIsBranch(node, true);
			for (var i = 0; i<node.childNodes.length; i++) {
				var cNode = node.getTreeNodeAt(i);
				setBranches(cNode);
				/*
				if(cNode.hasChildNodes()){
					treeview.setIsBranch(cNode, true);
					setBranches(cNode);
				}
				*/
				
			}
		}
	}
	

	
	
	/**
	 * Sets up the treeview with whatever datya is in the treeDP
	 * TODO - extend this to make it recurse all the way down the tree
	 * @usage   
	 * @return  
	 */
	private function setUpTreeview(){
			
		//Debugger.log('_workspaceView:'+_workspaceView,Debugger.GEN,'setUpTreeview','org.lamsfoundation.lams.common.ws.WorkspaceDialog');
		treeview.dataProvider = WorkspaceModel(_workspaceView.getModel()).treeDP;
		
		Debugger.log('WorkspaceModel(_workspaceView.getModel()).treeDP:'+WorkspaceModel(_workspaceView.getModel()).treeDP.toString(),Debugger.GEN,'setUpTreeview','org.lamsfoundation.lams.common.ws.WorkspaceDialog');
		
		//get the 1st child
		var fNode = treeview.dataProvider.firstChild;
		
		
		
			/*
	* 
	
		//loop thorigh th childresn to see if they are branches
		for (var i = 0; i<fNode.childNodes.length; i++) {
			var node:XMLNode = fNode.getTreeNodeAt(i);
			// Set each of the 3 initial child nodes to be branches
			if(node.attributes.isBranch){
				treeview.setIsBranch(node,true);
				//also check this branches children to see if they have isBranch set
				if(node.hasChildNodes()){
					for(var j=0; j<node.childNodes.length; j++){
						var cNode:XMLNode = node.getTreeNodeAt(j);
						if(cNode.attributes.isBranch){
							treeview.setIsBranch(cNode,true);
							
						}
						
					}
					
					
				}
				
				
				
			}
			
			
		}
	*/
		
		setBranches(fNode);
		
		
		
		Debugger.log('_workspaceView:'+_workspaceView,Debugger.GEN,'setUpTreeview','org.lamsfoundation.lams.common.ws.WorkspaceDialog');
		var wsc:WorkspaceController = _workspaceView.getController();
		Debugger.log('wsc:'+wsc,Debugger.GEN,'setUpTreeview','org.lamsfoundation.lams.common.ws.WorkspaceDialog');
		Debugger.log('wsc.onTreeNodeOpen:'+wsc.onTreeNodeOpen,Debugger.GEN,'setUpTreeview','org.lamsfoundation.lams.common.ws.WorkspaceDialog');
		
		
		treeview.addEventListener("nodeOpen", Delegate.create(wsc, wsc.onTreeNodeOpen));
		treeview.addEventListener("nodeClose", Delegate.create(wsc, wsc.onTreeNodeClose));
		treeview.addEventListener("change", Delegate.create(wsc, wsc.onTreeNodeChange));
		
		treeview.refresh();
		
    }
    
    /**
    * XML onLoad handler for treeview data
 */
    private function tvXMLLoaded (ok:Boolean,rootXML:XML){
        if(ok){
            /*
			//Set the XML as the data provider for the tree
            treeview.dataProvider = rootXML.firstChild;
            treeview.addEventListener("change", Delegate.create(this, onTvChange));
            
            //Add this function to prevent displaying [type function],[type function] when label attribute missing from XML
            treeview.labelFunction = function(node) {
                    return node.nodeType == 1 ? node.nodeName : node.nodeValue;
            };
            */
        }
    }
    
     
    /**
    * Main resize method, called by scrollpane container/parent
    */
    public function setSize(w:Number,h:Number){
        //Debugger.log('setSize',Debugger.GEN,'setSize','org.lamsfoundation.lams.common.ws.WorkspaceDialog');
        //Size the panel
        panel.setSize(w,h);

        //Buttons
        ok_btn.move(w-xOkOffset,h-yOkOffset);
        cancel_btn.move(w-xCancelOffset,h-yCancelOffset);
    }
    
    //Gets+Sets
    /**
    * set the container refernce to the window holding the dialog
    */
    function set container(value:MovieClip){
        _container = value;
    }
	
	/**
	 * 
	 * @usage   
	 * @param   newworkspaceView 
	 * @return  
	 */
	public function set workspaceView (newworkspaceView:WorkspaceView):Void {
		_workspaceView = newworkspaceView;
	}
	
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get workspaceView ():WorkspaceView {
		return _workspaceView;
	}
	
    
    function get selectedDesignId():Number { 
        return _selectedDesignId;
    }
	
	
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get resultDTO():Object {
		return _resultDTO;
	}
}