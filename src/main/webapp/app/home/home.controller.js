(function() {
    'use strict';

    angular
        .module('agreenApp')
        .controller('HeaderController', HeaderController)
        .controller('HomeController', HomeController)
        .controller('FooterController', FooterController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'AlertService', '$sce', 'Article', 'tmhDynamicLocale'];

    function HomeController ($scope, Principal, LoginService, $state, AlertService, $sce, Article, tmhDynamicLocale) {
        var vm = this;
        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        vm.information = null;
        vm.lang = "en";
        vm.headers = {};
        vm.pages = {};
        vm.partners = {};


        function startListeners(){

            $scope.$on('authenticationSuccess', function() {
                getAccount();
            });

            $scope.$on('language-changed', function() {
                getAccount();
            });
        }

        function getAccount() {
            Principal.identity().then(function(account) {
                if(account){
                    vm.account = account;
                    vm.isAuthenticated = Principal.isAuthenticated;
                    vm.lang = account.langKey;
                }
                loadPages();
            });
        }

        function loadPages(){
            Article.query({}, onSuccess, onError);
        }

        function onSuccess(articles) {
            console.log("######### ARTICLES ######");
            console.log(articles);
            var lang = tmhDynamicLocale.get('NG_TRANSLATE_LANG_KEY');

            angular.forEach(articles, function(article){

               console.log("######### PAGE ######");
               console.log(article);
                angular.forEach(article.pages, function(page, index){
                    if(page !== null && page.content !== null && page.lang == lang){
                        page.content = $sce.trustAsHtml(page.content);
                        page.id = index;

                        if(article.type == 'header'){
                            vm.headers[article.pid] = page;
                        }

                        if(article.type == 'page'){
                            vm.pages[article.pid] = page;
                        }

                        if(article.type == 'partner'){
                            vm.partners[article.pid] = page;
                        }

                    }
                });
            });
            console.log("######### FINAL-PAGES ######");
          console.log(vm.pages);
        }

        function onError(error) {
            AlertService.error(error.data.message);
        }

        function register () {
            $state.go('register');
        }

        getAccount();

        startListeners();


    }

    FooterController.$inject = ['$scope', '$rootScope', '$sce', 'tmhDynamicLocale', 'Global'];
    function FooterController($scope, $rootScope, $sce, tmhDynamicLocale, Global){
            $scope.footer = null;
            $scope.newsletter = null;
            $scope.about = null;
            $scope.association = null;

            function runFooter(){
                var lang = tmhDynamicLocale.get();
                Global.get(lang).then(function(global){
                    $scope.footer = global.data.footer;
                    $scope.newsletter = {
                        title: global.data.footer.newsletter.title,
                        content: $sce.trustAsHtml(global.data.footer.newsletter.content)
                    };
                    $scope.about = {
                        title: global.data.footer.about.title,
                        content: $sce.trustAsHtml(global.data.footer.about.content)
                    };
                    $scope.association = {
                        title: global.data.footer.association.title,
                        links: global.data.footer.association.links
                    };
                });
            }

            function bindListener(){
                $rootScope.$on("language-changed", runFooter);
            }

            runFooter();
            bindListener();
    }

    HeaderController.$inject = ['$scope','Slider','Principal','$location'];
    function HeaderController ($scope, Slider, Principal, $location){
        var slideBox = angular.element("#slider");
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated();
        vm.openAlbum = openAlbum;

        loadSlider();

        function loadSlider(){
            Slider.get().then(function(slides){
                slideBox.html(slides.data);
                slideBox.addClass('photo-banner');
                showEditBtn(vm.isAuthenticated);
            });
        }
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated();
                showEditBtn(true);
            });
        }
        getAccount();

        function showEditBtn(isAuthenticated){
            var editBtn =   slideBox.find("#editSliderBtn");
            if(editBtn && !isAuthenticated){
                editBtn.fadeOut();
            }
            if(editBtn && isAuthenticated){
                editBtn.fadeIn();
            }
        }

        $scope.$on('logoutSuccess', function() {
            showEditBtn(false);
        });

        function openAlbum(){
            console.log("open-album");
            $location.url = "#/album";
        }

    }

})();
