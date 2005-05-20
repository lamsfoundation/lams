import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.comms.*

/**
* Dictionary - Holds all of the LAMS text allowing LAMS to be multilingual
* 
* @class Dictionary
* @author DI
*/
dynamic class Dictionary {

	private static var items:Hashtable;
    private static var loaded:Boolean = false;
    private static var usingServer:Boolean;
    private static var language:String;

	/**
	* @method load
	* @tooltip Load the dictionary for the language specified
	* @param language (string) language parameter.
	* @return 
	*/
	public static function load(language:String):Void {
        //TODO DI 19/05/05 Check cookie and if not present load from server
        //Cookie or server?
        /*
        if(CookieMonster.cookieExists('Dictionary')) {
            //Update flag 
            usingServer=false;
            openFromDisk();
        }else {
            //Update flag 
            usingServer=true;
            //openFromServer();
        }*/
            
            //Only load dictionary once
            if(!loaded) {
                //store the current language
                Dictionary.language = language;
                //Debugger.log('Dictionary-Load',Debugger.GEN,'load','org.lamsfoundation.lams.dict.Dictionary');
                items = new Hashtable();
                //TODO: DI 28/04/05 Load the information for the current language from the server for create items in here
                //TODO: DI 19/05/05 Implement logic to check locally for language then check server if not found
                items.put(0,new DictionaryItem(0,'ws_dialog_title','Workspace'));
                items.put(1,new DictionaryItem(1,'ws_dialog_ok_button','OK'));
                items.put(2,new DictionaryItem(2,'ws_dialog_cancel_button','Cancel'));
                items.put(4,new DictionaryItem(4,'prefs_dialog_title','Preferences'));
                items.put(5,new DictionaryItem(5,'prefs_dialog_ok','OK'));
                items.put(6,new DictionaryItem(6,'prefs_dialog_cancel','Cancel'));
                
                //Set loaded flag
                loaded = true;
            }
            
	}
    
    /**
    * return the text value for the corresponding ID
    */
    public static function getValue(id:Number):String{
        //Debugger.log('returning item-' +id + '-' + items.get(id).value,Debugger.GEN,'getItemById','org.lamsfoundation.lams.dict.Dictionary');
        return items.get(id).value;
    }
    
    /**
    * Converts the dictionary data into a data object ready for serializ
    */
    public static function toData():Object{
        var obj:Object ={};
        var hashKeys:Array = items.keys();
        var hashValues:Array = items.values();
        
        obj.dictionaryItems = [];
        
        //Go through hash of dictionary items and get data objects for each
        for(var i=0;i<hashKeys.length;i++){
            obj.dictionaryItems[i] = hashValues[i].toData();
            obj.dictionaryItems[i].hashKey = hashKeys[i];
        }
        
        return obj;
    }
    
    /**
    * Creates Dictionary info from data object
    */
    public static function createFromData(dataObj:Object){
        //First empty hash 
        items.clear();
        
        //Go through data object and copy values to items hash
        for(var i=0;i<dataObj.dictionaryItems.length;i++){
            //Create hashKey object and then prune hashkey from dataObject before adding dictionary item to hashtable 
            var hashKey = dataObj.dictionaryItems[i].hashKey;
            delete dataObj.dictionaryItems[i].hashKey;
            //Create the dictionary item from the data object
            var dictItem:DictionaryItem = DictionaryItem.createFromData(dataObj.dictionaryItems[i]);
            items.put(hashKey,dictItem);
        }
    }
    
    /**
    * Save the Dictionary to disk 
    * @return  
    */
    public static function saveToDisk():Void{
        var wddx:Wddx = new Wddx();
        //Convert to data object and then serialize before saving to a cookie
        var dataObj = toData();
        var sx:Object = wddx.serialize(dataObj);
        CookieMonster.save(sx,'Dictionary');
    }
    
    /**
    * Open the Dictionary from disk 
    * @usage   
    * @return  
    */
    public static function openFromDisk():Void{
        var wddx:Wddx = new Wddx();
        var sx:Object = CookieMonster.open('Dictionary');
        var dx:Object = wddx.deserialize(sx);
        createFromData(dx);
    }
    
    /**
    * Returns a list of the languages available for lams
    */
    public static function getLanguages():Object{
        //Create object to store lanaguages info
        var langInfo:Object = {};
        var dictionaryObject
        //Get from Cookie or server?
        if(usingServer) {
            //Server 
            //Communication.getRequest()
        }else {
            //Cookie
            CookieMonster.open();
        }
        return langInfo;
    }
    
    /**
    * Handler function for language list being returned from the server
    */
    public function onLanguageListReceived(languageListObj:Object){
        
    }
    
    /**
    * Handler function for dictionary being returned from the server
    */
    public function onDictionaryReceivedFromServer(dictionaryDataObj:Object){
        //Create the dictionary from the data obj
        Dictionary.createFromData(dictionaryDataObj);
    }

    /**
    * Opens Dictionary from server
    */
    public function openFromServer() {
        
    }
    

}
