<!DOCTYPE html>
<html ng-app="app">
<head>
    <title>Dandelion Data Cleansing</title>
    <link rel="styleSheet" href="/css/ui-grid/ui-grid.min.css"/>
    <link rel="styleSheet" href="/css/dandelion.css"/>
    <link rel="styleSheet" href="/css/dropzone.css"/>
    <link rel="styleSheet" href="/css/jquery.fileuploader.css"/>
    <script src="/js/angularjs/1.5.0/angular.js"></script>
    <script src="/js/angularjs/1.5.0/angular-touch.js"></script>
    <script src="/js/angularjs/1.5.0/angular-animate.js"></script>
    <script src="/js/ui-grid.min.js"></script>
    <script src="/js/jquery-3.1.1.min.js"></script>
    <script src="/js/dropzone.js"></script>
    <script src="/js/dandelion.js"></script>
</head>
<body>
<form action="/upload.html" class="dropzone" id="my-awesome-dropzone"></form>

<div class="data-sources" ng-controller="MainCtrl">
<#list Request.FileNames as fileName>
    <div class="data-source">
        <button id="button_add" class="btn" data-file="${fileName}"
                ng-click="load($event, '${fileName}')">${fileName}</button>
    </div>
</#list>

    <div id="grid1" ui-grid="gridOptions" class="grid"></div>
</div>
</body>
</html>

