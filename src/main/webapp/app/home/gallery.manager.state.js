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
                url: '/gallery?page&sort&search',
                data: {
                    pageTitle: 'agreenApp.slideShow.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/home/gallery.html',
                        controller: 'SlideShowController',
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
                        $translatePartialLoader.addPart('slideShow');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('gallery-detail', {
                parent: 'entity',
                url: '/gallery/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreenApp.slideShow.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/home/gallery-detail.html',
                        controller: 'SlideShowDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('slideShow');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'SlideShow', function($stateParams, SlideShow) {
                        return SlideShow.get({id : $stateParams.id}).$promise;
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
                        templateUrl: 'app/home/gallery-dialog.html',
                        controller: 'SlideShowDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'max',
                        resolve: {
                            translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                                $translatePartialLoader.addPart('slide');
                                $translatePartialLoader.addPart('slideShow');
                                return $translate.refresh();
                            }],
                            entity: function () {
                                return {
                                    active: null,
                                    name: null,
                                    slides: [],
                                    modifiedDate: null,
                                    createdDate: null,
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
            .state('gallery.edit', {
                parent: 'gallery',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/home/gallery-dialog.html',
                        controller: 'SlideShowDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'max',
                        resolve: {
                            entity: ['SlideShow', function(SlideShow) {
                                return SlideShow.get({id : $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function() {
                            $state.go('gallery', null, { reload: true });
                        }, function() {
                            $state.go('gallery');
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
                        templateUrl: 'app/home/gallery-delete-dialog.html',
                        controller: 'SlideShowDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['SlideShow', function(SlideShow) {
                                return SlideShow.get({id : $stateParams.id}).$promise;
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
