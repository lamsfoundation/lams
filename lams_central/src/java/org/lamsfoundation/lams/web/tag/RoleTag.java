package org.lamsfoundation.lams.web.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.StringUtils;

/**
 * JSP tag. Converts role name into form usable as message resources key.
 *
 *
 *
 * Converts role name into form usable as message resources key
 *
 * @author jliew
 *
 */
public class RoleTag extends SimpleTagSupport {

    private String role;

    @Override
    public void doTag() throws JspException, IOException {
	if (StringUtils.isEmpty(role)) {
	    getJspContext().getOut().write(role);
	}

	role = role.replace(' ', '.');
	role = role.replaceFirst("COURSE", "GROUP");
	getJspContext().getOut().write(role.toString());
    }

    /**
     *
     *
     *
     * Role name as given from database
     *
     * @return
     */
    public String getRole() {
	return role;
    }

    public void setRole(String role) {
	this.role = role;
    }

}
