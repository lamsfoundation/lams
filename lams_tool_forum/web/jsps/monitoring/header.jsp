<%@ include file="/includes/taglibs.jsp"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<script type="text/javascript">
     //<![CDATA[
	function init(){
		selectTab(1);
	}  
        
	function doSelectTab(tabId) {
		var tag = document.getElementById("currentTab");
		tag.value = tabId;
		selectTab(tabId);
		
    	//for statistic page change:
    	if(tabId == 4)
    		doStatistic();	 		
	}
	var statisticTargetDiv = "statisticArea";
	function doStatistic(){
		var url = "<c:url value="/monitoring/statistic.do"/>";
	    var reqIDVar = new Date();
		var param = "toolContentID=" + ${param.toolContentID} +"&reqID="+reqIDVar.getTime();
		messageLoading();
	    var myAjax = new Ajax.Updater(
		    	statisticTargetDiv,
		    	url,
		    	{
		    		method:'get',
		    		parameters:param,
		    		onComplete:messageComplete,
		    		evalScripts:true
		    	}
	    );
		
	}
	
	function showBusy(targetDiv){
		if($(targetDiv+"_Busy") != null){
			Element.show(targetDiv+"_Busy");
		}
	}
	function hideBusy(targetDiv){
		if($(targetDiv+"_Busy") != null){
			Element.hide(targetDiv+"_Busy");
		}				
	}
	function messageLoading(){
		showBusy(statisticTargetDiv);
	}
	function messageComplete(){
		hideBusy(statisticTargetDiv);
	}        
	//]]>        
</script>