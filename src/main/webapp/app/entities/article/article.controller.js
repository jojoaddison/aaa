(function() {
    'use strict';

    angular
        .module('agreenApp')
        .controller('ArticleController', ArticleController);

    ArticleController.$inject = ['$scope', '$state', 'Article'];

    function ArticleController ($scope, $state, Article) {
        var vm = this;

        vm.preview = preview;
        vm.closeAll = closeAll;
        vm.closePreview = closePreview;

        vm.articles = [];

        loadAll();

        function closePreview(article){
            article.preview = false;
        }

        function closeAll(){
            preview("--null--");
        }

        function preview(pid){
            angular.forEach(vm.articles, function(article){
                article.preview = article.pid == pid;
            })
        }

        function loadAll() {
            Article.query(function(result) {
                vm.articles = result;
                angular.forEach(vm.articles, function(article){
                    article.preview = false;
                })
            });
        }
    }
})();
