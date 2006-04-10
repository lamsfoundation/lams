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

import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.mvc.*;
/**
* Provides basic services for the "controller" of a Model/View/Controller triad.
*/
class org.lamsfoundation.lams.common.mvc.AbstractController implements Controller {
	private var model : Observable;
	private var view : View;
	/**
	* Constructor
	*
	* @param   m   The model this controller's view is observing.
	*/
	public function AbstractController (m:Observable){
		// Set the model.
		setModel (m);
	}
	/**
	* Sets the model for this controller.
	*/
	public function setModel (m : Observable):Void 	{
		model = m;
	}
	/**
	* Returns the model for this controller.
	*/
	public function getModel () : Observable {
		return model;
	}
	/**
	* Sets the view that this controller is servicing.
	*/
	public function setView (v : View) : Void {
		view = v;
	}
	/**
	* Returns this controller's view.
	*/
	public function getView () : View {
		return view;
	}
}
