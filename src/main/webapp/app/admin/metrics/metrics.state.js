(function() {
    'use strict';

    angular
        .module('agreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('jhi-metrics', {
            parent: 'admin',
            url: '/metrics',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'metrics.title'
            },
            views: {
                'header@': {
                    templateUrl: 'app/admin/dashboard/db.header.html',
                    controller: 'DashboardController',
                    controllerAs: 'vm'
                },
                'content@': {
                    templateUrl: 'app/admin/metrics/metrics.html',
                    controller: 'JhiMetricsMonitoringController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('metrics');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
