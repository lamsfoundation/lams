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

import org.lamsfoundation.lams.common.ApplicationParent;
import org.lamsfoundation.lams.common.layout.*;

/**
 * Specifies the minimum services that a layout manager must provide
 */
interface org.lamsfoundation.lams.common.layout.ILayoutManager {
   
	/**
	* Sets the target.
	* 
	*
	*/
  
	public function setup(target:ApplicationParent):Void;
  
	public function getLayoutName():String;
  
	/**
	* Adds a layout item (movieclip component) to the array.
	*/
	public function addLayoutItem(a:LFLayoutItem);

	public function setEmptyLayoutItem(a:LFLayoutItem);

	public function completedLayout():Boolean;

	/**
	* Removes a layout item (movieclip component) from the array.
	*/
	public function removeLayoutItem(a:LFLayoutItem);


	/** 
	* Moves the position of the layout item.
	* @param   x 
	* @param   y 
	*/
	
	public function moveLayoutItem(a:Object, x:Number, y:Number);


	public function resize(w:Number, h:Number);
	
}