package me.taling.live.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Sl4jTest {

    private static String jndi = "${jndi:ldap://log4shell.huntress.com:1389/4882aae6-e3d3-468b-a46e-39a552f962d9}";
    private static final Logger log = LogManager.getLogger(Sl4jTest.class);

    public static void main(String[] args) {
        log.error(jndi);
    }
}
