package fgenejfx.jackson;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import fgenejfx.models.Contract;

public class ContractSerializer extends JsonSerializer<Contract> {

    @Override
    public void serialize(Contract value, JsonGenerator gen, SerializerProvider serializers)
    throws IOException, JsonProcessingException {
        gen.writeStartObject();
        gen.writeObjectField("pilot", value.getPilot().getName());
        gen.writeObjectField("team", value.getTeam().getName());
        gen.writeEndObject();
    }

}