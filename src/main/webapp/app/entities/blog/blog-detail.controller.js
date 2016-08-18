(function() {
    'use strict';

    angular
        .module('agreenApp')
        .controller('BlogDetailController', BlogDetailController);

    BlogDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Blog'];

    function BlogDetailController($scope, $rootScope, $stateParams, entity, Blog) {
        var vm = this;

        vm.blog = entity;

        var unsubscribe = $rootScope.$on('agreenApp:blogUpdate', function(event, result) {
            vm.blog = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
