(function() {
    'use strict';

    angular
        .module('agreenApp')
        .controller('DonationDetailController', DonationDetailController);

    DonationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Donation'];

    function DonationDetailController($scope, $rootScope, $stateParams, entity, Donation) {
        var vm = this;

        vm.donation = entity;

        var unsubscribe = $rootScope.$on('agreenApp:donationUpdate', function(event, result) {
            vm.donation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
