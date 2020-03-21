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
				class="col-12 offset-md-1 col-md-10 offset-xl-2 col-xl-8">
				<div class="alert alert-info">
					<fmt:message key="label.removed.user.warning" />
				</div>
			</div>
		</div>
		
		<div class="row no-gutter">
			<div class="col-12">
				
			</div>
		</div>
		
		<div>
		<a href="javascript:location.reload(true);"	class="btn btn-default voffset5">
					<fmt:message key="button.try.again" />
				</a>
		
			<a href="#nogo" path="FinishButton" id="finishButton" onclick="return finishSession()" class="btn btn-primary voffset5 pull-right na">
				<fmt:message key="label.finished" />
			</a>
		</div>
		
	</lams:Page>
	
	<div id="footer"></div>

</body>
</lams:html>
