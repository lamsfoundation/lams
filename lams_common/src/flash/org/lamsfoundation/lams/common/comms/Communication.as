import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.comms.*import org.lamsfoundation.lams.common.*;

/**
* Communication - responsible for server side communication and wddx serialisation/de-serialisation
* @author  DI   05/04/05
* 
*/
class org.lamsfoundation.lams.common.comms.Communication {
    private var _serverUrl:String;
    private var errorCodes:Array;
    private var ignoreWhite:Boolean;  
    private var responseXML:XML;      //XML object for server response
    private var wddx:Wddx;            //WDDX serializer/de-serializer
    
    private var requestHandlers:Hashtable;
    
    //TODO DI 11/04/05 Manage key/handler relationship - temp solution
    private var requestCallBack:Function;
    private var onXMLData:Function;
    
    /**
    * Comms constructor
    */
    function Communication(aServerUrl:String){
        trace('Communication.constructor');
        //Set up error codes used in communication with server
        errorCodes=[1,2,3];
        ignoreWhite = true;
		//_global.breakpoint();
		if(aServerUrl == null){
			_serverUrl = Config.getInstance().serverUrl;
		}		//Debugger.log('_serverUrl:'+_serverUrl,4,'Consturcutor','Communication');
        wddx = new Wddx();
    }
    
    /**
    * request a design from the server
    * @param requestUrl URL on the server for request script
    * @param handlerFn  a callback function to call with the response object
    */
    public function getRequest(requestUrl:String,handlerFn:Function,isFullURL){
        trace('Communication.getRequest()');
        //Create XML response object (deleting old XML if neccesary), assign data+load events and load XML
        if(responseXML!=null){
            delete responseXML;
        }
        responseXML = new XML();
        //Set ondata handler to validate data returned in XML object
        responseXML.onData = function(src){
            if (src != undefined) {
                //Check for login page
                if(src.indexOf("j_security_login_page") != -1){
                    //TODO DI 12/04/05 deal with error from session timeout/server error
                    trace('j_security_login_page received');
                    this.onLoad(false);
                }else {
                    //Data alright, must be a packet, allow onLoad event
                    this.parseXML(src);
                    this.loaded=true;
                    this.onLoad(true,this);
                }
            } else {
                this.onLoad(false);
            }            
        }
        //Assign onLoad handler
        responseXML.onLoad = Proxy.create(this,xmlOnLoad,responseXML);
        
        //TODO DI 11/04/05 Stub here for now until we have server implmenting new WDDX structure
        if(isFullURL){
			Debugger.log('Requesting:'+requestUrl,Debugger.GEN,'getRequest','Communication');			
			responseXML.load(requestUrl);
		}else{
			Debugger.log('Requesting:'+_serverUrl+requestUrl,Debugger.GEN,'getRequest','Communication');			
			responseXML.load(_serverUrl+requestUrl);
		}
        //_global.breakPoint();
        //responseXML.load('sampleLearningDesign.xml');
        requestCallBack = handlerFn;
    }
    
    /**
    * XML onLoad handler called after data validation
    * @param success            XML load status
    * @param wrappedPacketXML   The wrapped XML response object 
    */
    private function xmlOnLoad(success:Boolean,wrappedPacketXML:XML){
        trace('XML loaded success:'+ success);
        /*
        * Validate the XML
        * If No Errors THEN
        *   Use key to lookup the handler for the object
        *   fire callback handler passing in server response object
        * ELSE
        *   deal with errors
        * END IF
        */
        
        //Load ok?
        if(success){
            var responseObj:Object = wddx.deserialize(wrappedPacketXML);
            //Call request handler function now that we have a response
            //Match key with handler and call handler
            requestCallBack(responseObj.messageValue);
        }else {
            //TODO DI 12/04/05 Handle onLoad error
            trace('XML load failed!');
        }
    }
    
    /**
    * Send an object to the server
    */
    public function sendObject(){
        
    }
    
    /**
	* Sends a data object to the server and directs response to handler function.
	* 
    * @param dto		The flash object to send. Will be WDDX serialised    
	* @param requestUrl the url to send and load the data from
	* @param handlerFn  the function that will handle the response (usually an ACK response)
	* @param isFullURL  Inicates if the requestUrl is fully qualified, if false serverURL will be prepended
	* 
	* @usage: 
	* 
    */
    public function sendAndReceive(dto:Object, requestUrl:String,handlerFn:Function,isFullURL){
      //TODO: Implement!  
    }
	
	/**
	 * Serialzes an object into WDDX XML
	 * @usage  	var wddxXML:XML = commsInstance.serializeObj(obj); 
	 * @param   dto 	The object to be serialized	
	 * @return  sXML	WDDX Serialized XML
	 */
	public function serializeObj(dto:Object):XML{
		var sXML:XML = new XML();
		sXML = wddx.serialize(dto);
		return sXML;
	}
    
    
    /**
    * returns current server URL  
    */
    function get serverUrl():String{
        return _serverUrl;
    }


    /**
    * sets current server URL  
    */
    function set serverUrl(val:String){
        _serverUrl = val;
    }
}