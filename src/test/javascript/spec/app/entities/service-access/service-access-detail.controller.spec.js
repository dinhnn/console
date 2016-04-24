'use strict';

describe('Controller Tests', function() {

    describe('ServiceAccess Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockServiceAccess, MockClientApp, MockExternalService;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockServiceAccess = jasmine.createSpy('MockServiceAccess');
            MockClientApp = jasmine.createSpy('MockClientApp');
            MockExternalService = jasmine.createSpy('MockExternalService');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ServiceAccess': MockServiceAccess,
                'ClientApp': MockClientApp,
                'ExternalService': MockExternalService
            };
            createController = function() {
                $injector.get('$controller')("ServiceAccessDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'consoleApp:serviceAccessUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
