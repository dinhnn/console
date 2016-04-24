'use strict';

describe('Controller Tests', function() {

    describe('ExternalService Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockExternalService, MockGatewayModule;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockExternalService = jasmine.createSpy('MockExternalService');
            MockGatewayModule = jasmine.createSpy('MockGatewayModule');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ExternalService': MockExternalService,
                'GatewayModule': MockGatewayModule
            };
            createController = function() {
                $injector.get('$controller')("ExternalServiceDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'consoleApp:externalServiceUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
