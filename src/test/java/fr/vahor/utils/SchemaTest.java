package fr.vahor.utils;

import fr.vahor.API;
import fr.vahor.exceptions.InvalidFolderNameException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SchemaTest {

    @Test
    void isValidFolderName() {
        assertTrue(Schema.isValidFolderName("trees.big.truc.a"));
        assertFalse(Schema.isValidFolderName(".."));
        assertFalse(Schema.isValidFolderName("../../truc"));
        assertFalse(Schema.isValidFolderName("..t.r.u.c.t.t.t.t"));
    }
}