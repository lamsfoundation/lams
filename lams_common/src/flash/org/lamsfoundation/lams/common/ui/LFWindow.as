import mx.containers.*
import mx.managers.*
import mx.utils.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.ui.*

/**
* LFWindow - Extends the MM Window class and will be used for all LAMS windows + dialogs
* @author   DI
*  
* TODO DI-16/05/05  Configure resize/scroll policy logic to stop scrollbars appearing spuriously on resize 
*/
class LFWindow extends Window{

    //Declarations
    //Static vars
    public static var symbolOwner:Object = Window;
    //public static var symbolName:String = 'LFWindow';
	
    private static var MIN_WIDTH = 160;                      //Minimum window dimensions
    private static var MIN_HEIGHT = 120;
	
    private static var RESIZE_WIDTH_OFFSET = 10;            //Offset to place resize clip from bottom rhs of window
    private static var RESIZE_HEIGHT_OFFSET = 10;
    
    private static var MARGIN_WIDTH:Number = 9;             //Differences between scroll content + window dimensions
    private static var MARGIN_HEIGHT:Number = 38;
    
    //Public vars
	public var className:String = 'LFWindow';
    
    //Private vars
    private var resize_mc:MovieClip;					//Clip clicked on for resize 
    private var _scrollContentPath:String;				//Main content of the LFWindow within a scrollpane
    private var _helpButtonHandler:Function;            //Called when help button clicked
    private var help_btn:Button;                        //Help button reference
    
    private var contentOffsetWidth:Number;
    private var contentOffsetHeight:Number;
    
    public var centred:Boolean=false;
    private var setUpFinished:Boolean = false;
    
    private var _initObj:Object;
    
    //Constructor
    function LFWindow() {
    }
    
	public function init(Void):Void {
        //trace('init');
 	    super.init();
		//LFWindow contains a scroll pane which contains the content.
        contentPath = 'ScrollPane';

        //set up skin
        skinCloseOver  = 'LFCloseButtonOver';
        skinCloseDown = 'LFCloseButtonDown';
		
		//Add event listener for complete event, fired when scrollpane is loaded
		this.addEventListener('complete',Delegate.create(this,scrollLoaded));
		this._visible=false;
	}
	
	/**
	* Fired by Window when content has loaded  i.e. ScrollPane
	*/
	public function scrollLoaded(){
        Debugger.log('--',Debugger.GEN,'scrollLoaded','org.lamsfoundation.lams.LFWindow');

		//Assign scroll pane content
		content.contentPath = _scrollContentPath;

		//Assign reference to container (this) acessible in dialog
		content.content.container = this;
        
        //Set the size of the window to be greater than the content by the margin width and height
        setSize(content.content._width+MARGIN_WIDTH,content.content._height+MARGIN_HEIGHT);
		
        centre();
		this._visible = true;
        setUpFinished = true;
		this.removeEventListener('complete',scrollLoaded);
	}
    
    public function createChildren(Void):Void {
        //trace('createChildren');
        super.createChildren();
        
        //TODO DI-13/05/05 add the code to handle dynamic button addition
        //Add extra buttons as required
        
        //Attach resize and set up resize handling 
        resize_mc = this.createChildAtDepth('resize',DepthManager.kTop);
        resize_mc.resize_btn.useHandCursor = false;

        //Resize_mc contains resize_btn so enterFrame references within button are to _parent
        resize_mc.resize_btn.onPress = function(){
            _parent.onEnterFrame=function(){
                //Set the window size to shadow mouse movement
                var pt:Object = {x:_parent._xmouse,y:_parent._ymouse};
                //Validate the result & change the size if ok
                if(pt.x>MIN_WIDTH && pt.y>MIN_HEIGHT) {
                    _parent.setSize(pt.x,pt.y);
                } else if(pt.x>MIN_WIDTH){
                    _parent.setSize(pt.x,MIN_HEIGHT);
                } else if(pt.y>MIN_HEIGHT){
                    _parent.setSize(MIN_WIDTH,pt.y);
                }
            }
            this.startDrag();
        }
        //On release / releaseOutside stop the drag and kill onEnterFrame handler
        resize_mc.resize_btn.onRelease = resize_mc.resize_btn.onReleaseOutside = function (){
            this.stopDrag();
            delete _parent.onEnterFrame;
        }
    }
    
	public function draw(Void):Void {
        //trace('draw');
        //Call the super methods and size after a draw.
        super.draw();
        size();
    }   
	
    /**
	* called when object is sized, e.g. draw, setSize etc.
	*/
    public function size(Void):Void {
        //trace('size')
        super.size();
        
        //If the content is too small then put in scroll bars
		if(content.content._width > width-MARGIN_WIDTH){
            content.hScrollPolicy = 'on';
        } else {
            content.hScrollPolicy = 'off';
        }
        
		if(content.content._height > height-MARGIN_HEIGHT){
            content.vScrollPolicy = 'on';
        } else {
            content.vScrollPolicy = 'off';
        }
        
		//content.setSize(width,height);
        if(setUpFinished){
		    content.content.setSize(width-MARGIN_WIDTH,height-MARGIN_HEIGHT);
        }
        
        //Align the resize button with the bottom right
        resize_mc._x = width-RESIZE_WIDTH_OFFSET;
        resize_mc._y = height-RESIZE_HEIGHT_OFFSET;
    }
    
	/**
	* Drag handler
	*/
    public function startDragging(Void):Void {
        super.startDragging();
    }
    
    /**
    * Centres the window on the stage
    */
    public function centre() {
        //trace('centre');
        //Calculate centre
        this._x = Stage.width/2 - this.width/2;
        this._y = Stage.height/2 - this.height/2;
    }
    
    /**
    * overrides UIObject.setStyle to provide custom style setting
    */
    public function setStyle(styleName:String,styleObj:Object){
        //trace('setstyle');
        //Pass it up the inheritance chain to set any inherited styles or non-custom/LAMS style properties
        super.setStyle(styleName,styleObj);
        //Pass on to the scrollpane as the theme color doesn't seem to inherit correctly from the Window parent
        content.content.setStyle(styleName,styleObj);
        
        //Does help button exist?
        if (_helpButtonHandler) {
           //If style attribute defined for help button use it. Otherwise use themeColor
           if(styleObj.helpButtonColor) {
                    
           }else {
               
           }
        }
    }
    
    /**
    * method called by content of scrollpane when it is loaded
    */
    public function contentLoaded() {
        //dispatch an onContentLoaded event to listeners
        dispatchEvent({type:'contentLoaded',target:this});
    }
    
	//Getters+Setters
    /**
    * Sets the content of the scrollpane child. Allows content to be set from outside LFWindow
    */
    [Inspectable(defaultValue='')]
    function set scrollContentPath (path:String){
        _scrollContentPath = path;
    }
    
    /**
    * Sets HelpButton handler function and creates a help button on the window for calling it
    */
    [Inspectable(defaultValue='')]
    function set helpButtonHandler (value:Function){
        if(value){
            _helpButtonHandler = value;
            //Create Help Button
        }
    }
    
    /**
    * override parent property becuase we don't want to be able to set LFWindow content path
    * because LWWindow 'content' is created in createChildren
    */
	/*
    function set contentPath(value:Object){
        
    }
    */
	
    /**
    * Returns content inside the scrollPane
    */
    function get scrollContent():Object{
        return content.content;
    }
}