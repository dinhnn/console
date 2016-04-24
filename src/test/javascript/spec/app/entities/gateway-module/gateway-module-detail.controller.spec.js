'use strict';

describe('Controller Tests', function() {

    describe('GatewayModule Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockGatewayModule;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockGatewayModule = jasmine.createSpy('MockGatewayModule');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'GatewayModule': MockGatewayModule
            };
            createController = function() {
                $injector.get('$controller')("GatewayModuleDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'consoleApp:gatewayModuleUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
