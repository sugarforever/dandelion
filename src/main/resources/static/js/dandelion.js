var app = angular.module('app', ['ngAnimate', 'ngTouch', 'ui.grid']);

app.controller('MainCtrl', ['$scope', '$http', 'uiGridConstants', function ($scope, $http, uiGridConstants) {
    $scope.gridOptions = {
        enableSorting: false
    };

    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
    };

    $scope.load = function ($event, file) {
        $http.get('/api/datasource?name=' + file).success(function (data) {
            $scope.gridOptions.columnDefs = new Array();
            if (data.length > 0) {
                Object.keys(data[0]).forEach(function (k) {
                    $scope.gridOptions.columnDefs.push({
                        field: k
                    });
                });
            }
            $scope.gridOptions.data = data;
            //$scope.gridApi.grid.refresh();
        });
    };
}]);

// jQuery stuff