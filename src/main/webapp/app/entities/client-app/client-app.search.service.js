(function() {
    'use strict';

    angular
        .module('consoleApp')
        .factory('ClientAppSearch', ClientAppSearch);

    ClientAppSearch.$inject = ['$resource'];

    function ClientAppSearch($resource) {
        var resourceUrl =  'api/_search/client-apps/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
