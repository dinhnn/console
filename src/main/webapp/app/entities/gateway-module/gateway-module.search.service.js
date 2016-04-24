(function() {
    'use strict';

    angular
        .module('consoleApp')
        .factory('GatewayModuleSearch', GatewayModuleSearch);

    GatewayModuleSearch.$inject = ['$resource'];

    function GatewayModuleSearch($resource) {
        var resourceUrl =  'api/_search/gateway-modules/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
