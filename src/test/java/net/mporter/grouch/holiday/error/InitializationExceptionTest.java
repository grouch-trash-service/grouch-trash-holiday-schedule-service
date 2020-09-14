package net.mporter.grouch.holiday.error;

import org.junit.Test;

public class InitializationExceptionTest {

    @Test(expected = InitializationException.class)
    public void testInitializationExceptionTest() {
        throw new InitializationException("error", new RuntimeException());
    }
}