<?php
require_once ($CFG->dirroot.'/course/moodleform_mod.php');

class mod_lamstwo_mod_form extends moodleform_mod {

	function definition() {
	
		$mform    =& $this->_form;
		
//-------------------------------------------------------------------------------
        $mform->addElement('header', 'general', get_string('general', 'form'));

        $mform->addElement('text', 'name', get_string('lamstwoname', 'lamstwo'), array('size'=>'64'));
		$mform->setType('name', PARAM_TEXT);
		$mform->addRule('name', null, 'required', null, 'client');

		$mform->addElement('htmleditor', 'intro', get_string('introduction', 'lamstwo'));
		$mform->setType('intro', PARAM_RAW);
		$mform->addRule('intro', get_string('required'), 'required', null, 'client');
        $mform->setHelpButton('intro', array('writing', 'questions', 'richtext'), false, 'editorhelpbutton');
        
//-------------------------------------------------------------------------------
		$this->standard_coursemodule_elements();

//-------------------------------------------------------------------------------
        // buttons
        $this->add_action_buttons();
	}
	
}
?>