package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import io.ebean.Finder;
import io.ebean.Model;

// TODO: Auto-generated Javadoc
/**
 * The Class User.
 */
@Entity
@SequenceGenerator(name = "SEQ_STORE", sequenceName = "MEMBER_SEQ", allocationSize = 1)
@Table(name="MEMBER")
public class Member extends Model{

    /** The id. */
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
    public Long id;

    /**
     * The username.
     */
    @NotNull
    @Size(min = 2, max = 20)
    private String username;

    /**
     * The password.
     */
    @NotNull
    @Size(min = 2, max = 20)
    private String password;

    /**
     * The status.
     */
    @Size(min = 1, max = 2)
    private String status;


    /** The role. */
    @NotEmpty
    @Size(min = 2, max = 20)
    private String role;

    /** The avatar. */
    private String avatar;

    /** The description. */
    private String description;

    /** The Constant find. */
    public static final Finder<String, Member> find = new Finder<>(Member.class);

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
     * @param id the new id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
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
     * @param status the new status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the role.
     *
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role.
     *
     * @param role the new role
     */
    public void setRole(String role) {
        this.role = role;
    }

	/**
	 * Gets the avatar.
	 *
	 * @return the avatar
	 */
	public String getAvatar() {
		return avatar;
	}

	/**
	 * Sets the avatar.
	 *
	 * @param avatar the new avatar
	 */
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	/**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
	 * Gets the find.
	 *
	 * @return the find
	 */
	public static Finder<String, Member> getFind() {
		return find;
	}

}
