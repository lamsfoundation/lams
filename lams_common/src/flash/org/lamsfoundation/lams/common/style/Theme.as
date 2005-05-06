import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.common.util.*
import mx.styles.*
/**
* A theme represents a collections of styles for visual elements
* 
* @class    Theme
* @author   DI
*/
class Theme {

	//Declarations
	private var _className = 'Theme';
	private var _baseStyleObject:CSSStyleDeclaration;
    private var visualElements:Hashtable;
    private var _name:String;

	/**
    * Constructor
    * @param baseStyleObject     Base style for the theme, holds attributes such as themeColor, backgroundColor etc
    * @param name                Name of the theme
    */
	function Theme(name:String,baseStyleObject:CSSStyleDeclaration) {
        _baseStyleObject = baseStyleObject;
        _name = name;
        //Create the visual elements hashtable
        visualElements = new Hashtable('visualElements');
	}
    
    /**
    * Adds and element to the visual elements hash
    */
    public function addVisualElement(element:VisualElement){
        visualElements.put(element.name,element);
    }
    
    /**
    * Returns reqeusted visual element
    */
    public function getVisualElement(name:String):VisualElement{
        return visualElements.get(name);
    }

	//Getters+Setters
	function get name():String{
		return _name;
	}    
    
	function get className():String{
		return _className;
	}
}