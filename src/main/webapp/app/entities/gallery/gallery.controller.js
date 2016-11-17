(function() {
    'use strict';

    angular
        .module('agreenApp')
        .controller('GalleryController', GalleryController);

    GalleryController.$inject = ['$scope', '$state', 'Gallery'];

    function GalleryController ($scope, $state, Gallery) {
        var vm = this;

        vm.preview = preview;
        vm.closeAll = closeAll;
        vm.closePreview = closePreview;

        vm.galleries = [];

        loadAll();

        function closePreview(gallery){
            gallery.preview = false;
        }

        function closeAll(){
            preview("--null--");
        }

        function preview(pid){
            angular.forEach(vm.galleries, function(gallery){
                gallery.preview = gallery.pid == pid;
            })
        }

        function loadAll() {
            Gallery.query(function(result) {
                vm.galleries = result;
                angular.forEach(vm.galleries, function(gallery){
                    gallery.preview = false;
                })
            });
        }
    }
})();
