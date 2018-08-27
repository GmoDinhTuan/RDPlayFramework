package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * The Class Relationship.
 */
@Entity
@SequenceGenerator(name = "SEQ_STORE", sequenceName = "RELATIONSHIP_SEQ", allocationSize = 1)
@Table(name = "RELATIONSHIP")
public class Relationship {

    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
    @Column(name = "ID")
    public Long id;

    /** The member from. */
    @NotNull
    @Column(name = "MEMBERFROM")
    public Long memberFrom;

    /** The member to. */
    @NotNull
    @Column(name = "MEMBERTO")
    public Long memberTo;

    /** The name. */
    @NotNull
    @Column(name = "NAME")
    public String name;

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the member from.
     *
     * @return the memberFrom
     */
    public Long getMemberFrom() {
        return memberFrom;
    }

    /**
     * Sets the member from.
     *
     * @param memberFrom the memberFrom to set
     */
    public void setMemberFrom(Long memberFrom) {
        this.memberFrom = memberFrom;
    }

    /**
     * Gets the member to.
     *
     * @return the memberTo
     */
    public Long getMemberTo() {
        return memberTo;
    }

    /**
     * Sets the member to.
     *
     * @param memberTo the memberTo to set
     */
    public void setMemberTo(Long memberTo) {
        this.memberTo = memberTo;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
