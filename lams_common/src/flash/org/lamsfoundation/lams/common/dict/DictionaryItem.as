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

import org.lamsfoundation.lams.common.dict.*

/**
* Holds the metadata + value of a LAMS text component
* @author DI
*/
class DictionaryItem {
    
	private var _key:String;
	private var _description:String;
	private var _value:String;

	public function DictionaryItem(key:String, value:String,description:String) {
		//constructor 
        _key = key;
        _description = description;
        _value = value;
	}
    
    /**
    * Convert to data object for saving
    */
    public function toData():Object{
        var obj:Object = {};
        obj.key = _key;
        obj.description = _description;
        obj.value = _value;
        return obj;
    }
    
    /**
    * Create Dictionary item from dataObject
    */
    public static function createFromData(dataobj:Object):DictionaryItem{
        var dictionaryItem:DictionaryItem =  new DictionaryItem(dataobj.key,dataobj.value,dataobj.description);
        return dictionaryItem;
    }
    
    function get key():String{
        return _key;
    }
    
    function get value():String{
        return _value;
    }
	
	function get description():String{
        return _description;
    }

}