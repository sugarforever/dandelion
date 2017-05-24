<!DOCTYPE html>
<html ng-app="app">
<head>
    <title>Dandelion Data Cleansing</title>
    <link rel="styleSheet" href="https://cdn.rawgit.com/angular-ui/bower-ui-grid/master/ui-grid.min.css"/>
    <link rel="styleSheet" href="/css/dandelion.css"/>
    <link rel="styleSheet" href="/css/dropzone.css"/>
    <link rel="styleSheet" href="/css/jquery.fileuploader.css"/>
    <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.5.0/angular.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.5.0/angular-touch.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.5.0/angular-animate.js"></script>
    <script src="https://cdn.rawgit.com/angular-ui/bower-ui-grid/master/ui-grid.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
    <script src="/js/dropzone.js"></script>

</head>
<body>
<form action="/upload.html" class="dropzone" id="my-awesome-dropzone">

</form>

<div class="data-sources" ng-controller="MainCtrl">
<#list Request.FileNames as fileName>
    <div class="data-source">
        <button id="button_add" class="btn" data-file="${fileName}"
                ng-click="load($event, '${fileName}')">${fileName}</button>
    </div>
</#list>

    <div id="grid1" ui-grid="gridOptions" class="grid"></div>
</div>

<script src="/js/dandelion.js"></script>
</body>
</html>

