package org.lamsfoundation.lams.util;

import org.springframework.context.MessageSource;


public interface ILoadedMessageSourceService {

	/** Get the MessageSource for the specified message file.
	 * 
	 * @param messageFilename Mandatory
	 * @return Null if messageFilename is null, otherwise a MessageSource (even if the bundle cannot be found) 
	 */
	public abstract MessageSource getMessageService(String messageFilename);

}