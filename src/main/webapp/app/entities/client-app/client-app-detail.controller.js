(function() {
    'use strict';

    angular
        .module('consoleApp')
        .controller('ClientAppDetailController', ClientAppDetailController);

    ClientAppDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ClientApp', 'ServiceAccess'];

    function ClientAppDetailController($scope, $rootScope, $stateParams, entity, ClientApp, ServiceAccess) {
        var vm = this;
        vm.clientApp = entity;
        
        var unsubscribe = $rootScope.$on('consoleApp:clientAppUpdate', function(event, result) {
            vm.clientApp = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
