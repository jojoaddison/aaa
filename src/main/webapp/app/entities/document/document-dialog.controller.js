(function() {
    'use strict';

    angular
        .module('agreenApp')
        .controller('FileDocumentDialogController', FileDocumentDialogController);

    FileDocumentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FileDocument'];

    function FileDocumentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FileDocument) {
        var vm = this;

        vm.fileDocument = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.fileDocument.id !== null) {
                FileDocument.update(vm.fileDocument, onSaveSuccess, onSaveError);
            } else {
                FileDocument.save(vm.fileDocument, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('agreenApp:fileDocumentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
