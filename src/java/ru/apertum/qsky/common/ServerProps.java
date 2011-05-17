/*
 *  Copyright (C) 2010 {Apertum}Projects. web: www.apertum.ru email: info@apertum.ru
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
