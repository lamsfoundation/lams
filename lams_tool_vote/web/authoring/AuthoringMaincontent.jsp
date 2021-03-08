<!DOCTYPE html>
        

<%@ include file="/common/taglibs.jsp"%>

<%@ page import="java.util.LinkedHashSet" %>
<%@ page import="java.util.Set" %>
<%@ page import="org.lamsfoundation.lams.tool.vote.VoteAppConstants"%>

	<lams:html>
	<lams:head>
		<title> <fmt:message key="activity.title"/>  </title>
		<lams:headItems/>

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
        	document.forms.voteAuthoringForm.action=method + ".do";
        	document.forms.voteAuthoringForm.submit();
        }
        
		function submitModifyNomination(optionIndexValue, actionMethod) {
			document.forms.voteAuthoringForm.optIndex.value=optionIndexValue; 
			submitMethod(actionMethod);
		}

		function submitModifyAuthoringNomination(questionIndexValue, actionMethod) {
			document.forms.voteAuthoringForm.questionIndex.value=questionIndexValue; 
			submitMethod(actionMethod);
		}
		
		function submitMethod(actionMethod) {
			document.forms.voteAuthoringForm.action=actionMethod + ".do";
			document.forms.voteAuthoringForm.submit();
		}
		
		function deleteOption(deletableOptionIndex, actionMethod) {
			document.forms.voteAuthoringForm.deletableOptionIndex.value=deletableOptionIndex; 
			submitMethod(actionMethod);
		}
		
		function submitDeleteFile(uuid, actionMethod) {
			document.forms.voteAuthoringForm.uuid.value=uuid; 
			submitMethod(actionMethod);
		}
 
	</script>
	
</lams:head>
<body class="stripes" ><!-- onLoad="init();" -->
	<form:form modelAttribute="voteAuthoringForm" action="submitAllContent.do" method="POST">
		<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
		<form:hidden path="toolContentID"/>
		<form:hidden path="currentTab" styleId="currentTab" />
		<form:hidden path="httpSessionID"/>									
		<form:hidden path="contentFolderID"/>
		<input type="hidden" name="mode" value="${mode}">

		<c:set var="title"><fmt:message key="activity.title" /></c:set>
		<lams:Page title="${title}" type="navbar">
		
			 <lams:Tabs control="true" title="${title}" helpToolSignature="<%= VoteAppConstants.MY_SIGNATURE %>" helpModule="authoring">
                  <lams:Tab id="1" key="label.basic" />
                  <lams:Tab id="2" key="label.advanced" />
	         </lams:Tabs>   

			<lams:TabBodyArea>
				<lams:errors/>
              
                <!--  Set up tabs  -->
                 <lams:TabBodys>
    				<lams:TabBody id="1" page="BasicContent.jsp"/>
					<lams:TabBody id="2" page="AdvancedContent.jsp" />
                </lams:TabBodys>
  	
  				<lams:AuthoringButton formID="voteAuthoringForm" clearSessionActionUrl="/clearsession.do" toolSignature="lavote11" 
				cancelButtonLabelKey="label.cancel" saveButtonLabelKey="label.save" toolContentID="${voteAuthoringForm.toolContentID}" 
				contentFolderID="${voteAuthoringForm.contentFolderID}" accessMode="${mode}" defineLater="${mode=='teacher'}"/>
			</lams:TabBodyArea>
			
			<div id="footer"></div>

		</lams:Page>

	</form:form>

	<script type="text/javascript">
		<c:if test="${ not empty voteAuthoringForm.maxNominationCount }"> 			
			changeMinMaxVotes(${voteAuthoringForm.maxNominationCount}, ${voteAuthoringForm.minNominationCount});
		</c:if> 
	</script>

</body>
</lams:html>
