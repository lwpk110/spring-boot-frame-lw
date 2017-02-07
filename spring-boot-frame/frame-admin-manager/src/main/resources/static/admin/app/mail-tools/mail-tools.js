'use strict'

angular.module('app')

    .controller('MailToolsCtrl', function ($scope, $http, $window, $cookies, optionUrl) {
       $scope.actions={
           update:function(){
               $http.get(optionUrl.executetimingtask, {
                   withCredentials: true,
                   params: {
                   }
               }).success(function () { // 成功从后台获取数据
                   $scope.openAlert('success', '操作成功，后台处理中...', 5000);
               }).error(function () {
                   $scope.openAlert('danger', '网络异常，请稍后再试或者联系管理员', 5000);
               });
           }
       }

    });



