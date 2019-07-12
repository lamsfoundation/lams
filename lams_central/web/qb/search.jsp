<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<lams:html>
<lams:head>
	<title>Question Bank Search</title>
	
	<lams:css />
	
	<style>
		#close-button .fa {
		    font-size: 16px;
		    vertical-align: super;
		}
		#main-panel {
			border: none; 
			box-shadow: none;
		}
		
		/*----------STICKY FOOTER----------------*/
		html {
		    position: relative;
		    min-height: 100%;
		}
		body {
			margin-bottom: 44px;
		}
		footer {
			position: absolute;
		    bottom: 0;
		    width: 100%;
		    height: 44px;
		}
		footer > div {
			height: 44px;
		}
	</style>

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
</lams:head>
<body>
	<div id="main-panel" class="panel panel-default">
		<div class="panel-heading panel-title">
			<fmt:message key="label.search.question.bank"/>
			
			<button type="button" id="close-button" class="close" data-dismiss="modal" aria-label="Close"
				onclick="javascript:self.parent.tb_remove();">
				<i class="fa fa-times" aria-hidden="true"></i>
			</button>
		</div>
			
		<div class="panel-body">
			<%@ include file="searchWidget.jsp"%>
		</div>
	</div>
	
	<footer class="footer fixed-bottom">
		<div class="panel-heading">
        	<div class="pull-right">
			    <a href="#nogo" onclick="javascript:self.parent.tb_remove();" class="btn btn-sm btn-default loffset5" style="display: inline;">
					<fmt:message key="button.close" />
				</a>
			</div>	
      	</div>
    </footer>
</body>
</lams:html>
