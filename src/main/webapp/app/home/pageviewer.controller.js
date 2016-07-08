(function() {
    'use strict';

    angular
        .module('agreenApp')
        .controller('PageViewerController', PageViewerController);

    PageViewerController.$inject = ['$scope', '$state', '$stateParams', 'entity', 'PageService'];

    function PageViewerController($scope, $state, $stateParams, entity, PageService){
        var vm = this;
        vm.page = entity;

    }

})();
