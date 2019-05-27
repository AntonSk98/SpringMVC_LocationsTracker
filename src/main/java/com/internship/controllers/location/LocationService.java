package com.internship.controllers.location;

import com.internship.DAO.locationDAO.LocationDAO;
import com.internship.DAO.uniqueKeyDAO.UniqueKeyDAO;
import com.internship.models.locationModel.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.temporal.ChronoUnit;

import static java.lang.Math.*;

@Service
public class LocationService {
    @Autowired
    private LocationDAO locationDAO;
    @Autowired
    private UniqueKeyDAO uniqueKeyDAO;

    Location EmergingOrAdding(Location currentLocation, int id_key) throws ParseException {
        Location previousLocation;
        if(locationDAO.countOfNotes()==0) //если количество записей в таблице равно нулю
            locationDAO.insert(currentLocation, id_key); //добавляем координату в любом случае
        else {
            previousLocation = locationDAO.getLast(id_key); //получаем последнюю запись из БД
            System.out.println(previousLocation);
            double radiusOfTheEarth = 6371.210;
            double distanceInMetres = abs((((sin(currentLocation.getLat())*sin(previousLocation.getLat()))+(cos(currentLocation.getLat())*cos(previousLocation.getLat())*cos(currentLocation.getLng()-previousLocation.getLng())))*radiusOfTheEarth)/1000);
            double timeDifference = abs(ChronoUnit.MINUTES.between(currentLocation.getTime(),previousLocation.getTime())); //находим разницу во времени, разницу типа double в минутах
            if (distanceInMetres >= 5.0 && timeDifference >= 5.0){ //если расстоние больше 5 метров и время с последней записи больше 5 минут то добавляем координату
                locationDAO.insert(currentLocation, id_key);
                return currentLocation;
            }
            else {
                //иначе вычисляем среднее арифметическое ширины и долготы между псоледней записью из БД и данными полученными из JSON
                previousLocation.setLat((previousLocation.getLat() + currentLocation.getLat()) / 2);
                previousLocation.setLng((previousLocation.getLng() + currentLocation.getLng()) / 2);
                locationDAO.update(previousLocation);
            }
        }
        return null;
    }//данный метод реализует поведение для точки которая пришла в запрсое на location
    //или добавляет новую точку в бд или обновляет широту и долготу как среднее арифметическое
    public boolean checkingOnAvailabilityInDB(String key){
        return uniqueKeyDAO.existence(key) > 0;
    } //проверяет в БД существует ли запись по ключу, ели да то метод возвращает true
}
