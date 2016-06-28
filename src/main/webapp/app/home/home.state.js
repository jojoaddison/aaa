(function() {
    'use strict';

    angular
        .module('agreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('home', {
            parent: 'app',
            url: '/',
            data: {
                authorities: []
            },
            views: {
                'header@': {
                    templateUrl: 'blank.html',
                    controller: 'HeaderController',
                    controllerAs: 'vm'
                },
                'content@': {
                    templateUrl: 'app/home/main.html',
                    controller: 'HomeController',
                    controllerAs: 'vm'
                },
                'footer@': {
                    templateUrl: 'app/home/footer.html',
                    controller: 'FooterController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('global');
                    $translatePartialLoader.addPart('home');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
