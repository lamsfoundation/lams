import org.lamsfoundation.lams.common.*
import org.lamsfoundation.lams.common.util.*

/**
* Util methods for string manipulation
* @class	StringUtils
* @author	DI
*/
class StringUtils {

	//Declarations
	private var _className:String = 'StringUtils';
	private static var _nextID:Number = 0;

	//Constructor
	function StringUtils() {
	}

	//Getters+Setters
	function get className():String{
		return _className;
	}
	
	/**
	 * If the value passed in is any null type value or LAMS NULL VALUE then blank string is returned
	 * Used to prevent "undefined" appearing in the UI
	 * @usage   var myStr = StringUtils.cleanNull(STR)
	 * @param   str The string to search
	 * @return  a clean string
	 */
	public static function cleanNull(str:String):String{
		if(str == undefined){
			return "";
		}
		if(str == null){
			return "";
		}
		if(str == ""){
			return "";
		}
		if(str == Config.STRING_NULL_VALUE){
			return "";
		}
		if(str == "undefined"){
			return "";
		}
		if(str == "null"){
			return "";
		}
		
		return str;
		
	}
	
	/**
	 * Converts < to &lt; and > to &gt; 
	 * Used good for displaying html looking tags in textfields that expect HTML.
	 * @usage   var myStr:String = StringUtils.escapeAngleBrackets(str);
	 * @param   str 
	 * @return  
	 */
	public static function escapeAngleBrackets(str:String):String{
		var r:String = new String();
		//str = new String(str);
		r = replace(str, "<","&lt;");
		r = replace(r, ">","&gt;");
		return r;
	}
	
	/**
	 * Search replace function, leaves original string untouched
	 * @usage   var myStr:String = StringUtils.replace(str,search, replace);
	 * @param   s      The string to work on
	 * @param   p_str  The string to find
	 * @param   p_repl The string to replace
	 * @return  		The result
	 */
	public static function replace (s:String,p_str:String,p_repl:String) {
		//var s:String = this.toString();
		//var s:String = new String(;
		var position:Number;
		while((position = s.indexOf(p_str)) != -1) {
			position= s.indexOf(p_str);
			s = s.substring(0,position)+p_repl+s.substring(position+p_str.length,s.length)
		}
		return s
	}
	

    /**
    * Converts a query string to an object with attribute-value properies 
    * @usage    e.g. StringUtils.queryStringToObject('http://uklams.net/lams/authoring/author.do?method=getLearningDesignDetails&learningDesignID=1');
    *           will return and object containing two properties 'method' and 'learningDesignID'
    * @returns  Object - containing with attribute-value properies
    */
    public static function queryStringToObject(queryString):Object {
        //Return object
        var tmp_object = new Object();
    
        //Get the args part of the string, i.e. everything after question mark
        var args_arr:Array = queryString.substring(queryString.indexOf('?')+1,queryString.length)
        //Each attribute value pair will be separated with & so split using & as delimiter
        var str_arr:Array = args_arr.split('&');
        //Construct the object dynamically by looping through array of attributes and splitting at '='
        for (var i in str_arr) {
            var v_arr:Array = str_arr[i].split('=');
            tmp_object[v_arr[0]] = v_arr[1];
        }
        return tmp_object;
    } 
    
    /**
    * Pads a string with specified character
    * 
    * @usage trace('left - ' + StringUtils.pad('123',6,'x',true));  //returns '123xxx'
    * 
    * @param val:String - String to pad
    * @param length:Number - Amount to pad by
    * @param char:String - character to pad with, defaults to '0'
    * @param padRight:Boolean - set true for right padding
    * @returns String : padded string
    */
    public static function pad(val:String,length:Number,char:String,padRight:Boolean):String{
        //Char defaults to '0'
        if(!char){
            char='0';
        }
        //Left or right pad, assume left if not specified
        if(!padRight) {
            padRight=false;
        } 
        
        //Go through and pad out the string
        for (var i=val.length;i<length;i++) {
            if(padRight){
                val = val + char;
            }else {
                val = char + val;
            }
        }
        
        //Finished return string
        return val;
        
    }
    
    /**
    * Constructs a unique ID made up of YYYY-MM-DD-HH-SS-MS-ID where ID is a number between 1 and 999 
    * @usage    var myID:Number = NumberUtils.getUID();
    * @returns  String - Containing the UID  
    */
    public static function getUID():String {
        //Return object
        var uid:String;
        //Get the time now in ms and concatenate with random number between 1-10
        var date:Date = new Date();
        
        //YEAR
        var year_str:String = String(date.getFullYear());
        //MONTH
        var month_str:String = String(date.getMonth()+1);
        month_str = StringUtils.pad(month_str,2);
        //DAY
        var day_str:String = String(date.getDate());
        day_str = StringUtils.pad(day_str,2);
        //HOUR
        var hour_str:String = String(date.getHours());
        hour_str = StringUtils.pad(hour_str,2);

        //MINUTE
        var minute_str:String = String(date.getMinutes());
        minute_str = StringUtils.pad(minute_str,2);

        //SECOND
        var second_str:String = String(date.getSeconds());
        second_str = StringUtils.pad(second_str,2);

        //MILLI-SECOND
        var milliSecond_str:String = String(date.getMilliseconds());
        milliSecond_str = StringUtils.pad(milliSecond_str,3);

        //var rand_str:String = String(NumberUtils.randomBetween(0,999));
        //rand_str = StringUtils.pad(rand_str,3);
        
        var id:String = String(_nextID++);
        id = StringUtils.pad(id,3);
        
        //Reset _nextID?
        if(_nextID>999){
            _nextID=0;
        }
        
        //Convert back to a number for return
        //uid = year_str + month_str + day_str + hour_str + minute_str + second_str + milliSecond_str + '-'+ rand_str;
        uid = year_str + month_str + day_str + hour_str + minute_str + second_str + milliSecond_str + '-'+ id;
       
        return uid;
    } 
    
}