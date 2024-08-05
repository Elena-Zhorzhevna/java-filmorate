package ru.yandex.practicum.filmorate.storage.db_storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exception.InternalServerException;


import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;

@Log4j2
@RequiredArgsConstructor
public class BaseDbStorage<T> {
    public static final String CONTAINS_SQL_TEMPLATE = "SELECT EXISTS (SELECT 1 FROM %s WHERE %s = ?);";


    protected final JdbcTemplate jdbc;
    protected final RowMapper<T> mapper;

    protected T findOne(String query, Object... params) {
        List<T> resultList = jdbc.query(query, mapper, params);
        return resultList.isEmpty() ? null : resultList.getFirst();
    }

    protected List<T> findMany(String query, Object... params) {
        return jdbc.query(query, mapper, params);
    }

    public boolean delete(String query, Object... params) {
        int rowsDeleted = jdbc.update(query, params);
        return rowsDeleted > 0;
    }

    protected long insert(String query, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int idx = 0; idx < params.length; idx++) {
                ps.setObject(idx + 1, params[idx]);
            }
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKeyAs(Long.class);
        if (id != null) {
            return id;
        } else {
            throw new InternalServerException("Не удалось сохранить данные");
        }
    }

    protected void update(String query, Object... params) {
        int rowsUpdated = jdbc.update(query, params);
        if (rowsUpdated == 0) {
            log.error("Не удалось обновить данные! query={}, args={}", query, params);
            throw new InternalServerException("Не удалось обновить данные");
        }
    }
    protected boolean contains(String tableName, String idColumnName, Long idValue) {
        return Boolean.TRUE.equals(jdbc.query(CONTAINS_SQL_TEMPLATE.formatted(tableName, idColumnName), rs -> {
            if (rs.next()) {
                return rs.getBoolean(1);
            }
            return false;
        }, idValue));
    }

    protected String collectionToSqlSyntax(Collection<?> collection) {
        StringJoiner sb = new StringJoiner(",");
        for (Object o : collection) sb.add(o.toString());
        return sb.toString();
    }
}