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

	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="includes/javascript/profile.js"></script>
	<script type="text/javascript" src="includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery.blockUI.js"></script>
	<script type="text/javascript" src="includes/javascript/webrtc-capturestill.js"></script>
	<script type="text/javascript">
		//constant for croppie.js
		var PORTRAIT_SIZE = 400;
	</script>
	<script type="text/javascript" src="includes/javascript/croppie.js"></script>
	<script type="text/javascript">
		//variable defined in croppie.js
		var croppieWidget;
		
		jQuery(document).ready(function() {
			//handler for upload-webcam button
			$('#upload-webcam').click(function() {
				$.blockUI({
					message : '<h1><img src="images/loading.gif" style="padding-right:25px;"> <fmt:message key="label.portrait.please.wait" /> </h1>'
				});
				
				uploadProtraitToServerSide(croppieWidget);
			});

			//update dialog's height and title
			updateMyProfileDialogSettings('<fmt:message key="title.portrait.change.screen" />', '740');

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
					type : $("#PortraitActionForm").attr('method'),
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
<html:form action="/saveportrait.do" method="post" styleId="PortraitActionForm"	enctype="multipart/form-data">
	<html:hidden name="PortraitActionForm" property="portraitUuid" />
	<div style="clear: both"></div>
	
	<div class="container">
	<div class="row vertical-center-row">
	<div class="col-xs-12 col-sm-10 col-sm-offset-1 col-md-8 col-md-offset-2">

		<div class="currentPortrait text-center" style="margin:10px">
			<fmt:message key="label.portrait.current" />:<br/>
	
			<logic:notEqual name="PortraitActionForm" property="portraitUuid" value="0">
				<img class="img-thumbnail" src="/lams/download/?uuid=<bean:write name="PortraitActionForm" 
						property="portraitUuid" />&version=2&preferDownload=false" />
			</logic:notEqual>
	
			<logic:equal name="PortraitActionForm" property="portraitUuid"	value="0">
				<em><fmt:message key="msg.portrait.none" /></em>
			</logic:equal>
		</div>

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

		<c:if test="${!param.isReturnButtonHidden}">
			<div align="right">
				<a class="btn btn-sm btn-file btn-default offset5" role="button" href="<c:url value='/index.do'/>?method=profile">
					<fmt:message key="label.return.to.myprofile" />
				</a>
			</div>
		</c:if>
	</div>
	</div>
	</div>	
</html:form>
</body>
</lams:html>
