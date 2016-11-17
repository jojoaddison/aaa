(function() {
    'use strict';

    angular
        .module('agreenApp')
        .controller('DonationDeleteController',DonationDeleteController);

    DonationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Donation'];

    function DonationDeleteController($uibModalInstance, entity, Donation) {
        var vm = this;

        vm.donation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Donation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
