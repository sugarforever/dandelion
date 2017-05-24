<!DOCTYPE html>
<html ng-app="app">
<head>
    <title>Dandelion Data Cleansing</title>
    <link rel="styleSheet" href="/ui-grid/4.0.4/ui-grid.min.css"/>
    <link rel="styleSheet" href="/css/dandelion.css"/>
    <link rel="styleSheet" href="/css/dropzone.css"/>
    <link rel="styleSheet" href="/css/jquery.fileuploader.css"/>
    <script src="/js/angularjs/1.5.0/angular.js"></script>
    <script src="/js/angularjs/1.5.0/angular-touch.js"></script>
    <script src="/js/angularjs/1.5.0/angular-animate.js"></script>
    <script src="/ui-grid/4.0.4/ui-grid.min.js"></script>
    <script src="/js/jquery-3.1.1.min.js"></script>
    <script src="/js/dropzone.js"></script>
    <script src="/js/dandelion.js"></script>
</head>
<body>
<form action="/upload.html" class="dropzone" id="file-upload-dropzone"></form>

<div class="file-uploader" ng-controller="data-sources-controller">
    <div class="data-sources">
    <#list Request.FileNames as fileName>
        <div class="data-source">
            <a href="javascript:void(0)" id="button_add" class="source-link" data-file="${fileName}" ng-click="load($event)">${fileName}</a>
        </div>
    </#list>
    </div>
    <div id="grid1" ui-grid="gridOptions" class="grid"></div>
</div>
</body>
</html>

