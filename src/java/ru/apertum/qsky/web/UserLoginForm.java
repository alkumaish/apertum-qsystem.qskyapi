package ru.apertum.qsky.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;

public class UserLoginForm {

    @Init
    public void init() {
        final org.zkoss.zk.ui.Session sess = Sessions.getCurrent();
        if (sess.getAttribute(org.zkoss.web.Attributes.PREFERRED_LOCALE) == null && Executions.getCurrent().getHeader("accept-language") != null && !Executions.getCurrent().getHeader("accept-language").isEmpty()) {
            final String ln = Executions.getCurrent().getHeader("accept-language").replace("-", "_").split(",")[0];
            if (langs.contains(ln)) {
                lang = ln;
            } else {
                lang = "en_GB";
            }
            final Locale prefer_locale = lang.length() > 2
                    ? new Locale(lang.substring(0, 2), lang.substring(3)) : new Locale(lang);
            sess.setAttribute(org.zkoss.web.Attributes.PREFERRED_LOCALE, prefer_locale);
        } else {
            lang = sess.getAttribute(org.zkoss.web.Attributes.PREFERRED_LOCALE) == null ? "hz"
                    : (((Locale) sess.getAttribute(org.zkoss.web.Attributes.PREFERRED_LOCALE)).getLanguage() + "_"
                    + ((Locale) sess.getAttribute(org.zkoss.web.Attributes.PREFERRED_LOCALE)).getCountry());
            if (!langs.contains(lang)) {
                lang = "en_GB";
                final Locale prefer_locale = lang.length() > 2
                        ? new Locale(lang.substring(0, 2), lang.substring(3)) : new Locale(lang);
                sess.setAttribute(org.zkoss.web.Attributes.PREFERRED_LOCALE, prefer_locale);
            }
        }

    }

    //*****************************************************
    //**** Multilingual
    //*****************************************************
    private static final ArrayList<String> langs = new ArrayList<>(Arrays.asList("ru_RU", "en_GB", "es_ES", "de_DE", "pt_PT", "fr_FR", "it_IT", "cs_CZ", "pl_PL", "sk_SK", "ro_RO", "sr_SP", "uk_UA", "tr_TR", "hi_IN", "ar_EG", "iw_IL", "kk_KZ", "in_ID", "fi_FI"));

    public ArrayList<String> getLangs() {
        return langs;
    }
    private String lang;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    @Command("changeLang")
    public void changeLang() {
        if (lang != null) {
            final org.zkoss.zk.ui.Session session = Sessions.getCurrent();
            final Locale prefer_locale = lang.length() > 2
                    ? new Locale(lang.substring(0, 2), lang.substring(3)) : new Locale(lang);
            session.setAttribute(org.zkoss.web.Attributes.PREFERRED_LOCALE, prefer_locale);
            Executions.sendRedirect(null);
        }
    }
    //**** Multilingual
    ////////////**************************************************
    ////////////**************************************************
    ////////////**************************************************

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
        final User usr = new User(user.getName(), user.getPassword());
        final String usrs = System.getProperty("QSKY_USERS", "admin=admin");
        String[] ss = usrs.toLowerCase().replaceAll("\\s+", "").split(";|,");
        for (String s : ss) {
            final String[] ss1 = s.split("=");
            if (ss1.length != 2) {
                continue;
            }
            final String[] ss2 = ss1[1].split("@|$|&|%|#");
            if (ss2.length == 2 && user.getName().equals(ss1[0]) && user.getPassword().equals(ss2[0])) {
                final String[] ss3 = ss2[1].split("\\.|_");
                for (String ss31 : ss3) {
                    if (ss31.matches("\\d+")) {
                        usr.addBranch(Long.decode(ss31));
                    }
                }
                break;
            }
        }

        try {
            Sessions.getCurrent().setAttribute("USER", usr);
            Executions.sendRedirect("/dashboard.zul");
        } catch (Throwable t) {
            System.err.println("Server SOO is down! " + t);
            Executions.sendRedirect("/error.zul");
        }
    }
}
