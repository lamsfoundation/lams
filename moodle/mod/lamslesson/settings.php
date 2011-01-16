<?php

// This file is part of Moodle - http://moodle.org/
//
// Moodle is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// Moodle is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with Moodle.  If not, see <http://www.gnu.org/licenses/>.

/**
 * @package mod-lamslesson
 * @copyright  2011 LAMS Foundation - Ernie Ghiglione (ernieg@lamsfoundation.org) 
 * @license   http://www.gnu.org/licenses/gpl-2.0.html GNU GPL v2
 */

defined('MOODLE_INTERNAL') || die;

if ($ADMIN->fulltree) {
    require_once($CFG->dirroot.'/mod/lamslesson/lib.php');

    $settings->add(new admin_setting_heading('lamslesson_heading', get_string('adminheader', 'lamslesson'),
                       get_string('admindescription', 'lamslesson')));

    // Server URL 
    $settings->add(new admin_setting_configtext('lamslesson_serverurl', get_string('serverurl', 'lamslesson'),
                       get_string('serverurlinfo', 'lamslesson'), ''));

    // Server id 
    $settings->add(new admin_setting_configtext('lamslesson_serverid', get_string('serverid', 'lamslesson'),
                       get_string('serveridinfo', 'lamslesson'), ''));

    // Server key
    $settings->add(new admin_setting_configtext('lamslesson_serverkey', get_string('serverkey', 'lamslesson'),
                       get_string('serverkeyinfo', 'lamslesson'), ''));

    // requestsource
    $settings->add(new admin_setting_configtext('lamslesson_requestsource', get_string('requestsource', 'lamslesson'),
                       get_string('requestsourceinfo', 'lamslesson'), 'My Moodle'));


    // Validation button
    $html = <<<XXX
Here is where the button will go get_string('validationbutton', 'lamslesson') <strong>bold</strong>
XXX;

    $settings->add(new admin_setting_heading('lamslesson_validation', get_string('validationinfo', 'lamslesson'),
                       $html));
}

