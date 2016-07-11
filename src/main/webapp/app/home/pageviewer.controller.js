(function() {
    'use strict';

    angular
        .module('agreenApp')
        .controller('PageViewerController', PageViewerController);

    PageViewerController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'tmhDynamicLocale', '$sce'];

    function PageViewerController($scope, $stateParams, $uibModalInstance, entity, tmhDynamicLocale, $sce){
        var vm = this;
        vm.clear = clear;
        vm.page = {};


        openLang();


        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function openLang() {
            var lang = tmhDynamicLocale.get('NG_TRANSLATE_LANG_KEY');
            console.log(lang);
            console.log(entity);
            var page = null;

            angular.forEach(entity, function (p) {
                if (p.lang == lang) {
                    page = p;
                }
            });

            if(page == null){
                for(var i in entity){
                    var p = entity[i];
                    if(p.lang == lang){
                        page = p;
                    }
                }
            }

            if(page){
                vm.page = page;
                /**
                angular.forEach(page, function(v,k){
                    console.log("k: " + k);
                    if(k != 'createdDate' && k != 'modifiedDate' && k != 'id' && k != 'link'){
                        vm.page[k] = $sce.trustAsHtml(v);
                        console.log("v: " + v);
                    }else{
                        vm.page[k] = v;
                    }
                });
                */
                vm.page.content = $sce.trustAsHtml(page.content);
                console.log(vm.page);
            }
        }


    }

})();
