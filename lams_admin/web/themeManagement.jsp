<%@ include file="/taglibs.jsp"%>


<script type="text/javascript">
<!--
	function editTheme(name, description, imageDirectory, id, currentDefaultTheme, type) {
		document.getElementById("name").value = name;
		document.getElementById("description").value = description;
		document.getElementById("imageDirectory").value = imageDirectory;
		document.getElementById("id").value = id;
		
		document.getElementById("method").value = "addOrEditTheme";

		if(currentDefaultTheme == "true") {
			document.getElementById("currentDefaultTheme").checked = true;
		}
		document.getElementById("type").options[type -1].selected = true;
		
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
		document.getElementById("type").options[0].selected = true;
	}

	function removeTheme(id, name, type) {
		var answer = confirm("<fmt:message key="admin.themes.deleteConfirm" />")
		if (answer) {
			document.getElementById("name").value = name;
			document.getElementById("id").value = id;
			document.getElementById("type").options[type -1].selected = true;
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
//-->
</script>

<h4 class="align-left">
		<a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a>
</h4>

<h1><fmt:message key="admin.themes.title" /></h1>


<table class="alternative-color">
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
			<fmt:message key="admin.themes.type" />
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
					<img src="<lams:LAMSURL/>/images/tick.png" >
				</c:if>
			</td>
			<td>
				<c:choose>
					<c:when test="${theme.type==1}">
						<fmt:message key="admin.themes.html" />
					</c:when>
					<c:otherwise>
						<fmt:message key="admin.themes.flash" />
					</c:otherwise>
				</c:choose>
			</td>
			<td align="center">

				<c:choose>
					<c:when test="${not theme.notEditable}">
						<img src="<lams:LAMSURL/>/images/edit.gif" 
							title="<fmt:message key="admin.themes.edit" />"
							onclick="editTheme('${theme.name}', '${theme.description}', '${theme.imageDirectory}', '${theme.themeId}', '${theme.currentDefaultTheme}', '${theme.type}')"
						>
						<img src="<lams:LAMSURL/>/images/cross.gif" 
							title="<fmt:message key="admin.themes.remove" />"
							onclick="removeTheme('${theme.themeId}', '${theme.name}', '${theme.type}')"
						>
					</c:when>
					<c:otherwise>
						<a href="javascript:setAsDefault('${theme.name}')" class="button" title="<fmt:message key="admin.themes.makeThemeDefault"/>">
							<fmt:message key="admin.themes.makeDefault" />
						</a>
					</c:otherwise>
				</c:choose>
			</td>	
		</tr>
	</c:forEach>
</table>

<h3>
	<fmt:message key="admin.themes.addNew" />
</h3>
<br />
<html:form action="/themeManagement" method="post" styleId="themeForm">	
	<html:hidden property="method" styleId="method" value="addOrEditTheme"/>
	<html:hidden property="id" styleId="id" />
	
	<table>
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
		<tr>
			<td>
				<fmt:message key="admin.themes.type" />:
			</td>
			<td>
				<html:select property="type" styleId="type">
					<html:option value="1"><fmt:message key="admin.themes.html" /></html:option>
					<html:option value="2"><fmt:message key="admin.themes.flash" /></html:option>
				</html:select>
			</td>
		</tr>
	</table>
	
	<br />
	
	<div id="normalSave">
		<a href="javascript:checkAndSubmit()" class="button"><fmt:message key="admin.save" /></a>
	</div>
	<div id="cancelEdit" style="display:none;">
		<a href="javascript:submitForm()" class="button"><fmt:message key="admin.save" /></a> &nbsp;
		<a href="javascript:cancelEdit()" class="button"><fmt:message key="admin.cancel" /></a>
	</div>
</html:form>

