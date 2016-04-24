'use strict';

describe('Controller Tests', function() {

    describe('ClientApp Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockClientApp, MockServiceAccess;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockClientApp = jasmine.createSpy('MockClientApp');
            MockServiceAccess = jasmine.createSpy('MockServiceAccess');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ClientApp': MockClientApp,
                'ServiceAccess': MockServiceAccess
            };
            createController = function() {
                $injector.get('$controller')("ClientAppDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'consoleApp:clientAppUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
