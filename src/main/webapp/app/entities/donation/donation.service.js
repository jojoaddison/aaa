(function() {
    'use strict';
    angular
        .module('agreenApp')
        .factory('Donation', Donation);

    Donation.$inject = ['$resource', 'DateUtils'];

    function Donation ($resource, DateUtils) {
        var resourceUrl =  'api/donations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.donatedDate = DateUtils.convertDateTimeFromServer(data.donatedDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
