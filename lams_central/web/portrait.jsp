<!DOCTYPE html>

<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-bean" prefix="bean"%>
<%@ taglib uri="tags-logic" prefix="logic"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<lams:html>
<lams:head>
	<link rel="stylesheet" href="css/defaultHTML_learner.css" type="text/css" />
	<style type="text/css">
		.ui-state-active a, .ui-state-active a:link, .ui-state-active a:visited, .ui-state-active a:hover {
			color: #47bc23 !important;
			background-image: none;
			background-color: transparent;
		}
		
		.button {
			font-size: 11px;
			font-family: verdana, arial, helvetica, sans-serif;
		}
		
		.tableCaptions {
			text-align: right;
			vertical-align: top;
			width: 30%;
		}
		
		#accordion h3 a {
			border-bottom: 0;
		}
		
		#accordion div {
			text-align: center;
		}
		
		#accordion p {
			text-align: center;
			padding-bottom: 0px;
			margin-bottom: 0xp;
		}
		
		#webcam {
			padding-bottom: 17px;
		}
	</style>

	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="includes/javascript/profile.js"></script>
	<script type="text/javascript" src="/lams/includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery.blockUI.js"></script>
	<script type="text/javascript" src="includes/javascript/webcam.js"></script>
	<script type="text/javascript">
	
		webcam.set_swf_url("includes/flash/webcam.swf");
		webcam.set_shutter_sound(true, "includes/sounds/shutter.mp3"); // play shutter click sound
		webcam.set_api_url('<c:url value="/saveportrait.do"/>?method=saveWebcamPortrait');
		webcam.set_quality(100); // JPEG quality (1 - 100)
		
		//set onComplete callback
		webcam.set_hook('onComplete', 'onCompleteWebcam');
		function onCompleteWebcam(response) {
			window.parent.location.reload();
		}
		
		//set onError callback
		webcam.set_hook('onError', 'onErrorWebcam');
		function onErrorWebcam(msg) {
			var activeHeader = $("#accordion").accordion("option", "active");
			if (activeHeader == "0") {
				alert($("#webcam").attr("height") + "JPEGCam Flash Error: " + msg);
			}
			return true;
		}
	
		jQuery(document).ready(function() {
			//initialize webcam
			$("#webcam").html(webcam.get_html(320, 240, 120, 90));

			$('#takeSnapshot').click(function() {
				webcam.freeze();
				showConfirmationButtons();
			});

			$('#uploadWebcam').click(function() {
				$.blockUI({
					message : '<h1><img src="images/loading.gif" style="padding-right:25px;"> <fmt:message key="label.portrait.please.wait" /> </h1>'
				});
				webcam.upload();
				hideConfirmationButtons();
			});

			$('#resetWebcam').click(function() {
				webcam.reset();
				hideConfirmationButtons();
			});

			function hideConfirmationButtons() {
				$('#takeSnapshotArea').show();
				$('#uploadToServerArea').hide();
			}

			function showConfirmationButtons() {
				$('#takeSnapshotArea').hide();
				$('#uploadToServerArea').show();
			}

			//update dialog's height and title
			updateMyProfileDialogSettings('<fmt:message key="title.portrait.change.screen" />', '680');
		});
	</script>
	<script type="text/javascript">
		function submitPortrait() {
			var formData = new FormData(document.getElementById("PortraitActionForm"));
			$.ajax({ // create an AJAX call...
				data : formData,
				async : false,
				processData : false, // tell jQuery not to process the data
				contentType : false, // tell jQuery not to set contentType
				type : $("#PortraitActionForm").attr('method'),
				url : $("#PortraitActionForm").attr('action'),
				success : function(data) {
					window.parent.location.reload();
				}
			});
		}
	</script>
</lams:head>

<body>
<html:form action="/saveportrait.do" method="post" styleId="PortraitActionForm"	enctype="multipart/form-data">
	<html:hidden name="PortraitActionForm" property="portraitUuid" />
	<div style="clear: both"></div>
	<div class="container">
		<div class="row vertical-center-row">
			<div class="col-xs-12 col-sm-8 col-sm-offset-2 col-md-6 col-md-offset-3">

				<div class="currentPortrait text-center" style="margin:10px">
					<fmt:message key="label.portrait.current" />:<br/>
	
					<logic:notEqual name="PortraitActionForm" property="portraitUuid" value="0">
						<img class="img-thumbnail" src="/lams/download/?uuid=<bean:write name="PortraitActionForm" 
								property="portraitUuid" />&preferDownload=false" />
					</logic:notEqual>
	
					<logic:equal name="PortraitActionForm" property="portraitUuid"	value="0">
						<c:set var="lams">
							<lams:LAMSURL />
						</c:set>
						<em><fmt:message key="msg.portrait.none" /></em>
					</logic:equal>
				</div>
			
			<!--  -->
			<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
			  <div class="panel panel-default hidden-xs">
			    <div class="panel-heading" role="tab" id="headingOne">
			      <div class="panel-title">
			        <a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
			          <i class="fa fa-fw fa-camera text-primary"></i> <fmt:message key="label.portrait.take.snapshot.from.webcamera" />
			        </a>
			      </div>
			    </div>
			    <div id="collapseOne" class="panel-collapse collapse in hidden-xs" role="tabpanel" aria-labelledby="headingOne">
			      <div class="panel-body">
			      <!-- Webcam -->
			
							<div id="webcam"></div>
							<!-- Some buttons for controlling things -->
							<div id="takeSnapshotArea">
								<a href="javascript:webcam.configure();" class="btn btn-sm btn-default"><fmt:message
										key='label.portrait.configure' /></a> <a id="takeSnapshot"
									class="btn btn-sm btn-default" href="javascript:return false;"><fmt:message
										key='label.portrait.take.snapshot' /></a>
							</div>
							<div id="uploadToServerArea" style="display: none;">
								<div style="padding-bottom: 13px;">
									<fmt:message key='label.portrait.do.you.like.results' />
								</div>
			
								<a class="btn btn-xs btn-default" id="uploadWebcam" 
									><fmt:message
										key='label.portrait.yes.set.it.as.portrait' /></a> <a
									class="btn btn-xs btn-default" id="resetWebcam"
									href="javascript:return false;"><fmt:message
										key='label.portrait.no.take.another.one' /></a>
							</div>

			      </div>
			    </div>
			  </div>
			  <div class="panel panel-default">
			    <div class="panel-heading" role="tab" id="headingTwo">
			      <div class="panel-title">
			        <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
			         <i class="fa fa-fw fa-upload text-primary"></i> <fmt:message key="label.portrait.upload" />
			        </a>
			      </div>
			    </div>
			    <div id="collapseTwo" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingTwo">
			      <div class="panel-body">
			      <!-- Upload -->
							<div class="form-group">
								<html:file property="file" />
								<p class="help-block">
									<fmt:message key="msg.portrait.resized" />
								</p>								
							</div>	
							<br />
							 <a class="btn btn-sm btn-file btn-default offset5" role="button"
								href="<c:url value='/index.do'/>?method=profile"><fmt:message
									key="button.cancel" /></a>
							<a class="btn btn-sm btn-default offset5" onclick="submitPortrait()"
								role="button"><fmt:message key="button.save" /></a>
			
			      </div>
			    </div>
			  </div>
		</div>
		<div align="right">
		<a class="btn btn-sm btn-file btn-default offset5" role="button"
								href="<c:url value='/index.do'/>?method=profile"><fmt:message
									key="button.cancel" /></a></div>
	</div>
</div>
</div>	
</html:form>
</body>
</lams:html>