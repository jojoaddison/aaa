(function() {
    'use strict';

    angular
        .module('agreenApp')
        .controller('MediaController', MediaController);

    MediaController.$inject = ['$scope', '$state', '$log', 'Media'];

    function MediaController ($scope, $state, $log, Media) {
        var vm = this;

        vm.media = [];

        loadAll();

        function loadAll() {
            Media.query(function(result) {
                vm.media = result;
            });
        }

        vm.dzAddedFile = function( file ) {
            $log.log( file );
        };

        vm.dzError = function( file, errorMessage ) {
            $log.log(errorMessage);
        };

        vm.dropzoneConfig = {
            parallelUploads: 3,
            maxFileSize: 30
        };

        vm.save = save;

        function save(){
            $log("save files");
        }
    }
})();
