/**
* LFWindow - Extends the MM Window class and will be used for all LAMS windows + dialogs
* 
*/
import mx.containers.*
import mx.managers.*

class org.lamsfoundation.common.ui.LFWindow extends Window{

    public static var symbolOwner:Object = Window;
    
    //Declarations
    //Constructor
    function LFWindow() {
    }
  
    public function createChildren(Void):Void {
        super.createChildren();
        //Dynamically add extra buttons
    }
    
	public function draw(Void):Void {
        super.draw();
    }    
    
    public function size(Void):Void {
        super.size();
    }
    
	public function init(Void):Void {
		super.init();
        //FocusManager.setFocus(this);
		//visible = false;
	}
    
	public function doLayout(Void):Void {
		super.doLayout();
	}  
    
    public function startDragging(Void):Void {
        super.startDragging();
        trace('drag started');
    }
    
	//Getters+Setters
}