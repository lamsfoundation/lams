<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>
<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	<title>${formBean.title}</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<!-- depending on user / site preference this will get changed probbably use passed in variable from flash to select which one to use-->
    <link href="<html:rewrite page='/includes/css/aqua.css'/>" rel="stylesheet" type="text/css">
	<!-- this is the custom CSS for hte tool -->
	<link href="<html:rewrite page='/includes/css/tool_custom.css'/>" rel="stylesheet" type="text/css">
	<link href="<html:rewrite page='/includes/css/rsrc.css'/>" rel="stylesheet" type="text/css">
		<script language="JavaScript" type="text/JavaScript">
<!--
		
		function finish()
		{
			window.parent.opener.location.href='urlcontent.do?method=setContentDone&sessionId=27534&urlId=7365&mode=resume&actor=learner';
			window.parent.opener=null;
			window.parent.close();
		}

		function finishIns(){
			window.parent.opener=null;
			window.parent.close();
		}
		function nextIns(currIns){
			document.location.href="<c:url value='/nextInstruction.do?currInstructonId='/>" + currIns;
		}
//-->
</script>
		<!--progress:UpdateLearnerProgress/-->
	</head>


	<body bgcolor="#9DC5EC" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
		<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" class="body">
			<tr>
				<td width="10" align="left">
					&nbsp;
				</td>
				<td width="628" height="15" align="left" bgcolor="#282871">
					<font color="#FFFFFF" size="1" face="Verdana, Arial, Helvetica, sans-serif">${resourceItem.title} </font>
				</td>
				<td width="335" height="15" align="right" bgcolor="#282871">
					<font color="#FFFFFF" size="1" face="Verdana, Arial, Helvetica, sans-serif">Instructions&nbsp;</font>
				</td>
			</tr>
			<tr valign="top">
				<td height="5">
					<img width="16" height="5" src="../../images/spacer.gif" alt="space.gif" />
				</td>
				<td height="5" colspan="2" bgcolor="#FFFFFF">
					<img width="100" height="5" src="../../images/spacer.gif" alt="space.gif" />
				</td>
			</tr>
			<tr valign="top">
				<td valign="middle">
					&nbsp;
				</td>
				<td colspan="2" valign="middle" bgcolor="#FFFFFF">
					<table width="100%" height="100%" border="0" cellpadding="3" class="body" summary="This table is being used for layout purposes only">
						<tr>
							<td width="85%" valign="top">



								<span class="subHeader">Step ${formBean.current}  of ${formBean.total)}</span>
								<br>
								${formBean.instructions[${formBean.current}].description}

							</td>
							<td width="15%" valign="middle">

								<c:choose>
									<c:when test="${formBean.current < formBean.total}"?
										<input name="NextInstruction" type="button" class="buttonStyle" id="NextInstruction" 
											onClick="javascript:nextIns(${formBean.current})"
											value="<fmt:message key='lable.next.instruction'/>" />
									</c:when>
									<c:otherwise>
										<input name="FinishInstruction" type="button" class="buttonStyle" id="FinishInstruction" 
											onClick="javascript:finishIns()"
											value="<fmt:message key='lable.finish'/>" />
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr valign="top">
				<td height="5">
					<img width="10" height="5" src="${tool}includes/images/spacer.gif" alt="space.gif" />
				</td>
				<td height="5" colspan="2" bgcolor="#FFFFFF">
					<img width="100" height="5" src="${tool}includes/images/spacer.gif" alt="space.gif" />
				</td>
			</tr>
		</table>
	</body>
</html>
