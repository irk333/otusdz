package net.anarchy.social.samplesn.backend.dao;

import net.anarchy.social.samplesn.backend.entity.City;
import net.anarchy.social.samplesn.backend.entity.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int i) throws SQLException {
        User user = new User();

        user.setId(rs.getLong("id"));
        user.setEmail(rs.getString("email"));
        user.setUserPassword(rs.getString("userpassword"));
        user.setLastName(rs.getString("lastname"));
        user.setFirstName(rs.getString("firstname"));
        user.setAge(rs.getShort("age"));

        int gender = rs.getShort("gender");
        user.setGender(User.Gender.findById(gender));
        if (user.getGender() == null) {
            throw new SQLException("Invalid gender value"  + gender);
        }

        long cityId = rs.getLong("city_id");
        if (cityId != 0) {
            City city = new City();
            city.setId(cityId);
            user.setCity(city);

            String cityName = rs.getString("cityName");
            city.setName(cityName);
        }

        return user;
    }
}
