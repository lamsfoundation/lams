<!DOCTYPE html>

<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>


<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />
<c:set var="loading_animation" value="${lams}images/ajax-loader.gif"/>
<c:set var="loading_words"><fmt:message key="msg.loading" /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:html>
	<lams:head>
		
		<!-- ********************  CSS ********************** -->
		<lams:css />
		<link type="text/css" href="<lams:LAMSURL />css/jquery.treetable.css" rel="stylesheet"/>
		<link type="text/css" href="<lams:LAMSURL />css/jquery.treetable.lams.css" rel="stylesheet"/>
		
		<!-- ********************  javascript ********************** -->
		<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery-ui.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.jscroll.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.treetable.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/comments.js"></script>		
		<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.timeago.js"></script>		

		<script type="text/javascript">
			$(document).ready(function(){
			    scrollDoneCallback();
			    
   				$('#sortMenu').change(function(){
			        var url = "<lams:LAMSURL />comments/viewTopic.do?sessionMapID=${sessionMapID}&sticky=true&sortBy="+$(this).find("option:selected").attr('value');
			        reloadDivs(url);
			    });
			});
			
			function refreshComments(){
				var reqIDVar = new Date();
				reloadDivs('<lams:LAMSURL />comments/viewTopic.do?sessionMapID=${sessionMapID}&sticky=true&reqUid='+reqIDVar.getTime());
			}

			function reloadDivs(url) {
				$('.scroll').data('jscroll', null);
				$.ajaxSetup({ cache: true });
				$('#commentDiv').load(url);
			}
			
			function scrollDoneCallback() {
			}
		</script>		
		
	</lams:head>
	<body>
		
			<div class="voffset10">
			<c:if test='${not sessionMap.readOnly}'>
              <div class="comments"> 
			<%@ include file="new.jsp"%>
              </div>
			</c:if>

			
			<select id="sortMenu" name="sortMenu" class="form-control">
    			<option value='0' <c:if test='${sessionMap.sortBy == 0}'>selected</c:if>><fmt:message key="label.newest.first"/></option>
    			<option value='1' <c:if test='${sessionMap.sortBy == 1}'>selected</c:if>><fmt:message key="label.top.comments"/></option>
			</select>

			<div id="commentDiv" class="voffset2">
			<%@ include file="allview.jsp"%>
			</div>	
			</div>
	</body>
</lams:html>
