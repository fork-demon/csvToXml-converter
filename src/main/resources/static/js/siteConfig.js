var myApp = angular.module('myApp', []);
myApp.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;

            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);

myApp.service('fileUpload', ['$http', function ($http,$scope) {
    this.uploadFileToUrl = function(file, uploadUrl){
        var fd = new FormData();
        fd.append('file', file);
        return $http.post(uploadUrl, fd, {
            withCredentials : false,
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
        .success(function(data,status){
            console.log("success 1");
            $scope.messages = data || "Successful";
        })
        .error(function(data,status){
        	//$scope.messages = data || "Request failed";
            //$scope.status = status;
            console.log('error');
            console.log(data);
            console.log('message'+data.message)
            console.log(status);
        });
    }
}]);

/*error(function(data, status) {
    $scope.messages = data || "Request failed";
    $scope.status = status;
}); */ 


myApp.controller('SiteFileUploadCtrl', ['$scope', 'fileUpload',  
 function($scope, fileUpload){

    $scope.uploadFile = function(){
        //alert("hi");
        console.log("hello");
        var file = $scope.myFile;
        console.log('file is');
        console.dir(file);
       // var uploadUrl = '/Test/login/api/userfile/upload';
        var uploadUrl = '/api/userfiles/upload';
        fileUpload.uploadFileToUrl(file, uploadUrl).success(function(data) { 
        	console.log(data);
            alert('saved successfully!!!'); 
        }).error(function(data){
        	console.log("-----------------------")
        	console.log(data.detail)
            alert(data.detail);
        });
    };

}]);