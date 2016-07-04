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
    DashboardDetailController.$inject = ['Page', 'Album', 'PageService'];

    function DashboardDetailController (Page, Album, PageService) {
        var vm = this;
        vm.pagesExist = true;
        vm.pages = [
            {
                id: 1,
                pid: 'association',
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
            },
            {
                id: 2,
                pid: 'association',
                name: "Association",
                title: "Über der Verrein",
                content: "Ipsum lopsum docum lotum lopsum",
                links: [
                    {
                        name: "Association",
                        href: "association"
                    }
                ],
                lang: "de"
            },
            {
                id: 3,
                pid: 'association',
                name: "Association",
                title: "French version of Association",
                content: "Ipsum lopsum docum lotum lopsum",
                links: [
                    {
                        name: "Association",
                        href: "association"
                    }
                ],
                lang: "fr"
            }
        ];
        vm.page = [];
        vm.openPage = openPage;
        vm.clearPage = clearPage;
        vm.openLang = openLang;
        vm.article = null;
        vm.articles = {};
        vm.savePage = savePage;
        vm.cancelPage = cancelPage;
        vm.deletePage = deletePage;

        vm.link = {name: null, href: null};
        vm.addPageLink = addPageLink;
        vm.delPageLink = delPageLink;

        vm.albumsExist = true;
        vm.gallery = [];
        vm.album = null;
        vm.openAlbum = openAlbum;
        vm.clearAlbum = clearAlbum;

        vm.lang = 'en';

        vm.tinymceOptions = {
            plugins: 'code',
            toolbar: 'undo redo | bold italic | alignleft aligncenter alignright | code'
        };

        loadGallery();

        loadPages();


        function addPageLink(){
            vm.article.links.push(vm.link);
            clearPageLink();
        }

        function clearPageLink(){
            vm.link = {name: null, href: null};
        }

        function delPageLink(index){
            vm.article.links.splice(index, 1);
        }

        function savePage(){
            angular.forEach(vm.page, function(page){
                if(page.id){
                    Page.update(page);
                }else{
                    Page.save(page);
                }
            });
        }

        function cancelPage(){

        }

        function deletePage(){

        }

        function openLang(lang){

            vm.lang = lang;
            var article = null;

            angular.forEach(vm.page, function(p){
                if(p.lang == lang){
                    article = p;
                }
            });

            if(article == null){
                PageService.getWithLang(vm.article.name, lang).then(function(result){
                    article = result;
                    vm.page.push(result);
                });
            }

            if(article){
                vm.article = article;
            }

        }

        function openAlbum(album){
            vm.album = album;
            clearPage();
        }

        function openPage(page){
            vm.page = page;
            openLang(vm.lang);
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
                if(result && result.length > 0){
                    vm.pages = result;
                }
                vm.pagesExist = vm.pages.length > 0;
            });

            angular.forEach(vm.pages, function(page){
                if(!vm.articles[page.name]){
                    vm.articles[page.name] = [];
                }
                vm.articles[page.name].push(page);
            })
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
