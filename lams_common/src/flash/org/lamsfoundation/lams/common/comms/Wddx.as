/*
Wddx Serializer/Deserializer for Flash MX 2004 v1.0
Coded to work with ActionScript 2.0
-------------------------------------------------- 
	Created by 
		Branden Hall (bhall@waxpraxis.org)
		Dave Gallerizzo (dgallerizzo@figleaf.com)

	Based on code by
		Simeon Simeonov (simeons@allaire.com)
		Nate Weiss (nweiss@icesinc.com)

	Version History:
		8/10/2000 - First created
		9/5/2000  - Minor bug fix to the deserializer
		9/15/2000 - Commented out Wddxserializetype creation in the 
					 deserializer as they are not needed in most cases.
		9/21/2000 - Simplified the use of the deserializer. No longer need
		            to create the XML object yourself and the load and
					onLoad methods are part of the Wddx class.
		9/30/2000 - Cleaned up code and removed load and onLoad methods.
					Updated sample code.
		12/28/2000 - Added new duplicate method to WddxRecordset object
		1/10/2001 - Fixed problem where serialization caused text to always drop to lower
					case. Thanks to Bill Tremblay for spotting this one!
		1/17/2001 - Minor update to the serializer code so that it properly adds text nodes.
					Thanks for the catch from Carlos Mathews! 
					Also, the deserialization of primitive types now results in primitives 
					rather than instances of the object wrapper
		1/20/2001 - Quick fix to the deserializer for recordsets so that for..in loops can get
					the elements of the recordset in the proper order.
		2/5/2001  - Fix to the string deserialization so that it handles special characters 
					properly, thanks to Spike Washburn for this one!
		11/9/2001 - Finished small optimizations, fixed encoding issues and fixed case preservation
					issue.
		11/16/01  - (bkruse@macromedia.com)- put all Wddx classes in Object.Wddx namespace to fix
					scoping issues.
		4/19/2002 - Fixed various small bugs with date and number serialization/deserialization
		4/19/2002 - Removed Wddx classes from Wddx namespace and put into _global namespace
		9/11/2003 - Converted to AS 2.0 by Jobe Makar. Divided into two classes: Wddx and WddxRecordset
	Authors notes: 
		Serialization:	
			- Create an instance of the Wddx class
			- Call the serialize method, passing it the object your wish
			  to serialize, it will return an XML object filled with the
			  serialized object.


			Example:
				var myXML:XML;
				var foo:Wddx = new Wddx();
				myXML = foo.serialize(bar);
	
			 
		Deserializtion:
			- Get the XML you want to deserialize
			- Create an instance of the Wddx class
			- Call the serialize method of your Wddx
			  object and pass it your XML. It will return
			  the deserialized object.

			Example:
				var myXML:XML = new XML();
				//
				// XML is loaded here
				//
				var foo:Wddx = new Wddx();
				var myObj:Object = foo.deserialize(myXML);

			
		- Branden 9/30/00

*/

import org.lamsfoundation.lams.common.comms.*

class org.lamsfoundation.lams.common.comms.Wddx {
	// Build some tables needed for CDATA encoding
	var et:Object = new Object();
	var etRev:Object = new Object();
	var at:Object = new Object();
	var atRev:Object = new Object();
	var timezoneString:String;
	var preserveVarCase:Boolean = true;
	var useTimeZoneInfo:Boolean = true;
	var packet:XML;
	var wddxPacket:XMLNode;
	//var useTimeZoneInfo:String;
	var tzOffset:Number;
	function Wddx() {
		for (var i = 0; i<256; ++i) {
			if (i<32 && i != 9 && i != 10 && i != 13) {
				var hex = i.toString(16);
				if (hex.length == 1) {
					hex = "0"+hex;
				}
				et[i] = "<char code='"+hex+"'/>";
				at[i] = "";
			} else if (i<128) {
				et[i] = chr(i);
				at[i] = chr(i);
			} else { 
				et[i] = "&#x"+i.toString(16)+";";
				etRev["&#x"+i.toString(16)+";"] = chr(i);
				at[i] = "&#x"+i.toString(16)+";";
				atRev["&#x"+i.toString(16)+";"] = chr(i);
			}
		}
		et[ord("<")] = "&lt;";
		et[ord(">")] = "&gt;";
		et[ord("&")] = "&amp;";
		etRev["&lt;"] = "<";
		etRev["&gt;"] = ">";
		etRev["&amp;"] = "&";
		at[ord("<")] = "&lt;";
		at[ord(">")] = "&gt;";
		at[ord("&")] = "&amp;";
		at[ord("'")] = "&apos;";
		at[ord("\"")] = "&quot;";
		atRev["&lt;"] = "<";
		atRev["&gt;"] = ">";
		atRev["&amp;"] = "&";
		atRev["&apos;"] = "'";
		atRev["&quot;"] = "\"";
		// Deal with timezone offsets
		tzOffset = (new Date()).getTimezoneOffset();
		if (tzOffset>=0) {
			timezoneString = "-";
		} else {
			timezoneString = "+";
		}
		timezoneString += Math.floor(Math.abs(tzOffset)/60)+":"+(Math.abs(tzOffset)%60);
	}
	// Serialize a Flash object
	function serialize(rootObj) {
		delete wddxPacket;
		var temp:XML = new XML();
		packet = new XML();
		packet.appendChild(temp.createElement("wddxPacket"));
		wddxPacket = packet.firstChild;
		wddxPacket.attributes["version"] = "1.0";
		wddxPacket.appendChild(temp.createElement("header"));
		wddxPacket.appendChild(temp.createElement("data"));
		if (serializeValue(rootObj, wddxPacket.childNodes[1])) {
			return packet;
		} else {
			return null;
		}
	}
	// Determine the type of a Flash object and serialize it
	function serializeValue(obj, node) {
		var bSuccess:Boolean = true;
		var val:String = obj.valueOf();
		var tzString = null;
		var temp:XML = new XML();
		//  null object
		if (obj == null) {
			node.appendChild(temp.createElement("null"));
			// string object
		} else if (typeof (val) == "string") {
			serializeString(val, node);
			//  numeric objects (number or date)
		} else if (typeof (val) == "number") {
			//  date object
			if (typeof (obj.getTimezoneOffset) == "function") {
				//  deal with timezone offset if asked to
				if (useTimeZoneInfo) {
					tzString = timezoneString;
				}
				node.appendChild(temp.createElement("dateTime"));
				node.lastChild.appendChild(temp.createTextNode(obj.getFullYear()+"-"+(obj.getMonth()+1)+"-"+obj.getDate()+"T"+obj.getHours()+":"+obj.getMinutes()+":"+obj.getSeconds()+tzString));
				//  number object
			} else {
				node.appendChild((new XML()).createElement("number"));
				node.lastChild.appendChild((new XML()).createTextNode(val));
			}
			//  boolean object
		} else if (typeof (val) == "boolean") {
			node.appendChild(temp.createElement("boolean"));
			node.lastChild.attributes["value"] = val;
			//  actual objects
		} else if (typeof (obj) == "object") {
			//  if it has a built in serializer, use it
			if (typeof (obj.wddxSerialize) == "function") {
				bSuccess = obj.wddxSerialize(this, node);
				//  array object
			} else if (typeof (obj.join) == "function" && typeof (obj.reverse) == "function") {
				node.appendChild(temp.createElement("array"));
				node.lastChild.attributes["length"] = obj.length;
				for (var i:Number = 0; bSuccess && i<obj.length; ++i) {
					bSuccess = serializeValue(obj[i], node.lastChild);
				}
				//  generic object
			} else {
				node.appendChild(temp.createElement("struct"));
				if (typeof (obj.wddxSerializationType) == "string") {
					node.lastChild.attributes["type"] = obj.wddxSerializationType;
				}
				for (var prop in obj) {
					if (prop != "wddxSerializationType") {
						bSuccess = serializeVariable(prop, obj[prop], node.lastChild);
						if (!bSuccess) {
							break;
						}
					}
				}
			}
		} else {
			//  Error: undefined values or functions
			bSuccess = false;
		}
		//  Successful serialization
		return bSuccess;
	}
	// Serialize a Flash varible
	function serializeVariable(name:String, obj:Object, node:XMLNode) {
		var bSuccess = true;
		var temp = new XML();
		if (typeof (obj) != "function") {
			node.appendChild(temp.createElement("var"));
			node.lastChild.attributes["name"] = preserveVarCase ? this.serializeAttr(name) : this.serializeAttr(name.toLowerCase());
			
		bSuccess = this.serializeValue(obj, node.lastChild);
		}
		return bSuccess;
	}
	// Serialize a Flash String
	function serializeString(s:String, node:XMLNode) {
		var tempString:String = "";
		var temp:XML = new XML();
		var max:Number = s.length;
		node.appendChild(temp.createElement("string"));
		for (var i:Number = 0; i<max; ++i) {
			var char:String = substring(s, i+1, 1);
			var ord:String = ord(substring(s, i+1, 1));
			if(ord < 256) {
				tempString += (this.et[ord(substring(s, i+1, 1))]);
			} else {
				tempString += "&#" + ord + ";";
			}
		}
		node.lastChild.appendChild(temp.createTextNode(tempString));
	}
	// Serialize attributes of a Flash variable
	function serializeAttr(s:String) {
		var tempString:String = "";
		var max:Number = s.length;
		for (var i:Number = 0; i<max; ++i) {
			tempString += (this.at[ord(substring(s, i+1, 1))]);
		}
		return (tempString);
	}
	// wddx deserializer
	function deserialize(wddxPacket) {
		if (typeof (wddxPacket) != "object") {
			wddxPacket = new XML(wddxPacket);
		}
		var wddxRoot:XML = new XML();
		var wddxChildren:Array = new Array();
		var temp:Number;
		var dataObj:Object = new Object();
		// Get first node that is not Null
		while (wddxPacket.nodeName == null) {
			wddxPacket = wddxPacket.firstChild;
		}
		wddxRoot = wddxPacket;
		if (wddxRoot.nodeName.toLowerCase() == "wddxpacket") {
			wddxChildren = wddxRoot.childNodes;
			temp = 0;
			// dig down until we find the data node or run out of nodes
			while (wddxChildren[temp].nodeName.toLowerCase() != "data" && temp<wddxChildren.length) {
				++temp;
			}
			// if we found a data node then deserialize its contents
			var i=0;
			//trace('temp:'+temp);
			//trace('wddxChildren.length:'+wddxChildren.length);
			if (temp<wddxChildren.length) {
				dataObj = this.deserializeNode(wddxChildren[temp].firstChild);
				i++;
				//trace('i:'+i);
				return dataObj;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	// deserialize a single node of a WDDX packet
	function deserializeNode(node:XMLNode) {
		// get the name of the node
		var nodeType:String = node.nodeName.toLowerCase();
		//trace('nodeType:'+nodeType);
		// number node 
		if (nodeType == "number") {
			var dataObj:Object = node.firstChild.nodeValue;
			//	dataObj.wddxSerializationType = "number";
			return Number(dataObj);
			// boolean node
		} else if (nodeType == "boolean") {
			var dataObj:Boolean = (String(node.attributes.value).toLowerCase() == "true");
			//	dataObj.wddxSerializationType = "boolean";
			return dataObj;
			// string node
		} else if (nodeType == "string") {
			var dataObj:Object;
			if (node.childNodes.length>1) {
				dataObj = "";
				var i:Number = 0;
				for (i=0; i<node.childNodes.length; i++) {
					if (node.childNodes[i].nodeType == 3) {
						//this is a text node
						dataObj = dataObj+this.deserializeString(node.childNodes[i].nodeValue);
					} else if (node.childNodes[i].nodeName == 'char') {
						dataObj += chr(parseInt(node.childNodes[i].attributes["code"], 16));
					}
				}
			} else {
				dataObj = this.deserializeString(node.firstChild.nodeValue);
			}
			//	dataObj.wddxSerializationType = "string";
			return dataObj;
			// array node
		} else if (nodeType == "array") {
			var dataObj:Array = new Array();
			var temp:Number = 0;
			for (var i:Number = 0; i<node.attributes["length"]; ++i) {
				dataObj[i] = this.deserializeNode(node.childNodes[i].cloneNode(true));
			}
			//	dataObj.wddxSerializationType = "array";
			return dataObj;
			// datetime node
		} else if (nodeType == "datetime") {
			var dtString:Object = node.firstChild.nodeValue;
			var tPos:Number = dtString.indexOf("T");
			var tzPos:Number = dtString.indexOf("+");
			var dateArray:Array = new Array();
			var timeArray:Array = new Array();
			var tzArray:Array = new Array();
			var dataObj:Date = new Date();
			if (tzPos == -1) {
				tzPos = dtString.lastIndexOf("-");
				if (tzPos<tPos) {
					tzPos = -1;
				}
			}
			// slice the datetime node value into the date, time, and timezone info
			dateArray = (dtString.slice(0, tPos)).split("-");
			timeArray = (dtString.slice(tPos+1, tzPos)).split(":");
			tzArray = (dtString.slice(tzPos)).split(":");
			// set the time and date of the object
			dataObj.setFullYear(parseInt(dateArray[0]), parseInt(dateArray[1])-1, parseInt(dateArray[2]));
			dataObj.setHours(parseInt(timeArray[0]), parseInt(timeArray[1]));
			// deal with timezone offset if there is one
			if (tzPos != -1) {
				tzOffset = parseInt(tzArray[0])*60+parseInt(tzArray[1]);
				dataObj.setMinutes(dataObj.getMinutes()-(dataObj.getTimezoneOffset()+tzOffset));
			}
			//	dataObj.wddxSerializationType = "datetime";
			return dataObj;
			// struct node
		} else if (nodeType == "struct") {
			var dataObj:Object = new Object();
			//trace('struct node.childNodes.length:'+node.childNodes.length);
			for (var i = 0; i<node.childNodes.length; i++) {
				if (node.childNodes[i].nodeName.toLowerCase() == "var") {
					dataObj[this.deserializeAttr(node.childNodes[i].attributes["name"])] = this.deserializeNode(node.childNodes[i].firstChild);
				}
			}
			//	dataObj.wddxSerializationType = "struct";
			return dataObj;
			// recordset node
		} else if (nodeType == "recordset") {
			var dataObj = new WddxRecordset((node.attributes["fieldNames"]).split(",").reverse(), parseInt(node.attributes["rowCount"]));
			for (var i = (node.childNodes.length-1); i>=0; i--) {
				if (node.childNodes[i].nodeName.toLowerCase() == "field") {
					var attr:Object = this.deserializeAttr(node.childNodes[i].attributes["name"]);
					dataObj[attr].wddxSerializationType = "field";
					for (var j = (node.childNodes[i].childNodes.length-1); j>=0; j--) {
						dataObj[attr][j] = new Object();
						var tempObj:Object = this.deserializeNode(node.childNodes[i].childNodes[j]);
						dataObj.setField(j, attr, tempObj);
					}
				}
			}
			//	dataObj.wddxSerializationType = "recordset";
			return dataObj;
		}
	}
	function deserializeAttr(attr) {
		var max:Number = attr.length;
		var i:Number = 0;
		var char:String;
		var output:String = "";
		while (i<max) {
			char = substring(attr, i+1, 1);
			if (char == "&") {
				var buff = char;
				do {
					char = substring(attr, i+1, 1);
					buff += char;
					++i;
				} while (char != ";");
				output += this.atRev[buff];
			} else {
				output += char;
			}
			++i;
		}
		return output;
	}
	function deserializeString(str) {
		var max:Number = str.length;
		var i:Number = 0;
		var char:String;
		var output:String = "";
		while (i<max) {
			char = substring(str, i+1, 1);
			if (char == "&") {
				var buff:String = char;
				do {
					++i;
					char = substring(str, i+1, 1);
					buff += char;
				} while (char != ";");
				output += this.etRev[buff];
			} else {
				output += char;
			}
			++i;
		}
		//trace(output);
		return output;
	}
}
