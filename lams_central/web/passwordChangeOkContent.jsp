<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<lams:css/>
</lams:head>

<body>
	<div style="clear: both"></div>

	<div class="container">
		<div class="row vertical-center-row">
			<div class="col-xs-12 col-sm-8 col-sm-offset-2 col-md-6 col-md-offset-3">
				<div class="panel voffset20">
					<div class="panel-body">
					<div class="col-xs-12 text-center"  id="msgPasswordChanged">
						<fmt:message key="msg.password.changed"/>
					</div>
					
					<div class="col-xs-12 text-center voffset10">
						<a class="btn btn-sm btn-default" id="saveButton"
								href="<lams:LAMSURL/>${empty redirectURL ? 'index.do' : redirectURL}" role="button">
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