
<?php  // $Id$

require_once('../../config.php');
require_once('lib.php');

$id = optional_param('id', 0, PARAM_INT);    // Course Module ID

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
	$customcsv = "$USER->username,$course->id,$cm->section";
    $authorurl = lamstwo_get_url($USER->username, $locale['lang'], $locale['country'], 0, $course->id, $LAMS2CONSTANTS->author_method, $customcsv);
	
	echo $openauthorjs;

	echo '<br />';
	echo '<div class="singlebutton forumaddnew">';
	echo "<input type=\"submit\" value=\"".get_string('openauthor', 'lamstwo')."\" 
		onclick=\"openAuthor('$authorurl','author','location=0,toolbar=0,menubar=0,statusbar=0,width=796,height=570,resizable',0)\" />&nbsp;&nbsp;";
	echo "<form id=\"newlessonform\" method=\"get\" action=\"$CFG->wwwroot/mod/lamstwo/add.php\">";
	echo '<div>';
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
			$learnerurl = lamstwo_get_url($USER->username, $locale['lang'], $locale['country'], $lesson->lesson_id, $course->id, $LAMS2CONSTANTS->learner_method);
			$learnerurl = "onclick=\"javascript:window.open('".$learnerurl."','learner','location=0,toolbar=0,menubar=0,statusbar=0,width=796,height=570,resizable',0)\"";
			$lessonlink = "<a href=\"#\" $learnerurl>$lesson->name</a>";
		}
		if ($canmanage) {
			$monitorurl = lamstwo_get_url($USER->username, $locale['lang'], $locale['country'], $lesson->lesson_id, $course->id, $LAMS2CONSTANTS->monitor_method);
			$monitorurl = "onclick=\"javascript:window.open('".$monitorurl."','monitor','location=0,toolbar=0,menubar=0,statusbar=0,width=796,height=570,resizable',0)\"";
			$monitorlink = "<a href=\"#\" $monitorurl>".get_string('openmonitor', 'lamstwo')."</a>";
			$links .= $monitorlink;
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
