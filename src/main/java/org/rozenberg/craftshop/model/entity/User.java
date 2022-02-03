package org.rozenberg.craftshop.model.entity;

public class User {
    private long userId;
    private String name;
    private String surname;
    private String login;
    private String password;
    private String email;
    private Role role;
    private UserStatus userStatus;
    private long invoiceId;

    public User() {
    }

    public User(long userId, String name, String surname, String login, String password, String email, Role role, UserStatus userStatus, long invoiceId) {
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
        this.email = email;
        this.role = role;
        this.userStatus = userStatus;
        this.invoiceId = invoiceId;
    }

    public User(String name, String surname, String login, String password, String email, Role role, UserStatus userStatus, long invoiceId) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
        this.email = email;
        this.role = role;
        this.userStatus = userStatus;
        this.invoiceId = invoiceId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(long invoiceId) {
        this.invoiceId = invoiceId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User newUser = (User) obj;
        if (userId != newUser.userId) return false;
        if (name != null ? !name.equals(newUser.name) : newUser.name != null) return false;
        if (surname != null ? !surname.equals(newUser.surname) : newUser.surname != null) return false;
        if (login != null ? !login.equals(newUser.login) : newUser.login != null) return false;
        if (password != null ? !password.equals(newUser.password) : newUser.password != null) return false;
        if (email != null ? !email.equals(newUser.email) : newUser.email != null) return false;
        if (role != newUser.role) return false;
        if (userStatus != newUser.userStatus) return false;
        return invoiceId == newUser.invoiceId;
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (userStatus != null ? userStatus.hashCode() : 0);
        result = 31 * result + (int) (invoiceId ^ (invoiceId >>> 32));
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("userId=").append(userId);
        sb.append(", name='").append(name).append('\'');
        sb.append(", surname='").append(surname).append('\'');
        sb.append(", login='").append(login).append('\'');
        sb.append(", password='").append("********").append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", role=").append(role);
        sb.append(", userStatus=").append(userStatus);
        sb.append(", invoiceId=").append(invoiceId);
        sb.append('}');
        return sb.toString();
    }
}
