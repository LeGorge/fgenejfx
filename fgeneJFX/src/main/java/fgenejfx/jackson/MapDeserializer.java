package fgenejfx.jackson;

import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import fgenejfx.models.Pilot;
import fgenejfx.models.Powers;
import fgenejfx.models.Season;
import fgenejfx.models.Team;

public class MapDeserializer extends JsonDeserializer<Map<Object, Object>>
implements ContextualDeserializer {

private Class<?> keyAs;

private Class<?> contentAs;

@Override
public Map<Object, Object> deserialize(JsonParser p, DeserializationContext ctxt)
    throws IOException, JsonProcessingException {
    return this.deserialize(p, ctxt, new HashMap<>());
}

@Override
public Map<Object, Object> deserialize(JsonParser p, DeserializationContext ctxt,
Map<Object, Object> intoValue) throws IOException, JsonProcessingException {
    JsonNode node = p.readValueAsTree();
    ObjectCodec codec = p.getCodec();
    node.fields().forEachRemaining(entry -> {
        try {
            String chave = entry.getKey();
            Object chaveObj = null;
            
            switch (this.keyAs.getSimpleName()) {
                case "Pilot":
                chaveObj = Pilot.get(chave);
                break;
                
                case "Team":
                chaveObj = Team.get(chave);
                break;

                case "Season":
                chaveObj = Season.get(Integer.parseInt(chave));
                break;
                
                default:
                break;
            }
            JsonNode valueNode = entry.getValue();
            // intoValue.put(chaveObj, valueNode.traverse(codec).readValueAs(this.contentAs));
            if(this.contentAs.equals(EnumMap.class)){
                EnumMap<Powers,Double> enumMap = new EnumMap<>(Powers.class);
                valueNode.fields().forEachRemaining(e -> {
                    enumMap.put(Powers.valueOf(e.getKey()), e.getValue().asDouble());
                });;
                intoValue.put(chaveObj, enumMap);
            }else{
                intoValue.put(chaveObj, valueNode.traverse(codec).readValueAs(this.contentAs));
            }
        } catch (NullPointerException | IOException e) {
            // skip entry
        }
    });
    return intoValue;
}

@Override
public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
throws JsonMappingException {
    JsonDeserialize jsonDeserialize = property.getAnnotation(JsonDeserialize.class);
    this.keyAs = jsonDeserialize.keyAs();
    this.contentAs = jsonDeserialize.contentAs();
    return this;
}

}