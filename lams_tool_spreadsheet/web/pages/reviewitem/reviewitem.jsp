<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<title>
			<fmt:message key="label.reviewitem.title" />
		</title>	
		<%@ include file="/common/header.jsp"%>
	</lams:head>
	<body class="stripes">
	
		<div id="content">	
			<h1><fmt:message key="label.reviewitem.spreadsheet.sent.by" /> ${userName}	</h1><br>
			
			<c:choose>
				<c:when test="${code == null}">
					<br>
					<div>
						<b> <fmt:message key="label.reviewitem.user.hasnot.sent.spreadsheet" /> </b>
					</div>
				</c:when>
				<c:otherwise>
				    <html:hidden property="spreadsheet.code" styleId="spreadsheet.code" value="${code}"/>	
					<iframe
						id="externalSpreadsheet" name="externalSpreadsheet" src="<html:rewrite page='/includes/javascript/simple_spreadsheet/spreadsheet_offline.html'/>?lang=<%=request.getLocale().getLanguage()%>"
						style="width:635px;" frameborder="no" height="385px"
						scrolling="no">
					</iframe>
					<br><br><br>
					<table cellpadding="0" style="width:655px;">
						<tr>
							<td align="right" >
								<a href="javascript:window.close();" class="button"><fmt:message key="button.close"/></a>
							</td>
						</tr>
					</table>
					
				</c:otherwise>
			</c:choose>	
		</div>
		<div id="footer"></div>

	</body>
</lams:html>
