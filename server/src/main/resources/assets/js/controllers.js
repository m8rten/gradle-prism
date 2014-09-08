'use strict';

var gradlePrismControllers = angular.module('gradlePrismControllers', []);

gradlePrismControllers.controller('QueryContainerCtrl',  ['$scope', 'Query', function QueryContainerCtrl($scope, Query) {

    $scope.queries = [];

    $scope.init = function(){
        $scope.queries = Query.query();
    };

    $scope.addQuery = function() {
        var query = new Query({mongoQuery: $scope.mongoQuery, name: $scope.name});
        query.$save(function() {
            $scope.queries.push(query);
        })
    };

    $scope.removeQuery = function(query) {
        /* Clean up this mess, it should be without messag*/
//        query.$delete(function(){
        Query.delete({id: query.id}, function(){
            var index = $scope.queries.indexOf(query);
            $scope.queries.splice(index, 1);
        })
    };
}]);


gradlePrismControllers.controller('QueryCtrl', ['$scope', 'Query', function QueryCtrl($scope, Query) {

    $scope.editMode = false;

    $scope.showInvocations = false;

    $scope.init = function(query){
        $scope.query = query;
        $scope.id = query.id;
        $scope.invocations = query.invocations
        listenForUpdates();
    };

    $scope.toggleEditMode = function(){
        if($scope.editMode)
            $scope.editMode = false;
        else $scope.editMode = true;
    }

    $scope.toggleShowInvocations = function(){
        if($scope.showInvocations)
            $scope.showInvocations = false;
        else $scope.showInvocations = true;
    }

    $scope.saveQuery = function() {

        console.log("saving...");

        $scope.query.$save(function() {
            console.log("saved!");
        })
        $scope.toggleEditMode()
    };

    var listenForUpdates = function() {
        Query.waitUntilUpdated({id: $scope.query.id}, function(){
            $scope.query = Query.get({id: $scope.query.id}, function(){
                console.log($scope.query)
                listenForUpdates()
            });
        });
    };
}]);

gradlePrismControllers.controller('InvocationCtrl', ['$scope', function InvocationCtrl($scope) {
    $scope.init = function(invocation){
        $scope.invocation = invocation;
    }

    $scope.time = function(){
        return new Date($scope.invocation.time).toLocaleString()
    }
}]);

gradlePrismControllers.controller('TaskCtrl', ['$scope', function TaskCtrl($scope) {
    $scope.init = function(task){
        $scope.task = task;
    }
}]);

