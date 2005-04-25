/**
* LFWindow - Extends the MM Window class and will be used for all LAMS windows + dialogs
* @author   DI
* 
*/
import mx.containers.*
import mx.managers.*

class org.lamsfoundation.lams.common.ui.LFWindow extends Window{

    //Declarations
    //Static vars
    public static var symbolOwner:Object = Window;
    private static var MIN_WIDTH = 80;                      //Minimum window dimensions
    private static var MIN_HEIGHT = 60;
    private static var RESIZE_WIDTH_OFFSET = 15;            //Offset to place resize clip from bottom rhs of window
    private static var RESIZE_HEIGHT_OFFSET = 15;
    
    
    //Public vars
	public var className:String = 'LFWindow';
    
    //Private vars
    private var resize_mc:MovieClip;
    private var scrollPane:MovieClip;
    
    //Constructor
    function LFWindow() {
    }
  
    public function createChildren(Void):Void {
        super.createChildren();
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
        
        //Add the scrollpane
        //scrollPane = createClassObject(mx.containers.ScrollPane,"border_mc", getNextHighestDepth()); 
        
    }
    
	public function draw(Void):Void {
        super.draw();
    }    
    
    public function size(Void):Void {
        super.size();
        //forward the resize to the content
        this.content.size(width,height);
        
        resize_mc._x = width-RESIZE_WIDTH_OFFSET;
        resize_mc._y = height-RESIZE_HEIGHT_OFFSET;
    }
    
	public function init(Void):Void {
		super.init();
        contentPath = 'ScrollPane';
        scrollContentPath = 'workspaceDialog';
        //FocusManager.setFocus(this);
		//visible = false;
	}
    
	public function doLayout(Void):Void {
		super.doLayout();
	}  
    
    public function startDragging(Void):Void {
        super.startDragging();
        //trace('drag started');
    }
    
	//Getters+Setters
    [Inspectable(defaultValue='')]
    function set scrollContentPath (path:String){
        trace('setting scrollContentPath-'+content);
        //Set the content path of the contained scrollpane
        content.contentPath = path;    
    }
}