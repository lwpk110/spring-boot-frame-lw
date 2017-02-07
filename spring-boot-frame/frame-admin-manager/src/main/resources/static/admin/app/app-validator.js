'use strict'

angular.module('app')

.directive("json", function() {
    return {
        restrict: "A",
        require: "ngModel",
        link: function(scope, element, attributes, ngModel) {
            
        	ngModel.$validators.json = function(modelValue) {  
            	if (!modelValue) {
            		return true;
            	}
            	try {
            		var isEmpty = function (obj) { // empty object check
            		    for(var prop in obj) {
            		        if(obj.hasOwnProperty(prop))
            		            return false;
            		    }
            		    return true;
            		};
            		
            		if (modelValue.indexOf('{') > -1 && modelValue.indexOf('}') > -1) {
            			if (isEmpty(JSON.parse(modelValue))) {
            				return false;
            			}
            			return true;
            		} else {
            			return false;
            		}
            	} catch (e) {
            		return false;
            	}
            	return true;
            }
        	
        }
    };
})

.directive("dateTime", function() {
    return {
        restrict: "A",
        require: "ngModel",
        link: function(scope, element, attributes, ngModel) {
            
        	ngModel.$validators.dateTime = function(modelValue) {  
            	if (!modelValue) {
            		return true;
            	}
            	
            	var formats = ['dd-MM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'yyyy-MM-dd'];
            	
            	function formatDate(f) {
            		var dt = new Date(modelValue);
            		var y = dt.getFullYear();
            		var m = dt.getMonth() + 1 + '';
            		if (m.indexOf('月') > 0) {
            			m = formatMonth(m);
            		}
            		var d = dt.getDate();
            		switch (f) {
            		case formats[0]:
            			return d + '-' + m + '-' + y;
            		case formats[1]:
            			return y + '/' + m + '/' + d;
            		case formats[2]:
            			return d + '.' + m + '.' + y;
            		case formats[3]:
            			return y + '-' + m + '-' + d;
            		}
            	}
            	
            	function formatMonth(m) {
            		switch (m) {
					case '一月':
						return '01';
					case '二月':
						return '02';
					case '三月':
						return '03';
					case '四月':
						return '04';
					case '五月':
						return '05';
					case '六月':
						return '06';
					case '七月':
						return '07';
					case '八月':
						return '08';
					case '九月':
						return '09';
					case '十月':
						return '10';
					case '十一月':
						return '11';
					case '十二月':
						return '12';
					}
            	}
            	
            	// (0?[1-9]|[12][0-9]|3[01]) date
            	// \d{4}\ year
            	// (0?[1-9]|1[012]) month
            	
            	var res1, res2, res3, res4;
            	res1 = /^(0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|1[012])\-\d{4}\$/.test(formatDate(formats[0]));
            	res2 = /^\d{4}\\\/(0?[1-9]|1[012])\\\/(0?[1-9]|[12][0-9]|3[01])$/.test(formatDate(formats[1]));
            	res3 = /^(0?[1-9]|1[012]).(0?[1-9]|1[012])\.\d{4}\$/.test(formatDate(formats[2]));
            	res4 = /^\d{4}\-(0?[1-9]|1[012])\-(0?[1-9]|[12][0-9]|3[01])$/.test(formatDate(formats[3]));
            	
            	return res1 || res2 || res3 || res4;
            			
            }
        	
        }
    };
})

.directive("placeholder", function($log, $timeout, $document) {
	return {
	    restrict: 'A',
	    require: 'ngModel',
	    link: function(scope, element, attr, ctrl) {      
	      
	    	function hasPlaceholder () {
	    		// test for native placeholder support
	    		var test = $document[0].createElement('input');
	    		return (test.placeholder !== void 0);
	    	}
	    	
	    	if (hasPlaceholder()) {
	    		return true;
	    	}
	    	
	    	element.attr('title', attr.placeholder);
	    }
	};
});