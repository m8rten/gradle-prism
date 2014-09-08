'use strict';

var gradlePrismServices = angular.module('gradlePrismServices', ['ngResource']);

gradlePrismServices.factory('Query', ['$resource',
    function($resource){
        return $resource(
            'api/query/:id/:action',
            {
                action: '',
                id: '@id'
            },{
                invocations : {
                    method:'GET',
                    isArray: true,
                    params: { action: 'invocations'}
                },
                waitUntilUpdated : {
                    method:'GET',
                    params: { action: 'waitUntilUpdated'}
                },
                delete : {
                    method:'DELETE'
                },
                get : {
                    method: 'GET'
                }
            }
        );
    }]);