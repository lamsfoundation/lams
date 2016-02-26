<%@ page language="java"  pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>

<!DOCTYPE html>
<lams:html>
	<lams:head>
		<title><fmt:message key="title.import" /></title>
		
		<lams:css />
		
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
					document.getElementById('importForm').submit();
				}
			}
			
		</script>
	</lams:head>

<c:set var="title" scope="request">
	<fmt:message key="title.import" />
</c:set>

	<body class="stripes">
			<lams:Page type="admin" title="${title}">

				<div class="panel">
					<fmt:message key="title.import.instruction" />
				</div>

				<form action="<c:url value="/authoring/importToolContent.do"/>" method="post" enctype="multipart/form-data" id="importForm">
					<p>
						<label for="UPLOAD_FILE"><fmt:message key="label.ld.zip.file" /></label>
						<input type="hidden" name="customCSV" id="customCSV" value="${customCSV}" />
						<input type="file" name="UPLOAD_FILE" id="UPLOAD_FILE"/>
						<a href="javascript:;" class="btn btn-primary pull-right voffset10" onclick="verifyAndSubmit();"><fmt:message key="button.import" /> 
						&nbsp;<i class="fa fa-sm fa-upload"></i></a>
					</p>
				</form>

			</div>
			<!--closes content-->


			<div id="footer">
			</div>
			<!--closes footer-->
</lams:Page>			
	</body>
</lams:html>