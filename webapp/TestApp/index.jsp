<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login via custom auth protocol</title>
</head>
  <body>
    <h2>Single sign-on with custom protocol</h2>
      <form action="https://localhost:9443/identity">
        <input type="hidden" name="sp_id" value="custom-inbound">
        <input type="hidden" name="CustomAuthRequest" value="true">
        <input type="submit" value="Login">
      </form>
  </body>
</html>
