import org.lamsfoundation.lams.authoring.tb.*;
import org.lamsfoundation.lams.common.util.*;


/*
* Model for the Toolbar
*/
class ToolbarModel extends Observable {
    
	private var __width:Number;
	private var __height:Number;
	private var __x:Number;
	private var __y:Number;
	private var _isDirty:Boolean;
	private var infoObj:Object;
	private var _btnState:Boolean = false;
    
	/**
	* Constructor.
	*/
	public function ToolbarModel (){
		
	}
    
    /**
    * set the size on the model, this in turn will set a changed flag and notify observers (views)
    * @param width - Tookit width
    * @param height - Toolkit height
    */
    public function setSize(width:Number,height:Number) {
		__width = width;
		__height = height;
		
		setChanged();
		//send an update
		infoObj = {};
		infoObj.updateType = "SIZE";
		notifyObservers(infoObj);
    }
    
	/**
	* set the button state to enabled/disabled and set a changed flag and notify view and controller.
    */
	
	public function setDefaultState(){
		Debugger.log('setDefaultState is called: ',Debugger.GEN,'setDefaultState','Toolbar');
		setButtonState("preview", false)
	}

	/**
	 * 
	 * @usage   
	 * @param   btnName  
	 * @param   btnState 
	 * @return  
	 */
	public function setButtonState(btnName:Object, btnState:Boolean){
		Debugger.log('button name in setButtonState is : '+btnName, Debugger.GEN,'setButtonState','ToolbarModel');		
		
		setChanged();
		infoObj = {};
		infoObj.updateType = "BUTTON";
		infoObj.button = btnName
		infoObj.buttonstate = btnState
		notifyObservers(infoObj);
		
	}
	
	/**
	* Used by View to get the size
	* @returns Object containing width(w) & height(h).  obj.w & obj.h
	*/
	public function getSize():Object{
		var s:Object = {};
		s.w = __width;
		s.h = __height;
		return s;
	}  
	
	/**
	* Used by View to get the button state enabled/disabled
	* @returns Object containing true or false .  obj.newbtnstate
	*/
	
	public function getState():Object{
		var s:Object = {};
		s.newbtnstate = _btnState;
		return s;
	}  
    
	/**
    * sets the model x + y vars
	*/
	public function setPosition(x:Number,y:Number):Void{
        //Set state variables
		__x = x;
		__y = y;
        //Set flag for notify observers
		setChanged();
        
		//build and send update object
		infoObj = {};
		infoObj.updateType = "POSITION";
		notifyObservers(infoObj);
	}  

	/**
	* Used by View to get the size
	* @returns Object containing width(w) & height(h).  obj.w & obj.h
	*/
	public function getPosition():Object{
		var p:Object = {};
		p.x = x;
		p.y = y;
		return p;
	}  
    
    //Acessors for x + y coordinates
    public function get x():Number{
        return __x;
    }
    
    public function get y():Number{
        return __y;
    }

    //Acessors for x + y coordinates
    public function get width():Number{
        return __width;
    }
    
    public function get height():Number{
        return __height;
    }    
    
}
