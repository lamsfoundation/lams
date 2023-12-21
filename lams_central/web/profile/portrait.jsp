<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<c:set var="ENABLE_PORTRAIT_EDITING"><%=Configuration.get(ConfigurationKeys.ENABLE_PORTRAIT_EDITING)%></c:set>
<c:set var="lams"><lams:LAMSURL /></c:set>

<lams:html>
<lams:head>
	<lams:css/>
	<link rel="stylesheet" href="css/croppie.css" />
	<style type="text/css">
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
		
		#canvas, #still-portrait, #upload-croppie, #save-upload-button {
			display:none;
		}
	</style>

	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
	<lams:JSImport src="includes/javascript/profile.js" />
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/webrtc-capturestill.js"></script>
	<script type="text/javascript">
		//constant for croppie.js
		var PORTRAIT_SIZE = 400;
	</script>
	<script type="text/javascript" src="${lams}includes/javascript/croppie.js"></script>
	<script type="text/javascript">
		//variable defined in croppie.js
		var croppieWidget;
		
		jQuery(document).ready(function() {
			//handler for upload-webcam button
			$('#upload-webcam').click(function() {
				$.blockUI({
					message : '<h1><img src="images/loading.gif" style="padding-right:25px;"> <spring:escapeBody javaScriptEscape="true"><fmt:message key="label.portrait.please.wait" /></spring:escapeBody> </h1>'
				});
				
				uploadProtraitToServerSide(croppieWidget);
			});

			//update dialog's height and title
			updateMyProfileDialogSettings('<spring:escapeBody javaScriptEscape="true"><fmt:message key="title.portrait.change.screen" /></spring:escapeBody>', '740');

			//init croppie widget on Upload tab
			var $uploadCroppie = $('#upload-croppie').croppie({
	  	  	    viewport: {
	  	  	        width: PORTRAIT_SIZE,
	  	  	        height: PORTRAIT_SIZE
	  	  	    },
	  	  		enforceBoundary: false,
	  	  	    boundary: { width: 500, height: 500 }
			});

			$('#upload-input').on('change', function () { 
				//readFile
	 			if (this.files && this.files[0]) {
		            var reader = new FileReader();
		            
		            reader.onload = function (e) {
						$('#save-upload-button, #upload-croppie').show();
		            		$uploadCroppie.croppie('bind', {
		            			url: e.target.result
		            		});
		            }
		            
		            reader.readAsDataURL(this.files[0]);
		        } else {
			        alert("Sorry - you're browser doesn't support the FileReader API");
			    }
			});
			$('#save-upload-button').on('click', function (ev) {
				uploadProtraitToServerSide($uploadCroppie);
			});
		});

		//Creates a Blob object representing the image contained in the canvas. Which we then upload to the server.
		function uploadProtraitToServerSide(uploadCroppie) {
			uploadCroppie.croppie('result', {
		        type: 'blob',
		        size: {
		            width:  PORTRAIT_SIZE,
		            height: PORTRAIT_SIZE,
		        },
		        format: 'jpeg',
		        quality: 0.95,
		        backgroundColor: '#FFF',
    		}).then(function(blob) {
				var formData = new FormData();
				formData.append("file", blob);
				
				//upload protrait to server side
				$.ajax({
					data : formData,
					async : false,
					processData : false, // tell jQuery not to process the data
					contentType : false, // tell jQuery not to set contentType
					type : "post",
					url : $("#PortraitActionForm").attr('action'),
					success : function(data) {
						window.parent.location.reload();
					}
				});
			});
		}
	</script>
</lams:head>

<body>
<c:set var="csrfToken"><csrf:token/></c:set>
<form:form action="saveportrait.do?${csrfToken}" method="post" modelAttribute="PortraitActionForm" id="PortraitActionForm" >
	<form:hidden path="portraitUuid" />
	<div style="clear: both"></div>
	
	<div class="container">
	<div class="row vertical-center-row">
	<div class="col-xs-12 col-sm-10 col-sm-offset-1 col-md-8 col-md-offset-2">

		<div class="currentPortrait text-center" style="margin:10px">
			<fmt:message key="label.portrait.current" />:<br/>
	
			<c:choose>
				<c:when test="${PortraitActionForm.portraitUuid == 0}">
					<em><fmt:message key="msg.portrait.none" /></em>
				</c:when>
				<c:otherwise>
				<img class="img-thumbnail" src="/lams/download?uuid=${PortraitActionForm.portraitUuid}&version=2&preferDownload=false" />
				</c:otherwise>
			</c:choose>
		</div>

		<c:if test="${ENABLE_PORTRAIT_EDITING}">
			<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
			
				<!-- Webcamera -->
				<div class="panel panel-default">
				    <div class="panel-heading" role="tab" id="headingOne">
				    	<div class="panel-title">
				        	<a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" 
				        			aria-expanded="true" aria-controls="collapseOne">
				          	<i class="fa fa-fw fa-camera text-primary"></i> 
				          	<fmt:message key="label.portrait.take.snapshot.from.webcamera" />
				        	</a>
				    	</div>
				    </div>
				    
				    <div id="collapseOne" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingOne">
				    	<div class="panel-body">
				      			
							<div>
							    <video id="video">
							    		<fmt:message key="label.video.stream.not.available" />
							    </video>
							</div>
							    
							<a id="startbutton" class="btn btn-sm btn-file btn-default voffset5" role="button" href="#nogo">
								<fmt:message key='label.portrait.take.snapshot' />
							</a>
							
							<canvas id="canvas"></canvas>
							<div id="still-portrait">
								<div class="output voffset10">
									<div id="photo" ></div> 
								</div>
								
								<a class="btn btn-sm btn-default voffset10" id="upload-webcam">
									<fmt:message key='label.portrait.yes.set.it.as.portrait' />
								</a>
							</div>
				    	</div>
				    </div>
				</div>
				
				<!-- Upload -->
				<div class="panel panel-default">
					<div class="panel-heading" role="tab" id="headingTwo">
						<div class="panel-title">
				        		<a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo" 
				        				aria-expanded="false" aria-controls="collapseTwo">
				        			<i class="fa fa-fw fa-upload text-primary"></i> 
				        			<fmt:message key="label.portrait.upload" />
				        		</a>
						</div>
					</div>
				    
				    <div id="collapseTwo" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingTwo">
				    		<div class="panel-body">
							<div class="form-group">
								<label class="btn btn-default">
									<input type="file" name="file" value="" id="upload-input" accept="image/*">
								</label>
								
								<p class="help-block">
									<fmt:message key="msg.portrait.resized" />
								</p>	
								<div id="upload-croppie"></div>
								
								<a class="btn btn-sm btn-default offset5" id="save-upload-button" role="button">
									<fmt:message key='label.portrait.yes.set.it.as.portrait' />
								</a>
							</div>
				    		</div>
				    </div>
				</div>
			</div>
		</c:if>

		<c:if test="${!param.isReturnButtonHidden}">
			<div align="right">
				<button type="button" class="btn btn-sm btn-file btn-primary offset5" id="portraitReturnButton" onclick="history.go(-1);">
					<fmt:message key="label.return.to.myprofile" />
				</button>
			</div>
		</c:if>
	</div>
	</div>
	</div>	
</form:form>
</body>
</lams:html>
