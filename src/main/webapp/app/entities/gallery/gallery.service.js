(function() {
    'use strict';
    angular
        .module('agreenApp')
        .factory('Gallery', Gallery);

    Gallery.$inject = ['$resource'];

    function Gallery ($resource) {
        var resourceUrl =  'api/gallery/:id';

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

    GalleryService.$inject = ['$http'];

    function GalleryService($http){
        return{
            getByPid: function(pid){
                var url = "api/gallery/by-pid/" + pid;
                console.log(url);
                return $http.get(url);
            },
            getByType: function(type){
                var url = "api/gallery/by-type/" + type;
                return $http.get(url);
            }
        }
    }
})();
