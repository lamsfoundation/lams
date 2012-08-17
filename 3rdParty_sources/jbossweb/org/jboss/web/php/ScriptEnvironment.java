/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.web.php;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.Globals;
import org.apache.catalina.util.IOTools;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger;


/**
 * Encapsulates the Script Environment and rules to derive
 * that environment from the servlet container and request information.
 *
 * @author Mladen Turk
 * @version $Revision$, $Date$
 * @since 1.0
 */
public class ScriptEnvironment {

    private static Logger log = Logger.getLogger(ScriptEnvironment.class);

    /**
     * The Request attribute key for the client certificate chain.
     */
    private static final String CERTIFICATE_KEY = "javax.servlet.request.X509Certificate";
    private static final String CIPHER_SUITE    = "javax.servlet.request.cipher_suite";
    private static final String SSL_SESSION     = "javax.servlet.request.ssl_session";
    private static final String KEY_SIZE        = "javax.servlet.request.key_size";

    /** context of the enclosing servlet */
    private ServletContext context = null;

    /** full path to the script file */
    private String scriptFullPath = null;

    /** context path of enclosing servlet */
    private String contextPath = null;

    /** servlet URI of the enclosing servlet */
    private String servletPath = null;

    /** pathInfo for the current request */
    private String pathInfo = null;

    /** real file system directory of the enclosing servlet's web app */
    private String webAppRootDir = null;

    /** tempdir for context - used to expand scripts in unexpanded wars */
    private File tempDir = null;

    /** derived script environment */
    private Hashtable env = null;

    /** script's desired working directory */
    private File workingDirectory = null;

    /** script's desired working directory */
    private File scriptFile = null;

    /** query parameters */
    private ArrayList queryParameters = new ArrayList();

    /** whether or not this object is valid or not */
    private boolean valid = false;

    /** object used to ensure multiple threads don't try to expand same file */
    private static Object expandFileLock = new Object();

    /**
     *  The Script search path will start at
     *    webAppRootDir + File.separator + scriptPathPrefix
     *    (or webAppRootDir alone if scriptPathPrefix is
     *    null)
     */
    private String scriptPathPrefix = null;

    /**
     * Resolves core information about the php script.
     *
     * <p>
     * Example URI:
     * <pre> /servlet/scriptGateway/dir1/realScript/pathinfo1 </pre>
     * <ul>
     * <li><code>path</code> = $CATALINA_HOME/mywebapp/dir1/realScript
     * <li><code>scriptName</code> = /servlet/scriptGateway/dir1/realScript
     * <li><code>fullName</code> = /dir1/realScript
     * <li><code>name</code> = realScript
     * </ul>
     * </p>
     * <p>
     * Script search algorithm: search the real path below
     *    &lt;my-webapp-root&gt; and find the first non-directory in
     *    the getPathTranslated("/"), reading/searching from left-to-right.
     *</p>
     *<p>
     *   The Script search path will start at
     *   webAppRootDir + File.separator + scriptPathPrefix
     *   (or webAppRootDir alone if scriptPathPrefix is
     *   null).
     *</p>
     *<p>
     *   scriptPathPrefix is defined by setting
     *   this servlet's scriptPathPrefix init parameter
     *
     *</p>
     *
     * @param pathInfo       String from HttpServletRequest.getPathInfo()
     * @param webAppRootDir  String from context.getRealPath("/")
     * @param contextPath    String as from
     *                       HttpServletRequest.getContextPath()
     * @param servletPath    String as from
     *                       HttpServletRequest.getServletPath()
     * @param scriptPathPrefix  Subdirectory of webAppRootDir below which
     *                       the web app's Scripts may be stored; can be null.
     *                       The Script search path will start at
     *                       webAppRootDir + File.separator + scriptPathPrefix
     *                       (or webAppRootDir alone if scriptPathPrefix is
     *                       null).  scriptPathPrefix is defined by setting
     *                       the servlet's scriptPathPrefix init parameter.
     *
     *
     * @return
     * <ul>
     * <li>
     * <code>path</code> -    full file-system path to valid script file,
     *                        or null if no script file was found
     * <li>
     * <code>scriptName</code> -
     *                        Script variable SCRIPT_NAME; the full URL path
     *                        to valid script file or null if no script was
     *                        found
     * <li>
     * <code>fullName</code> - servlet pathInfo fragment corresponding to
     *                        the script itself, or null if not found
     * <li>
     * <code>name</code> -    simple name (no directories) of the
     *                        script, or null if no script was found
     * </ul>
     *
     */
    protected String[] findScript(String pathInfo, String webAppRootDir,
                                  String contextPath, String servletPath,
                                  String scriptPathPrefix)
    {
        String path = null;
        String name = null;
        String scriptName = null;
        String fullName = null;

        if ((webAppRootDir != null)
            && (webAppRootDir.lastIndexOf(File.separator) ==
                (webAppRootDir.length() - 1))) {
                //strip the trailing "/" from the webAppRootDir
                webAppRootDir =
                webAppRootDir.substring(0, (webAppRootDir.length() - 1));
        }

        if (scriptPathPrefix != null) {
            webAppRootDir = webAppRootDir + File.separator
                + scriptPathPrefix;
        }
        File currentLocation = new File(webAppRootDir);
        StringTokenizer dirWalker = new StringTokenizer(pathInfo, "/");

        while (!currentLocation.isFile() && dirWalker.hasMoreElements()) {
            currentLocation = new File(currentLocation,
                                       (String)dirWalker.nextElement());
        }
        if (!currentLocation.isFile()) {
            return new String[] { null, null, null, null };
        }
        else {

            path = currentLocation.getAbsolutePath();
            name = currentLocation.getName();
            fullName =
            currentLocation.getParent().substring(webAppRootDir.length())
                + File.separator + name;
            // NOTE: Original CGI messes the Win path.
            fullName = fullName.replace(File.separatorChar, '/');

            if (contextPath != null && ! "".equals(contextPath) && ! "/".equals(contextPath)) {
                scriptName = contextPath + fullName;
            }
            else {
                // NOTE: set scriptName to fullName
                scriptName = fullName;
            }

        }

        return new String[] { path, scriptName, fullName, name };
    }

    /**
     * Extracts requested resource from web app archive to context work
     * directory to enable script to be executed.
     */
    protected void expandScript()
    {
        StringBuffer srcPath = new StringBuffer();
        StringBuffer dstPath = new StringBuffer();
        InputStream is = null;

        // paths depend on mapping
        if (scriptPathPrefix == null) {
            srcPath.append(pathInfo);
            is = context.getResourceAsStream(srcPath.toString());
            dstPath.append(tempDir);
            dstPath.append(pathInfo);
        }
        else {
            // essentially same search algorithm as findScript()
            srcPath.append(scriptPathPrefix);
            StringTokenizer dirWalker = new StringTokenizer(pathInfo, "/");
            // start with first element
            while (dirWalker.hasMoreElements() && (is == null)) {
                srcPath.append("/");
                srcPath.append(dirWalker.nextElement());
                is = context.getResourceAsStream(srcPath.toString());
            }
            dstPath.append(tempDir);
            dstPath.append("/");
            dstPath.append(srcPath);
        }

        if (is == null) {
            // didn't find anything, give up now
            return;
        }

        File f = new File(dstPath.toString());
        if (f.exists()) {
            // Don't need to expand if it already exists
            return;
        }

        // create directories
        String dirPath = new String(dstPath.toString().substring( 0,
                                    dstPath.toString().lastIndexOf("/")));
        File dir = new File(dirPath);
        dir.mkdirs();

        try {
            synchronized (expandFileLock) {
                // make sure file doesn't exist
                if (f.exists()) {
                    return;
                }

                // create file
                if (!f.createNewFile()) {
                    return;
                }
                FileOutputStream fos = new FileOutputStream(f);

                // copy data
                IOTools.flow(is, fos);
                is.close();
                fos.close();
            }
        } catch (IOException ioe) {
            // delete in case file is corrupted
            if (f.exists()) {
                f.delete();
            }
        }
    }

    /**
     * Constructs the CGI environment to be supplied to the invoked CGI
     * script; relies heavliy on Servlet API methods and findCGI
     *
     * @param    req request associated with the CGI
     *           invokation
     *
     * @return   true if environment was set OK, false if there
     *           was a problem and no environment was set
     */
    protected boolean setEnvironment(HttpServletRequest req)
        throws IOException
    {

        /*
         * This method is slightly ugly; c'est la vie.
         * "You cannot stop [ugliness], you can only hope to contain [it]"
         * (apologies to Marv Albert regarding MJ)
         */

        Hashtable envp = new Hashtable();

        // Add the shell environment variables (if any)
        // envp.putAll(shellEnv);

        // Add the Script environment variables
        String sPathInfoOrig = null;
        String sPathTranslatedOrig = null;
        String sPathInfo = null;
        String sPathTranslated = null;
        String sFullPath = null;
        String sScriptName = null;
        String sFullName = null;
        String sName = null;
        String[] sScriptNames;


        sPathInfoOrig = this.pathInfo;
        sPathInfoOrig = sPathInfoOrig == null ? "" : sPathInfoOrig;

        sPathTranslatedOrig = req.getPathTranslated();
        sPathTranslatedOrig =
            sPathTranslatedOrig == null ? "" : sPathTranslatedOrig;

        if (webAppRootDir == null ) {
            // The app has not been deployed in exploded form
            webAppRootDir = tempDir.toString();
            expandScript();
        }

        sScriptNames = findScript(sPathInfoOrig,
                                  webAppRootDir,
                                  contextPath,
                                  servletPath,
                                  scriptPathPrefix);

        sFullPath = sScriptNames[0];
        sScriptName = sScriptNames[1];
        sFullName = sScriptNames[2];
        sName = sScriptNames[3];

        if (sFullPath == null
            || sScriptName == null
            || sFullName == null
            || sName == null) {
            log.error("Invalid script names");
            return false;
        }

        envp.put("SERVER_SOFTWARE", "JBossWebServer");
        envp.put("SERVER_NAME", nullsToBlanks(req.getServerName()));
        envp.put("GATEWAY_INTERFACE", "CGI/1.1");
        envp.put("SERVER_PROTOCOL", nullsToBlanks(req.getProtocol()));

        int port = req.getServerPort();
        Integer sPort = (port == 0 ? new Integer(-1) : new Integer(port));
        envp.put("SERVER_PORT", sPort.toString());

        /*
         * Local addres and port
         */
        envp.put("LOCAL_NAME", nullsToBlanks(req.getLocalName()));
        port = req.getLocalPort();
        Integer iPort = (port == 0 ? new Integer(-1) : new Integer(port));
        envp.put("LOCAL_PORT", iPort.toString());
        envp.put("LOCAL_ADDR", nullsToBlanks(req.getLocalAddr()));

        envp.put("REQUEST_METHOD", nullsToBlanks(req.getMethod()));

        /*-
         * PATH_INFO should be determined by using sFullName:
         * 1) Let sFullName not end in a "/" (see method findScript)
         * 2) Let sFullName equal the pathInfo fragment which
         *    corresponds to the actual script.
         * 3) Thus, PATH_INFO = request.getPathInfo().substring(
         *                      sFullName.length())
         *
         * (see method findScript, where the real work is done)
         *
         */
        if (pathInfo == null
            || (pathInfo.substring(sFullName.length()).length() <= 0)) {
            sPathInfo = "";
        }
        else {
            sPathInfo = pathInfo.substring(sFullName.length());
        }
        envp.put("PATH_INFO", sPathInfo);

        /*-
         * PATH_TRANSLATED must be determined after PATH_INFO (and the
         * implied real cgi-script) has been taken into account.
         *
         * The following example demonstrates:
         *
         * servlet info   = /servlet/cgigw/dir1/dir2/cgi1/trans1/trans2
         * cgifullpath    = /servlet/cgigw/dir1/dir2/cgi1
         * path_info      = /trans1/trans2
         * webAppRootDir  = servletContext.getRealPath("/")
         *
         * path_translated = servletContext.getRealPath("/trans1/trans2")
         *
         * That is, PATH_TRANSLATED = webAppRootDir + sPathInfo
         * (unless sPathInfo is null or blank, then the CGI
         * specification dictates that the PATH_TRANSLATED metavariable
         * SHOULD NOT be defined.
         *
         */
        if (sPathInfo != null && !("".equals(sPathInfo))) {
            sPathTranslated = context.getRealPath(sPathInfo);
        }
        else {
            sPathTranslated = null;
        }
        if (sPathTranslated == null || "".equals(sPathTranslated)) {
            // Nothing.
        }
        else {
            envp.put("PATH_TRANSLATED", nullsToBlanks(sPathTranslated));
        }


        envp.put("SCRIPT_NAME", nullsToBlanks(sScriptName));
        envp.put("QUERY_STRING", nullsToBlanks(req.getQueryString()));
        envp.put("REMOTE_HOST", nullsToBlanks(req.getRemoteHost()));
        envp.put("REMOTE_ADDR", nullsToBlanks(req.getRemoteAddr()));
        envp.put("AUTH_TYPE", nullsToBlanks(req.getAuthType()));
        envp.put("REMOTE_USER", nullsToBlanks(req.getRemoteUser()));
        envp.put("REMOTE_IDENT", ""); //not necessary for full compliance
        envp.put("CONTENT_TYPE", nullsToBlanks(req.getContentType()));


        /* Note CGI spec says CONTENT_LENGTH must be NULL ("") or undefined
         * if there is no content, so we cannot put 0 or -1 in as per the
         * Servlet API spec.
         */
        int contentLength = req.getContentLength();
        String sContentLength = (contentLength <= 0 ? "" :
                                 (new Integer(contentLength)).toString());
        envp.put("CONTENT_LENGTH", sContentLength);


        Enumeration headers = req.getHeaderNames();
        String header = null;

        while (headers.hasMoreElements()) {
            header = null;
            header = ((String)headers.nextElement()).toUpperCase();
            //REMIND: rewrite multiple headers as if received as single
            //REMIND: change character set
            //REMIND: I forgot what the previous REMIND means
            if ("AUTHORIZATION".equalsIgnoreCase(header) ||
                "PROXY_AUTHORIZATION".equalsIgnoreCase(header)) {
                //NOOP per CGI specification section 11.2
            }
            else {
                envp.put("HTTP_" + header.replace('-', '_'),
                         req.getHeader(header));
            }
        }

        scriptFile = new File(sFullPath);
        scriptFullPath = scriptFile.getCanonicalPath();
        workingDirectory = new File(scriptFullPath.substring(0,
                                scriptFullPath.lastIndexOf(File.separator)));

        envp.put("SCRIPT_FILENAME", scriptFullPath);

        envp.put("CONTEXT_PATH", nullsToBlanks(contextPath));

        String self = "";
        if (contextPath != null && ! "".equals(contextPath) && ! "/".equals(contextPath)) {
            self = contextPath;
        }
        if (servletPath != null && ! "".equals(servletPath) && ! "/".equals(servletPath)) {
            self = self.concat(servletPath);
        }
        
        envp.put("PHP_SELF", nullsToBlanks(self));

        if (req.isSecure()) {
            envp.put("HTTPS", "ON");
            envp.put("SSL_CIPHER", req.getAttribute(CIPHER_SUITE));
            envp.put("SSL_SESSION_ID", req.getAttribute(SSL_SESSION));
            envp.put("SSL_CIPHER_USEKEYSIZE", String.valueOf(req.getAttribute(KEY_SIZE)));
            X509Certificate[] certs =
                (X509Certificate[])req.getAttribute(CERTIFICATE_KEY);
            if (certs != null) {
                // Well use the first, normaly the client certificate.
                envp.put("SSL_SERVER_V_START", certs[0].getNotAfter().toString());
                envp.put("SSL_SERVER_V_END",  certs[0].getNotBefore().toString());
                
                envp.put("SSL_CLIENT_A_KEY",  certs[0].getSigAlgName());
                
                // Oops getEncoded gives a DER not PEM encoded ... envp.put("SSL_CLIENT_CERT",  certs[0].getEncoded());

                envp.put("SSL_SERVER_M_SERIAL", certs[0].getSerialNumber().toString());
                envp.put("SSL_SERVER_M_VERSION", String.valueOf(certs[0].getVersion()));

                // Subject
                envp.put("SSL_CLIENT_S_DN", certs[0].getSubjectX500Principal().getName());
                // To fill the elements C,ST... Email
                String pr = certs[0].getSubjectX500Principal().getName();
                String prs[] = pr.split(", ");
                for (int c = 0; c < prs.length; c++) {
                    String pprs[] = prs[c].split("=");
                    envp.put("SSL_CLIENT_S_DN_" + pprs[0], pprs[1]);
                }

                // Issuer
                envp.put("SSL_CLIENT_I_DN", certs[0].getIssuerX500Principal().getName());
                // To fill the elements C,ST... Email Still to TODO.
                pr = certs[0].getSubjectX500Principal().getName();
                prs = pr.split(", ");
                for (int c = 0; c < prs.length; c++) {
                    String pprs[] = prs[c].split("=");
                    envp.put("SSL_CLIENT_I_DN_" + pprs[0], pprs[1]);
                }


                // envp.put("CERT_ISSUER",
                //         nullsToBlanks(certs[c].getIssuerX500Principal().getName()));
            }
        }

        this.env = envp;
        return true;
    }


    /**
     * Creates a CGIEnvironment and derives the necessary environment,
     * query parameters, working directory, cgi command, etc.
     *
     * @param  req       HttpServletRequest for information provided by
     *                   the Servlet API
     * @param  context   ServletContext for information provided by the
     *                   Servlet API
     *
     */
    public ScriptEnvironment(HttpServletRequest req,
                             ServletContext context,
                             String scriptPathPrefix)
        throws IOException
    {
        this.scriptPathPrefix = scriptPathPrefix;
        this.context = context;
        this.webAppRootDir = context.getRealPath("/");
        this.tempDir = (File)context.getAttribute(Globals.WORK_DIR_ATTR);


        if (req.getAttribute(Globals.INCLUDE_CONTEXT_PATH_ATTR) != null) {
           // Include
           this.contextPath = (String) req.getAttribute(Globals.INCLUDE_CONTEXT_PATH_ATTR);
           this.servletPath = (String) req.getAttribute(Globals.INCLUDE_SERVLET_PATH_ATTR);
           this.pathInfo = (String) req.getAttribute(Globals.INCLUDE_PATH_INFO_ATTR);
        }
        else {
           // Direct call
           this.contextPath = req.getContextPath();
           this.servletPath = req.getServletPath();
           this.pathInfo = req.getPathInfo();
        }

        // If getPathInfo() returns null, must be using extension mapping
        // In this case, pathInfo should be same as servletPath
        if (this.pathInfo == null) {
            this.pathInfo = this.servletPath;
        }
        this.valid = setEnvironment(req);
    }


    /**
     * Gets derived script full path
     *
     * @return  full script path
     *
     */
    public String getFullPath()
    {
        return scriptFullPath;
    }

    /**
     * Gets derived Script file
     *
     * @return  Script file
     *
     */
    public File getScriptFile()
    {
        return scriptFile;
    }

    /**
     * Gets derived Script working directory
     *
     * @return  working directory
     *
     */
    public File getWorkingDirectory()
    {
        return workingDirectory;
    }

    /**
     * Gets derived Script environment
     *
     * @return   Script environment
     *
     */
    public Hashtable getEnvironment()
    {
        return env;
    }

    /**
     * Gets derived Script query parameters
     *
     * @return   Script query parameters
     *
     */
    public ArrayList getParameters()
    {
        return queryParameters;
    }

    /**
     * Gets validity status
     *
     * @return   true if this environment is valid, false
     *           otherwise
     *
     */
    public boolean isValid()
    {
        return valid;
    }

    /**
     * Converts null strings to blank strings ("")
     *
     * @param    s string to be converted if necessary
     * @return   a non-null string, either the original or the empty string
     *           ("") if the original was <code>null</code>
     */
    protected String nullsToBlanks(String s)
    {
        return nullsToString(s, "");
    }

    /**
     * Converts null strings to another string
     *
     * @param    couldBeNull string to be converted if necessary
     * @param    subForNulls string to return instead of a null string
     * @return   a non-null string, either the original or the substitute
     *           string if the original was <code>null</code>
     */
    protected String nullsToString(String couldBeNull,
                                   String subForNulls)
    {
        return (couldBeNull == null ? subForNulls : couldBeNull);
    }

    /**
     * Converts blank strings to another string
     *
     * @param    couldBeBlank string to be converted if necessary
     * @param    subForBlanks string to return instead of a blank string
     * @return   a non-null string, either the original or the substitute
     *           string if the original was <code>null</code> or empty ("")
     */
    protected String blanksToString(String couldBeBlank,
                                    String subForBlanks)
    {
        return (("".equals(couldBeBlank) || couldBeBlank == null)
                ? subForBlanks
                : couldBeBlank);
    }

    /**
     * Converts Environment Hastable to String array
     *
     * @return   Srring array containing name value pairs.
     * @exception  NullPointerException   if a hash key has a null value
     */
    public String[] getEnvironmentArray()
        throws NullPointerException
    {
        return hashToStringArray(env);
    }

    /**
     * Converts a Hashtable to a String array by converting each
     * key/value pair in the Hashtable to two consecutive Strings
     *
     * @param  h   Hashtable to convert
     * @return     converted string array
     * @exception  NullPointerException   if a hash key has a null value
     */
    public static String[] hashToStringArray(Hashtable h)
        throws NullPointerException
    {
        Vector v = new Vector();
        Enumeration e = h.keys();
        while (e.hasMoreElements()) {
            String k = e.nextElement().toString();
            v.add(k);
            v.add(h.get(k));
        }
        String[] strArr = new String[v.size()];
        v.copyInto(strArr);
        return strArr;
    }

}
