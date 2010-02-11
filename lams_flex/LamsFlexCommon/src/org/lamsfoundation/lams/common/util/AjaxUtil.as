package org.lamsfoundation.lams.common.util
{
	import flash.external.ExternalInterface;
	import flash.utils.Dictionary;
	
	import mx.controls.Alert;
	import mx.utils.StringUtil;
	
	/**
	 * A utility to perfom synchronous ajax calls
	 * A workaround for the flex shortcoming that only allows asynchronous calls
	 * 
	 * NOTE: THIS CAN ONLY WORK ON THE SERVER, NOT FROM A STATIC FILE
	 * 
	 * @author lfoxton
	 * 
	 */
	public class AjaxUtil
	{
		public static const GET:String = "GET";
        public static const POST:String = "POST";
		
		public function AjaxUtil(){}
		
		/**
		 * Makes an ajax call to the server so it can be done synchronously
		 * 
		 * @param url the url to call
		 * @param params hashmap of the key,value pairs for the url params
		 * @param requestType GET or POST
		 * @return return object
		 * 
		 */
		public static function send(url:String, params:Dictionary = null, requestType:String = GET):Object {
			var ret:Object = null
			if (ExternalInterface.available) {
				var funcString:String = getAjaxFunction(url, params, requestType);
				ret = ExternalInterface.call(funcString);
			} else {
				Alert.show("Browser not supported or javascript disabled");
			}
			return ret;
		}
		
		
		/**
		 * Creates the neccesary javascript code to call the server
		 * with the given parmas 
		 * @param url the url to call
		 * @param params hashmap of the key,value pairs for the url params
		 * @param requestType GET or POST
		 * @return return object
		 * 
		 */
		private static function getAjaxFunction(url:String, params:Dictionary, requestType:String):String {
			
			var paramsStr:String = "";
			if (params != null) {
				paramsStr += "?"
				var i:int = 0;
				for (var key:Object in params) {
					if (i > 0) {
						paramsStr += "&";
					}
					paramsStr += key.toString() + "=" + params[key];
					i++;
				}
			}
			
			url += paramsStr;
			
			var ret:String = "function() {" + 
            	"var xmlHttp;"+
            	"try {" +         
           			"xmlHttp = new ActiveXObject('Msxml2.XMLHTTP');" +                                     
          		"} catch(e) {" + 
            		"try {" + 
              			"xmlHttp=new ActiveXObject('Microsoft.XMLHTTP');" + 
	            	"} catch(oc) {" + 
	             		"xmlHttp=null;" + 
           			"}" +             
         		"}" + 
          		"if(!xmlHttp && typeof XMLHttpRequest != 'undefined') {" + 
             		"xmlHttp=new XMLHttpRequest();" + 
         		"} try {" + 
           			"xmlHttp.open('"+ requestType + "','" + url + "', false);"+
           			"xmlHttp.send();"+
           			"return xmlHttp.responseText;" +            
         		"}catch(x){alert(x)}"+      
       		"}";
       		
       		return ret;
		}

	}
}