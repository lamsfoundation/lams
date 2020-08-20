<?php

/**
 * The mod_lamslesson course module viewed event.
 *
 * @package    mod_lamslesson
 * @copyright  2020 LAMS Foundation
 * @license    http://www.gnu.org/copyleft/gpl.html GNU GPL v2
 *
 * Author: Francisco Hernan Aravena Oñate <fraaravena@utalca.cl>
 */

namespace mod_lamslesson\event;

defined('MOODLE_INTERNAL') || die();

/**
 * The mod_lamslesson course module viewed event class.
 *
 * @package    mod_quiz
 * @since      Moodle 3.9
 * @copyright  2020 LAMS Foundation
 * @license    http://www.gnu.org/copyleft/gpl.html GNU GPL v2
 */
class course_module_viewed extends \core\event\course_module_viewed {

    /**
     * Init method.
     *
     * @return void
     */
    protected function init() {
        $this->data['crud'] = 'r';        
        $this->data['objecttable'] = 'lamslesson';
        $this->data['edulevel'] = self::LEVEL_PARTICIPATING;
    }    

    /**
     * This is used when restoring course logs where it is required that we
     * map the objectid to it's new value in the new course.
     *
     * @return array
     */
    public static function get_objectid_mapping() {
        return ['db' => 'lamslesson', 'restore' => 'lamslesson'];
    }

    /**
     * Replace add_to_log() statement.
     *
     * @return array of parameters to be passed to legacy add_to_log() function.
     */
    protected function get_legacy_logdata() {
        return array($this->courseid, 'lamslesson', 'pre-view', 'view.php?id=' . $this->contextinstanceid, $this->objectid, $this->contextinstanceid);
    }

    // public static function get_objectid_mapping() {
    //     return array('db' => 'lamslesson', 'restore' => 'lmslesson');
    // }
}
