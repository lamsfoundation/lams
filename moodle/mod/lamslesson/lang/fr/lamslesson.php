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
 * French strings for lamslesson
  *
   * You can have a rather longer description of the file as well,
    * if you like, and it can span multiple lines.
     *
      * @package   mod_lamslesson
       * @copyright 2011 LAMS Foundation - Ernie Ghiglione (ernieg@lamsfoundation.org) 
        * @license  http://www.gnu.org/licenses/gpl-2.0.html GNU GPL v2
         */

         defined('MOODLE_INTERNAL') || die();

         $string['modulename'] = 'Leçon LAMS';
         $string['modulenameplural'] = 'Leçons LAMS';
         $string['modulename_help'] = 'Le module LAMS leçon permet aux enseignants de créer des leçons LAMS dans Moodle.';
         $string['modulename_link'] = 'lamslesson';
         $string['lamslessonfieldset'] = 'Custom example fieldset';
         $string['lamslessonname'] = 'Nom de leçon';
         $string['lamslessonname_help'] = 'Leçon LAMS';
         $string['lamslesson'] = 'Leçon LAMS';
         $string['pluginadministration'] = 'Administration leçon LAMS';
         $string['pluginname'] = 'Leçon LAMS';
         $string['selectsequence'] = 'Sélection d´une séquence';
         $string['displaydesign'] = 'Afficher image du design?';
         $string['allowlearnerrestart'] = 'Learners can restart the lesson?';
         $string['availablesequences'] = 'Séquences';
         $string['openauthor'] = 'Créer nouvelle leçon LAMS';

         // Capabilities
         $string['lamslesson:manage'] = 'Gestion leçons';
         $string['lamslesson:participate'] = 'Suivre une leçon';


         // Admin interface
         $string['adminheader'] = 'Configuration LAMS Server';
         $string['admindescription'] = 'Configure your LAMS server settings. Make <strong>sure</strong> that the values you enter here correspond with the once you already entered in your LAMS server. Otherwise the integration might not work (pas traduit en français).';

         $string['serverurl'] = 'LAMS Server URL:';
         $string['serverurlinfo'] = 'Here you need to enter the URL for your LAMS server. ie: http://localhost:8080/lams/.';

         $string['serverid'] = 'Server ID:';
         $string['serveridinfo'] = 'What is the Server ID you entered in your LAMS server?';

         $string['serverkey'] = 'Server Key:';
         $string['serverkeyinfo'] = 'What is the Server Key you entered in your LAMS server?';

         $string['validationbutton'] = "Validate settings";
         $string['validationheader'] = 'Settings validation';
         $string['validationinfo'] = 'Before you save your settings, please press the button to validate them with the LAMS server. If the validation is correct, save these settings. If not, please check that the settings you have entered match with the values in the LAMS server';
         $string['validationhelp'] = 'Need help? check out the';
         $string['lamsmoodlehelp'] = 'LAMS-Moodle integration tutorial';

         $string['validationsuccessful'] = 'Validation successful! You can now save your settings and start using LAMS within Moodle.';
         $string['validationfailed'] = 'Validation failed: please check that the settings you have entered match with the settings in LAMS';
         //


         // Labels for errors when calling LAMS Server
         $string['restcallfail'] = 'Call to LAMS failed: received no response or connection was refused. Please check that you have the correct LAMS server URL and that it is online.';
         $string['sequencenotselected'] = 'You must select a sequence to proceed.';
         $string['previewthislesson'] = 'Preview this lesson';
         $string['updatewarning'] = 'Warning: selecting another sequence than the current one will create a new lesson for the students. This might result in some slightly confused students';
         $string['currentsequence'] = 'Current sequence:';

         // view.php

         $string['nolessons'] = 'Il y n´a pas de leçons LAMS dans cette instance.';
         $string['lessonname'] = 'Nom de la leçon';
         $string['links'] = 'Liens';
         $string['introduction']  = 'Introduction';
         $string['openmonitor'] = 'Suivi (monitoring) de la leçon';
         $string['lastmodified'] = 'Dernière modification';
         $string['openlesson'] = 'Ouvrir leçon';
         $string['empty'] = 'vide';
         $string['completionfinish'] = 'Afficher comme complété une fois que l´utilisateur a terminé la leçon';
         $string['yourprogress'] = 'Progrès avec la leçon';
         $string['youhavecompleted'] = 'Vous avez terminé: ';
         $string['outof'] = 'sur approximativement';
         $string['lessonincompleted'] = 'la leçon n´est pas encore terminé';
         $string['lessoncompleted'] = 'Vous avez terminé la leçon';
         $string['activities'] = 'activités';
         $string['ymmv'] = 'Le total des activités dépend de votre chemin d´apprentissage.';
         $string['yourmarkis'] = 'Votre score/note final est de:';
         $string['outofmark'] = 'sur';
