package fr.vahor.i18n;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @Test
    void loadLanguage() {

        Message.loadLanguage();

        System.out.println("Message.INVENTORY_ROTATION_LORE.toString().split(\"\\n\") = " + Message.INVENTORY_ROTATION_LORE);

        assertNotEquals(1, Message.INVENTORY_ROTATION_LORE.toString().split("\n").length);
        for (Message message : Message.values()) {
            assertNotNull(message.toString());
        }
    }
}