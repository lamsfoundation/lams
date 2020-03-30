package org.lamsfoundation.lams.etherpad.service;

import java.util.Map;

import javax.servlet.http.Cookie;

import org.lamsfoundation.lams.etherpad.EtherpadException;

import net.gjerull.etherpad.client.EPLiteClient;

public interface IEtherpadService {

    String DEFAULT_PAD_NAME = "LAMS-pad";

    String PREFIX_REGULAR_GROUP = "LAMS-group-";

    /**
     * Creates EPLiteClient that will make calls to Etherpad server. Throws DokumaranConfigurationException
     *
     * @throws DokumaranConfigurationException
     *             if the etherpad is not configured appropriately (either server URL or API key).
     */
    EPLiteClient getClient() throws EtherpadException;

    Map<String, Object> createPad(String groupIdentifier) throws EtherpadException;

    /**
     * Constructs cookie to be stored at a clientside browser.
     */
    Cookie createCookie(String etherpadSessionIds) throws EtherpadException;

    /**
     * Returns valid Etherpad session. Returns existing one if finds such one and creates the new one otherwise
     */
    String getExistingSessionID(String authorId, String etherpadGroupId, Map<String, Object> etherpadSessions)
	    throws EtherpadException;

    String createAuthor(Integer userId, String userName) throws EtherpadException;
}