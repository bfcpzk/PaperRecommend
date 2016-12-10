<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Paper Recommend of bfcpzk</title>
    <script src="js/jquery-1.10.2.js"></script>
    <script src="js/jquery-1.11.1.min.js"></script>
    <script src="js/mainpage.js"></script>
    <link type="text/css" rel="stylesheet" href="css/bootstrap.min.css" />
    <style type="text/css">
        .result_hide {display: none;  position:fixed;  top: 10%; left: 25%;  width: 50%; height: 60%; z-index:1005;  overflow: auto; overflow-y:scroll;}
    </style>
</head>
<body>
    <div class="container">
        <input id="iter" type="hidden" value="${iter}">
        <div class="row">
            <div class="col-md-6">
                <h3>Research Paper Recommendation</h3>
            </div>
            <div class="col-md-4">
                <h5 style="float: right">这是第${iter}轮打分</h5>
            </div>
        </div>
        <hr>
        <div id="content" class="row set" style="overflow-y:scroll; margin-top:35px">
            <table class="table table-striped table-bordered table-hover">
                <thead>
                    <tr>
                        <th width="10%">ID</th>
                        <th width="32%">标题</th>
                        <th width="32%">期刊名称</th>
                        <th width="13%">引用数</th>
                        <th width="13%">发表时间</th>
                    </tr>
                </thead>
                <tbody id="tbody_content">
                    <c:forEach items="${paperList}" var="item">
                        <tr>
                            <td id="id_${item.item_ut}">${item.paper_id}</td>
                            <td id="title_${item.item_ut}">${item.article_title}</td>
                            <td id="journal_${item.item_ut}">${item.full_source_title}</td>
                            <td id="cite_${item.item_ut}">${item.cited_count}</td>
                            <td id="time_${item.item_ut}">${item.publication_year}.${item.publication_date}</td>
                        </tr>
                        <tr>
                            <td>文章摘要:</td>
                            <td id="abstract_${item.item_ut}" colspan="4">${item.abs}</td>
                        </tr>
                        <tr style="margin-bottom:5px;">
                            <td>用户打分:</td>
                            <td id="action_${item.item_ut}" colspan="2">
                                <a style="margin-left: 40px" id="button_${item.item_ut}_1" href="javascript:void(0)" onclick="chooseRelativity('${item.item_ut}',-1.0)" class="btn btn-danger">完全不相关</a>
                                <a style="margin-left: 35px" id="button_${item.item_ut}_2" href="javascript:void(0)" onclick="chooseRelativity('${item.item_ut}',-0.5)" class="btn btn-warning">不太相关</a>
                                <a style="margin-left: 35px" id="button_${item.item_ut}_3" href="javascript:void(0)" onclick="chooseRelativity('${item.item_ut}',0.0)" class="btn btn-info">说不清</a>
                                <a style="margin-left: 35px" id="button_${item.item_ut}_4" href="javascript:void(0)" onclick="chooseRelativity('${item.item_ut}',0.5)" class="btn btn-warning">有点相关</a>
                                <a style="margin-left: 35px" id="button_${item.item_ut}_5" href="javascript:void(0)" onclick="chooseRelativity('${item.item_ut}',1.0)" class="btn btn-success">很相关</a>
                            </td>
                            <td>标注结果:</td>
                            <td id="res_${item.item_ut}"></td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td colspan="5" style="text-align: center;">
                            <a id="submit" href="javascript:void(0)" onclick="submitTogether()" class="btn btn-success">提交</a>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
    <div id="rec_res" class="panel panel-success result_hide">
        <div class="panel-heading">
            <h4>您可能会感兴趣的文章</h4>
        </div>
        <div class="panel-body">
            <table class="table table-striped table-bordered table-hover">
                <thead>
                    <tr>
                        <th width="20%">ID</th>
                        <th width="80%">文章题目</th>
                    </tr>
                </thead>
                <tbody id="t_body">

                </tbody>
            </table>
        </div>
    </div>
</body>
</html>