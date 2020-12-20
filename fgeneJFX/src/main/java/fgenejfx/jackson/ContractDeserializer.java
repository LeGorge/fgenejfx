package fgenejfx.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import fgenejfx.models.Contract;
import fgenejfx.models.Pilot;
import fgenejfx.models.Team;

public class ContractDeserializer extends JsonDeserializer<Contract>
		implements ContextualDeserializer {

	@Override
	public Contract deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		return this.deserialize(p, ctxt, new Contract());
	}

	@Override
	public Contract deserialize(JsonParser p, DeserializationContext ctxt, Contract intoValue)
			throws IOException, JsonProcessingException {
		JsonNode node = p.readValueAsTree();
		intoValue.setPilot(Pilot.get(node.findValue("pilot").textValue()));
		intoValue.setTeam(Team.get(node.findValue("team").textValue()));
		intoValue.setIsFirst(node.findValue("isFirst").asBoolean());
		intoValue.setYears(node.findValue("years").asInt());
		return intoValue;
	}

	@Override
	public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
			throws JsonMappingException {
		return this;
	}
}