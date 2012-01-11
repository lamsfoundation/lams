<?php

// This file is part of Moodle - http://moodle.org/
//
// Moodle is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// Moodle is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with Moodle.  If not, see <http://www.gnu.org/licenses/>.


/**
 * The main lamslesson configuration form
 *
 * It uses the standard core Moodle formslib. For more info about them, please
 * visit: http://docs.moodle.org/en/Development:lib/formslib.php
 *
 * @package   mod_lamslesson
 * @copyright 2011 LAMS Foundation - Ernie Ghiglione (ernieg@lamsfoundation.org) 
 * @license  http://www.gnu.org/licenses/gpl-2.0.html GNU GPL v2
 */

defined('MOODLE_INTERNAL') || die();

$PAGE->requires->yui2_lib('yahoo-dom-event');
$PAGE->requires->yui2_lib('treeview');

require_once($CFG->dirroot.'/course/moodleform_mod.php');

class mod_lamslesson_mod_form extends moodleform_mod {

    function definition() {
      global $COURSE, $USER, $CFG;

        $mform =& $this->_form;

        if (!empty($this->_instance)) {
	  $sequence_id = $this->current->sequence_id;
	  $currentsequence = get_string('currentsequence','lamslesson');
	  $updatewarning =  get_string('updatewarning','lamslesson');
	} else {
	  $sequence_id = 0;
	  $currentsequence = '';
	  $updatewarning = '';
	}

    //-------------------------------------------------------------------------------
    /// Adding the "general" fieldset, where all the common settings are showed
        $mform->addElement('header', 'general', get_string('general', 'form'));

    /// Adding the standard "name" field
        $mform->addElement('text', 'name', get_string('lamslessonname', 'lamslesson'), array('size'=>'64'));
        if (!empty($CFG->formatstringstriptags)) {
            $mform->setType('name', PARAM_TEXT);
        } else {
            $mform->setType('name', PARAM_CLEAN);
        }
        $mform->addRule('name', null, 'required', null, 'client');
        $mform->addRule('name', get_string('maximumchars', '', 255), 'maxlength', 255, 'client');
        $mform->addHelpButton('name', 'lamslessonname', 'lamslesson');

    /// Adding the standard "intro" and "introformat" fields
        $this->add_intro_editor();

//-------------------------------------------------------------------------------
    /// Adding the rest of lamslesson settings, spreeading all them into this fieldset
    /// or adding more fieldsets ('header' elements) if needed for better logic

    // Set needed vars
	$context = get_context_instance(CONTEXT_COURSE, $COURSE->id);
	$locale = lamslesson_get_locale($COURSE->id);
	$canmanage = has_capability('mod/lamslesson:manage', $context);

    //-- Open Author & Preview URL buttons

	// Check whether this person has the right permissions to see the open button. 
	if ($canmanage) {

	  $customcsv = "$USER->username,$COURSE->id,$CFG->lamslesson_serverid";
	  $authorurl = lamslesson_get_url($USER->username, $locale['lang'], $locale['country'], 0, $COURSE->id, $COURSE->fullname, $COURSE->timecreated, LAMSLESSON_PARAM_AUTHOR_METHOD, $customcsv);

	  $previewurl = $CFG->wwwroot.'/mod/lamslesson/preview.php?';
	  $popupoptions = LAMSLESSON_POPUP_OPTIONS;
	  $openauthorlabel = get_string('openauthor', 'lamslesson');
	  $openpreviewlabel = get_string('previewthislesson', 'lamslesson');

	  // html "chunk" for open Author button 

        $authorpreviewbutton = html_writer::script(js_writer::set_variable('authorWin', 'null') . 
                                js_writer::set_variable('previewWin', 'null') .
                                js_writer::set_variable('options', $popupoptions) .
                                js_writer::set_variable('requestsrc', $CFG->lamslesson_requestsource) .
                                js_writer::set_variable('course', $COURSE->id)
                );

        $authorpreviewbutton .= html_writer::script('', $CFG->wwwroot.'/mod/lamslesson/preview.js');


        $authorpreviewbutton .= html_writer::start_tag('div', array('id' => 'buttons', 'style' => 'float:right;'));
        // Preview button
        $authorpreviewbutton .= html_writer::start_tag('span', array('id' => 'previewbutton', 'style' => 'visibility:hidden;', 'class' => 'yui-button yui-link-button'));
        $authorpreviewbutton .= html_writer::start_tag('span', array('class' => 'first-child'));
        $authorpreviewbutton .= html_writer::link('#nogo', $openpreviewlabel, array('onclick' => js_writer::function_call('openPreview', array('1' => $previewurl, '2' => preview, '3' => 0))));
        $authorpreviewbutton .= html_writer::end_tag('span');
        $authorpreviewbutton .= html_writer::end_tag('span');
	//Authoring button
        $authorpreviewbutton .= html_writer::start_tag('span', array('id' => 'authorbutton', 'class' => 'yui-button yui-link-button'));
        $authorpreviewbutton .= html_writer::start_tag('span', array('class' => 'first-child'));
        $authorpreviewbutton .= html_writer::link('#nogo', $openauthorlabel, array('onclick' => js_writer::function_call('openAuthor', array('1' => $authorurl, '2' => author, '3' => 0))));
        $authorpreviewbutton .= html_writer::end_tag('span');
        $authorpreviewbutton .= html_writer::end_tag('span');
        $authorpreviewbutton .= html_writer::end_tag('div');
	}

    $mform->addElement('hidden', 'sequence_id');
    $mform->setType('sequence_id', PARAM_INT);

    $mform->addElement('hidden', 'customCSV', $customcsv);
    $mform->setType('customCSV', PARAM_TEXT);

    // display user's lams workspace
    $lds = lamslesson_get_sequences_rest($USER->username, $COURSE->id, $COURSE->fullname, $COURSE->timecreated, $USER->country, $USER->lang) ;

    

    // html "chuck" for YUI tree
    $html = <<<XXX
     <script type="text/javascript">
       <!--
       function selectSequence(obj, name){
	// if the selected object is a sequence (id!=0) then we assign the id to the hidden sequence_id
	// also if the name is blank we just add the name of the sequence to the name too. 

	document.getElementsByName("sequence_id")[0].value = obj;

	  if (obj!=0) {
	    if (document.getElementsByName("name")[0].value == '') {
	      document.getElementsByName("name")[0].value = name;
	    }
	    document.getElementById('previewbutton').style.visibility='visible';
	  } else {
	    document.getElementById('previewbutton').style.visibility='hidden';
	  }
      }

        //-->
     </script>

<div id="treeDiv"></div>
<div id="updatesequence"></div>
<script type="text/javascript">
var tree;
tree = new YAHOO.widget.TreeView("treeDiv",[
$lds
					    ]);
// expand only the first two nodes
tree.getNodeByIndex(1).expand(true);
tree.getNodeByIndex(2).expand(true);

if ($sequence_id > 0) {
  var node = tree.getNodeByProperty('id', $sequence_id);
  var sequenceName = node.label;
  var updateDiv = document.createElement('div');
  updateDiv.setAttribute('class','note');
  updateDiv.setAttribute('id','currentsequence');
  updateDiv.innerHTML = '<p>$updatewarning</p><strong>$currentsequence ' + sequenceName +'</strong>';
  document.getElementById('updatesequence').appendChild(updateDiv);
}
tree.render();
tree.subscribe('clickEvent',function(oArgs) {
    selectSequence(oArgs.node.data.id, oArgs.node.label);
  });
</script>

XXX;

    // Now we put the two html chunks together
$html = $authorpreviewbutton . $html; 

        $mform->addElement('header', 'selectsequence', get_string('selectsequence', 'lamslesson'));

        $mform->addElement('static', 'sequencemessage', '', $html);
    	$mform->addElement('checkbox', 'displaydesign', get_string('displaydesign', 'lamslesson'));
//	$mform->setAdvanced('displaydesign');
//-------------------------------------------------------------------------------
	$this->standard_grading_coursemodule_elements();

	// set the default grade to 'No Grade' so it doesn't record
	// anything on gradebook unless specifically set.
	$mform->setDefault('grade', 0);

        // add standard elements, common to all modules
        $this->standard_coursemodule_elements();
//-------------------------------------------------------------------------------
        // add standard buttons, common to all modules
        $this->add_action_buttons();
    }
	
    function validation($data) {
      $errors = array();
      // a sequence needs to be selected                                                                                   
      if (empty($data['sequence_id']) || $data['sequence_id'] <= 0) {
	$errors['sequencemessage'] = get_string('sequencenotselected', 'lamslesson');
      }
      return $errors;
    }

    function add_completion_rules() {
        $mform =& $this->_form;

        $mform->addElement('checkbox', 'completionfinish', '', get_string('completionfinish', 'lamslesson'));
        return array('completionfinish');
    }

    function completion_rule_enabled($data) {
        return !empty($data['completionfinish']);
    }

}
