(function() {
    'use strict';

    angular
        .module('agreenApp')
        .controller('MediaDetailController', MediaDetailController);

    MediaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Media'];

    function MediaDetailController($scope, $rootScope, $stateParams, entity, Media) {
        var vm = this;

        vm.media = entity;

        var unsubscribe = $rootScope.$on('agreenApp:mediaUpdate', function(event, result) {
            vm.media = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
