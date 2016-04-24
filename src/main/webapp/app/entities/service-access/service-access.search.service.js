(function() {
    'use strict';

    angular
        .module('consoleApp')
        .factory('ServiceAccessSearch', ServiceAccessSearch);

    ServiceAccessSearch.$inject = ['$resource'];

    function ServiceAccessSearch($resource) {
        var resourceUrl =  'api/_search/service-accesses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
