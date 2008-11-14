<?php

	require_once('../../config.php');
	require_once('lib.php');
	require_once('add_form.php');
	
	$lamstwo = optional_param('lamstwo', 0, PARAM_INT);
	$group = optional_param('group', 0, PARAM_INT);
	
	if (!empty($lamstwo)) {      // User is starting a new lesson in a lamstwo
        if (! $lamstwo = get_record('lamstwo', 'id', $lamstwo)) {
            error('The lamstwo number was incorrect');
        }
    }
    if (! $course = get_record('course', 'id', $lamstwo->course)) {
        error('The course number was incorrect');
    }
    if (! $cm = get_coursemodule_from_instance('lamstwo', $lamstwo->id, $course->id)) {
    	error('Course Module ID was incorrect');
    }
    
    require_login($course, false, $cm);
    $modcontext = get_context_instance(CONTEXT_MODULE, $cm->id);
	
    // Print header
    $strchoices = get_string('modulenameplural', 'lamstwo');
    print_header_simple(format_string($lamstwo->name), '',
        "<a href=\"index.php?id=$course->id\">$strchoices</a> 
        -> <a href=\"view.php?id=$cm->id\">".format_string($lamstwo->name)."</a> 
        -> ".get_string('addlesson', 'lamstwo'), '', '', true,
        update_module_button($cm->id, $course->id, get_string('modulename', 'lamstwo')), navmenu($course, $cm));
    
	$mform_add = new mod_lamstwo_add_form('add.php', 
		array('course'=>$course, 'lamstwo'=>$lamstwo, 'customCSV'=>"$USER->username,$course->id,$cm->section"));

	// Form processing
	if ($form = $mform_add->get_data()) {
		$lamstwo_lesson_id = lamstwo_add_lesson($form);
		if (isset($lamstwo_lesson_id) && $lamstwo_lesson_id > 0) {
			$message = get_string('addedlesson', 'lamstwo');
		} else {
			$message = get_string('failedaddlesson', 'lamstwo');
		}
		//print_r($form);
		redirect('view.php?id='.$cm->id, $message);
	}
	
	// Populate form
	$mform_add->set_data(array('groupid'=>$group));
		
	$mform_add->display();
	
	print_footer($course);
?>
