package rps;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTests {
    @Test
    void testSingleKeyValuePair() {
        StringParser parser = new StringParser();
        String input = "regCompany&companyName@Apple";
        Map<String, String> result = parser.parse(input);

        assertEquals(2, result.size());
        assertEquals("regCompany", result.get("commandName"));
        assertEquals("Apple", result.get("companyName"));
    }

    @Test
    void testMultipleKeyValuePairs() {
        StringParser parser = new StringParser();
        String input = "regCompany&companyName@Apple#companyID@1234";
        Map<String, String> result = parser.parse(input);

        assertEquals(3, result.size());
        assertEquals("regCompany", result.get("commandName"));
        assertEquals("Apple", result.get("companyName"));
        assertEquals("1234", result.get("companyID"));
    }

//    @Test
//    void testEmptyValueValidInput() {
//        StringParser parser = new StringParser();
//        String input = "regCompany&companyName@#companyID@1234";
//        Map<String, String> result = parser.parse(input);
//
//        assertEquals(3, result.size());
//        assertEquals("regCompany", result.get(null));
//        assertEquals("", result.get("companyName"));
//        assertEquals("1234", result.get("companyID"));
//    }

    @Test
    void testOnlyCommand() {
        StringParser parser = new StringParser();
        String input = "regCompany";
        Map<String, String> result = parser.parse(input);

        assertEquals(1, result.size());
        assertEquals("regCompany", result.get("commandName"));
    }

    @Test
    void testEmptyKey() {
        StringParser parser = new StringParser();
        String input = "regCompany&@value#companyID@1234";
        Map<String, String> result = parser.parse(input);

        assertEquals(3, result.size());
        assertEquals("regCompany", result.get("commandName"));
        assertEquals("value", result.get(""));
        assertEquals("1234", result.get("companyID"));
    }

    @Test
    void testTrailingDelimiter() {
        StringParser parser = new StringParser();
        String input = "regCompany&key@value#";
        Map<String, String> result = parser.parse(input);

        assertEquals(2, result.size());
        assertEquals("regCompany", result.get("commandName"));
        assertEquals("value", result.get("key"));
    }
}



//package il.co.ilrd.rps;
//
//import org.junit.jupiter.api.*;
//import java.util.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//public class StringParserTest {
//
//    private StringParser parser;
//
//    @BeforeEach
//    void setUp() {
//        parser = new StringParser();
//    }
//
//
//
//    @Test
//    void testMultipleKeyValueParsing() {
//        String request = "commandName&Key1@value1#Key2@value2#Key3@value3";
//        Map<String, String> result = parser.parse(request);
//
//        assertNotNull(result);
//        assertEquals(4, result.size());
//        assertEquals("commandName", result.get("commandName"));
//        assertEquals("value1", result.get("Key1"));
//        assertEquals("value2", result.get("Key2"));
//        assertEquals("value3", result.get("Key3"));
//    }
//
//    @Test
//    void testMalformedRequest() {
//        String request = "commandName&Key1value1";
//        assertThrows(ArrayIndexOutOfBoundsException.class, () -> parser.parse(request));
//    }
//
//    @Test
//    void testCommandOnly() {
//        String request = "commandName&";
//        Map<String, String> result = parser.parse(request);
//
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals("commandName", result.get("commandName"));
//    }
//
//    @Test
//    void testExtraDelimiters() {
//        String request = "commandName&&Key1@value1#Key2@value2";
//        assertThrows(ArrayIndexOutOfBoundsException.class, () -> parser.parse(request));
//    }
//
//    @Test
//    void testEmptyValues() {
//        String request = "commandName&Key1@#Key2@value2";
//        assertThrows(ArrayIndexOutOfBoundsException.class, () -> parser.parse(request));
//    }
//
//    @Test
//    void testMissingKeyOrValue() {
//        String request = "commandName&Key1#@value2";
//        assertThrows(ArrayIndexOutOfBoundsException.class, () -> parser.parse(request));
//    }
//}
//
