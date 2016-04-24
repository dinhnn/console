(function() {
    'use strict';

    angular
        .module('consoleApp')
        .controller('ClientAppDialogController', ClientAppDialogController);

    ClientAppDialogController.$inject = ['$timeout', '$scope', '$stateParams',/* '$uibModalInstance',*/ 'entity', 'ClientApp', 'ServiceAccess'];

    function ClientAppDialogController ($timeout, $scope, $stateParams, /*$uibModalInstance,*/ entity, ClientApp, ServiceAccess) {
        var vm = this;
        vm.clientApp = entity;
        vm.clientAppFields = [
            {
      key: 'name',
      type: 'input',
      templateOptions: {
        type: 'text',
        label: 'Email address',
        placeholder: 'Enter email'
      }
    },
    {
      key: 'description',
      type: 'input',
      templateOptions: {
        type: 'password',
        label: 'Password',
        placeholder: 'Password'
      }
    },
    {
      key: 'contact',
      type: 'input',
      templateOptions: {
        type: 'password',
        label: 'Password',
        placeholder: 'Password'
      }
    }
	
	];
        vm.serviceaccesses = ServiceAccess.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('consoleApp:clientAppUpdate', result);
            //$uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.clientApp.id !== null) {
                ClientApp.update(vm.clientApp, onSaveSuccess, onSaveError);
            } else {
                ClientApp.save(vm.clientApp, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            //$uibModalInstance.dismiss('cancel');
        };
    }
})();
