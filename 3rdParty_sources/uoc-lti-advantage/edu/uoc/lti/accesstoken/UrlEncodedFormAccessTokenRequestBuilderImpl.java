package edu.uoc.lti.accesstoken;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

public class UrlEncodedFormAccessTokenRequestBuilderImpl implements AccessTokenRequestBuilder {

  @Override
  public String build(final AccessTokenRequest request) throws IOException {
    final Method[] methods = request.getClass().getDeclaredMethods();
    
		return Arrays.stream(methods)
						.map(method -> {
							try {
                if (!method.getName().startsWith("get")) {
                  return null;
                }                
								final Object value = method.invoke(request, null);
								String valueAsString = null;
								if (method.getReturnType().getSimpleName().equals("String")) {
									valueAsString = (String) value;
								}
								else if (method.getReturnType().isPrimitive()) {
									valueAsString = String.valueOf(value);
								}
								return valueAsString != null && valueAsString.length() > 0
												? method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4) + "=" + URLEncoder.encode(valueAsString, StandardCharsets.UTF_8.toString())
												: null;
							} catch (IllegalArgumentException | InvocationTargetException | UnsupportedEncodingException
              | IllegalAccessException e) {
								e.printStackTrace();
              }
							return null;
						})
						.filter(value -> value != null)
						.collect(Collectors.joining("&"));
  }

  @Override
  public String getContentType() {
    return "application/x-www-form-urlencoded";
  }

}