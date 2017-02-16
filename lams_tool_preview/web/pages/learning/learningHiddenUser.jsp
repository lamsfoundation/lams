<!DOCTYPE html>
<%@include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>
	<meta http-equiv="refresh" content="60">
	
	<script type="text/javascript">

 		function finishSession(){
 			document.getElementById("finishButton").disabled = true;
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}"/>';
			return false;
		}
 		
     </script>
</lams:head>

<body class="stripes">

	<c:set scope="request" var="title">
		<fmt:message key="activity.title" />
	</c:set>
	<lams:Page type="learner" title="${title}">
	
		<div class="row no-gutter">
			<div
				class="col-xs-12 col-sm-offset-1 col-sm-10 col-lg-offset-2 col-lg-8">
				<div class="alert alert-info">
					<fmt:message key="label.removed.user.warning" />
				</div>
			</div>
		</div>
		
		<div class="row no-gutter">
			<div class="col-xs-12">
				
			</div>
		</div>
		
		<div>
		<a href="javascript:location.reload(true);"	class="btn btn-default voffset5">
					<fmt:message key="button.try.again" />
				</a>
		
			<html:link href="#nogo" property="FinishButton" styleId="finishButton" onclick="return finishSession()" styleClass="btn btn-primary voffset5 pull-right na">
				<fmt:message key="label.finished" />
			</html:link>
		</div>
		
	</lams:Page>
	
	<div id="footer"></div>

</body>
</lams:html>
