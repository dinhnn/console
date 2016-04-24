(function() {
    'use strict';
    angular
        .module('consoleApp')
        .factory('ServiceAccess', ServiceAccess);

    ServiceAccess.$inject = ['$resource'];

    function ServiceAccess ($resource) {
        var resourceUrl =  'api/service-accesses/:id';

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
