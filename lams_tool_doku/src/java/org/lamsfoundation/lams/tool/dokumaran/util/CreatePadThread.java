package org.lamsfoundation.lams.tool.dokumaran.util;

import org.lamsfoundation.lams.tool.dokumaran.DokumaranConstants;
import org.lamsfoundation.lams.tool.dokumaran.service.DokumaranConfigurationException;
import org.lamsfoundation.lams.tool.dokumaran.service.IDokumaranService;

import net.gjerull.etherpad.client.EPLiteClient;
import net.gjerull.etherpad.client.EPLiteException;

public class CreatePadThread implements Runnable {

    private IDokumaranService service;
    private Long toolSessionId;
    private String dokumaranInstructions;

    public CreatePadThread(IDokumaranService service, Long toolSessionId,
	    String dokumaranInstructions) {
	this.service = service;
	this.toolSessionId = toolSessionId;
	this.dokumaranInstructions = dokumaranInstructions;
    }

    @Override
    public void run() {
	EPLiteClient client;
	try {
	    client = service.initializeEPLiteClient();
	} catch (DokumaranConfigurationException e1) {
	    throw new RuntimeException(e1);
	}
	
	String padId = DokumaranConstants.PAD_ID_PREFIX + toolSessionId;
	try {
	    client.createPad(padId);
	} catch (EPLiteException e) {
	    // allow recreating existing pads
	    if (!"padID does already exist".equals(e.getMessage())) {
		throw e;
	    }
	}
	
	//set initial content
	String etherpadHtml = "<html><body>" + dokumaranInstructions.replaceAll("[\n\r\f]", "").replaceAll("&nbsp;", "")
		+ "</body></html>";
	client.setHTML(padId, etherpadHtml);
	
	//gets read-only id
	String etherpadReadOnlyId = (String) client.getReadOnlyID(padId).get("readOnlyID");
	service.setEtherpadReadOnlyId(toolSessionId, etherpadReadOnlyId);
    }
}
