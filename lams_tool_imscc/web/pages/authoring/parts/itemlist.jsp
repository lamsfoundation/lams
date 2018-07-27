<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />
<c:set var="tool"><lams:WebAppURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<div id="itemList">
	<div class="panel panel-default voffset5">
		<div class="panel-heading panel-title">
			<fmt:message key="label.authoring.basic.resource.list.title" />
			<i class="fa fa-refresh fa-spin fa-1x fa-fw" style="display:none" id="commonCartridgeListArea_Busy"></i>
		</div>

		<table class="table table-condensed" id="itemTable">
			<c:forEach var="commonCartridge" items="${sessionMap.commonCartridgeList}" varStatus="status">
			
				<tr>
					<%-- CommonCartridge Type:1=URL,2=File,3=Website,4=Learning Object --%>
					<c:if test="${commonCartridge.type == 1}">
						<td>
							<span class="field-name"> 
								<fmt:message key="label.authoring.basic.resource.url" /> : 
							</span>
							<c:out value="${commonCartridge.title}" escapeXml="true"/>
						</td>
						<td width="8%">
							<a href="#" onclick="previewItem(1,${status.index},'${sessionMapID}')" class="btn btn-xs btn-default"> 
								<fmt:message key="label.authoring.basic.resource.verify.url" /> 
							</a>
						</td>
						<td width="5%">
							<c:set var="editItemUrl" >
								<c:url value='/authoring/editItemInit.do'/>?sessionMapID=${sessionMapID}&itemIndex=${status.index}&KeepThis=true&TB_iframe=true&modal=true
							</c:set>		
							<a href="${editItemUrl}" class="thickbox" style="border-style: none;"> 
								<i class="fa fa-pencil"	title="<fmt:message key="label.authoring.basic.resource.edit" />"></i>
							</a>
						</td>
						<td width="5%">
							<i class="fa fa-times" title="<fmt:message key="label.authoring.basic.resource.delete" />"
								onclick="javascript:deleteItem(${status.index},'${sessionMapID}')"></i>
						</td>
					</c:if>
	
					<c:if test="${commonCartridge.type == 2}">
						<td>
							<span class="field-name"> 
								<fmt:message key="label.authoring.basic.resource.file" /> : 
							</span>
							<c:out value="${commonCartridge.title}" escapeXml="true"/>
						</td>
						<td>
							<a href="#" onclick="previewItem(2,${status.index},'${sessionMapID}')"> 
								<fmt:message key="label.authoring.basic.resource.preview" /> 
							</a>
						</td>
						<td>
							<i class="fa fa-pencil"	title="<fmt:message key="label.authoring.basic.resource.edit" />"
								onclick="javascript:editItem(${status.index},'${sessionMapID}')"></i>	
						</td>
						<td>
							<i class="fa fa-times" title="<fmt:message key="label.authoring.basic.resource.delete" />"
								onclick="javascript:deleteItem(${status.index},'${sessionMapID}')"></i>
						</td>
					</c:if>
	
					<c:if test="${commonCartridge.type == 3}">
						<td>
							<span class="field-name"> 
								<fmt:message key="label.authoring.basic.resource.website" /> : 
							</span>
							<c:out value="${commonCartridge.title}" escapeXml="true"/>
						</td>
						<td>
							<a href="#" onclick="previewItem(3,${status.index},'${sessionMapID}')"> 
								<fmt:message key="label.authoring.basic.resource.preview" /> 
							</a>
						</td>
						<td>
							<i class="fa fa-pencil"	title="<fmt:message key="label.authoring.basic.resource.edit" />"
								onclick="javascript:editItem(${status.index},'${sessionMapID}')"></i>
						</td>
						<td>
							<i class="fa fa-times" title="<fmt:message key="label.authoring.basic.resource.delete" />"
								onclick="javascript:deleteItem(${status.index},'${sessionMapID}')"></i>
						</td>
					</c:if>
	
					<c:if test="${commonCartridge.type == 4}">
						<td>
							<span class="field-name"> 
								<fmt:message key="label.authoring.basic.resource.learning.object" /> : 
							</span>
							<c:out value="${commonCartridge.title}" escapeXml="true"/>
						</td>
						<td>
							<a href="#" onclick="previewItem(4,${status.index},'${sessionMapID}')"> 
								<fmt:message key="label.authoring.basic.resource.preview" /> 
							</a>
						</td>
						<td>
							<i class="fa fa-pencil"	title="<fmt:message key="label.authoring.basic.resource.edit" />"
								onclick="javascript:editItem(${status.index},'${sessionMapID}')"></i>
						</td>
						<td>
							<i class="fa fa-times" title="<fmt:message key="label.authoring.basic.resource.delete" />"
								onclick="javascript:deleteItem(${status.index},'${sessionMapID}')"></i>
						</td>
					</c:if>
				</tr>
			</c:forEach>
		</table>
	</div>
</div>

<%-- This script will works when a new resoruce item submit in order to refresh "CommonCartridge List" panel. --%>
<script lang="javascript">	
	if (window.parent && window.parent.refreshThickbox) {
		window.parent.tb_remove();
		var obj = window.parent.document.getElementById('commonCartridgeListArea');
		obj.innerHTML= document.getElementById("itemList").innerHTML;
		window.parent.refreshThickbox();
	}
</script>
