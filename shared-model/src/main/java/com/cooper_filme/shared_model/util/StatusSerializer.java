package com.cooper_filme.shared_model.util;

import com.cooper_filme.shared_model.model.ScriptStatus;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StatusSerializer extends JsonSerializer<ScriptStatus> {

    @Override
    public void serialize(ScriptStatus value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String valueTranslated = StatusTranslator.translateStatusToString(value);
        jsonGenerator.writeString(valueTranslated);
    }
}