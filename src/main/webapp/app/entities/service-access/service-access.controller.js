(function() {
    'use strict';

    angular
        .module('consoleApp')
        .controller('ServiceAccessController', ServiceAccessController);

    ServiceAccessController.$inject = ['$scope', '$state', 'ServiceAccess', 'ServiceAccessSearch'];

    function ServiceAccessController ($scope, $state, ServiceAccess, ServiceAccessSearch) {
        var vm = this;
        vm.serviceAccesses = [];
        vm.loadAll = function() {
            ServiceAccess.query(function(result) {
                vm.serviceAccesses = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ServiceAccessSearch.query({query: vm.searchQuery}, function(result) {
                vm.serviceAccesses = result;
            });
        };
        vm.loadAll();
        
    }
})();
