package dev.glory.demo.docs;

import static org.springframework.restdocs.snippet.Attributes.key;

import org.springframework.restdocs.snippet.Attributes;

public interface DocumentFormatGenerator {

    static Attributes.Attribute getDateFormat() {
        return getDateFormat("yyyy-MM-dd");
    }

    static Attributes.Attribute getDateFormat(String format) {
        return key("format").value(format);
    }

    static Attributes.Attribute getDateTimeFormat() {
        return getDateTimeFormat("yyyy-MM-dd’T’HH:mm:ss");
    }

    static Attributes.Attribute getDateTimeFormat(String format) {
        return key("format").value(format);
    }

    static Attributes.Attribute getDateTimeMsFormat() {
        return key("format").value("yyyy-MM-dd’T’HH:mm:ss.SSS");
    }

}
