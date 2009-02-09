/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ************************************************************************
 */

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
	public static var reserved:Array = [";","/","?",":","@","&","=","+","$",",","\"","%","#","|","<",">","\\"]; 	// reserved URI characters
	public static var reserved_str:String = "; / ? : @ & = + $ , \" % # | &gt; &lt; \\";
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
		var position:Number;
		
		while((position = s.indexOf(p_str)) != -1) {
			position= s.indexOf(p_str);
			s = s.substring(0,position)+p_repl+s.substring(position+p_str.length,s.length)
		}
		
		return s;
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

        var id:String = String(_nextID++);
        id = StringUtils.pad(id,3);
        
        //Reset _nextID?
        if(_nextID>999){
            _nextID=0;
        }
        
        //Convert back to a number for return
        uid = year_str + month_str + day_str + hour_str + minute_str + second_str + milliSecond_str + '-'+ id;
       
        return uid;
    } 
	
	
	/**
	 * Enter description here
	 * 
	 * @usage   		To Convert Month Number into full Month Name
	 * @param   		month number
	 * @return  		Month Name String
	 */
	public static function getMonthAsString(month:Number):String {
		var monthNames_array:Array = new Array("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
		return monthNames_array[month];
}

	
	/**
	 * Checks to see if the value passed in is null, undefined or a blank string
	 * @usage   
	 * @param   v 
	 * @return  
	 */
	public static function isEmpty(v):Boolean{
		if(v == undefined || v== null || v==""){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Returns a new String based on the input String with the leading and trailing spaces removed
	 * @usage   
	 * @param   str 
	 * @return  
	 */
	public static function trim(str:String):String {
		var startIdx:Number;
		for (var i=0; i<str.length; i++) {
			if (str.charAt(i) != " ") {
				startIdx = i;
				break;
			}
		}
		
		var endIdx:Number;
		for (var i=(str.length); i>0; i--) {
			if (str.charAt(i-1) != " ") {
				endIdx = i;
				break;
			}
		}
		
		// if there are no spaces, return a new instance of the original string, else return the trimmed string
		if (startIdx == undefined || startIdx == null || endIdx == undefined || endIdx == null) { 
			if (str.length > 0) return new String(""); // string contains only spaces, return a new empty string
		}
		
		var trimmedStr:String = str.substring(startIdx, endIdx);
		return trimmedStr;
	}
	
	/**
	 * Checks to see if the value passed in is any of the null values defined in Config.
	 * @usage   
	 * @param   v 
	 * @return  boolean true if null
	 */
	public static function isWDDXNull(v):Boolean{
		if(v==Config.STRING_NULL_VALUE){
			return true;
		}
		if(v==Config.NUMERIC_NULL_VALUE){
			return true;
		}
		if(v==Config.STRING_NULL_VALUE){
			return true;
		}
		if(v==Config.DATE_NULL_VALUE){
			return true;
		}
		if(v==Config.BOOLEAN_NULL_VALUE){
			return true;
		}
		return false;
	}
	
	public static function isNull(v):Boolean{
		if(isEmpty(v)){
			return true;
		}else if(isWDDXNull(v)){
			return true;
		}else{
			return false;
		}
		
	}
	
	public static function getButtonWidthForStr(s:String):Number {
		var spacing:Number = 10;
		if(s != null) {
			var container = ApplicationParent.root;
			container.createTextField("str", container.getNextHighestDepth(), -1000, -1000, 0, 0);
			var str:TextField = container["str"];
			str.text = s;
			return str.textWidth + spacing;
		} else {
			return 0;
		}
	}
	
	public static function scanString(s:String, char:String):Array {
		var charArray:Array = new Array();
		for(var i:Number = 0; i < s.length; i++) {
			if(s.charAt(i) == char){
				charArray.push(i);
			}
		}
		return charArray;
	}
	
	public static function isANumber(s:String):Boolean {
		for (var i:Number = 0; i < s.length; i++) {
			Debugger.log("stringUtils isANumber char at " + i + ": " + s.charCodeAt(i), Debugger.MED, "isANumber", "StringUtils");
			if (s.charCodeAt(i) > 57 || s.charCodeAt(i) < 48) {
				Debugger.log("stringUtils isANumber false strlen: " + s.length, Debugger.MED, "isANumber", "StringUtils");
				return false;
			}
		}
		Debugger.log("stringUtils isANumber true with strlen: " + s.length, Debugger.MED, "isANumber", "StringUtils");
		return true;
	}
    
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
	
	public static function removeAccents(s:String):String {
		var newString:String = new String("");
		for (var i:Number = 0; i < s.length; i++) {
			//Debugger.log("stringUtils isANumber char at " + i + ": " + s.charCodeAt(i), Debugger.MED, "isANumber", "StringUtils");
			var char:Number = s.charCodeAt(i);
			if (char >= 192 && char <= 197)
				newString += "A";
			else if (char == 199)
				newString += "C";
			else if (char >= 200 && char <= 203)
				newString += "E";
			else if (char >= 204 && char <= 207)
				newString += "I";
			else if (char == 209)
				newString += "N";
			else if ((char >= 210 && char <= 214) || char == 216)
				newString += "O";
			else if (char >= 217 && char <= 220)
				newString += "U";
			else if (char == 221)
				newString += "Y";
			else if (char >= 224 && char <= 229)
				newString += "a";
			else if (char == 231)
				newString += "c";
			else if (char >= 232 && char <= 235)
				newString += "e";
			else if (char >= 236 && char <= 239)
				newString += "i";
			else if (char == 241)
				newString += "n";
			else if ((char >= 242 && char <= 246) || char == 240 || char == 248)
				newString += "o";
			else if (char >= 249 && char <= 252)
				newString += "u";
			else if (char == 253 || char == 255)
				newString += "y";
			else
				newString += s.charAt(i);
				
		}

		//Debugger.log("originalString: " + s + " newString: " + newString, Debugger.MED, "correctPresenceName", "StringUtils");
		return newString;
	}
	
	public static function correctPresenceRoomName(s:String):String {
		var newRoomName:String = new String("");
		for (var i:Number = 0; i < s.length; i++) {
			switch(s.charAt(i)) {
				case " ": {
					newRoomName += "_";
					break;
				}
				case ":": {
					newRoomName += "_";
					break;
				}
				default: {
					newRoomName += s.charAt(i);
				}
			}
		}
		Debugger.log("correctPresenceRoomName originalRoomName: " + s + " correctedRoomName: " + newRoomName, Debugger.MED, "correctPresenceRoomName", "StringUtils");
		return newRoomName;
	}
	
	public static function containsReservedChar(str:String):Boolean {
		for(var i=0; i<reserved.length; i++)
			if(str.indexOf(reserved[i]) != -1) return true;
			
		return false;
	}
}