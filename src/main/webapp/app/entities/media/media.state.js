(function() {
    'use strict';

    angular
        .module('agreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('media', {
            parent: 'entity',
            url: '/media',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'agreenApp.media.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/media/media.html',
                    controller: 'MediaController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('media');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('media-detail', {
            parent: 'entity',
            url: '/media/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'agreenApp.media.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/media/media-detail.html',
                    controller: 'MediaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('media');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Media', function($stateParams, Media) {
                    return Media.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('media.new', {
            parent: 'media',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/media/media-dialog.html',
                    controller: 'MediaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                url: null,
                                thumb: null,
                                type: null,
                                description: null,
                                size: null,
                                createdDate: null,
                                modifiedDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('media', null, { reload: true });
                }, function() {
                    $state.go('media');
                });
            }]
        })
        .state('media.edit', {
            parent: 'media',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/media/media-dialog.html',
                    controller: 'MediaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Media', function(Media) {
                            return Media.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('media', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('media.file', {
                parent: 'media',
                url: '/file',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/media/media-file.html',
                        controller: 'MediaFileController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            mediaFiles: function () {
                                return [];
                            }
                        }
                    }).result.then(function() {
                            $state.go('media', null, { reload: true });
                        }, function() {
                            $state.go('media');
                        });
                }]
            })
        .state('media.delete', {
            parent: 'media',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/media/media-delete-dialog.html',
                    controller: 'MediaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Media', function(Media) {
                            return Media.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('media', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
