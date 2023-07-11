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
		<script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	
	</lams:head>
	<body class="stripes">
		<lams:Page type="learner" title="${scribeDTO.title}">
		
			<%@ include file="parts/reportBody.jsp"%>
		
			
			<c:if test="${MODE == 'learner' || MODE == 'author'}">
				<%@ include file="parts/finishButton.jsp"%>
			</c:if>
		</div>
		
		<div id="footer"></div>
		
		</lams:Page>
	</body>
</lams:html>