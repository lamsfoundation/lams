package org.lamsfoundation.lams.contentrepository;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="lams_cr_workspace_credential"
 *     
*/
public class CrWorkspaceCredential implements Serializable {

    /** identifier field */
    private Long wcId;

    /** persistent field */
    private org.lamsfoundation.lams.contentrepository.CrWorkspace crWorkspace;

    /** persistent field */
    private org.lamsfoundation.lams.contentrepository.CrCredential crCredential;

    /** full constructor */
    public CrWorkspaceCredential(org.lamsfoundation.lams.contentrepository.CrWorkspace crWorkspace, org.lamsfoundation.lams.contentrepository.CrCredential crCredential) {
        this.crWorkspace = crWorkspace;
        this.crCredential = crCredential;
    }

    /** default constructor */
    public CrWorkspaceCredential() {
    }

    /** 
     *            @hibernate.id
     *             generator-class="identity"
     *             type="java.lang.Long"
     *             column="wc_id"
     *             unsaved-value="0"
     *         
     */
    public Long getWcId() {
        return this.wcId;
    }

    public void setWcId(Long wcId) {
        this.wcId = wcId;
    }

    /** 
     * bi-directional many-to-one association to CrWorkspace
     * 
     *            @hibernate.many-to-one not-null="true"
     * 
     *            @hibernate.column name="workspace_id"         
     *         
     */
    public org.lamsfoundation.lams.contentrepository.CrWorkspace getCrWorkspace() {
        return this.crWorkspace;
    }

    public void setCrWorkspace(org.lamsfoundation.lams.contentrepository.CrWorkspace crWorkspace) {
        this.crWorkspace = crWorkspace;
    }

    /** 
     * bi-directional many-to-one association to CrCredential
     * 
     *            @hibernate.many-to-one not-null="true"
     * 
     *            @hibernate.column name="credential_id"         
     *         
     */
    public org.lamsfoundation.lams.contentrepository.CrCredential getCrCredential() {
        return this.crCredential;
    }

    public void setCrCredential(org.lamsfoundation.lams.contentrepository.CrCredential crCredential) {
        this.crCredential = crCredential;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("wcId", getWcId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof CrWorkspaceCredential) ) return false;
        CrWorkspaceCredential castOther = (CrWorkspaceCredential) other;
        return new EqualsBuilder()
            .append(this.getWcId(), castOther.getWcId())
            .append(this.getCrWorkspace(), castOther.getCrWorkspace())
            .append(this.getCrCredential(), castOther.getCrCredential())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getWcId())
            .append(getCrWorkspace())
            .append(getCrCredential())
            .toHashCode();
    }

}
