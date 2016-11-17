(function() {
    'use strict';

    angular
        .module('agreenApp')
        .controller('ArticleCreateController', ArticleCreateController);

    ArticleCreateController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Article'];

    function ArticleCreateController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Article) {
        var vmEditor = this;

        vmEditor.openLang = openLang;
        vmEditor.article = entity;
        vmEditor.clear = clear;
        vmEditor.save = save;
        vmEditor.page = null;
        vmEditor.pageIndex = 0;

        vmEditor.types = ["project", "modal","slider","header","page","partner", "links"];

        vmEditor.showLinksEditor = false;
        vmEditor.showAlbumEditor = false;

        vmEditor.tinymceOptions = {
            plugins: 'code'
        };

        vmEditor.toggleLinksEditor = toggleLinksEditor;

        vmEditor.toggleAlbumEditor = toggleAlbumEditor;


        openLang("en");

        function toggleAlbumEditor(){
            vmEditor.showAlbumEditor = !vmEditor.showAlbumEditor;
        }

        function toggleLinksEditor(){
            vmEditor.showLinksEditor = !vmEditor.showLinksEditor;
        }

        function openLang(lang){

            vmEditor.lang = lang;

            if(vmEditor.page){
                vmEditor.article.pages[vmEditor.page.id] = vmEditor.page;
            }

            if(vmEditor.article.album){

            }

            angular.forEach(vmEditor.article.pages, function(page, k){
                if(page.lang == lang){
                    page.id = k;
                    page.pid = vmEditor.article.pid;
                    vmEditor.page = page;
                    vmEditor.pageIndex = k;
                }
            });


        }

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vmEditor.isSaving = true;
            if (vmEditor.article.id !== null) {
                Article.update(vmEditor.article, onSaveSuccess, onSaveError);
            } else {
                angular.forEach(vmEditor.article.pages, function(page){
                    page.pid = vmEditor.article.pid;
                });
                //console.log(vmEditor.article);
                Article.save(vmEditor.article, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('agreenApp:articleUpdate', result);
            $uibModalInstance.close(result);
            vmEditor.isSaving = false;
        }

        function onSaveError () {
            vmEditor.isSaving = false;
        }


    }
})();
