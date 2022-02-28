package fr.vahor.i18n;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @Test
    void loadLanguage() {

        Message.loadLanguage();

        assertEquals("Salut", Message.PREFIX.toString());
        assertEquals("SalutSalut", Message.PREFIX + Message.PREFIX.toString());

        for (Message message : Message.values()) {
            assertNotNull(message.toString());
        }
    }
}