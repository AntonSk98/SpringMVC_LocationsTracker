package com.internship.DAO.locationDAO;

import com.internship.models.locationModel.Location;

import java.text.ParseException;
import java.util.List;

public interface LocationDAO {

    boolean insert(Location location, int id_key);
    List<Location> getAllLocations();
    List<Location> getAllLocationsById(int id);
    Location getLast(int id_key);
    boolean update(Location UpdatedLocation) throws ParseException;
    Integer countOfNotes();
    List<Integer> getKeysByUserId(int userID);
    List<Location> getLocationsByKeyId(int key_id);
    List<Location> getLocationsByKeyIdLimit(int key_id, int number);
    Integer setDefaultValueOfpointsByUsername(String user);
    Integer getDefaultKeyByUsername(String user);
}
