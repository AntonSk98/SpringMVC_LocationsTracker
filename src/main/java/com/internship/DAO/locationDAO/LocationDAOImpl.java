package com.internship.DAO.locationDAO;
import com.internship.models.locationModel.LatitudesLongitudesMapper;
import com.internship.models.locationModel.Location;
import com.internship.models.locationModel.LocationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
@Transactional
public class LocationDAOImpl implements LocationDAO {

    @Autowired
    private JdbcTemplate setupJdbcTemplate;

    @Override
    public boolean insert(Location location, int id_key) {
        String SQL_INSERT_PERSON = "INSERT INTO locations(lat,lng,time, key_id) VALUES (?,?,?,?);";
        setupJdbcTemplate.update(SQL_INSERT_PERSON, location.getLat(),location.getLng(), Timestamp.valueOf(location.getTime()), id_key);
        return true;
    }

    @Override
    public List<Location> getAllLocationsById(int id) {
        String SQL_GET_COORDINATES_BY_ID = "SELECT lat, lng from locations inner join unique_keys on (locations.key_id=unique_keys.key_id) WHERE user_id=?;"; //?зачем?
        return setupJdbcTemplate.query(SQL_GET_COORDINATES_BY_ID, new LatitudesLongitudesMapper(), id);
    }
    @Override
    public List<Location> getAllLocations() {
        String SQL_GET_ALL = "SELECT * FROM locations;";
        return setupJdbcTemplate.query(SQL_GET_ALL, new LocationMapper());
    }

    @Override
    public Location getLast(int id_key) { //получение последней записи из БД, используя последний id
        String SQL_GET_LAST_PERSON ="SELECT * FROM locations WHERE key_id=? ORDER BY id desc LIMIT 1;";
        return setupJdbcTemplate.queryForObject(SQL_GET_LAST_PERSON, new LocationMapper(), id_key);
    }

    @Override
    public boolean update(Location updatedLocation) { //метод Update обновляет запись
        String SQL_UPDATE="UPDATE locations SET lat=?, lng=? WHERE id=(SELECT MAX(id) FROM locations);";
        setupJdbcTemplate.update(SQL_UPDATE, updatedLocation.getLat(), updatedLocation.getLng());
        return true;
    }

    @Override
    public Integer countOfNotes() {
        String SQL_COUNT="SELECT COUNT(1) FROM locations;"; // запрос на количество записей в таблице с названием колонки "count"
        return setupJdbcTemplate.queryForObject(SQL_COUNT, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException { //аннонимный класс для обертки данных из БД в тип Integer
                return rs.getInt("count");
            }
        });
    }

    @Override
    public List<Integer> getKeysByUserId(int userID) {
        String SQL_GET_KEYS_BY_USER_ID = "SELECT DISTINCT locations.key_id FROM locations inner join unique_keys on (locations.key_id=unique_keys.key_id) where user_id=?;";
        return setupJdbcTemplate.query(SQL_GET_KEYS_BY_USER_ID, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("key_id");
            }
        },userID);
    }

    @Override
    public List<Location> getLocationsByKeyId(int key_id) {
        String SQL_GET_LOCATIONS_BY_KEY_ID = "SELECT * FROM locations WHERE key_id=?;";
        return setupJdbcTemplate.query(SQL_GET_LOCATIONS_BY_KEY_ID, new LatitudesLongitudesMapper(),key_id);
    }
    @Override
    public List<Location> getLocationsByKeyIdLimit(int key_id, int number) {
        String SQL_GET_LOCATIONS_BY_KEY_ID = "SELECT * FROM locations WHERE key_id=? ORDER BY id desc LIMIT ?;";
        return setupJdbcTemplate.query(SQL_GET_LOCATIONS_BY_KEY_ID, new LatitudesLongitudesMapper(), key_id, number);
    }

    @Override
    public Integer setDefaultValueOfpointsByUsername(String user){
        String SQL_GET_DEFAULT_POINTS_VALUE = "select default_value from default_values where user_id=(select user_id from app_user where app_user.user_name=?);";
        return setupJdbcTemplate.queryForObject(SQL_GET_DEFAULT_POINTS_VALUE, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("default_value");
            }
        }, user);
    }

    @Override
    public Integer getDefaultKeyByUsername(String user) {
        String SQL_GET_DEFAULT_KEY_BY_USERNAME = "select default_key_id from default_values where user_id=(select user_id from app_user where app_user.user_name=?);";
        return setupJdbcTemplate.queryForObject(SQL_GET_DEFAULT_KEY_BY_USERNAME, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("default_key_id");
            }
        },user);
    }
}
