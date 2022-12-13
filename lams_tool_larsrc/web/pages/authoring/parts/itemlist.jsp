<%@ include file="/common/taglibs.jsp"%>

<div id="itemList">

<div class="panel panel-default voffset5">
	<div class="panel-heading panel-title">
		<fmt:message key="label.authoring.basic.resource.list.title" /> 
		<lams:WaitingSpinner id="resourceListArea_Busy" showInline="true"/>
	</div>

	<table class="table table-striped table-condensed" id="itemTable">
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
		<c:set var="listSize" value="${fn:length(sessionMap.resourceList)}" />
		<c:forEach var="resource" items="${sessionMap.resourceList}"
			varStatus="status">
			<tr>
				<%-- Resource Type:1=URL,2=File--%>
				<c:if test="${resource.type == 1}">
					<td>
						<span class="field-name"> <fmt:message
								key="label.authoring.basic.resource.url" /> : </span>
						<c:out value="${resource.title}" escapeXml="true"/>
					</td>
					<td width="5%">
						<i class="fa fa-eye" title="<fmt:message key="label.authoring.basic.resource.preview" />"
							onclick="previewItem(1, ${status.index},'${sessionMapID}')">
						</i>
					</td>
				</c:if>

				<c:if test="${resource.type == 2}">
					<td>
						<span class="field-name"> <fmt:message
								key="label.authoring.basic.resource.file" /> : </span>
						<c:out value="${resource.title}" escapeXml="true"/>
					</td>
					<td width="5%">
						<i class="fa fa-eye" title="<fmt:message key="label.authoring.basic.resource.preview" />"
							onclick="previewItem(2, ${status.index},'${sessionMapID}')">
						</i>
					</td>
				</c:if>
				<c:if test="${resource.type == 3}">
					<td>
						<span class="field-name"> <fmt:message
								key="label.authoring.basic.resource.website" /> : </span>
						<c:out value="${resource.title}" escapeXml="true"/>
					</td>
					<td width="5%">
						<i class="fa fa-eye" title="<fmt:message key="label.authoring.basic.resource.preview" />"
							onclick="previewItem(3, ${status.index},'${sessionMapID}')">
						</i>
					</td>
				</c:if>
				
				<td class="arrows" style="width:5%">
					<!-- Don't display up icon if first line -->
					<c:if test="${not status.first}">
	 					<lams:Arrow state="up" titleKey="label.up" onclick="switchItem(${status.index}, ${status.index - 1}, '${sessionMapID}')"/>
	 				</c:if>
					<!-- Don't display down icon if last line -->
					<c:if test="${not status.last}">
						<lams:Arrow state="down" titleKey="label.down" onclick="switchItem(${status.index}, ${status.index + 1}, '${sessionMapID}')"/>
	 				</c:if>
				</td>
				<td align="center" style="width:5%"><i class="fa fa-pencil"	title="<fmt:message key="label.edit" />" id="edit${status.index}"
					onclick="editItem(${status.index},'${sessionMapID}')"></i></td>
				<td  align="center" style="width:5%"><i class="fa fa-times"	title="<fmt:message key="label.delete" />" id="delete${status.index}" 
					onclick="deleteItem(${status.index},'${sessionMapID}')"></i></td>
				
			</tr>
		</c:forEach>
	</table>
</div>
</div>

<%-- This script will works when a new resource item submit in order to refresh "Resource List" panel. --%>
<script lang="javascript">
	hideResourceItem();
	var obj = document.getElementById('resourceListArea');
	obj.innerHTML= document.getElementById("itemList").innerHTML;
</script>
