import org.lamsfoundation.lams.common.*
/*
* Dialog Interface - This ensures that Theme, Resize + Close events are handled in Dialog classes
* @author   DI
* @usage    
*           import org.lamsfoundation.lams.common.*
*           class MyDialog implements Dialog{....
*/
interface Dialog {
    //Called by Parent LFWindow when it is resized
    public function setSize(w:Number,h:Number):Void;
    
    //Click handler for close button of LFWindow    
    public function click(e:Object):Void;

    //Handler for theme changes broadcast from Theme Manager
    public function themeChanged(event:Object):Void;
    
}
