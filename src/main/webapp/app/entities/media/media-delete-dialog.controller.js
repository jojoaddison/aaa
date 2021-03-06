(function() {
    'use strict';

    angular
        .module('agreenApp')
        .controller('MediaDeleteController',MediaDeleteController);

    MediaDeleteController.$inject = ['$uibModalInstance', 'entity', 'Media', 'MediaService'];

    function MediaDeleteController($uibModalInstance, entity, Media, MediaService) {
        var vm = this;

        vm.media = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Media.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                    MediaService.deleteMedia(vm.media.name);
                });
        }
    }
})();
