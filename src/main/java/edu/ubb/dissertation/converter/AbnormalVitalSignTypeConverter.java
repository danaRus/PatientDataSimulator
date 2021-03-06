package edu.ubb.dissertation.converter;

import edu.ubb.dissertation.model.AbnormalVitalSignType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Responsible for ensuring that {@link AbnormalVitalSignType} is correctly mapped, when
 * - persisting to a database
 * - reading the values from a database
 */
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
        return Optional.ofNullable(s)
                .map(code -> Stream.of(AbnormalVitalSignType.values())
                        .filter(c -> c.getCode().equals(code))
                        .findFirst()
                        .orElseThrow(IllegalArgumentException::new))
                .orElseThrow(IllegalArgumentException::new);
    }
}
