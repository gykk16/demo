package dev.glory.demo.docs;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.payload.AbstractFieldsSnippet;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadSubsectionExtractor;
import org.springframework.restdocs.snippet.Attributes;

public class CustomResponseFieldsSnippet extends AbstractFieldsSnippet {

    public CustomResponseFieldsSnippet(String type, PayloadSubsectionExtractor<?> subsectionExtractor,
            List<FieldDescriptor> descriptors, Map<String, Object> attributes, boolean ignoreUndocumentedFields) {
        super(type, descriptors, attributes, ignoreUndocumentedFields, subsectionExtractor);
    }

    public static CustomResponseFieldsSnippet customResponseFields(String type,
            PayloadSubsectionExtractor<?> subsectionExtractor, Map<String, Object> attributes,
            FieldDescriptor... descriptors) {
        return new CustomResponseFieldsSnippet(type, subsectionExtractor, Arrays.asList(descriptors), attributes, true);
    }

    public static FieldDescriptor[] responseEnumConvertFieldDescriptor(Map<String, List<String>> enumValues) {
        return enumValues.entrySet().stream()
                .map(x -> fieldWithPath(x.getKey())
                        .attributes(new Attributes.Attribute("status", x.getValue().get(0)))
                        .description(x.getValue().get(1)))
                .toArray(FieldDescriptor[]::new);
    }

    /**
     * Map 으로 넘어온 enumValue 를 fieldWithPath 로 변경 하여 반환
     */
    public static FieldDescriptor[] statusEnumConvertFieldDescriptor(Map<String, List<String>> enumValues) {
        return enumValues.entrySet().stream()
                .map(x -> fieldWithPath(x.getKey())
                        .description(x.getValue().get(0)))
                .toArray(FieldDescriptor[]::new);
    }

    @Override
    protected MediaType getContentType(Operation operation) {
        return operation.getResponse().getHeaders().getContentType();
    }

    @Override
    protected byte[] getContent(Operation operation) throws IOException {
        return operation.getResponse().getContent();
    }

}
