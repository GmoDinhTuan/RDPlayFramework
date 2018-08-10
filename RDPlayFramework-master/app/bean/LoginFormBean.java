package bean;

import javax.validation.constraints.Size;

// TODO: Auto-generated Javadoc
/**
 * The Class LoginFormBean.
 */
public class LoginFormBean {
	
    /** The username. */
    @Size(min = 6, max = 50)
	public String username;

    /** The password. */
    @Size(min = 6, max = 50)
	public String password;

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
}
