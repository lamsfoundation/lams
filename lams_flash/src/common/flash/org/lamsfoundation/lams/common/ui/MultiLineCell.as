/***************************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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
import org.lamsfoundation.lams.common.util.Debugger;
import org.lamsfoundation.lams.common.util.ObjectUtils;
import org.lamsfoundation.lams.common.util.StringUtils;
import mx.controls.DataGrid;
import mx.controls.Label;

class org.lamsfoundation.lams.common.ui.MultiLineCell extends mx.core.UIComponent
{
	var multiLineLabel; 
	var owner; // row that contains the cell
	var listOwner; // List that contains the cell

	function MultiLineCell() {
	}

	function createChildren():Void {
		var c = multiLineLabel = createLabel("multiLineLabel", 10);
		c.styleName = listOwner;
		c.selectable = false;
		c.tabEnabled = false;
		c.border = false;
		c.multiline = true;
		c.wordWrap = true;
	}

	function size():Void {
		var c = multiLineLabel;

		c._width = __width;
		c._height = __height;
	}

	function getPreferredHeight():Number {
		return owner.__height - 4;
	}

	function setValue(suggested:String, item:Object, selected:Boolean):Void {
		multiLineLabel.text = suggested;
	}

	function getPreferredWidth() {
		return owner.__width;
	}
}

