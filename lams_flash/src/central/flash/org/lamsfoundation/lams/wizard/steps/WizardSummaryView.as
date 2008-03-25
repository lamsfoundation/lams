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

import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.wizard.*
import org.lamsfoundation.lams.wizard.steps.*
import org.lamsfoundation.lams.monitoring.User;
import org.lamsfoundation.lams.monitoring.Orgnanisation;
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.mvc.*
import org.lamsfoundation.lams.common.ws.*
import org.lamsfoundation.lams.common.Config

import mx.controls.*
import mx.utils.*
import mx.managers.*
import mx.events.*

/**
 *
 * @author Mitchell Seaton
 * @version 2.0.3
 **/
class org.lamsfoundation.lams.wizard.steps.WizardSummaryView  extends AbstractView {
	
	private var _className = "WizardSummaryView";
	
	// conclusion UI elements
	private var confirmMsg_txt:TextField;

	//Defined so compiler can 'see' events added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;

	function WizardSummaryView(){
	}
	
	public function init(m:Observable,c:Controller) {
		super(m, c)
	}
	
	public function show(v:Boolean):Void {
		confirmMsg_txt.visible = v;
	}
	
	public function showConfirmMessage(mode:Number){
		var msg:String = "";
		var lessonName:String = "<b>" + _parent.resultDTO.resourceTitle +"</b>";
		switch(mode){
			case WizardView.FINISH_MODE : 
				msg = Dictionary.getValue('confirmMsg_3_txt', [lessonName]);
				break;
			case WizardView.START_MODE :
				msg = Dictionary.getValue('confirmMsg_1_txt', [lessonName]);
				break;
			case WizardView.START_SCH_MODE :
				msg = Dictionary.getValue('confirmMsg_2_txt', [lessonName, unescape(_parent.resultDTO.scheduleDateTime)]);
				break;
			default:
				trace('unknown mode');
		}
		
		confirmMsg_txt.html = true;
		confirmMsg_txt.htmlText = msg;
		confirmMsg_txt._width = confirmMsg_txt.textWidth + 5;
		
		confirmMsg_txt._x = _parent.panel._x + (_parent.panel._width/2) - (confirmMsg_txt._width/2);
		confirmMsg_txt._y = _parent.panel._y + (_parent.panel._height/4);
		
		confirmMsg_txt.visible = true;
		
	}

	/**
	 * Overrides method in abstract view to ensure cortect type of controller is returned
	 * @usage   
	 * @return  CanvasController
	 */
	public function getController():WizardController{
		var c:Controller = _parent.getController();
		return WizardController(c);
	}
	
	/*
    * Returns the default controller for this view.
    */
    public function defaultController (model:Observable):Controller {
        return new WizardController(model);
    }
}