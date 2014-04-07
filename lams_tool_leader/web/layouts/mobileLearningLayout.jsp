<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" 
	"http://www.w3.org/TR/html4/strict.dtd">

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
		<link rel="stylesheet" href="${lams}css/jquery.mobile.css" />
		<link rel="stylesheet" href="${lams}css/defaultHTML_learner_mobile.css" />

		<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>
		<script src="${lams}includes/javascript/jquery.js"></script>
		<script src="${lams}includes/javascript/jquery.mobile.js"></script>		
		
	</lams:head>
	<body class="large-font">
		<div data-role="page" data-dom-cache="true">
			<tiles:insert attribute="bodyMobile" />
		</div>
		
		<div data-role="page" data-dom-cache="true" id="leader-selection-dialog">
		
			<div data-role="header" data-theme="b" data-nobackbtn="true">
				<h1>
					<c:out value="${content.title}"/>
				</h1>
			</div>
	
			<div data-role="content">
				<div style="font-weight:bold; margin: 10px 0 20px;">
					${content.instructions}
					<br>
					<fmt:message key="label.are.you.going.to.be.leader" />
				</div>
					
				<div>
					<fmt:message key="label.users.from.group" />
				</div>
					
				<div style="text-align: right;">
					<c:forEach var="user" items="${groupUsers}" varStatus="status">
						<div>
							<c:out value="${user.firstName} ${user.lastName}"/>
						</div>
					</c:forEach>
				</div>
				
				<div class="space-bottom-top">
					<div class="right-buttons">
						<button data-theme="b" id="dialog-button-yes">
							<fmt:message key="label.yes.become.leader" />
						</button>
						<button data-theme="c" id="dialog-button-no">
							<fmt:message key="label.no" />
						</button>
					</div>
				</div>
				
			</div>

		</div>		
	</body>
</lams:html>
