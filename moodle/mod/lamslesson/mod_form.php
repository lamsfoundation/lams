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

require_once($CFG->dirroot.'/course/moodleform_mod.php');

class mod_lamslesson_mod_form extends moodleform_mod {

    function definition() {
      global $COURSE, $USER, $CFG;
        $mform =& $this->_form;

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

    //-- Open URL button

	// Check whether this person has the right permissions to see the open button. 
	if ($canmanage) {

	  $customcsv = "$USER->username,$COURSE->id,$CFG->lamslesson_serverid";
	  $authorurl = lamslesson_get_url($USER->username, $locale['lang'], $locale['country'], 0, $COURSE->id, $COURSE->fullname, $COURSE->timecreated, LAMSLESSON_PARAM_AUTHOR_METHOD, $customcsv);
	  $openauthorlabel = get_string('openauthor', 'lamslesson');

	  // html "chunk" for open Author button 
	  $authorbutton = <<<XXX
<script language="javascript" type="text/javascript">
<!--
		var authorWin = null;
		function openAuthor(url,name,options,fullscreen) {
                url = url + "&requestSrc=" + escape("$CFG->lamslesson_requestsource");
                url = url + "&notifyCloseURL=" + escape(window.location.href);
                if(authorWin && !authorWin.closed){
                        authorWin.focus();
                }else{
                        authorWin = window.open(url,name,options);
                        if (fullscreen) {
                                authorWin.moveTo(0,0);
                                authorWin.resizeTo(screen.availWidth,screen.availHeight);
                        }
                        authorWin.focus();
                }
                return false;
        }
//-->
</script>

<p align="center">
    <input type="submit" value="$openauthorlabel" onclick="openAuthor('$authorurl','author','location=0,toolbar=0,menubar=0,statusbar=0,width=996,height=700,resizable',0)">
</p>
XXX;
	}


    
    $mform->addElement('hidden', 'sequence_id');
    $mform->setType('sequence_id', PARAM_INT);

    $mform->addElement('hidden', 'customCSV', $customcsv);
    $mform->setType('customCSV', PARAM_TEXT);

    // display user's lams workspace
    $lds = lamslesson_get_sequences_rest($USER->username, $COURSE->id, $COURSE->fullname, $COURSE->timecreated, $USER->country, $USER->lang) ;

    // html "chuck" for YUI tree
    $html = <<<XXX
     <!-- Combo-handled YUI CSS files: -->
     <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.8.2r1/build/fonts/fonts-min.css" /> 
     <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.8.2r1/build/treeview/assets/skins/sam/treeview.css" /> 
     <script type="text/javascript" src="http://yui.yahooapis.com/2.8.2r1/build/yahoo-dom-event/yahoo-dom-event.js"></script> 
     <script type="text/javascript" src="http://yui.yahooapis.com/2.8.2r1/build/treeview/treeview-min.js"></script> 
     <!--bring in the folder-style CSS for the TreeView Control--> 
     <link rel="stylesheet" type="text/css" href="http://developer.yahoo.com/yui/examples/treeview/assets/css/folders/tree.css"> 
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
	  }
      }

        //-->
     </script>

<div id="treeDiv"></div>
<script type="text/javascript">
var tree;
tree = new YAHOO.widget.TreeView("treeDiv",[
$lds
					    ]);
// expand only the first two nodes
tree.getNodeByIndex(1).expand(true);
tree.getNodeByIndex(2).expand(true);
tree.render();
tree.subscribe('clickEvent',function(oArgs) {
    selectSequence(oArgs.node.data.id, oArgs.node.label);
  });



</script>

XXX;

    // Now we put the two html chunks together
$html = $authorbutton . $html; 

        $mform->addElement('header', 'selectsequence', get_string('selectsequence', 'lamslesson'));

        $mform->addElement('static', 'sequencemessage', '', $html);
	
//-------------------------------------------------------------------------------
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
