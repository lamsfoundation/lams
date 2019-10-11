<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>
<c:set var="serverVersion"><%=Configuration.get(ConfigurationKeys.VERSION)%></c:set>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="sysadmin.maintain"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
</lams:head>
    
<body class="stripes">
	<lams:Page type="admin" title="${title}">
		<div class="row"> 
			<div class="col-xs-12"> 
				<div class="alert alert-info text-center"> 
					<img src="/lams/images/svg/lams_logo_black.svg" width="100px"><br> 
					<fmt:message key="config.version" />&nbsp;${serverVersion}
				</div> 
   			</div> 
  		</div>
	
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
</body>
</lams:html>


	
