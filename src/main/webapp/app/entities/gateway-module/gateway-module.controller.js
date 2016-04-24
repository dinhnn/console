(function() {
    'use strict';

    angular
        .module('consoleApp')
        .controller('GatewayModuleController', GatewayModuleController);

    GatewayModuleController.$inject = ['$scope', '$state', 'GatewayModule', 'GatewayModuleSearch'];

    function GatewayModuleController ($scope, $state, GatewayModule, GatewayModuleSearch) {
        var vm = this;
        vm.gatewayModules = [];
        vm.loadAll = function() {
            GatewayModule.query(function(result) {
                vm.gatewayModules = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            GatewayModuleSearch.query({query: vm.searchQuery}, function(result) {
                vm.gatewayModules = result;
            });
        };
        vm.loadAll();
        
    }
})();
