package edu.ubb.dissertation.util;

import edu.ubb.dissertation.model.AbnormalVitalSignType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Optional;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class AbnormalVitalSignTypeConverter implements AttributeConverter<AbnormalVitalSignType, String> {

    @Override
    public String convertToDatabaseColumn(final AbnormalVitalSignType abnormalVitalSignType) {
        return Optional.ofNullable(abnormalVitalSignType)
                .map(AbnormalVitalSignType::getCode)
                .orElse(null);
    }

    @Override
    public AbnormalVitalSignType convertToEntityAttribute(final String s) {
        // TODO throw exception or return null ?
        return Optional.ofNullable(s)
                .map(code -> Stream.of(AbnormalVitalSignType.values())
                        .filter(c -> c.getCode().equals(code))
                        .findFirst()
                        .orElseThrow(IllegalArgumentException::new))
                .orElseThrow(IllegalArgumentException::new);
    }

}
