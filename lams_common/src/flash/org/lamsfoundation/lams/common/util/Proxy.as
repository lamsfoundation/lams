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

import org.lamsfoundation.lams.common.util.*




/**
 * The proxy class, contains 1 static method
 *          Dave
 * @version 1.0
 * @since   
 */
class org.lamsfoundation.lams.common.util.Proxy {
	
	
	
	/**
	 * Creates a function that executes in the scope passed in as target, 
	 * not the scope it is actually executed in.  
	 * Like MMs delegate function but can accept parameters and pass them onto 
	 * the function
	 * @usage   
	 * @param   oTarget   the scope the function should execute in
	 * @param   fFunction the function to execute, followed by any other parameters to pass on.
	 * @return  
	 */
	public static function create (oTarget:Object, fFunction:Function):Function	{
		var parameters:Array = new Array ();
		var l = arguments.length;
		
		for (var i = 2; i < l; i++) {
			parameters[i - 2] = arguments[i];
		}
		
		var fProxy:Function = function (){
			var totalParameters:Array = arguments.concat (parameters);
			fFunction.apply (oTarget, totalParameters);
		};
		return fProxy;
	}
}
