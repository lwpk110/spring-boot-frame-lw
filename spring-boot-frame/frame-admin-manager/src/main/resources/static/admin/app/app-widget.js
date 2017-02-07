'use strict'

angular.module('app')

.directive('widget.remove', function() {
	return {
		restrict: 'ACEM',
		template: '<a href="javascript:void(0);" title="Remove" class="btn-borderless btn-remove"><i class="fa fa-times"></i></a>',
		replace: true,
		link: function (scope, el, attr) {
			el.click(function(e){
				e.preventDefault();
				$(this).parents('.widget').fadeOut(300, function(){
					$(this).remove();
				});
			});
		}
	}
})

.directive('widget.expand', function() {
	return {
		restrict: 'ACEM',
		template: '<a href="javascript:void(0);" title="Expand/Collapse" class="btn-borderless btn-toggle-expand"><i class="fa fa-chevron-up"></i></a>',
		replace: true,
		link: function(scope, el, attr) {
			el.clickToggle(
				function(e) {
					e.preventDefault();
					$(this).parents('.widget').find('.widget-content').slideUp(300);
					$(this).find('i.fa-chevron-up').toggleClass('fa-chevron-down');
				},
				function(e) {
					e.preventDefault();
					$(this).parents('.widget').find('.widget-content').slideDown(300);
					$(this).find('i.fa-chevron-up').toggleClass('fa-chevron-down');
				}
			);
		}
	}
})

.directive('widget.focus', function() {
	return {
		restrict: 'ACEM',
		template: '<a href="javascript:void(0);" id="tour-focus" title="Focus" class="btn-borderless btn-focus"><i class="fa fa-eye"></i></a>',
		replace: true,
		link: function(scope, el, attr) {
			el.clickToggle(
				function(e) {
					e.preventDefault();
					$(this).find('i.fa-eye').toggleClass('fa-eye-slash');
					$(this).parents('.widget').find('.btn-remove').addClass('link-disabled');
					$(this).parents('.widget').addClass('widget-focus-enabled');
					$('<div id="focus-overlay"></div>').hide().appendTo('body').fadeIn(300);
				},
				function(e) {
					e.preventDefault();
					var theWidget = $(this).parents('.widget');
					
					$(this).find('i.fa-eye').toggleClass('fa-eye-slash');
					theWidget.find('.btn-remove').removeClass('link-disabled');
					$('body').find('#focus-overlay').fadeOut(function(){
						$(this).remove();
						theWidget.removeClass('widget-focus-enabled');
					});
				}
			);
		}
	}
})

.directive('loading', function () { //旋转
    return {
        restrict: 'A',
        link: function (scope, el, attr) {
            var n = 50;
            setInterval(function() {
            	var r = 'rotate(' + n + 'deg)';
                el.css({
                    '-moz-transform': r,
                    '-webkit-transform': r,
                    '-o-transform': r,
                    '-ms-transform': r,
                    '-ms-filter': r

                });
                n += 50;
            }, 100, 50, true);
        }
    }
})

.controller('DatePickerCtrl', function ($scope) { // 未使用
    
    // Disable weekend selection
    $scope.disabled = function (date, mode) {
        return (mode === 'day' && (date.getDay() === 0 || date.getDay() === 6));
    };

    $scope.open = function ($event) {
        $scope.status.opened = true;
    };

    $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'yyyy-MM-dd'];
    $scope.format = $scope.formats[4];
    $scope.format1 = $scope.formats[0];
    $scope.format2 = $scope.formats[1];
    $scope.format3 = $scope.formats[2];
    $scope.format4 = $scope.formats[3];
    $scope.format5 = $scope.formats[4];

    $scope.status = {
        opened: false
    };

    // default text
    $scope.clearText = '清除';
    $scope.currentText = '今天';
    $scope.closeText = '关闭';
});