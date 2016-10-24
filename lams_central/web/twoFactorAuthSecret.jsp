<%@ taglib uri="tags-fmt" prefix="fmt" %>

<p class=body>
	<fmt:message key="label.your.new.shared.secret">
		<fmt:param>${sharedSecret}</fmt:param>
	</fmt:message>
</p>
<p>
	<img alt="" src="${QRCode}">
</p>
<p class=body>	
	<input type="submit" class="button" value="Ok"
		onClick="javascript:document.location='index.do?state=active&method=profile';" />
</p>
