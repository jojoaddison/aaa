(function() {
    'use strict';
    angular
        .module('agreenApp')
        .factory('FileDocument', FileDocument);

    FileDocument.$inject = ['$resource'];

    function FileDocument ($resource) {
        var resourceUrl =  'api/file-documents/:id';

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
})();
