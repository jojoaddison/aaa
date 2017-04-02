(function() {
    'use strict';

    angular
        .module('agreenApp')
        .controller('MediaFileController', MediaFileController);

    MediaFileController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'mediaFiles', 'Media'];

    function MediaFileController ($timeout, $scope, $stateParams, $uibModalInstance, mediaFiles, Media) {
        var vm = this;

        vm.mediaFiles = mediaFiles;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.mediaFilesId = $stateParams.id;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.media.id !== null) {
                Media.update(vm.media, onSaveSuccess, onSaveError);
            } else {
                Media.save(vm.media, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('agreenApp:mediaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.modifiedDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
