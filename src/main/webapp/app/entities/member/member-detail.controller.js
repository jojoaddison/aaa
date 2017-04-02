(function() {
    'use strict';

    angular
        .module('agreenApp')
        .controller('MemberDetailController', MemberDetailController);

    MemberDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Member'];

    function MemberDetailController($scope, $rootScope, $stateParams, entity, Member) {
        var vm = this;

        vm.member = entity;

        var unsubscribe = $rootScope.$on('agreenApp:memberUpdate', function(event, result) {
            vm.member = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
