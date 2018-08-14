package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.ebean.Model;

// TODO: Auto-generated Javadoc
/**
 * The Class Groups.
 */
@Entity
public class Groups extends Model {
    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public String id;

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
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the id to set
     */
    public void setId(String id) {
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
