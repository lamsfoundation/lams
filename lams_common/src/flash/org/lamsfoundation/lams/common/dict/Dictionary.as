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

	private var items:Hashtable;
    private var _currentLanguage:String;
    
    //Application instance is stored as a static in the application class
    private static var _instance:Dictionary = null;     

    /**
    * Constructor - Dictionary is singleton so private 
    */
    private function Dictionary() {
        items = new Hashtable();
    }

    /**
    * Retrieves an instance of the Application singleton
    */ 
    public static function getInstance():Dictionary{
        if(Dictionary._instance == null){
            Dictionary._instance = new Dictionary();
        }
        return Dictionary._instance;
    }


	/**
	* Load the dictionary for the language specified
	* @param language (string) language parameter.
	*/
	public function load(language:String):Void {
        //TODO DI 19/05/05 Check cookie and if not present load from server
        //Set Language
        this._currentLanguage = language;
        //Cookie or server?
        //if(CookieMonster.cookieExists('dictionary.'+language)) {
            //openFromDisk(language);
        //}else {
            openFromServer(language);
        //}        
	}
    
 
    /**
    * @returns a string holding the text requested
    */
    public static function getValue(id:Number):String{
        //Debugger.log('returning item-' +id + '-' + items.get(id).value,Debugger.GEN,'getItemById','org.lamsfoundation.lams.dict.Dictionary');
        return _instance.items.get(id).value;
    }
    
    /**
    * Converts the dictionary data into a data object ready for serializing
    * @returns Object containing the dictionary converted to a dataObject 
    */
    public function toData():Object{
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
    * @param dataObj - a data Object to convert to a dictionary
    */
    public function createFromData(dataObj:Object){
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
    public function saveToDisk():Void{
        //Convert to data object and then serialize before saving to a cookie
        var dataObj = toData();
        CookieMonster.save(dataObj,'dictionary.' + _currentLanguage,true);
    }
    
    /**
    * Open the Dictionary from disk 
    */
    public function openFromDisk():Void{
        var dataObj:Object = CookieMonster.open('dictionary.' + _currentLanguage,true);
        createFromData(dataObj);
    }
    
    /**
    * Handler function for dictionary being returned from the server
    */
    public function onDictionaryloadedFromServer(dictionaryDataObj:Object){
        //Create the dictionary from the data obj
        createFromData(dictionaryDataObj);
    }

    /**
    * Opens Dictionary from server
    */
    public function openFromServer() {
        //TODO DI 31/05/05 Stub for now until server holds language
        //when server does implement this pseudo-code will be:
        //comms.getRequest(url,callback)
        //this will return a structure that will be passed into createFromData()
        createDictionaryFromCode(_currentLanguage);
    }
    
    /**
    * TODO: THIS IS ONLY USED FOR TESTING WHILST SERVER DOES NOT SUPPORT STRUCTURE
    * REMOVE WHEN NO LONGER REQUIRED
    */
    public function createDictionaryFromCode(lang:String) {
        //_global.breakpoint();
        //Clear the dictionary + set language
        items.clear();
        _currentLanguage = lang;
        switch (lang) {
            case 'en' : 
                //this will return a structure that will be passed into createFromData()
                items.put(0,new DictionaryItem(0,'ws_dlg_title','Workspace'));
                items.put(1,new DictionaryItem(1,'ws_dlg_ok_button','OK'));
                items.put(2,new DictionaryItem(2,'ws_dlg_cancel_button','Cancel'));
                items.put(4,new DictionaryItem(4,'prefs_dlg_title','Preferences'));
                items.put(5,new DictionaryItem(5,'prefs_dlg_ok','OK'));
                items.put(6,new DictionaryItem(6,'prefs_dlg_cancel','Cancel'));
                items.put(7,new DictionaryItem(7,'prefs_dlg_lng_lbl','Language'));
                items.put(8,new DictionaryItem(8,'prefs_dlg_theme_lbl','Theme'));
                
                //--Menu Items
                items.put(9,new DictionaryItem(9,'mnu_file_new','New'));
                items.put(10,new DictionaryItem(10,'mnu_file_open','Open'));
                items.put(11,new DictionaryItem(11,'mnu_file_revert','Revert'));
                items.put(12,new DictionaryItem(12,'mnu_file_close','Close'));
                items.put(13,new DictionaryItem(13,'mnu_file_save','Save'));
                items.put(14,new DictionaryItem(14,'mnu_tools_trans','Draw Transition'));
                items.put(15,new DictionaryItem(15,'mnu_tools_opt','Draw Optional'));
                items.put(16,new DictionaryItem(16,'mnu_tools_prefs','Preferences'));
                items.put(17,new DictionaryItem(17,'mnu_file','File'));
                items.put(18,new DictionaryItem(18,'mnu_tools','Tools'));
                items.put(19,new DictionaryItem(19,'mnu_help','Help'));
                items.put(20,new DictionaryItem(20,'mnu_help_abt','About'));
                items.put(21,new DictionaryItem(21,'mnu_tools_prefs','Preferences'));

                break;
            case 'fr' : 
                //FRENCH TEXT
                items.put(0,new DictionaryItem(0,'ws_dlg_title','zone de travail'));
                items.put(1,new DictionaryItem(1,'ws_dlg_ok_button','OK'));
                items.put(2,new DictionaryItem(2,'ws_dlg_cancel_button','Annuler'));
                items.put(4,new DictionaryItem(4,'prefs_dlg_title','Préférences'));
                items.put(5,new DictionaryItem(5,'prefs_dlg_ok','OK'));
                items.put(6,new DictionaryItem(6,'prefs_dlg_cancel','Annuler'));
                items.put(7,new DictionaryItem(7,'prefs_dlg_lng_lbl','Langue'));
                items.put(8,new DictionaryItem(8,'prefs_dlg_theme_lbl','Thème'));

                //--Menu Items
                items.put(9,new DictionaryItem(9,'mnu_file_new','Nouveau'));
                items.put(10,new DictionaryItem(10,'mnu_file_open','Ouvert'));
                items.put(11,new DictionaryItem(11,'mnu_file_revert','Retournez'));
                items.put(12,new DictionaryItem(12,'mnu_file_close','Fin'));
                items.put(13,new DictionaryItem(13,'mnu_file_save','Save'));
                items.put(14,new DictionaryItem(14,'mnu_tools_trans','Aspiration Transitoire'));
                items.put(15,new DictionaryItem(15,'mnu_tools_opt','Aspiration Facultative'));
                items.put(16,new DictionaryItem(16,'mnu_tools_prefs','Préférences'));
                items.put(17,new DictionaryItem(17,'mnu_file','Fichier'));
                items.put(18,new DictionaryItem(18,'mnu_tools','Outils'));
                items.put(19,new DictionaryItem(19,'mnu_tools','Aide'));
                items.put(20,new DictionaryItem(20,'mnu_help_abt','À propos de...'));
                items.put(21,new DictionaryItem(21,'mnu_tools_prefs','Préférences'));
                break;
            default :
        }
    }
    
    /**
    * Returns the currently selected language
    */
    function get currentLanguage():String {
        return _currentLanguage;
    }
}
