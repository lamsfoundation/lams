import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.comms.*import org.lamsfoundation.lams.common.*;

/**
* Communication - responsible for server side communication and wddx serialisation/de-serialisation
* @author  DI   05/04/05
* 
*/
class org.lamsfoundation.lams.common.comms.Communication {
    
    private static var FRIENDLY_ERROR_CODE:Number = 1;  //Server error codes
    private static var SYSTEM_ERROR_CODE:Number = 2;
    
    private static var MAX_REQUESTS:Number = 20;       //Maximum no of simultaneous requests
    
    private var _serverURL:String;
    private var ignoreWhite:Boolean;  
    private var responseXML:XML;      //XML object for server response
    private var wddx:Wddx;            //WDDX serializer/de-serializer
    
    private var queue:Array;
    private var queueID:Number;
    
   
    /**
    * Comms constructor
    */
    function Communication(serverURL:String){
        trace('Communication.constructor');

        //Set up queue
        queue=[];
        queueID=0;
        
        
        ignoreWhite = true;
		//_global.breakpoint();
		if(_serverURL == null){
			//_serverURL = Config.getInstance().serverUrl;
            
		}
        _serverURL = _root.serverURL;
        		//Debugger.log('_serverURL:'+_serverURL,4,'Consturcutor','Communication');
        wddx = new Wddx();
    }
    
    /**
    * Make a request to the server.  Each request handlers is added to a queue whilst waiting for the (asynchronous) response 
    * from the server. The server returns a wrapped XML packet that is unwrapped and de-serialised on load and passed back
    * to the request handler
    * 
    * @usage -  var myObject.test = function (deserializedObject) {
    *               //Code to process object here....
    *           }
    *           commsInstance.getRequest('http://dolly.uklams.net/lams/lams_authoring/all_library_details.xml',myObject.test,true)   
    * 
    * @param requestURL URL on the server for request script
    * @param handlerFn  a callback function to call with the response object
    * @returns Void
    */
    public function getRequest(requestURL:String,handler:Function,isFullURL:Boolean):Void{
        trace('Communication.getRequest()');
        
        //Create XML response object 
        var responseXML = new XML();
        
        //Add request to queue
        addToQueue(queueID,handler);
        
        //Reset queueID?
        if(queueID>=MAX_REQUESTS) {
            queueID=0;
        }
        
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
        responseXML.onLoad = Proxy.create(this,onServerResponse,queueID);
        
        //Increment the queueID for next time
        queueID++;
        
        //TODO DI 11/04/05 Stub here for now until we have server implmenting new WDDX structure
        if(isFullURL){
			Debugger.log('Requesting:'+requestURL,Debugger.GEN,'getRequest','Communication');			
			responseXML.load(requestURL);
		}else{
			Debugger.log('Requesting:'+_serverURL+requestURL,Debugger.GEN,'getRequest','Communication');			
			responseXML.load(_serverURL+requestURL);
		}
    }
    

    
    /**
    * XML load handler called after data validation on XML returned from server
    * @param success            XML load status
    * @param wrappedPacketXML   The wrapped XML response object 
    */
    private function onServerResponse(success:Boolean,wrappedPacketXML:XML,queueID:Number){
        trace('XML loaded success:'+ success);
        //Load ok?
        if(success){
            var responseObj:Object = wddx.deserialize(wrappedPacketXML);

            //Now delete the XML 
            delete wrappedPacketXML;
            
            //Check for errors in message type that's returned from server
            if(responseObj.messageType == FRIENDLY_ERROR_CODE){
                //user friendly error
                //showAlert("Oops", responseObj.body, "sad");
            }else if(responseObj.messageType == SYSTEM_ERROR_CODE){
                //showAlert("System error", "<p>Sorry there has been a system error, please try the operation again. If the problem persistes please contact support</p><p>Additional information:"+responseObj.body+"</p>", "sad");
            }else{
                //Everything is fine so lookup callback handler on queue 
                var index:Number = getIndexByQueueID(queueID);
                //Dispatch handler passing in object stored under messageValue
                queue[index].handler(responseObj.messageValue);
                //Now that request has been dealt with remove item from queue
                removeFromQueue(queueID);
            }            
        }else {
            //TODO DI 12/04/05 Handle onLoad error
            //showAlert("System error", "<p>Communication Error</p>", "sad");
        }
    }
    
    /**
    * Adds a key handler pair to the queue
    */
    private function addToQueue(queueID:Number,handler:Function){
        queue.push({queueID:queueID,handler:handler});
    }
    
    /**
    * removes a key handler pair from the queue
    * @returns boolean indicating success
    */
    private function removeFromQueue(queueID:Number):Boolean{
        //find item and delete it
        var index:Number = getIndexByQueueID(queueID);
        if(index!=-1) {
            //Remove the item from the queue
            queue.splice(index,1);
            return true;
        }else {
            return false;
            Debugger.log('Item not found in queue :' + queueID,Debugger.GEN,'removeFromQueue','Communication');
        }
    }
    
    /**
    * searches queue for item by key 
    * @returns Array index value, -1 if key not found
    */
    private function getIndexByQueueID(queueID):Number {
        //Go through handlers and remove key
        for(var i=0;i<queue.length;i++) {
            //If key found, delete key/handler object from array 
            if(queue[i].queueID==queueID) {
                return i;
            }
        }
        //If we're here then we didn't find it return an error
        return -1;
    }
    
    /**
	* Sends a data object to the server and directs response to handler function.
	* 
    * @param dto		The flash object to send. Will be WDDX serialised    
	* @param requestURL the url to send and load the data from
	* @param handlerFn  the function that will handle the response (usually an ACK response)
	* @param isFullURL  Inicates if the requestURL is fully qualified, if false serverURL will be prepended
	* 
	* @usage: 
	* 
    */
    public function sendAndReceive(dto:Object, requestURL:String,handlerFn:Function,isFullURL,overrideKey:String){
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
        return _serverURL;
    }


    /**
    * sets current server URL  
    */
    function set serverUrl(val:String){
        _serverURL = val;
    }
}