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
		var canvasView:MovieClip = cv.getCanvasModel().activeView.content;
		
		var iconMouseX = _xmouse - cv.model.getPosition().x;
		var iconMouseY = _ymouse - cv.model.getPosition().y;
		
		Debugger.log("iconMouseX: "+iconMouseX+" iconMouseY: "+iconMouseY, Debugger.GEN, "iconDrop", "ToolkitController");
		
		var optionalOnCanvas:Array  = cv.getCanvasModel().findOptionalActivities();
		
		//SEE IF ITS HIT the canvas
		var isCanvasDrop:Boolean = cv.getCanvasModel().activeView.content.hitTest(dragIcon_mc);
		
		Debugger.log('isCanvasDrop:'+isCanvasDrop,Debugger.GEN,'iconDrop','ToolkitController');
		
		for(var i=0; i<optionalOnCanvas.length; i++){
			var optionalX:Number = optionalOnCanvas[i].activity.xCoord;
			var optionalY:Number = optionalOnCanvas[i].activity.yCoord;
			var optionalWidth:Number = optionalOnCanvas[i]._width;
			var optionalHeight:Number = optionalOnCanvas[i]._height;
			
			if(iconMouseX >= optionalX && iconMouseX <= (optionalX + optionalWidth)){
				if(iconMouseY >= optionalY && iconMouseY <= (optionalY + optionalHeight)){
					isCanvasDrop = false;
					dragIcon_mc.removeMovieClip();
					
					var ta:TemplateActivity = _toolkitModel.getSelectedTemplateActivity();
					
					if(optionalOnCanvas[i].locked){
						var msg:String = (!optionalOnCanvas[i].activity.isSequenceBased) ? Dictionary.getValue('act_lock_chk') : Dictionary.getValue('act_seq_lock_chk');
						LFMessage.showMessageAlert(msg);
					} else if(optionalOnCanvas[i].activity.isSequenceBased && optionalOnCanvas[i].activity.noSequences <= 0) {
						var msg:String = Dictionary.getValue('ta_iconDrop_optseq_error_msg');
						LFMessage.showMessageAlert(msg);
					} else {
						if(optionalOnCanvas[i].activity.isSequenceBased) {
							// test mouse ptr 
							var _children:Array = optionalOnCanvas[i].children;
							var sequenceDropUIID:Number = null;
							var sequence = null;
							var mouseX = iconMouseX - optionalX;
							var mouseY = iconMouseY - optionalY;
							
							for(var j=0; j<_children.length; j++) {
								if(mouseX >= _children[j].activity.xCoord && 
								   mouseX <= (_children[j].activity.xCoord + _children[j]._width) &&
								   mouseY >= _children[j].activity.yCoord && 
								   mouseY <= (_children[j].activity.yCoord + _children[j]._height)) {
									sequenceDropUIID = _children[j].activity.activityUIID;
									sequence = _children[j];
									break;
								}
							}
							
							if(sequenceDropUIID != null && sequence != null) {
								cv.setDroppedTemplateActivity(ta, sequenceDropUIID, sequence);
							} else {
								var msg:String = Dictionary.getValue('activityDrop_optSequence_error_msg');
								LFMessage.showMessageAlert(msg);
							}
										
						} else { cv.setDroppedTemplateActivity(ta, optionalOnCanvas[i].activity.activityUIID); }
					}
				}					
			}			
		}
		
		//remove the drag icon
		dragIcon_mc.removeMovieClip();
		
		if(isCanvasDrop){
			var ta:TemplateActivity;
			ta = _toolkitModel.getSelectedTemplateActivity();			Debugger.log('ta:'+ta.toolActivity.title,4,'canvasDrop','ToolkitController');		
			cv.setDroppedTemplateActivity(ta);
		}
	}
}
