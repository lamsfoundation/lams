<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<%@ page import="java.util.LinkedHashSet" %>
<%@ page import="java.util.Set" %>
<%@ page import="org.lamsfoundation.lams.tool.vote.VoteAppConstants"%>

    <% 
		Set tabs = new LinkedHashSet();
		tabs.add("label.basic");
		tabs.add("label.advanced");
		pageContext.setAttribute("tabs", tabs);
	%>

	<lams:html>
	<lams:head>
	<title> <fmt:message key="activity.title"/>  </title>

	<%@ include file="/common/tabbedheader.jsp"%>
	<script type="text/javascript" src="${lams}includes/javascript/prototype.js"></script>

	<script type="text/javascript">
		var noneDataFlowSelectedPreviously = null;
		
        function init(){
	    	var tag = document.getElementById("currentTab");
			if(tag.value != "")
		    	selectTab(tag.value);
	        else
	        	selectTab(1); //select the default tab;
			
			//actually, we don't set the value right, but opposite, so onSelectDataInput() can work
			noneDataFlowSelectedPreviously =  document.getElementById("dataFlowNoneOption")!=null 
											  && !document.getElementById("dataFlowNoneOption").selected;
			onSelectDataInput();
        }     
        
        function doSelectTab(tabId) {
        	var tag = document.getElementById("currentTab");
	    	tag.value = tabId;
	    	selectTab(tabId);
        } 

        function changeMinMaxVotes(maxNominationCount, minNominationCount) {
        	processSelect("maxNominationCount", maxNominationCount);
        	processSelect("minNominationCount", minNominationCount);
		}

        function processSelect(id, initSelectedItem) {        
			var select = document.getElementById(id);
			if (select) {
				var table = document.getElementById("itemTable");
				var trs = table.getElementsByTagName("tr");    
				var options = select.options;
				var numberOptions = select.length;
				
				//when first enter, it should get value from VoteContent
				var selectedItem = (initSelectedItem < 0) ? -1 : initSelectedItem;
				for (var i = 0; i < numberOptions; i++) {
					if(options[0].selected && selectedItem == -1 ){
						selectedItem = options[0].value;
					}
					select.removeChild(options[0]);
				}
	
				var isTextAllowed = document.getElementById("allowText").checked ? 1 : 0;
				for(var i = 1; i <= (trs.length+isTextAllowed); i++){
					var opt = document.createElement("option");
					var optT = document.createTextNode(i);
					opt.value = i;
					//get back user choosen value
					if(selectedItem > 0 && selectedItem == i){
						opt.selected = true;
					} else {
						opt.selected = false;
					}
					opt.appendChild(optT);
					select.appendChild(opt);
				}
			}
        }		
        
        function doSubmit(method) {
        	document.VoteAuthoringForm.dispatch.value=method;
        	document.VoteAuthoringForm.submit();
        }
        
		function submitModifyNomination(optionIndexValue, actionMethod) {
			document.VoteAuthoringForm.optIndex.value=optionIndexValue; 
			submitMethod(actionMethod);
		}

		function submitModifyAuthoringNomination(questionIndexValue, actionMethod) {
			document.VoteAuthoringForm.questionIndex.value=questionIndexValue; 
			submitMethod(actionMethod);
		}
		
		function submitMethod(actionMethod) {
			document.VoteAuthoringForm.dispatch.value=actionMethod; 
			document.VoteAuthoringForm.submit();
		}
		
		function deleteOption(deletableOptionIndex, actionMethod) {
			document.VoteAuthoringForm.deletableOptionIndex.value=deletableOptionIndex; 
			submitMethod(actionMethod);
		}
		
		function submitDeleteFile(uuid, actionMethod) {
			document.VoteAuthoringForm.uuid.value=uuid; 
			submitMethod(actionMethod);
		}

	</script>
	
</lams:head>
<body class="stripes" onLoad="init();">

<div id="page">
	<h1> <fmt:message key="label.authoring.vote"/> </h1>
	
	<div id="header">			
		<lams:Tabs collection="${tabs}" useKey="true" control="true"/>								
	</div>
	
	<div id="content">	
		<html:form  styleId="authoringForm" action="/authoring?validate=false" enctype="multipart/form-data" method="POST" target="_self">
		<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
		<html:hidden property="dispatch" value="submitAllContent"/>
		<html:hidden property="toolContentID"/>
		<html:hidden property="currentTab" styleId="currentTab" />
		<html:hidden property="httpSessionID"/>									
		<html:hidden property="contentFolderID"/>
		<input type="hidden" name="mode" value="${mode}">
		
		<%@ include file="/common/messages.jsp"%>
		
		<lams:help toolSignature="<%= VoteAppConstants.MY_SIGNATURE %>" module="authoring"/>
					
			<lams:TabBody id="1" titleKey="label.basic" page="BasicContent.jsp"/>
			      
			<lams:TabBody id="2" titleKey="label.advanced" page="AdvancedContent.jsp" />

			<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" toolSignature="lavote11" 
				cancelButtonLabelKey="label.cancel" saveButtonLabelKey="label.save" toolContentID="${formBean.toolContentID}" 
				contentFolderID="${formBean.contentFolderID}" accessMode="${mode}" defineLater="${mode=='teacher'}"/>
			
		</html:form>
	</div>

	<div id="footer"></div>
	</div>

	<script type="text/javascript">
		<c:if test="${ not empty formBean.maxNominationCount }"> 			
			changeMinMaxVotes(${formBean.maxNominationCount}, ${formBean.minNominationCount});
		</c:if>
	</script>

</body>
</lams:html>
