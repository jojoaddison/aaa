(function() {
    'use strict';

    angular
        .module('agreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('pageviewer', {
                parent: 'app',
                url: '/{pid}',
                data: {
                    authorities: [],
                    pageTitle: 'agreenApp.page.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/home/pageviewer.html',
                        controller: 'PageViewerController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('page');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'PageService', function ($stateParams, PageService) {
                        return PageService.getByPid({pid: $stateParams.pid}).$promise;
                    }]
                }
            }
        );
    }
})();
