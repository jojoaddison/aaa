(function() {
    'use strict';

    angular
        .module('agreenApp')
        .controller('JhiLanguageController', JhiLanguageController);

    JhiLanguageController.$inject = ['$rootScope', '$translate', 'JhiLanguageService', 'tmhDynamicLocale'];

    function JhiLanguageController ($rootScope, $translate, JhiLanguageService, tmhDynamicLocale) {
        var vm = this;

        vm.changeLanguage = changeLanguage;
        vm.languages = null;

        JhiLanguageService.getAll().then(function (languages) {
            vm.languages = languages;
        });

        function changeLanguage (languageKey) {
            $translate.use(languageKey);
            tmhDynamicLocale.set(languageKey);
            $rootScope.$broadcast('language-changed');
        }
    }
})();
