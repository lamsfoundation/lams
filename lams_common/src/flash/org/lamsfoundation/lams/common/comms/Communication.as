import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.comms.*import org.lamsfoundation.lams.common.*;
import org.lamsfoundation.lams.common.ui.*;
import org.lamsfoundation.lams.authoring.*;

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
	
	//so compiler can see application when not in LAMS
	private var Application;
    
   
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
			if(_root.serverURL==null){
				Debugger.log("! serverURL is not set, unable to connect to server !!",Debugger.CRITICAL,'Consturcutor','Communication');
			}else{
				_serverURL = _root.serverURL;
			}
			
		}
        
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
    * @param requestURL:String  URL on the server for request script
    * @param handler:Function   callback function to call with the response object
  	* @param isFullURL:Boolean  Inicates if the requestURL is fully qualified, if false serverURL will be prepended
    * @returns Void
    */
    public function getRequest(requestURL:String,handler:Function,isFullURL:Boolean):Void{
		if(Application != null){
			Cursor.showCursor(Application.C_HOURGLASS);
		}
		//Create XML response object 
        var responseXML = new XML();
        //Assign onLoad handler
        responseXML.onLoad = Proxy.create(this,onServerResponse,queueID);
        //Add handler to queue
        addToQueue(handler);        
        //Assign onData function        
        setOnData(responseXML);
        
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
    * XML load handler for getRequest()
    * @param success            XML load status
    * @param wrappedPacketXML   The wrapped XML response object 
    * @param queueID:Number     ID of request handler on queue 
    */
    private function onServerResponse(success:Boolean,wrappedPacketXML:XML,queueID:Number){
        trace('XML loaded success:'+ success);
        //Load ok?
        if(success){
            var responseObj:Object = wddx.deserialize(wrappedPacketXML);

            
			
			if(responseObj.messageType == null){
				Debugger.log('Message type was:'+responseObj.messageType+' , cannot continue',Debugger.CRITICAL,'getRequest','Communication');			
				Debugger.log('xml recieved is:'+wrappedPacketXML.toString(),Debugger.CRITICAL,'getRequest','Communication');			
				return -1;
			}
			
            //Check for errors in message type that's returned from server
			if(responseObj.messageType == FRIENDLY_ERROR_CODE){
                //user friendly error
                //showAlert("Oops", responseObj.body, "sad");
            }else if(responseObj.messageType == SYSTEM_ERROR_CODE){
                //showAlert("System error", "<p>Sorry there has been a system error, please try the operation again. If the problem persistes please contact support</p><p>Additional information:"+responseObj.body+"</p>", "sad");
            }else{
                //Everything is fine so lookup callback handler on queue 
                if(responseObj.messageValue != null){
					dispatchToHandlerByID(queueID,responseObj.messageValue);
				}else{
					Debugger.log('Message value was null, cannot continue',Debugger.CRITICAL,'getRequest','Communication');			
				}
            }
			
			//Now delete the XML 
            delete wrappedPacketXML;
        }else {
            //TODO DI 12/04/05 Handle onLoad error
            //showAlert("System error", "<p>Communication Error</p>", "sad");
			Debugger.log("XML Load failed",Debugger.CRITICAL,'onServerResponse','Communication');			
			}
    }
    
  
    /**
	* Sends a data object to the server and directs response to handler function.
	* 
    * @param dto:Object 		The flash object to send. Will be WDDX serialised    
	* @param requestURL:String  The url to send and load the data from
	* @param handler:Function   The function that will handle the response (usually an ACK response)
	* @param isFullURL:Boolean  Inicates if the requestURL is fully qualified, if false serverURL will be prepended
	* 
	* @usage: 
	* 
    */
    public function sendAndReceive(dto:Object, requestURL:String,handler:Function,isFullURL){
        //Serialise the Data Transfer Object
        var xmlToSend:XML = serializeObj(dto);
		//xmlToSend.contentType="dave";
        
		//Create XML response object 
        var responseXML = new XML();
		 //Assign onLoad handler
        responseXML.onLoad = Proxy.create(this,onServerResponse,queueID);

        //Assign onLoad handler
       // responseXML.onLoad = Proxy.create(this,onSendACK,queueID);
        //Add handler to queue
        addToQueue(handler);        
        //Assign onData function        
        setOnData(responseXML);
        
        //TODO DI 11/04/05 Stub here for now until we have server implmenting new WDDX structure
        if(isFullURL){
            Debugger.log('Posting to:'+requestURL,Debugger.GEN,'sendAndReceive','Communication');			
			Debugger.log('Sending XML:'+xmlToSend.toString(),Debugger.GEN,'sendAndReceive','Communication');			
            xmlToSend.sendAndLoad(requestURL,responseXML);
        }else{
            Debugger.log('Posting to:'+_serverURL+requestURL,Debugger.GEN,'sendAndReceive','Communication');
			Debugger.log('Sending XML:'+xmlToSend.toString(),Debugger.GEN,'sendAndReceive','Communication');			
            xmlToSend.sendAndLoad(_serverURL+requestURL,responseXML);
        }
    }
    
    /**
     * Received Acknowledgement from server, response to sendAndReceive() method
     * 
     * @usage   
     * @param   success     
     * @param   loadedXML   
     * @param   queueID     
     * @param   deserialize 
     * @return  
     */
    private function onSendACK(success:Boolean,loadedXML:XML,queueID:Number,deserialize:Boolean) {
       //TODO DI 06/06/05 find out what is required here, is it enough to assign onLoad handler to onServerResponse?
    }
    
    /**
    * Load Plain XML with optional deserialisation
    */
    public function loadXML(requestURL:String,handler:Function,isFullURL:Boolean,deserialize:Boolean):Void{
        //Create XML response object 
        var responseXML = new XML();
        //Assign onLoad handler
        responseXML.onLoad = Proxy.create(this,onXMLLoad,queueID,deserialize);
        //Add handler to queue
        addToQueue(handler);        
        //Assign onData function        
        setOnData(responseXML);
        
        //TODO DI 11/04/05 Stub here for now until we have server implmenting new WDDX structure
        if(isFullURL){
			Debugger.log('Requesting:'+requestURL,Debugger.GEN,'loadXML','Communication');			
			responseXML.load(requestURL);
		}else{
			Debugger.log('Requesting:'+_serverURL+requestURL,Debugger.GEN,'loadXML','Communication');			
			responseXML.load(_serverURL+requestURL);
		}
    }
    
    /**
    * Load XML load handler for loadXML() - 
    * 
    * @param success:Boolean        XML load status
    * @param loadedXML:XML          unwrapped WDDX XML 
    * @param queueID:Number         ID of request handler on queue 
    * @param deserialize:Boolean    flag to determin whether object should be deserialised before being passed back to handler 
    */
    private function onXMLLoad(success:Boolean,loadedXML:XML,queueID:Number,deserialize:Boolean) {
        //Deserialize
        if(deserialize){
            //Deserialize and pass back to handler
            var responseObj:Object = deserializeObj(loadedXML);
            dispatchToHandlerByID(queueID,responseObj);
        }else {
            //Call handler, passing in XML Object
            dispatchToHandlerByID(queueID,loadedXML);
        }
    }
    
    /**
    * Assign the onData method for the XML object
    * 
    * @param xmlObject:XML  the xml object to assign the onData method
    */
    private function setOnData(xmlObject:XML){
        //Set ondata handler to validate data returned in XML object
        xmlObject.onData = function(src){
			Debugger.log('src:'+src,Debugger.GEN,' xmlObject.onData ','Communication');		
			if(Application != null){
				Cursor.showCursor(Application.C_DEFAULT);
            }
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
    }    
	
	/**
	 * Serializes an object into WDDX XML
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
	 * Deserializes WDDX XML to produce an object
	 * @usage  	var wddxXML:XML = commsInstance.serializeObj(obj); 
	 * @param   wddx 	The XML to be de-serialized	
	 * @return  Object	de-serialized object
	 */
	public function deserializeObj(xmlObj:XML):Object{
        var dto:Object = wddx.deserialize(xmlObj);
		return dto;
	}  
    
    /**
    * Finds handler in queue, dispatches object to it and deletes item from queue
    */
    private function dispatchToHandlerByID (ID:Number,o:Object) {
        var index:Number = getIndexByQueueID(ID);
        //Dispatch handler passing in object stored under messageValue
        queue[index].handler(o);
        //Now that request has been dealt with remove item from queue
        removeFromQueue(ID);
    }
    
    /**
    * Adds a key handler pair to the queue
    */
    private function addToQueue(handler:Function){
        //Add to the queue and increment the queue ID
        queue.push({queueID:queueID++,handler:handler});
        
        //Reset queueID?
        if(queueID>=MAX_REQUESTS) {
            queueID=0;
        }
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
    
    /**
    * @returns Number - length of the queue
    */
    function get queueLength():Number {
        return queue.length;
    }
}