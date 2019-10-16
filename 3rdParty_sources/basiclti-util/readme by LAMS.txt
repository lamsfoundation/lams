The following bits were modified by LAMS.

* LDEV-4221: Some of the integrations complain about outcome call request format (when LAMS is used as LTI Tool Provider)
Which required changes to be done for org.imsglobal.pox.IMSPOXRequest.java

* LDEV-4589: Added a few valid parameter names

* LDEV-4883 Fix HttpClient warning “Invalid expires attribute”
	org.imsglobal.pox.IMSPOXRequest.java is customized as a result.