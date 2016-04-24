(function() {
    'use strict';

    angular
        .module('consoleApp')
        .controller('GatewayModuleDeleteController',GatewayModuleDeleteController);

    GatewayModuleDeleteController.$inject = ['$uibModalInstance', 'entity', 'GatewayModule'];

    function GatewayModuleDeleteController($uibModalInstance, entity, GatewayModule) {
        var vm = this;
        vm.gatewayModule = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            GatewayModule.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
