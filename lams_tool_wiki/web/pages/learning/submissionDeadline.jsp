<!DOCTYPE html>
            

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
		<c:set var="lams">
		<lams:LAMSURL />
	</c:set>
	<c:set var="tool">
		<lams:WebAppURL />
	</c:set>
	
	<lams:head>  
		<title>
			<fmt:message key="activity.title" />
		</title>
		<lams:css/>
	
		<lams:JSImport src="includes/javascript/common.js" />
		<lams:JSImport src="includes/javascript/wikiCommon.js" relative="true" />
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	</lams:head>

	<body class="stripes">
			<c:set var="title" scope="request">
					<fmt:message key="activity.title" />
			</c:set>		
			
			<lams:Page type="learner" title="${title}">
				<lams:Alert type="danger" close="false" id="submissionDeadline">
					<fmt:message key="authoring.info.teacher.set.restriction" >
						<fmt:param><lams:Date value="${submissionDeadline}" /></fmt:param>
					</fmt:message>							
				</lams:Alert>
				 	
				<c:if test="${mode == 'learner' || mode == 'author'}">
					<%@ include file="parts/finishButton.jsp"%>
				</c:if>
			</lams:Page>

		<div class="footer">
		</div>					
	</body>
</lams:html>