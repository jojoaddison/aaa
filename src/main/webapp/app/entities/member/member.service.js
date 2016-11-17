(function() {
    'use strict';
    angular
        .module('agreenApp')
        .factory('Member', Member);

    Member.$inject = ['$resource', 'DateUtils'];

    function Member ($resource, DateUtils) {
        var resourceUrl =  'api/members/:id';

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
})();
