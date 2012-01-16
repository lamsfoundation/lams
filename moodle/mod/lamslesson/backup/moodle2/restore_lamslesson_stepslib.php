<?php

/**
 * @package moodlecore
 * @subpackage backup-moodle2
 * @copyright 2010 onwards Eloy Lafuente (stronk7) {@link http://stronk7.com}
 * @license   http://www.gnu.org/copyleft/gpl.html GNU GPL v3 or later
 */

/**
 * Define all the restore steps that will be used by the restore_lamslesson_activity_task
 */

/**
 * Structure step to restore one lamslesson activity
 */
class restore_lamslesson_activity_structure_step extends restore_activity_structure_step {

    protected function define_structure() {

        $paths = array();
        $userinfo = $this->get_setting_value('userinfo');

        $paths[] = new restore_path_element('lamslesson', '/activity/lamslesson');
        if ($userinfo) {
		// No need to restore userdata as that's already in LAMS
        }

        // Return the paths wrapped into standard activity structure
        return $this->prepare_activity_structure($paths);
    }

    protected function process_lamslesson($data) {
        global $DB;

        $data = (object)$data;
        $oldid = $data->id;
        $data->course = $this->get_courseid();

        $data->timemodified = $this->apply_date_offset($data->timemodified);

        // insert the lamslesson record
        $newitemid = $DB->insert_record('lamslesson', $data);
        // immediately after inserting "activity" record, call this
        $this->apply_activity_instance($newitemid);
    }

    protected function after_execute() {
        // Add lamslesson related files, no need to match by itemname (just internally handled context)
        $this->add_related_files('mod_lamslesson', 'intro', null);
    }
}
