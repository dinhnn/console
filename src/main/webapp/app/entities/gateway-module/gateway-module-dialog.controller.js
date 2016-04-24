(function() {
    'use strict';

    angular
        .module('consoleApp')
        .controller('GatewayModuleDialogController', GatewayModuleDialogController);

    GatewayModuleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'GatewayModule'];

    function GatewayModuleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, GatewayModule) {
        var vm = this;
        vm.gatewayModule = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('consoleApp:gatewayModuleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.gatewayModule.id !== null) {
                GatewayModule.update(vm.gatewayModule, onSaveSuccess, onSaveError);
            } else {
                GatewayModule.save(vm.gatewayModule, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
