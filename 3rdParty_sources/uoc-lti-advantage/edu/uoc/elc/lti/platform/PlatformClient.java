package edu.uoc.elc.lti.platform;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author xaracil@uoc.edu
 */
public class PlatformClient {
	private final static String CHARSET = StandardCharsets.UTF_8.toString();

	public <T> T post(URL url, String body, String contentType, Class<T> type) throws IOException {
		final HttpURLConnection connection = createConnection(url, contentType);
		sendData(connection, body);
		String response = getResponse(connection);
		return formatResponse(response, type);
	}

	private HttpURLConnection createConnection(URL url, String contentType) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection = initConnection(connection);
		connection = setContentType(connection, contentType);
		return  connection;
	}

	private HttpURLConnection initConnection(HttpURLConnection connection) throws IOException {
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setRequestProperty("Accept-Charset", CHARSET);
		return connection;
	}

	private HttpURLConnection setContentType(HttpURLConnection connection, String contentType) {
		if (contentType != null) {
			connection.setRequestProperty("Content-Type", contentType);
		}
		return connection;
	}

	private void sendData(HttpURLConnection connection, String body) throws IOException {
		try (OutputStream output = connection.getOutputStream()) {
			output.write(body.getBytes(CHARSET));
		}
	}

	private String getResponse(HttpURLConnection connection) throws IOException {
		return IOUtils.toString(connection.getInputStream(), CHARSET);
	}

	private <T> T formatResponse(String response, Class<T> type) throws IOException {
		if (type.getSimpleName().equals("String")) {
			return (T) response;
		}

		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(response, type);
	}
}
