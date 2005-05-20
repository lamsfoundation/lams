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
    
    /**
    * Convert to data object for saving
    */
    public function toData():Object{
        var obj:Object = {};
        obj.id = _id;
        obj.name = _name;
        obj.value = _value;
        return obj;
    }
    
    /**
    * Create Dictionary item from dataObject
    */
    public static function createFromData(dataobj:Object):DictionaryItem{
        var dictionaryItem:DictionaryItem =  new DictionaryItem(dataobj.id,dataobj.name,dataobj.value)
        return dictionaryItem;
    }
    
    function get name():String{
        return _name;
    }
    
    function get value():String{
        return _value;
    }

}