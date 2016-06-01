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
			if (document.getElementById("UPLOAD_FILE").value.length == 0) {
				var msg = "<fmt:message key="button.select.importfile"/>";
				alert(msg);
				return (false);
			} else {
    	    	var options = { 
	    	    	target:  parent.jQuery('#questionListArea'), 
	    		   	success: afterRatingSubmit  // post-submit callback
	    		}; 				
		    		    				
	    		$('#importForm').ajaxSubmit(options);
			}
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
				<div class="form-group">
				    <label for="UPLOAD_FILE">
				    	<fmt:message key="label.import.file" />
				    </label>
					<input type="file" name="UPLOAD_FILE" id="UPLOAD_FILE"/>
				</div>
						
				<a href="#nogo" class="btn btn-primary pull-right" onclick="javascript:verifyAndSubmit();">
					<fmt:message key="button.import" />
				</a>
			</form>

		</div>
		<!--closes content-->

		<div id="footer">
		</div>
		<!--closes footer-->
	</body>
</lams:html>
