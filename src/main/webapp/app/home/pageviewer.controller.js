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

        var count = 0;


        openLang();


        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function openLang() {
            console.log($stateParams);
            console.log("scope.pid >>:" + $scope.pid);

            if(count < 2){
                count++;
                if(entity && entity.length == 0){
                    console.log("empty entity: " + entity);
                    if(!isEmpty($stateParams)){
                        PageService.getByPid($scope.pid).then(function(result){
                            vm.data = result.data;
                            console.log("PageService>>:");
                            console.log(vm.data);
                            openLang();
                        });
                    }
                }
            }
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
            }else{
                console.log("Entity>>:");
                console.log(entity);
                console.log("VM.Data>>:");
                console.log(vm.data);
            }
        }

        function isEmpty(item){
            return (item.hasOwnProperty() == null);
        }


    }

})();
