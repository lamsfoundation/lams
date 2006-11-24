<?php // $Id$
function migrate2utf8_lamstwo_name($recordid){
    global $CFG, $globallang;

/// Some trivial checks
    if (empty($recordid)) {
        log_the_problem_somewhere();
        return false;
    }

    if (!$lamstwo = get_record('lamstwo','id',$recordid)) {
        log_the_problem_somewhere();
        return false;
    }

    if ($globallang) {
        $fromenc = $globallang;
    } else {
        $sitelang   = $CFG->lang;
        $courselang = get_course_lang($lamstwo->course);  //Non existing!
        $userlang   = get_main_teacher_lang($lamstwo->course); //N.E.!!

        $fromenc = get_original_encoding($sitelang, $courselang, $userlang);
    }

/// We are going to use textlib facilities
    
/// Convert the text
    if (($fromenc != 'utf-8') && ($fromenc != 'UTF-8')) {
        $result = utfconvert($lamstwo->name, $fromenc);

        $newlamstwo = new object;
        $newlamstwo->id = $recordid;
        $newlamstwo->name = $result;
        migrate2utf8_update_record('lamstwo',$newlamstwo);
    }
/// And finally, just return the converted field
    return $result;
}

function migrate2utf8_lamstwo_introduction($recordid){
    global $CFG, $globallang;

/// Some trivial checks
    if (empty($recordid)) {
        log_the_problem_somewhere();
        return false;
    }

    if (!$lamstwo = get_record('lamstwo','id',$recordid)) {
        log_the_problem_somewhere();
        return false;
    }
    if ($globallang) {
        $fromenc = $globallang;
    } else {
        $sitelang   = $CFG->lang;
        $courselang = get_course_lang($lamstwo->course);  //Non existing!
        $userlang   = get_main_teacher_lang($lamstwo->course); //N.E.!!

        $fromenc = get_original_encoding($sitelang, $courselang, $userlang);
    }

/// We are going to use textlib facilities
    
/// Convert the text
    if (($fromenc != 'utf-8') && ($fromenc != 'UTF-8')) {
        $result = utfconvert($lamstwo->introduction, $fromenc);

        $newlamstwo = new object;
        $newlamstwo->id = $recordid;
        $newlamstwo->introduction = $result;
        migrate2utf8_update_record('lamstwo',$newlamstwo);
    }
/// And finally, just return the converted field
    return $result;
}
?>
