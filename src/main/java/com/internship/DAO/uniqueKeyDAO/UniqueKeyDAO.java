package com.internship.DAO.uniqueKeyDAO;

import com.internship.models.uniqueKeyModel.UniqueKey;

public interface UniqueKeyDAO{
    boolean insert(UniqueKey uniqueKey);
    Integer existence(String key);
    int getIdByKey(String key);
}
