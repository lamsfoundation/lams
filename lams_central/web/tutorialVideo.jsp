<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<!DOCTYPE html>
<html>
<head>
	<!-- This page is used to display a tutorial video. Content and other parameters are passed in request. -->
	
	<c:url var="tutorialActionUrl" value="tutorial.do" />
	
   	<lams:css/>
   	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript">
		var doNotShowAgainInitialValue = ${param.doNotShowAgain};
		
		function closeTutorial(){
			//If the user changed the checkbox value,
			// we either add the video to user's disabled list or remove it from there.
			if (doNotShowAgainInitialValue){
			 		if (! $("#doNotShowAgain").attr("checked")){
				 		$.ajax({		
							type: "GET",
							url: "${tutorialActionUrl}",
							data: {method : "enableSingleTutorialVideo", pageString : "${param.pageString}"},
							cache: false
						});
			 		}
			 	}
			 	else {
			 		if ($("#doNotShowAgain").attr("checked")){
			 			alert("<fmt:message key="label.tutorial.disable.single" />");
			 			
				 		$.ajax({		
							type: "GET",
							url: "${tutorialActionUrl}",
							data: {method : "disableSingleTutorialVideo", pageString : "${param.pageString}"},
							cache: false
						});
			 		}
			 	}
			 	
			 self.parent.tb_remove();
		}
			
		$(document).ready(function(){
				/* 
				   This call is needed to update the checkbox state.
				   First attempt was to mark the checkbox checked/unchecked statically, based on parameters passed in request.
				   It didn't work out, since parent page parameters stay the same
				   (after closing thickbox, parent page is not updated)
				   and state might have been changed by the user.
				   That's why we need to check it each time we enter this thickbox.
				*/
	 			$.get(
	 				"${tutorialActionUrl}",
					{method : "getDoNotShowAgainValue", pageString : "${param.pageString}"},
					function (data, textStatus) {
						// Comparing with "==" did not work
						doNotShowAgainInitialValue = data.indexOf("true")>-1;
						$("#doNotShowAgain").attr("checked",doNotShowAgainInitialValue);
 					},
					"text"
				);
		});
	</script>
</head>
<body>
	<h2 style="text-align: center">${param.title}</h2>
   <div style="text-align: center">
    <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width="${param.videoWidth}" height="${param.videoHeight}">
       <param name="movie" value="${param.videoUrl}" />
       <param name="quality" value="best" />
       <param name="bgcolor" value="#1a1a1a" />
       <param name="allowfullscreen" value="false" />
       <param name="scale" value="showall" />
       <param name="allowscriptaccess" value="always" />
       <param name="flashvars" value="autostart=true&color=0x000000,0x000000" />
       <!--[if !IE]>-->
       <object type="application/x-shockwave-flash" data="${param.videoUrl}" width="${param.videoWidth}" height="${param.videoHeight}">
           <param name="quality" value="best" />
           <param name="bgcolor" value="#1a1a1a" />
           <param name="allowfullscreen" value="false" />
           <param name="scale" value="showall" />
           <param name="allowscriptaccess" value="always" />
           <param name="flashvars" value="autostart=true&color=0x000000,0x000000" />
       <!--<![endif]-->
           <div id="noUpdate">
               <p>The Camtasia Studio video content presented here requires a more recent version of the Adobe Flash Player. If you are you using a browser with JavaScript disabled please enable it now. Otherwise, please update your version of the free Flash Player by <a href="http://www.adobe.com/go/getflashplayer">downloading here</a>.</p>
           </div>
       <!--[if !IE]>-->
       </object>
       <!--<![endif]-->
    </object>
   </div>
   <div style="float: left;">
	   <input id="doNotShowAgain" style="margin: 12px 0px 0px 15px;" type="checkbox"><fmt:message key="label.tutorial.video.never.show.again" /></input>
   </div>
   <a class="button" style="margin: 8px 10px 0px 0px; float: right;" href="javascript:closeTutorial();"><fmt:message key="button.close" /></a>
</body>
<html>