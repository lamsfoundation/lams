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

import org.lamsfoundation.lams.common.mvc.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.authoring.*
import org.lamsfoundation.lams.authoring.cv.*
import org.lamsfoundation.lams.authoring.tk.*
/**
* Controller for the toolkit
*/
class ToolkitController extends AbstractController {
	private var _toolkitModel:ToolkitModel;
	/**
	* Constructor
	*
	* @param   cm   The model to modify.
	*/
	public function ToolkitController (cm:Observable) {		super (cm);
		_toolkitModel = ToolkitModel(model);
		//Debugger.log('fINISHED',4,'Constructor','ToolkitController');
	}
    
	/**
	*Called by ToolkitActivity when one in clicked
	* @param toolkitActivity - the toolkitActivity that was clicked
	*/
	public function selectTemplateActivity(templateActivity:TemplateActivity):Void{
		//Debugger.log('templateActivity:'+templateActivity.getTemplateActivityData().title,4,'selectTemplateActivity','ToolkitController');
		//_global.breakpoint();
		_toolkitModel = ToolkitModel(model);
		_toolkitModel.setSelectedTemplateActivity(templateActivity);
	}
	
	/**
	*Called by the view when a template activity icon is dropped
	*/
	public function iconDrop(dragIcon_mc:MovieClip):Void{
		//lets do a test to see if we got the canvas
		var cv:Canvas = Application.getInstance().getCanvas();
		var canvasView:MovieClip = cv.getCanvasView().getViewMc();
	
		//SEE IF ITS HIT the canvas
		var isCanvasDrop:Boolean = canvasView.hitTest(dragIcon_mc);
		Debugger.log('isCanvasDrop:'+isCanvasDrop,4,'dropIcon','TemplateActivity');
		if(isCanvasDrop){			//remove the drag icon
			dragIcon_mc.removeMovieClip();
			
			var ta:TemplateActivity;
			ta = _toolkitModel.getSelectedTemplateActivity();			Debugger.log('ta:'+ta.toolActivity.title,4,'canvasDrop','ToolkitController');		
			cv.setDroppedTemplateActivity(ta);
		}
		
				
	
	
	
	
	}
	
	
	
	
       
}
