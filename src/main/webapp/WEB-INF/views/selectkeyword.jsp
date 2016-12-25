<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Paper Recommend of bfcpzk</title>
    <script src="js/jquery-1.11.1.min.js"></script>
    <script src="js/mainpage.js"></script>
    <link type="text/css" rel="stylesheet" href="css/bootstrap.min.css" />
    <style type="text/css">
        .warn_info{display: none;  position:fixed;  top: 10%; left: 35%;  width: 30%; height: 13%; padding: 16px;  border: 8px solid orange;  background-color: white;  z-index:1005;  overflow: auto; overflow-y:scroll;}
        .result_hide {display: none;  position:fixed;  top: 10%; left: 25%;  width: 50%; height: 60%; z-index:1005;  overflow: auto; overflow-y:scroll;}
    </style>
</head>
<body>
    <div class="container">
        <div class="row">
            <div class="col-md-6">
                <h3>Research Paper Recommendation</h3>
            </div>
        </div>
        <hr>
        <div class="row">
            <div class="col-lg-5">
                <div id="rec_res" class="panel panel-success">
                    <div class="panel-heading">
                        <h4>关键词排行榜(挑选感兴趣的主题)</h4>
                    </div>
                    <div class="panel-body" style="overflow-y:auto; height: 400px;">
                        <table class="table table-striped table-bordered table-hover">
                            <thead>
                            <tr>
                                <th width="70%">关键词</th>
                                <th width="30%">出现次数</th>
                            </tr>
                            </thead>
                            <tbody id="t_body">
                                <c:forEach items="${wcList}" var="item">
                                    <tr>
                                        <td id="${item.kid}">
                                            <a id="button_${item.kid}" href="javascript:void(0)" onclick="chooseKeywords(${item.kid},'${item.keywords}')" class="btn btn-danger">${item.keywords}</a>
                                        </td>
                                        <td>${item.wordcount}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="col-lg-6">
                <div class="row">
                    <div class="panel panel-info">
                        <div class="panel-heading">
                            <h4>文献阅读偏好调查</h4>
                        </div>
                        <div class="panel-body">
                            <table class="table table-striped table-bordered table-hover">
                                <tbody>
                                    <tr>
                                       <td colspan="2">
                                           <h4>1.对于文献时间的年份要求如何？(重要等级)</h4>
                                       </td>
                                    </tr>
                                    <tr>
                                        <td width="75%">
                                            <a style="margin-left: 2px" href="javascript:void(0)" onclick="chooseTime('1级', '-0.1')" class="btn btn-danger">1级</a>
                                            <a style="margin-left: 5px" href="javascript:void(0)" onclick="chooseTime('2级', '-0.05')" class="btn btn-warning">2级</a>
                                            <a style="margin-left: 5px" href="javascript:void(0)" onclick="chooseTime('3级', '0.0')" class="btn btn-info">3级</a>
                                            <a style="margin-left: 5px" href="javascript:void(0)" onclick="chooseTime('4级', '0.05')" class="btn btn-warning">4级</a>
                                            <a style="margin-left: 5px" href="javascript:void(0)" onclick="chooseTime('5级', '0.1')" class="btn btn-danger">5级</a>
                                        </td>
                                        <input type="hidden" id="timeLevel" value="">
                                        <td id="timeRes">

                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">
                                            <h4>2.对于文献篇幅长短要求如何？(重要等级)</h4>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td width="75%">
                                            <a style="margin-left: 2px" href="javascript:void(0)" onclick="chooseLength('1级', '-0.1')" class="btn btn-danger">1级</a>
                                            <a style="margin-left: 5px" href="javascript:void(0)" onclick="chooseLength('2级', '-0.05')" class="btn btn-warning">2级</a>
                                            <a style="margin-left: 5px" href="javascript:void(0)" onclick="chooseLength('3级', '0.0')" class="btn btn-info">3级</a>
                                            <a style="margin-left: 5px" href="javascript:void(0)" onclick="chooseLength('4级', '0.05')" class="btn btn-warning">4级</a>
                                            <a style="margin-left: 5px" href="javascript:void(0)" onclick="chooseLength('5级', '0.1')" class="btn btn-danger">5级</a>
                                        </td>
                                        <input type="hidden" id="lengthLevel" value="">
                                        <td id="lengthRes">

                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">
                                            <h4>3.对于文献的影响力要求如何？(重要等级)</h4>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td width="75%">
                                            <a style="margin-left: 2px" href="javascript:void(0)" onclick="chooseCite('1级', '-0.1')" class="btn btn-danger">1级</a>
                                            <a style="margin-left: 5px" href="javascript:void(0)" onclick="chooseCite('2级', '-0.05')" class="btn btn-warning">2级</a>
                                            <a style="margin-left: 5px" href="javascript:void(0)" onclick="chooseCite('3级', '0.0')" class="btn btn-info">3级</a>
                                            <a style="margin-left: 5px" href="javascript:void(0)" onclick="chooseCite('4级', '0.05')" class="btn btn-warning">4级</a>
                                            <a style="margin-left: 5px" href="javascript:void(0)" onclick="chooseCite('5级', '0.1')" class="btn btn-danger">5级</a>
                                        </td>
                                        <input type="hidden" id="citeLevel" value="">
                                        <td id="citeRes">

                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="panel-footer" style="text-align: center">
                            <a style="text-align: center" id="submit" href="javascript:void(0)" onclick="submitKeywords()" class="btn btn-info">提交</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <hr>
        <div id="warn_info" class="panel-body warn_info" style='text-align:center;'>

        </div>
        <div style="text-align: center; margin-bottom: 15px;">
            copyright @ zhaokang Pan & fangyu Liu
        </div>
    </div>
</body>
</html>