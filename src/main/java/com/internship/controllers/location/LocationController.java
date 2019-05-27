package com.internship.controllers.location;
import com.internship.DAO.uniqueKeyDAO.UniqueKeyDAO;
import com.internship.models.locationModel.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.util.Map;


@SuppressWarnings("SpringJavaAutowiringInspection")
@Controller
public class LocationController {

    @Autowired
    private LocationService locationService;
    @Autowired
    private UniqueKeyDAO uniqueKeyDAO;

    @Autowired
    private SimpMessagingTemplate sendDataToSubscribers;

    @ResponseBody
    @RequestMapping(value = "/location", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity saveLocation(@RequestHeader Map<String,String> header, @RequestBody Location newLocation) throws ParseException {
        int id_key = uniqueKeyDAO.getIdByKey(header.get("key")); //получаем id ключ с использованием ключа переданного в header
        Location possibleLocation = locationService.EmergingOrAdding(newLocation, id_key);
        sendDataToSubscribers.convertAndSend("topic.key", id_key);
        if(possibleLocation!=null){
            Location locationNotTime = possibleLocation;
            locationNotTime.setNullTime();
            this.sendDataToSubscribers.convertAndSend("topic.locations."+id_key, locationNotTime);
        }


        return new ResponseEntity(HttpStatus.OK); // если все успешно возвращаем код 200
    }//данный метод
    //обрабатывает url = location, принимает json параметр, отправляет также json параметр
    //внутри метода получаем id уникального ключа из дб по значению ключа переданного в запросе, проверяем можно ли
    //если выполняется условие что рассояние больше 5 м и время больше 5 минут то отсылаем даннные по topic.locations. где их примет и обработает подписчик
}
