package net.anarchy.social.samplesn.backend.dao;

import net.anarchy.social.samplesn.backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SearchDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int countUsers(String searchText) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("searchText", "%" + searchText + "%");

        String sql = SQL_COUNT_SELECT + " " + SQL_BASE_FROM + " " + SQL_BASE_WHERE;
        List<Integer> lst = namedParameterJdbcTemplate.query(sql,parameters, new IntegerRowMapper("cntall") );
        if (lst == null || lst.size() == 0) {
            return 0;
        } else {
            return lst.get(0);
        }
    }

    public List<User> listUsers(String searchText, int start, int pageSize) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("searchText", "%" + searchText + "%");
        parameters.addValue("startpos", start);
        parameters.addValue("pagesize", pageSize);

        String sql = SQL_LIST_SELECT + " " + SQL_BASE_FROM + " " + SQL_BASE_WHERE + " " + SQL_LIST_ORDER_BY + " " + SQL_LIST_LIMIT;
        List<User> lst = namedParameterJdbcTemplate.query(sql,parameters,new UserRowMapper());
        return lst;
    }

    private static final String SQL_COUNT_SELECT = "select count(u.id) as cntall";
    private static final String SQL_LIST_SELECT = "select u.*, c.name as cityName";
    private static final String SQL_LIST_ORDER_BY = "order by u.lastname, u.firstname";
    private static final String SQL_LIST_LIMIT = "limit :startpos, :pagesize";
    private static final String SQL_BASE_FROM = "from users u left outer join city c on (c.id = u.city_id)";
    private static final String SQL_BASE_WHERE = "where lower(concat(u.lastname,' ',u.firstname,' ',c.name, ' ', case when u.gender = 1 then 'male' else 'female' end, ' ', u.age)) like :searchText or exists(select ui.* from users_interest ui inner join interest i on (i.id = ui.interest_id) where (ui.user_id = u.id) and (lower(i.name) like :searchText))";
}
