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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * PHP SAPI interface.
 *
 * @author Mladen Turk
 * @version $Revision$, $Date$
 * @since 1.0
 */
public final class SAPI
{

    public static int write(HttpServletResponse res,
                            byte[] buf, int len)
    {
        try {
            res.getOutputStream().write(buf, 0, len);
            return len;
        } catch (IOException e) {
            return -1;
        }
    }

    public static int read(HttpServletRequest req,
                           byte[] buf, int len)
    {
        try {
            return req.getInputStream().read(buf, 0, len);
        } catch (IOException e) {
            return -1;
        }
    }

    public static void log(Handler h, String msg)
    {
        h.log("php: " + msg);
    }

    public static int flush(HttpServletResponse res)
    {
        try {
            res.getOutputStream().flush();
            return 0;
        } catch (IOException e) {
            return -1;
        }
    }

    public static void header(boolean set,
                              HttpServletResponse res,
                              String name, String value)
    {
        if (name.equalsIgnoreCase("Content-type")) {
            res.setContentType(value);
        }
        else if (name.equalsIgnoreCase("Location")) {
            try {
                res.sendRedirect(value);
            } catch (IOException e) {
                // Nothing.
            }
        }
        else {
            if (set)
                res.setHeader(name, value);
            else
                res.addHeader(name, value);
        }
    }

    public static void status(HttpServletResponse res,
                              int sc)
    {
        res.setStatus(sc);
    }

    public static String[] env(ScriptEnvironment e)
    {
        return e.getEnvironmentArray();
    }

    public static String cookies(ScriptEnvironment e)
    {
        return (String)e.getEnvironment().get("HTTP_COOKIE");
    }


}
