(function() {
    'use strict';

    angular
        .module('agreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('album', {
            parent: 'dashboard',
            url: '/album?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'agreenApp.album.home.title'
            },
            views: {
                'header@': {
                    templateUrl: 'app/admin/dashboard/db.header.html',
                    controller: 'DashboardController',
                    controllerAs: 'vm'
                },
                'content@': {
                    templateUrl: 'app/entities/album/albums.html',
                    controller: 'AlbumController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('album');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('album-detail', {
            parent: 'dashboard',
            url: '/album/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'agreenApp.album.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/album/album-detail.html',
                    controller: 'AlbumDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('album');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Album', function($stateParams, Album) {
                    return Album.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('album.new', {
            parent: 'dashboard',
            url: '/album/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/album/album-dialog.html',
                    controller: 'AlbumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('album');
                            return $translate.refresh();
                        }],
                        entity: function () {
                            return {
                                name: null,
                                url: null,
                                description: null,
                                photos: null,
                                isDefault: null,
                                created: null,
                                modified: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('dashboard', null, { reload: true });
                }, function() {
                    $state.go('dashboard');
                });
            }]
        })
        .state('album.edit', {
            parent: 'dashboard',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/album/album-dialog.html',
                    controller: 'AlbumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('album');
                        return $translate.refresh();
                        }],
                        entity: ['Album', function(Album) {
                            return Album.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dashboard', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('album.delete', {
            parent: 'dashboard',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/album/album-delete-dialog.html',
                    controller: 'AlbumDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('album');
                            return $translate.refresh();
                        }],
                        entity: ['Album', function(Album) {
                            return Album.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dashboard', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
