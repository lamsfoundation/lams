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

    // Servers time offset
    $settings->add(new admin_setting_configtext('lamslesson_servertimeoffset', get_string('servertimeoffset', 'lamslesson'),
                       get_string('servertimeoffsetinfo', 'lamslesson'), '0'));



    // Sync LAMS server time
    $html =  html_writer::tag('p', get_string('offsetinfo', 'lamslesson'));
    $html .=  html_writer::start_tag('div', array('style' => 'text-align:center;padding-top: 15px; padding-bottom:5px;'));
    $html .=  html_writer::start_tag('span', array('id' => 'offsetbutton', 'class' => 'yui3-button yui3-link-button'));

    $html .= html_writer::tag('a', get_string('offsetbutton', 'lamslesson'),
                array('id' => 'offsetbtn',
                        'name' => 'offsetbtn',
                        'href' => 'javascript:calculateOffset();'));
    $html .= html_writer::end_tag('span');
    $html .= html_writer::end_tag('div');

    $settings->add(new admin_setting_heading('lamslesson_offset', get_string('offsetheader', 'lamslesson'),
                       $html));



    // Validation button

    $html = html_writer::script('', $CFG->wwwroot.'/mod/lamslesson/validate.js');
    $html .=  html_writer::tag('p', get_string('validationinfo', 'lamslesson')); 
    $html .=  html_writer::start_tag('div', array('style' => 'text-align:center;padding-top: 15px; padding-bottom:5px;'));
    $html .=  html_writer::start_tag('span', array('id' => 'validatebutton', 'class' => 'yui3-button yui3-link-button'));
    $html .=  html_writer::tag('a', get_string('validationbutton', 'lamslesson'), 
		array('id' => 'validatebtn', 
			'name' => 'validatebtn', 
			'href' => 'javascript:validate();'));
    $html .= html_writer::end_tag('span');
    $html .= html_writer::end_tag('div');



    $html .= html_writer::tag('div', get_string('validationhelp', 'lamslesson') . ' ' 
		.html_writer::link('http://wiki.lamsfoundation.org/display/lamsdocs/Moodle2',  get_string('lamsmoodlehelp', 'lamslesson'), 
					array('target' => '_blank')), 
		array('style' => 'text-align: right; font-size: 10px;', 'target' => '_blank'));

    $settings->add(new admin_setting_heading('lamslesson_validation', get_string('validationheader', 'lamslesson'),
                       $html));
    $html .= html_writer::end_tag('div');




}

