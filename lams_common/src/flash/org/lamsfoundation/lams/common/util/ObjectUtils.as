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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ************************************************************************
 */

import org.lamsfoundation.lams.common.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.comms.*

/**  
* Recursivly prints out all the values in an object
* 
* 
*/  
class org.lamsfoundation.lams.common.util.ObjectUtils{
	

	
	/**
	* Recursively goes through and object and prints out property values
	* @usage   printObject(_global);
	* @param   target 
	* @return  
	*/
	public static function printObject(target):String{  
		var str:String = "";
		for(var o in target){  
			//trace('object:' + o  + ' value:' + target[o]);  
			
			
			Debugger.log(o  + ':' + target[o],Debugger.GEN,'printObject','org.lamsfoundation.lams.common.util.ObjectUtils');
			str += o  + ':' + target[o];
			printObject(target[o]);  
		}  
		
		return str;  
	}
	
	/**
	* Recursively goes through and object and returns string out property values
	* @usage   printObject(_global);
	* @param   target 
	* @return  
	*/
	public static function toString(target):String{ 
		var str:String = "";
		for(var o in target){  
			//trace('object:' + o  + ' value:' + target[o]);  
			
			
			//Debugger.log(o  + ':' + target[o],Debugger.GEN,'printObject','org.lamsfoundation.lams.common.util.ObjectUtils');
			str += '\n'+ o  + ':' + target[o];
			toString(target[o]);  
		}  
		return str;  
	}
	
	public static function deNull(obj:Object):Object{
		Debugger.log('FOR:'+printObject(obj),Debugger.GEN,'deNull','org.lamsfoundation.lams.common.util.ObjectUtils');
			//Removes null values in objects suitable for sending XML to the Java WDDX deserialiser
		
		if (obj instanceof Array) {
			for (var i = 0; i<obj.length; i++) {
				if(typeof (obj[i]) == "object"){
					obj[i].deNull();
				}else if (obj[i] == null || obj[i] == undefined){
					obj[i] = replaceNullValues(obj[i]);
				}
			}
		} else {
			for (var i in obj) {
				if(typeof (obj[i]) == "object"){
					obj[i].deNull();
				}else if (obj[i] == null || obj[i] == undefined){
					obj[i] = replaceNullValues(obj[i]);
				}
			}
		}
		Debugger.log('Returning:'+printObject(obj),Debugger.GEN,'deNull','org.lamsfoundation.lams.common.util.ObjectUtils');
		return obj;

	}
	
	private static function replaceNullValues(val){
		//Debugger.log('Communication.STRING_NULL_VALUE:'+Communication.STRING_NULL_VALUE,Debugger.GEN,'deNull','org.lamsfoundation.lams.common.util.ObjectUtils');

		switch(typeof(val)){
			
			case "string":
				val = Config.STRING_NULL_VALUE;
				return val;
				break;
			case "number":
				val = Config.NUMERIC_NULL_VALUE;
				return val;
				break;
			case "date":
				val = Config.DATE_NULL_VALUE;
				return val;
				break;
			case "boolean":
				val = Config.BOOLEAN_NULL_VALUE;
				return val;
				break;
			case "null":
				val = Config.STRING_NULL_VALUE;
				return val;
				break;
			case "undefined":
				val = Config.STRING_NULL_VALUE;
				return val;
				break;
			default:
				val = Config.STRING_NULL_VALUE;
				return val;
			
						
		
		}
		
	}
	
	
}
