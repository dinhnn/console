(function() {
    'use strict';

    angular
        .module('consoleApp')
        .controller('ClientAppController', ClientAppController);

    ClientAppController.$inject = ['$scope', '$state', 'ClientApp', 'ClientAppSearch'];

    function ClientAppController ($scope, $state, ClientApp, ClientAppSearch) {
        var vm = this;
        vm.clientApps = [];
        vm.loadAll = function() {
            ClientApp.query(function(result) {
                vm.clientApps = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ClientAppSearch.query({query: vm.searchQuery}, function(result) {
                vm.clientApps = result;
            });
        };
        vm.loadAll();
        
    }
})();
