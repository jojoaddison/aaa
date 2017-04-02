'use strict';

describe('Controller Tests', function() {

    describe('FileDocument Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockFileDocument;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockFileDocument = jasmine.createSpy('MockFileDocument');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'FileDocument': MockFileDocument
            };
            createController = function() {
                $injector.get('$controller')("FileDocumentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'agreenApp:fileDocumentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
