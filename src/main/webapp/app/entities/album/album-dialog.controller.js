(function() {
    'use strict';

    angular
        .module('agreenApp')
        .controller('AlbumDialogController', AlbumDialogController);

    AlbumDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Album', 'Upload'];

    function AlbumDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Album, Upload) {
        var vm = this;

        vm.album = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.files = [];
        vm.upload = upload;

        vm.dzAddedFile = function( file ) {
            // console.log( file );
            if(file != null){
                vm.files.push(file);
            }
            console.log(vm.files);
        };

        vm.dropzoneConfig = {
            parallelUploads: 3,
            maxFileSize: 100,
            url: '/api/album/pictures',
            acceptedFiles : 'image/jpeg, images/jpg, image/png',
            addRemoveLinks : true,
            dictDefaultMessage : 'Click to add or drop photos',
            dictRemoveFile : 'Remove photo',
            dictResponseError : 'Could not upload this photo',
            autoProcessQueue: false
        };

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.album.id !== null) {
                Album.update(vm.album, onSaveSuccess, onSaveError);
            } else {
                Album.save(vm.album, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('agreenApp:albumUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.created = false;
        vm.datePickerOpenStatus.modified = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }


        function upload(files){
            if (files && files.length) {
                for (var i = 0; i < files.length; i++) {
                    var file = files[i];
                    console.log(file);
                    if (!file.$error) {
                        Upload.upload({
                            // url: 'api/media/file',
                            url: 'api/gridfs',
                            data: {
                                mediaFile: file,
                                gallery: vm.gallery
                            }
                        }).then(function (resp) {
                            $timeout(function() {
                                vm.log = 'file: ' +
                                    resp.config.data.mediaFile.name +
                                    ', Response: ' + JSON.stringify(resp.data) +
                                    '\n' + vm.log;
                                console.log(vm.log);
                            });
                            vm.isSaving = false;
                        }, null, function (evt) {
                            var progressPercentage = parseInt(100.0 *
                                evt.loaded / evt.total);
                            vm.log = 'progress: ' + progressPercentage +
                                '% ' + evt.config.data.mediaFile.name + '\n' +
                                vm.log;
                            console.log(vm.log);
                            onSaveError();
                        });
                    }
                }
                onSaveSuccess(vm.files);
            }
        }
    }
})();
