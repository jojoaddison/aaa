(function() {
    'use strict';

    angular
        .module('agreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('page', {
            parent: 'entity',
            url: '/page?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'agreenApp.page.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/page/pages.html',
                    controller: 'PageController',
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
                    $translatePartialLoader.addPart('page');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('page-detail', {
            parent: 'entity',
            url: '/page/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'agreenApp.page.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/page/page-detail.html',
                    controller: 'PageDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('page');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Page', function($stateParams, Page) {
                    return Page.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('page.new', {
            parent: 'page',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/page/page-dialog.html',
                    controller: 'PageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null,
                                content: null,
                                link: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('page', null, { reload: true });
                }, function() {
                    $state.go('page');
                });
            }]
        })
        .state('page.edit', {
            parent: 'page',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/page/page-dialog.html',
                    controller: 'PageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Page', function(Page) {
                            return Page.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('page', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('page.delete', {
            parent: 'page',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/page/page-delete-dialog.html',
                    controller: 'PageDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Page', function(Page) {
                            return Page.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('page', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
