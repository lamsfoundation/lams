package org.lamsfoundation.lams.usermanagement;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="lams_language"
 *     
*/
public class Language implements Serializable {

    private static final long serialVersionUID = 3078170339946420515L;

	/** identifier field */
    private Byte languageId;

    /** persistent field */
    private String isoCode;

    /** full constructor */
    public Language(String isoCode) {
        this.isoCode = isoCode;
    }

    /** default constructor */
    public Language() {
    }

    /** 
     *            @hibernate.id
     *             generator-class="native"
     *             type="java.lang.Byte"
     *             column="language_id"
     *         
     */
    public Byte getLanguageId() {
        return this.languageId;
    }

    public void setLanguageId(Byte languageId) {
        this.languageId = languageId;
    }

    /** 
     *            @hibernate.property
     *             column="iso_code"
     *             unique="true"
     *             length="2"
     *             not-null="true"
     *         
     */
    public String getIsoCode() {
        return this.isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("languageId", getLanguageId())
            .toString();
    }

}
