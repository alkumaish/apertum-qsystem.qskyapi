package ru.apertum.qsky.web;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;

public class UserLoginForm {

    private User user = new User();

    public User getUser() {
        final User cl = (User) Sessions.getCurrent().getAttribute("USER");
        if (cl != null) {
            user = cl;
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Command
    public void submit() {
        try {
            Sessions.getCurrent().setAttribute("USER", user);
            Executions.sendRedirect("/dashboard.zul");
        } catch (Throwable t) {
            System.err.println("Server SOO is down! " + t);
            Executions.sendRedirect("/error.zul");
        }
    }
}
