import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.comms.*

/**
* CookieMonster - Singleton used for opening and saving shared objects (cookies);
* 
* 
* @author   DI
* @usage    
*           //Import and get and instance of CookieMonster singleton
*           import CookieMonster
*           var cm = CookieMonster.getInstance();
*           
*           //Create an object and save under a name with cmInstance.save(obj:Object,name:String);
*           var john:Object = {name:'john',age:25,town:'London'};
*           cm.save(john,'john');
*           //The following code should trace "name:John age:25"
*           var savedPerson:Object = cm.open('john');
*           trace('name:' + savedPerson.name + ' age:' + savedPerson.age);
*/
class CookieMonster {
    
    //CookieMonster instance is stored as a static in the CookieMonster class
    private static var _instance:CookieMonster = null;     
    private static var SO_ROOT:String = 'org.lamsfoundation.lams';

    private var _className = 'CookieMonster';
    private var _so:SharedObject;

    //Constructor    
    private function CookieMonster(){
    }
    
    /**
    * Clears all LAMS shared objects 
    * NOTE: - This should be called only when ALL local data for lams is to be deleted
    */
    public static function purge():Void {
        //Go through all shared objects defined on this machine and delete them
        //Get shared object containing cookie indices
        var so = SharedObject.getLocal(SO_ROOT+'.'+'index','/');        
        var index_array:Array = so.data.index;
        //Go through index array and delete shared objects
        for(var i=0;i<index_array.length;i++) {
            deleteCookie(index_array[i]);
        }
        //Now that all shared objects have been deleted, delete index SO too
        so.clear();
    }
    
    /**
    * Retrieves an instance of the CookieMonster singleton
    */ 
    public static function getInstance():CookieMonster{
        if(CookieMonster._instance == null){
            CookieMonster._instance = new CookieMonster();
        }
        return CookieMonster._instance;
    }
    
    /**
    * Saves the object under the name provided
    * @param obj - The object to save in a cookie 
    * @param name - the handle of the shared object used for reference with open();
    */
    public static function save(obj:Object,name:String,serialize:Boolean):Boolean{
        //Serialize first
        if(serialize) {
            if(obj) {
                var wddx:Wddx = new Wddx();
                var obj:Object = wddx.serialize(obj);
            }else {
                //TODO DI Raise error
                Debugger.log('!! NO OBJECT TO SERIALIZE',Debugger.CRITICAL,'save','org.lamsfoundation.lams.CookieMonster');
                return false;
            }
        }
        //TODO DI 16/05/05 Error handling needs to provide for size limits, duplicates etc.
        //Save the object under the name provided
        var _so = SharedObject.getLocal(SO_ROOT+'.'+name,'/');        
        _so.data.name = name;
        
        //Append object to _so.data and save cookie
        _so.data[name]=obj;    
        var res = _so.flush();
        
        //TODO DI 26/05/05 Build in error handling
        if(res==true) {
            //Index this entry 
            CookieMonster.addToIndex(name);
            return true;
        }else {
            return false;
        }
    }
    
    /**
    * Returns (opens) the named cookie
    * @param name - the handle of the shared object used for reference with save();
    */
    public static function open(name:String,deserialize:Boolean):Object{
       //TODO DI 16/05/05 Error handling needs to provide for missing objects
       if(CookieMonster.cookieExists(name)){
            //Get named local object
            var _so = SharedObject.getLocal(SO_ROOT+'.'+name,'/');        
            var obj = _so.data[name];
            //Wddx required?
            if(deserialize) {
                var wddx:Wddx = new Wddx();
                var obj:Object = wddx.deserialize(obj);
            }
            //Return the retrieved object
            return obj;
        } else {
            //TODO DI 27/05/05 Raise error
            Debugger.log('cookie not found :' + name,Debugger.CRITICAL,'open','CookieMonster');
            return null;
        }
    }
    
    
    /**
    * @returns Array containing index of shared objects saved with cookie monster.
    */
    public static function getSharedObjectsIndex():Array {
        //Open shared object containing array
        if (CookieMonster.cookieExists('index')) {
            var index_array:Array = Array(CookieMonster.open('index',false));
            return index_array;
        } else {
            return [];
        }
    }
    
    /**
    * will store the name of the shared object in the catalog
    * @returns true/false indicating success status
    */
    public static function addToIndex(name:String):Boolean {
        //Open the index object from disk, creating if necessary
        //Get the index array
        
        var itemFound:Boolean = false;
        var index_array:Array;
        
        //Retrieve index data 
        //Open shared object containing array, use getLocal for referencing shared objects whether they exist or not
        var so = SharedObject.getLocal(SO_ROOT+'.'+'index','/');        
        if (!CookieMonster.cookieExists('index')) {
            so.data.name = 'index';
            //Create empty index_array
            index_array = [];
        } else {
            //Already exists so retrieve the index array
            index_array = Array(CookieMonster.open('index',false));
        }
        
        //Go through and add item if not present
        for(var i=0;i<index_array.length;i++){
            //If match is found set flag and exit search
            if(index_array[i]==name){
                itemFound = true;
                break;
            }
        }
        //If item wasn't found, then add to array and save
        if(!itemFound) {
            index_array.push(name);
            //Can't use save for this as that's where we were called from
            so.data.index = index_array; 
            var res = so.flush();
            
            //TODO DI 27/05/05 Build in error handling
            if(res==true) {
                return true;
            }else {
                return false;
            }
        }else {
            //Item was found (i.e. already there) so we didn't need to save therefore return true
            return true;
        }
    }
    
    /**
    * Removes this cookie name from the index array
    * @returns Boolean representing succes in removal
    */
    public static function removeFromIndex(name:String):Boolean {
        var indexFound:Boolean = false;
        //Get shared index array, splice to remove index and then flush
        if(CookieMonster.cookieExists('index')) {
            var so = SharedObject.getLocal(SO_ROOT+'.'+'index','/');        
            var index_array:Array = so.data.index;
            
            for (var i=0;i<index_array.length;i++) {
                if(index_array[i]==name) {
                    index_array.splice(i);
                    indexFound=true;
                    break;
                }    
            }
            //Was it found, if so saved shared object and return true 
            if(indexFound) {
               so.data.index = index_array;
               so.flush();
               return true;
            } else {
               Debugger.log('Index not found :' + name,Debugger.CRITICAL,'removeFromIndex','CookieMonster'); 
               return false;
            }
        }
    }    
    
    /**
    * Check whether cookie exists
    */
    public static function cookieExists(name:String):Boolean {
        //TODO - DI 19/05/05 Check that cookie exists
        //Get a reference to the shared object, and see if it existst
        var so = SharedObject.getLocal(SO_ROOT+'.'+name,'/');
        if(so.data.name){
            return true;
        }else {
            return false;    
        }
    }
    
    /**
    * Deletes a Cookie (SharedObject)
    * @returns Boolean representing success
    */
    public static function deleteCookie(name:String):Boolean{
        if(CookieMonster.cookieExists(name)){
            Debugger.log('Deleting cookie :' + name,Debugger.GEN,'deleteCookie','CookieMonster');
            var so:SharedObject = SharedObject.getLocal(SO_ROOT+'.'+name,'/');
            so.clear();
            //Now check whether it's been removed for return
            if (!CookieMonster.cookieExists(name)) {
                //Object did exist and now doesn't so delete was successful
                return true;
            } else {
                //Object still exists after attempted delete so operation failed
                return false;
            }
        } else {
            //TODO DI 31/05/05 Raise Error
            Debugger.log('Cookie not found :' + name,Debugger.CRITICAL,'deleteCookie','CookieMonster');
            return false;
        }
    }
    
    function get className():String{
        return _className;
    }
}
