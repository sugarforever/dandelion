var app = angular.module('app', ['ngAnimate', 'ngTouch', 'ui.grid']);

app.controller('data-sources-controller', ['$scope', '$http', 'uiGridConstants', function ($scope, $http, uiGridConstants) {
    $scope.gridOptions = {
        enableSorting: false
    };

    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
    };

    $scope.load = function ($event) {
        var file = $event.currentTarget.getAttribute('data-file');
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

// DropZone options
Dropzone.options.fileUploadDropzone = {
    success: function(response) {
        var fileName = response.name;
        var $dataSource = $('<div class="data-source"><a href="javascript:void(0)" id="button_add" class="btn" data-file="' + fileName + '" ng-click="load($event)">' + fileName + '</a></div>');
        $dataSource.appendTo($(".data-sources"));
    }
};