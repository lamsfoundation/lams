<?php // $Id$

/// This page lists all the instances of lamstwo in a particular course

    require_once('../../config.php');
    require_once('lib.php');

    $id = required_param('id', PARAM_INT);   // course

    if (! $course = get_record('course', 'id', $id)) {
        error('Course ID is incorrect');
    }

    require_login($course->id);

    add_to_log($course->id, 'lamstwo', 'view all', 'index.php?id='.$course->id, '');


/// Get all required strings

    $strlamstwos = get_string('modulenameplural', 'lamstwo');
    $strlamstwo  = get_string('modulename', 'lamstwo');


/// Print the header

    $navigation = '';
    if ($course->category) {
        $navigation = "<a href=\"../../course/view.php?id=$course->id\">$course->shortname</a> ->";
    }

    print_header("$course->shortname: $strlamstwos", "$course->fullname", "$navigation $strlamstwos", '', '', true, '', navmenu($course));

/// Get all the appropriate data

    if (! $lamstwos = get_all_instances_in_course('lamstwo', $course)) {
        notice(get_string('nolamsv2', 'lamstwo'), "../../course/view.php?id=$course->id");
        die;
    }
    
/// Print the list of instances (your module will probably extend this)

    $timenow = time();
    $strname  = get_string('name');
    $strintro = get_string('introduction', 'lamstwo');
    $strweek  = get_string('week');
    $strtopic  = get_string('topic');

    if ($course->format == 'weeks') {
        $table->head  = array ($strweek, $strname, $strintro);
        $table->align = array ('center', 'left', 'left');
    } else if ($course->format == 'topics') {
        $table->head  = array ($strtopic, $strname, $strintro);
        $table->align = array ('center', 'left', 'left');
    } else {
        $table->head  = array ($strname, $strintro);
        $table->align = array ('left', 'left');
    }
    
    $generaltable->head = array ($strname, $strintro);
    $generaltable->align = array ('left', 'left');
    
    foreach ($lamstwos as $lamstwo) {
        if (!$lamstwo->visible) {
            //Show dimmed if the mod is hidden
            $link = "<a class=\"dimmed\" href=\"view.php?id=$lamstwo->coursemodule\">$lamstwo->name</a>";
        } else {
            //Show normal if the mod is visible
            $link = "<a href=\"view.php?id=$lamstwo->coursemodule\">$lamstwo->name</a>";
        }
        
        // site level or section 0
        if (($course->id == SITEID) or empty($lamstwo->section) or $lamstwo->section == 0) {
        	$generaltable->data[] = array ($link, $lamstwo->intro);
        	continue;
        }

        if ($course->format == 'weeks' or $course->format == 'topics') {
            $table->data[] = array ($lamstwo->section, $link, $lamstwo->intro);
        } else {
            $table->data[] = array ($link, $lamstwo->intro);
        }
    }

    echo '<br />';

    print_heading(get_string('generallamsv2', 'lamstwo'));
    print_table($generaltable);
    
    print_heading(get_string('learninglamsv2', 'lamstwo'));
    print_table($table);

/// Finish the page

    print_footer($course);

?>
