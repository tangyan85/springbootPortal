package com.wanda.portal.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "t_user")
public class User implements Serializable, UserDetails {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_key", unique = true)
    private String userKey;

    @Column(name = "user_name")
    private String username;
    
    @Column(name = "password")
    private String password;
    
    @Column(name = "raw_password")
    private String rawPassword;

	@Column(name = "mobile")
	private String mobile;

	@Column(name = "mail")
	private String mail;

	@Column(name = "dept")
	private String dept;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH, targetEntity = Role.class)
	@JoinTable(name = "t_user_role", joinColumns = @JoinColumn(name = "user_key", referencedColumnName = "user_key"), inverseJoinColumns = @JoinColumn(name = "role_key", referencedColumnName = "role_key"))
	private List<Role> roles = new ArrayList<>();

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> ga = new ArrayList<>();
		for (Role role : this.getRoles()) {
			ga.add(new SimpleGrantedAuthority(role.getRoleKey()));
		}
		if (ga.size() <= 0) {
			ga.add(new SimpleGrantedAuthority("ROLE_NORMAL_USER"));
		}
		return ga;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRawPassword() {
		return rawPassword;
	}

	public void setRawPassword(String rawPassword) {
		this.rawPassword = rawPassword;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", userkey='" + userKey + '\'' +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", rawPassword='" + rawPassword + '\'' +
				", mobile='" + mobile + '\'' +
				", mail='" + mail + '\'' +
				", dept='" + dept + '\'' +
				'}';
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return  Objects.equals(username, user.username) &&
                Objects.equals(mobile, user.mobile) &&
                Objects.equals(mail, user.mail) &&
                Objects.equals(dept, user.dept);
    }

    @Override
    public int hashCode() {

        return Objects.hash(username, mobile, mail, dept);
    }
}
