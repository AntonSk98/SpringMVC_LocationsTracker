package com.internship.controllers.viewControllers;


        import com.internship.DAO.UsersDAO.AppUserDAO;
        import com.internship.DAO.locationDAO.LocationDAO;
        import com.internship.DAO.userInfoDAO.UserInfoDAO;
        import com.internship.models.locationModel.Location;
        import com.internship.models.locationModel.LocationsAndDefaultKey;
        import com.internship.models.userInfoModel.UserInfo;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Controller;
        import org.springframework.web.bind.annotation.*;

        import javax.servlet.http.HttpServletRequest;
        import java.util.List;


@Controller
public class Authorize {

    @Autowired
    private UserInfoDAO userInfoDAO;
    @Autowired
    private AppUserDAO appUserDAO;
    @Autowired
    private LocationDAO locationDAO;
    @RequestMapping(value = "ui/authorize")
    public String authorizePage() {
        return "authorize";
    }

    @RequestMapping(value = "ui/main")
    public String mainPage() {
        return "main";
    }

    @RequestMapping(value = "ui/403Page")
    public String errorPage(){return "403Page";}

    @ResponseBody
    @RequestMapping(value="/user", method = RequestMethod.GET,  produces = "application/json")
    public UserInfo submitForm(HttpServletRequest request){
        String name = request.getUserPrincipal().getName(); //the first way to get a login
       // String name1 = SecurityContextHolder.getContext().getAuthentication().getName(); //another way to get a login
        return userInfoDAO.findByLogin(name);
    }
    @ResponseBody
    @RequestMapping(value="/points", method = RequestMethod.GET,  produces = "application/json")
    public List<Location> returnLocations(HttpServletRequest request){
        String name = request.getUserPrincipal().getName();
        return locationDAO.getAllLocationsById(appUserDAO.getIdByLogin(name));
    }
    @ResponseBody
    @RequestMapping(value="/user/keys", method = RequestMethod.GET) //принимает запрос от клиента и возвращает ответ в виде массива id принадлужащий залогиненному юзеру
    public List<Integer> returnAvailableKeys(HttpServletRequest request){
        String name = request.getUserPrincipal().getName();
        return locationDAO.getKeysByUserId(appUserDAO.getIdByLogin(name));
    }
    @ResponseBody
    @RequestMapping(value="/getCoordinates", method = RequestMethod.GET) //принимает запрос от клиента и возвращает ответ в виде списка локаций доступных по данному айди ключа
    public List<Location> getCoordinates(@RequestParam("key") Integer key){
        return locationDAO.getLocationsByKeyId(key);
    }
    @ResponseBody
    @RequestMapping(value = "/limitPoints", method = RequestMethod.GET)
    public List<Location> getLimitedPoints(@RequestParam("key") Integer key, @RequestParam(required = false) Integer number){ //выводит точки по ключу
        if(number==null)
            return locationDAO.getLocationsByKeyId(key);
        return locationDAO.getLocationsByKeyIdLimit(key,number);
    }
    @ResponseBody
    @RequestMapping(value = "/locations/default", method = RequestMethod.GET) //выводит значения по умолчанию
    public LocationsAndDefaultKey getDefaultPoints(HttpServletRequest request){
        String name = request.getUserPrincipal().getName();
        int defaultkey = locationDAO.getDefaultKeyByUsername(name);
        int defaultValue = locationDAO.setDefaultValueOfpointsByUsername(name);
        LocationsAndDefaultKey locationsAndDefaultKey = new LocationsAndDefaultKey();
        locationsAndDefaultKey.setLocations(locationDAO.getLocationsByKeyIdLimit(defaultkey,defaultValue));
        locationsAndDefaultKey.setDefaultkey(defaultkey);
        return locationsAndDefaultKey;

    }
    @ResponseBody
    @RequestMapping(value = "/editData", method = RequestMethod.POST, consumes = "application/json") //метод принимает в теле объект содержащий инфо о пользователе и обновляет данные пользователя
    public boolean editData(HttpServletRequest request, @RequestBody UserInfo userInfo){
        String login = request.getUserPrincipal().getName();
        userInfoDAO.updateByLogin(login, userInfo);
        return true;
    }
}