import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.util.*

/**
* Dictionary - Holds all of the LAMS text allowing LAMS to be multilingual
* 
* @class Dictionary
* @author DI
*/
dynamic class Dictionary {

	private static var items:Hashtable;

	/**
	* @method load
	* @tooltip Load the dictionary for the language specified
	* @param language (string) language parameter.
	* @return 
	*/
	public static function load(language:String):Void {
        trace('Dictionary-Load!!!!');
        items = new Hashtable();
		//TODO: DI 28/04/05 Load the information for the current language from the server for create items in here
        items.put(0,new DictionaryItem(0,'ws_dialog_title','Workspace'));
        items.put(1,new DictionaryItem(1,'ws_dialog_ok_button','OK'));
        items.put(2,new DictionaryItem(2,'ws_dialog_cancel_button','Cancel'));
	}
    
	/**
	* @tooltip returns the dictionary item based on ID
	* @param id (Number) id parameter.
	*/
	public static function getItemById(id:Number):DictionaryItem {
        trace('Dictionary.getItemById');
        return items.get(id);
	}
    
    /**
    * return the text value for the corresponding ID
    */
    public static function getValue(id:Number):String{
        return items.get(id).value;
    }
}
