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

import org.lamsfoundation.lams.authoring.tb.ToolbarModel
import org.lamsfoundation.lams.common.mvc.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.authoring.*


/*
* Makes changes to the Canvas Authoring model's data based on user input.
*/
class org.lamsfoundation.lams.authoring.tb.ToolbarController extends AbstractController {
	/**
	* Constructor
	*
	* @param   cm   The model to modify.
	*/
	private var _app:Application;
	
	public function ToolbarController (cm:Observable) {
		super (cm);
		_app = Application.getInstance();
	}
    
    
    
	/**
	 * Recieves the click events from the Toolbar buttons.  Based on the label
	 * the relevent method is called to action the user request
	 * @param   evt 
	 */
	public function click(evt):Void{
		Debugger.log('click evt.target.label:'+evt.target.label,Debugger.GEN,'click','ToolbarController');
		var tgt:String = new String(evt.target);
		
		if(tgt.indexOf("new") != -1){
			_app.getCanvas().clearCanvas(false);
		
		}else if(tgt.indexOf("open") != -1){
			_app.getCanvas().openDesignBySelection();
		}else if(tgt.indexOf("save") != -1){
			_app.getCanvas().saveDesign();
		}else if(tgt.indexOf("copy") != -1){
			_app.copy();
		}else if(tgt.indexOf("paste") != -1){
			_app.paste();
		}else if(tgt.indexOf("trans") != -1){	
			_app.getCanvas().toggleTransitionTool();
					
		}else if(tgt.indexOf("optional") != -1){
			_app.getCanvas().toggleOptionalActivity();
						
		}else if(tgt.indexOf("gate") != -1){
			_app.getCanvas().toggleGateTool();
						
		}else if(tgt.indexOf("group") != -1){
			_app.getCanvas().toggleGroupTool();
			
		}else if(tgt.indexOf("preview") != -1){
			_app.getCanvas().launchPreviewWindow();
		}
	}
     
}
