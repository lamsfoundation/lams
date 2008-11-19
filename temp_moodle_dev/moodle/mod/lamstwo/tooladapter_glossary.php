<?php
 	
 	/**
 	 * This page is a Moodle glossary tool adapter interface for LAMS to call.  It
 	 * is secured by the LAMS - Moodle authentication mechanism.
 	 */
 	    include_once('../../config.php');
 	    include_once($CFG->libdir.'/datalib.php');
 	    include_once('../glossary/lib.php');
	
 	    $ts       = optional_param('ts', '', PARAM_TEXT);
 	    $username = optional_param('un', '', PARAM_TEXT);
 	    $hs       = optional_param('hs', '', PARAM_TEXT);
 	
 	
 	    if ( ! isset($CFG->lamstwo_serverid) || ! isset($CFG->lamstwo_serverkey) ) {
 	        header('HTTP/1.1 401 Unauthenticated');
 	        exit(1);
 	    }
 	    $plaintext = trim($ts).trim($username).trim($CFG->lamstwo_serverid).trim($CFG->lamstwo_serverkey);
 	    $hash = sha1(strtolower($plaintext));
 	    
 	     if ( $hash != $hs ){
 	        header('HTTP/1.1 401 Unauthenticated');
 	        exit(1);
 	    }
		
 	    
 	   
 	    $method     = optional_param('method', '', PARAM_TEXT);
 	    $cmid       = optional_param('extToolContentID', 0, PARAM_INT);
 	    $sectionid  = optional_param('section', 1, PARAM_INT);
 	    $courseid   = optional_param('cs', 1, PARAM_INT);
 	    $outputname = optional_param('oname', '', PARAM_TEXT);
 	
 	    $user = get_record('user', 'username', $username);
 	
 	    switch ($method) {
 	        case 'clone':
 	            $newcmid = glossary_clone_instance($cmid, $sectionid, $courseid);
 	            echo $newcmid;
 	            break;
 	        case 'import':
 	            $newcmid = glossary_import_instance($_FILES['upload_file']['tmp_name'], $user->id, $courseid, $sectionid);
 	            echo $newcmid;
 	            break;
 	        case 'export':
 	            glossary_export_instance($cmid);
 	            break;
 	        case 'export_portfolio':
 	            $text = glossary_export_portfolio($cmid, $user->id);
 	            echo $text;
 	            break;
 	        case 'output':
 	            $output = glossary_get_tool_output($cmid, $user->id);
 	            echo $output;
 	            break;
 	        default:
 	    }
 	?>