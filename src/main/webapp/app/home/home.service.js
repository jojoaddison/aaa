(function() {
    'use strict';
angular.module('agreenApp')
    .factory('Global', Global)
    .factory('Slider', Slider);
    Global.$inject = ['$http'];
    Slider.$inject = ['$http'];
    function Global($http){
        return{
            get: function(lang){
                var path = "i18n/" + lang + "/global.json";
                return $http.get(path);
            }
        }
    }
    function Slider($http){
        return{
            get: function(){
                var path = "app/home/slider.html";
                return $http.get(path);
            },
            getData: function(path){
                return $http.get(path);
            }
        }
    }

})();

