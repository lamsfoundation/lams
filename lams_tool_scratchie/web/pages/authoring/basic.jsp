<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<c:set var="sessionMapID" value="${formBean.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:if test="${itemList == null}">
	<c:set var="itemList" value="${sessionMap.itemList}"/>
</c:if>

<script lang="javascript">

	var itemTargetDiv = "#itemArea";

	function addItem(){
		//check maximum number of answers
		var numberOfAnswers = $("textarea[name^=itemDescription]").length;
		if (numberOfAnswers >= 10) {
			alert("<fmt:message key="label.authoring.maximum.answers.warning" />");
			return;
		}
		
		var url= "<c:url value='/authoring/addItem.do'/>";
		prepareItemEditorsForAjaxSubmit();
		var itemList = $("#authoringForm").serialize(true);
		$(itemTargetDiv).load(
			url,
			{
				sessionMapID: "${sessionMapID}",
				itemList: itemList 
			}
		);
	}
	function removeItem(idx){
		var url= "<c:url value='/authoring/removeItem.do'/>";
		prepareItemEditorsForAjaxSubmit();
		var itemList = $("#authoringForm").serialize(true);
		$(itemTargetDiv).load(
				url,
				{
					sessionMapID: "${sessionMapID}",
					itemIndex: idx,
					itemList: itemList 
				}
		);
	}
	function upItem(idx){
		var url= "<c:url value='/authoring/upItem.do'/>";
		prepareItemEditorsForAjaxSubmit();
		var itemList = $("#authoringForm").serialize(true);
		$(itemTargetDiv).load(
				url,
				{
					sessionMapID: "${sessionMapID}",
					itemIndex: idx,
					itemList: itemList 
				}
		);
	}
	function downItem(idx){
		var url= "<c:url value='/authoring/downItem.do'/>";
		prepareItemEditorsForAjaxSubmit();
		var itemList = $("#authoringForm").serialize(true);
		$(itemTargetDiv).load(
				url,
				{
					sessionMapID: "${sessionMapID}",
					itemIndex: idx,
					itemList: itemList 
				}
		);
	}
	
	//in order to be able to use item's value, copy it from CKEditor to textarea
	function prepareItemEditorsForAjaxSubmit(){
		$("textarea[name^=itemDescription]").each(function() {
			var ckeditorData = CKEDITOR.instances[this.name].getData();
			//skip out empty values
			this.value = ((ckeditorData == null) || (ckeditorData.replace(/&nbsp;| |<br \/>|\s|<p>|<\/p>|\xa0/g, "").length == 0)) ? "" : ckeditorData;
		});
	}


</script>
<!-- Basic Tab Content -->
<table class="space-bottom">
	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="label.authoring.basic.title"/>
			</div>
			<html:text property="scratchie.title" style="width: 99%;"/>
		</td>
	</tr>
	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="label.authoring.basic.instruction"/>
			</div>
			<lams:CKEditor id="scratchie.instructions" value="${formBean.scratchie.instructions}" contentFolderID="${formBean.contentFolderID}"/>
		</td>
	</tr>


</table>

<!-- Items -->
<div>
	<div  id="itemArea">
		<%@ include file="parts/itemlist.jsp"%>
	</div>
	
	<div  style="margin: 0 40px 80px;">
	<a href="javascript:;" onclick="addItem();" class="button-add-item right-buttons">
		<fmt:message key="label.authoring.basic.add.another.scratchie" /> 
	</a>
	</div>
</div>
