(function() {
    'use strict';

    angular
        .module('agreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig ($stateProvider) {
        $stateProvider.state('docs', {
            parent: 'admin',
            url: '/docs',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'global.menu.admin.apidocs'
            },
            views: {
                'header@': {
                    templateUrl: 'app/admin/dashboard/db.header.html',
                    controller: 'DashboardController',
                    controllerAs: 'vm'
                },
                'content@': {
                    templateUrl: 'app/admin/docs/docs.html'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', function ($translate) {
                    return $translate.refresh();
                }]
            }
        });
    }
})();
