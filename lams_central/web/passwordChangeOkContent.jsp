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
			<div class="col-12 col-md-8 offset-md-2 col-lg-6 offset-lg-3">
				<div class="panel voffset20">
					<div class="panel-body">
					<div class="col-12 text-center">
						<fmt:message key="msg.password.changed"/>
					</div>
					
					<div class="col-12 text-center voffset10">
						<a class="btn btn-sm btn-secondary"
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