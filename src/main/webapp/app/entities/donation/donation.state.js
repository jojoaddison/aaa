(function() {
    'use strict';

    angular
        .module('agreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('donation', {
            parent: 'entity',
            url: '/donation',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'agreenApp.donation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/donation/donations.html',
                    controller: 'DonationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('donation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('donation-detail', {
            parent: 'entity',
            url: '/donation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'agreenApp.donation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/donation/donation-detail.html',
                    controller: 'DonationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('donation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Donation', function($stateParams, Donation) {
                    return Donation.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('donation.new', {
            parent: 'donation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/donation/donation-dialog.html',
                    controller: 'DonationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                amount: null,
                                memberId: null,
                                donatedDate: null,
                                projectId: null,
                                type: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('donation', null, { reload: true });
                }, function() {
                    $state.go('donation');
                });
            }]
        })
        .state('donation.edit', {
            parent: 'donation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/donation/donation-dialog.html',
                    controller: 'DonationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Donation', function(Donation) {
                            return Donation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('donation', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('donation.delete', {
            parent: 'donation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/donation/donation-delete-dialog.html',
                    controller: 'DonationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Donation', function(Donation) {
                            return Donation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('donation', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
