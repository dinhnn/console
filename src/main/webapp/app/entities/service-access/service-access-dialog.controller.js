(function() {
    'use strict';

    angular
        .module('consoleApp')
        .controller('ServiceAccessDialogController', ServiceAccessDialogController);

    ServiceAccessDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ServiceAccess', 'ClientApp', 'ExternalService'];

    function ServiceAccessDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ServiceAccess, ClientApp, ExternalService) {
        var vm = this;
        vm.serviceAccess = entity;
        vm.clientapps = ClientApp.query();
        vm.externalservices = ExternalService.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('consoleApp:serviceAccessUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.serviceAccess.id !== null) {
                ServiceAccess.update(vm.serviceAccess, onSaveSuccess, onSaveError);
            } else {
                ServiceAccess.save(vm.serviceAccess, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
