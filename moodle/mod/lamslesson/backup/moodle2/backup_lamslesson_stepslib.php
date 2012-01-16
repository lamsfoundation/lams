<?php

/**
 * @package moodlecore
 * @subpackage backup-moodle2
 * @copyright 2010 onwards Eloy Lafuente (stronk7) {@link http://stronk7.com}
 * @license   http://www.gnu.org/copyleft/gpl.html GNU GPL v3 or later
 */

/**
 * Define all the backup steps that will be used by the backup_lamslesson_activity_task
 */

/**
 * Define the complete lamslesson structure for backup, with file and id annotations
 */
class backup_lamslesson_activity_structure_step extends backup_activity_structure_step {

    protected function define_structure() {

        // To know if we are including userinfo
        $userinfo = $this->get_setting_value('userinfo');

        // Define each element separated
        $lamslesson = new backup_nested_element('lamslesson', array('id'), array(
            'course', 'name', 'intro', 'introformat', 'groupid',
            'grade', 'sequence_id', 'lesson_id', 'timemodified',
            'completionfinished', 'displaydesign'));

        // Define sources
        $lamslesson->set_source_table('lamslesson', array('id' => backup::VAR_ACTIVITYID));

        // All the rest of elements only happen if we are including user info
        if ($userinfo) {
		// there's no need to do anything here as all the userdata is in LAMS
        }

        // Return the root element (lamslesson), wrapped into standard activity structure
        return $this->prepare_activity_structure($lamslesson);
    }
}
