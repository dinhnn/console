(function() {
    'use strict';

    angular
        .module('consoleApp')
        .factory('ExternalServiceSearch', ExternalServiceSearch);

    ExternalServiceSearch.$inject = ['$resource'];

    function ExternalServiceSearch($resource) {
        var resourceUrl =  'api/_search/external-services/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
