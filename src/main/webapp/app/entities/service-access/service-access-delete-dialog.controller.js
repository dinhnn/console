(function() {
    'use strict';

    angular
        .module('consoleApp')
        .controller('ServiceAccessDeleteController',ServiceAccessDeleteController);

    ServiceAccessDeleteController.$inject = ['$uibModalInstance', 'entity', 'ServiceAccess'];

    function ServiceAccessDeleteController($uibModalInstance, entity, ServiceAccess) {
        var vm = this;
        vm.serviceAccess = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            ServiceAccess.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
