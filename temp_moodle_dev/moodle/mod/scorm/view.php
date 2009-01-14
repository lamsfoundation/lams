<?php  // $Id$

    require_once("../../config.php");
    require_once('locallib.php');
    
    $id = optional_param('id', '', PARAM_INT);       // Course Module ID, or
    $a = optional_param('a', '', PARAM_INT);         // scorm ID
    $organization = optional_param('organization', '', PARAM_INT); // organization ID
    $editing  = optional_param('editing', 0, PARAM_INT); // 1 if editing in Lams
    $is_learner  = optional_param('is_learner',0, PARAM_INT); //variable that indicates if is a learner doing the activity or is a teacher editing it
	
    if (!empty($id)) {
        if (! $cm = get_coursemodule_from_id('scorm', $id)) {
            error("Course Module ID was incorrect");
        }
        if (! $course = get_record("course", "id", $cm->course)) {
            error("Course is misconfigured");
        }
        if (! $scorm = get_record("scorm", "id", $cm->instance)) {
            error("Course module is incorrect");
        }
    } else if (!empty($a)) {
        if (! $scorm = get_record("scorm", "id", $a)) {
            error("Course module is incorrect");
        }
        if (! $course = get_record("course", "id", $scorm->course)) {
            error("Course is misconfigured");
        }
        if (! $cm = get_coursemodule_from_instance("scorm", $scorm->id, $course->id)) {
            error("Course Module ID was incorrect");
        }
    } else {
        error('A required parameter is missing');
    }

    require_login($course->id, false, $cm);

    $context = get_context_instance(CONTEXT_COURSE, $course->id);

    if (isset($SESSION->scorm_scoid)) {
        unset($SESSION->scorm_scoid);
    }

    $strscorms = get_string("modulenameplural", "scorm");
    $strscorm  = get_string("modulename", "scorm");

    if ($course->id != SITEID) { 
        
        if ($scorms = get_all_instances_in_course('scorm', $course)) {
            // The module SCORM activity with the least id is the course  
            $firstscorm = current($scorms);
            if (!(($course->format == 'scorm') && ($firstscorm->id == $scorm->id))) {
                $navlinks[] = array('name' => $strscorms, 'link' => "index.php?id=$course->id", 'type' => 'activity');
            }       
        }
    }
    $pagetitle = strip_tags($course->shortname.': '.format_string($scorm->name));

    add_to_log($course->id, 'scorm', 'pre-view', 'view.php?id='.$cm->id, "$scorm->id", $cm->id);

    if ((has_capability('mod/scorm:skipview', get_context_instance(CONTEXT_MODULE,$cm->id))) && scorm_simple_play($scorm,$USER)) {
        exit;
    }

    //
    // Print the page header
    //
    $navlinks = array();
    $navlinks[] = array('name' => format_string($scorm->name,true), 'link' => "view.php?id=$cm->id", 'type' => 'activityinstance');
    $navigation = build_navigation($navlinks);
    //if is_lams=1 hide Moodle's headers
    if($scorm->is_lams==0){
    	print_header($pagetitle, $course->fullname, $navigation,
                 '', '', true, update_module_button($cm->id, $course->id, $strscorm), navmenu($course, $cm));
    }else{
    	print_header();
    }
    if (has_capability('mod/scorm:viewreport', $context)) {
        
        $trackedusers = scorm_get_count_users($scorm->id, $cm->groupingid);
        if ($trackedusers > 0) {
            echo "<div class=\"reportlink\"><a $CFG->frametarget href=\"report.php?id=$cm->id\"> ".get_string('viewalluserreports','scorm',$trackedusers).'</a></div>';
        } else {
            echo '<div class="reportlink">'.get_string('noreports','scorm').'</div>';
        }
    }

    // Print the main part of the page
    print_heading(format_string($scorm->name));
    print_box(format_text($scorm->summary), 'generalbox', 'intro');
    //lams: we pass a new variable called editing to be able to display the correct lams button when we are in player.php
    scorm_view_display($USER, $scorm, 'view.php?id='.$cm->id, $cm,'',$editing);
    
    if($cm->is_lams==1&&$editing==1){
		 		//lams: display the finish edditing button if we have added any kind of question
            	include('showlamsfinish.php');   
	}
    //we pass a new parameter to the function so it won't we printed if is_lams=1
	print_footer($course,null, false,$cm->is_lams);
?>
