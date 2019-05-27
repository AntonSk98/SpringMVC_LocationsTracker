angular.module('app')
    .config(function mctRouter($routeProvider) {
        $routeProvider
            .when("/", {
                templateUrl: "/static/views/content.html",
                controller: 'getDataController'
            })
            .when("/edit", {
                templateUrl: "/static/views/editData.html",
                controller: 'getEditData'
            })

    });
