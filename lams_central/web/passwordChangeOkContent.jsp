<!DOCTYPE html>

<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-bean" prefix="bean"%>
<%@ taglib uri="tags-logic" prefix="logic"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<lams:html>
<lams:head>
	<link rel="stylesheet" href="css/defaultHTML_learner.css" type="text/css" />
</lams:head>

<body>
	<div style="clear: both"></div>

	<div class="container">
		<div class="row vertical-center-row">
			<div class="col-xs-12 col-sm-8 col-sm-offset-2 col-md-6 col-md-offset-3">
				<div class="panel voffset20">
					<div class="panel-body">
					<div class="col-xs-12 text-center">
						<fmt:message key="msg.password.changed"/>
					</div>
					
					<div class="col-xs-12 text-center voffset10">
						<a class="btn btn-sm btn-default"
							href="<lams:LAMSURL/>index.do" role="button">
							Ok
						</a>
					</div>
					
					</div>
				</div>
			</div>
		</div>
	</div>

</body>
</lams:html>