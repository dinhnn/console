(function() {
    'use strict';

    angular
        .module('consoleApp')
        .controller('GatewayModuleDetailController', GatewayModuleDetailController);

    GatewayModuleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'GatewayModule'];

    function GatewayModuleDetailController($scope, $rootScope, $stateParams, entity, GatewayModule) {
        var vm = this;
        vm.gatewayModule = entity;
        
        var unsubscribe = $rootScope.$on('consoleApp:gatewayModuleUpdate', function(event, result) {
            vm.gatewayModule = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
