<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
	</lams:head>
	<body class="stripes">

		<c:set scope="request" var="title">
			<c:out value="${title}" escapeXml="true"/>
		</c:set>
		<lams:Page type="learner" title="${title}">
		
			<div class="panel panel-default">	
				<div class="panel-heading panel-title ">
					<div class="row">
						<a href="javascript:;" onclick="javascipt:launchPopup('<c:url value='${popupUrl}'/>','popupUrl');"
								class="btn btn-primary float-end me-2">
							<fmt:message key="open.in.new.window" />
						</a>
					</div>
				</div>
			</div>
			
		</lams:Page>
	</body>
</lams:html>
