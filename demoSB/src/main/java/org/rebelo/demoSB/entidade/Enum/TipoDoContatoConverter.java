package org.rebelo.demoSB.entidade.Enum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class TipoDoContatoConverter implements AttributeConverter<TipoDoContato, String> {

	@Override
	public String convertToDatabaseColumn(TipoDoContato tipo) {

		if (tipo == null) {
			return null;
		}
		return tipo.getCode();
	}

	@Override
	public TipoDoContato convertToEntityAttribute(String code) {

		if (code == null) {
			return null;
		}

		return Stream.of(TipoDoContato.values()).filter(c -> c.getCode().equals(code)).findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

}
