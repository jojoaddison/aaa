(function() {
    'use strict';

    angular
        .module('agreenApp')
        .controller('PageViewerController', PageViewerController);

    PageViewerController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'tmhDynamicLocale', '$sce', 'PageService'];

    function PageViewerController($scope, $stateParams, $uibModalInstance, entity, tmhDynamicLocale, $sce, PageService){
        var vm = this;
        vm.clear = clear;
        vm.page = vm.page | {};
        vm.data = null;
        $scope.pid = $stateParams.pid;


        openLang();


        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function openLang() {
            console.log($stateParams);
            console.log("scope.pid >>:" + $scope.pid);
            PageService.getByPid($scope.pid).then(function(result){
                vm.data = result.data;
                console.log("PageService>>:");
                console.log(vm.data);
               // openLang();
                var lang = tmhDynamicLocale.get('NG_TRANSLATE_LANG_KEY');
                console.log(lang);
                console.log(vm.data);
                var page = null;

                angular.forEach(vm.data, function (p) {
                    if (p.lang == lang) {
                        page = p;
                    }
                });

                if(page == null){
                    for(var i in vm.data){
                        var p = vm.data[i];
                        if(p.lang == lang){
                            page = p;
                        }
                    }
                }

                if(page){
                    vm.page = page;
                    vm.page.content = $sce.trustAsHtml(page.content);
                    console.log(vm.page);
                }else{
                    console.log("Entity>>:");
                    console.log(entity);
                    console.log("VM.Data>>:");
                    console.log(vm.data);
                }
            });

        }



    }

})();
