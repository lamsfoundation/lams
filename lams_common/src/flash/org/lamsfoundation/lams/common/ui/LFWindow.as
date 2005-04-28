/**
* LFWindow - Extends the MM Window class and will be used for all LAMS windows + dialogs
* @author   DI
* 
*/
import mx.containers.*
import mx.managers.*
import org.lamsfoundation.lams.util.*

class org.lamsfoundation.lams.common.ui.LFWindow extends Window{

    //Declarations
    //Static vars
    public static var symbolOwner:Object = Window;
    private static var MIN_WIDTH = 80;                      //Minimum window dimensions
    private static var MIN_HEIGHT = 60;
    private static var RESIZE_WIDTH_OFFSET = 15;            //Offset to place resize clip from bottom rhs of window
    private static var RESIZE_HEIGHT_OFFSET = 15;
    
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
    
    
    //Constructor
    function LFWindow() {
    }
    
	public function init(Void):Void {
 	    super.init();
        //trace('LFWindow.init()');
        //contentPath = 'ScrollPane';
	}
    
  
    public function createChildren(Void):Void {
        super.createChildren();

        //Add the scrollpane
        scrollPane = createClassObject(mx.containers.ScrollPane,"scrollPane", getNextHighestDepth(),{contentPath:_scrollContentPath,_x:SCROLL_X_OFFSET,_y:SCROLL_Y_OFFSET});

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
        //trace('LFWindow.draw');
        super.draw();
        size()
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

        //trace('scrollPane.content.size :' + scrollPane.content.size);
        //forward the resize to any registered listeners, i.e  the content
        //dispatchEvent({type:"size",target:this});
    }
    
    public function startDragging(Void):Void {
        super.startDragging();
    }

	//Getters+Setters
    [Inspectable(defaultValue='')]
    function set scrollContentPath (path:String){
        //trace('setting scrollContentPath-'+content);
        _scrollContentPath = path;
    }
    
    /**
    * override parent property becuase we don't want to be able to set LFWindow content path
    * because LWWindow 'content' is created in createChildren
    */
    function set contentPath(value:Object){
        
    }
    
    
}