angular.module('myApp.services', ['ngResource'])
    .factory('AngularIssues', function($resource){
        return $resource('https://api.github.com/repos/angular/angular.js/issues', {})
    })
    .value('version', '0.1');



angular.module('myApp.controllers', []).
    controller('MyCtrl1', ['$scope', 'AngularIssues', function($scope, AngularIssues) {
        // Instantiate an object to store your scope data in (Best Practices)
        $scope.data = {};

        AngularIssues.query(function(response) {
            // Assign the response INSIDE the callback
            $scope.data.issues = response;
        });
    }])
    .controller('MyCtrl2', [function() {
    }]);



function QueryContainerCtrl($scope) {
    $scope.queries = [
        {uuid:'learn angular'},
        {uuid:'build an angular app'}];
};
function QueryCtrl($scope) {
    $scope.queries = [
        {uuid:'learn angular'},
        {text:'build an angular app'}];
}
function TodoCtrl2($scope) {
    $scope.todos = [
        {text:'learn angular', done:true},
        {text:'build an angular app', done:false}];

    $scope.addTodo = function() {
        $scope.todos.push({text:$scope.todoText, done:false});
        $scope.todoText = '';
    };

    $scope.remaining = function() {
        var count = 0;
        angular.forEach($scope.todos, function(todo) {
            count += todo.done ? 0 : 1;
        });
        return count;
    };

    $scope.archive = function() {
        var oldTodos = $scope.todos;
        $scope.todos = [];
        angular.forEach(oldTodos, function(todo) {
            if (!todo.done) $scope.todos.push(todo);
        });
    };
};