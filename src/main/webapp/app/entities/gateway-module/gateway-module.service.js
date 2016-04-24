(function() {
    'use strict';
    angular
        .module('consoleApp')
        .factory('GatewayModule', GatewayModule);

    GatewayModule.$inject = ['$resource'];

    function GatewayModule ($resource) {
        var resourceUrl =  'api/gateway-modules/:id';

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
