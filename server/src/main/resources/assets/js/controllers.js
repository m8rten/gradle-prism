'use strict';

var gradlePrismControllers = angular.module('gradlePrismControllers', []);

gradlePrismControllers.controller('QueryContainerCtrl',  ['$scope', 'Query', function QueryContainerCtrl($scope, Query) {

    $scope.queries = [];

    $scope.init = function(){
        $scope.queries = Query.query();
    };

    $scope.addQuery = function() {
        var query = new Query({mongoQuery: $scope.mongoQuery, name: "Test Query"});
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


gradlePrismControllers.controller('QueryCtrl', ['$scope', 'Query', 'Graph', function QueryCtrl($scope, Query, Graph) {

    $scope.editMode = false;

    $scope.init = function(query){
        $scope.query = query;
        $scope.invocations = Query.invocations({id: query.id});
        //$scope.graphs = Query.graphs({id: query.id});
        //console.log($scope.graphs);
        console.log($scope.invocations);
        console.log("youduderino")
        listenForUpdates();
    };

    $scope.toggleEditMode = function(){
        if($scope.editMode)
            $scope.editMode = false;
        else $scope.editMode = true;
    }

    $scope.saveQuery = function() {
        $scope.query.$save(function() {

        })
    };

    var listenForUpdates = function() {
        Query.waitUntilUpdated({id: $scope.query.id}, function(){
            $scope.invocations = Query.invocations({id: $scope.query.id});
            listenForUpdates()
        });
    };
}]);

gradlePrismControllers.controller('GraphCtrl', ['$scope', 'Graph', function GraphCtrl($scope, Graph) {
    $scope.init = function(graph){
        $scope.graph = graph;
        console.log(graph)
    }
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

//    $scope.status = function(){
//        console.log($scope.task);
//        if ($scope.task.success){
//            return "success"
//        } else {
//            return "fail"
//        }
//    }
}]);

