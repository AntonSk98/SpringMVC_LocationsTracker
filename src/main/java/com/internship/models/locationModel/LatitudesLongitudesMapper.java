package com.internship.models.locationModel;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LatitudesLongitudesMapper implements RowMapper<Location> {
    @Override
    public Location mapRow(ResultSet rs, int rowNum) throws SQLException {
        Location latitudesLongitudes = new Location();
        latitudesLongitudes.setLat(rs.getDouble("lat"));
        latitudesLongitudes.setLng(rs.getDouble("lng"));
        return latitudesLongitudes;
    } //mapper для получения значений из бд с широтой и долготой
}
