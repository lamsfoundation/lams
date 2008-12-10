<?php  // $Id$

    require_once("../../config.php");
    require_once("lib.php");
 
    $id = optional_param('id', 0, PARAM_INT);  // Course Module ID
    $a  = optional_param('a', 0, PARAM_INT);   // Assignment ID
	$editing  = optional_param('editing', 0, PARAM_INT); // 1 if editing in Lams
	$saved  = optional_param('saved', 0, PARAM_INT); 
	
	
    if ($id) {
        
        if (! $cm = get_coursemodule_from_id('assignment', $id)) {
            error("Course Module ID was incorrect");
        }
       
        if (! $assignment = get_record("assignment", "id", $cm->instance)) {
            error("assignment ID was incorrect");
        }

        if (! $course = get_record("course", "id", $assignment->course)) {
            error("Course is misconfigured");
        }
    } else {
        if (!$assignment = get_record("assignment", "id", $a)) {
            error("Course module is incorrect");
        }
        if (! $course = get_record("course", "id", $assignment->course)) {
            error("Course is misconfigured");
        }
        if (! $cm = get_coursemodule_from_instance("assignment", $assignment->id, $course->id)) {
            error("Course Module ID was incorrect");
        }
	    
    }
    //if is lams display navigation buttons so you can finish uploading or go to next activity
	if($cm->is_lams==1){
    		if($editing==1){
				include('showlamsfinish.php');
			}else{
		     	$isteacher = has_capability('mod/assignment:preview', get_context_instance(CONTEXT_MODULE, $cm->id)); // indicates if is a teacher, useful in lams
				if($isteacher||$saved==1||$assignment->assignmenttype=="offline"){
				       include('showlamsnext.php');
				}
			}
	}

    require_login($course, true, $cm);

    require ("$CFG->dirroot/mod/assignment/type/$assignment->assignmenttype/assignment.class.php");
    $assignmentclass = "assignment_$assignment->assignmenttype";
    $assignmentinstance = new $assignmentclass($cm->id, $assignment, $cm, $course);
    $assignmentinstance->view();   // Actually display the assignment! 

?>
