/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.apertum.qsky.common;

/**
 *
 * @author egorov
 */
public class ServerProps {

    //final Properties settings;
    private ServerProps() {
        /*
        settings = new Properties();
        final InputStream inStream = settings.getClass().getResourceAsStream("/qskyapi.properties");
        try {
        settings.load(inStream);
        } catch (IOException ex) {
        throw new RuntimeException("Проблемы с чтением свойств. ", ex);
        }
         *
         */
    }

    public static ServerProps getInstance() {
        return ServerPropsHolder.INSTANCE;
    }

    private static class ServerPropsHolder {

        private static final ServerProps INSTANCE = new ServerProps();
    }
    final private String vers = "1;2";

    public boolean isSupportClient(String clientVersion) {
        for (String ver : vers.split(";")) {
            if (clientVersion.trim().equalsIgnoreCase(ver.trim())) {
                return true;
            }
        }
        return false;
    }
}
