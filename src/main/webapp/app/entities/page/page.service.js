(function() {
    'use strict';
    angular
        .module('agreenApp')
        .factory('Page', Page)
        .factory('PageService', PageService);

    Page.$inject = ['$resource'];

    function Page ($resource) {
        var resourceUrl =  'api/page/:id';

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

    PageService.$inject = ['$http'];

    function PageService($http){
        return{
            getWithLang: function(name, lang){
                var url = "api/pages/search/" + name + "/" + lang;
                return $http.get(url);
            },
            getByName: function(name){
                var url = "api/pages/search/by-name/" + name;
                return $http.get(url);
            },
            getByPid: function(pid){
                var url = "api/pages/search/by-pid/" + pid;
                return $http.get(url);
            }
        }
    }

})();
