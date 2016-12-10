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
</head>
<body>
    <div class="container">
        <div class="row">
            <div class="col-md-6">
                <h3>Research Paper Recommendation</h3>
            </div>
        </div>
        <hr>
        <div id="content" class="row" style="overflow-y:scroll; margin-top:35px">
            <table class="table table-striped table-bordered table-hover">
                <thead>
                    <tr>
                        <th colspan="${rowSize}" width="100%"><h4>选出你感兴趣的关键词</h4></th>
                    </tr>
                </thead>
                <tbody id="tbody_content">
                    <c:forEach items="${klist}" var="keyword">
                        <tr>
                            <c:forEach items="${keyword.klist}" var="item">
                                <td id="${item.kid}">
                                    <a id="button_${item.kid}" href="javascript:void(0)" onclick="chooseKeywords(${item.kid},'${item.keywords}')" class="btn btn-danger">${item.keywords}</a>
                                </td>
                            </c:forEach>
                        </tr>
                    </c:forEach>
                <tr>
                    <td colspan="7" style="text-align: center;">
                        <a id="submit" href="javascript:void(0)" onclick="submitKeywords()" class="btn btn-info">提交</a>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>