(function() {
    'use strict';

    angular
        .module('agreenApp')
        .controller('PhotoDetailController', PhotoDetailController);

    PhotoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Photo'];

    function PhotoDetailController($scope, $rootScope, $stateParams, entity, Photo) {
        var vm = this;

        vm.photo = entity;

        var unsubscribe = $rootScope.$on('agreenApp:photoUpdate', function(event, result) {
            vm.photo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
