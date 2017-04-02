(function() {
    'use strict';

    angular
        .module('agreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('photo', {
            parent: 'entity',
            url: '/photo',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'agreenApp.photo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/photo/photos.html',
                    controller: 'PhotoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('photo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('photo-detail', {
            parent: 'entity',
            url: '/photo/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'agreenApp.photo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/photo/photo-detail.html',
                    controller: 'PhotoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('photo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Photo', function($stateParams, Photo) {
                    return Photo.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('photo.new', {
            parent: 'photo',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/photo/photo-dialog.html',
                    controller: 'PhotoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                url: null,
                                createdDate: null,
                                tags: null,
                                comments: null,
                                description: null,
                                modifiedDate: null,
                                thumb: null,
                                type: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('photo', null, { reload: true });
                }, function() {
                    $state.go('photo');
                });
            }]
        })
        .state('photo.edit', {
            parent: 'photo',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/photo/photo-dialog.html',
                    controller: 'PhotoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Photo', function(Photo) {
                            return Photo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('photo', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('photo.delete', {
            parent: 'photo',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/photo/photo-delete-dialog.html',
                    controller: 'PhotoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Photo', function(Photo) {
                            return Photo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('photo', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
