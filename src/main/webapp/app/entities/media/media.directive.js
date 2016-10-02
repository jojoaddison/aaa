(function() {
    'use strict';
    /**
     * Created by Kojo Ampia on 23/06/16.
     */
    angular
        .module('agreenApp')
        .directive('dropzone', MediaDirective);

     function MediaDirective() {
        return {
            restrict: 'C',
            link: function (scope, element, attrs) {
                var config = {
                    url: 'api/media/file',
                    maxFilesize: 100,
                    paramName: "uploadfile",
                    maxThumbnailFilesize: 10,
                    parallelUploads: 1,
                    autoProcessQueue: false
                };
                var eventHandlers = {
                    'addedfile': function (file) {
                        scope.file = file;
                        if (this.files[1] != null) {
                            this.removeFile(this.files[0]);
                        }
                        scope.$apply(function () {
                            scope.fileAdded = true;
                        });
                    },
                    'success': function (file, response) {
                    }
                };
                var dropzone = new Dropzone(element[0], config);
                angular.forEach(eventHandlers, function (handler, event) {
                    dropzone.on(event, handler);
                });
                scope.processDropzone = function () {
                    dropzone.processQueue();
                };
                scope.resetDropzone = function () {
                    dropzone.removeAllFiles();
                };
                return dropzone;
            }
        }
    }

});
