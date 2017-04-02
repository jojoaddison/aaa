(function() {
    'use strict';

    angular
        .module('agreenApp')
        .controller('FileDocumentDetailController', FileDocumentDetailController);

    FileDocumentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'FileDocument'];

    function FileDocumentDetailController($scope, $rootScope, $stateParams, entity, FileDocument) {
        var vm = this;

        vm.fileDocument = entity;

        var unsubscribe = $rootScope.$on('agreenApp:fileDocumentUpdate', function(event, result) {
            vm.fileDocument = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
