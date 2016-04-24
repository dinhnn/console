(function() {
    'use strict';

    angular
        .module('consoleApp')
        .controller('ExternalServiceDialogController', ExternalServiceDialogController);

    ExternalServiceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ExternalService', 'GatewayModule'];

    function ExternalServiceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ExternalService, GatewayModule) {
        var vm = this;
        vm.externalService = entity;
        vm.gatewaymodules = GatewayModule.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('consoleApp:externalServiceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.externalService.id !== null) {
                ExternalService.update(vm.externalService, onSaveSuccess, onSaveError);
            } else {
                ExternalService.save(vm.externalService, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
