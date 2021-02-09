package org.rebelo.demoSB.entidade.Enum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class MarcaConverter implements AttributeConverter<Marca, String> {

	@Override
	public String convertToDatabaseColumn(Marca marca) {
		
		if (marca == null) {
			return null;
		}
		return marca.getMarca();
		
	}

	@Override
	public Marca convertToEntityAttribute(String marca) {
		

		if (marca == null) {
			return null;
		}

		return Stream.of(Marca.values()).filter(m -> m.getMarca().equals(marca)).findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}
		
	}


