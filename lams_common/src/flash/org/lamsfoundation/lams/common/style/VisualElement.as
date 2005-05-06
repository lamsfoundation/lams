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