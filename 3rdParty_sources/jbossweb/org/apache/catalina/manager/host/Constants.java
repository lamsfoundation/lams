/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.apache.catalina.manager.host;


public class Constants {

    public static final String Package = "org.apache.catalina.manager.host";

    public static final String HTML_HEADER_SECTION =
        "<html>\n" +
        "<head>\n" +
        "<link href=\"{0}/jbossweb.css\" rel=\"stylesheet\" type=\"text/css\" />\n";

    public static final String BODY_HEADER_SECTION =
        "<title>{1}</title>\n" +
        "</head>\n" +
        "\n" +
        "<body>\n" +
        "\n" +
        "<div class=\"wrapper\">\n" +
        "  <div class=\"header\">\n" +
        "    <div class=\"floatleft\"><a href=\"list\"><img src=\"{0}/images/hdr_hdrtitle.gif\" border=\"0\"></a></div>\n" +
        "    <div class=\"floatright\"><a href=\"http://www.jboss.com/\"><img src=\"{0}/images/hdr_jbosslogo.gif\" alt=\"JBoss, a division of Red Hat\" border=\"0\"></a><a href=\"http://www.jboss.org\"><img src=\"{0}/images/hdr_jbossorglogo.gif\" alt=\"JBoss.org - Community driven.\" border=\"0\" /></a></div>\n" +
        "  </div>\n" +
        "    <div class=\"container\">\n" +
        "\n";

    public static final String MESSAGE_SECTION =
        "<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\">\n" +
        " <tr>\n" +
        "  <td class=\"row-left\" width=\"10%\">" +
        "<small><strong>{0}</strong></small>&nbsp;</td>\n" +
        "  <td class=\"row-left\"><pre>{1}</pre></td>\n" +
        " </tr>\n" +
        "</table>\n" +
        "<br>\n" +
        "\n";

    public static final String MANAGER_SECTION =
        "<div class=\"leftcol\"><dl>\n" +
        "  <dt>Manager</dt>" +
        "    <dd><a href=\"{1}\">{2}</a></dd>" +
        "    <dd><a href=\"{3}\">{4}</a></dd>" +
        "    <dd><a href=\"{5}\">{6}</a></dd>" +
        "    <dd><a href=\"{7}\">{8}</a></dd>" +
        "</dl></div>\n" +
        "<div class=\"maincol\">\n";

    public static final String SERVER_HEADER_SECTION =
        "<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\">\n" +
        "<tr>\n" +
        " <td colspan=\"6\" class=\"title\">{0}</td>\n" +
        "</tr>\n" +
        "<tr>\n" +
        " <td class=\"header-center\"><small>{1}</small></td>\n" +
        " <td class=\"header-center\"><small>{2}</small></td>\n" +
        " <td class=\"header-center\"><small>{3}</small></td>\n" +
        " <td class=\"header-center\"><small>{4}</small></td>\n" +
        " <td class=\"header-center\"><small>{5}</small></td>\n" +
        " <td class=\"header-center\"><small>{6}</small></td>\n" +
        "</tr>\n";

    public static final String SERVER_ROW_SECTION =
        "<tr>\n" +
        " <td class=\"row-center\"><small>{0}</small></td>\n" +
        " <td class=\"row-center\"><small>{1}</small></td>\n" +
        " <td class=\"row-center\"><small>{2}</small></td>\n" +
        " <td class=\"row-center\"><small>{3}</small></td>\n" +
        " <td class=\"row-center\"><small>{4}</small></td>\n" +
        " <td class=\"row-center\"><small>{5}</small></td>\n" +
        "</tr>\n" +
        "</table>\n" +
        "<br>\n" +
        "\n";

    public static final String HTML_TAIL_SECTION =
        "  </div>\n" +
        "  </div>\n" +
        "  <div class=\"footer\">&copy; 2008 Red Hat Middleware, LLC. All Rights Reserved. </div>\n" +
        "</div></body></html>";

    public static final String CHARSET="utf-8";

    // FIXME need we this?
    public static final String XML_DECLARATION =
        "<?xml version=\"1.0\" encoding=\""+CHARSET+"\"?>";
		
    public static final String XML_STYLE =
        "<?xml-stylesheet type=\"text/xsl\" href=\"/manager/xform.xsl\" ?>";

}

