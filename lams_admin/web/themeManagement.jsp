<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.themes.title"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
	
	<script type="text/javascript">
		function editTheme(name, description, imageDirectory, id, currentDefaultTheme) {
			document.getElementById("name").value = name;
			document.getElementById("description").value = description;
			document.getElementById("imageDirectory").value = imageDirectory;
			document.getElementById("id").value = id;
			
			if(currentDefaultTheme == "true") {
				document.getElementById("currentDefaultTheme").checked = true;
			}
			document.getElementById("cancelEdit").style.display="block";
			document.getElementById("normalSave").style.display="none";
		}
	
		function cancelEdit() {
			document.getElementById("name").value = "";
			document.getElementById("description").value = "";
			document.getElementById("imageDirectory").value = "";
			document.getElementById("id").value = "";
			document.getElementById("currentDefaultTheme").checked = false;
			document.getElementById("cancelEdit").style.display="none";
			document.getElementById("normalSave").style.display="block";
		}
	
		function removeTheme(id, name) {
			var answer = confirm("<fmt:message key="admin.themes.deleteConfirm" />")
			if (answer) {
				document.getElementById("name").value = name;
				document.getElementById("id").value = id;
				submitForm('removeTheme');
			}
		}
	
		function setAsDefault(name) {
			document.getElementById("name").value = name;
			submitForm('setAsDefault');
		} 
	
		function checkAndSubmit() {
			var name = document.getElementById("name").value;
	
			if (name == null || name == "") {
				alert("<fmt:message key="admin.themes.nameAlreadyExists" />");
				return;
			}
			
			<c:forEach var="theme" items="${themes}" >
				if (name == '${theme.name}') {
					alert("<fmt:message key="admin.themes.nameAlreadyExists" />");
					return;
				}
			</c:forEach>
			submitForm('addOrEditTheme');
		}

		function submitForm(methodName) {
			var f = document.getElementById('themeForm');
			if (methodName) {
				f.action = methodName + ".do?<csrf:token/>";
				}
			f.submit();
		}
		
	</script>
</lams:head>
    
<body class="component pb-4 pt-2 px-2 px-sm-4">
	<%-- Build breadcrumb --%>
	<c:set var="breadcrumbItems"><lams:LAMSURL/>admin/appadminstart.do | <fmt:message key="appadmin.maintain" /></c:set>
	<c:set var="breadcrumbItems">${breadcrumbItems}, . | <fmt:message key="admin.themes.title"/></c:set>


	<lams:Page5 type="admin" title="${title}" formID="themeForm" breadcrumbItems="${breadcrumbItems}">	
			
			<table class="table table-striped table-bordered bg-white align-middle" >
				<thead>
				<tr>
					<th>
						<fmt:message key="admin.themes.theme" />
					</th>
					<th>
						<fmt:message key="admin.themes.description" />
					</th>
					<th>
						<fmt:message key="admin.themes.imageDir" />
					</th>
					<th>
						<fmt:message key="admin.themes.defaultTheme" />
					</th>
					<th class="text-center">
						<fmt:message key="admin.actions" />
					</th>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="theme" items="${themes}" >
					<tr>
						<td>
							<c:out value="${theme.name}" escapeXml="true"/>
						</td>
						<td>
							<c:out value="${theme.description}" escapeXml="true"/>
						</td>
						<td>
							<c:out value="${theme.imageDirectory}" escapeXml="true"/>
						</td>
						<td class="text-center">
							<c:if test="${theme.currentDefaultTheme}">
								<i class="fa fa-check text-success"></i>
							</c:if>
						</td>
						<td class="text-center">
			
							<c:choose>
								<c:when test="${not theme.notEditable}">
									<i class="btn btn-secondary fa fa-pencil" 
										title="<fmt:message key="admin.themes.edit" />"
										onclick="editTheme('${theme.name}', '${theme.description}', '${theme.imageDirectory}', '${theme.themeId}', '${theme.currentDefaultTheme}')"
									></i>
									<i class="btn btn-danger fa fa-times" 
										title="<fmt:message key="admin.themes.remove" />"
										onclick="removeTheme('${theme.themeId}', '${theme.name}')"
									></i>
								</c:when>
								<c:otherwise>
									<a href="javascript:setAsDefault('${theme.name}')" class="btn btn-secondary" title="<fmt:message key="admin.themes.makeThemeDefault"/>">
										<fmt:message key="admin.themes.makeDefault" />
									</a>
								</c:otherwise>
							</c:choose>
						</td>	
					</tr>
				</c:forEach>
				</tbody>
			</table>
			
			<h2><fmt:message key="admin.themes.addNew" /></h2>
			<form:form action="addOrEditTheme.do" method="post" modelAttribute="themeForm" id="themeForm">	
				<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
				<form:hidden path="id" id="id" />
				
				<div class="mb-3">
					<label for="name"><fmt:message key="admin.themes.name" /></label> <span class="text-danger">*</span>
					<input class="form-control" id="name" name="name" type="text" value="" maxlength="40" required>
				</div>
				<div class="mb-3">
					<label for="description"><fmt:message key="admin.themes.description" /></label>:
					<form:input class="form-control" path="description" id="description" />
				</div>
				<div class="mb-3">
					<label class="form-check-label" for="imageDirectory"><fmt:message key="admin.themes.imageDir" /></label>:
					<form:input class="form-control" path="imageDirectory" id="imageDirectory" size="40" />
				</div>
				<div class="form-check">
					<form:checkbox class="form-check-input"  path="currentDefaultTheme" id="currentDefaultTheme" />
					<label class="form-check-label" for="currentDefaultTheme"><fmt:message key="admin.themes.makeThemeDefault" /></label>
				</div>
				
				<div id="normalSave" class="float-end">
					<a href="<lams:LAMSURL/>admin/appadminstart.do" class="btn btn-secondary">
						<fmt:message key="admin.cancel"/>
					</a>
					<a href="javascript:checkAndSubmit()" class="btn btn-primary"><fmt:message key="admin.save" /></a>
				</div>
				<div id="cancelEdit" style="display:none;" class="float-end">
					<a href="javascript:cancelEdit()" class="btn btn-secondary"><fmt:message key="admin.cancel" /></a>
					<a href="javascript:submitForm()" class="btn btn-primary"><fmt:message key="admin.save" /></a>
				</div>
			</form:form>
	</lams:Page5>
</body>
</lams:html>
