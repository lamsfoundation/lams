package org.lamsfoundation.lams.themes;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.User;


/** 
 *        @hibernate.class
 *         table="lams_css_theme_ve"
 *     
 * Models both a theme, and the visual elements that make up a theme. If we ever have 
 * themes within themes then this data structure will support that too!
 * 
 * The isTheme() call will tell you if this is a theme or a visual element.
 * 
*/
public class CSSThemeVisualElement implements Serializable {

	protected Logger log = Logger.getLogger(CSSThemeVisualElement.class);	

	/** identifier field */
    private Long id;

    /** persistent field */
    private String name;

    /** nullable persistent field */
    private String description;

    /** persistent field */
    private boolean theme;

    /** nullable persistent field */
    private CSSThemeVisualElement parentTheme;

    /** persistent field */
    private Set users;

    /** persistent field */
    private Set styles;

    /** persistent field */
    private Set elements;

    /** full constructor */
    public CSSThemeVisualElement(Long id, String name, String description, boolean theme, CSSThemeVisualElement parentTheme, Set styles, Set users, Set elements) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.theme = theme;
        this.parentTheme = parentTheme;
        this.styles = styles;
        this.users = users;
        this.elements = elements;
    }

    /** default constructor */
    public CSSThemeVisualElement() {
    }

    /** minimal constructor */
    public CSSThemeVisualElement(Long id, String name, boolean theme) {
        this.id = id;
        this.name = name;
        this.theme = theme;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.Long"
     *             column="theme_ve_id"
     *         
     */
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** 
     *            @hibernate.property
     *             column="name"
     *             length="100"
     *             not-null="true"
     *         
     */
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** 
     *            @hibernate.property
     *             column="description"
     *             length="100"
     *         
     */
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /** 
     *      @hibernate.property
     *       column="theme_flag"
     *       length="1"
     *       not-null="true"
     *         
     */
    public boolean isTheme() {
        return this.theme;
    }

    public void setTheme(boolean theme) {
        this.theme = theme;
    }

    /** 
     * If a theme, then this is the base style. If a visual element, then
     * it is the style for this element.
     */
    public CSSStyle getStyle() {
        Set styleSet = getStyles();
        if ( styleSet != null ) 
            if ( styleSet.size() > 1 ) {
                log.warn("Theme/Visual element has more than one style - only one can be used. "+this);
            }
        	if ( styleSet.size() > 0 ) {
        	    return (CSSStyle) styleSet.iterator().next();
        }
        return null;
    }
    
    /** 
     * If a theme, then this is the base style. If a visual element, then
     * it is the style for this element.
     */
    public void setStyle(CSSStyle style) {
        if ( getStyles() != null ) {
            getStyles().clear();
            getStyles().add(style);
        } else {
            Set styleSet = new HashSet();
            styleSet.add(style);
            setStyles(styleSet);
        }
        style.setThemeElement(this);
    }

    /**
     * 
     * This is really a 1-1 as each theme/visual element has style should only be for one theme. Even if the 
     * values are the same, two styles should have different baseStyle records.
     * But its easier with Hibernate to make it a uni-directional many to one.
     * 
     * 	  @hibernate.set
     *       lazy="false"
     *       inverse="true"
     *       cascade="all-delete-orphan"
     *
     *      @hibernate.collection-key
     *       column="theme_ve_id"
     *
     *      @hibernate.collection-one-to-many
     *       class="org.lamsfoundation.lams.themes.CSSStyle"
     *         
     */
    private Set getStyles() {
        return this.styles;
    }

    private void setStyles(Set styles) {
        this.styles = styles;
    }

    /**
     * 
     * Users who have this theme as their style.
     *  
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="theme_id"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.usermanagement.User"
     *         
     */
    private Set getUsers() {
        return this.users;
    }

    private void setUsers(Set users) {
        this.users = users;
    }

     public void clearUsers() {
        if ( getUsers() != null ) {
            getUsers().clear();
        } 
    }

    public void addUser(User user) {
        if ( getUsers() == null ) {
            Set set = new HashSet();
            set.add(user);
            setUsers(set);
        } else {
            getUsers().add(user);
        }
        user.setTheme(this);
    }
    
    /**
     * 
     * The visual elements (or in the future sub-themes) that make up this theme
     *  
     *            @hibernate.set
     *             lazy="false"
     *             inverse="true"
     *             cascade="all-delete-orphan"
     *            @hibernate.collection-key
     *             column="parent_id"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.themes.CSSThemeVisualElement"
     *         
   */
    public Set getElements() {
        return this.elements;
    }

    private void setElements(Set elements) {
        this.elements = elements;
    }

     public void clearElements() {
        if ( getElements() != null ) {
            getElements().clear();
        } 
    }

    public void addElement(CSSThemeVisualElement element) {
        if ( getElements() == null ) {
            Set set = new HashSet();
            set.add(element);
            setElements(set);
        } else {
            getElements().add(element);
        }
        element.setParentTheme(this);
    }
    
    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     * 			   cascade="none"
     * 			   update="true"
     * 			   insert="true"
    *            @hibernate.column name="parent_id"         
     *         
     */
    public CSSThemeVisualElement getParentTheme() {
        return this.parentTheme;
    }

    public void setParentTheme(CSSThemeVisualElement parentTheme) {
        this.parentTheme = parentTheme;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .append("name", getName())
            .append("description", getDescription())
            .append("is theme?", isTheme())
            .append("style", getStyle())
            .append("visual elements", getElements())
            .append("parent theme", getParentTheme())
            .toString();
    }

}
