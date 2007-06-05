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
import org.lamsfoundation.lams.common.mvc.*;
import org.lamsfoundation.lams.authoring.cv.*;
import org.lamsfoundation.lams.monitoring.mv.*;
import org.lamsfoundation.lams.authoring.Activity;
import org.lamsfoundation.lams.authoring.Transition;


/**
 * Provides common services for the "canvas view" of
 * a either Author or Monitor applications.
 */
class org.lamsfoundation.lams.common.CommonCanvasView extends AbstractView {
  private var model:Observable;
  private var controller:Controller;

  public function CommonCanvasView (m:Observable, c:Controller) {
		super(m, c);
  }

  public function highlightActivity(model:Observable){
		Debugger.log('running..',Debugger.CRITICAL,'highlightActivity','CommonCanvasView');
		
		var m = (model instanceof MonitorModel) ? MonitorModel(model) : null;
		m = (model instanceof CanvasModel) ? CanvasModel(model) : m;
		
		if(m != null) {
			
			var ca = CanvasActivity(m.selectedItem);
			var a:Activity = ca.activity;	
		
			// deselect previously selected item
			if(m.prevSelectedItem != null) {
				// if child of an complex activity is previously selected, it is easiest to clear all the children
				if(m.prevSelectedItem.activity.parentUIID != null) {
					var caComplex = m.activitiesDisplayed.get(m.prevSelectedItem.activity.parentUIID);
					caComplex.refreshChildren();
				} else {
					var dca:ICanvasActivity = m.activitiesDisplayed.get(m.prevSelectedItem.activity.activityUIID);
					dca.setSelected(false);
				}
			}
			
			
			//try to cast the selected item to see what we have (instance of des not seem to work)
			if(ICanvasActivity(m.selectedItem) != null){
				Debugger.log('Its a canvas activity',4,'highlightActivity','CommonCanvasView');
				ca.setSelected(true);
			} else if(CanvasTransition(m.selectedItem) != null) {
				var ct = CanvasTransition(m.selectedItem);
				var t:Transition = ct.transition;
				Debugger.log('Its a canvas transition',4,'highlightActivity','CommonCanvasView');
				
			} else {
				Debugger.log('Its a something we dont know',Debugger.CRITICAL,'highlightActivity','CommonCanvasView');
			
			}
			
		}
	}

}