import mx.containers.*
import mx.managers.*
import org.lamsfoundation.lams.util.*

/**
* LFWindow - Extends the MM Window class and will be used for all LAMS windows + dialogs
* @author   DI
* 
*/
class org.lamsfoundation.lams.common.ui.LFWindow extends Window{

    //Declarations
    //Static vars
    public static var symbolOwner:Object = Window;
    private static var MIN_WIDTH = 80;                      //Minimum window dimensions
    private static var MIN_HEIGHT = 60;
    private static var RESIZE_WIDTH_OFFSET = 10;            //Offset to place resize clip from bottom rhs of window
    private static var RESIZE_HEIGHT_OFFSET = 10;
    
    private static var SCROLL_X_OFFSET = 3;
    private static var SCROLL_Y_OFFSET = 30;
    private static var SCROLL_X_PADDING = 3;
    private static var SCROLL_Y_PADDING = 3;
    
    //Public vars
	public var className:String = 'LFWindow';
    
    //Private vars
    private var resize_mc:MovieClip;
    private var scrollPane:MovieClip;
    private var _scrollContentPath:String;
    private var _helpButtonHandler:Function;            //Called when help button clicked
    
    //Constructor
    function LFWindow() {
    }
    
	public function init(Void):Void {
 	    super.init();
        
        //set up skin
        skinCloseOver  = 'LFCloseButtonOver';
        skinCloseDown = 'LFCloseButtonDown';
	}
  
    public function createChildren(Void):Void {
        super.createChildren();

        //Add the scrollpane
        scrollPane = createClassObject(mx.containers.ScrollPane,"scrollPane", getNextHighestDepth(),{contentPath:_scrollContentPath,_x:SCROLL_X_OFFSET,_y:SCROLL_Y_OFFSET});
        //Pass in a reference to the Window (this) to the content of the scrollpane
        scrollPane.content.container=this;
        
        //Dynamically add extra buttons
        
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
        //Call the super methods and size after a draw.
        super.draw();
        size();
    }    
    
    public function size(Void):Void {
        //trace('LFWindow.size');
        super.size();
        //Size the scrollpane
        var w:Number = width-SCROLL_X_OFFSET-SCROLL_X_PADDING;
        var h:Number = height-SCROLL_Y_OFFSET-SCROLL_Y_PADDING
        scrollPane.setSize(w,h);
        
        //Align the resize button with the bottom right
        resize_mc._x = width-RESIZE_WIDTH_OFFSET;
        resize_mc._y = height-RESIZE_HEIGHT_OFFSET;
        
        //Resize the scrollpane content
        scrollPane.content.setSize(w,h);
    }
    
    public function startDragging(Void):Void {
        super.startDragging();
    }
    
    /**
    * overrides UIObject.setStyle to provide custom style setting
    */
    public function setStyle(styleName:String,styleObj:Object){
        trace('setstyle');
        //Pass it up the inheritance chain to set any inherited styles or non-custom/LAMS style properties
        super.setStyle(styleName,styleObj);
        //Pass on to the scrollpane as the theme color doesn't seem to inherit correctly from the Window parent
        scrollPane.setStyle(styleName,styleObj);
        //If the button style is to be set then set it
        if(typeof(styleObj)=='object'){
            if(styleName=='closeButton'){
            }
        }
    }
    
	//Getters+Setters
    /**
    * Sets the content of the scrollpane child. Allows content to be set from outside LFWindow
    */
    [Inspectable(defaultValue='')]
    function set scrollContentPath (path:String){
        //trace('setting scrollContentPath-'+content);
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
    function set contentPath(value:Object){
        
    }
}