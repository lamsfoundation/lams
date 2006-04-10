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
 *
 * @author  DI
 * @version 12/04/05
 * 
 */
 class org.lamsfoundation.lams.common.util.CallBack {
    
    private var scope:Object;
    private var fn:Function;
    private var args:Array;
    
    
    function CallBack(scope:Object,fn:Function){
        this.scope = scope;
        this.fn = fn;
        args = [];
        
        //Get arguments from the arguments object ignoring first two as they are scope+function
		var l = arguments.length;
		for (var i = 2; i < l; i++) {
			args[i - 2] = arguments[i];
		}
    }
    
    /**
    * Apply the callback
    */
    public function call(){
        fn.apply(scope,args);
    }
    
    /**
    * Adds additional arguments to args array for dynamic runtime callback modification
    */
    public function addArgs(extraArgs:Array){
        args = args.concat(extraArgs);
    }
}