<?php

// This file keeps track of upgrades to 
// the forum module
//
// Sometimes, changes between versions involve
// alterations to database structures and other
// major things that may break installations.
//
// The upgrade function in this file will attempt
// to perform all the necessary actions to upgrade
// your older installtion to the current version.
//
// If there's something it cannot do itself, it
// will tell you what you need to do.
//
// The commands in here will all be database-neutral,
// using the functions defined in lib/ddllib.php

function xmldb_lamstwo_upgrade($oldversion=0) {

    $result = true;

    if ($result && $oldversion < 2008052100) {
    
        // LDEV-1435
        $table = new XMLDBTable('lamstwo');
        $field = new XMLDBField('introduction');
        $field->setAttributes(XMLDB_TYPE_TEXT, 'small', null, true, null, null, null, null, null);
        $result = change_field_default($table, $field);
        
        // save lamstwo data
        $lamstwos = get_records('lamstwo');
        
        // modify lamstwo table
        $result = $result && rename_field($table, $field, 'intro');
        $result = $result && drop_field($table, new XMLDBField('sequence_id'));
        $result = $result && drop_field($table, new XMLDBField('lesson_id'));
        
        // add table lamstwo_lesson
        $table = new XMLDBTable('lamstwo_lesson');
        
        $field = new XMLDBField('id');
        $field->setAttributes(XMLDB_TYPE_INTEGER, 10, true, true, true, null, null, null, null);
        $table->addField($field);
        
        $field = new XMLDBField('course');
        $field->setAttributes(XMLDB_TYPE_INTEGER, 10, true, true, null, null, null, 0, null);
        $table->addField($field);
        
        $field = new XMLDBField('lamstwo');
        $field->setAttributes(XMLDB_TYPE_INTEGER, 10, true, true, null, null, null, 0, null);
        $table->addField($field);
        
        $field = new XMLDBField('name');
        $field->setAttributes(XMLDB_TYPE_CHAR, 255, null, true, null, null, null, null, null);
        $table->addField($field);
        
        $field = new XMLDBField('intro');
        $field->setAttributes(XMLDB_TYPE_TEXT, 'small', null, true, null, null, null, null, null);
        $table->addField($field);
        
        $field = new XMLDBField('groupid');
        $field->setAttributes(XMLDB_TYPE_INTEGER, 20, null, null, null, null, null, 0, null);
        $table->addField($field);
        
        $field = new XMLDBField('sequence_id');
        $field->setAttributes(XMLDB_TYPE_INTEGER, 20, true, null, null, null, null, 0, null);
        $table->addField($field);
        
        $field = new XMLDBField('lesson_id');
        $field->setAttributes(XMLDB_TYPE_INTEGER, 20, true, null, null, null, null, 0, null);
        $table->addField($field);
        
        $field = new XMLDBField('timemodified');
        $field->setAttributes(XMLDB_TYPE_INTEGER, 10, true, true, null, null, null, 0, null);
        $table->addField($field);
        
        $key = new XMLDBKey('primary_lamstwo_lesson');
        $key->setAttributes(XMLDB_KEY_PRIMARY, array('id'));
        $table->addKey($key);
        
        $index = new XMLDBIndex('course_lamstwo_lesson');
        $index->setAttributes(XMLDB_INDEX_NOTUNIQUE, array('course'));
        $table->addIndex($index);
        
        $result = $result && create_table($table);
        
        // insert lamstwo data into lamstwo_lesson table
        foreach ($lamstwos as $lamstwoid => $lamstwo) {
            $lesson->course       = $lamstwo->course;
            $lesson->lamstwo      = $lamstwoid;
            $lesson->name         = str_replace("'", "\'", $lamstwo->name);
            $lesson->intro        = str_replace("'", "\'", $lamstwo->introduction);
            $lesson->sequence_id  = $lamstwo->sequence_id;
            $lesson->lesson_id    = $lamstwo->lesson_id;
            $lesson->timemodified = $lamstwo->timemodified;
            $lesson->id = insert_record('lamstwo_lesson', $lesson);
        }
        
        // add table lamstwo_grade
        $table = new XMLDBTable('lamstwo_grade');
        
        $field = new XMLDBField('id');
        $field->setAttributes(XMLDB_TYPE_INTEGER, 10, true, true, true, null, null, null, null);
        $table->addField($field);
        
        $field = new XMLDBField('lamstwolesson');
        $field->setAttributes(XMLDB_TYPE_INTEGER, 10, true, true, null, null, null, 0, null);
        $table->addField($field);
        
        $field = new XMLDBField('user');
        $field->setAttributes(XMLDB_TYPE_INTEGER, 10, true, true, null, null, null, 0, null);
        $table->addField($field);
        
        $field = new XMLDBField('completed');
        $field->setAttributes(XMLDB_TYPE_INTEGER, 1, true, null, null, null, null, 0, null);
        $table->addField($field);
        
        $key = new XMLDBKey('primary_lamstwo_grade');
        $key->setAttributes(XMLDB_KEY_PRIMARY, array('id'));
        $table->addKey($key);
        
        $result = $result && create_table($table);
    }
    
    // LDEV-1965 add more meaningful log info
    if ($result && $olversion < 2009033000) {
      $log_display->module = 'lamstwo';
      $log_display->action = 'add';
      $log_display->mtable = 'lamstwo';
      $log_display->field = 'name';
      insert_record('log_display', $log_display);
      
      $log_display->action = 'add lesson';
      $log_display->mtable = 'lamstwo_lesson';
      insert_record('log_display', $log_display);
      
      $log_display->action = 'update';
      $log_display->mtable = 'lamstwo';
      insert_record('log_display', $log_display);
      
      $log_display->action = 'view lamstwo';
      insert_record('log_display', $log_display);
    }

    return $result;
}

?>
