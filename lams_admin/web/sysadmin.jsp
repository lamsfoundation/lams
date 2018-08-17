<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><tiles:getAsString name="title"/></c:set>
	<c:set var="title"><fmt:message key="${title}"/></c:set>
	<title>${title}</title>

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>/admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-smoothness-theme.css" type="text/css" media="screen">
	<script language="JavaScript" type="text/JavaScript" src="<lams:LAMSURL/>/includes/javascript/changeStyle.js"></script>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
</lams:head>
    
<body class="stripes">
	<c:set var="subtitle"><tiles:getAsString name="subtitle" ignore="true"/></c:set>	
	<c:if test="${not empty subtitle}">
		<c:set var="title">${title}: <fmt:message key="${subtitle}"/></c:set>
	</c:if>
	
	<c:set var="help"><tiles:getAsString name='help'  ignore="true"/></c:set>
	<c:choose>
		<c:when test="${not empty help}">
			<c:set var="help"><lams:help style="small" page="${help}" /></c:set>
			<lams:Page type="admin" title="${title}" titleHelpURL="${help}">
			<c:forEach items="${groupedLinks}" var="links">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="panel-title"><fmt:message key="${links[0]}"/></div>
					</div>
					<div class="list-group">
						<c:set var="linkBeans" value="${links[1]}"/>
			 			<c:forEach items="${linkBeans}" var="linkBean">
							<span class="list-group-item">
							<a href="<c:out value="${linkBean.link}"/>">
									<fmt:message><c:out value="${linkBean.name}"/></fmt:message>
							</a>
							</span>
						</c:forEach>
			 		</div>
				</div>		
			</c:forEach>
			</lams:Page>
		</c:when>
		<c:otherwise>
			<lams:Page type="admin" title="${title}" >
				<c:forEach items="${groupedLinks}" var="links">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="panel-title"><fmt:message key="${links[0]}"/></div>
					</div>
					<div class="list-group">
						<c:set var="linkBeans" value="${links[1]}"/>
			 			<c:forEach items="${linkBeans}" var="linkBean">
							<span class="list-group-item">
							<a href="<c:out value="${linkBean.link}"/>">
									<fmt:message><c:out value="${linkBean.name}"/></fmt:message>
							</a>
							</span>
						</c:forEach>
			 		</div>
				</div>		
			</c:forEach>
			</lams:Page>
		</c:otherwise>
	</c:choose>


</body>
</lams:html>


	
