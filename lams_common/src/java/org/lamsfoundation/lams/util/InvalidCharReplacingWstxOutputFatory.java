package org.lamsfoundation.lams.util;

import com.ctc.wstx.stax.WstxOutputFactory;

/**
 * It replaces invalid characters with a space rather than throwing an error
 * when exporting an object into XML.
 */
public class InvalidCharReplacingWstxOutputFatory extends WstxOutputFactory {

    public InvalidCharReplacingWstxOutputFatory() {
	setProperty(com.ctc.wstx.api.WstxOutputProperties.P_OUTPUT_INVALID_CHAR_HANDLER,
		new com.ctc.wstx.api.InvalidCharHandler.ReplacingHandler(' '));
    }
}