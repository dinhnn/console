(function() {
    'use strict';
    angular
        .module('consoleApp')
        .factory('ClientApp', ClientApp);

    ClientApp.$inject = ['$resource'];

    function ClientApp ($resource) {
        var resourceUrl =  'api/client-apps/:id';

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
