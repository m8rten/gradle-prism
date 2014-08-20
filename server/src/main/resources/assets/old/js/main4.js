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