package com.internship.DAO.uniqueKeyDAO;

import com.internship.models.uniqueKeyModel.UniqueKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;


@Repository
public class UniqueKeyDAOImpl implements UniqueKeyDAO {
    @Autowired
    private JdbcTemplate setupJdbcTemplate;
    @Override
    public boolean insert(UniqueKey uniqueKey) {
        String SQL_INSERT = "INSERT INTO unique_keys(key) VALUES (?);";
        try{
        setupJdbcTemplate.update(SQL_INSERT, uniqueKey.getKey());
        }catch (DuplicateKeyException e){
            return false;
        }
        return true;
    }

    @Override
    public Integer existence(String key) {
        String SQL_EXIST="SELECT COUNT(*) FROM unique_keys WHERE key = '"+key+"';";
        return setupJdbcTemplate.queryForObject(SQL_EXIST, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("count");
            }
        });

    }

    @Override
    public int getIdByKey(String key) {
        String SQL_GET_ID_BY_KEY = "SELECT key_id FROM unique_keys WHERE key='"+key+"';";
        return setupJdbcTemplate.queryForObject(SQL_GET_ID_BY_KEY, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("key_id");
            }
        });
    }
}
