package net.gjerull.etherpad.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * A class for easily executing an HTTP GET request.<br />
 * <br />
 * Example:<br />
 * <br />
 * <code>
 * Request req = new GETRequest(url_object);<br />
 * String resp = req.send();<br />
 * </code>
 */
public class GETRequest implements Request {
    private final URL url;

    /**
     * Instantiates a new GETRequest.
     * 
     * @param url the URL object
     */
    public GETRequest(URL url) {
        this.url = url;
    }

    /**
     * Sends the request and returns the response.
     * 
     * @return String
     */
    public String send() throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String response = "";
        String buffer;
        while ((buffer = in.readLine()) != null) {
            response += buffer;
        }
        in.close();
        return response;
    }
}
