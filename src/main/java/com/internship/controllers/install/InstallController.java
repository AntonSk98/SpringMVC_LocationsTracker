package com.internship.controllers.install;

import com.internship.models.uniqueKeyModel.UniqueKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class InstallController {
    @Autowired
    private InstallService installService;
    @RequestMapping(value = "/install", method = RequestMethod.POST)
    public ResponseEntity saveUniqueKey(@RequestBody String key){
        installService.writeKeyIntoDB(new UniqueKey(key));
        return new ResponseEntity(HttpStatus.OK); // если все успешно возвращаем код 200
    }//принимает ключ из запроса и передает его в
    // метод writeKeyIntoDB(описание к методу смотри в классе, реализующем бизнесс-логику контроллера)
}
