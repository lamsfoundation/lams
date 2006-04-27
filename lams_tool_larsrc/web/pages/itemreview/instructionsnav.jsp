<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%= session.getAttribute("instructionNavForm") %>" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>${formBean.title}</title>
	
		<%@ include file="/common/header.jsp"%>
		<script language="JavaScript" type="text/JavaScript">
<!--
		
		function finish()
		{
			if(${mode.learner}){
			   var reqIDVar = new Date();
			   //if auto run mode, the opener will be null
			   if(window.parent.opener != null) 
				   window.parent.opener.parent.frames['learningFrame'].location.href="<c:url value="/learning/completeItem.do"/>?itemUid=${itemUid}&reqID="+reqIDVar.getTime();
			   else{
			   //set complete flag and finish this activity as well.
			       window.parent.opener.parent.frames['learningFrame'].location.href='<c:url value="/learning/finish.do?toolSessionID=${toolSessionID}&itemUid=${itemUid}"/>';
			   }
			}
		   if(window.parent.opener != null) {
				window.parent.opener=null;
				window.parent.close();
			}
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


	<body bgcolor="#9DC5EC" border="1" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
		<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" class="body">
			<tr>
				<td width="2%"  bgcolor="#282871"></td>
				<td width="81%" height="15" align="left" bgcolor="#282871">
					<font color="#FFFFFF" size="1" face="Verdana, Arial, Helvetica, sans-serif">${formBean.title} </font>
				</td>
				<td width="5%" height="15" align="right" bgcolor="#282871">
					<font color="#FFFFFF" size="1" face="Verdana, Arial, Helvetica, sans-serif">Instructions&nbsp;</font>
				</td>
				<td width="2%"  bgcolor="#282871"></td>
			</tr>
			<tr valign="top">
				<td></td>
				<td colspan="2" valign="middle" bgcolor="#FFFFFF">
					<table width="100%" height="100%" border="0" cellpadding="3" class="body" summary="This table is being used for layout purposes only">
						<tr>
							<td width="85%" valign="top">
								<h2>Step ${formBean.current} of ${formBean.total}</h2>
								<P>
								<span style="align:center">
								<c:choose>
									<c:when test="${formBean.instruction == null}">
										<fmt:message key="msg.no.instruction"/>
									</c:when>
									<c:otherwise>
										${formBean.instruction.description}
									</c:otherwise>
								</c:choose>
								</span>
							</td>
							<td width="15%" valign="middle">
								<c:choose>
									<c:when test="${formBean.current < formBean.total}">
										<input name="NextInstruction" type="button" class="buttonStyle" id="NextInstruction" onClick="javascript:nextIns(${formBean.current})" value="<fmt:message key='label.next.instruction'/>" />
									</c:when>
									<c:otherwise>
										<input name="FinishInstruction" type="button" class="buttonStyle" style="width:150" id="FinishInstruction" onClick="javascript:finishIns()" value="<fmt:message key='label.finish'/>" />
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</table>
				</td>
				<td></td>
			</tr>
			<tr>
				<td colspan="3" bgcolor="#282871" height="5"></td>
			</tr>
		</table>
	</body>
</html>
