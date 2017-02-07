'use strict'

angular.module('app')

    .controller('MailDeliveryTemplateApproveCtrl', function ($scope, $http, $uibModal, $window, $cookies, optionUrl) {
        $cookies.put('currentPath', 'mail-template-approve');
        $scope.setMenuActive();
        $scope.currentPage = 1;
        $scope.maxSize = 6;
        $scope.itemsPerPage = 10;
        $scope.isHaveTemplate = false;

        $scope.reportShow = function (item) {
            return (!item.scheduled && item.deliveryStatus==100) || (item.scheduled && item.scheduledDate < Date.now());
        }

        $scope.clockShow = function (item) {
            return item.scheduled && item.scheduledDate > Date.now();
        }


        function getData() { // 从后台获取数据
        	$scope.loadDivVisible = true;
            $http.get(optionUrl.mailTemplateApproveUrl, {
                withCredentials: true,
                params: {
                    page: $scope.currentPage - 1,
                }
            }).success(function (data) { // 成功从后台获取数据
                $scope.totalItems = data.totalElements;
                $scope.items = data.content;
                if(data.content.length > 0)
                {
                    $scope.isHaveTemplate = true;
                }else{
                    $scope.isHaveTemplate = false;
                }
                $scope.itemDetail = $scope.items[0];
                $scope.loadDivVisible = false;
            }).error(function () {
                $scope.openAlert('danger', '网络异常，请稍后再试或者联系管理员', 5000);
                $scope.loadDivVisible = false;
            });
        }

        $scope.pageChanged = function () { // 单击页码按钮实现数据动态变化
            getData();
        };

        $scope.search = function () {
            getData();
        };

        if (!$scope.items) {
            getData();
        }

        $scope.templateDetails = function(item){
            $scope.itemDetail = item;
            return false;
        }

        $scope.swapHtml = function(item){
            $('#templateBody').html(item);
        }

        function templateApproveCheck(id, status) {
            var url = optionUrl.mailTemplateApproveCheckUrl.replace('{id}', id);
            $http({
                method: "post",
                url: url,
                params: {
                    status: status
                }
            }).success(function () { // 成功从后台获取数据
                $scope.openAlert('success', '审核成功', 5000);
            }).error(function () {
                $scope.openAlert('danger', '网络异常，请稍后再试或者联系管理员', 5000);
                $scope.loadDivVisible = false;
            });
        }

        $scope.approveBtn = function(id){
            $scope.templateId=id;
            $scope.confirm = true;
            $scope.confirmTemplateModal = $uibModal.open({
                animation: true,
                templateUrl: 'app/mail-delivery-template-approve/mail-template-confirm.html',
                scope: $scope
            });
        }

        $scope.enterBtn = function(){
            var status = 0;
            if($scope.confirm)
                status = 1
            templateApproveCheck($scope.templateId, status);
            $scope.confirmTemplateModal.close();
            location.reload();

        }

        $scope.closeConfirmView = function(){
            $scope.confirmTemplateModal.close();
        }

        $scope.close = function(){
            $scope.confirmTemplateModal.close();
        }


        $scope.checkFail = function(id){
            $scope.templateId=id;
            $scope.confirm = false;
            $scope.confirmTemplateModal = $uibModal.open({
                animation: true,
                templateUrl: 'app/mail-delivery-template-approve/mail-template-confirm.html',
                scope: $scope
            });
        }

        $("#data-item").on('click', 'tr', function(e){
            var $this = $(e.target).closest('tr');
            $this.children('td').css({'background':'darkgrey'});
            $this.siblings('tr').children('td').css('background-color','#fff');

        })
    });



