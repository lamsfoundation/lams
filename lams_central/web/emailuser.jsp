<!DOCTYPE HTML>

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<lams:html>
<lams:head>
	<title><fmt:message key="title.admin.window" /></title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>favicon.ico" type="image/x-icon" />

	<lams:css />
	<style type="text/css">
		#buttonsDiv {
			text-align: right;
		}

		input, textarea {
			width: 100%;
		}		
		</style>
	</lams:head>
	
	
	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery.form.js"></script>
	<script type="text/javascript">
		var returnUrl = '${param.returnUrl}';
	
		function sendEmail(){
			$('#emailUserForm').ajaxSubmit({
				'success' : function(result){
					if (result) {
						$('#errorArea').text(result);
					} else {
						closeDialog();
					}
			}});	
		}
		
		function closeDialog(){
			if (returnUrl == '') {
				// if this page is in dialog, close it
				if (parent.closeEmailDialog) {
					parent.closeEmailDialog();
				} else {
					// if this is a pop up, close it
					window.close();
				}
			} else {
				// if it is a main page, navigate away 
				document.location.href = returnUrl; 
			}
		}
	</script>

<body class="stripes">
	<div id="page">
		<div id="content">
			<h1>
				<fmt:message key="email.compose.mail" />
			</h1>
			
			<c:if test="${errorsPresent}">
				<div id="errorArea" class="warning">
					<html:messages id="error">
						<c:out value="${error}" escapeXml="false" />
						<br />
					</html:messages>
				</div>
			</c:if>

			<form id="emailUserForm" action="<lams:LAMSURL/>emailUser.do?method=send" method="post">
				 <c:if test="${not empty user}">
					<input type="hidden" name="userId" value="${user.userId}" />
				</c:if>
				<input type="hidden" name="lessonID" value="${param.lessonID}" />
				
				<table>
					<tr>
						<td><fmt:message key="email.to" /></td>
						 <c:if test="${not empty user}">
							<td><c:out value="${user.firstName} ${user.lastName}"/> &lt;<c:out value="${user.email}"/>&gt;</td>
						 </c:if>
					</tr>

					<tr>
						<td class="small-space-top" ><fmt:message key="email.subject" /></td>
						<td class="small-space-top"><input type="text" name="subject"
							<c:if test="${sendDisabled}">disabled="disabled"</c:if> >
						</td>
					</tr>

					<tr>
						<td colspan="2" class="small-space-top"><textarea name="body" rows="16"
							<c:if test="${sendDisabled}">disabled="disabled"</c:if>></textarea>
						</td>
					</tr>
				</table>

				<div id="buttonsDiv" class="space-top">
					<c:if test="${not sendDisabled}">
						<a href="#" onclick="javascript:sendEmail()" class="button">
							<fmt:message key="email.send" />
						</a> 
					</c:if>
					
					<a href="#" onclick="javascript:closeDialog()" class="button space-left">
						<fmt:message key="button.cancel" />
					</a>
				</div>
			</form>
		</div>
	</div>
</body>
</lams:html>
