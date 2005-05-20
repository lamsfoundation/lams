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
    
    /**
    * Returns an object containing the serializable (data) parts of this class
    */
    public function toData():Object{
        //Create the empty object for holding data
        var obj:Object = {};
        
        //Create array of visual elements.
        obj.visualElements = [];
        
        //Save the name
        obj.name = _name;
        
        //Base StyleObject
        obj.baseStyleObject = ThemeManager.styleObjectToData(_baseStyleObject);
        
        //Populate from visual elements hash
        var hashValues:Array = visualElements.values();
        for (var i=0;i<hashValues.length;i++) {
            //Call toData method on each value to get the data only
            obj.visualElements[i] = hashValues[i].toData();
        }
        return obj;
    }
    
    /**
    * Creates an instance of Theme from a data object
    */
    public static function createFromData(dataObj):Theme {
        //Convert the base style object data object to a style object 
        var tmpStyleObject = ThemeManager.dataToStyleObject(dataObj.baseStyleObject);
        
        //Create new instance of this
        var obj:Theme = new Theme(dataObj.name,tmpStyleObject);
        //Add the visual styles
        for(var i=0;i<dataObj.visualElements.length;i++){
            var tmpVisualElement:VisualElement = VisualElement.createfromData(dataObj.visualElements[i]);
            obj.addVisualElement(tmpVisualElement);
        }
        return obj;
    }
    
	//Getters+Setters
	function get name():String{
		return _name;
	}    
    
	function get className():String{
		return _className;
	}
}