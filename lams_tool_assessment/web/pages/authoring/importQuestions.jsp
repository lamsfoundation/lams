<%@ page language="java"  pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ include file="/common/taglibs.jsp"%>

<!DOCTYPE html>
<lams:html>
	<lams:head>
		<title><fmt:message key="authoring.title.import" /></title>
		<!-- ********************  CSS ********************** -->
		<lams:css />
		
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
		<script type="text/javascript">
			function closeWin(){
				window.close();
			}
			
			function verifyAndSubmit() {
				if (document.getElementById("UPLOAD_FILE").value.length == 0)	{
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
    		function afterRatingSubmit(responseText, statusText)  { 
    			self.parent.refreshThickbox();
    			self.parent.tb_remove();
    		}  
			
		</script>
	</lams:head>

	<body class="stripes">
			<div id="content">
			<h1>
				<fmt:message key="authoring.title.import" />
			</h1>

				<h2>
					<fmt:message key="title.import.instruction" />
				</h2>
				<p>
					&nbsp;
				</p>
				<p>
					<fmt:message key="msg.import.file.format"/>
				</p>
				<form action="<c:url value="/authoring/importQuestions.do"/>?sessionMapID=${sessionMapID}" method="post" enctype="multipart/form-data" id="importForm">
					<p>
						<fmt:message key="label.import.file" />
						<input type="file" name="UPLOAD_FILE" id="UPLOAD_FILE"/>
						<a href="javascript:;" class="button" onclick="verifyAndSubmit();"><span class="import"><fmt:message key="button.import" /></span></a>
					</p>
				</form>

			</div>
			<!--closes content-->


			<div id="footer">
			</div>
			<!--closes footer-->


	</body>
</lams:html>
