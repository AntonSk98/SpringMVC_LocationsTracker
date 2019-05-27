package com.internship.controllers.install;

import com.internship.DAO.uniqueKeyDAO.UniqueKeyDAO;
import com.internship.models.uniqueKeyModel.UniqueKey;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class InstallService {
    @Autowired
    private UniqueKeyDAO uniquekeyDAO;
    void writeKeyIntoDB(UniqueKey uniquekey){
        uniquekeyDAO.insert(uniquekey);

    } //данный метод записывает значение ключа в БД при передачи ключа
}
