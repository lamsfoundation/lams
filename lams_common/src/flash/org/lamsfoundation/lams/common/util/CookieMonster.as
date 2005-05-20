import org.lamsfoundation.lams.common.util.*

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
    * Clears all data in org.lamsfoundation.lams shared object
    * NOTE: - This should be called only when ALL local data for lams is to be deleted
    */
    public function purge():Void {
        _so.clear();
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
    public static function save(obj:Object,name:String){
        //TODO DI 16/05/05 Error handling needs to provide for size limits, duplicates etc.
        //Save the object under the named reference provided
        var _so = SharedObject.getLocal(SO_ROOT+'.'+name,'/');        
        _so.data.name = name;
        
        //Append object to _so.data and save cookie
        _so.data[name]=obj;    
        _so.flush();
    }
    
    
    /**
    * Returns (opens) the named cookie
    * @param name - the handle of the shared object used for reference with save();
    */
    public static function open(name:String):Object{
        //TODO DI 16/05/05 Error handling needs to provide for missing objects
        //Get named local object
        var _so = SharedObject.getLocal(SO_ROOT+'.'+name,'/');        
        return _so.data[name];    
    }
    
    /**
    * Check whether cookie exists
    */
    public static function cookieExists(cookieName:String):Boolean {
        //TODO - DI 19/05/05 Check that cookie exists
        return true;
    }
    
    function get className():String{
        return _className;
    }
}
