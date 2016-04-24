(function() {
    'use strict';

    angular
        .module('consoleApp')
        .controller('ClientAppDeleteController',ClientAppDeleteController);

    ClientAppDeleteController.$inject = ['$uibModalInstance', 'entity', 'ClientApp'];

    function ClientAppDeleteController($uibModalInstance, entity, ClientApp) {
        var vm = this;
        vm.clientApp = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            ClientApp.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
