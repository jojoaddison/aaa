(function() {
    'use strict';

    angular
        .module('agreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('article', {
            parent: 'entity',
            url: '/article',
            data: {
                authorities: [],
                pageTitle: 'agreenApp.article.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/article/articles.html',
                    controller: 'ArticleController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('page');
                    $translatePartialLoader.addPart('article');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('article-detail', {
            parent: 'entity',
            url: '/article/{id}',
            data: {
                authorities: [''],
                pageTitle: 'agreenApp.article.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/article/article-detail.html',
                    controller: 'ArticleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('article');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Article', function($stateParams, Article) {
                    return Article.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('article.new', {
            parent: 'article',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/article/article-dialog.html',
                    controller: 'ArticleDialogController',
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
                    $state.go('article', null, { reload: true });
                }, function() {
                    $state.go('article');
                });
            }]
        })
        .state('article.create', {
                parent: 'article',
                url: '/create',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/article/article-create.view.html',
                        controller: 'ArticleCreateController',
                        controllerAs: 'vmEditor',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                                $translatePartialLoader.addPart('article');
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
                            $state.go('article', null, { reload: true });
                        }, function() {
                            $state.go('article');
                        });
                }]
            })
        .state('article.edit', {
            parent: 'article',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/article/article-create.view.html',
                    controller: 'ArticleCreateController',
                    controllerAs: 'vmEditor',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('article');
                            $translatePartialLoader.addPart('page');
                            $translatePartialLoader.addPart('global');
                            return $translate.refresh();
                        }],
                        entity: ['Article', function(Article) {
                            return Article.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('article', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('article.delete', {
            parent: 'article',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/article/article-delete-dialog.html',
                    controller: 'ArticleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Article', function(Article) {
                            return Article.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('article', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
