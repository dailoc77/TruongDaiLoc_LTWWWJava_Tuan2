<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<form action="upload" method="post" enctype="multipart/form-data">
    Select files to upload: <br>
    <input type="file" name="file" /> <br>
    <input type="file" name="file" /> <br>
    <input type="file" name="file" /> <br>
    <input type="file" name="file" /> <br>
    <input type="file" name="file" /> <br>
    <input type="submit" value="Upload" />
</form>
</body>
</html>