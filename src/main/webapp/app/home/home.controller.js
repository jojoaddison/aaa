(function() {
    'use strict';

    angular
        .module('agreenApp')
        .controller('HeaderController', HeaderController)
        .controller('HomeController', HomeController)
        .controller('FooterController', FooterController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state'];

    function HomeController ($scope, Principal, LoginService, $state) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }
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
        var slideBox = $("#slider");
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated();
        vm.openAlbum = openAlbum;

        loadSlider();

        function loadSlider(){
            Slider.get().then(function(slides){
                slideBox.html(slides.data);

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
