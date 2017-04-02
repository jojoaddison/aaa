(function() {
    'use strict';

    angular
        .module('agreenApp')
        .controller('DashboardDeleteController', DashboardDeleteController)
        .controller('DashboardController', DashboardController)
        .controller('DashboardDetailController', DashboardDetailController)
        .controller('DashboardDialogController',DashboardDialogController);

    DashboardEditorController.$inject = ['stateParams','entity', '$uibModalInstance', 'Article', 'ArticleService'];

    function DashboardEditorController(stateParams, entity, $uibModalInstance, Article, Articleservice){

    }


    /**
     * Dashboard General Controller
     **/
    DashboardController.$inject = ['$state', 'Auth', 'Principal', 'ProfileService', 'LoginService', 'AlertService'];

    function DashboardController($state, Auth, Principal, ProfileService, LoginService, AlertService) {
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
    DashboardDetailController.$inject = ['$scope', 'Page', 'Album', 'PageService', 'AlertService'];

    function DashboardDetailController ($scope, Page, Album, PageService, AlertService) {
        var vm = this;
        vm.pagesExist = true;
        vm.pages = [];
        vm.page = [];
        vm.pageIndex = 0;
        vm.openPage = openPage;
        vm.clearPage = clearPage;
        vm.openLang = openLang;
        vm.article = null;
        vm.articles = {};
        vm.newPage = newPage;
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
        vm.pid = null;

        vm.tinymceOptions = {
            plugins: 'code',
            toolbar: 'undo redo | bold italic | alignleft aligncenter alignright | code'
        };

        loadGallery();

        loadPages();


        function newPage(){
            vm.page = [
                {
                    lang: "en",
                    pid: null,
                    id: null,
                    title: null,
                    name: null,
                    content: null,
                    links: []
                },
                {
                    lang: "fr",
                    pid: null,
                    id: null,
                    title: null,
                    name: null,
                    content: null,
                    links: []
                },
                {
                    lang: "de",
                    pid: null,
                    id: null,
                    title: null,
                    name: null,
                    content: null,
                    links: []
                }
            ];
            openLang(vm.lang);
        }

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
                    Page.update(page, function(){
                        AlertService.success(page.pid + " " + page.lang + " updated");
                    });
                }else{
                    Page.save(page, function(){
                        AlertService.success(page.pid + " " + page.lang + " save");
                    });
                }
            });
            loadPages();
        }

        function cancelPage(){
            vm.article = null;
        }

        function deletePage(){
            angular.forEach(vm.page, function(page){
                Page.delete({id: page.id});
            });
        }

        function openLang(lang){

            vm.lang = lang;
            var article = null;
            var pid = null;

            angular.forEach(vm.page, function(p, k){
                if(p.pid != null){
                    pid = p.pid;
                }
                if(p.lang == lang){
                    article = p;
                    vm.pageIndex = k;
                }
            });

            if(article == null){
                PageService.getWithLang(vm.article.pid, lang).then(function(result){
                    console.log("GetWithLang");
                    console.log(result);
                    article = result;
                    vm.page = result;
                });
            }

            console.log("vm.page>>");
            console.log("\t");
            console.log(vm.page);


            if(article){
                /*
                if(vm.article != null && vm.article.pid != null){
                    article.pid = vm.article.pid;

                    for(var i in vm.page){
                        var p = vm.page[i];
                        if(p.lang == vm.article.lang){
                            p.content = vm.article.content;
                            break;
                        }
                    }
                }
                 */
                if(article.pid == null && pid != null){
                    article.pid = pid;
                }

                if(article.pid == null){
                    if( vm.article != null && vm.article.pid != null){
                        article.pid = pid;
                    }
                }


                vm.article = article;
            }
            console.log("vm.article>>");
            console.log("\t");
            console.log(vm.article);

        }

        function openAlbum(album){
            vm.album = album;
            clearPage();
        }

        function openPage(page){
            vm.page = page;
            vm.pid = page.pid;
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
            Page.query({},function(result){
                if(result && result.length > 0){
                    vm.pages = result;
                }
                vm.pagesExist = vm.pages.length > 0;

                angular.forEach(vm.pages, function(page){
                    if(!vm.articles[page.pid]){
                        vm.articles[page.pid] = [];
                    }
                    vm.articles[page.pid].push(page);
                });
                $scope.$broadcast('pages-loaded');
            });

        }

        function loadGallery(){
            Album.query(function(result){
                vm.gallery = result;
                vm.albumsExist = vm.gallery.length > 0;
            });
        }

        function getPageByPid(pid){
            var page = null;
            angular.forEach(vm.pages, function(p){
                if(p.pid == pid){
                    page = p;
                }
            });
            return page;
        }

        $scope.$on('pages-loaded', function(){
            if(vm.pid != null){
                openPage(getPageByPid(vm.pid));
            }
        });

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
