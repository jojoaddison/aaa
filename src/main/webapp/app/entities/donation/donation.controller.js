(function() {
    'use strict';

    angular
        .module('agreenApp')
        .controller('DonationController', DonationController);

    DonationController.$inject = ['$scope', '$state', 'Donation'];

    function DonationController ($scope, $state, Donation) {
        var vm = this;
        
        vm.donations = [];

        loadAll();

        function loadAll() {
            Donation.query(function(result) {
                vm.donations = result;
            });
        }
    }
})();
