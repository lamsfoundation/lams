<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>
<c:set var="serverVersion"><%=Configuration.get(ConfigurationKeys.VERSION)%></c:set>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="appadmin.maintain"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
</lams:head>
    
<body class="component pb-4 pt-2 px-2 px-sm-4">
	<lams:Page5 type="admin" title="${title}">
		<div class="row"> 
			<div class="col text-center"> 
				<a href="/" class="lams-logo" alt="LAMS logo">
					<img src="<lams:LAMSURL/>images/svg/lamsv5_logo.svg" alt="LAMS logo" />
				</a>
   			</div> 
  		</div>
		<div class="row"> 
			<div class="col text-center my-2"> 
				<fmt:message key="config.version" />&nbsp;${serverVersion}
   			</div> 
  		</div>
 		
		<c:forEach items="${groupedLinks}" var="links">
			<div class="row">
				<div class="col text-center h4">
					<fmt:message key="${links[0]}"/>
				</div>
			</div>
			

			<div class="row mb-3">
				<div class="col-6 offset-3">
					<ul class="list-group">
			 			<c:forEach items="${links[1]}" var="linkBean">
							<li class="list-group-item">
								<a href="<c:out value="${linkBean.link}"/>" class="text-decoration-none">
									<fmt:message><c:out value="${linkBean.name}"/></fmt:message>
								</a>
							</li>
						</c:forEach>
					</ul>
				</div>
			</div>
		</c:forEach>
	</lams:Page5>
</body>
</lams:html>


	
