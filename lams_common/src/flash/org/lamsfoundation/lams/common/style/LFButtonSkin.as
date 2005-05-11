import mx.skins.RectBorder;
import mx.core.ext.UIObjectExtensions;
import org.lamsfoundation.lams.common.style.*


/**
* LFButtonSkin allows buttons to be customised further than setStyle allows.  Essentially it overrides the skin behaviour of
* the button component and draws the various button states.  
* 
* @class    LFButtonSkin
* @author   DI
* @usage
*       Add A movieclip to FLA library with name and library linkage identifier 'ButtonSkin'. In the AS2 class field point
*       to this class.  Create (if needed) style and add/load into ThemeManager class.
*       NOTE: the code below works by setting the value 'c' for each button state.  This value is retrieved from the ThemeManager
* 
*/
class LFButtonSkin extends RectBorder{
    
    static var symbolName:String = "LFButtonSkin";
    static var symbolOwner:Object = LFButtonSkin;
    private var styleObj:mx.styles.CSSStyleDeclaration;
    private var themeManager:ThemeManager;
  
    function size():Void {
    var c:Number; // color
    var borderStyle:String = getStyle("borderStyle");
    themeManager = ThemeManager.getInstance();
    styleObj = themeManager.getStyleObject('LFButton');    
    
    //Select from the possible button states
    switch (borderStyle) {
    case "falseup":
        c = styleObj.getStyle('up');
        break;
    case "falserollover":
        c = styleObj.getStyle('over');
        break;
    case "falsedisabled":
        c = styleObj.getStyle('disabled');
        break;
    case "falsedown":0
        c = styleObj.getStyle('down');
        break;
    case "trueup":
    case "truedown":
    case "truerollover":
    case "truedisabled":
        break;
    }
    
    clear();
    //Line thickness
    var thickness = _parent.emphasized ? 2 : 0;
    lineStyle(thickness, 0, 100);
    //Fill with color and draw rectangle
    beginFill(c, 100);
    drawRect(0, 0, __width, __height);
    endFill();
    }
    
    // required for skins
    static function classConstruct():Boolean {
        UIObjectExtensions.Extensions();
        _global.skinRegistry["ButtonSkin"] = true;
        return true;
    }
    static var classConstructed:Boolean = classConstruct();
    static var UIObjectExtensionsDependency = UIObjectExtensions;
}