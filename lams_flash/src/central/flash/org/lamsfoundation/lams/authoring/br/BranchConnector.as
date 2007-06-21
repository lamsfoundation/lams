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

import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.ui.*;
import org.lamsfoundation.lams.common.*;
import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.authoring.cv.*;
import org.lamsfoundation.lams.authoring.br.*;

class org.lamsfoundation.lams.authoring.br.BranchConnector extends CanvasTransition {
	
	private static var DIR_FROM_START:Number = 0;
	private static var DIR_TO_END:Number = 1;
	
	private var _branch:Branch;
	private var _direction:Number;

	function BranchConnector(){
		super();
		
		override_fromAct = (_direction == DIR_FROM_START) ? _canvasBranchView.startHub : null;
		override_toAct = (_direction == DIR_TO_END) ? _canvasBranchView.endHub : null;
	}
	
	public function get branch():Branch{
		return _branch;
	}
	
	public function set branch(b:Branch){
		_branch = b;
		_transition = _branch;
	}
	
	public function get direction():Number {
		return _direction;
	}
}  