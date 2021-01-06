package net.anarchy.social.samplesn.backend.dao;

import net.anarchy.social.samplesn.backend.SocialNetworkException;
import net.anarchy.social.samplesn.backend.entity.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class CityDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Optional<City> findById(long id) {
        List<City> lst = jdbcTemplate.query("select * from city where id = ?", new CityRowMapper(), id);
        if (lst.size() == 0) {
            return Optional.empty();
        } else {
            return Optional.of(lst.get(0));
        }
    }

    public long insert(@NonNull String name) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("insert into city (name) values (?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            return ps;
        }, keyHolder);

        return ((Number) keyHolder.getKey()).longValue();
    }

    public void delete(long id) {
        jdbcTemplate.update("delete from city where id = ?", id);
    }

    public City update(long id, @NonNull String newName) throws SocialNetworkException {
        jdbcTemplate.update("update city set name = ? where id = ?", newName, id);
        List<City> lst = jdbcTemplate.query("select * from city where id = ?", new CityRowMapper(), id);
        if (lst.size() == 0) {
            throw new SocialNetworkException("city " + id + " not exists");
        }
        return lst.get(0);
    }

    public Optional<City> findByNameExact(@NonNull String name) {
        List<City> lst = jdbcTemplate.query("select * from city where lower(name) = ?", new CityRowMapper(), name.trim().toLowerCase());
        if (lst.size() == 0) {
            return Optional.empty();
        } else {
            return Optional.of(lst.get(0));
        }
    }

    public List<City> findByName(String name, int maxCount) {
        List<City> lst = jdbcTemplate.query("select * from city where lower(name) like ? order by name limit ?", new CityRowMapper(), "%" + name.trim().toLowerCase() + "%", maxCount);
        return lst;
    }
}
