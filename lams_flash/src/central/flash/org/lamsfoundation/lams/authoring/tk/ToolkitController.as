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

import org.lamsfoundation.lams.common.mvc.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.dict.*
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
		var canvasView:MovieClip = cv.getCanvasView().content;
		var iconMouseX = _xmouse - cv.model.getPosition().x;
		var iconMouseY = _ymouse - cv.model.getPosition().y;
		trace("iconMouseX: "+iconMouseX+" and iconMouseY: "+iconMouseY)
		var optionalOnCanvas:Array  = cv.getCanvasModel().findOptionalActivities();
		//SEE IF ITS HIT the canvas
		var isCanvasDrop:Boolean = canvasView.hitTest(dragIcon_mc);
		//var isOptionalDrop:Boolean;
		Debugger.log('isCanvasDrop:'+isCanvasDrop,4,'dropIcon','TemplateActivity');
		for (var i=0; i<optionalOnCanvas.length; i++){
			var optionalX:Number = optionalOnCanvas[i].activity.xCoord;
			var optionalY:Number = optionalOnCanvas[i].activity.yCoord;
			var optionalWidth:Number = optionalOnCanvas[i]._width
			var optionalHeight:Number = optionalOnCanvas[i]._height
			trace("optional xPos: "+optionalX+", optional yPos: "+optionalY+  ", width: "+optionalWidth+" and height: "+optionalHeight)
			if (iconMouseX >= optionalX && iconMouseX <= (optionalX + optionalWidth)){
				if (iconMouseY >= optionalY && iconMouseY <= (optionalY + optionalHeight)){
					isCanvasDrop = false;
					dragIcon_mc.removeMovieClip();
					trace("optional Container is hitted")
					if (optionalOnCanvas[i].locked){
						var msg:String = Dictionary.getValue('act_lock_chk');
						LFMessage.showMessageAlert(msg);
					}else{
						trace("hit with optional")
						var ta:TemplateActivity;
						ta = _toolkitModel.getSelectedTemplateActivity();
						cv.setDroppedTemplateActivity(ta, optionalOnCanvas[i].activity.activityUIID);
					}
				}					
			}			
		}
		if(isCanvasDrop){			//remove the drag icon
			dragIcon_mc.removeMovieClip();
			
			var ta:TemplateActivity;
			ta = _toolkitModel.getSelectedTemplateActivity();			Debugger.log('ta:'+ta.toolActivity.title,4,'canvasDrop','ToolkitController');		
			cv.setDroppedTemplateActivity(ta);
		}
	}
}
