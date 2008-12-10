 <html>  
 <head>  
 <title>LAMS frame</title>  
 <style type="text/css">  
	 <!--  
	 #next{ 
		 display: none;  
	 }  
	 #hidden {   
	     display: none; 
	 }  

	  body {   
	    background-color:#D8E4F1;
	
	 }  
	 -->  
 </style>  
 <script language="JavaScript">  
	<?php  require_once("../config.php");
		$id   = optional_param('id', PARAM_INT);
		$lamsupdateurl = optional_param('lamsUpdateURL', PARAM_TEXT);
		$returnurl   = optional_param('returnUrl', PARAM_TEXT);  // lams url to proceed to next in sequence 
		$isfinished  = optional_param('isfinished',false, PARAM_TEXT); //if you have done the activity at least once the variable will be true
	?> 		/* Function to display Next or Finish button in LAMS sequence*/
			function toggle(id) {  
            	var state = document.getElementById(id).style.display;  
                if (state == 'block') {  
                    document.getElementById(id).style.display = 'block';  
                } else {  
                    document.getElementById(id).style.display = 'block';  
              	}  
            }
            /* Function to return to Lams the new ID and come back to LAMS page*/
			 function returnlams (url,id){
            	window.parent.location=url+'&extToolContentID='+id;	
            }
             /* Function that permits send the new ID when you onClick the LAMS button*/
             function changeid(iddiv,id) {  
             		document.getElementById(iddiv).onclick=function(){returnlams('<?php  echo(urldecode($lamsupdateurl))?>',id)};
           }
        
  </script>  
 </head>  
 <body>     

	<div id="hidden" align="right">
	<?php  //Creates button that passes lamsupdateurl variable to be able to come back to Lams pages 
		echo('<input id="returnbutton" type="button" value= "Finished and back to LAMS" onclick="window.parent.location=\''.$lamsupdateurl.'\'" />');
	?>
	</div>
	
	<div id="next">
	<?php //Creates button that passes returnurl variable to be able to go to the Lams Next Activity
		echo '<div align="right"><p><input id="nextbutton" type="button" value="Next Activity" onclick="window.parent.location=\''.$returnurl.'\'" /></p></div>';
		if($isfinished=='true'){//if you have done the activity at least once let trhe Next Activity button appeare
			echo('<script>');
			echo('toggle("next");');
			echo('</script>');
		}
	?>
	</div>
 </body>  
</html>  


