<?php

require_once($CFG->libdir.'/formslib.php');
require_once('lib.php');

class mod_lamstwo_add_form extends moodleform {

    function definition() {
        global $USER;
	
        $mform =& $this->_form;
		
        $course = $this->_customdata['course'];
        $lamstwo = $this->_customdata['lamstwo'];
        $customcsv = $this->_customdata['customCSV'];
		
        $mform->addElement('header', 'general', 'Your new LAMS lesson');

        $mform->addElement('text', 'name', 'Lesson name', 'size="48"');
        $mform->setType('name', PARAM_TEXT);
        $mform->addRule('name', get_string('required'), 'required', null, 'client');

        $mform->addElement('htmleditor', 'intro', 'Introduction');
        $mform->setType('intro', PARAM_RAW);
        $mform->setHelpButton('intro', array('richtext'), false, 'editorhelpbutton');
        
        // display user's lams workspace
        $sequencelabel = get_string('selectsequence', 'lamstwo');
        $lds = '[' . lamstwo_get_sequences_rest($USER->username, $course->id, $USER->country, $USER->lang) . ']';
        $html = <<<XXX
	<script type="text/javascript" src="tree.js"></script>
	<script type="text/javascript" src="tree_tpl.js"></script>
	<script language="javascript">
		<!--
			function selectSequence(obj){
				document.getElementsByName("sequence_id")[0].value = obj;
			}
			var TREE_ITEMS = $lds; new tree (TREE_ITEMS, TREE_TPL);
		//-->
	</script>
XXX;

        $mform->addElement('static', 'sequencemessage', get_string('selectsequence', 'lamstwo'), $html);
        //$mform->addElement('html', $html);
        
        $mform->addElement('checkbox', 'schedule', get_string('schedulestart', 'lamstwo'));

        $mform->addElement('date_time_selector', 'schedulestart', get_string('dateandtime', 'lamstwo'));
        $mform->disabledIf('schedulestart', 'schedule');
        
        $this->add_action_buttons(false, 'Add lesson');
        
        $mform->addElement('hidden', 'course', $course->id);
        $mform->setType('course', PARAM_INT);

        $mform->addElement('hidden', 'customCSV', $customcsv);
        $mform->setType('customCSV', PARAM_TEXT);

        $mform->addElement('hidden', 'lamstwo', $lamstwo->id);
        $mform->setType('lamstwo', PARAM_INT);

        $mform->addElement('hidden', 'groupid');
        $mform->setType('groupid', PARAM_INT);
		
        // value filled after submit by lamstwo_add_instance
        $mform->addElement('hidden', 'lesson_id');
        $mform->setType('lesson_id', PARAM_INT);
		
        // value filled by javascript when user selects a sequence
        $mform->addElement('hidden', 'sequence_id');
        $mform->setType('sequence_id', PARAM_INT);
        //$mform->addRule('sequence_id', get_string('required'), 'required', null, 'client');
    }
	
    function validation($data) {
        $errors = array();
        // scheduled date needs to be in the future
        if (isset($data['schedule']) && $data['schedule']) {
            if ($data['schedulestart'] <= date('U')) {
                $errors['schedulestart'] = get_string('nopast', 'lamstwo');
            }
        }
        // a sequence needs to be selected
        if (empty($data['sequence_id']) || $data['sequence_id'] <= 0) {
            $errors['sequencemessage'] = get_string('sequencenotselected', 'lamstwo');
        }
        return $errors;
    }

}

?>
