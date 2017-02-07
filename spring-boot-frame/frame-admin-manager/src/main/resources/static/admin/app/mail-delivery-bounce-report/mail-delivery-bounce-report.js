'use strict'

angular.module('app')

.controller('RecipientBounceSummaryReportUrl',function($scope, $rootScope, $routeParams, $http, $location, optionUrl,$cookies, ErrorsService) {

			$cookies.put('currentPath', 'mail-channel-report');
			$scope.setMenuActive();
			// 初始化table参数
			$scope.currentPage = 1;
			$scope.maxSize = 6;
			$scope.itemsPerPage = 10;
			$scope.dt = {
				startDate : null,
				endDate : null
			}

			function getData() { // 从后台获取数据

				var starDate = $scope.dt.startDate;
				var endDate = $scope.dt.endDate;

				if ($rootScope.startDate4 != null && starDate == null) {
					starDate = $rootScope.startDate4;
				}

				if ($rootScope.endDate4 != null && endDate == null) {
					starDate = $rootScope.endDate4;
				}

				var starDate2 = (!starDate) ? new Date().format('yyyy-MM-ddT00:00:00.000Z') : starDate.format('yyyy-MM-ddT00:00:00.000Z');
				var endDate2 = (!endDate) ? new Date().format('yyyy-MM-ddT23:59:59.000Z') : endDate.format('yyyy-MM-ddT23:59:59.000Z');

				$scope.loadDivVisible = true;
				$http.get(optionUrl.recipientBounceSummaryReportUrl, {
					withCredentials : true,
					params : {
						"start" : starDate2,
						"end" : endDate2
					}
				}).success(function(data) { // 成功从后台获取数据

					if (data.length == 0) {
						alert("暂无当天日期的数据，请查询其他日期");
					}
					$scope.loadDivVisible = false;
					$scope.data = data;
					$scope.totalItems = data.length;
					$scope.currentPageNum = 1;
					$scope.reports = [];
					for (var i = 0; i < data.length; i++) {
						var sdd = data[i];
						$scope.reports.push(sdd);
						$scope.i = i;
						if (i == 9) {
							$scope.currentPageNum = 1;
							return;
						}
					}

				}).error(function() {
					$scope.openAlert('danger', '网络异常，请稍后再试或者联系管理员', 5000);
					$scope.loadDivVisible = false;
				});
			}

			$scope.pageChanged = function() { // 单击页码按钮实现数据动态变化
				getData();
			};

			$scope.search = function() {

				var starDate = $scope.dt.startDate;
				var endDate = $scope.dt.endDate;

				var starDate3 = (!starDate) ? starDate : starDate.format('yyyyMMdd');
				var endDate3 = (!endDate) ? endDate : endDate.format('yyyyMMdd');
				
				if (starDate3 > endDate3) {
					alert("起始日期不能大于结束日期");
					return;
				}
				getData();
			};

			$scope.pageOptionsData = {
				availableOptions : [ {
					name : '10',
					value : '10'
				}, {
					name : '30',
					value : '30'
				}, {
					name : '50',
					value : '50'
				}, {
					name : '80',
					value : '80'
				}, {
					name : '100',
					value : '100'
				} ],
				selectedOption : {
					name : '10',
					value : '10'
				}
			// This sets the default value of the select in the ui
			};

			if (!$rootScope.editModel) { // 在更新页面刷新的时候可以根据id重新找到服务器的数据，否则所有数据都清空
				// console.log('你刷新了或者第一次进入channels页面，呵呵');
				var id = $routeParams.id;
				if (id) {
					$scope.getById(id);
				} else {
					getData(); // 初始化list数据
				}
			} else {
				if ($location.path().indexOf('edit') < 0)
					getData(); // 初始化list数据
			}

			$scope.gotoBounceRecord = function(recipientsMailSuffix) {
				$rootScope.recipientsMailSuffix = recipientsMailSuffix;
				$rootScope.startDate = $scope.dt.startDate;
				$rootScope.endDate = $scope.dt.endDate;

				if ($scope.dt.startDate == null) {
					$rootScope.startDate = $rootScope.startDate4;
				}

				if ($scope.dt.endDate == null) {
					$rootScope.endDate = $rootScope.endDate4;
				}

				$location.url('mail-delivery-bounce-report/bounce');
			}

			$scope.gotoPageUpAndDown = function(upAndDown) {

				var data = $scope.data;
				var k = 0;
				if (upAndDown == 'down') {

					if ($scope.i != 0) {
						k = $scope.i + 1;
					} else {
						k = 10;
					}
					if (k >= data.length) {
						alert("已经无数据");
						return;
					}

					$scope.currentPageNum = $scope.currentPageNum + 1;

					$scope.reports = []
					for (var i = k; i < data.length; i++) {
						var sdd = data[i];
						$scope.reports.push(sdd);

						if (i == data.length - 1) {
							$scope.d = i;
							$scope.i = i;
							return;
						}

						if (i == k + 9) {
							$scope.d = i;
							$scope.i = i;
							return;
						}
					}
				}
				if (upAndDown == 'up') {

					var pageSize = 10;
					var index = $scope.d;
					var pageIndex = 0;

					if (index == data.length - 1) {
						pageIndex = index - index % 10 - 19;
					} else {
						pageIndex = index - 19;
					}

					if (pageIndex < 0) {
						alert("已经无数据");
						return;
					}

					$scope.currentPageNum = $scope.currentPageNum - 1;

					$scope.reports = []
					for (var i = pageIndex; i < data.length; i++) {
						var sdd = data[i];
						$scope.reports.push(sdd);

						if (i == pageIndex + 9) {
							$scope.i = i;
							$scope.d = i;
							return;
						}
					}

				}

			}

		});