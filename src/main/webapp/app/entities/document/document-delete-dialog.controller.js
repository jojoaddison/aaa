(function() {
    'use strict';

    angular
        .module('agreenApp')
        .controller('FileDocumentDeleteController',FileDocumentDeleteController);

    FileDocumentDeleteController.$inject = ['$uibModalInstance', 'entity', 'FileDocument'];

    function FileDocumentDeleteController($uibModalInstance, entity, FileDocument) {
        var vm = this;

        vm.fileDocument = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FileDocument.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
