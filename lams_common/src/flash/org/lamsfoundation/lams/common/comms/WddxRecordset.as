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
 
import org.lamsfoundation.lams.common.comms.*
class org.lamsfoundation.lams.common.comms.WddxRecordset {
	//  Add default properties
	var preserveFieldCase:Boolean = true;
	//  Add extensions
	/*
		if (typeof (wddxRecordsetExtensions) == "object") {
			for (prop in wddxRecordsetExtensions) {
				//  Hook-up method to WddxRecordset object
				this[prop] = wddxRecordsetExtensions[prop];
			}
		}
		*/
	//  Perfom any needed initialization
	function WddxRecordset(arguments) {
		if (arguments.length>0) {
			var val;
			if (typeof (val=arguments[0].valueOf()) == "boolean") {
				//  Case preservation flag is provided as 1st argument
				preserveFieldCase = arguments[0];
			} else {
				//  First argument is the array of column names
				var cols = arguments[0];
				//  Second argument could be the length or the preserve case flag
				var nLen = 0;
				if (arguments.length>1) {
					if (typeof (val=arguments[1].valueOf()) == "boolean") {
						//  Case preservation flag is provided as 2nd argument
						preserveFieldCase = arguments[1];
					} else {
						//  Explicitly specified recordset length
						nLen = arguments[1];
						if (arguments.length>2) {
							//  Case preservation flag is provided as 3rd argument
							preserveFieldCase = arguments[2];
						}
					}
				}
				for (var i:Number = 0; i<cols.length; ++i) {
					var colValue:Array = new Array(nLen);
					for (var j:Number = 0; j<nLen; ++j) {
						colValue[j] = null;
					}
					this[preserveFieldCase ? cols[i] : cols[i].toLowerCase()] = colValue;
				}
			}
		}
	}
	//  duplicate() returns a new copy of the current recordset
	function duplicate() {
		var copy:WddxRecordset = new WddxRecordset();
		for (var i in this) {
			if (i.toUpperCase() == "PRESERVEFIELDCASE") {
				copy[i] = this[i];
			} else {
				if (this[i].isColumn()) {
					copy.addColumn(i);
					for (var j in this[i]) {
						copy.setField(j, i, this.getField(j, i));
					}
				}
			}
		}
		return (copy);
	}
	//  isColumn(name) returns true/false based on whether this is a column name
	function isColumn(name) {
		return (typeof (this[name]) == "object" && name.indexOf("_private_") == -1);
	}
	//  getRowCount() returns the number of rows in the recordset
	function getRowCount() {
		var nRowCount:Number = 0;
		for (var col in this) {
			if (this.isColumn(col)) {
				nRowCount = this[col].length;
				break;
			}
		}
		return nRowCount;
	}
	//  addColumn(name) adds a column with that name and length == getRowCount()
	function addColumn(name:String) {
		var nLen = this.getRowCount();
		var colValue = new Array(nLen);
		for (var i = 0; i<nLen; ++i) {
			colValue[i] = null;
		}
		this[this.preserveFieldCase ? name : name.toLowerCase()] = colValue;
	}
	//  addRows() adds n rows to all columns of the recordset
	function addRows(n:Number) {
		for (var col in this) {
			if (isColumn(col)) {
				var nLen = this[col].length;
				for (var i:Number = nLen; i<nLen+n; ++i) {
					this[col][i] = "";
				}
			}
		}
	}
	//  getField() returns the element in a given (row, col) position
	function getField(row, col) {
		return this[preserveFieldCase ? col : col.toLowerCase()][row];
	}
	//  setField() sets the element in a given (row, col) position to value
	function setField(row, col, value) {
		this[preserveFieldCase ? col : col.toLowerCase()][row] = value;
	}
	//  wddxSerialize() serializes a recordset
	//  returns true/false
	function wddxSerialize(serializer, node) {
		var colNamesList:String = "";
		var colNames:Array = new Array();
		var i:Number = 0;
		for (var col in this) {
			if (isColumn(col)) {
				colNames[i++] = col;
				if (colNamesList.length>0) {
					colNamesList += ",";
				}
				colNamesList += col;
			}
		}
		var nRows:Number = this.getRowCount();
		node.appendChild((new XML()).createElement("recordset"));
		node.lastChild.attributes["rowCount"] = nRows;
		node.lastChild.attributes["fieldNames"] = colNamesList;
		var bSuccess:Boolean = true;
		for (i=0; bSuccess && i<colNames.length; i++) {
			var name = colNames[i];
			node.lastChild.appendChild((new XML()).createElement("field"));
			node.lastChild.lastChild.attributes["name"] = name;
			for (var row:Number = 0; bSuccess && row<nRows; row++) {
				bSuccess = serializer.serializeValue(this[name][row], node.lastChild.lastChild);
			}
		}
		return bSuccess;
	}
}
