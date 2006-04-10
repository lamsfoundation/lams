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
import org.lamsfoundation.lams.learner.*
import org.lamsfoundation.lams.learner.lb.*;
import org.lamsfoundation.lams.learner.ls.Lesson;

/**
* Controller for the sequence library
*/
class LibraryController extends AbstractController {
	private var _libraryModel:LibraryModel;
	
	/**
	* Constructor
	*
	* @param   cm   The model to modify.
	*/
	public function LibraryController (cm:Observable) {
		super (cm);
		_libraryModel = LibraryModel(model);
	}
	
	// control methods
	
	/**
	*Called by Lesson when one in clicked
	* @param lesson - the lesson that was clicked
	*/
	public function selectLesson(lesson:Lesson):Void{
		_libraryModel = LibraryModel(model);
		_libraryModel.setSelectedLesson(lesson);
	}
	
	public function getActiveLessons():Void {
		_libraryModel = LibraryModel(model);
		_libraryModel.getLibrary().getActiveLessons();
	}
	
}