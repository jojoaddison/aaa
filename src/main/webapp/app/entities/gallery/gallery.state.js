(function() {
    'use strict';

    angular
        .module('agreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('gallery', {
            parent: 'entity',
            url: '/gallery',
            data: {
                authorities: [],
                pageTitle: 'agreenApp.gallery.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/gallery/gallery.html',
                    controller: 'GalleryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('page');
                    $translatePartialLoader.addPart('gallery');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('gallery-detail', {
            parent: 'entity',
            url: '/gallery/{id}',
            data: {
                authorities: [''],
                pageTitle: 'agreenApp.gallery.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/gallery/gallery-detail.html',
                    controller: 'GalleryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('gallery');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Gallery', function($stateParams, Gallery) {
                    return Gallery.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('gallery.new', {
            parent: 'gallery',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gallery/gallery-dialog.html',
                    controller: 'GalleryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                pid: null,
                                pages: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('gallery', null, { reload: true });
                }, function() {
                    $state.go('gallery');
                });
            }]
        })
        .state('gallery.create', {
                parent: 'gallery',
                url: '/create',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/gallery/gallery-create.view.html',
                        controller: 'GalleryCreateController',
                        controllerAs: 'vmEditor',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                                $translatePartialLoader.addPart('gallery');
                                $translatePartialLoader.addPart('page');
                                $translatePartialLoader.addPart('global');
                                return $translate.refresh();
                            }],
                            entity: function () {
                                return {
                                    id: null,
                                    type: 'page',
                                    pid: 'new_page',
                                    pages: [
                                        {
                                            id: null,
                                            pid: 'new_page',
                                            lang: 'en',
                                            name: null,
                                            title: null,
                                            content: null,
                                            links: [
                                                {
                                                    id: null,
                                                    name: null,
                                                    href: null
                                                }
                                            ]
                                        },
                                        {
                                            id: null,
                                            pid: 'new_page',
                                            lang: 'de',
                                            name: null,
                                            title: null,
                                            content: null,
                                            links: [
                                                {
                                                    id: null,
                                                    name: null,
                                                    href: null
                                                }
                                            ]
                                        },
                                        {
                                            id: null,
                                            pid: 'new_page',
                                            lang: 'fr',
                                            name: null,
                                            title: null,
                                            content: null,
                                            links: [
                                                {
                                                    id: null,
                                                    name: null,
                                                    href: null
                                                }
                                            ]
                                        }
                                    ],
                                    links: [],
                                    album: null
                                };
                            }
                        }
                    }).result.then(function() {
                            $state.go('gallery', null, { reload: true });
                        }, function() {
                            $state.go('gallery');
                        });
                }]
            })
        .state('gallery.edit', {
            parent: 'gallery',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gallery/gallery-create.view.html',
                    controller: 'GalleryCreateController',
                    controllerAs: 'vmEditor',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('gallery');
                            $translatePartialLoader.addPart('page');
                            $translatePartialLoader.addPart('global');
                            return $translate.refresh();
                        }],
                        entity: ['Gallery', function(Gallery) {
                            return Gallery.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('gallery', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('gallery.delete', {
            parent: 'gallery',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gallery/gallery-delete-dialog.html',
                    controller: 'GalleryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Gallery', function(Gallery) {
                            return Gallery.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('gallery', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
