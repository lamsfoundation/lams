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

	<body class="stripes">
			<div id="content">
			<h1>
				<fmt:message key="title.import" />
			</h1>

				<h2>
					<fmt:message key="title.import.instruction" />
				</h2>
				<p>
					&nbsp;
				</p>
				<form action="<c:url value="/authoring/importToolContent.do"/>" method="post" enctype="multipart/form-data" id="importForm">
					<p>
						<fmt:message key="label.ld.zip.file" />
						<input type="hidden" name="customCSV" id="customCSV" value="${customCSV}" />
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