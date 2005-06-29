package org.lamsfoundation.lams.tool.forum.util;

import org.lamsfoundation.lams.tool.forum.persistence.Message;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.util.logging.Logger;
import java.util.List;
import java.util.Set;
import java.util.Iterator;
import java.util.Map;
import java.text.SimpleDateFormat;

/**
 * Created by IntelliJ IDEA.
 * User: conradb
 * Date: 24/06/2005
 * Time: 15:33:51
 * To change this template use File | Settings | File Templates.
 */
public class MessageMapTag extends TagSupport {

    private Logger log = Logger.getLogger(MessageMapTag.class.getName());
    private Set replies = null;
    private String ordered = "ul";
    private String path;
    private String topicId;

    private SimpleDateFormat formatter = new SimpleDateFormat("dd:MM:yy hh:mm a");

    public MessageMapTag() {

    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public void setReplies(Set replies) {
        this.replies = replies;
    }

    public void setOrdered(String ordered) {
        if (ordered.equals("true")) {
            this.ordered = "ol";
        }
    }

    /**
     * Defer our checking until the end of this tag is encountered.
     *
     * @exception javax.servlet.jsp.JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {
        return (SKIP_BODY);
    }

    /**
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {
        try {

            if (replies != null) {
            String output = "";
                Iterator it = replies.iterator();

                /*
                while (it.hasNext()) {
                    output = output + "<" + ordered + ">" + getThreadView((Message) it.next())
                    + "\n</" + ordered +">";
                }
                output =  output + "<br/><br/>";
                //output = output + " <table style=\"background-color:#ffffff;\" cellspacing=\"3\" cellpadding=\"3\" width=\"100%\">";
                it = replies.iterator();
                */

                while (it.hasNext()) {
                    String indentPrefix = "<table style=\"background-color:#ffffff;\" cellspacing=\"1\" cellpadding=\"1\" width=\"100%\">\n";
                    String indentPostfix = "</table>\n<br/><br/>\n";
                    int level = 0;
                    output = output + this.getIndentedView(level, indentPrefix, indentPostfix, (Message) it.next());
                }
                JspWriter writer = pageContext.getOut();
                writer.print(output);
            }
            return EVAL_PAGE;
        } catch (Exception e) {
            throw new JspException(e.getMessage());
        }
    }

    /**
     * Release any acquired resources.
     */
    public void release() {
        super.release();
        replies = null;
        ordered = "ul";
    }

    private StringBuffer getThreadView(Message reply) throws Exception {
        StringBuffer buffer = new StringBuffer();

        buffer.append("\n<li><a href=\"");
        String id = "#" + reply.getId();
        buffer.append(id + "\">");
        buffer.append(reply.getBody());
        buffer.append("</a>");
        buffer.append(" <i> - </i> ");
        buffer.append(formatter.format(reply.getCreated()));
        Set subReplies = reply.getReplies();

        if (subReplies != null) {
            buffer.append("\n<");
            buffer.append(ordered);
            buffer.append(">");
            Iterator it = subReplies.iterator();
            while (it.hasNext()) {
                buffer.append(getThreadView((Message) it.next()));
            }
            buffer.append("\n</");
            buffer.append(ordered);
            buffer.append(">");
        }
        buffer.append("</li>");
        return buffer;
    }

    private StringBuffer getIndentedView(int level, String indentPrefix, String indentPostfix, Message reply) throws Exception {
        StringBuffer buffer = new StringBuffer();
        buffer.append(indentPrefix);
        buffer.append("<tr>\n");
        buffer.append("<td>\n");
        if (reply.getCreatedBy() == null) {
            buffer.append("<b>Anonymous</b>\n");
        } else {
          buffer.append("<b>" + reply.getCreatedBy() + "</b>\n");
        }
        buffer.append("</td>\n");
        buffer.append("<td>\n");
        buffer.append("<i>" + formatter.format(reply.getCreated()) + "</i>\n");
        buffer.append("</td>\n");
        buffer.append("<td>\n");
        buffer.append("<a href=\"/forum/learning/message/post.do?topicId=");
        buffer.append(topicId);
        buffer.append("&parentId=");
        buffer.append(reply.getId() + "\">");
        buffer.append("<b>Reply</b>");
        buffer.append("</a>\n");
        buffer.append("</td>\n");
        buffer.append("<tr>\n");
        buffer.append("<td colspan=\"3\">\n");
        buffer.append(reply.getBody());
        buffer.append("</td>\n");
        buffer.append("</tr>\n");
        buffer.append(indentPostfix);
        Iterator it = reply.getReplies().iterator();
        level = level + 2;
        while (it.hasNext()) {
            StringBuffer preFix = new StringBuffer();
            preFix.append("<table cellspacing=\"1\" cellpadding=\"1\" width=\"100%\">\n");
            preFix.append("<tr>\n");
            preFix.append("<td colspan=\"" + level + "\"\\>\n");
            preFix.append("<td>");
            preFix.append("<table style=\"background-color:#ffffff;\" cellspacing=\"1\" cellpadding=\"1\" width=\"100%\">\n");

            StringBuffer postFix = new StringBuffer();
            postFix.append("</table>\n");
            postFix.append("</td>\n");
            preFix.append("</tr>\n");
            postFix.append("</table>\n");
            postFix.append("<br/><br/>\n");
            buffer.append(this.getIndentedView(level, preFix.toString(), postFix.toString(), (Message) it.next()));
        }
        return buffer;
    }
}
