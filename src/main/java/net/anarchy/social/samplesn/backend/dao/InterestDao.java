package net.anarchy.social.samplesn.backend.dao;

import net.anarchy.social.samplesn.backend.SocialNetworkException;
import net.anarchy.social.samplesn.backend.entity.Interest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class InterestDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Optional<Interest> findById(long id) {
        List<Interest> lst = jdbcTemplate.query("select * from interest where id = ?", new InterestRowMapper(), id);
        if (lst.size() == 0) {
            return Optional.empty();
        } else {
            return Optional.of(lst.get(0));
        }
    }

    public long insert(@NonNull String name) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("insert into interest (name) values (?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            return ps;
        }, keyHolder);

        return ((Number) keyHolder.getKey()).longValue();
    }

    public void delete(long id) {
        jdbcTemplate.update("delete from interest where id = ?", id);
    }

    public Interest update(long id, @NonNull String newName) throws SocialNetworkException {
        jdbcTemplate.update("update interest set name = ? where id = ?", newName, id);
        List<Interest> lst = jdbcTemplate.query("select * from interest where id = ?", new InterestRowMapper(), id);
        if (lst.size() == 0) {
            throw new SocialNetworkException("interest " + id + " not exists");
        }
        return lst.get(0);
    }

    public Optional<Interest> findByNameExact(@NonNull String name) {
        List<Interest> lst = jdbcTemplate.query("select * from interest where lower(name) = ?", new InterestRowMapper(), name.trim().toLowerCase());
        if (lst.size() == 0) {
            return Optional.empty();
        } else {
            return Optional.of(lst.get(0));
        }
    }

    public List<Interest> findByName(String name, int maxCount) {
        List<Interest> lst = jdbcTemplate.query("select * from interest where lower(name) like ? order by name limit ?", new InterestRowMapper(), "%" + name.trim().toLowerCase() + "%", maxCount);
        return lst;
    }

    public List<Interest> findByNames(Set<String> interestNames) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("names", interestNames );
        List<Interest> lst = namedParameterJdbcTemplate.query("select * from interest where lower(name) in (:names)", parameters, new InterestRowMapper());
        return lst;
    }
}
