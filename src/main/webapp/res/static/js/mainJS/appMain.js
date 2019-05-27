var app = angular.module('app', ['ngRoute']);
app.controller('getDataController', function ($scope, $http, $interval) {
    var socket = new SockJS('/stomp'); //регистрируем сокет
    var stompClient = Stomp.over(socket); //
    $scope.keyActive = {};
    stompClient.connect({}, function () {
        stompClient.subscribe('topic.key', function (key) { //отслеживает появление новой локации, при появлении создает объект с ключом key, ключ key содержит флаг и прошедшее время с последней пройденной координаты
            $scope.$apply(function () {
                $scope.keyActive[key.body]={sentTime: Date.now(), active: true, passedTime: 0};
            })
        })
    });
    $scope.passedTime = 0;
    var locationsOnMap = [];
    var route = [];
    var markers = [];
    var subscribers = [];
    var map = new google.maps.Map(document.getElementById('map'), {
        zoom: 3,
        center: {lat: -28.024, lng: 140.887}
    }); //инициализируем мапу, ставим зум и координату по умолчанию

    $interval(checkOnActiveDeviceOrNot, 7000); //каждые 7 секунд проверяет пришла ли локация или нет, если нет то выставляем статус false
    function checkOnActiveDeviceOrNot() {
        $scope.user.keys.forEach(function (key) {
            if ($scope.keyActive[key]!=null){
                if(Date.now()-$scope.keyActive[key].sentTime>10000){
                    $scope.keyActive[key].active = false;
                }
            }
        })
    }
    $interval(periodOfTime, 1000); //каждую секунду проверяет - если нет оъекта по ключу или если объект по ключу не активен, то увеличиваем поле passed time у данного объекта
    function periodOfTime() {
        $scope.user.keys.forEach(function (key) {
            if($scope.keyActive[key]!=null && !$scope.keyActive[key].active){
                $scope.keyActive[key].passedTime = $scope.keyActive[key].passedTime + 1000;
            }
        })
    }

    $http.get("/locations/default") //отображение точек по умолчанию
        .then(function (points) {
            drawRoute(points.data.locations);
            drawLocations(points.data.locations, points.data.defaultkey);
        });

    $http.get("/user") //заполняем страничку данными о пользователе
        .then(function (response) {
            $scope.user = {
                login: response.data.login,
                name: response.data.name,
                surname: response.data.surname,
                age: response.data.age
            }
        });

    //при каждом нажатии на кнопку отправляем запрос для отображения координат соотв. ключа. При нажатии на кнопку закрытие старых соединений, открытие нового соединения
    $scope.onClickUserKey = function (key) {
        $scope.lastSelectedKey = key; //заносим в переменную значение активный ключ
        $http.get("/limitPoints?key=" + $scope.lastSelectedKey)
            .then(function (locations) {
                deleteRouteAndMarkers();
                redrawRoute(locations.data, undefined, $scope.lastSelectedKey);
            });

        if (subscribers.length > 0) { //при каждой смене состояния кнопки необходимо закрыть открытое соединение
            for (var i = 0; i < subscribers.length; i++) {
                subscribers[i].unsubscribe(i);
            }
            subscribers = [];
        }
        if (stompClient.connected) { //если мы законектились то подписывается на topic.locations. и слушаем все изменения
            var subscriber = stompClient.subscribe('topic.locations.' + key, function (locations) { //каждая нажатая кнопка является подписчиком
                redrawRoute([JSON.parse(locations.body)], $scope.maxLocationCount);
            });
            subscribers.push(subscriber);
        }
    };

    //при поступлении данных с устройства проверяем корректность отображения данных (ориентируемся на input). При подписке добавляем подписчика к массиву подписок
    $scope.onChangeMaxLocationsCount = function (maxLocationsCount) { //слушаем все изменения инпута и при каждом изменении отсылаем запрос на лимит отображаемых точек, отображаем мапу
        $scope.maxLocationCount = maxLocationsCount;
        if (!maxLocationsCount)
            return;
        if (!$scope.lastSelectedKey)
            return;
        $http.get("/limitPoints?key=" + $scope.lastSelectedKey + "&number=" + (maxLocationsCount))
            .then(function (locations) {
                locationsOnMap = locations.data;
                redrawRoute(locations.data, undefined, $scope.lastSelectedKey);
            });
    };//проверяет изменения в input,при каждом изменении поля обращается к серверу для повторного отображения


    function redrawRoute(locations, maxLocationCount, key) { //функия принимает get запрос на сервер, данны с input для отображения и ключ для подписи к маркеру и затем выводит резьтаты на мап
        if (maxLocationCount <= 0) {
            deleteRouteAndMarkers();
        }
        else if (maxLocationCount > 0) {
            if (maxLocationCount > locationsOnMap.length) {
                deleteRouteAndMarkers();
                locationsOnMap.push(locations[0]);
                drawLocations([locationsOnMap[0], locationsOnMap[locationsOnMap.length - 1]], key);
                drawRoute(locationsOnMap);
            } else {
                deleteRouteAndMarkers();
                while (locationsOnMap.length >= maxLocationCount) {
                    locationsOnMap.shift();
                }
                locationsOnMap.push(locations[0]);
                drawLocations([locationsOnMap[0], locationsOnMap[locationsOnMap.length - 1]], key);
                drawRoute(locationsOnMap);
            }
        }
        else {
            deleteRouteAndMarkers();
            drawRoute(locations);
            drawLocations(locations, key);
        }
    }//функия принимает get запрос на сервер и затем выводит резьтаты на мапу

    function drawLocations(locations, key) { //рисует локации - начало и конец пути
        if (locations.length > 1) {
            markers.push(new google.maps.Marker({
                position: locations[0],
                map: map
            }));
            markers.push(new google.maps.Marker({
                position: locations[locations.length - 1],
                map: map
            }));
        } else {
            markers.push(new google.maps.Marker({
                position: locations[0],
                map: map
            }));
        }
        markers.forEach(function (marker) {
            marker.addListener('click', function () {
                new google.maps.InfoWindow({
                    content: key.toString()
                }).open(map,marker);
            })
        });
    } //функция принимающая массиив локаций, обновляет массив маркеров новыми локациями
    function drawRoute(locations) { //рисует маршрут
        route.push(new google.maps.Polyline({
            path: locations,
            geodesic: true,
            strokeColor: '#FF0000',
            strokeOpacity: 1.0,
            strokeWeight: 2,
            map: map
        }))
    }

    function deleteRouteAndMarkers() {
        for (var i = 0; i < route.length; i++) {
            route[i].setMap(null);
        }
        for (var i = 0; i < markers.length; i++) { //проходим по всем макерам и устанавлием им map = null
            markers[i].setMap(null);
        }
        markers = [];
        route = [];
    }
    $http.get("/user/keys") //получаем массив ключей для отображения кнопки для каждого элемента из массива
        .then(function (keys) {
            $scope.user.keys = keys.data;
        });

});
