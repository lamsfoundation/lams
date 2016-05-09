<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean"
	value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<script type="text/javascript">
<!-- Common Javascript functions for LAMS -->

	/**
	 * Launches the popup window for the instruction files
	 */
	function showConditionMessage(url) {
		$("#conditionInputArea").load(url, function() {
			var area=document.getElementById("conditionInputArea");
			if(area != null){
				area.style.width="100%";
				area.style.height="100%";
				area.style.display="block";
			}
			var elem = document.getElementById("saveCancelButtons");
			if (elem != null) {
				elem.style.display="none";
			}
			location.hash = "conditionInputArea";
		});
	}
	function hideConditionMessage(){
		var area=document.getElementById("conditionInputArea");
		if(area != null){
			area.style.width="0px";
			area.style.height="0px";
			area.style.display="none";
		}
		var elem = document.getElementById("saveCancelButtons");
		if (elem != null) {
			elem.style.display="block";
		}
	}

	function editCondition(orderId,sessionMapID){
		 var reqIDVar = new Date();
		var url = "<c:url value="/authoring/editCondition.do?orderId="/>" + orderId +"&sessionMapID="+sessionMapID;;
		showConditionMessage(url);
	}
	//The panel of taskList list panel
	var conditionListTargetDiv = "conditionsArea";
	function deleteCondition(orderId,sessionMapID){
		var url = "<c:url value="/authoring/removeCondition.do"/>";
	    var reqIDVar = new Date();
		var param = "orderId=" + orderId +"&sessionMapID="+sessionMapID;
		$.ajax({
            type: 'get', 
            url: url,
            data: param,
            success: function(data) {
            	$("#"+conditionListTargetDiv).html(data);
            }
        });
	}
	
	function upCondition(orderId,sessionMapID){
		var url = "<c:url value="/authoring/upCondition.do"/>";
	    var reqIDVar = new Date();
		var param = "orderId=" + orderId + "&sessionMapID="+sessionMapID;;
		$.ajax({
            type: 'get', 
            url: url,
            data: param,
            success: function(data) {
            	$("#"+conditionListTargetDiv).html(data);
            }
        });
	}
	function downCondition(orderId,sessionMapID){
		var url = "<c:url value="/authoring/downCondition.do"/>";
	    var reqIDVar = new Date();
		var param = "orderId=" + orderId + "&sessionMapID="+sessionMapID;;
		$.ajax({
            type: 'get', 
            url: url,
            data: param,
            success: function(data) {
            	$("#"+conditionListTargetDiv).html(data);
            }
        });
	}
	
	//Packs additional elements and submits the question form
	function submitCondition(){
	
	    $.ajax({ // create an AJAX call...
           	type: $("#forumConditionForm").attr('method'),
			url: $("#forumConditionForm").attr('action'),
			data: $("#forumConditionForm").serialize(), 
			success: function(data) {
               $('#forumConditionForm').html(data);
			}
	    });
	}   

</script>

<!-- Conditions Tab Content -->

<div id="conditionsArea">
	<c:set var="sessionMapID" value="${formBean.sessionMapID}" />
	<%@ include file="/jsps/authoring/conditionList.jsp"%>
</div>
 
 <div class="form-inline">
	<a href="javascript:showConditionMessage('<html:rewrite page="/authoring/newConditionInit.do?sessionMapID=${formBean.sessionMapID}"/>');" 
		class="btn btn-default btn-sm"><i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.authoring.conditions.add.condition" /></a> 
</div>

<div id="conditionInputArea" name="conditionInputArea" class="voffset10"></div>
