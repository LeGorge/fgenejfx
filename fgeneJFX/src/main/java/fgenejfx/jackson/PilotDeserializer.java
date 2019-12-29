package fgenejfx.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import fgenejfx.models.Pilot;

import java.io.IOException;

public class PilotDeserializer extends StdDeserializer<Pilot> {

    public PilotDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Pilot deserialize(JsonParser parser, DeserializationContext deserializer) throws IOException {
		System.out.println("AKI");
        Pilot car = new Pilot();
        while(!parser.isClosed()){
			System.out.println("WHILE");
            JsonToken jsonToken = parser.nextToken();
			
            if(JsonToken.FIELD_NAME.equals(jsonToken)){
				System.out.println("fieldname");
				String fieldName = parser.getCurrentName();
				Object value = parser.getCurrentValue();
                System.out.println(fieldName);
                System.out.println(value);

                jsonToken = parser.nextToken();

            }
        }
        return car;
    }
}