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

import org.lamsfoundation.lams.common.*;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.util.ui.*;
import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.authoring.cv.*;
import org.lamsfoundation.lams.monitoring.mv.*;
import org.lamsfoundation.lams.common.style.*

import com.polymercode.Draw;
import mx.managers.*
import mx.containers.*;
import mx.events.*
import mx.utils.*

/**  
* PlusIcon
*/  
class org.lamsfoundation.lams.monitoring.mv.PlusIcon extends MovieClip {
	
	/* internal instances */
	private var click_mc:MovieClip;
	
	/* constants */
 	public static var ICON_WIDTH:Number = 8;
	public static var ICON_HEIGHT:Number = 20;
	
	/* references */
	private var _monitorController:MonitorController;
	private var _monitorView;
	private var _tm:ThemeManager;
	private var _activity:Activity;
	private var learner:Progress;
	
	private var _isSelected:Boolean;
	private var app:ApplicationParent; 
	private var toolTip:ToolTip;
	
	function PlusIcon(){
		Debugger.log('PlusIcon created',Debugger.MED,'PlusIcon','PlusIcon');
		
		_tm = ThemeManager.getInstance();
		app = ApplicationParent.getInstance();
		
		toolTip = new ToolTip();
		init();
	}
	
	public function init(initObj):Void{
		if(initObj){
			_monitorController = initObj._monitorController;
			_activity = initObj._activity;
			this._x = initObj._x;
			this._y = initObj._y;
		}
	}
}