(function() {
    'use strict';

    angular
        .module('agreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('dashboard', {
                parent: 'admin',
                url: '/dashboard',
                data: {
                    authorities: ['ROLE_ADMIN','ROLE_USER'],
                    pageTitle: 'dashboard.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/admin/dashboard/db.home.html',
                        controller: 'DashboardDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('page');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('dashboard-page', {
                parent: 'admin',
                url: '/dashboard/page/{pid}/edit',
                data: {
                    authorities: ['ROLE_ADMIN','ROLE_USER'],
                    pageTitle: 'dashboard.home.title'
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/admin/dashboard/db.editor.html',
                        controller: 'DashboardEditorController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Article', function(ArticleService) {
                                return ArticleService.getByPid({pid : $stateParams.pid});
                            }]
                        }
                    }).result.then(function() {
                            $state.go('dashboard', null, { reload: true });
                        }, function() {
                            $state.go('^');
                        });
                }]
            })
            /**
            .state('dashboard-detail', {
                parent: 'admin',
                url: '/dashboard/:login',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'dashboard.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/admin/dashboard/dashboard-detail.html',
                        controller: 'DashboardDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('dashboard');
                        return $translate.refresh();
                    }]
                }
            })
            .state('dashboard.new', {
                parent: 'dashboard',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/admin/dashboard/dashboard-dialog.html',
                        controller: 'DashboardDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null, login: null, firstName: null, lastName: null, email: null,
                                    activated: true, langKey: null, createdBy: null, createdDate: null,
                                    lastModifiedBy: null, lastModifiedDate: null, resetDate: null,
                                    resetKey: null, authorities: null
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
            .state('dashboard.edit', {
                parent: 'dashboard',
                url: '/{login}/edit',
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/admin/dashboard/dashboard-dialog.html',
                        controller: 'DashboardDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['User', function(User) {
                                return User.get({login : $stateParams.login});
                            }]
                        }
                    }).result.then(function() {
                            $state.go('dashboard', null, { reload: true });
                        }, function() {
                            $state.go('^');
                        });
                }]
            })
            .state('dashboard.delete', {
                parent: 'dashboard',
                url: '/{login}/delete',
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/admin/dashboard/dashboard-delete-dialog.html',
                        controller: 'DashboardDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['User', function(User) {
                                return User.get({login : $stateParams.login});
                            }]
                        }
                    }).result.then(function() {
                            $state.go('dashboard', null, { reload: true });
                        }, function() {
                            $state.go('^');
                        });
                }]
            })
             */
             ;
    }
})();
