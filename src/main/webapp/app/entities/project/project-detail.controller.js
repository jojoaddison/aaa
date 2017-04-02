(function() {
    'use strict';

    angular
        .module('agreenApp')
        .controller('ProjectDetailController', ProjectDetailController);

    ProjectDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Project'];

    function ProjectDetailController($scope, $rootScope, $stateParams, entity, Project) {
        var vm = this;

        vm.project = entity;

        var unsubscribe = $rootScope.$on('agreenApp:projectUpdate', function(event, result) {
            vm.project = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
