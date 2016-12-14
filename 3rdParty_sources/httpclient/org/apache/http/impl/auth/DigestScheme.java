/*
 * ====================================================================
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.http.impl.auth;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.http.annotation.NotThreadSafe;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.ChallengeState;
import org.apache.http.auth.ContextAwareAuthScheme;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.AUTH;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.auth.params.AuthParams;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.BasicHeaderValueFormatter;
import org.apache.http.message.BufferedHeader;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EncodingUtils;

/**
 * Digest authentication scheme as defined in RFC 2617.
 * Both MD5 (default) and MD5-sess are supported.
 * Currently only qop=auth or no qop is supported. qop=auth-int
 * is unsupported. If auth and auth-int are provided, auth is
 * used.
 * <p>
 * Credential charset is configured via the
 * {@link org.apache.http.auth.params.AuthPNames#CREDENTIAL_CHARSET}
 * parameter of the HTTP request.
 * <p>
 * Since the digest username is included as clear text in the generated
 * Authentication header, the charset of the username must be compatible
 * with the
 * {@link org.apache.http.params.CoreProtocolPNames#HTTP_ELEMENT_CHARSET
 *        http element charset}.
 * <p>
 * The following parameters can be used to customize the behavior of this
 * class:
 * <ul>
 *  <li>{@link org.apache.http.auth.params.AuthPNames#CREDENTIAL_CHARSET}</li>
 * </ul>
 *
 * @since 4.0
 */
@NotThreadSafe
public class DigestScheme extends RFC2617Scheme {

    /**
     * Hexa values used when creating 32 character long digest in HTTP DigestScheme
     * in case of authentication.
     *
     * @see #encode(byte[])
     */
    private static final char[] HEXADECIMAL = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
        'e', 'f'
    };

    /** Whether the digest authentication process is complete */
    private boolean complete;

    private static final int QOP_UNKNOWN = -1;
    private static final int QOP_MISSING = 0;
    private static final int QOP_AUTH_INT = 1;
    private static final int QOP_AUTH = 2;

    private String lastNonce;
    private long nounceCount;
    private String cnonce;
    private String a1;
    private String a2;

    /**
     * Creates an instance of <tt>DigestScheme</tt> with the given challenge
     * state.
     *
     * @since 4.2
     */
    public DigestScheme(final ChallengeState challengeState) {
        super(challengeState);
        this.complete = false;
    }

    public DigestScheme() {
        this(null);
    }

    /**
     * Processes the Digest challenge.
     *
     * @param header the challenge header
     *
     * @throws MalformedChallengeException is thrown if the authentication challenge
     * is malformed
     */
    @Override
    public void processChallenge(
            final Header header) throws MalformedChallengeException {
        super.processChallenge(header);
        this.complete = true;
    }

    /**
     * Tests if the Digest authentication process has been completed.
     *
     * @return <tt>true</tt> if Digest authorization has been processed,
     *   <tt>false</tt> otherwise.
     */
    public boolean isComplete() {
        String s = getParameter("stale");
        if ("true".equalsIgnoreCase(s)) {
            return false;
        } else {
            return this.complete;
        }
    }

    /**
     * Returns textual designation of the digest authentication scheme.
     *
     * @return <code>digest</code>
     */
    public String getSchemeName() {
        return "digest";
    }

    /**
     * Returns <tt>false</tt>. Digest authentication scheme is request based.
     *
     * @return <tt>false</tt>.
     */
    public boolean isConnectionBased() {
        return false;
    }

    public void overrideParamter(final String name, final String value) {
        getParameters().put(name, value);
    }

    /**
     * @deprecated (4.2) Use {@link ContextAwareAuthScheme#authenticate(Credentials, HttpRequest, org.apache.http.protocol.HttpContext)}
     */
    @Deprecated 
    public Header authenticate(
            final Credentials credentials, final HttpRequest request) throws AuthenticationException {
        return authenticate(credentials, request, new BasicHttpContext());
    }

    /**
     * Produces a digest authorization string for the given set of
     * {@link Credentials}, method name and URI.
     *
     * @param credentials A set of credentials to be used for athentication
     * @param request    The request being authenticated
     *
     * @throws org.apache.http.auth.InvalidCredentialsException if authentication credentials
     *         are not valid or not applicable for this authentication scheme
     * @throws AuthenticationException if authorization string cannot
     *   be generated due to an authentication failure
     *
     * @return a digest authorization string
     */
    @Override
    public Header authenticate(
            final Credentials credentials,
            final HttpRequest request,
            final HttpContext context) throws AuthenticationException {

        if (credentials == null) {
            throw new IllegalArgumentException("Credentials may not be null");
        }
        if (request == null) {
            throw new IllegalArgumentException("HTTP request may not be null");
        }
        if (getParameter("realm") == null) {
            throw new AuthenticationException("missing realm in challenge");
        }
        if (getParameter("nonce") == null) {
            throw new AuthenticationException("missing nonce in challenge");
        }
        // Add method name and request-URI to the parameter map
        getParameters().put("methodname", request.getRequestLine().getMethod());
        getParameters().put("uri", request.getRequestLine().getUri());
        String charset = getParameter("charset");
        if (charset == null) {
            charset = AuthParams.getCredentialCharset(request.getParams());
            getParameters().put("charset", charset);
        }
        return createDigestHeader(credentials, request);
    }

    private static MessageDigest createMessageDigest(
            final String digAlg) throws UnsupportedDigestAlgorithmException {
        try {
            return MessageDigest.getInstance(digAlg);
        } catch (Exception e) {
            throw new UnsupportedDigestAlgorithmException(
              "Unsupported algorithm in HTTP Digest authentication: "
               + digAlg);
        }
    }

    /**
     * Creates digest-response header as defined in RFC2617.
     *
     * @param credentials User credentials
     *
     * @return The digest-response as String.
     */
    private Header createDigestHeader(
            final Credentials credentials,
            final HttpRequest request) throws AuthenticationException {
        String uri = getParameter("uri");
        String realm = getParameter("realm");
        String nonce = getParameter("nonce");
        String opaque = getParameter("opaque");
        String method = getParameter("methodname");
        String algorithm = getParameter("algorithm");

        Set<String> qopset = new HashSet<String>(8);
        int qop = QOP_UNKNOWN;
        String qoplist = getParameter("qop");
        if (qoplist != null) {
            StringTokenizer tok = new StringTokenizer(qoplist, ",");
            while (tok.hasMoreTokens()) {
                String variant = tok.nextToken().trim();
                qopset.add(variant.toLowerCase(Locale.US));
            }
            if (request instanceof HttpEntityEnclosingRequest && qopset.contains("auth-int")) {
                qop = QOP_AUTH_INT;
            } else if (qopset.contains("auth")) {
                qop = QOP_AUTH;
            }
        } else {
            qop = QOP_MISSING;
        }

        if (qop == QOP_UNKNOWN) {
            throw new AuthenticationException("None of the qop methods is supported: " + qoplist);
        }

        // If an algorithm is not specified, default to MD5.
        if (algorithm == null) {
            algorithm = "MD5";
        }
        String charset = getParameter("charset");
        if (charset == null) {
            charset = "ISO-8859-1";
        }

        String digAlg = algorithm;
        if (digAlg.equalsIgnoreCase("MD5-sess")) {
            digAlg = "MD5";
        }

        MessageDigest digester;
        try {
            digester = createMessageDigest(digAlg);
        } catch (UnsupportedDigestAlgorithmException ex) {
            throw new AuthenticationException("Unsuppported digest algorithm: " + digAlg);
        }

        String uname = credentials.getUserPrincipal().getName();
        String pwd = credentials.getPassword();

        if (nonce.equals(this.lastNonce)) {
            nounceCount++;
        } else {
            nounceCount = 1;
            cnonce = null;
            lastNonce = nonce;
        }
        StringBuilder sb = new StringBuilder(256);
        Formatter formatter = new Formatter(sb, Locale.US);
        formatter.format("%08x", nounceCount);
        String nc = sb.toString();

        if (cnonce == null) {
            cnonce = createCnonce();
        }

        a1 = null;
        a2 = null;
        // 3.2.2.2: Calculating digest
        if (algorithm.equalsIgnoreCase("MD5-sess")) {
            // H( unq(username-value) ":" unq(realm-value) ":" passwd )
            //      ":" unq(nonce-value)
            //      ":" unq(cnonce-value)

            // calculated one per session
            sb.setLength(0);
            sb.append(uname).append(':').append(realm).append(':').append(pwd);
            String checksum = encode(digester.digest(EncodingUtils.getBytes(sb.toString(), charset)));
            sb.setLength(0);
            sb.append(checksum).append(':').append(nonce).append(':').append(cnonce);
            a1 = sb.toString();
        } else {
            // unq(username-value) ":" unq(realm-value) ":" passwd
            sb.setLength(0);
            sb.append(uname).append(':').append(realm).append(':').append(pwd);
            a1 = sb.toString();
        }

        String hasha1 = encode(digester.digest(EncodingUtils.getBytes(a1, charset)));

        if (qop == QOP_AUTH) {
            // Method ":" digest-uri-value
            a2 = method + ':' + uri;
        } else if (qop == QOP_AUTH_INT) {
            // Method ":" digest-uri-value ":" H(entity-body)
            HttpEntity entity = null;
            if (request instanceof HttpEntityEnclosingRequest) {
                entity = ((HttpEntityEnclosingRequest) request).getEntity();
            }
            if (entity != null && !entity.isRepeatable()) {
                // If the entity is not repeatable, try falling back onto QOP_AUTH
                if (qopset.contains("auth")) {
                    qop = QOP_AUTH;
                    a2 = method + ':' + uri;
                } else {
                    throw new AuthenticationException("Qop auth-int cannot be used with " +
                            "a non-repeatable entity");
                }
            } else {
                HttpEntityDigester entityDigester = new HttpEntityDigester(digester);
                try {
                    if (entity != null) {
                        entity.writeTo(entityDigester);
                    }
                    entityDigester.close();
                } catch (IOException ex) {
                    throw new AuthenticationException("I/O error reading entity content", ex);
                }
                a2 = method + ':' + uri + ':' + encode(entityDigester.getDigest());
            }
        } else {
            a2 = method + ':' + uri;
        }

        String hasha2 = encode(digester.digest(EncodingUtils.getBytes(a2, charset)));

        // 3.2.2.1

        String digestValue;
        if (qop == QOP_MISSING) {
            sb.setLength(0);
            sb.append(hasha1).append(':').append(nonce).append(':').append(hasha2);
            digestValue = sb.toString();
        } else {
            sb.setLength(0);
            sb.append(hasha1).append(':').append(nonce).append(':').append(nc).append(':')
                .append(cnonce).append(':').append(qop == QOP_AUTH_INT ? "auth-int" : "auth")
                .append(':').append(hasha2);
            digestValue = sb.toString();
        }

        String digest = encode(digester.digest(EncodingUtils.getAsciiBytes(digestValue)));

        CharArrayBuffer buffer = new CharArrayBuffer(128);
        if (isProxy()) {
            buffer.append(AUTH.PROXY_AUTH_RESP);
        } else {
            buffer.append(AUTH.WWW_AUTH_RESP);
        }
        buffer.append(": Digest ");

        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>(20);
        params.add(new BasicNameValuePair("username", uname));
        params.add(new BasicNameValuePair("realm", realm));
        params.add(new BasicNameValuePair("nonce", nonce));
        params.add(new BasicNameValuePair("uri", uri));
        params.add(new BasicNameValuePair("response", digest));

        if (qop != QOP_MISSING) {
            params.add(new BasicNameValuePair("qop", qop == QOP_AUTH_INT ? "auth-int" : "auth"));
            params.add(new BasicNameValuePair("nc", nc));
            params.add(new BasicNameValuePair("cnonce", cnonce));
        }
        if (algorithm != null) {
            params.add(new BasicNameValuePair("algorithm", algorithm));
        }
        if (opaque != null) {
            params.add(new BasicNameValuePair("opaque", opaque));
        }

        for (int i = 0; i < params.size(); i++) {
            BasicNameValuePair param = params.get(i);
            if (i > 0) {
                buffer.append(", ");
            }
            boolean noQuotes = "nc".equals(param.getName()) || "qop".equals(param.getName());
            BasicHeaderValueFormatter.DEFAULT.formatNameValuePair(buffer, param, !noQuotes);
        }
        return new BufferedHeader(buffer);
    }

    String getCnonce() {
        return cnonce;
    }

    String getA1() {
        return a1;
    }

    String getA2() {
        return a2;
    }

    /**
     * Encodes the 128 bit (16 bytes) MD5 digest into a 32 characters long
     * <CODE>String</CODE> according to RFC 2617.
     *
     * @param binaryData array containing the digest
     * @return encoded MD5, or <CODE>null</CODE> if encoding failed
     */
    static String encode(byte[] binaryData) {
        int n = binaryData.length;
        char[] buffer = new char[n * 2];
        for (int i = 0; i < n; i++) {
            int low = (binaryData[i] & 0x0f);
            int high = ((binaryData[i] & 0xf0) >> 4);
            buffer[i * 2] = HEXADECIMAL[high];
            buffer[(i * 2) + 1] = HEXADECIMAL[low];
        }

        return new String(buffer);
    }


    /**
     * Creates a random cnonce value based on the current time.
     *
     * @return The cnonce value as String.
     */
    public static String createCnonce() {
        SecureRandom rnd = new SecureRandom();
        byte[] tmp = new byte[8];
        rnd.nextBytes(tmp);
        return encode(tmp);
    }

}
