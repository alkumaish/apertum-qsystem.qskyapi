package ru.apertum.qsky.web;

import java.util.Arrays;
import java.util.Map;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

public class UserLoginValidator extends AbstractValidator {

    static public void main(String[] strs) {
        System.out.println("!");
        String st = "1=11;   2 =  22 , 3  =33 #    4   =44;=";
        String[] ss = st.toLowerCase().replaceAll("\\s+", "").split(";|,|#");
        System.out.println(Arrays.toString(ss));
        System.out.println();
        for (String s : ss) {
            System.out.println(Arrays.toString(s.split("=")));
        }

    }

    @Override
    public void validate(ValidationContext ctx) {
        //all the bean properties
        final Map<String, Property> beanProps = ctx.getProperties(ctx.getProperty().getBase());
        final String usrs = System.getProperty("QSKY_USERS", "admin=admin");
        String[] ss = usrs.toLowerCase().replaceAll("\\s+", "").split(";|,|#");
        System.out.println(Arrays.toString(ss));
        for (String s2 : ss) {
            final String[] usr = s2.split("=");
            if (usr.length == 2 && usr[0].equalsIgnoreCase((String) beanProps.get("name").getValue()) && usr[1].equalsIgnoreCase((String) beanProps.get("password").getValue())) {
                return;
            }
        }
        this.addInvalidMessage(ctx, "login", "Доступ закрыт!");
    }

}
