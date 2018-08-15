package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.ebean.Model;

// TODO: Auto-generated Javadoc
/**
 * The Class Groups.
 */
@Entity
@SequenceGenerator(name = "SEQ_STORE", sequenceName = "GROUPS_SEQ", allocationSize = 1)
@Table(name="GROUPS")
public class Groups extends Model {
    /** The id. */
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
    public Long id;

    /** The groupsname. */
    @NotNull
    @Size(min = 2, max = 255)
    private String groupsname;

    /** The status. */
    @NotNull
    @Size(max = 1)
    private String status;

    /** The description. */
    private String description;

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
     * Gets the groupsname.
     *
     * @return the groupsname
     */
    public String getGroupsname() {
        return groupsname;
    }

    /**
     * Sets the groupsname.
     *
     * @param groupsname the groupsname to set
     */
    public void setGroupsname(String groupsname) {
        this.groupsname = groupsname;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
