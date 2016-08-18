(function() {
    'use strict';

    angular
        .module('agreenApp')
        .controller('GalleryManagerController', GalleryManagerController);

        GalleryManagerController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'mediaFiles', 'Media'];

        function GalleryManagerController(){

        }
}
)
