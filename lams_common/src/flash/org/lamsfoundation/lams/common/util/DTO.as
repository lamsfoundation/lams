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

/**
* DTO  Generic data transfer obj
*/
class DTO 
{
	//Declarations
	//signifies update or errrr another type
	public var _type : String;
	public var _body : Object;
	//Constructor
	function DTO (t : String, b : Object)
	{
		_type = t;
		_body = b;
	}
	//Getters+Setters
	public function get type () : String
	{
		return _type;
	}
	public function get _body () : Object
	{
		return body;
	}
	public function set type (t : String) : Void
	{
		_type = t;
	}
	
		public function set body (b : Object) : Void
	{
		_body = b;
	}
}
