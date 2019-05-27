package com.internship.models.locationModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationMapper implements RowMapper<Location> {
    public Location mapRow(ResultSet resultSet, int i) throws SQLException {
        Location location = new Location();
        location.setTime(resultSet.getString("time"));
        location.setLat(resultSet.getDouble("lat"));
        location.setLng(resultSet.getDouble("lng"));
        return location;
    } //mapper для получения значений из бд с широтой, долготой и временем
}
