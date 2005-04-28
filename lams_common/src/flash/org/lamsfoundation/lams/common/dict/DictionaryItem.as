import org.lamsfoundation.lams.common.dict.*

/**
* Holds the metadata + value of a LAMS text component
* @author DI
*/
class DictionaryItem {
    
	private var _id:Number;
	private var _name:String;
	private var _value:String;

	public function DictionaryItem(id:Number,name:String,value:String) {
		//constructor 
        _id = id;
        _name = name;
        _value = value;
	}
    
    function get name():String{
        return _name;
    }
    
    function get value():String{
        return _value;
    }

}