<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<%-- runAuto and reflectOn does not set when  authoring preview mode then set false as default value --%>
<c:choose>
	<c:when test="${empty sessionMap.runAuto}">
		<c:set var="runAuto" value="false"/>
	</c:when>
	<c:otherwise>
		<c:set var="runAuto" value="${sessionMap.runAuto}"/>
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${empty sessionMap.reflectOn}">
		<c:set var="reflectOn" value="false"/>
	</c:when>
	<c:otherwise>
		<c:set var="reflectOn" value="${sessionMap.reflectOn}"/>
	</c:otherwise>
</c:choose>

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
					   if(${runAuto}){
					  		//set complete flag and finish this activity as well.
					        window.parent.location.href='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${param.toolSessionID}&itemUid=${param.itemUid}"/>';
					   }else{
						    window.parent.opener.location.href="<c:url value="/learning/completeItem.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&itemUid=${param.itemUid}&reqID="+reqIDVar.getTime();
					   }
					}
				   if(window.parent.opener != null) {
						window.parent.opener=null;
						window.parent.close();
					}
				}
				function continueReflect(){
					 window.parent.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
				}
				function nextIns(currIns){
					document.location.href="<c:url value='/nextInstruction.do'/>?mode=${mode}&insIdx=" + currIns + "&sessionMapID=${sessionMapID}&itemUid=${param.itemUid}&itemIndex=${param.itemIndex}";
				}
		//-->
		</script>
	</head>
	<body class="stripes">
		
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
							<c:when test="${reflectOn && runAuto}">
								<a href="#" id="FinishInstruction" onClick="javascript:continueReflect()" class="button">
									<fmt:message key='label.continue'/>
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
