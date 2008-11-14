<?php

	function lamstwo_restore_mods($mod,$restore) {
        global $CFG;
        $status = true;

        //Get record from backup_ids
        $data = backup_getid($restore->backup_unique_code, $mod->modtype, $mod->id);

        if ($data) {
            //Now get completed xmlized object
            $info = $data->info;
            
            //traverse_xmlize($info);                                                                     //Debug
            //print_object ($GLOBALS['traverse_array']);                                                  //Debug
            //$GLOBALS['traverse_array']="";                                                              //Debug
            
            //Now, build the LAMSTWO record structure
            $lamstwo->course = $restore->course_id;
            $lamstwo->name = backup_todb($info['MOD']['#']['NAME']['0']['#']);
            $lamstwo->intro = backup_todb($info['MOD']['#']['INTRO']['0']['#']);
            $lamstwo->timemodified = backup_todb($info['MOD']['#']['TIMEMODIFIED']['0']['#']);
            
            $lamstwo->id = insert_record('lamstwo', $lamstwo);
            
            //Do some output
            if (!defined('RESTORE_SILENTLY')) {
                echo "<li>".get_string('modulename','lamstwo')." \"".format_string(stripslashes($lamstwo->name), true)."\"</li>";
            }
            backup_flush(300);
            
            if ($lamstwo->id) {
                //We have the newid, update backup_ids
                backup_putid($restore->backup_unique_code, $mod->modtype, $mod->id, $lamstwo->id);
                
                //Restore userdata?
                $restoreuserdata = restore_userdata_selected($restore, 'lamstwo', $mod->id);
                
                //Restore lamstwo_lessons
                $status = lamstwo_lessons_restore_mods ($lamstwo->id, $info, $restore, $restoreuserdata);
            } else {
                $status = false;
            }
        }

        return $status;
    }
    
    /**
     * Since this method contains links to LAMS sequence and lesson ids, it requires the original LAMS
     * server to be integrated with this Moodle instance.
     */
    function lamstwo_lessons_restore_mods($lamstwoid, $info, $restore, $restoreuserdata=true) {
        global $CFG;
        $status = true;
    
        // Put lessons part of XML into array
        $lessons = array();
        if (isset($info['MOD']['#']['LESSONS']['0']['#']['LESSON'])) {
            $lessons = $info['MOD']['#']['LESSONS']['0']['#']['LESSON'];
        }
        
        // output <ul>'s
        if (!defined('RESTORE_SILENTLY')) {
            echo '<ul>';
        }
        
        // Iterate over lessons
        for ($i=0; $i<sizeof($lessons); $i++) {
            $lesson = $lessons[$i];
            $oldid = $lesson['#']['ID']['0']['#'];
            
            $newlesson->course       = $restore->course_id;
            $newlesson->lamstwo      = $lamstwoid; 
            $newlesson->name         = backup_todb($lesson['#']['NAME']['0']['#']);
            $newlesson->intro        = backup_todb($lesson['#']['INTRO']['0']['#']);
            $newlesson->groupid      = backup_todb($lesson['#']['GROUPID']['0']['#']);
            $newlesson->sequence_id  = backup_todb($lesson['#']['SEQUENCE_ID']['0']['#']);
            $newlesson->lesson_id    = backup_todb($lesson['#']['LESSON_ID']['0']['#']);
            $newlesson->timemodified = backup_todb($lesson['#']['TIMEMODIFIED']['0']['#']);
            
            if ($restoreuserdata) {
                // use existing lams lesson
            	$newlesson->id = insert_record('lamstwo_lesson', $newlesson);
            } else {
                // create new lams lesson based on sequence_id
                require_once($CFG->dirroot.'/mod/lamstwo/lib.php');
                
                $newlesson->customCSV = '';
                
                $newlesson->id = lamstwo_add_lesson($newlesson);
                
                // do some output
                if (!defined('RESTORE_SILENTLY')) {
                    if ($newlesson->id) {
                        echo '<li>'.get_string('addedlesson','lamstwo').' "'.format_string(stripslashes($newlesson->name), true).'"</li>';
                    } else {
                        echo '<li>'.get_string('failedaddlesson','lamstwo').' "'.format_string(stripslashes($newlesson->name), true).'"</li>';
                    }
                    backup_flush(300);
                }
            }
        
            if ($newlesson->id) {
                //We have the newid, update backup_ids
                backup_putid($restore->backup_unique_code, 'lamstwo_lesson', $oldid, $newlesson->id);
            } else {
                $status = false;
            }
        }
        
        // output </ul>'s
        if (!defined('RESTORE_SILENTLY')) {
            echo '</ul>';
        }
        
        return $status;
    }

?>