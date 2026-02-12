package com.alp.entities;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.postgresql.util.PGobject;
import java.sql.SQLException;

@Converter(autoApply = true)
public class PuntoGeograficoConverter implements AttributeConverter<PuntoGeografico, Object> {

    @Override
    public Object convertToDatabaseColumn(PuntoGeografico attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            PGobject pgObj = new PGobject();
            pgObj.setType("point");
            pgObj.setValue("(" + attribute.getX() + "," + attribute.getY() + ")");
            return pgObj;
        } catch (SQLException e) {
            throw new RuntimeException("Error al convertir PuntoGeografico a PGobject", e);
        }
    }

    @Override
    public PuntoGeografico convertToEntityAttribute(Object dbData) {
        if (dbData == null) {
            return null;
        }
        String value = dbData.toString();

        try {
            value = value.replace("(", "").replace(")", "");
            String[] parts = value.split(",");

            double x = Double.parseDouble(parts[0]);
            double y = Double.parseDouble(parts[1]);

            return new PuntoGeografico(x, y);
        } catch (Exception e) {
            throw new RuntimeException("Error al parsear el punto de la base de datos: " + value, e);
        }
    }
}