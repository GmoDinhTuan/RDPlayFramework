package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import io.ebean.Model;

/**
 * The Class MembersGroup.
 */
@Entity
@SequenceGenerator(name = "SEQ_STORE", sequenceName = "MEMBERSGROUP_SEQ", allocationSize = 1)
@Table(name="MEMBERSGROUP")
public class MembersGroup extends Model {

    /** The id. */
	@Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
	@Column(name="ID")
    public Long id;

	@NotNull
    /** The group id. */
	@Column(name="GROUPID")
    public Long groupId;

	@NotNull
    /** The member id. */
	@Column(name="MEMBERID")
    public Long memberId;

	@NotNull
    /** The status. */
	@Column(name="STATUS")
    public String status;

    /** The username. */
//    public String username;
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    /**
     * @return the groupId
     */
    public Long getGroupId() {
        return groupId;
    }


	/**
     * @param groupId the groupId to set
     */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    /**
     * @return the memberId
     */
    public Long getMemberId() {
        return memberId;
    }

    /**
     * @param memberId the memberId to set
     */
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the username
     */
//    public String getUsername() {
//        return username;
//    }
//
//    /**
//     * @param username the username to set
//     */
//    public void setUsername(String username) {
//        this.username = username;
//    }

}
