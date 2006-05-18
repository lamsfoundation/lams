<%@ include file="/common/taglibs.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>${instructions.title}</title>
	
		<%@ include file="/common/header.jsp"%>
		<script language="JavaScript" type="text/JavaScript">
<!--
		function finishIns(){
			if(${param.mode == "learner"}){
			   var reqIDVar = new Date();
			   //if auto run mode, the opener will be null
			   if(window.parent.opener != null) 
				    window.parent.opener.location.href="<c:url value="/learning/completeItem.do"/>?itemUid=${param.itemUid}&reqID="+reqIDVar.getTime();
			   else{
			  		//set complete flag and finish this activity as well.
			        window.parent.location.href='<c:url value="/learning/finish.do?toolSessionID=${param.toolSessionID}&itemUid=${param.itemUid}"/>';
			   }
			}
		   if(window.parent.opener != null) {
				window.parent.opener=null;
				window.parent.close();
			}
		}
		function nextIns(currIns){
			document.location.href="<c:url value='/nextInstruction.do'/>?insIdx=" + currIns + "&itemUid=${param.itemUid}&itemIndex=${param.itemIndex}&mode=${param.mode}";
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
					<font color="#FFFFFF" size="1" face="Verdana, Arial, Helvetica, sans-serif">${instructions.title} </font>
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
								<h2>Step ${instructions.current} of ${instructions.total}</h2>
								<P>
								<span style="align:center">
								<c:choose>
									<c:when test="${instructions.instruction == null}">
										<fmt:message key="msg.no.instruction"/>
									</c:when>
									<c:otherwise>
										${instructions.instruction.description}
									</c:otherwise>
								</c:choose>
								</span>
							</td>
							<td width="15%" valign="middle">
								<c:choose>
									<c:when test="${instructions.current < instructions.total}">
										<input name="NextInstruction" type="button" class="buttonStyle" id="NextInstruction" onClick="javascript:nextIns(${instructions.current})" value="<fmt:message key='label.next.instruction'/>" />
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
