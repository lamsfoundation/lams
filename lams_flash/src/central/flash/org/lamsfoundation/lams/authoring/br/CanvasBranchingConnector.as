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
import org.lamsfoundation.lams.authoring.br.*;
import org.lamsfoundation.lams.monitoring.mv.*;
import org.lamsfoundation.lams.monitoring.mv.tabviews.LearnerTabView;
import org.lamsfoundation.lams.common.style.*
import mx.controls.*
import com.polymercode.Draw
import mx.managers.*
import mx.containers.*
import mx.events.*
import mx.utils.*

/**  
* CanvasBranchingConnector
*/  
class CanvasBranchingConnector extends CanvasActivity { 

	function CanvasBranchingConnector(){
		super(true);
		branchConnector = true;
	}	
	
	private function onPress():Void{
		if (_module == "monitoring"){
			Debugger.log('SingleClicking:+'+this,Debugger.GEN,'onPress','CanvasActivity for monitoring');
			_monitorController.activityClick(this);
		}else {
			Debugger.log('SingleClicking:+'+this,Debugger.GEN,'onPress','CanvasActivity');
			_canvasController.activityClick(this);
		}
	}
	
	
}