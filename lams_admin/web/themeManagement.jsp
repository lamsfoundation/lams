<%@ include file="/taglibs.jsp"%>


<script type="text/javascript">
	function editTheme(name, description, imageDirectory, id, currentDefaultTheme) {
		document.getElementById("name").value = name;
		document.getElementById("description").value = description;
		document.getElementById("imageDirectory").value = imageDirectory;
		document.getElementById("id").value = id;
		
		document.getElementById("method").value = "addOrEditTheme";

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
			document.getElementById("method").value = "removeTheme";
			submitForm();
		}
	}

	function setAsDefault(name) {
		document.getElementById("name").value = name;
		document.getElementById("method").value = "setAsDefault";
		submitForm();
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
		submitForm();
	}

	function submitForm() {
		document.getElementById("themeForm").submit();
	}
</script>

<p><a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a></p>


<table class="table table-striped table-condensed" >
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
		<th>
		</th>
		<th>
		</th>
	</tr>
	<c:forEach var="theme" items="${themes}" >
		<tr>
			<td>
				${theme.name}
			</td>
			<td>
				${theme.description}
			</td>
			<td>
				${theme.imageDirectory}
			</td>
			<td align="center">
				<c:if test="${theme.currentDefaultTheme}">
					<i class="fa fa-check"></i>
				</c:if>
			</td>
			<td align="center">

				<c:choose>
					<c:when test="${not theme.notEditable}">
						<i class="fa fa-pencil" 
							title="<fmt:message key="admin.themes.edit" />"
							onclick="editTheme('${theme.name}', '${theme.description}', '${theme.imageDirectory}', '${theme.themeId}', '${theme.currentDefaultTheme}')"
						></i>
						<i class="fa fa-times" 
							title="<fmt:message key="admin.themes.remove" />"
							onclick="removeTheme('${theme.themeId}', '${theme.name}')"
						></i>
					</c:when>
					<c:otherwise>
						<a href="javascript:setAsDefault('${theme.name}')" class="btn btn-default btn-sm" title="<fmt:message key="admin.themes.makeThemeDefault"/>">
							<fmt:message key="admin.themes.makeDefault" />
						</a>
					</c:otherwise>
				</c:choose>
			</td>	
		</tr>
	</c:forEach>
</table>

<div class="panel panel-default">
	<div class="panel-heading">
		<div class="panel-title"><fmt:message key="admin.themes.addNew" /></div>
	</div>
	
	<div class="panel-body">
	<html:form action="/themeManagement" method="post" styleId="themeForm">	
	<html:hidden property="method" styleId="method" value="addOrEditTheme"/>
	<html:hidden property="id" styleId="id" />
	
	
	<table class="table table-no-border" >
		<tr>
			<td>
				* <fmt:message key="admin.themes.name" />:
			</td>
			<td>
				<html:text property="name" styleId="name" size="40"></html:text>
			</td>
		</tr>
		<tr>
			<td>
				<fmt:message key="admin.themes.description" />:
			</td>
			<td>
				<html:text property="description" styleId="description" size="40"></html:text>
			</td>
		</tr>
		<tr>
			<td>
				<fmt:message key="admin.themes.imageDir" />:
			</td>
			<td>
				<html:text property="imageDirectory" styleId="imageDirectory" size="40"></html:text>
			</td>
		</tr>
		<tr>
			<td>
				<fmt:message key="admin.themes.makeThemeDefault" />:
			</td>
			<td>
				<html:checkbox property="currentDefaultTheme" styleId="currentDefaultTheme" />
			</td>
		</tr>
	</table>
	
	<div class="pull-right">
	<div id="normalSave">
		<a href="javascript:checkAndSubmit()" class="btn btn-default"><fmt:message key="admin.save" /></a>
	</div>
	<div id="cancelEdit" style="display:none;">
		<a href="javascript:cancelEdit()" class="btn btn-default"><fmt:message key="admin.cancel" /></a>
		<a href="javascript:submitForm()" class="btn btn-default loffset5"><fmt:message key="admin.save" /></a>
	</div>
	</div>
	</html:form>
	
</div>
</div>

