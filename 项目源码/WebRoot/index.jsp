<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
<title>108java小区物业管理系统-首页</title>
<link href="<%=basePath %>css/index.css" rel="stylesheet" type="text/css" />
 </head>
<body>
<script>location.href="login.jsp";</script>
<div id="container">
	<div id="banner"><img src="<%=basePath %>images/logo.gif" /></div>
	<div id="globallink">
		<ul>
			<li><a href="<%=basePath %>index.jsp">首页</a></li>
			<li><a href="<%=basePath %>Building/Building_FrontQueryBuilding.action" target="OfficeMain">楼栋</a></li> 
			<li><a href="<%=basePath %>Employee/Employee_FrontQueryEmployee.action" target="OfficeMain">员工</a></li> 
			<li><a href="<%=basePath %>Owner/Owner_FrontQueryOwner.action" target="OfficeMain">业主</a></li> 
			<li><a href="<%=basePath %>Parking/Parking_FrontQueryParking.action" target="OfficeMain">停车位</a></li> 
			<li><a href="<%=basePath %>Repair/Repair_FrontQueryRepair.action" target="OfficeMain">报修</a></li> 
			<li><a href="<%=basePath %>FeeType/FeeType_FrontQueryFeeType.action" target="OfficeMain">费用类别</a></li> 
			<li><a href="<%=basePath %>Fee/Fee_FrontQueryFee.action" target="OfficeMain">收费</a></li> 
			<li><a href="<%=basePath %>Facility/Facility_FrontQueryFacility.action" target="OfficeMain">设施</a></li> 
			<li><a href="<%=basePath %>Salary/Salary_FrontQuerySalary.action" target="OfficeMain">工资</a></li> 
			<li><a href="<%=basePath %>LeaveWord/LeaveWord_FrontQueryLeaveWord.action" target="OfficeMain">留言投诉</a></li> 
			<li><a href="<%=basePath %>Manager/Manager_FrontQueryManager.action" target="OfficeMain">管理员</a></li> 
		</ul>
		<br />
	</div> 

	<div id="loginBar">
	  <%
	  	String user_name = (String)session.getAttribute("user_name");
	    if(user_name==null){
	  %>
	  <form action="<%=basePath %>login/login_CheckFrontLogin.action" style="height:25px;margin:0px 0px 2px 0px;" method="post">
		用户名：<input type=text name="userName" size="12"/>&nbsp;&nbsp;
		密码：<input type=password name="password" size="12"/>&nbsp;&nbsp;
		<input type=submit value="登录" />
	  </form>
	  <%} else { %>
	    <ul>
	    	<li><a href="<%=basePath %>UserInfo/UserInfo_SelfModifyUserInfoQuery.action?user_name=<%=user_name %>" target="OfficeMain">【修改个人信息】</a></li>
	    	<li><a href="<%=basePath %>login/login_LoginOut.action">【退出登陆】</a></li>
	    </ul>
	 <% } %>
	</div> 

	<div id="main">
	 <iframe id="frame1" src="<%=basePath %>desk.jsp" name="OfficeMain" width="100%" height="100%" scrolling="yes" marginwidth=0 marginheight=0 frameborder=0 vspace=0 hspace=0 >
	 </iframe>
	</div>
	<div id="footer">
		<p>&nbsp;&nbsp;<a href="<%=basePath%>login.jsp"><font color=red>后台登陆</font></a></p>
	</div>
</div>
</body>
</html>
