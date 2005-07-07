package org.lamsfoundation.lams.tool.forum.util;

import org.lamsfoundation.lams.tool.forum.persistence.Message;
import org.lamsfoundation.lams.tool.forum.permissions.Permission;

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
    private String path="";
    private String mode;
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

    public void setMode(String mode) {
        this.mode = mode;
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

                while (it.hasNext()) {
                    String indentPrefix = "<table class=\"forumTable\">\n";
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
    }

    private StringBuffer getIndentedView(int level, String indentPrefix, String indentPostfix, Message reply) throws Exception {
        StringBuffer buffer = new StringBuffer();
        buffer.append(indentPrefix);
        buffer.append("<tr>\n");

        //Creadted By Link
        buffer.append("<td>\n");
        if (reply.getCreatedBy() == null) {
            buffer.append("<b>Anonymous</b>\n");
        } else {
          buffer.append("<b>" + reply.getCreatedBy() + "</b>\n");
        }
        buffer.append("</td>\n");

        //Created Date
        buffer.append("<td>\n");
        buffer.append("<i>" + formatter.format(reply.getCreated()) + "</i>\n");
        buffer.append("</td>\n");

        //Edit Link
        if (Permission.MODERATE.equals(mode)) {
            buffer.append("<td>\n");
            //buffer.append("<a href=\"/forum/learning/message/getMessage.do?topicId=");
            buffer.append("<a href=\"" + path + "?topicId=");
            buffer.append(topicId);
            buffer.append("&messageId=");
            buffer.append(reply.getId() + "\">");
            buffer.append("<b>Edit</b>");
            buffer.append("</a>\n");
            buffer.append("</td>\n");
        }

        //Reply To Link
        if (Permission.MODERATE.equals(mode) || Permission.WRITE.equals(mode)) {
            buffer.append("<td>\n");
            buffer.append("<a href=\"/forum/learning/message/post.do?topicId=");
            buffer.append(topicId);
            buffer.append("&parentId=");
            buffer.append(reply.getId() + "\">");
            buffer.append("<b>Reply</b>");
            buffer.append("</a>\n");
            buffer.append("</td>\n");
        }

        //Body of Message
        buffer.append("<tr>\n");
        buffer.append("<td>\n");
        buffer.append(reply.getBody());
        buffer.append("</td>\n");
        buffer.append("</tr>\n");

        //Intented Replies of the Messages
        buffer.append(indentPostfix);
        Iterator it = reply.getReplies().iterator();
        level = level + 2;
        while (it.hasNext()) {
            StringBuffer preFix = new StringBuffer();
            preFix.append("<table class=\"forumTable\">\n");
            preFix.append("<tr>\n");
            preFix.append("<td colspan=\"" + level + "\"\\>\n");
            preFix.append("<td>");
            preFix.append("<table class=\"forumTable\">\n");
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
