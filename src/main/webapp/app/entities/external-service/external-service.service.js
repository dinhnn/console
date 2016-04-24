(function() {
    'use strict';
    angular
        .module('consoleApp')
        .factory('ExternalService', ExternalService);

    ExternalService.$inject = ['$resource'];

    function ExternalService ($resource) {
        var resourceUrl =  'api/external-services/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
