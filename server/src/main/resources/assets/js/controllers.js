'use strict';

var gradlePrismControllers = angular.module('gradlePrismControllers', []);

gradlePrismControllers.controller('QueryContainerCtrl',  ['$scope', 'Query', function QueryContainerCtrl($scope, Query) {

    $scope.queries = [];

    $scope.init = function(){
        $scope.queries = Query.query();
        listenForUpdates()
    };

    $scope.addQuery = function() {
        var query = new Query({mongoQuery: $scope.mongoQuery, name: $scope.name});
        query.$save(function() {
            $scope.queries.unshift(query);
        })
    };

    $scope.removeQuery = function(query) {
        Query.delete({id: query.id}, function(){
            var index = $scope.queries.indexOf(query);
            $scope.queries.splice(index, 1);
        })
    };

    var listenForUpdates = function() {
        Query.waitUntilUpdated(function(ids){
            ids.forEach(function(id){
                $scope.$broadcast("updateHasHappend", {id: id});
            });
            listenForUpdates();
        });
    };
}]);


gradlePrismControllers.controller('QueryCtrl', ['$scope','$timeout', 'Query', function QueryCtrl($scope, $timeout, Query) {

    $scope.editMode = false;

    $scope.showInvocations = false;

    $scope.init = function(query){
        $scope.query = query;
        $scope.id = query.id;
        $scope.invocations = query.invocations
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
        $scope.query.$save();
        $scope.toggleEditMode()
    };

    $scope.$on("updateHasHappend", function (event, args) {
        if($scope.query.id == args.id){
            Query.get({id: $scope.query.id}, function(query){

                /* update model */
                $scope.query = query;
                $scope.invocations = query.invocations

                /* Do animation */
                $scope.animation = 'flash-red';
                $timeout(function(){$scope.animation = ''}, 3000);
            });
        }
    });
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

