package org.lamsfoundation.lams.common.util
{
	public class WDDXUtil
	{
		//nulls
		public static var STRING_NULL_VALUE:String = "string_null_value";
		public static var NUMERIC_NULL_VALUE:Number = -111111;
		public static var DATE_NULL_VALUE:Date = new Date(0);
		public static var BOOLEAN_NULL_VALUE:String = "boolean_null_value";
			
		public function WDDXUtil()
		{
		}
		
		/**
		 * If the value passed in is any null type value or LAMS NULL VALUE then blank string is returned
		 * Used to prevent "undefined" or "string_null_value" appearing in the UI
		 * @usage   var myStr = WDDXUtils.cleanNull(WDDXOBJ)
		 * @param   obj 
		 * @return  clean string or null value.
		 */
		public static function cleanNull(obj:Object):Object {
			
			if(obj is String && obj == STRING_NULL_VALUE){
				return "";
			} else if(obj is Number && obj == NUMERIC_NULL_VALUE){
				obj = null;
			} else if(obj is Boolean && obj == BOOLEAN_NULL_VALUE) {
				obj = null;
			}
			
			return obj;
			
		}
		
		/**
		 * Checks to see if the value passed in is any of the null values defined in Config.
		 * @usage   
		 * @param   v 
		 * @return  boolean true if null
		 */
		public static function isWDDXNull(v:Object):Boolean{
			if(v==STRING_NULL_VALUE){
				return true;
			}
			if(v==NUMERIC_NULL_VALUE){
				return true;
			}
			if(v==STRING_NULL_VALUE){
				return true;
			}
			if(v==DATE_NULL_VALUE){
				return true;
			}
			if(v==BOOLEAN_NULL_VALUE){
				return true;
			}
			return false;
		}
	
		public static function toWDDXNull(obj:Object):Object {
			if(obj is String && (obj == "" || obj == null))
				return STRING_NULL_VALUE;
			else if(obj is Number || obj is int || obj is uint)
				if(obj == 0 || obj == null)
					return NUMERIC_NULL_VALUE;
		
			return obj;
		}

	}
}