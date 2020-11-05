(function(window, angular, undefined){
                'use strict';
                angular.module('TestApp', [

                ]);
                angular.module('TestApp').controller('testController', testController);
                function testController($scope, $window){
                                $scope.employees = [{"id":"1","employee_name":"Tiger Nixon","employee_salary":"320800","employee_age":"61"},{"id":"2","employee_name":"Garrett Winters","employee_salary":"434343","employee_age":"63"},{"id":"3","employee_name":"Ashton Cox","employee_salary":"86000","employee_age":"66"},{"id":"4","employee_name":"Cedric Kelly","employee_salary":"433060","employee_age":"22"},{"id":"5","employee_name":"Airi Satou","employee_salary":"162700","employee_age":"33"}];
                                console.log($scope.employees);
                                $scope.addEmployee = function () {
                                                //Add the new item to the Array.
            var employee = {
                id: $scope.employees.length+1,
                employee_name : $scope.name,
                employee_age : $scope.age,
                employee_salary : $scope.salary
            };

            $scope.employees.push(employee);
            $scope.name = '';
            $scope.age = '';
            $scope.salary = '';
                                }
                                $scope.removeRow = function (index) {
            //Find the record using Index from Array.
            var name = $scope.employees[index].employee_name;
            if ($window.confirm("Do you want to delete: " + name)) {
                //Remove the item from Array using Index.
                $scope.employees.splice(index, 1);
            }
        }
                }


})(window, window.angular);
