(function() {
    'use strict';
    angular
        .module('agreenApp')
        .factory('Media', Media)
        .factory('MediaService', MediaService);

    Media.$inject = ['$resource', 'DateUtils'];

    function Media ($resource, DateUtils) {
        var resourceUrl =  'api/media/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createdDate = DateUtils.convertDateTimeFromServer(data.createdDate);
                        data.modifiedDate = DateUtils.convertDateTimeFromServer(data.modifiedDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }

    MediaService.$inject = ['$http'];

    function MediaService($http){
        return {
            uploadToGridFS: function(files){
                var url = "/api/gridfs";
                $http.post(url, files);
            },
            uploadToMediaFS: function(files){
                var url = "/api/media/files";
                $http.post(url, files);
            }
        }
    }

})();
