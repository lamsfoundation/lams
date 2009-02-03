package org.lamsfoundation.lams.common.util
{
	import mx.utils.StringUtil;

	public class LAMSStringUtil extends StringUtil
	{
		public function LAMSStringUtil()
		{
			super();
		}

        // convert a string to a boolean
        public static function stringToBool(string:String):Boolean{
        	switch(string){
        		case "1":
        		case "true":
        		case "yes":
        			return true;
        		case "0":
        		case "false":
        		case "no":
        			return false;
        		default:
        		return Boolean(string);
        	}
        }
	}
}