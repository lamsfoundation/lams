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

import org.lamsfoundation.lams.common.mvc.*;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.monitoring.*;
import org.lamsfoundation.lams.monitoring.mv.*;

/**
* Controller for the sequence library
*/
class MonitorController extends AbstractController {
	private var _monitorModel:MonitorModel;
	
	/**
	* Constructor
	*
	* @param   cm   The model to modify.
	*/
	public function MonitorController (cm:Observable) {
		super (cm);
		_monitorModel = MonitorModel(model);
	}
	
	// add control methods
	
}