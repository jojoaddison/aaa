(function() {
    'use strict';

    angular
        .module('agreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('file-document', {
            parent: 'entity',
            url: '/file-document',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'agreenApp.fileDocument.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/file-document/file-documents.html',
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
        .state('file-document-detail', {
            parent: 'entity',
            url: '/file-document/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'agreenApp.fileDocument.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/file-document/file-document-detail.html',
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
        .state('file-document.new', {
            parent: 'file-document',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/file-document/file-document-dialog.html',
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
                    $state.go('file-document', null, { reload: true });
                }, function() {
                    $state.go('file-document');
                });
            }]
        })
        .state('file-document.edit', {
            parent: 'file-document',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/file-document/file-document-dialog.html',
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
                    $state.go('file-document', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('file-document.delete', {
            parent: 'file-document',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/file-document/file-document-delete-dialog.html',
                    controller: 'FileDocumentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FileDocument', function(FileDocument) {
                            return FileDocument.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('file-document', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
