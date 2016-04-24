(function() {
    'use strict';

    angular
        .module('consoleApp')
        .controller('ServiceAccessDetailController', ServiceAccessDetailController);

    ServiceAccessDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ServiceAccess', 'ClientApp', 'ExternalService'];

    function ServiceAccessDetailController($scope, $rootScope, $stateParams, entity, ServiceAccess, ClientApp, ExternalService) {
        var vm = this;
        vm.serviceAccess = entity;
        
        var unsubscribe = $rootScope.$on('consoleApp:serviceAccessUpdate', function(event, result) {
            vm.serviceAccess = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
