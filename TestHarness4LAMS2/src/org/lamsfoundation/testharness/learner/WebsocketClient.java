package org.lamsfoundation.testharness.learner;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.ClientEndpointConfig.Builder;
import javax.websocket.ClientEndpointConfig.Configurator;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

/**
 * Simple websocket client for TestHarness purposes.
 * Uses Undertow libraries.
 * 
 * @author Marcin Cieslak
 */
public class WebsocketClient {
    private final WebsocketEndpoint websocketEndpoint;

    public WebsocketClient(String uri, final String sessionID, MessageHandler.Whole<String> messageHandler)
	    throws IOException {

	// add session ID so the request gets through LAMS security
	Builder configBuilder = ClientEndpointConfig.Builder.create();
	configBuilder.configurator(new Configurator() {
	    @Override
	    public void beforeRequest(Map<String, List<String>> headers) {
		headers.put("Cookie", Arrays.asList("JSESSIONID=" + sessionID));
	    }
	});
	ClientEndpointConfig clientConfig = configBuilder.build();
	this.websocketEndpoint = new WebsocketEndpoint(messageHandler);
	WebSocketContainer container = ContainerProvider.getWebSocketContainer();
	try {
	    container.connectToServer(websocketEndpoint, clientConfig, new URI(uri));
	} catch (DeploymentException | URISyntaxException e) {
	    throw new IOException("Error while connecting to websocket server", e);
	}
    }

    public boolean sendMessage(String message) throws IOException {
	if (websocketEndpoint.session == null) {
	    return false;
	}
	websocketEndpoint.session.getBasicRemote().sendText(message);
	return true;
    }

    public void close() throws IOException {
	if (websocketEndpoint.session != null) {
	    websocketEndpoint.session.close();
	}
    }
}

class WebsocketEndpoint extends Endpoint {
    Session session = null;
    private final MessageHandler.Whole<String> messageHandler;

    WebsocketEndpoint(MessageHandler.Whole<String> messageHandler) {
	this.messageHandler = messageHandler;
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
	this.session = session;
	if (messageHandler != null) {
	    session.addMessageHandler(messageHandler);
	}
    }
}