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

import org.lamsfoundation.lams.common.*;
import org.lamsfoundation.lams.common.dict.*;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.ui.*;
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.wizard.*;

import mx.managers.*
import mx.events.*
import mx.utils.*
import mx.controls.*

/**  
*  
*/  
class WizardSummery extends MovieClip {  
//class org.lamsfoundation.lams.authoring.cv.CanvasActivity extends MovieClip{  
	
	//this is set by the init object
	private var _wizardController:WizardController;
	private var _wizardView:WizardView;
	private var _tm:ThemeManager;
	
	private var app:Application;
	
	//local components - labels
	private var design_lbl:Label;
	private var title_lbl:Label;
	private var desc_lbl:Label;
	private var course_lbl:Label;
	private var class_lbl:Label;
	private var staff_lbl:Label;
	private var learners_lbl:Label;
	
	//local components - textfields
	private var design_txt:TextField;
	private var title_txt:TextField;
	private var desc_txt:TextField;
	private var coursename_txt:TextField;
	private var classname_txt:TextField;
	private var staff_txt:TextField;
	private var learners_txt:TextField;
	
	function WizardSummery(){
		_tm = ThemeManager.getInstance();
		app = Application.getInstance();
		
		init();
	}
	
	public function init(initObj):Void{
		
		if(initObj){
			_wizardView = initObj._wizardView;
			_wizardController = initObj._wizardController;
		}

		_visible = false;
		
		MovieClipUtils.doLater(Proxy.create(this,draw));

	}
	

	/**
	 * Does the work of laying out the screen assets.
	 * Depending on type of Activity different bits will be shown
	 * @usage   
	 * @return  
	 */
	private function draw(){
		
		setupLabels();
		setupStyles();
		_visible = true;
	}
	
	
	/**
	 * Get the CSSStyleDeclaration objects for each component and applies them
	 * directly to the instanced
	 * @usage   
	 * @return  
	 */
	private function setupStyles() {
		var styleObj = _tm.getStyleObject('label');
		design_lbl.setStyle('styleName',styleObj);
		title_lbl.setStyle('styleName',styleObj);
		desc_lbl.setStyle('styleName',styleObj);
		course_lbl.setStyle('styleName',styleObj);
		class_lbl.setStyle('styleName',styleObj);
		staff_lbl.setStyle('styleName',styleObj);
		learners_lbl.setStyle('styleName',styleObj);
    }
	
	private function setupLabels() {
		design_lbl.text = Dictionary.getValue('summery_design_lbl');
		title_lbl.text = Dictionary.getValue('summery_title_lbl');
		desc_lbl.text = Dictionary.getValue('summery_desc_lbl');
		course_lbl.text = Dictionary.getValue('summery_course_lbl');
		class_lbl.text = Dictionary.getValue('summery_class_lbl');
		staff_lbl.text = Dictionary.getValue('summery_staff_lbl');
		learners_lbl.text = Dictionary.getValue('summery_learners_lbl');
	}
    

}