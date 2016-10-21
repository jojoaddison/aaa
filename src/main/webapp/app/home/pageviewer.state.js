(function() {
    'use strict';

    angular
        .module('agreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('pageviewer', {
                parent: 'home',
                url: '/page/{pid}',
                data: {
                    authorities: [],
                    pageTitle: 'agreenApp.page.detail.title'
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/home/pageviewer.html',
                        controller: 'PageViewerController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            translatePartialLoader: ['$translate', '$translatePartialLoader',
                                function ($translate, $translatePartialLoader) {
                                $translatePartialLoader.addPart('page');
                                return $translate.refresh();
                            }],
                            entity: ['$stateParams', 'PageService', function ($stateParams, PageService) {
                                if($stateParams && $stateParams.length > 0){
                                    return PageService.getByPid($stateParams.pid).then(function(result){
                                        return result.data;
                                    });
                                }
                            }]
                        }
                    }).result.then(function() {
                            $state.go('^');
                        }, function() {
                            $state.go('home');
                        });
                }]
            }
        );
    }
})();
