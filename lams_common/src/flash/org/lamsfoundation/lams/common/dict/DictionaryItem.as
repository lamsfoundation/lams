import org.lamsfoundation.lams.common.dict.*

/**
* Holds the metadata + value of a LAMS text component
* @author DI
*/
class DictionaryItem {
    
	private var _key:String;
	private var _description:String;
	private var _value:String;

	public function DictionaryItem(key:String, value:String,description:String) {
		//constructor 
        _key = key;
        _description = description;
        _value = value;
	}
    
    /**
    * Convert to data object for saving
    */
    public function toData():Object{
        var obj:Object = {};
        obj.key = _key;
        obj.description = _description;
        obj.value = _value;
        return obj;
    }
    
    /**
    * Create Dictionary item from dataObject
    */
    public static function createFromData(dataobj:Object):DictionaryItem{
        var dictionaryItem:DictionaryItem =  new DictionaryItem(dataobj.key,dataobj.value,dataobj.description);
        return dictionaryItem;
    }
    
    function get key():String{
        return _key;
    }
    
    function get value():String{
        return _value;
    }
	
	function get description():String{
        return _description;
    }

}