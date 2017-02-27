<%@ page language="java"  pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ include file="/common/taglibs.jsp"%>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<title><fmt:message key="authoring.title.import" /></title>
	<%@ include file="/common/header.jsp"%>

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
	<script type="text/javascript">
		function closeWin() {
			window.close();
		}
			
		function verifyAndSubmit() {
			debugger;
			var filename = document.getElementById("UPLOAD_FILE").value;
			if (filename.length == 0) {
				var msg = "<fmt:message key="button.select.importfile"/>";
				alert(msg);
				return (false);
			} else {
				if ( filename ) {
					var msg = '<fmt:message key="error.import.file.format"/>';
					var extname = filename.substr((~-filename.lastIndexOf(".") >>> 0) + 2);
					if ( extname.length == 0) {
						alert(msg);
						return (false);
					} else {
						extname = extname.toUpperCase();
						if ( "XML" != extname) {
							alert(msg);
							return (false);
						}
					}
				}
				
			}
			var options = { 
	    	   	target:  parent.jQuery('#questionListArea'), 
	    	   	success: afterRatingSubmit  // post-submit callback
	    	}; 							
    		$('#importForm').ajaxSubmit(options);
		}
		// post-submit callback 
   		function afterRatingSubmit(responseText, statusText) { 
   			self.parent.refreshThickbox();
   			self.parent.tb_remove();
   		}  
			
	</script>
</lams:head>

<body class="stripes">

	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="panel-title">
				<fmt:message key="authoring.title.import" />
			</div>
		</div>
			
		<div class="panel-body">
				
			<p>
				<fmt:message key="msg.import.file.format"/>
			</p>
				
			<form action="<c:url value="/authoring/importQuestions.do"/>?sessionMapID=${sessionMapID}" method="post" enctype="multipart/form-data" id="importForm">	
				<input type="file" name="UPLOAD_FILE" id="UPLOAD_FILE"/>
						
				<div class="voffset10" id="buttonsDiv">
					<input class="btn btn-sm btn-default" value='<fmt:message key="label.authoring.cancel.button"/>' type="button"
						onClick="javascript:self.parent.tb_remove();" />
					<a href="#nogo" class="btn btn-sm btn-primary" onclick="javascript:verifyAndSubmit();">
						<i class="fa fa-sm fa-upload"></i>&nbsp;<fmt:message key="button.import" />
					</a>
				</div>
			</form>

		</div>
		<!--closes content-->

		<div id="footer">
		</div>
		<!--closes footer-->
	</body>
</lams:html>
