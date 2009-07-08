
<?php  // $Id$

require_once('../../config.php');
require_once('lib.php');

$id = optional_param('id', 0, PARAM_INT);    // Course Module ID
$delete = optional_param('delete', 0, PARAM_INT);    // lamstwo_lesson id to delete
$confirm = optional_param('confirm', 0, PARAM_INT);    // boolean confirming deletion

if (! $cm = get_coursemodule_from_id('lamstwo', $id)) {
    error('Course Module ID was incorrect');
}

if (! $course = get_record('course', 'id', $cm->course)) {
    error('Course is misconfigured');
}

if (! $lamstwo = get_record('lamstwo', 'id', $cm->instance)) {
    error('Course module is incorrect');
}

$locale = lamstwo_get_locale($course->id);

require_course_login($course, true, $cm);
$context = get_context_instance(CONTEXT_MODULE, $cm->id);

// Print header
$strchoices = get_string('modulenameplural', 'lamstwo');
print_header_simple(format_string($lamstwo->name), "",
        "<a href=\"index.php?id=$course->id\">$strchoices</a> 
        -> ".format_string($lamstwo->name), '', '', true,
        update_module_button($cm->id, $course->id, get_string('modulename','lamstwo')), navmenu($course, $cm));

// Delete lesson prompt if requested
if (!empty($delete)) {
  if (!empty($confirm) && $confirm == 1) {
    // delete and redirect to this lamstwo page
    if (lamstwo_delete_lamstwo_lesson($delete)) {
      redirect('view.php?id='.$id, get_string('deletesuccess', 'lamstwo'));
    } else {
      redirect('view.php?id='.$id);
    }
  } else {
    // delete confirmation
    print_simple_box_start('center', '60%', '#FFAAAA', 20, 'noticebox');
    print_heading(get_string('deletelessonconfirm', 'lamstwo'));
    echo "<form method='post' action='view.php' />";
    echo "<input type='hidden' name='id' value='$id' />";
    echo "<input type='hidden' name='delete' value='$delete' />";
    echo "<input type='hidden' name='confirm' value='1' />";
    echo "<input type='submit' value='yes' />";
    echo "<input type='button' value='no' onclick='javascript:history.go(-1);' />";
    echo "</form>";
    print_simple_box_end();
    print_footer($course);
    return;
  }
} 
        
// Find out current groups mode
$groupmode = groupmode($course, $cm);
$currentgroup = setup_and_print_groups($course, $groupmode, 'view.php?id=' . $cm->id);

// Print intro
echo '<br /><br /><div class="box clearer">&nbsp;</div><div id="intro" class="box generalbox"><p>' . $lamstwo->intro . '</p></div>';
$canmanage = has_capability('mod/lams:manage', $context);
if ($canmanage) {
	$openauthorjs = <<<XXX
<script language="javascript" type="text/javascript">
<!--
		var authorWin = null;
		function openAuthor(url,name,options,fullscreen) {
                url = url + "&requestSrc=" + escape("$CFG->lamstwo_requestsource");
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
XXX;
    $customcsv = "$USER->username,$course->id,$cm->section,$CFG->lamstwo_serverid";
    $authorurl = lamstwo_get_url($USER->username, $locale['lang'], $locale['country'], 0, $course->id, $course->fullname, $course->timecreated, $LAMS2CONSTANTS->author_method, $customcsv);
	
    echo $openauthorjs;

    echo '<br />';
    echo '<div class="singlebutton forumaddnew">';
    echo "<input type=\"submit\" value=\"".get_string('openauthor', 'lamstwo')."\" 
        onclick=\"openAuthor('$authorurl','author','location=0,toolbar=0,menubar=0,statusbar=0,width=996,height=700,resizable',0)\" />&nbsp;&nbsp;";
    echo "<form id=\"newlessonform\" method=\"get\" action=\"$CFG->wwwroot/mod/lamstwo/add.php\">";
    echo '<div>';
    echo "<input type=\"hidden\" name=\"customCSV\" value=\"$customcsv\" />";
    echo "<input type=\"hidden\" name=\"lamstwo\" value=\"$lamstwo->id\" />";
    echo "<input type=\"hidden\" name=\"group\" value=\"$currentgroup\" />";
    echo '<input type="submit" value="'.get_string('addlesson', 'lamstwo').'" />';
    echo '</div>';
    echo '</form>';
    echo "</div>\n";
}

// Log the lamstwo view.
add_to_log($course->id, "lamstwo", "view lamstwo", "view.php?id=$cm->id", "$lamstwo->id", $cm->id);
        
// Get raw data
if ($currentgroup != 0) {
	$lessons = get_records_select('lamstwo_lesson', 'lamstwo='.$lamstwo->id.' and (groupid='.$currentgroup.' or groupid=0)');
} else {
	$lessons = get_records('lamstwo_lesson', 'lamstwo', $lamstwo->id);
}

// Arrange data
echo '<br />';
if (!empty($lessons)) {
	$canparticipate = has_capability('mod/lams:participate', $context);

	$table->head = array(get_string('lessonname', 'lamstwo'), get_string('introduction', 'lamstwo'), get_string('links', 'lamstwo'), 'last modified');
	$table->align = array('left', 'left', 'left', 'right');

	foreach ($lessons as $lesson) {
		$links = '';
		$lessonlink = $lesson->name;
		if ($canparticipate) {
			$learnerurl = lamstwo_get_url($USER->username, $locale['lang'], $locale['country'], $lesson->lesson_id, $course->id, $course->fullname, $course->timecreated, $LAMS2CONSTANTS->learner_method);
			$learnerurl = "onclick=\"javascript:window.open('".$learnerurl."','learner','location=0,toolbar=0,menubar=0,statusbar=0,width=996,height=600,resizable',0)\"";
			$lessonlink = "<a href=\"#\" $learnerurl>$lesson->name</a>";
		}
		if ($canmanage) {
			$monitorurl = lamstwo_get_url($USER->username, $locale['lang'], $locale['country'], $lesson->lesson_id, $course->id, $course->fullname, $course->timecreated, $LAMS2CONSTANTS->monitor_method);
			$monitorurl = "onclick=\"javascript:window.open('".$monitorurl."','monitor','location=0,toolbar=0,menubar=0,statusbar=0,width=996,height=600,resizable',0)\"";
			$monitorlink = "<a href=\"#\" $monitorurl>".get_string('openmonitor', 'lamstwo')."</a>";
			$links .= $monitorlink;
			$deletelink = "<a href=\"view.php?id=$id&delete=$lesson->id\">".get_string('deletelesson', 'lamstwo')."</a>";
			$links .= "&nbsp;&nbsp;&nbsp;".$deletelink;
		}
		$table->data[] = array($lessonlink, $lesson->intro, $links, date('r', $lesson->timemodified));
	}

	print_table($table);
} else {
	echo "<div class=\"forumnodiscuss\">".get_string('nolessons', 'lamstwo')."</div>";
}

// Print footer
print_footer($course);

?>
