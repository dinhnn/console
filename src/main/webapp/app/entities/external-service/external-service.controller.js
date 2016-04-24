(function() {
    'use strict';

    angular
        .module('consoleApp')
        .controller('ExternalServiceController', ExternalServiceController);

    ExternalServiceController.$inject = ['$scope', '$state', 'ExternalService', 'ExternalServiceSearch'];

    function ExternalServiceController ($scope, $state, ExternalService, ExternalServiceSearch) {
        var vm = this;
        vm.externalServices = [];
        vm.loadAll = function() {
            ExternalService.query(function(result) {
                vm.externalServices = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ExternalServiceSearch.query({query: vm.searchQuery}, function(result) {
                vm.externalServices = result;
            });
        };
        vm.loadAll();
        
    }
})();
