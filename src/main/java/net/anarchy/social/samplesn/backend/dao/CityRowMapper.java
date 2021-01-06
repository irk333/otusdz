package net.anarchy.social.samplesn.backend.dao;

import net.anarchy.social.samplesn.backend.entity.City;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CityRowMapper implements RowMapper<City> {

    @Override
    public City mapRow(ResultSet rs, int i) throws SQLException {
        City city = new City();
        city.setId(rs.getLong("id"));
        city.setName(rs.getString("name"));
        return city;
    }
}
