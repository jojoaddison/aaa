(function() {
    'use strict';

    angular
        .module('agreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('document', {
            parent: 'entity',
            url: '/document',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'agreenApp.fileDocument.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/document/documents.html',
                    controller: 'FileDocumentController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fileDocument');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('document-detail', {
            parent: 'entity',
            url: '/document/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'agreenApp.fileDocument.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/document/document-detail.html',
                    controller: 'FileDocumentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fileDocument');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'FileDocument', function($stateParams, FileDocument) {
                    return FileDocument.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('document.new', {
            parent: 'document',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/document/document-dialog.html',
                    controller: 'FileDocumentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                category: null,
                                dateCreated: null,
                                fileType: null,
                                fileData: null,
                                fileName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('document', null, { reload: true });
                }, function() {
                    $state.go('document');
                });
            }]
        })
        .state('document.edit', {
            parent: 'document',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/document/document-dialog.html',
                    controller: 'FileDocumentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FileDocument', function(FileDocument) {
                            return FileDocument.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('document', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('document.delete', {
            parent: 'document',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/document/document-delete-dialog.html',
                    controller: 'FileDocumentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FileDocument', function(FileDocument) {
                            return FileDocument.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('document', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
