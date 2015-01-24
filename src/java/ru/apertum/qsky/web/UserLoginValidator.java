package ru.apertum.qsky.web;

import java.util.Arrays;
import java.util.Map;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;

public class UserLoginValidator extends AbstractValidator {

    public String l(String resName) {
        return Labels.getLabel(resName);
    }

    static public void main(String[] strs) {
        final String usrs = System.getProperty("QSKY_USERS", "admin=admin");
        System.out.println("users="+usrs);
        System.out.println("!");
        String st = "1=11;   2 =  22 , 3  =33 #    4   =44@12345656_25436456.3465563245_r_d3_??;=;kievfriend=kievfriend@3";
        String[] ss = st.toLowerCase().replaceAll("\\s+", "").split(";|,");
        System.out.println(Arrays.toString(ss));
        System.out.println();
        for (String s : ss) {
            String[] ss1 = s.split("=");
            System.out.println(Arrays.toString(ss1));
            if (ss1.length < 2) {
                continue;
            }
            System.out.println("user=" + ss1[0]);
            String[] ss2 = ss1[1].split("@|$|&|%|#");
            System.out.println("password=" + ss2[0]);
            if (ss2.length > 1) {
                String[] ss3 = ss2[1].split("\\.|_");
                System.out.println("access " + Arrays.toString(ss3));
                for (String ss31 : ss3) {
                    if (ss31.matches("\\d+")) {
                        System.out.println("id=" + ss31);
                    }
                }
            } else {
                System.out.println("no limit permition");
            }
        }

    }

    @Override
    public void validate(ValidationContext ctx) {
        //all the bean properties
        final Map<String, Property> beanProps = ctx.getProperties(ctx.getProperty().getBase());
        final String usrs = System.getProperty("QSKY_USERS", "admin=admin");
        String[] ss = usrs.toLowerCase().replaceAll("\\s+", "").split(";|,");
        for (String s2 : ss) {
            final String[] usr = s2.split("=");
            if (usr.length != 2) {
                this.addInvalidMessage(ctx, "login", l("access_denied" + "!"));
                return;
            }
            final String user = usr[0];
            final String[] ss2 = usr[1].split("@|$|&|%|#");
            if (ss2.length != 1 && ss2.length != 2) {
                this.addInvalidMessage(ctx, "login", l("access_denied") + "!!");
                return;
            }
            final String password = ss2[0];
            if (usr.length == 2 && user.equalsIgnoreCase((String) beanProps.get("name").getValue()) && password.equalsIgnoreCase((String) beanProps.get("password").getValue())) {
                return;
            }
        }
        this.addInvalidMessage(ctx, "login", l("access_denied"));
    }

}
