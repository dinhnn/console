(function() {
    'use strict';

    angular
        .module('consoleApp')
        .controller('ExternalServiceDetailController', ExternalServiceDetailController);

    ExternalServiceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ExternalService', 'GatewayModule'];

    function ExternalServiceDetailController($scope, $rootScope, $stateParams, entity, ExternalService, GatewayModule) {
        var vm = this;
        vm.externalService = entity;
        
        var unsubscribe = $rootScope.$on('consoleApp:externalServiceUpdate', function(event, result) {
            vm.externalService = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
