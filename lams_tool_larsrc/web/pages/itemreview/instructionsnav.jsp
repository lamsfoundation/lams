<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">
<%@ include file="/common/taglibs.jsp"%>
<html>
	<head>
		<title>${instructions.title}</title>

		<%@ include file="/common/header.jsp"%>
		
		<%-- param has higher level for request attribute --%>
		<c:if test="${not empty param.mode}">
			<c:set var="mode" value="${param.mode}"/>
		</c:if>		
		<script language="JavaScript" type="text/JavaScript">
		<!--
				function finishIns(){
				//learner and author(preview mode) will mark the finish
					if(${mode == "learner"} || ${mode == "author"}){
					   var reqIDVar = new Date();
					   //if auto run mode, the opener will be null
					   if(window.parent.opener != null) 
						    window.parent.opener.location.href="<c:url value="/learning/completeItem.do"/>?mode=${mode}&itemUid=${param.itemUid}&reqID="+reqIDVar.getTime();
					   else{
					  		//set complete flag and finish this activity as well.
					        window.parent.location.href='<c:url value="/learning/finish.do?mode=${mode}&toolSessionID=${param.toolSessionID}&itemUid=${param.itemUid}"/>';
					   }
					}
				   if(window.parent.opener != null) {
						window.parent.opener=null;
						window.parent.close();
					}
				}
				function nextIns(currIns){
					document.location.href="<c:url value='/nextInstruction.do'/>?mode=${mode}&insIdx=" + currIns + "&itemUid=${param.itemUid}&itemIndex=${param.itemIndex}";
				}
		//-->
		</script>
	</head>
	<body>
		
		<div id="content" style="padding-bottom: 0px;float:none;margin: auto;">
			<table cellpadding="0">
				<tr valign="top">
					<td width="75%" valign="top">
						<h2>
							Step ${instructions.current} of ${instructions.total}
						</h2>
						<P>
							<span style="align:center"> <c:choose>
									<c:when test="${instructions.instruction == null}">
										<fmt:message key="msg.no.instruction" />
									</c:when>
									<c:otherwise>
							${instructions.instruction.description}
						</c:otherwise>
								</c:choose> </span>
					</td>
					<td width="25%" valign="middle">
						<c:choose>
							<c:when test="${instructions.current < instructions.total}">
								<a href="#" id="NextInstruction" onClick="javascript:nextIns(${instructions.current})" class="button">
									<fmt:message key='label.next.instruction'/>
								</a>
							</c:when>
							<c:otherwise>
								<a href="#" id="FinishInstruction" onClick="javascript:finishIns()" class="button">
									<fmt:message key='label.finish'/>
								</a>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</table>
		</div>
		<div id="footer" style="float:none;margin: auto;"></div>
	</body>
</html>
