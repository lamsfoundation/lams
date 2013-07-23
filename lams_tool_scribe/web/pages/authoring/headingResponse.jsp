<%@ include file="/common/taglibs.jsp"%>

<div style="display: none;">

	<div id="itemList">
		<%@ include file="parts/headingList.jsp"%>
	</div>

	<script type="text/javascript">
		var win = window.hideMessage ? window : window.top;
		if (win.hideMessage != null){
			win.hideMessage();
			var obj = win.document.getElementById('itemListArea');
			obj.innerHTML= document.getElementById("itemList").innerHTML;
		}
	</script>

</div>


