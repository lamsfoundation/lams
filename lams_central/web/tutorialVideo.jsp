<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<c:url var="tutorialActionUrl" value="tutorial.do" />

<!DOCTYPE html>
<html>
<head>
	<!-- This page is used to display a tutorial video. Content and other parameters are passed in request. -->
   	<lams:css/>
   	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css">
   	
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
<div style="position:relative;height:0;padding-bottom:75.0%"><iframe src="https://www.youtube.com/embed/5uUugBONK4k?rel=0&amp;showinfo=0&amp;ecver=2" width="480" height="360" frameborder="0" allow="autoplay; encrypted-media" style="position:absolute;width:100%;height:100%;left:0" allowfullscreen></iframe></div>
   </div>
   <div style="float: left;">
	   <input id="doNotShowAgain" style="margin: 12px 0px 0px 15px;" type="checkbox"><fmt:message key="label.tutorial.video.never.show.again" /></input>
   </div>
   <button class="ui-button ui-corner-all pull-right" onclick="javascript:closeTutorial();">
   	<fmt:message key="button.close" />
   	</button>
</body>
<html>
