angular.module('app')
    .controller('getEditData', function ($scope, $http) {
        $scope.onClickEditInformation = function () {
            var name = $scope.edit.name;
            var surname = $scope.edit.surname;
            var age = $scope.edit.age;
            $http.post("/editData", JSON.stringify({name: name, surname: surname, age: age}))
                .success(function () {
                    alert("Successful")
                })
                .error(function () {
                    alert("Wrong")
                })
        }
    });