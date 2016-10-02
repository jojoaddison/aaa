(function() {
    'use strict';

    angular
        .module('agreenApp')
        .controller('ArticleDetailController', ArticleDetailController);

    ArticleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Article'];

    function ArticleDetailController($scope, $rootScope, $stateParams, entity, Article) {
        var vm = this;

        vm.article = entity;

        var unsubscribe = $rootScope.$on('agreenApp:articleUpdate', function(event, result) {
            vm.article = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
