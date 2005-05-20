import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.common.util.*
import mx.styles.*

/**
* A visual element can be anything visual in LAMS that requires style information
* e.g. 'Button', 'LFWindow', 'Label' etc
* 
* @class    VisualElement
* @author   DI
*/
class VisualElement {

	//Declarations
    private var _className = 'VisualElement';
    private var _styleObject:CSSStyleDeclaration;
    private var _name:String;
    
	/**
    * Constructor
    * @param name               Name representing the relevant visual element
    * @param styleObject        Optional
    */
	function VisualElement(name:String,styleObject:CSSStyleDeclaration) {
        _name = name;
        _styleObject = styleObject;
	}
    
    
    /**
    * Returns an object containing the serializable (data) parts of this class
    */
    public function toData():Object{
        var obj:Object = {};
        //Style object is ok as it so attach it
        obj.styleObject = ThemeManager.styleObjectToData(_styleObject);
        obj.name = _name;
        return obj;
    }
    
    /**
    * Creates an instance of VisualElement from data that has been serialized
    */
    public static function createfromData(dataObj):VisualElement{
        //Create the style object and populate it from data
        var tmpSO = ThemeManager.dataToStyleObject(dataObj.styleObject);
        //Create new instance of this
        var obj:VisualElement = new VisualElement(dataObj.name,tmpSO);
        return obj;
    }

	//Getters+Setters
	function get styleObject():CSSStyleDeclaration{
		return _styleObject;
	}

	function get className():String{
		return _className;
	}

	function get name():String{
		return _name;
	}
}