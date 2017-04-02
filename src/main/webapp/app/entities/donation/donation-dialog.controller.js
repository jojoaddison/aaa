(function() {
    'use strict';

    angular
        .module('agreenApp')
        .controller('DonationDialogController', DonationDialogController);

    DonationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Donation'];

    function DonationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Donation) {
        var vm = this;

        vm.donation = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.donation.id !== null) {
                Donation.update(vm.donation, onSaveSuccess, onSaveError);
            } else {
                Donation.save(vm.donation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('agreenApp:donationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.donatedDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
