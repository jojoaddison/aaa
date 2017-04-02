(function() {
    'use strict';

    angular
        .module('agreenApp')
        .controller('FileDocumentController', FileDocumentController);

    FileDocumentController.$inject = ['$scope', '$state', 'FileDocument'];

    function FileDocumentController ($scope, $state, FileDocument) {
        var vm = this;
        
        vm.fileDocuments = [];

        loadAll();

        function loadAll() {
            FileDocument.query(function(result) {
                vm.fileDocuments = result;
            });
        }
    }
})();
