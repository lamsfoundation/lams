<?php
	//  adds or updates modules in a course using new formslib
    
    require_once("../../config.php");
    require_once("lib.php");
    require_once($CFG->libdir.'/gradelib.php');
    
    require_login();

    $add           = optional_param('add', 0, PARAM_ALPHA);
    $update        = optional_param('update', PARAM_INT);
    $return        = optional_param('return', 0, PARAM_BOOL); //return to course/view.php if false or mod/modname/view.php if true
    $course          = optional_param('course', '', PARAM_ALPHANUM);
    $type          = optional_param('type', '', PARAM_ALPHANUM);
    $section          = optional_param('section', '', PARAM_ALPHANUM);
    $is_learner  = optional_param('is_learner', 0, PARAM_INT);
    $returnurl = urlencode(optional_param('returnUrl', PARAM_TEXT));
    $id        = optional_param('id',0, PARAM_INT);
	$nexturl = "../../course/modedit-lams.php?add=".$add."&course=".$course."&section=".$section."&return=".$return."&editing=1";
	
	if($id!=0){
		redirect($nexturl."&type=".$type."&update=".$update);
	
	}else{
			print("<html >");
			print("<head>");
			print("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />");
			print("<title>Untitled Document</title>");
			print("</head>");
			print("<body>");	
			print("<br> </br> <br> </br>");
			print("<font face='Verdana'color='#333333' size=+1 >");
			print("<center> Choose the type of Assignment you prefer <br> </br>");
			print("<form name='chooseType' method='POST' action='".$nexturl."'>");
			
			print("<select name='type'>");
			print("<option value='upload'>Advanced uploading of files</option>");
			print("<option value='online'>Online text</option>");
			print("<option value='uploadsingle'>Upload a single file</option>");
			print("<option value='offline'>Offline activity</option>");
			print("</select>");
			print("<t> </t>");
			print("<input type='submit' value='Select Assignment type'>");
			print("</form> ");
			print("</center>");
			print("</font>");
			print("</body>");
			print("</html>");
	}
	
	
	
	
	

?>