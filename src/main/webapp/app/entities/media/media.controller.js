(function() {
    'use strict';

    angular
        .module('agreenApp')
        .controller('MediaController', MediaController);

    MediaController.$inject = ['$scope', '$timeout', '$state', '$uibModal', 'Upload', 'Media', 'MediaService'];

    function MediaController ($scope, $timeout, $state, $uibModal, Upload, Media, MediaService) {
        var vm = this;

        vm.media = [];


        loadAll();

        function loadAll() {
            Media.query(function(result) {
                vm.media = result;
            });
        }

        vm.previewImage = function(image){
            $uibModal.open(
                {
                    template: '<div id="mediaModal" class="well well-md">' +
                              '<span class="media-close" ng-click="mVm.closeModal()">x</span>'+
                              '<img class="media-modal-content" src="data:{{mVm.media.type}};base64, {{mVm.media.content}}" id="{{mVm.media.id}}">' +
                              '<div class="media-caption">{{mVm.media.description}}</div>' +
                              '</div>',
                    controller: ['$uibModalInstance','media', function($uibModalInstance, media){
                        //debugger;
                        var mVm = this;

                        mVm.media = media;
                        mVm.closeModal = function () {
                            $uibModalInstance.dismiss('cancel');
                        };

                    }],
                    controllerAs: 'mVm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        media: [function() {
                            return image;
                        }]
                    }
                }
            ).result.then(function() {
                    $state.go('media', null, { reload: true });
                }, function() {
                    $state.go('media');
                });
        };
    }
})();
