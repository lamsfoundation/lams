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

import org.lamsfoundation.lams.learner.ls.*;
import org.lamsfoundation.lams.common.mvc.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.comms.Communication;
import org.lamsfoundation.lams.learner.*

/*
* Make changes to Lesson's model data based on user input
*/
class LessonController extends AbstractController {
	
	/**
	* Constructor
	*
	* @param   cm   The model to modify.
	*/
	private var _lessonModel:LessonModel;
	private var _app:Application;
	private var _comms:Communication;

	
	public function LessonController (cm:Observable) {
		super (cm);
		_app = Application.getInstance();
		_comms = _app.getComms();
		_lessonModel = LessonModel(model);
	}
	
	/**
	 * Recieves the click events from the Lesson buttons.  Based on the label
	 * the relevent method is called to action the user request
	 * @param   evt 
	 */
	public function click(evt):Void{
		trace(String(evt.target));
		trace('onClick event: joining lesson...');
		_lessonModel = LessonModel(model);
		_lessonModel.getLesson().joinLesson();
		
		/*
		Debugger.log('click evt.target.label:'+evt.target.label,Debugger.GEN,'click','LessonController');
		var tgt:String = new String(evt.target);
		if(tgt.indexOf("join") != -1){
			_lessonModel.getLesson().joinLesson();
		}*/
	}
	
	
}