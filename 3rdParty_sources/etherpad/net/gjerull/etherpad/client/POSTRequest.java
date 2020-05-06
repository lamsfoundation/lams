package net.gjerull.etherpad.client;

import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * A class for easily executing an HTTP POST request.<br />
 * <br />
 * Example:<br />
 * <br />
 * <code>
 * Request req = new POSTRequest(url_object);<br />
 * String resp = req.send();<br />
 * </code>
 */
public class POSTRequest implements Request {
    private final URL url;
    private final String body;

    /**
     * Instantiates a new POSTRequest.
     *
     * @param url the URL object
     * @param body url-encoded (application/x-www-form-urlencoded) request body
     */
    public POSTRequest(URL url, String body) {
        this.url = url;
        this.body = body;
    }

    /**
     * Sends the request and returns the response.
     * 
     * @return String
     */
    public String send() throws Exception {
        URLConnection con = this.url.openConnection();
        con.setDoOutput(true);

        OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
        out.write(this.body);
        out.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder response = new StringBuilder();
        String buffer;
        while ((buffer = in.readLine()) != null) {
            response.append(buffer);
        }
        in.close();
        return response.toString();
    }
}
