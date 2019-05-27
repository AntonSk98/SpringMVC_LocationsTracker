package com.internship.controllers.location;

import com.internship.DAO.locationDAO.LocationDAO;
import com.internship.models.locationModel.Location;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Random;

@Service
@ConditionalOnProperty(value = "config", havingValue = "beanSimulator") //Conditional that checks if the specified properties have a specific value.
// By default the properties must be present in the Environment and not equal to false.
public class BeanSimulator implements InitializingBean {
    private Location simulatedLocation;
    @Autowired
    LocationDAO locationDAO;
    @Override
    public void afterPropertiesSet() {
        final Thread myThready = new Thread(new Runnable()
        {
            public void run() //Этот метод будет выполняться в побочном потоке
            {
                while (true){
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        return;
                    }
                    try {
                        generateLocations();
                        //locationController.saveLocation(Collections.singletonMap("key", "sunnyDay"), simulatedLocation); //симулируем ситуацю отправления точки на /location
                        //locationController.saveLocation(Collections.singletonMap("key", "2"), simulatedLocation);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        myThready.start();
    }//после того как установлены параметры отправляем запрос на /location со случайно сгенерированной координатой
    private Location generateLocations(){
        Random rand = new Random();
        simulatedLocation = new Location();
        double angle;
        double metres;
        metres = rand.nextDouble()*10;
        angle = rand.nextInt(361);
        double goneYmetres = metres * Math.sin(angle);
        double goneXmetres = metres * Math.cos(angle);
        double lastLat = locationDAO.getLast(3).getLat();
        double lastLng = locationDAO.getLast(3).getLng();
        simulatedLocation.setLat(lastLat+(goneYmetres/(Math.cos(lastLat)*111111)));
        simulatedLocation.setLng(lastLng+(goneXmetres/(Math.cos(lastLng)*111111)));
        simulatedLocation.setTime(LocalDateTime.now().plusMinutes(rand.nextInt(500)).toString());
        return simulatedLocation;
    }//генерируем точку
    public Location getLocation() {
        return simulatedLocation;
    }
}
