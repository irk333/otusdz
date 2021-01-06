package net.anarchy.social.samplesn.backend.dao;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LongRowMapper implements RowMapper<Long> {
    private String fieldName;

    public LongRowMapper(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public Long mapRow(ResultSet rs, int i) throws SQLException {
        return rs.getLong(fieldName);
    }
}
