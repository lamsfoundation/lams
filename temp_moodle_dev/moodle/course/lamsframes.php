<html><head>
<title>lamsframes</title>
</head>
 <script language="JavaScript">  
<?php
	require_once("../config.php");
	require_once($CFG->dirroot.'/mod/quiz/lib.php');
	$id   = optional_param('id',0, PARAM_INT);
	$lamsupdateurl = urlencode(optional_param('lamsUpdateURL', PARAM_TEXT));
	$returnurl = urlencode(optional_param('returnUrl', PARAM_TEXT));
    $dest = optional_param('dest', PARAM_TEXT);
    $section = optional_param('section', PARAM_TEXT);
    $courseid = optional_param('course', PARAM_TEXT);
    $is_learner  = optional_param('is_learner',0, PARAM_INT);
    if($id==0){
    	?>
        /*Lams navigation buttons frame */
    	document.write('<frameset rows="7%,*" >');
		document.write('<frame name="button" src="lamsbuttons.php?<?php echo('&amp;lamsUpdateURL='.$lamsupdateurl.'&amp;returnUrl='.$returnurl)?>"  scrolling="no" noresize>'); 
	 	document.write('<frame name="contenido" src="<?php echo($CFG->wwwroot.'/'.$dest.'?add=quiz&course='.$courseid.'&section='.$section.'&is_learner='.$is_learner)?>" scrolling="yes" noresize>');
		document.write('</frameset>');
    	
    	<?php 
    }else{
    	
    	?>
    	/*Contents frame */ 
    	document.write('<frameset rows="7%,*" >');
		document.write('<frame name="button" src="lamsbuttons.php?<?php echo('id='.$id.'&amp;lamsUpdateURL='.$lamsupdateurl.'&amp;returnUrl='.$returnurl)?>"  scrolling="no" noresize>'); 
	 	document.write('<frame name="contenido" src="<?php echo($CFG->wwwroot.'/'.$dest.'?id='.$id.'&update='.$id.'&is_learner='.$is_learner)?>" scrolling="yes" noresize>');
		document.write('</frameset>');
    	
    	<?php 
    }
    ?>
  </script>  
  <body>
  </body>
</html>


