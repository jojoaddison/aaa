(function() {
    'use strict';

    angular
        .module('agreenApp')
        .controller('DashboardDeleteController', DashboardDeleteController)
        .controller('DashboardController', DashboardController)
        .controller('DashboardDetailController', DashboardDetailController)
        .controller('DashboardDialogController',DashboardDialogController);


    /**
     * Dashboard General Controller
     **/
    DashboardController.$inject = ['$state', 'Auth', 'Principal', 'ProfileService', 'LoginService'];

    function DashboardController($state, Auth, Principal, ProfileService, LoginService) {
        var vm = this;

        vm.isNavbarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;

        ProfileService.getProfileInfo().then(function(response) {
            vm.inProduction = response.inProduction;
            vm.swaggerDisabled = response.swaggerDisabled;
        });

        vm.login = login;
        vm.logout = logout;
        vm.toggleNavbar = toggleNavbar;
        vm.collapseNavbar = collapseNavbar;
        vm.$state = $state;

        function login() {
            collapseNavbar();
            LoginService.open();
        }

        function logout() {
            collapseNavbar();
            Auth.logout();

            $state.go('home');
        }

        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
        }

        function collapseNavbar() {
            vm.isNavbarCollapsed = true;
        }
    }


    /**
     * Dashboard Dialog Controller
     **/

    DashboardDialogController.$inject = ['$stateParams', '$uibModalInstance', 'entity', 'User', 'JhiLanguageService'];

    function DashboardDialogController ($stateParams, $uibModalInstance, entity, User, JhiLanguageService) {
        var vm = this;

        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        vm.clear = clear;
        vm.languages = null;
        vm.save = save;
        vm.user = entity;


        JhiLanguageService.getAll().then(function (languages) {
            vm.languages = languages;
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function onSaveSuccess (result) {
            vm.isSaving = false;
            $uibModalInstance.close(result);
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        function save () {
            vm.isSaving = true;
            if (vm.user.id !== null) {
                User.update(vm.user, onSaveSuccess, onSaveError);
            } else {
                User.save(vm.user, onSaveSuccess, onSaveError);
            }
        }
    }

    /**
     * Dashboard Detail Controller
     **/
    DashboardDetailController.$inject = ['Page', 'Album'];

    function DashboardDetailController (Page, Album) {
        var vm = this;
        vm.pagesExist = true;
        vm.pages = [
            {
                name: "Association",
                title: "About the Association",
                content: "Ipsum lopsum docum lotum lopsum",
                links: [
                    {
                        name: "Association",
                        href: "association"
                    }
                ],
                lang: "en"
            }
        ];
        vm.page = null;
        vm.openPage = openPage;
        vm.clearPage = clearPage;

        vm.albumsExist = true;
        vm.gallery = [];
        vm.album = null;
        vm.openAlbum = openAlbum;
        vm.clearAlbum = clearAlbum;

        loadGallery();

        loadPages();

        function openAlbum(album){
            vm.album = album;
            clearPage();
        }

        function openPage(page){
            vm.page = page;
            clearAlbum();
        }

        function clearPage(){
            vm.page = null;
        }

        function clearAlbum(){
            vm.album = null;
        }

        function loadPages(){
            Page.query(function(result){
                vm.pages = result;
                vm.pagesExist = vm.pages.length > 0;
            });
        }

        function loadGallery(){
            Album.query(function(result){
                vm.gallery = result;
                vm.albumsExist = vm.gallery.length > 0;
            });
        }

    }

    /**
     * Dashboard Delete Controller
     **/
    DashboardDeleteController.$inject = ['$uibModalInstance', 'entity', 'User'];

    function DashboardDeleteController ($uibModalInstance, entity, User) {
        var vm = this;

        vm.user = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (login) {
            User.delete({login: login},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
