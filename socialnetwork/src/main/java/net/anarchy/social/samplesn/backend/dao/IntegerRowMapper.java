package net.anarchy.social.samplesn.backend.dao;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IntegerRowMapper implements RowMapper<Integer> {
    private String fieldName;

    public IntegerRowMapper(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public Integer mapRow(ResultSet rs, int i) throws SQLException {
        return rs.getInt(fieldName);
    }
}
