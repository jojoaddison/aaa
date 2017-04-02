(function() {
    'use strict';
    angular
        .module('agreenApp')
        .factory('Album', Album);

    Album.$inject = ['$resource', 'DateUtils'];

    function Album ($resource, DateUtils) {
        var resourceUrl =  'api/albums/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.created = DateUtils.convertDateTimeFromServer(data.created);
                        data.modified = DateUtils.convertDateTimeFromServer(data.modified);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
