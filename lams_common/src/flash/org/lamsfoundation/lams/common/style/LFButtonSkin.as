import mx.skins.RectBorder;
import mx.core.ext.UIObjectExtensions;
import org.lamsfoundation.lams.common.style.*


/**
* LFButtonSkin allows buttons to be customised further than setStyle allows.  Essentially it overrides the skin behaviour of
* the button component and draws the various button states.  
* @class    LFButtonSkin
* @author   DI
* 
*/
class LFButtonSkin extends RectBorder
{
  static var symbolName:String = "LFButtonSkin";
  static var symbolOwner:Object = LFButtonSkin;
  private var styleObj:mx.styles.CSSStyleDeclaration;
  private var themeManager:ThemeManager;
  
  function size():Void
  {
    var c:Number; // color
    var borderStyle:String = getStyle("borderStyle");
    themeManager = ThemeManager.getInstance();
    styleObj = themeManager.getStyleObject('Button');    

    switch (borderStyle) {
      case "falseup":
      case "falserollover":
      case "falsedisabled":
        c = 0x7777FF;
        break;
      case "falsedown":0
        c = getStyle('lala',styleObj);
        trace('****getting color c: ' + c);
        break;
      case "trueup":
      case "truedown":
      case "truerollover":
      case "truedisabled":
        c = 0xFF7777;
        break;
    }

    clear();
    var thickness = _parent.emphasized ? 2 : 0;
    lineStyle(thickness, 0, 100);
    beginFill(c, 100);
    drawRect(0, 0, __width, __height);
    endFill();
  }
  
  // required for skins
  static function classConstruct():Boolean
  {
    UIObjectExtensions.Extensions();
    _global.skinRegistry["ButtonSkin"] = true;
    return true;
  }
  static var classConstructed:Boolean = classConstruct();
  static var UIObjectExtensionsDependency = UIObjectExtensions;
}