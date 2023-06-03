<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<fmt:message key='activity.title' var="title"/>
<lams:PageLearner title="${title}" toolSessionID="${sessionMap.toolSessionID}" >

	<div class="container-lg">
		<div class="row justify-content-md-center">
			  <div class="col-md-8">
				<lams:DefineLater />
			  </div>
			</div>
			
		</div>
		
	</div>

</lams:PageLearner>