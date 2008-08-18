<?php

function lamstwo_backup_mods($bf,$preferences) {
       
        global $CFG;

        $status = true;

        //Iterate over lamstwo table
        $lamstwos = get_records ("lamstwo","course",$preferences->backup_course,"id");
        if ($lamstwos) {
            foreach ($lamstwos as $lamstwo) {
                if (backup_mod_selected($preferences,'lamstwo',$lamstwo->id)) {
                    $status = lamstwo_backup_one_mod($bf,$preferences,$lamstwo);
                    // backup files happens in backup_one_mod now too.
                }
            }
        }
        return $status;
    }


    function lamstwo_backup_one_mod($bf,$preferences,$lamstwo) {
    
        global $CFG;
        
        if (is_numeric($lamstwo)) {
            $lamstwo = get_record('lamstwo','id',$lamstwo);
        }
        $instanceid = $lamstwo->id;
        
        $status = true;
        
        //Start mod
        fwrite ($bf,start_tag("MOD",3,true));
        
        //Print lamstwo data
        fwrite ($bf,full_tag("ID",4,false,$lamstwo->id));
        fwrite ($bf,full_tag("MODTYPE",4,false,"lamstwo"));
        fwrite ($bf,full_tag("NAME",4,false,$lamstwo->name));
        fwrite ($bf,full_tag("INTRO",4,false,$lamstwo->intro));
        fwrite ($bf,full_tag("TIMEMODIFIED",4,false,$lamstwo->timemodified));
        
        if (backup_userdata_selected($preferences,'lamstwo',$lamstwo->id)) {
        
        }
        
        $status = backup_lamstwo_lessons ($bf,$preferences,$lamstwo->id);
        
        //End mod
        $status =fwrite ($bf,end_tag("MOD",3,true));
        return $status;
    }
    
    
    function backup_lamstwo_lessons ($bf,$preferences,$lamstwo) {     

        global $CFG;

        $status = true;

        $lamstwo_lessons = get_records("lamstwo_lesson","lamstwo",$lamstwo,"id");
        //If there are lessons
        if ($lamstwo_lessons) {
            //Write start tag
            $status =fwrite ($bf,start_tag("LESSONS",4,true));
            //Iterate over each lesson
            foreach ($lamstwo_lessons as $lesson) {
                //Start lesson
                $status =fwrite ($bf,start_tag("LESSON",5,true));
                //Print lesson contents
                fwrite ($bf,full_tag("ID",6,false,$lesson->id));
                fwrite ($bf,full_tag("NAME",6,false,$lesson->name));
                fwrite ($bf,full_tag("INTRO",6,false,$lesson->intro));
                fwrite ($bf,full_tag("GROUPID",6,false,$lesson->groupid));
                fwrite ($bf,full_tag("SEQUENCEID",6,false,$lesson->sequence_id));
                fwrite ($bf,full_tag("LESSONID",6,false,$lesson->lesson_id));
                //End lesson
                $status =fwrite ($bf,end_tag("LESSON",5,true));
            }
            //Write end tag
            $status =fwrite ($bf,end_tag("LESSONS",4,true));
        }
        return $status;
    }
    
    
    ////Return an array of info (name,value)
   function lamstwo_check_backup_mods($course,$user_data=false,$backup_unique_code,$instances=null) {
       
       if (!empty($instances) && is_array($instances) && count($instances)) {
           $info = array();
           foreach ($instances as $id => $instance) {
               $info += lamstwo_check_backup_mods_instances($instance,$backup_unique_code);
           }
           return $info;
       }
        //First the course data
        $info[0][0] = get_string('modulenameplural','lamstwo');
        if ($ids = lamstwo_ids ($course)) {
            $info[0][1] = count($ids);
        } else {
            $info[0][1] = 0;
        }

        //Lessons
        $info[1][0] = get_string('lessons','lamstwo');
        if ($ids = lamstwo_lesson_ids_by_course ($course)) {
            $info[1][1] = count($ids);
        } else {
            $info[1][1] = 0;
        }
            
        return $info;
    }
    
    
    function lamstwo_check_backup_mods_instances($instance,$backup_unique_code) {
        $info[$instance->id.'0'][0] = '<b>'.$instance->name.'</b>';
        $info[$instance->id.'0'][1] = '';
        //if (!empty($instance->userdata)) {
            $info[$instance->id.'1'][0] = get_string('lessons','lamstwo');
            if ($ids = lamstwo_lesson_ids_by_instance ($instance->id)) {
                $info[$instance->id.'1'][1] = count($ids);
            } else {
                $info[$instance->id.'1'][1] = 0;
            }
        //}
        return $info;

    }
    
    
    // INTERNAL FUNCTIONS. BASED IN THE MOD STRUCTURE

    //Returns an array of lamstwos id
    function lamstwo_ids ($course) {

        global $CFG;

        return get_records_sql ("SELECT a.id, a.course
                                 FROM {$CFG->prefix}lamstwo a
                                 WHERE a.course = '$course'");
    }
    
    //Returns an array of forum subscriptions id
    function lamstwo_lesson_ids_by_course ($course) {

        global $CFG;

        return get_records_sql ("SELECT l.id , l.forum
                                 FROM {$CFG->prefix}lamstwo_lesson l,
                                      {$CFG->prefix}lamstwo a
                                 WHERE a.course = '$course' AND
                                       l.lamstwo = a.id");
    }

    //Returns an array of forum subscriptions id 
    function lamstwo_lesson_ids_by_instance($instanceid) {
 
        global $CFG;
        
        return get_records_sql ("SELECT l.id , l.lamstwo
                                 FROM {$CFG->prefix}lamstwo_lesson l
                                 WHERE l.lamstwo = $instanceid");
    }

?>