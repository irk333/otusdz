package net.anarchy.social.samplesn.backend.dao;

import net.anarchy.social.samplesn.backend.entity.Interest;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InterestRowMapper implements RowMapper<Interest> {

    @Override
    public Interest mapRow(ResultSet rs, int i) throws SQLException {
        Interest interest = new Interest();
        interest.setId(rs.getLong("id"));
        interest.setName(rs.getString("name"));
        return interest;
    }
}
