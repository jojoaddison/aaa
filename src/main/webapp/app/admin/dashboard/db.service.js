(function() {
    'use strict';
    angular
        .module('agreenApp')
        .factory('Article', Article)
        .factory('ArticleService', ArticleService);

    Article.$inject = ['$resource'];

    function Article ($resource) {
        var resourceUrl =  'api/articles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }

    ArticleService.$inject = ['$http'];

    function ArticleService($http){
        return{
            getByPid: function(article){
                var url = "api/articles/by-pid/" + article.pid;
                console.log(url);
                return $http.get(url);
            }
        }
    }

})();
