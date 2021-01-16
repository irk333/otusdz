package net.anarchy.social.samplesn.backend.dao;

import net.anarchy.social.samplesn.backend.SocialNetworkException;
import net.anarchy.social.samplesn.backend.entity.Interest;
import net.anarchy.social.samplesn.backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.repository.support.SimpleJdbcRepository;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Optional<User> findById(long id) {
        List<User> lst = jdbcTemplate.query("select u.*, c.name as cityName from users u left join city c on (c.id = u.city_id) where u.id = ?",new UserRowMapper(), id );
        if (lst.size() == 0) {
            return Optional.empty();
        } else {
            return Optional.of(lst.get(0));
        }
    }

    public Optional<User> findByEmail(@NonNull String email) {
        List<User> lst = jdbcTemplate.query("select u.*, c.name as cityName from users u left join city c on (c.id = u.city_id) where lower(u.email) = ?",new UserRowMapper(), email.trim().toLowerCase() );
        if (lst.size() == 0) {
            return Optional.empty();
        } else {
            return Optional.of(lst.get(0));
        }
    }

    public long insert(@NonNull User user, @NonNull String password) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("insert into users (email, lastname, firstname, age, gender, city_id, userpassword) values (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail().trim());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getFirstName());
            ps.setInt(4, user.getAge());
            ps.setInt(5, user.getGender().getId());
            if (user.getCity() == null) {
                ps.setNull(6, Types.BIGINT);
            } else {
                ps.setLong(6,user.getCity().getId());
            }
            ps.setString(7, password);
            return ps;
        }, keyHolder);

        return ((Number) keyHolder.getKey()).longValue();
    }

    public List<User> findFriends(long userId) {
        List<User> lst = jdbcTemplate.query("select * from v_users_city u inner join users_friends f on (f.friend_id = u.id) where f.user_id = ? ", new UserRowMapper(), userId);
        return lst;
    }

    public List<User> findFriendOfUsers(long userId) {
        List<User> lst = jdbcTemplate.query("select * from v_users_city u inner join users_friends f on (f.user_id = u.id) where f.friend_id = ? ", new UserRowMapper(), userId);
        return lst;
    }

    public List<Long> findFriendIds(long userId, Set<Long> ids) {
        if (ids.size() == 0) {
            return new ArrayList<>();
        }
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("friendIds", ids );
        parameters.addValue("userId", userId);

        List<Long> resIds = namedParameterJdbcTemplate.query("select friend_id from users_friends where user_id = :userId and friend_id in (:friendIds)", parameters,new LongRowMapper("friend_id") );
        return resIds;
    }

    public List<Long> findFriendOfUserIds(long userId, Set<Long> ids) {
        if (ids.size() == 0) {
            return new ArrayList<>();
        }
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("friendIds", ids );
        parameters.addValue("userId", userId);

        List<Long> resIds = namedParameterJdbcTemplate.query("select user_id from users_friends where friend_id = :userId and user_id in (:friendIds)", parameters,new LongRowMapper("user_id") );
        return resIds;
    }


    public List<Interest> loadInterests(long userId) {
        List<Interest> lst = jdbcTemplate.query("select i.* from users_interest ui inner join interest i on (i.id = ui.interest_id) where ui.user_id = ?", new InterestRowMapper(), userId);
        return lst;
    }

    public List<User> loadFriends(long userId) {
        List<User> lst = jdbcTemplate.query("select i.*, c.name as cityName from users_friends ui inner join users i on (i.id = ui.friend_id) left outer join city c on (c.id = i.city_id) where ui.user_id = ?", new UserRowMapper(), userId);
        return lst;
    }

    public List<User> loadFriendsOf(long userId) {
        List<User> lst = jdbcTemplate.query("select i.*, c.name as cityName from users_friends ui inner join users i on (i.id = ui.user_id) left outer join city c on (c.id = i.city_id) where ui.friend_id = ?", new UserRowMapper(), userId);
        return lst;
    }

    public User update(@NonNull User user) throws SocialNetworkException {
        jdbcTemplate.update("update users set lastname = ?, firstname = ?, age = ?, gender = ?, city_id = ? where id = ?", user.getLastName(), user.getFirstName(), user.getAge(), user.getGender().getId(), (user.getCity() == null)?null:user.getCity().getId(), user.getId() );

        Optional<User> updatedUser = findById(user.getId());
        if (!updatedUser.isPresent()) {
            throw new SocialNetworkException(HttpStatus.NOT_FOUND, "User " + user.getId() + " not exists");
        }
        return updatedUser.get();
    }

    public void updateInterests(long userId, Set<Long> interestIds) {
        // remove all existing interests
        jdbcTemplate.update("delete from users_interest where user_id = ?", userId);

        // add new interests
        for (Long interestId: interestIds) {
            jdbcTemplate.update("insert into users_interest(user_id, interest_id) values (?,?)", userId, interestId);
        }
    }

    public void delete(long id) {
        jdbcTemplate.update("delete from users where id = ?", id);
    }

    public void insertFriend(long userId, long friendUserId) {
        jdbcTemplate.update("insert into users_friends (user_id, friend_id) values (?,?)", userId, friendUserId);
    }

    public void deleteFriend(long userId, long friendUserId) {
        jdbcTemplate.update("delete from users_friends where user_id = ? and friend_id = ?", userId, friendUserId);
    }
}
