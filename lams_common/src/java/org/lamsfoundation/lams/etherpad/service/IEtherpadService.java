package org.lamsfoundation.lams.etherpad.service;

import java.util.Map;

import javax.servlet.http.Cookie;

import org.lamsfoundation.lams.etherpad.EtherpadException;

import net.gjerull.etherpad.client.EPLiteClient;

public interface IEtherpadService {

    String DEFAULT_PAD_NAME = "LAMS-pad";

    String PREFIX_REGULAR_GROUP = "LAMS-group-";

    /**
     * Creates EPLiteClient that will make calls to Etherpad server
     *
     * @throws EtherpadException
     *             if the Etherpad is not configured appropriately (either server URL or API key).
     */
    EPLiteClient getClient() throws EtherpadException;

    /**
     * Using API client creates/fetches Etherpad Pad.
     *
     * @param groupIdentifier
     *            LAMS-specific group ID (just any identifier); do not confuse with Etherpad groupId, readOnlyId or
     *            padId
     * @param content
     *            initial content of Etherpad; ignored if null or Etherpad already exists
     */
    Map<String, Object> createPad(String groupIdentifier, String content) throws EtherpadException;

    /**
     * Constructs cookie to be stored at a client side browser.
     */
    Cookie createCookie(String etherpadSessionIds) throws EtherpadException;

    /**
     * Constructs cookie to be stored at a client side browser.
     */
    Cookie createCookie(String authorId, String etherpadGroupId, boolean includeAllGroup) throws EtherpadException;

    /**
     * Returns valid Etherpad session. Returns existing one if finds such one and creates the new one otherwise
     */
    String getExistingSessionID(String authorId, String etherpadGroupId, Map<String, Object> etherpadSessions)
	    throws EtherpadException;

    /**
     * Returns valid Etherpad session. Returns existing one if finds such one and creates the new one otherwise
     */
    String getExistingSessionID(String authorId, String etherpadGroupId, Map<String, Object> etherpadSessions,
	    boolean includeAllGroups) throws EtherpadException;

    String createAuthor(Integer userId, String userName) throws EtherpadException;
}