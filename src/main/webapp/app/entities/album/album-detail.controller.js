(function() {
    'use strict';

    angular
        .module('agreenApp')
        .controller('AlbumDetailController', AlbumDetailController);

    AlbumDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Album'];

    function AlbumDetailController($scope, $rootScope, $stateParams, entity, Album) {
        var vm = this;

        vm.album = entity;

        var unsubscribe = $rootScope.$on('agreenApp:albumUpdate', function(event, result) {
            vm.album = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
