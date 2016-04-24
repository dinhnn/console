(function() {
    'use strict';

    angular
        .module('consoleApp')
        .controller('ExternalServiceDeleteController',ExternalServiceDeleteController);

    ExternalServiceDeleteController.$inject = ['$uibModalInstance', 'entity', 'ExternalService'];

    function ExternalServiceDeleteController($uibModalInstance, entity, ExternalService) {
        var vm = this;
        vm.externalService = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            ExternalService.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
