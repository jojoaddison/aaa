(function() {
    'use strict';

    angular
        .module('agreenApp')
        .controller('MediaDialogController', MediaDialogController);

    MediaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'Upload', 'entity', 'Media', 'MediaService'];

    function MediaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, Upload, entity, Media, MediaService) {
        var vm = this;

        vm.gallery = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.log = "";

        vm.files = [];

        vm.upload = upload;

        vm.dzAddedFile = function( file ) {
            // console.log( file );
            if(file != null){
                vm.files.push(file);
            }
            console.log(vm.files);
        };

        vm.dzError = function( file, errorMessage ) {
            console.log(errorMessage);
            console.log(file);
        };

        vm.dropzoneConfig = {
            parallelUploads: 3,
            maxFileSize: 100,
            url: '/api/media/files',
            acceptedFiles : 'image/jpeg, images/jpg, image/png',
            addRemoveLinks : true,
            dictDefaultMessage : 'Click to add or drop photos',
            dictRemoveFile : 'Remove photo',
            dictResponseError : 'Could not upload this photo',
            autoProcessQueue: false
        };

        function upload(files){
            if (files && files.length) {
                for (var i = 0; i < files.length; i++) {
                    var file = files[i];
                    console.log(file);
                    if (!file.$error) {
                        Upload.upload({
                            url: 'api/media/file',
                            data: {
                                mediaFile: file,
                                gallery: vm.gallery.description
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

        vm.reset = function() {
            vm.dropzone.removeAllFiles();
        };



        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            upload(vm.files);
            /*
            if (vm.media.id !== null) {
                Media.update(vm.media, onSaveSuccess, onSaveError);
            } else {
                Media.save(vm.media, onSaveSuccess, onSaveError);
            }
            */
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
